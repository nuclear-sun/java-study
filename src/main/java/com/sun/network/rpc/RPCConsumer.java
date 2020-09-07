package com.sun.network.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;

public class RPCConsumer {

    private RPCClient client;

    public RPCConsumer() {
    }

    public RPCConsumer(String host, int port) throws InterruptedException {
        client = new RPCClient(host, port);
    }

    <T> T get(Class<T> tClass) {

        return (T)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{tClass}, new InvocationHandler() {




            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                String intName = tClass.getName();
                String methodName = method.getName();

                // 0. 本地拦截
                if(client == null){
/*
                    Object impl = RPCProvider.get(intName);
                    if(impl == null) {
                        throw new Exception("Impl for "+intName + " not found.");
                    }
                    return method.invoke(impl, args);*/
                }

                // 1. 组装消息
                ReqMsg reqMsg = new ReqMsg();
                reqMsg.setClassName(intName);
                reqMsg.setMethodName(methodName);
                reqMsg.setArgs(args);
                reqMsg.setParamTypes(method.getParameterTypes());
                reqMsg.setRequestId(RequestIdGenerator.next());

                System.out.println("reqMsg to sent: " + reqMsg);

                // 2. 序列化消息
                // 2.1 消息体
                byte[] bytes = SerDeUtil.serialize(reqMsg);
                // 2.2 消息头
                MsgHeader msgHeader = new MsgHeader();
                msgHeader.setType(0);
                msgHeader.setContentLength(bytes.length);
                byte[] hdrBytes= SerDeUtil.serialize(msgHeader);

                System.out.println("header length: " + hdrBytes.length);

                // 4.0 准备回调，需要在发送消息前做好
                CompletableFuture<ResMsg> promise = new CompletableFuture<>();
                client.register(reqMsg.getRequestId(), promise);

                // 3. 发送消息
                ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
                buffer.writeBytes(hdrBytes);
                buffer.writeBytes(bytes);
                client.write(buffer);

                // 4. 阻塞等待返回
                ResMsg resMsg = promise.get();
                client.unregister(reqMsg.getRequestId());
                if(resMsg.isSuccess()) {
                    return resMsg.getRetValue();
                } else {
                    throw (Throwable)resMsg.getRetValue();
                }
            }
        });
    }




}
