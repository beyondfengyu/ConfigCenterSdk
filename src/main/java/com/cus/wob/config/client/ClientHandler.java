package com.cus.wob.config.client;

import com.cus.wob.config.common.codec.YYPDecoder;
import com.cus.wob.config.common.codec.YYPEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/20
 */
public class ClientHandler extends ChannelInitializer<SocketChannel> {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    private ConfigCenterClient centerClient;

    public ClientHandler(ConfigCenterClient centerClient){
        this.centerClient = centerClient;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("YYPEncoder",new YYPEncoder())
                .addLast("YYPDecoder",new YYPDecoder())
                .addLast("DispathHandler",new DispathHandler(centerClient));
    }
}
