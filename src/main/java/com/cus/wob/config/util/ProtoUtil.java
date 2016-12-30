package com.cus.wob.config.util;

import com.cus.wob.config.common.constant.URI;
import com.cus.wob.config.common.protocol.PingReqBody;
import com.cus.wob.config.common.protocol.PullInfoReqBody;
import com.cus.wob.config.common.protocol.RegisterReqBody;
import com.cus.wob.config.common.protocol.YYProto;

import java.util.List;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/22
 */
public class ProtoUtil {

    public static YYProto buildRegistReqProto(String version, String s2sName, String s2sKey) {
        RegisterReqBody body = new RegisterReqBody();
        body.setS2sKey(s2sKey);
        body.setS2sName(s2sName);
        body.setVersion(version);
        return new YYProto(URI.CONFIG_CENTER_REGISTER_REQ, body);
    }

    public static YYProto buildPullReqProto(List<String> grpNames) {
        PullInfoReqBody body = new PullInfoReqBody();
        body.setGrpNames(grpNames);
        return new YYProto(URI.PULL_CONFIG_GRPBYNAME_REQ, body);
    }

    public static YYProto buildPingReqProto(long lastlsn) {
        PingReqBody yypBody = new PingReqBody();
        yypBody.setLastlsn(lastlsn);
        return new YYProto(URI.CLIENT_CONFIG_PING_REQ, yypBody);
    }
}
