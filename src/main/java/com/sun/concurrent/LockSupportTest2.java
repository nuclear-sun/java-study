package com.sun.concurrent;

import java.util.concurrent.locks.LockSupport;

public class LockSupportTest2 {

    private static final Object obj = new Object();

    public static void main(String[] args) {

        Thread t1 = new Thread() {
            public void run() {
                LockSupport.park(obj);
                System.out.println("wake up");
                System.out.println("interrupted? " + Thread.interrupted());
            }
        };

        t1.start();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //t1.interrupt();
        LockSupport.unpark(t1);

        System.out.println("Main end.");
    }
}
