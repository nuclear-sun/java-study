package com.sun.algorithm;

public class SegmentTree {

    private static int[] origin;
    private static int[] seg;
    private static int offset;

    static {
        origin = new int[4];
        for(int i=0;i<origin.length; i++) {
            origin[i] = i+1;
        }
        seg = new int[16];

        int n = 1;
        while ((n= n << 1) > seg.length - 1);
        offset = n / 2;
    }

    static void build(int node, int left, int right) {
        if(left == right) {
            seg[left] = origin[left - offset];
            return;
        }

    }


    static void build(int[] ret, int[] origin, int left, int right) {
        if(left == right) {
            ret[left] = origin[left-1];
            return;
        }


    }

}
