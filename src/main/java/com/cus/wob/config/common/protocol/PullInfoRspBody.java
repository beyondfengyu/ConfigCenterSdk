package com.cus.wob.config.common.protocol;

import com.cus.wob.config.common.model.ConfigInfo;
import com.yy.ent.commons.protopack.base.Unpack;
import com.yy.ent.commons.protopack.util.MarshallUtils;

import java.util.List;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/22
 */
public class PullInfoRspBody extends YYPBody {
//    int64_t lastlsn;
//    bool isEnd;
//    std::vector<ConfigInfo> infoList;
//    std::vector<ConfigTestInfo> testList;

    private long lastlsn;
    private boolean isEnd;
    private List<ConfigInfo> infoList;

    public void unmarshal(Unpack unpack) {
        lastlsn = unpack.popLong();
        isEnd = unpack.popBoolean();
        infoList = MarshallUtils.unpackList(unpack, ConfigInfo.class);
    }

    public long getLastlsn() {
        return lastlsn;
    }

    public void setLastlsn(long lastlsn) {
        this.lastlsn = lastlsn;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public List<ConfigInfo> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<ConfigInfo> infoList) {
        this.infoList = infoList;
    }
}
