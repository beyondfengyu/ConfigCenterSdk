package com.cus.wob.config.client;

import com.cus.wob.config.common.protocol.*;
import com.cus.wob.config.listener.ConfigListener;
import com.cus.wob.config.util.ProtoUtil;
import com.cus.wob.config.common.model.ConfigInfo;
import com.cus.wob.config.common.model.ConfigLog;
import com.cus.wob.config.common.constant.Constants;
import com.cus.wob.config.common.constant.SyncStatus;
import com.cus.wob.config.common.constant.URI;
import com.cus.wob.config.listener.DefaultConfigListener;
import com.cus.wob.config.util.ClientUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 配置中心的客户端
 *
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/19
 */
public class ConfigCenterClient implements Client {

    private static final Logger logger = LoggerFactory.getLogger(ConfigCenterClient.class);
    private static final long CONNECTION_TIMEOUT = 5000L; //连接请求超时时间
    private static final long HEARTBEAT_INTERVAL = 5000L;//心跳间隔时间10s
    private static final long MAX_RETRY_TIMEOUT = 30000L;//最大重试连接超时时间

    private volatile boolean isPass;//标识是否注册成功
    private volatile boolean isSubs;//标识是否订阅成功
    private volatile long lastlsn;  //
    private CountDownLatch countDownLatch;
    private ReentrantReadWriteLock rwLock;
    private Timer timer;    //心跳定时器
    private Balance balance;//负载均衡器

    private Bootstrap b;
    private ChannelFuture cf;
    private EventLoopGroup group;
    private DefaultEventExecutorGroup executorGroup;


    private String version;
    private String s2sName;
    private String s2sKey;
    private List<String> grpNames;
    private ConfigListener listener;

    /**
     * 全网的配置项
     */
    private ConcurrentMap<String, ConfigInfo> infoProdMap;
    /**
     * 灰度的配置项
     */
    private ConcurrentMap<String, ConfigInfo> infoTestMap;

    public ConfigCenterClient(ConfigListener listener) {
        this(Constants.DEFAULT_VERSION, Constants.DEFAULT_S2SNAME, Constants.DEFAULT_S2SKEY, listener);
    }

    public ConfigCenterClient(String version, String s2sName, String s2sKey, ConfigListener listener) {
        this.version = version;
        this.s2sName = s2sName;
        this.s2sKey = s2sKey;
        this.rwLock = new ReentrantReadWriteLock();
        this.balance = new Balance(Constants.SERVER_HOST);
        this.listener = listener;
        if (listener == null) {
            this.listener = new DefaultConfigListener();
        }
    }

