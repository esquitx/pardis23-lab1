package task2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MainC {

    private static volatile int counter = 0;
    private static boolean isPDC = false;

    public static class MyIncrementerThread extends Thread {

        synchronized static void incrementer() {

            for (int i = 0; i < 1_000_000; i++) {
                counter++;
            }

        }

        public void run() {
            incrementer();
        }

    }

    static long run_experiment(int n) {

        long startTime = System.nanoTime();

        // START JOB
        Thread[] threads = new Thread[n];
        for (int i = 0; i < n; i++) {
            threads[i] = new MyIncrementerThread();
            threads[i].start();
        }

        // JOIN JOBS
        for (int i = 0; i < n; i++) {
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

    static void writer(boolean isPDC, int n, double mean, double stdev) throws IOException {

        if (isPDC) {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/resultsPDC.dat", true));
            writer.write(n + " ");
            writer.write(mean + " ");
            writer.write(stdev + " ");
            writer.newLine();
            writer.flush();
            writer.close();
        } else {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/resultslocal.dat", true));
            writer.write(n + " ");
            writer.write(mean + " ");
            writer.write(stdev + " ");
            writer.newLine();
            writer.flush();
            writer.close();

        }
    }

    public static void main(String[] args) {

        if (args.length > 0) {
            isPDC = Boolean.parseBoolean(args[0]);
        }

        int X = 25;
        int Y = 50;
        double mean;
        double st_dev;

        int[] threadArray = new int[] { 1, 2, 4, 8, 16, 32, 64 };
        for (int n : threadArray) {

            // WARMUP PHASE
            for (int i = 0; i < X; i++) {
                run_experiment(n);
            }

            // MEASUREMENT PHASE
            long[] results = new long[Y];
            double sum = 0.0;
            for (int i = 0; i < Y; i++) {
                long result = run_experiment(n);
                results[i] = result;
                sum += result;
            }
            // MEAN
            mean = (sum / results.length);

            // System.out.println("" + mean);

            // ST DEV
            double devSum = 0.0;
            for (long res : results) {
                devSum += Math.pow(res - mean, 2);
            }
            st_dev = Math.sqrt(devSum / results.length);

            // System.out.println("" + stdev);

            try {
                writer(isPDC, n, mean, st_dev);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
