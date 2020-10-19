package com.sun.algorithm.leetcode;

public class LC_53_MaxSubArray {

    public int maxSubArray(int[] nums) {

        int global = nums[0];
        int local = nums[0];

        for(int i = 1; i< nums.length; i++) {
            local = Math.max(nums[i], local + nums[i]);
            global = Math.max(local, global);
        }

        return global;

    }
}
