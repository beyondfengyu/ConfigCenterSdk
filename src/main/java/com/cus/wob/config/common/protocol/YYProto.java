package com.cus.wob.config.common.protocol;

import com.cus.wob.config.common.constant.URI;
import com.yy.ent.commons.protopack.base.Marshallable;
import com.yy.ent.commons.protopack.base.Pack;
import com.yy.ent.commons.protopack.base.Unpack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/20
 */
public class YYProto implements Marshallable {
    private static final Logger logger = LoggerFactory.getLogger(YYProto.class);

    private YYPHeader header;
    private YYPBody body;

    public YYProto() {
    }

    public YYProto(int uri) {
        this.header = new YYPHeader(uri);

    }

    public YYProto(YYPHeader header, YYPBody body) {
        this.body = body;
        this.header = header;
    }

    public YYProto(int uri, YYPBody body) {
        this.header = new YYPHeader(uri);
        this.body = body;
    }

    public int uri() {
        return header.uri();
    }

    @Override
    public void marshal(Pack pack) {
        header.marshal(pack);
        body.marshal(pack);
    }

    @Override
    public void unmarshal(Unpack unpack) {
        if (header == null) {
            header = new YYPHeader();
        }
        header.unmarshal(unpack);
        logger.info("YYProto unmarshal=============================uri({}) ", uri());

        switch (uri()) {
            case URI.CLIENT_CONFIG_PING_RSP:
                body = new PingRspBody();
                break;
            case URI.CONFIG_CENTER_REGISTER_RSP:
                body = new RegisterRspBody();
                break;
            case URI.PULL_CONFIG_GRPBYNAME_RSP:
                body = new PullInfoRspBody();
                break;
            case URI.SYNC_CONFIG_LOG_RSP:
                body = new SyncLogRspBody();
                break;
        }
        if (body != null) {
            body.unmarshal(unpack);
        }
    }

    public YYPHeader getHeader() {
        return header;
    }

    public void setHeader(YYPHeader header) {
        this.header = header;
    }

    public YYPBody getBody() {
        return body;
    }

    public void setBody(YYPBody body) {
        this.body = body;
    }
}
