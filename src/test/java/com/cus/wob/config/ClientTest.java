package com.cus.wob.config;

import com.cus.wob.config.client.ConfigCenterClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/21
 */
public class ClientTest {

    public static void main(String[] args) throws Exception {
        ConfigCenterClient centerClient = new ConfigCenterClient("1.0","javasdk","",null);
        centerClient.init();
        centerClient.register();
        List<String> grpNames = new ArrayList<String>();
        grpNames.add("videotranscode");
        centerClient.subscribe(grpNames);
//        //模拟通道关闭，测试重连
//        TimeUnit.SECONDS.sleep(5);
//        centerClient.getCf().channel().close();

    }
}
