package com.cus.wob.config.common.protocol;

import com.yy.ent.commons.protopack.base.Pack;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/22
 */
public class RegisterReqBody extends YYPBody {
    private String version;
    private String s2sName;
    private String s2sKey;

    @Override
    public void marshal(Pack pack) {
        pack.putVarstr(version);
        pack.putVarstr(s2sName);
        pack.putVarstr(s2sKey);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getS2sName() {
        return s2sName;
    }

    public void setS2sName(String s2sName) {
        this.s2sName = s2sName;
    }

    public String getS2sKey() {
        return s2sKey;
    }

    public void setS2sKey(String s2sKey) {
        this.s2sKey = s2sKey;
    }
}
