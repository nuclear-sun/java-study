package com.sun.network.rpc.user;

import java.util.List;

public class StrCaterImpl implements StrCater {

    private static final String DELIMITER = ":";

    @Override
    public String cat(List<String> list) {

        StringBuilder sb = new StringBuilder();
        for(String item: list) {
            sb.append(item).append(DELIMITER);
        }
        return sb.toString();
    }
}
