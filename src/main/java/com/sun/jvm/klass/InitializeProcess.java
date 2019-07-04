package com.sun.jvm.klass;


public class InitializeProcess {
    static {
        i = 0;
        //System.out.println(i); // compile error
    }
    static int i;

    static public void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread() + " before init");
                BrokenInitializer a = new BrokenInitializer();
                System.out.println(Thread.currentThread()  + " after init");
            }
        };

        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
    }
}

class BrokenInitializer {
    static {
        if (true) {
            System.out.println(Thread.currentThread() + " start init BrokenInitializer");
            while (true) {
            }
        }
    }
}


