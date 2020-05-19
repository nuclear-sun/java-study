package org.apache.pig;

public class Main {

    //private static Thread backendThread = null;

    public static void main(String[] args) {

        Thread backendThread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {

                    }

                    System.out.println("hello");
                }
            }
        };

        backendThread.setDaemon(true);

        backendThread.start();

        System.out.println("Main function exit.");

    }
}
