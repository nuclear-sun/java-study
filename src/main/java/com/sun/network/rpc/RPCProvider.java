package com.sun.network.rpc;

import java.util.concurrent.ConcurrentHashMap;

public class RPCProvider {

    private ConcurrentHashMap<String, Object> impls = new ConcurrentHashMap<>();

    public void register(String intName, Object impl) {
        impls.putIfAbsent(intName, impl);
    }

    public void unregister(String intName) {
        impls.remove(intName);
    }

    public Object get(String intName) {
        return impls.get(intName);
    }
}
