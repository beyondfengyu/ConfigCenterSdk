package com.cus.wob.config.common.protocol;

import com.yy.ent.commons.protopack.base.Pack;
import com.yy.ent.commons.protopack.util.MarshallUtils;

import java.util.List;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/22
 */
public class PullInfoReqBody extends YYPBody {

    private List<String> grpNames;

    @Override
    public void marshal(Pack pack) {
        MarshallUtils.packList(pack,grpNames,String.class);
    }

    public List<String> getGrpNames() {
        return grpNames;
    }

    public void setGrpNames(List<String> grpNames) {
        this.grpNames = grpNames;
    }
}
