package com.cus.wob.config.common.constant;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/22
 */
public class URI {

    /**
     * 客户端注册uri
     */
    public static final int CONFIG_CENTER_REGISTER_REQ = 4307 << 8 | 88;
    public static final int CONFIG_CENTER_REGISTER_RSP = 4308 << 8 | 88;

    /**
     * 拉取配置信息uri
     */
    public static final int PULL_CONFIG_GRPBYNAME_REQ = 4301 << 8 | 88;
    public static final int PULL_CONFIG_GRPBYNAME_RSP = 4302 << 8 | 88;

    /**
     * 重连后同步uri
     */
    public static final int SYNC_CONFIG_LOG_REQ = 4303 << 8 | 88;
    public static final int SYNC_CONFIG_LOG_RSP = 4304 << 8 | 88;

    /**
     *客户端ping请求uri
     */
    public static final int CLIENT_CONFIG_PING_REQ = 4305 << 8 | 88;
    public static final int CLIENT_CONFIG_PING_RSP = 4306 << 8 | 88;
}
