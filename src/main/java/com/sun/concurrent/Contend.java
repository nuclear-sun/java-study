package com.sun.concurrent;


import sun.misc.Contended;

class A {

    volatile long f1 = 0;
    volatile long f2 = 0;

}

class B {

    @Contended("g1")
    volatile long f1 = 0;
    @Contended("g1")
    volatile long f2 = 0;
}

class C {

    volatile long f1 = 0;
    long p1, p2, p3, p4, p5, p6, p7;
    volatile long f2 = 0;
}



public class Contend {

    private static final A a = new A();

    private static final B b = new B();

    private static final C c = new C();


    private static void testA() throws InterruptedException {
        Thread t1 = new Thread() {
            public void run() {
                for(int i=0; i<10000_0000L; i++){
                    a.f1 = i;
                }
            }
        };

        Thread t2 = new Thread() {
            public void run() {
                for (int i=0; i<10000_0000L; i++) {
                    a.f2 = i;
                }
            }
        };

        long start = System.nanoTime();
        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println(System.nanoTime() - start);
    }

    public static void testB() throws InterruptedException {

        Thread t1 = new Thread() {
            public void run() {
                for(int i=0; i<10000_0000L; i++){
                    b.f1 = i;
                }
            }
        };

        Thread t2 = new Thread() {
            public void run() {
                for (int i=0; i<10000_0000L; i++) {
                    b.f2 = i;
                }
            }
        };

        long start = System.nanoTime();
        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println(System.nanoTime() - start);
    }


    public static void testC() throws InterruptedException {

        Thread t1 = new Thread() {
            public void run() {
                for(int i=0; i<10000_0000L; i++){
                    c.f1 = i;
                }
            }
        };

        Thread t2 = new Thread() {
            public void run() {
                for (int i=0; i<10000_0000L; i++) {
                    c.f2 = i;
                }
            }
        };

        long start = System.nanoTime();
        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println(System.nanoTime() - start);
    }

    public static void main(String[] args) throws InterruptedException {
        testA();
        testB();
        testC(); // outperform
    }
}
