package com.cus.wob.config.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/20
 */
public class RemoteUtil {

    /**
     * 根据域名获取IP 101.226.185.117
     *
     * @param name
     * @return
     */
    public static String[] getIpsByName(String name) {
        try {
            InetAddress[] addrs = InetAddress.getAllByName(name);
            if (addrs != null && addrs.length > 0) {
                String[] ips = new String[addrs.length];
                for (int i = 0; i < addrs.length; i++) {
                    ips[i] = addrs[i].getHostAddress();
                    System.out.println("ip:" + ips[i]);
                }
                return ips;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static String[] getIpsByName(String name) {
//        String[] ips = {"101.226.185.117","106.38.255.22","140.207.209.116"};
//        return ips;
//    }

    public static void main(String[] args) {
        getIpsByName("svcconfig.yy.com");
        for(int i=0;i<100;i++)
        System.out.println(ClientUtil.randomSleepConn());
    }
}
