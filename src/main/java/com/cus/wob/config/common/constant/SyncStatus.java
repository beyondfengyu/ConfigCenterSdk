package com.cus.wob.config.common.constant;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/22
 */
public class SyncStatus {
    public static final int CONFIG_ADD = 0;     //增加
    public static final int CONFIG_UPDATE = 1;  //更新
    public static final int CONFIG_ROLLBACK = 2;//回滚
    public static final int CONFIG_DELETE = 3;  //删除
    public static final int CONFIG_TEST_OPEN = 4;   //灰度发布
    public static final int CONFIG_TEST_CLOSE = 5;  //灰度删除
}
