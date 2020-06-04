package com.sun.jvm;

import org.openjdk.jol.info.ClassLayout;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

public class ObjectLayout {

    private static ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) throws Exception {

        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();

        new Thread() {
            public void run() {
                Set<Integer> set = new HashSet<>();
                while (true) {
                    Integer ele = queue.poll();
                    set.add(ele);
                    if(set.size() > 1) {
                        System.out.println("Set size : " + set.size());
                        break;
                    }
                }
            }
        }.start();

        for(;;) {
            new Thread(){
                public void run() {
                    queue.offer(S.getInstance().hashCode());
                }
            }.start();
        }
    }
}

class S {
    private static S instance = null;

    private S() {}

    public static S getInstance() {
        if(instance == null) {
            synchronized (S.class) {
                if(instance == null) {
                    instance = new S();
                }
            }
        }
        return instance;
    }
}
