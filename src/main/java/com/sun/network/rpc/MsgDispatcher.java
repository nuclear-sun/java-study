package com.sun.network.rpc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class MsgDispatcher {

    private ConcurrentHashMap<String, CompletableFuture> map = new ConcurrentHashMap<>();

    public void register(String requestId, CompletableFuture future) {
        map.put(requestId, future);
    }

    public void unregister(String requestId) {
        map.remove(requestId);
    }

    public void dispatch(String requestId, Object result) {
        CompletableFuture future = map.get(requestId);
        if(future!=null ){
            future.complete(result);
        }
    }

}
