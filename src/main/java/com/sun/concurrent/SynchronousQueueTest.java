package com.sun.concurrent;

import java.util.concurrent.Semaphore;

public class SynchronousQueueTest {

    public static void main(String[] args) {
        final NaiveSynchronousQueue<String> queue = new NaiveSynchronousQueue<>();

        new Thread(){
            public void run() {
                try {
                    System.out.println("put at " + System.currentTimeMillis());
                    queue.put("sun");
                    System.out.println("put complete at " + System.currentTimeMillis());
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread(){
            public void run() {
                try {
                    Thread.sleep(2000);
                    System.out.println("take at " + System.currentTimeMillis());
                    String s = queue.take();
                    System.out.println("take complete at " + System.currentTimeMillis());
                    System.out.println(s);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

class NaiveSynchronousQueue<E> {
    private boolean putting = false;
    private E item = null;

    /**
     * Two kinds of threads needs managing:
     * 1. put-take and vice verser
     * 2. put-put
     */


    /**
     * 1. thread-safe
     * 2. block until take
     * @param e
     */
    public synchronized void put(E e) throws InterruptedException{
        if (e==null) return;
        while (putting) {
            wait();
        }
        item = e;
        e = null;
        notifyAll();
        while (item != null) {
            wait();
        }
        putting = false;
        notifyAll();//notify other putting threads
    }

    public synchronized E take() throws InterruptedException{
        while (item == null) {
            wait();
        }
        E e = item;
        item = null;
        notifyAll();
        return e;
    }
}

// TODO
class SemphoreSynchronousQueue<E> {
    E item = null;

    Semaphore sync = new Semaphore(0);
    Semaphore send = new Semaphore(1);
    Semaphore recv = new Semaphore(0);

    public void put(E e) throws InterruptedException{
        sync.acquire();
        item = e;
        e = null;
        recv.release();

    }

    public E take() throws InterruptedException {
        sync.release();
        recv.acquire();
        E e = item;
        item = null;

        return e;
    }
}
