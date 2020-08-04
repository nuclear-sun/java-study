package com.sun.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class SelectorTest {

    private Selector selector = null;
    private ServerSocketChannel server = null;

    private int port = 9090;

    private volatile boolean runFlag = true;


    private void initServer() throws IOException {
        server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress("localhost", port));
        selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);
    }


    private void start() throws IOException {
        initServer();

        while (runFlag) {

            if (selector.select(1000) > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isAcceptable()) {
                        acceptHandler(key.channel());
                    } else if (key.isReadable()) {
                        readHandler(key.channel());
                    }
                }
            } else {
                Thread.yield();
            }
        }
    }


    private void acceptHandler(Channel channel) throws IOException {
        SocketChannel client = ((ServerSocketChannel) channel).accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        int port = client.socket().getPort();
        String hostAddress = client.socket().getInetAddress().getHostAddress();
        System.out.println("client connected from " + hostAddress+ ":" + port);

    }

    /**
     * 生产环境中应该避免在IO线程中处理数据
     */
    private void readHandler(Channel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        int read;

        while ((read = ((SocketChannel) channel).read(buffer)) > 0) {
            buffer.flip();
            System.out.print(new String(buffer.array(), buffer.position(), buffer.limit()));
            buffer.clear();
        }
    }

    public static void main(String[] args) throws IOException {

        new SelectorTest().start();
    }


}
