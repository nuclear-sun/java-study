package com.sun.concurrent;

import org.testng.annotations.Test;

import java.util.concurrent.locks.LockSupport;

public class LockSupportTest {

    public static void main(String[] args) throws InterruptedException {


        //new OneTwo().test();
        new OneTwoUsingCondition().test();
/*

        MyThread thread = new MyThread("t1");

        thread.start();

        Thread.sleep(3000);
        thread.interrupt();
*/

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
        if (Thread.currentThread().isInterrupted()) {
            System.out.println(getName() + " is interrupted");
        }

        System.out.println(getName() + " end.");
    }
}

/**
 * 两个线程交替打印 0-100 正解：
 */
class OneTwoUsingLockSupport {

    @Test
    public void test() {
        RoundThread t1;
        RoundThread t2;

        t1 = new RoundThread() {
            public void run() {
                for (int i = 0; i <= 100; i += 2) {

                    System.out.println(getName() + ":" + i);
                    LockSupport.unpark(other);
                    if (i == 100) {
                        break;
                    }
                    LockSupport.park();
                }
            }
        };
        t2 = new RoundThread() {
            public void run() {
                for (int i = 1; i <= 99; i += 2) {
                    LockSupport.park();
                    System.out.println(getName() + ":" + i);
                    LockSupport.unpark(other);
                }
            }
        };

        t1.setOther(t2);
        t2.setOther(t1);

        t2.start();
        t1.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class RoundThread extends Thread {

    protected Thread other;

    public void setOther(Thread thread) {
        this.other = thread;
    }

}

/**
 * 先通知后等待，将导致通知丢失！！！ 下面是错误的解法
 */
class OneTwoUsingCondition {

    public void test() {

        RoundThread t1;
        RoundThread t2;

        t1 = new RoundThread() {

            public void run() {
                for (int i = 0; i <= 100; i += 2) {
                    System.out.println(getName() + ":" + i);
                    synchronized (other) {
                        other.notifyAll();
                    }
                    if (i == 100) {
                        break;
                    }
                    synchronized (this) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        t2 = new RoundThread() {

            public void run() {

                for (int i = 1; i <= 99; i += 2) {
                    synchronized (this) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println(getName() + ":" + i);
                    synchronized (other) {
                        other.notifyAll();
                    }
                }
            }
        };

        t1.setOther(t2);
        t2.setOther(t1);

        t2.start();
        t1.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}