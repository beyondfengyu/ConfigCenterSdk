package com.cus.wob.config.common.protocol;

import com.yy.ent.commons.protopack.base.Pack;
import com.yy.ent.commons.protopack.base.Unpack;
import com.yy.ent.commons.protopack.marshal.BeanMarshal;
import com.yy.ent.commons.protopack.util.Uint;

/**
 * @author laochunyu
 * @version 1.0
 * @date 2016/12/19
 */
public class YYPHeader extends BeanMarshal {

    public int uri;

    public short resCode = 200;

    public YYPHeader() {

    }

    public YYPHeader(int uri) {
        this.uri = uri;
    }

    public YYPHeader(int uri, short resCode) {
        this.uri = uri;
        this.resCode = resCode;
    }

    public int getUri() {
        return uri;
    }

    public void setUri(int uri) {
        this.uri = uri;
    }

    public short getResCode() {
        return resCode;
    }

    public void setResCode(short resCode) {
        this.resCode = resCode;
    }

    @Override
    public void marshal(Pack pack) {
        pack.putUInt(new Uint(uri));
        pack.putShort(resCode);
    }

    @Override
    public void unmarshal(Unpack unpack) {
        this.uri = unpack.popUInt().intValue();
        this.resCode = unpack.popShort();
    }

    @Override
    public String toString() {
        return "sid=" + (uri & (Integer.valueOf("ff", 16))) + ",cid=" + (uri >> 8) + ",res_code=" + resCode;
    }

    public int uri() {
        return uri;
    }

}
