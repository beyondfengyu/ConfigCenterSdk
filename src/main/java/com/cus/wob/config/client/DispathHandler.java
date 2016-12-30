package com.cus.wob.config.client;

import com.cus.wob.config.common.protocol.YYProto;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/21
 */
public class DispathHandler extends ChannelHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(DispathHandler.class);
    private ConfigCenterClient centerClient;

    public DispathHandler( ConfigCenterClient centerClient){
        this.centerClient = centerClient;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof YYProto) {
            YYProto yyProto = (YYProto)msg;
            logger.info("DispathHandler uri :{}", yyProto.uri());
            centerClient.callBack(yyProto);
        }

    }
}
