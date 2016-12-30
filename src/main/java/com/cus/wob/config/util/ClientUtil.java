package com.cus.wob.config.util;

import com.yy.ent.commons.protopack.util.Uint;
import com.cus.wob.config.common.model.ConfigInfo;
import com.cus.wob.config.common.model.ConfigLog;

import java.util.Random;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/22
 */
public class ClientUtil {

    /**
     * 构建Map的key，唯一标识ConfigInfo项
     * @param grpName
     * @param key
     * @return
     */
    public static String buildInfoKey(String grpName, String key) {
        return grpName + "_" + key;
    }

    /**
     * 构建Map的key，唯一标识ConfigInfo项
     * @param info
     * @return
     */
    public static String buildInfoKey(ConfigInfo info) {
        return buildInfoKey(info.getGrpName(),info.getCkey());
    }

    /**
     * 构建Map的key，唯一标识ConfigInfo项
     * @param log
     * @return
     */
    public static String buildInfoKey(ConfigLog log) {
        return buildInfoKey(log.getGrpName(),log.getCkey());
    }

    /**
     * 构建灰度发布的信息项
     * @param configLog
     * @return
     */
    public static ConfigInfo buildTestInfoByLog(ConfigLog configLog) {
        return buildInfoByLog(configLog, true);
    }

    /**
     * 构建全网发布的信息项
     * @param configLog
     * @return
     */
    public static ConfigInfo buildProdInfoByLog(ConfigLog configLog) {
        return buildInfoByLog(configLog, false);
    }

    /**
     * 构建发布的信息项
     * @param configLog
     * @param isTest 是否灰度
     * @return
     */
    public static ConfigInfo buildInfoByLog(ConfigLog configLog,boolean isTest) {
        ConfigInfo configInfo = new ConfigInfo();
        configInfo.setCkey(configLog.getCkey());
        configInfo.setCval(configLog.getCval());
        configInfo.setGrpName(configLog.getGrpName());
        Uint status = isTest ? new Uint(0) : new Uint(1);
        configInfo.setStatus(status);
        configInfo.setVersion(configLog.getVersion());
        configInfo.setCommitTime(configLog.getCommitTime());
        return configInfo;
    }

    /**
     * 等待下次重连等待的时间，单位秒
     * @return
     */
    public static int randomSleepConn() {
        Random random = new Random();
        return random.nextInt(5) + 1;
    }
}
