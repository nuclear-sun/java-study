package com.sun.network.rpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.Method;
import java.net.SocketAddress;

public class RPCServer {

    private volatile ServerBootstrap serverBootstrap;


    public RPCServer(int port, RPCProvider provider) {
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(new NioEventLoopGroup(2))
                .localAddress(port)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        //pipeline.addLast(new ServerHandler(provider));
                        pipeline.addLast(new RPCMessageDecoder())
                                .addLast(new ReqMsgHandler(provider));
                    }
                });
    }

    public void start() {

        new Thread() {
            public void run() {
                while (serverBootstrap == null) {}
                serverBootstrap.bind();
            }
        }.start();
    }
}

// V1: echo server
class EchoServerHandler extends ChannelInboundHandlerAdapter {

    // TODO bug for server itself
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        System.out.println("In server " + socketAddress + " connected.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);

        String s = new String(bytes, 0, bytes.length);

        System.out.println("server receive: " + s);

        ByteBuf sendBuf = Unpooled.copiedBuffer(bytes);
        ctx.channel().writeAndFlush(sendBuf);
    }
}

// V2: handler that may throw exception when deserialize
class ServerHandler extends ChannelInboundHandlerAdapter {

    private RPCProvider provider;

    public ServerHandler(RPCProvider provider) {
        this.provider = provider;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        // 0. 反序列化请求消息
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        ReqMsg reqMsg = (ReqMsg)SerDeUtil.deserialize(bytes);

        System.out.println(reqMsg);

        // 1. 找到实现实例
        Object impl = provider.get(reqMsg.getClassName());

        // 2. 反射调用
        Method method = null;
        Throwable ex = null;
        Object retValue = null;
        try {
            method = impl.getClass().getMethod(reqMsg.getMethodName(), reqMsg.getParamTypes());
        } catch (NoSuchMethodException|SecurityException e) {
            throw new Exception("this should not happen");
        }

        try {
            retValue = method.invoke(impl, reqMsg.getArgs());
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            ex = e;
        }

        // 4. 封装响应消息
        ResMsg resMsg = new ResMsg();
        resMsg.setRequestId(reqMsg.getRequestId());
        resMsg.setSuccess(ex == null);
        resMsg.setRetValue(ex != null ? ex: retValue);

        // 5. 序列化结果
        byte[] serialized = SerDeUtil.serialize(resMsg);

        // 6. 结果发送给客户端
        ByteBuf sendBuf = Unpooled.copiedBuffer(serialized);
        ctx.channel().writeAndFlush(sendBuf);
    }
}


// V3: using message decoder to handle seperated package
class ReqMsgHandler extends ChannelInboundHandlerAdapter {


    private RPCProvider provider;

    ReqMsgHandler(RPCProvider provider) {
        this.provider = provider;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ReqMsg reqMsg = (ReqMsg) msg;

        System.out.println(reqMsg);

        // 异步执行
        ctx.executor().execute(new Runnable() {
            @Override
            public void run() {

                try {

                    // 1. 找到实现实例
                    Object impl = provider.get(reqMsg.getClassName());

                    // 2. 反射调用
                    Method method = null;
                    Throwable ex = null;
                    Object retValue = null;
                    try {
                        method = impl.getClass().getMethod(reqMsg.getMethodName(), reqMsg.getParamTypes());
                    } catch (NoSuchMethodException | SecurityException e) {
                        throw new Exception("this should not happen");
                    }

                    try {
                        retValue = method.invoke(impl, reqMsg.getArgs());
                    } catch (Throwable e) {
                        System.out.println(e.getMessage());
                        ex = e;
                    }

                    // 4. 封装响应消息
                    ResMsg resMsg = new ResMsg();
                    resMsg.setRequestId(reqMsg.getRequestId());
                    resMsg.setSuccess(ex == null);
                    resMsg.setRetValue(ex != null ? ex : retValue);

                    // 5. 序列化结果
                    byte[] serialized = SerDeUtil.serialize(resMsg);
                    MsgHeader header = new MsgHeader();
                    header.setType(1);
                    header.setContentLength(serialized.length);
                    byte[] hdrBytes = SerDeUtil.serialize(header);

                    // 6. 结果发送给客户端
                    ByteBuf buffer = ctx.alloc().buffer();
                    buffer.writeBytes(hdrBytes);
                    buffer.writeBytes(serialized);

                    ctx.channel().writeAndFlush(buffer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}