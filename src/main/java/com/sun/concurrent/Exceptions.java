package com.sun.concurrent;

public class Exceptions {

    public static void main(String[] args) {

        Object lock = new Object();
        synchronized (lock) {
            throw new RuntimeException("fdjisfds");
        }

    }
}
