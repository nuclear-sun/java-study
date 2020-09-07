package com.sun.network.rpc;

import java.io.Serializable;

public class MsgHeader implements Serializable {

    private int type; // 0 for request, 1 for response

    private int flags;

    private long contentLength;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }
}
