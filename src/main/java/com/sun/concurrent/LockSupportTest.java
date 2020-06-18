package com.sun.concurrent;

import java.util.concurrent.locks.LockSupport;

public class LockSupportTest {

    public static void main(String[] args) throws InterruptedException {
        MyThread thread = new MyThread("t1");

        thread.start();

        Thread.sleep(3000);
        thread.interrupt();

        System.out.println("Main end.");

    }
}

class MyThread extends Thread {

    public MyThread(String name) {
        super(name);
    }

    public void run() {
        System.out.println("Thread started: " + getName());
        LockSupport.park();
        if(Thread.currentThread().isInterrupted()) {
            System.out.println(getName() + " is interrupted");
        }

        System.out.println(getName() + " end.");
    }
}
