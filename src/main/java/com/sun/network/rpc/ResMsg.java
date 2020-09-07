package com.sun.network.rpc;

import java.io.Serializable;

public class ResMsg implements Serializable {

    private String requestId;

    private boolean success;

    private Object retValue;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getRetValue() {
        return retValue;
    }

    public void setRetValue(Object retValue) {
        this.retValue = retValue;
    }

    @Override
    public String toString() {
        return "ResMsg{" +
                "requestId='" + requestId + '\'' +
                ", success=" + success +
                ", retValue=" + retValue +
                '}';
    }
}
