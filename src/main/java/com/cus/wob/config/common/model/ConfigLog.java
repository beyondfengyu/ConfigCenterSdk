package com.cus.wob.config.common.model;

import com.yy.ent.commons.protopack.base.Marshallable;
import com.yy.ent.commons.protopack.base.Pack;
import com.yy.ent.commons.protopack.base.Unpack;
import com.yy.ent.commons.protopack.util.Uint;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/21
 */
public class ConfigLog implements Marshallable {

    private long lsn;
    private String grpName;
    private String ckey;
    private String cval;
    private Uint op;
    private String ipallow;
    private String grpallow;
    private long version;
    private Uint commitTime;

    public long getLsn() {
        return lsn;
    }

    public void setLsn(long lsn) {
        this.lsn = lsn;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public String getCkey() {
        return ckey;
    }

    public void setCkey(String ckey) {
        this.ckey = ckey;
    }

    public String getCval() {
        return cval;
    }

    public void setCval(String cval) {
        this.cval = cval;
    }

    public Uint getOp() {
        return op;
    }

    public void setOp(Uint op) {
        this.op = op;
    }

    public String getIpallow() {
        return ipallow;
    }

    public void setIpallow(String ipallow) {
        this.ipallow = ipallow;
    }

    public String getGrpallow() {
        return grpallow;
    }

    public void setGrpallow(String grpallow) {
        this.grpallow = grpallow;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Uint getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Uint commitTime) {
        this.commitTime = commitTime;
    }

    @Override
    public String toString(){
        return "ConfigLog toString:{lsn:"+lsn+", grpName:"+grpName+",ckey:"+ckey+",cval:"+cval+",op:"+op
                +", ipallow:"+ipallow+",grpallow:"+grpName+",version:"+version+",commitTime:"+commitTime+"}";
    }

    @Override
    public void marshal(Pack pack) {

    }

    @Override
    public void unmarshal(Unpack unpack) {
        lsn = unpack.popLong();
        grpName = unpack.popVarstr();
        ckey = unpack.popVarstr();
        cval = unpack.popVarstr();
        op = unpack.popUInt();
        ipallow = unpack.popVarstr();
        grpallow = unpack.popVarstr();
        version = unpack.popLong();
        commitTime = unpack.popUInt();
    }
}
