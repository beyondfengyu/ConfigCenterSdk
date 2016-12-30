package com.cus.wob.config.common.codec;

import com.cus.wob.config.common.protocol.YYProto;
import com.cus.wob.config.util.ProtocolValue;
import com.yy.ent.commons.protopack.base.Unpack;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.util.List;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/21
 */
public class YYPDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(YYPDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        // 设置小端模式
        buf = buf.order(ByteOrder.LITTLE_ENDIAN);
        int length = buf.readableBytes();
        if (length < 4) {
            return;
        }

        int firstIntValue = buf.markReaderIndex().readInt();
        int[] protoValue = ProtocolValue.parse(firstIntValue);
        int curPacketSize = protoValue[1];
        int dataSize = curPacketSize - 4;
        length = buf.readableBytes();
        if (length < dataSize) {
            buf.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[dataSize];
        buf.readBytes(bytes, 0, bytes.length);
        Unpack unpack = new Unpack(bytes);
        byte protoType = (byte) protoValue[0];
        if (protoType == 0) {
            YYProto proto = new YYProto();
            proto.unmarshal(unpack);
            list.add(proto);
        }
    }
}
