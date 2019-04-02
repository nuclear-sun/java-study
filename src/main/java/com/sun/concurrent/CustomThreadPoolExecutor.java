package com.sun.concurrent;

import java.util.concurrent.*;

public class CustomThreadPoolExecutor {

    public static void main(String[] args) {

        RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println(r.toString() + " rejected");
            }
        };

        ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 4, 20,
                TimeUnit.SECONDS, new SynchronousQueue<>(), rejectedExecutionHandler);
        executor.prestartAllCoreThreads();

        for (int i = 0; i < 30; i++) {
            //Runnable runnable = new MyRunnable(String.valueOf(i));
            Callable<Integer> callable = new MyCallable(String.valueOf(i));
            //executor.execute(runnable);
            executor.submit(callable);
        }
        executor.shutdown();
    }

    private static class MyRunnable implements Runnable {

        private String name;

        public MyRunnable(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "[" + this.name + "]";
        }
    }

    private static class MyCallable implements Callable<Integer> {
        private String name;

        public MyCallable(String name) {
            this.name = name;
        }

        @Override
        public Integer call() {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return name.length();
        }

        public String toString() {
            return "[" + name + "]";
        }
    }
}



