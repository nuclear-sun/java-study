package com.sun.initialization;

public class Main {
    public static void main(String[] args) {
        Base obj = new Concreate();
        System.out.println(obj.name);
    }

}

abstract class Base {
    public String name;

    public Base() {
        this.name = createName();
    }

    protected abstract String createName();
}

class Concreate extends Base {

    private String subName = "zink";

    public String createName() {
        // cause `name` in base is null
        return subName;
    }
}
