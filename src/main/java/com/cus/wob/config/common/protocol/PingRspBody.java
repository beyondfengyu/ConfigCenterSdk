package com.cus.wob.config.common.protocol;

import com.yy.ent.commons.protopack.base.Unpack;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/22
 */
public class PingRspBody extends YYPBody {
    private long lastlsn;

    public void unmarshal(Unpack unpack) {
       lastlsn = unpack.popLong();
    }

    public long getLastlsn() {
        return lastlsn;
    }

    public void setLastlsn(long lastlsn) {
        this.lastlsn = lastlsn;
    }
}
