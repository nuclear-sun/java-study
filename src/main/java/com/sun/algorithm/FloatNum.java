package com.sun.algorithm;

import java.util.HashMap;
import java.util.Map;

public class FloatNum {


    public static void calc2(int n, int m) {

        int s = n / m;
        int r = n % m;

        if(r == 0) {
            System.out.println(s);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(s).append(".");
        int i = sb.length() -1;

        Map<Integer, Integer> map = new HashMap<>();
        map.put(r, i);


        while (true) {

            r = r * 10;
            i ++;

            while (r < m) {
                r = r * 10;
                sb.append(0);
                i++;
            }

            s = r / m;
            r = r % m;

            sb.append(s);

            if(r == 0) {
                break;
            } else {
                if(map.containsKey(r)) {
                    int ind = map.get(r);
                    sb.insert(ind+1, '(');
                    sb.append(')');
                    break;
                } else {
                    map.put(r, i);
                }
            }
        }

        System.out.println(sb.toString());

    }

    public static void main(String[] args) {
        calc2(7, 33);
    }
}
