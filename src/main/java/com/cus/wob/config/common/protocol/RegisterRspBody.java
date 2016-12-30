package com.cus.wob.config.common.protocol;

import com.yy.ent.commons.protopack.base.Unpack;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/22
 */
public class RegisterRspBody extends YYPBody{
    private boolean isPass;

    @Override
    public void unmarshal(Unpack unpack) {
        isPass = unpack.popBoolean();
    }

    public boolean isPass() {
        return isPass;
    }

    public void setIsPass(boolean isPass) {
        this.isPass = isPass;
    }
}
