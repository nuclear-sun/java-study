package com.sun.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;

public class NettyUsage1 {

    public static void main(String[] args) {

    }

    public static void testEventLoopGroup() {
        NioEventLoopGroup selector = new NioEventLoopGroup(2);
        selector.execute(() -> {

            while (true) {
                System.out.println("you win");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        selector.execute(() -> {
            while (true) {
                System.out.println("hello");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void printBuf(ByteBuf buf) {
        System.out.println(buf.isReadable());

    }

    @Test
    public static void clientMode() throws InterruptedException {

        NioEventLoopGroup eventExecutors = new NioEventLoopGroup(1);
        NioSocketChannel channel = new NioSocketChannel();

        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new MyHandler());

        eventExecutors.register(channel);

        ChannelFuture connect = channel.connect(new InetSocketAddress("localhost", 9090));
        connect.sync();

        ByteBuf byteBuf = Unpooled.copiedBuffer("you win".getBytes());
        ChannelFuture writerFuture = channel.writeAndFlush(byteBuf);
        writerFuture.sync();

        channel.closeFuture().sync();

//        channel.close().sync();

    }

    @Test
    public static void serverMode() throws InterruptedException {
        NioServerSocketChannel channel = new NioServerSocketChannel();
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new MyHandler());

        NioEventLoopGroup eventExecutors = new NioEventLoopGroup(1);
        eventExecutors.register(channel);

        channel.bind(new InetSocketAddress(9090));

        channel.closeFuture().sync();

    }
}


class MyHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        System.out.println("channel registered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("channel active");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        System.out.println(msg.getClass());

        if(msg instanceof ByteBuf) {

            ByteBuf buf = (ByteBuf) msg;

            CharSequence charSequence = buf.getCharSequence(0, buf.readableBytes(), CharsetUtil.UTF_8);
//        CharSequence charSequence = buf.readCharSequence(buf.readableBytes(), CharsetUtil.UTF_8);
            System.out.println(charSequence);

//        buf.resetReaderIndex();

            ctx.writeAndFlush(buf);
        } else if(msg instanceof NioSocketChannel) {
            NioSocketChannel client = (NioSocketChannel) msg;

        }
    }
}
