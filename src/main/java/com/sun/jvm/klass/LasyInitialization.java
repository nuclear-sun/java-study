package com.sun.jvm.klass;

public class LasyInitialization {

    public static void main(String[] args) {
        Tool.sayHi();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Tool.cut(new Object());
    }

}

class Tool {

    private static class PrinterHolder {
        private final static StdoutPrinter printer = new StdoutPrinter();
    }

    public static void sayHi() {
        System.out.println("hello");
    }

    public static void cut(Object object) {
        PrinterHolder.printer.print(object);
    }
}

class StdoutPrinter {

    public StdoutPrinter() {
        System.out.println("Class init");
    }

    void print(Object object) {
        System.out.println(object);
    }
}