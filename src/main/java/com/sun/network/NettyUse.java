package com.sun.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyUse {

    static class MyServerSocketHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            super.channelRegistered(ctx);
            System.out.println("server registered");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            System.out.println("server activated");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            //super.channelRead(ctx, msg);
//            NioSocketChannel socketChannel = (NioSocketChannel)msg;
            System.out.println("msg type: "+msg.getClass().getName());
        }
    }

    static class MySocketHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            //super.channelRead(ctx, msg);
            ByteBuf buf = (ByteBuf) msg;
            ctx.channel().writeAndFlush(buf);
        }
    }

    public static void startServer() throws InterruptedException {

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup(1))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new MySocketHandler());
                    }
                });
        ChannelFuture bind = bootstrap.bind(9090);
        ChannelFuture future = bind.sync();
        future.channel().closeFuture().sync();

    }

    public static void startClient() {

    }

    public static void main(String[] args) throws InterruptedException {
        startServer();
    }


}

