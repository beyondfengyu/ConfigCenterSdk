package com.cus.wob.config.example;

import com.cus.wob.config.listener.ConfigListener;
import com.cus.wob.config.common.model.ConfigInfo;
import com.cus.wob.config.common.model.ConfigLog;
import com.cus.wob.config.service.ConfigCenterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/23
 */
public class DemoService {
    private static final Logger logger = LoggerFactory.getLogger(DemoService.class);
    private static boolean isSubs = false;
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        //1 先创建Service对象
        final ConfigCenterService centerService = new ConfigCenterService("1.0", "javasdk", "", new ConfigListener() {
            @Override
            public void onRegister(boolean isPass) {

            }

            @Override
            public void onSubscribe(boolean isSubs, List<ConfigInfo> infoList) {
                DemoService.isSubs = isSubs;
                countDownLatch.countDown();
            }

            @Override
            public void onSyncLog(List<ConfigLog> logList) {

            }

            @Override
            public void onPing(long lastlsn) {

            }
        });
        //2 初始化
        centerService.init();
        //3 向配置中心注册客户端信息
        centerService.register();
        List<String> grpNames = new ArrayList<String>();
        grpNames.add("videotranscode");
        //4 订阅感兴趣的配置项
        centerService.subscribe(grpNames);
        //5 等待订阅结果
        countDownLatch.await(5, TimeUnit.SECONDS);
        List<ConfigInfo> list = centerService.getSubConfigInfos();
        for (ConfigInfo info : list) {
            logger.info(">>>>>>>>>>>>>configInfo: {}", info);
        }

    }

}
