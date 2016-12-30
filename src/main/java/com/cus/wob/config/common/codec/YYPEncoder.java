package com.cus.wob.config.common.codec;

import com.cus.wob.config.common.protocol.YYProto;
import com.cus.wob.config.util.ProtocolValue;
import com.yy.ent.commons.protopack.base.Pack;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/21
 */
public class YYPEncoder extends MessageToByteEncoder<YYProto> {

    private static final Logger logger = LoggerFactory.getLogger(YYPEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, YYProto proto, ByteBuf byteBuf) throws Exception {
        try {
            Pack pack = new Pack();
            proto.marshal(pack);
            ByteBuffer data = pack.getBuffer();
            byte protoType = 0;
            if (pack.getAttachment() != null) {
                protoType = Byte.parseByte(pack.getAttachment().toString());
            }
            // 字节序转成YY协议的低端字节
            byteBuf = byteBuf.order(ByteOrder.LITTLE_ENDIAN);
            byteBuf.writeBytes(getOutBytes(data, protoType));

        } catch (Throwable e) {
            logger.error("throwable: " + e.getMessage(), e);
            throw new EncoderException(e);
        }
    }

    protected byte[] getOutBytes(ByteBuffer data, byte protoType) {
        int len = data.limit() - data.position() + 4;
        ByteBuffer out = ByteBuffer.allocate(len);
        // 设置小端模式
        out.order(ByteOrder.LITTLE_ENDIAN);
        int nFirstValue = ProtocolValue.combine(len, protoType);
        // 长度包含包长度int 4个字节
        out.putInt(nFirstValue);
        out.put(data);
        out.flip();
        return out.array();


    }
}
