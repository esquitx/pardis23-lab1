package task2;

public class MainB {

    public static volatile int counter = 0;

    public static class MyIncrementer implements Runnable {

        public synchronized void run() {
            for (int i = 0; i < 1000000; i++) {
                counter++;
            }

        }
    }

    public static void main(String[] args) {

        int n = 4;

        // START JOB
        Thread[] threads = new Thread[n];
        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(new MyIncrementer());
            threads[i].start();
        }

        for (int i = 0; i < n; i++) {
            // Try except block to avoid InterruptedException with join method
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // END JOB

        // CHECKER
        System.out.println("Counter value: " + counter);
    }

}
