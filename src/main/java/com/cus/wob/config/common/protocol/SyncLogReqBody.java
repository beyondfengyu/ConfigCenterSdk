package com.cus.wob.config.common.protocol;

import com.yy.ent.commons.protopack.base.Pack;
import com.yy.ent.commons.protopack.util.MarshallUtils;

import java.util.List;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/22
 */
public class SyncLogReqBody extends YYPBody {
    //    int64_t lastlsn;
//    std::set<std::string> grpNames;
    private long lastlsn;
    private List<String> grpNames;

    @Override
    public void marshal(Pack pack) {
        pack.putLong(lastlsn);
        MarshallUtils.packList(pack,grpNames,String.class);
    }

    public long getLastlsn() {
        return lastlsn;
    }

    public void setLastlsn(long lastlsn) {
        this.lastlsn = lastlsn;
    }

    public List<String> getGrpNames() {
        return grpNames;
    }

    public void setGrpNames(List<String> grpNames) {
        this.grpNames = grpNames;
    }
}
