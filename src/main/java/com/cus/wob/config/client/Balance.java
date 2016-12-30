package com.cus.wob.config.client;

import com.cus.wob.config.util.RemoteUtil;

/**
 * 负载均衡器
 * 通过轮询的策略来选择IP
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/22
 */
public class Balance {
    private String host;
    private String[] ips;
    private int curIndex;

    public Balance(String host){
        this.host = host;
        this.ips = RemoteUtil.getIpsByName(host);
        curIndex = -1;
    }

    public String select() {
        curIndex++;
        if(curIndex>ips.length-1){
            this.ips = RemoteUtil.getIpsByName(host);
            curIndex = 0;
        }
        return ips[curIndex];
    }

    public int size(){
        return ips.length;
    }
}
