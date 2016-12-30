package com.cus.wob.config.service;

import com.cus.wob.config.client.ConfigCenterClient;
import com.cus.wob.config.listener.ConfigListener;
import com.cus.wob.config.util.ClientUtil;
import com.cus.wob.config.common.model.ConfigInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/22
 */
public class ConfigCenterService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigCenterService.class);
    protected ConfigCenterClient client;


    public ConfigCenterService(ConfigListener listener) {
        client = new ConfigCenterClient(listener);
    }

    public ConfigCenterService(String version, String s2sName, String s2sKey, ConfigListener listener) {
        client = new ConfigCenterClient(version, s2sName, s2sKey, listener);
    }

    /**
     * 初始化客户端
     * 注册或者订阅之前必须先初始化
     *
     * @throws Exception
     */
    public void init() throws Exception {
        client.init();
    }

    /**
     * 向服务器注册，注册结果会通过回调形式返回
     */
    public void register() {
        client.register();
    }

    /**
     * 订阅感兴趣的组的配置项，订阅结果会通过回调的形式返回
     * 组的配置项有变化时，服务器以日志的形式推送下来
     * 注意，订阅是一个异步的操作，获取配置项前需要判断是否已经订阅成功
     *
     * @param grpName
     */
    public void subscribe(List<String> grpName) {
        client.subscribe(grpName);
    }

    public ConfigCenterClient getClient() {
        return client;
    }

    /**
     * 判断是否订阅成功，
     * 只有订阅成功才能获取到订阅的配置信息
     * @return
     */
    public boolean isSubSuccess() {
        return client.isSubs();
    }

    /**
     * 获取全网发布的配置信息
     * @return
     */
    public List<ConfigInfo> getSubConfigInfos(){
        ConcurrentMap<String,ConfigInfo> infoMap = client.getInfoProdMap();
        List<ConfigInfo> list = new ArrayList<ConfigInfo>();
        for (String key : infoMap.keySet()) {
            list.add(infoMap.get(key));
        }
        return list;
    }

    /**
     * 获取灰度发布的配置信息
     * @return
     */
    public List<ConfigInfo> getTestConfigInfos(){
        ConcurrentMap<String,ConfigInfo> infoMap = client.getInfoTestMap();
        List<ConfigInfo> list = new ArrayList<ConfigInfo>();
        for (String key : infoMap.keySet()) {
            list.add(infoMap.get(key));
        }
        return list;
    }

    /**
     * 通过组名和KEY获取对应的配置值
     * @param grpName 组名
     * @param key   配置项key
     * @return
     */
    public ConfigInfo getInfoByGrpAndKey(String grpName, String key) {
        ConcurrentMap<String,ConfigInfo> infoMap = client.getInfoProdMap();
        return infoMap.get(ClientUtil.buildInfoKey(grpName, key));
    }

    /**
     * 获取字符串类型的配置值
     * @param grpName
     * @param key
     * @return
     */
    public String getStrByGrpAndKey(String grpName, String key) {
        ConfigInfo info = getInfoByGrpAndKey(grpName, key);
        if (info != null) {
            return info.getCval();
        }
        return null;
    }

    /**
     * 获取整型的配置值
     * @param grpName
     * @param key
     * @return
     */
    public Integer getIntByGrpAndKey(String grpName, String key) {
        ConfigInfo info = getInfoByGrpAndKey(grpName, key);
        if (info != null) {
            return Integer.valueOf(info.getCval());
        }
        return null;
    }

    /**
     * 获取布尔类型的配置值
     * @param grpName
     * @param key
     * @return
     */
    public Boolean getBooleanByGrpAndKey(String grpName, String key) {
        ConfigInfo info = getInfoByGrpAndKey(grpName, key);
        if (info != null) {
            return Boolean.valueOf(info.getCval());
        }
        return null;
    }


    /**
     * 通过组名和KEY获取灰度发布的配置值
     * @param grpName 组名
     * @param key   配置项key
     * @return
     */
    public ConfigInfo getTestInfoByGrpAndKey(String grpName, String key) {
        ConcurrentMap<String,ConfigInfo> infoMap = client.getInfoProdMap();
        return infoMap.get(ClientUtil.buildInfoKey(grpName,key));
    }

    /**
     * 获取字符串类型的灰度发布的配置值
     * @param grpName
     * @param key
     * @return
     */
    public String getTestStrByGrpAndKey(String grpName, String key) {
        ConfigInfo info = getTestInfoByGrpAndKey(grpName, key);
        if (info != null) {
            return info.getCval();
        }
        return null;
    }

    /**
     * 获取整型的度发布的配置值
     * @param grpName
     * @param key
     * @return
     */
    public Integer getTestIntByGrpAndKey(String grpName, String key) {
        ConfigInfo info = getTestInfoByGrpAndKey(grpName, key);
        if (info != null) {
            return Integer.valueOf(info.getCval());
        }
        return null;
    }

    /**
     * 获取布尔类型的度发布的配置值
     * @param grpName
     * @param key
     * @return
     */
    public Boolean getTestBooleanByGrpAndKey(String grpName, String key) {
        ConfigInfo info = getTestInfoByGrpAndKey(grpName, key);
        if (info != null) {
            return Boolean.valueOf(info.getCval());
        }
        return null;
    }
}
