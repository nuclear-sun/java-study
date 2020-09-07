package com.sun.network.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class RPCClient {

    //private Bootstrap bootstrap;

    //private ConcurrentHashMap<String, CompletableFuture> nofications = new ConcurrentHashMap<>();

    private ChannelFuture future;

    private MsgDispatcher dispatcher;

    public RPCClient(String host, int port) throws InterruptedException {

        dispatcher = new MsgDispatcher();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(1))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {

                        ChannelPipeline pipeline = socketChannel.pipeline();
                        //pipeline.addLast(new ClientHandler(nofications));
                        pipeline.addLast(new RPCMessageDecoder())
                                .addLast(new ResMsgHandler(dispatcher));
                    }
                });
        future = bootstrap.connect(host, port);
        future.sync();
    }

    public void write(ByteBuf buf) throws InterruptedException {
        future.channel().writeAndFlush(buf).sync();
    }

    public void register(String requestId, CompletableFuture<?> future) {
        this.dispatcher.register(requestId, future);
        //this.nofications.putIfAbsent(requestId, future);
    }

    public void unregister(String requestId) {
        this.dispatcher.unregister(requestId);
        //this.nofications.remove(requestId);
    }

    public void close() {
        future.channel().close();
    }

}

//V1: decode directly
class ClientHandler extends ChannelInboundHandlerAdapter {

    private ConcurrentHashMap<String, CompletableFuture> nofications;

    public ClientHandler(ConcurrentHashMap<String, CompletableFuture> nofications) {
        this.nofications = nofications;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        // 1. 反序列化
        byte[] bytes = new byte[buf.readableBytes()];
        if (buf.isReadable()) {
            buf.readBytes(bytes);
        }
        ResMsg resMsg = (ResMsg) SerDeUtil.deserialize(bytes);
        System.out.println("client receive: " + resMsg);

        // 2. 放置到合适的位置

        // 3. 发通知
        CompletableFuture completableFuture = nofications.get(resMsg.getRequestId());

        if(completableFuture != null) {
            completableFuture.complete(resMsg);
        }
    }
}

// V2: using message decoder to handler seperated packages
class ResMsgHandler extends ChannelInboundHandlerAdapter {

    private MsgDispatcher dispatcher;

    ResMsgHandler(MsgDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ResMsg resMsg = (ResMsg) msg;
        System.out.println(resMsg);
        dispatcher.dispatch(resMsg.getRequestId(), resMsg);
    }
}
