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
public class ConfigInfo implements Marshallable {
    private String grpName;
    private String ckey;
    private String cval;
    private Uint status;
    private Long version;
    private Uint commitTime;

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

    public Uint getStatus() {
        return status;
    }

    public void setStatus(Uint status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Uint getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Uint commitTime) {
        this.commitTime = commitTime;
    }

    public void marshal(Pack pack) {

    }

    public void unmarshal(Unpack unpack) {
        this.setGrpName(unpack.popVarstr());
        this.setCkey(unpack.popVarstr());
        this.setCval(unpack.popVarstr());
        this.setStatus(unpack.popUInt());
        this.setVersion(unpack.popLong());
        this.setCommitTime(unpack.popUInt());
    }

    @Override
    public String toString(){
        return "ConfigInfo toString: {grpName:"+grpName+",ckey:"+ckey+",cval:"+cval+",status:"+status
                +", version:"+version+",commitTime:"+commitTime+"}";
    }
}
