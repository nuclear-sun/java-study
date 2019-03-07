package com.sun.jvm;

/**
 * java -client -Xms20m -Xmx20m -Xmn10m -XX:SurvivorRatio=8 -XX:+UseSerialGC \
 *    -XX:PretenureSizeThreshold=3145728 -XX:+PrintGCDetails PretenureSizeThreshold
 * Verify that object 'array' is allocated on Eden space by default, on Tenured generation
 *    after setting PretenureSizeThreshold to 3M
 */
public class PretenureSizeThreshold {
    private static final int _1M = 1024 * 1024;

    public static void main(String[] args) {
        byte[] array = new byte[4 * _1M];
    }
}
