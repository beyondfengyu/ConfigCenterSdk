package com.cus.wob.config.util;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/23
 */
public class ProtocolValue {
    public static int combine(int len, int protoType) {
        return protoType << 24 | len;
    }

    public static int[] parse(int firstValue) {
        int nProtoType = firstValue >> 24;
        int packetSize = firstValue & 0x00ffffff;
        return new int[]{nProtoType, packetSize};
    }
}
