package com.cus.wob.config.listener;

import com.cus.wob.config.common.model.ConfigInfo;
import com.cus.wob.config.common.model.ConfigLog;

import java.util.List;

/**
 *
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/22
 */
public interface ConfigListener {
    /**
     * 注册回调方法
     * @param isPass 注册状态
     */
    void onRegister(boolean isPass);

    /**
     * 订阅回调方法
     * @param isSubs 订阅状态
     * @param infoList  配置项列表
     */
    void onSubscribe(boolean isSubs, List<ConfigInfo> infoList);

    /**
     * 推送同步日志回调方法
     * @param logList 同步的日志列表
     */
    void onSyncLog(List<ConfigLog> logList);

    /**
     * ping请求响应回调
     * @param lastlsn
     */
    void onPing(long lastlsn);
}
