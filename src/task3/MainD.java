package task3;

public class MainD {

    public static class TestB {
        private static int sharedInt = 0;
        private static boolean done = false;
        private long incrementingTime = 0;
        private long printingTime = 0;

        long runTest(boolean verbose) {

            Thread incrementingThread = new Thread(() -> {
                for (int i = 0; i < 1000000; i++) {
                    sharedInt++;
                }
                done = true;
                incrementingTime = System.nanoTime();
            });

            Thread printingThread = new Thread(() -> {

                while (!done) {
                }
                System.out.println(sharedInt);
                printingTime = System.nanoTime();

            });

            incrementingThread.start();
            printingThread.start();

            if (verbose) {
                System.out.println("Ellapsed time : " + (printingTime - incrementingTime));
            }

            return printingTime - incrementingTime;
        }

    }

    public static class TestC {
        private static int sharedInt = 0;
        private static Object lock = new Object();
        private long incrementingTime = 0;
        private long printingTime = 0;

        long runTest(boolean verbose) {

            Thread incrementingThread = new Thread(() -> {
                synchronized (lock) {
                    try {
                        for (int i = 0; i < 1_000_000; i++) {
                            sharedInt++;
                        }
                        incrementingTime = System.nanoTime();

                    } finally {
                        lock.notify();
                    }
                }
            });

            Thread printingThread = new Thread(() -> {

                synchronized (lock) {
                    try {
                        lock.wait();
                        System.out.println(sharedInt);
                        printingTime = System.nanoTime();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            incrementingThread.start();
            printingThread.start();

            if (verbose) {
                System.out.println("Ellapsed time : " + (printingTime - incrementingTime));
            }

            return printingTime - incrementingTime;

        }

    }

    public static void main(String[] args) {

        // WARMUP ROUNDS
        System.out.println("Warming up... ");
        for (int i = 0; i < 10; i++) {

            // Create
            TestB testB = new TestB();
            TestC testC = new TestC();

            // Run (without outputs)
            testB.runTest(false);
            testC.runTest(false);
        }

        // TESTING
        System.out.println("Starting tests...");
        for (int i = 0; i < 30; i++) {

            // Create

            TestB testB = new TestB();
            TestC testC = new TestC();

            // Run (printing outputs)
            testB.runTest(true);
            testC.runTest(true);
        }
    }

}