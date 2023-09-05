package task2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;

public class MainC {

    public static volatile int counter = 0;

    public static class MyIncrementer implements Runnable {

        public synchronized void run() {
            for (int i = 0; i < 1000000; i++) {
                counter++;
            }

        }
    }

    static long run_experiment(int n) {

        long startTime = System.nanoTime();
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
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public static void main(String[] args) {

        int n = 1;
        int X = 10;
        int Y = 20;
        long mean;
        long st_dev;

        for (int k = 0; k < 6; k++) {

            // WARMUP PHASE
            for (int i = 0; i < X; i++) {
                run_experiment(n);
            }

            // MEASUREMENT PHASE
            long[] results = new long[Y];
            long total = 0;
            for (int i = 0; i < Y; i++) {
                long result = run_experiment(n);
                results[i] = result;
                total = total + result;
            }

            // Update value of n for future loop
            n = 2 * n;

            // MEAN
            mean = total / results.length;

            // ST DEV
            long sum = 0;
            for (long res : results) {
                sum += Math.pow(res - mean, 2);
            }
            st_dev = sum / results.length;

            try {

                BufferedWriter writer = new BufferedWriter(new FileWriter("data/results.dat", true));
                writer.write(n + " ");
                writer.write(mean + " ");
                writer.write(st_dev + " ");
                writer.newLine();
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
