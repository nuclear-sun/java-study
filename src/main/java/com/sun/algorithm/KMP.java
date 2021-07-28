package com.sun.algorithm;

import java.util.ArrayList;
import java.util.List;

public class KMP {


    List<Integer> search(String s, String p) {

        List<Integer> results = new ArrayList<>();

        int[] next = genNext(p);
        int i = 0;
        while (i <= s.length() - p.length()) {

            int matched;
            if ((matched = equalChars(s, i, p)) == p.length()) {
                results.add(i);
                i = i + p.length();
            } else {
                if (matched == 0) {
                    i = i + 1;
                } else {
                    int k = next[matched - 1];
                    i = i + (matched - k);
                }
            }
        }

        return results;
    }

    private int equalChars(String s, int i, String p) {
        int j = 0;
        for (; j < p.length() && j + i < s.length(); j++) {
            if (s.charAt(i + j) != p.charAt(j)) return j;
        }
        return j;
    }

    int[] genNext(String p) {
        if (p == null) return null;

        int[] next = new int[p.length()];
        if (next.length < 1) {
            return next;
        }
        next[0] = 0;

        for (int i = 1; i < next.length; i++) {

            int k = next[i - 1];
            while (p.charAt(i) != p.charAt(k) && k > 0) {
                k = next[k];
            }
            if (k > 0) {
                next[i] = k + 1;
            } else {
                if (p.charAt(i) == p.charAt(k)) {
                    next[i] = 1;
                } else {
                    next[i] = 0;
                }
            }
        }

        return next;
    }

    public static void main(String[] args) {

        KMP kmp = new KMP();

        String s = "abcdabcabdddabcabdx";
        String p = "abcabd";

        //int[] next = kmp.genNext(p);

        //int i = kmp.equalChars(s, 4, p);
        //System.out.println(i);

        List<Integer> results = kmp.search(s, p);

        System.out.println(results);
    }


}