    public void init() throws Exception {
        b = new Bootstrap();
        group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
        executorGroup = new DefaultEventExecutorGroup(Runtime.getRuntime().availableProcessors() * 2,
                new DefaultThreadFactory("worker group"));
        b.group(group)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new ClientHandler(this));
        boolean isSuccess = connect(balance.select(), Constants.SERVER_PORT);
        if (isSuccess) {
            sendHeartbeat();
        }
        //注册进程钩子，退出时释放资源
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (timer != null) {
                    timer.cancel();
                }
                closeSync();
            }
        }));
        logger.info("start client success:{}", isSuccess);
    }

    public void register() {
//        countDownLatch = new CountDownLatch(1);
        sendProto(ProtoUtil.buildRegistReqProto(version, s2sName, s2sKey));
    }

    public void subscribe(List<String> grpNames) {
        try {
            //阻塞等待注册完成
//            countDownLatch.await(MAX_RETRY_TIMEOUT, TimeUnit.SECONDS);
            this.grpNames = grpNames;
            this.sendProto(ProtoUtil.buildPullReqProto(grpNames));
        } catch (Exception e) {
            logger.error("CenterClient---->>>>subscribe fail");
        }
    }

    @Override
    public boolean connect(String host, int port) throws InterruptedException, TimeoutException {
        long startTime = System.currentTimeMillis();
        long tryTime = 0;
        //限定时间内重试所有IP,每次换不同的IP地址
        while (tryTime < balance.size() && System.currentTimeMillis() - startTime < MAX_RETRY_TIMEOUT) {
            try {
                logger.info("connect with host:{}, port:{}", host, port);
                cf = b.connect(host, port).sync();
                return true;
            } catch (Exception e) {
                logger.warn("connect fail,try time:{}, host:{}", tryTime + 1, host, e);
                tryTime++;
                startTime = System.currentTimeMillis();
                host = balance.select();
                //重连前等待一段时间
                TimeUnit.SECONDS.sleep(ClientUtil.randomSleepConn());
            }
        }
        throw new TimeoutException("connect fail ,retry time " + tryTime);
    }

    @Override
    public boolean reconnect() throws Exception {
        logger.info("start reconnect to server.");
        //关闭原Channel通道
        close();
        long startTime = System.currentTimeMillis();
        long tryTime = 0;
        //限定时间内重试所有IP,每次换不同的IP地址
        String curIp = balance.select();
        while (tryTime < balance.size() && System.currentTimeMillis() - startTime < MAX_RETRY_TIMEOUT) {
            try {
                logger.info("reconnect with host:{}, port:{}", curIp, Constants.SERVER_PORT);
                //重新建立异步长连接
                cf = b.connect(curIp, Constants.SERVER_PORT);
                //最多等待2秒，如连接建立成功立即返回
                cf.get(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
                break;
            } catch (Exception e) {
                logger.warn("reconnect fail,try time:{}, host:{}", tryTime + 1, curIp, e);
                tryTime++;
                startTime = System.currentTimeMillis();
                curIp = balance.select();
                //重连前等待一段时间
                TimeUnit.SECONDS.sleep(ClientUtil.randomSleepConn());
            }
        }
        //重连成功后重新注册并订阅
        if (isAlive()) {
            reconnectOpt();
        } else {
            throw new Exception("reconnect fail retry time " + tryTime);
        }
        logger.info("end reconnect to server result:" + isAlive());
        return isAlive();
    }

    @Override
    public void closeSync() {
        try {
            logger.info("closeSync--->>>sync close channel");
            cf.channel().close().sync();
            group.shutdownGracefully();
        } catch (InterruptedException e) {
            logger.error("closeSync--->>>close channel fail", e);
        }
    }

    @Override
    public void close() {
        //关闭原Channel通道
        if (isAlive()) {
            cf.channel().close();
        }
        //关闭心跳定时器
        if (timer != null) {
            timer.cancel();
        }
        isPass = false;
        isSubs = false;
    }


    @Override
    public boolean isAlive() {
        return cf != null && cf.channel().isActive();
    }

    public void sendProto(YYProto yyProto) {
        try {
            if (!isAlive()) {
                reconnect();
            }
            cf.channel().writeAndFlush(yyProto);
        } catch (Exception e) {
            logger.error("sendProto Exception error", e);
        }
    }

    /**
     * 重连后的操作
     */
    private void reconnectOpt(){
        sendHeartbeat();
        register();
        if(grpNames!=null) {
            subscribe(grpNames);
        }
    }

    public void callBack(YYProto yyProto) {
        try {
            rwLock.writeLock().lock();
            switch (yyProto.uri()) {
                case URI.CONFIG_CENTER_REGISTER_RSP:    //注册响应
                    isPass = ((RegisterRspBody) yyProto.getBody()).isPass();
//                    countDownLatch.countDown();
                    listener.onRegister(isPass());
                    break;
                case URI.PULL_CONFIG_GRPBYNAME_RSP: //拉取响应
                    PullInfoRspBody pullBody = (PullInfoRspBody) yyProto.getBody();
                    lastlsn = pullBody.getLastlsn();
                    infoProdMap = new ConcurrentHashMap<String, ConfigInfo>();
                    for (ConfigInfo info : pullBody.getInfoList()) {
                        infoProdMap.put(info.getGrpName() + "_" + info.getCkey(), info);
                        logger.info("config:{}", info.toString());
                    }
                    isSubs = true;
                    listener.onSubscribe(isSubs, pullBody.getInfoList());
                    break;
                case URI.SYNC_CONFIG_LOG_RSP:   //同步响应
                    SyncLogRspBody logBody = (SyncLogRspBody) yyProto.getBody();
                    lastlsn = logBody.getLastlsn();
                    handleSysLog(logBody.getLogs());
                    listener.onSyncLog(logBody.getLogs());
                    break;
                case URI.CLIENT_CONFIG_PING_RSP:    //PING响应
                    PingRspBody body = (PingRspBody) yyProto.getBody();
                    lastlsn = body.getLastlsn();
                    listener.onPing(lastlsn);
                    break;
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * 处理返回的同步日志
     *
     * @param logs
     */
    private void handleSysLog(List<ConfigLog> logs) {
        for (ConfigLog log : logs) {
            logger.info("configLog:{}", log.toString());
            switch (log.getOp().intValue()) {
                case SyncStatus.CONFIG_ADD:     //增加配置项
                    ConfigInfo info = ClientUtil.buildProdInfoByLog(log);
                    infoProdMap.put(ClientUtil.buildInfoKey(info), info);
                    break;
                case SyncStatus.CONFIG_UPDATE:  //更新配置项
                    ConfigInfo tmpInfo = infoProdMap.get(ClientUtil.buildInfoKey(log));
                    if (tmpInfo != null) {
                        info = ClientUtil.buildProdInfoByLog(log);
                        infoProdMap.put(ClientUtil.buildInfoKey(info), info);
                    }
                    break;
                case SyncStatus.CONFIG_ROLLBACK://回滚配置项
                    tmpInfo = infoProdMap.get(ClientUtil.buildInfoKey(log));
                    if (tmpInfo != null) {
                        info = ClientUtil.buildProdInfoByLog(log);
                        infoProdMap.put(ClientUtil.buildInfoKey(info), info);
                    }
                    break;
                case SyncStatus.CONFIG_DELETE:  //删除配置项
                    infoProdMap.remove(ClientUtil.buildInfoKey(log));
                    break;
                case SyncStatus.CONFIG_TEST_OPEN://灰度添加配置项
                    info = ClientUtil.buildTestInfoByLog(log);
                    infoTestMap.put(ClientUtil.buildInfoKey(info), info);
                    break;
                case SyncStatus.CONFIG_TEST_CLOSE://灰度删除配置项
                    infoTestMap.remove(ClientUtil.buildInfoKey(log));
                    break;
            }
        }
    }

    /**
     * 启动定时器发送心跳
     */
    private void sendHeartbeat() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                logger.info("CenterClient --->>>send ping");
                ConfigCenterClient.this.sendProto(ProtoUtil.buildPingReqProto(lastlsn));
            }
        }, 3000, HEARTBEAT_INTERVAL);
    }

    public boolean isPass() {
        return isPass;
    }

    public long getLastlsn() {
        return lastlsn;
    }

    public boolean isSubs() {
        return isSubs;
    }

    public ConcurrentMap<String, ConfigInfo> getInfoProdMap() {
        return infoProdMap;
    }

    public ConcurrentMap<String, ConfigInfo> getInfoTestMap() {
        return infoTestMap;
    }

}
