package com.cus.wob.config.common.protocol;

import com.yy.ent.commons.protopack.base.Pack;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/22
 */
public class PingReqBody extends YYPBody {
    private long lastlsn;

    @Override
    public void marshal(Pack pack) {
        pack.putLong(lastlsn);
    }

    public long getLastlsn() {
        return lastlsn;
    }

    public void setLastlsn(long lastlsn) {
        this.lastlsn = lastlsn;
    }
}
