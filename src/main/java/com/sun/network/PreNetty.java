package com.sun.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class PreNetty {

    public static void main(String[] args) {

        new Boss().start();
    }

}


interface SelectionKeyHandler {

    void doRead(SelectionKey key) throws IOException;

    void doWrite(SelectionKey key) throws IOException;

    void doAccept(SelectionKey key) throws IOException;

    void doConnect(SelectionKey key) throws IOException;
}

abstract class SelectionLoop implements Runnable {

    protected Selector selector;

    private SelectionKeyHandler handler;

    private volatile boolean runFlag = true;

    private Thread thread;

    protected void setHandler(SelectionKeyHandler handler) {
        this.handler = handler;
    }

    public void run() {

        try {
            if(this.handler == null) {
                throw new RuntimeException("SelectionLoop.handler is null");
            }
            init();

            while (runFlag) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if(key.isAcceptable()) {
                        handler.doAccept(key);
                    } else if(key.isReadable()) {
                        handler.doRead(key);
                    } else if(key.isWritable()) {
                        handler.doWrite(key);
                    } else if(key.isConnectable()) {
                        handler.doConnect(key);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    protected void init() throws IOException {
        selector = Selector.open();
    }

    protected void destroy() throws IOException {
        selector.close();
        selector = null;
    }

    public void start() {
        Thread thread = new Thread(this);
        this.thread = thread;
        thread.start();
        //System.out.println(selector.toString() + " started.");
    }

    public void stop() {
        this.runFlag = false;
    }
}

// TODO: use interface
class SelectableChannelDispatcher {

    private Worker[] workers;

    private int n = 0;

    public SelectableChannelDispatcher(Worker[] workers) {
        this.workers = workers;
    }

    public void dispatch(SelectableChannel channel) {
        if(n >= workers.length) {
            n = 0;
        }
        if(n < workers.length) {
            try {
                workers[n].register(channel, SelectionKey.OP_READ);
                n += 1;
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }
        }
    }

}

class Boss extends SelectionLoop {

    private static class AcceptHandler implements SelectionKeyHandler {

        private SelectableChannelDispatcher dispatcher;

        public AcceptHandler(SelectableChannelDispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }


        @Override
        public void doRead(SelectionKey key) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void doWrite(SelectionKey key) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void doAccept(SelectionKey key) throws IOException {
            SelectableChannel channel = key.channel();
            channel.configureBlocking(false);

        }

        @Override
        public void doConnect(SelectionKey key) throws IOException {
            throw new UnsupportedOperationException();
        }

        private void dispatch(SelectableChannel channel) {
            dispatcher.dispatch(channel);
        }

    }

    private final Worker[] workers;

    private final SelectableChannelDispatcher dispatcher;

    private static final int PORT = 9090;

    public Boss() {

        workers = createWorkers();
        dispatcher = new SelectableChannelDispatcher(workers);
        AcceptHandler acceptHandler = new AcceptHandler(dispatcher);
        super.setHandler(acceptHandler);
    }

    protected void init() throws IOException {
        super.init();
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(PORT));
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private Worker[] createWorkers() {

        int nWorkers = 5;
        Worker[] workers = new Worker[nWorkers];
        for(int i=0;i<nWorkers;i++) {
            workers[i] = new Worker();
        }

        return workers;
    }

    @Override
    public void start() {
        super.start();

        for(Worker worker: workers) {
            worker.start();
        }

    }
}


class Worker extends SelectionLoop {

    private static class RWHandler implements SelectionKeyHandler {

        @Override
        public void doRead(SelectionKey key) throws IOException {
            SocketChannel channel = (SocketChannel)key.channel();
            ByteBuffer byteBuffer = (ByteBuffer)key.attachment();
            channel.read(byteBuffer);

            byteBuffer.flip();
            System.out.println(byteBuffer.array());
        }

        @Override
        public void doWrite(SelectionKey key) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void doAccept(SelectionKey key) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void doConnect(SelectionKey key) throws IOException {
            throw new UnsupportedOperationException();
        }
    }

    public Worker() {

        RWHandler rwHandler = new RWHandler();
        super.setHandler(rwHandler);
    }

    public void register(SelectableChannel channel, int op) throws ClosedChannelException {
        channel.register(this.selector, op);
    }

}
