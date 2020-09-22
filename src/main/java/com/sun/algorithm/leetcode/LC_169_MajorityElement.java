package com.sun.algorithm.leetcode;

public class LC_169_MajorityElement {


    public static int majorityElement(int[] nums) {

        int t=0,c=0;

        for (int num : nums) {
            if(c == 0) {
                t = num;
                c++;
            } else {
                if(num == t) {
                    c++;
                } else {
                    c--;
                }
            }
        }
        return t;
    }

    public static void main(String[] args) {
        int[] data = new int[] {3,3,4};
        int result = majorityElement(data);
        System.out.println(result);
    }


}
