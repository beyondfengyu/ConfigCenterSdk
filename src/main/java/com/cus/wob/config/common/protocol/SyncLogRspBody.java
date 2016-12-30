package com.cus.wob.config.common.protocol;

import com.cus.wob.config.common.model.ConfigLog;
import com.yy.ent.commons.protopack.base.Unpack;
import com.yy.ent.commons.protopack.util.MarshallUtils;

import java.util.List;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/22
 */
public class SyncLogRspBody extends YYPBody {
//    int64_t lastlsn;
//    std::vector<ConfigLog> logs;
    private long lastlsn;
    private List<ConfigLog> logs;

    @Override
    public void unmarshal(Unpack unpack) {
        lastlsn = unpack.popLong();
        logs = MarshallUtils.unpackList(unpack, ConfigLog.class);

    }

    public long getLastlsn() {
        return lastlsn;
    }

    public void setLastlsn(long lastlsn) {
        this.lastlsn = lastlsn;
    }

    public List<ConfigLog> getLogs() {
        return logs;
    }

    public void setLogs(List<ConfigLog> logs) {
        this.logs = logs;
    }
}
