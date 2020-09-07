package com.sun.network.rpc;

import java.util.Random;

public class RequestIdGenerator {

    private static final Random rand = new Random();

    public static String next() {
        return  String.valueOf(rand.nextLong());
    }
}
