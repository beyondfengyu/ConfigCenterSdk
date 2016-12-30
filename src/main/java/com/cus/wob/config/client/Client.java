package com.cus.wob.config.client;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/20
 */
public interface Client {

    boolean connect(String ip,int port) throws InterruptedException, TimeoutException;

    boolean reconnect() throws Exception;

    boolean isAlive();

    void close();

    void closeSync();

}
