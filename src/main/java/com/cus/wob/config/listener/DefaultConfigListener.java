package com.cus.wob.config.listener;

import com.cus.wob.config.common.model.ConfigInfo;
import com.cus.wob.config.common.model.ConfigLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/22
 */
public class DefaultConfigListener implements ConfigListener {
    private static final Logger logger = LoggerFactory.getLogger(DefaultConfigListener.class);

    @Override
    public void onRegister(boolean isPass) {
        logger.info("onRegister ---> isPass:{}", isPass);
    }

    @Override
    public void onSubscribe(boolean isSubs, List<ConfigInfo> infoList) {
        logger.info("onSubscribe ---> isSubs:{}", isSubs);
        if (isSubs) {
            for (ConfigInfo info : infoList) {
                logger.info("onSubscribe ---> configInfo:{}", info);
            }
        }
    }

    @Override
    public void onSyncLog(List<ConfigLog> logList) {
        for (ConfigLog info : logList) {
            logger.info("onSyncLog ---> configLog:{}", info);
        }
    }

    @Override
    public void onPing(long lastlsn) {
        logger.info("onPing ---> lastlsn:{}",lastlsn);
    }
}
