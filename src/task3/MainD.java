package task3;

public class MainD {

    public static class TestB {
        private int sharedInt = 0;
        private boolean done = false;
        private long incrementingTime = 0;
        private long printingTime = 0;

        TestB() {
            this.sharedInt = 0;
        }

        long runTest(boolean verbose) throws InterruptedException {

            Thread incrementingThread = new Thread(() -> {
                for (int i = 0; i < 1000000; i++) {
                    sharedInt++;
                }
                done = true;
                incrementingTime = System.nanoTime();

                return;
            });

            Thread printingThread = new Thread(() -> {

                while (!done) {
                }
                System.out.println("Test B prints : ");
                System.out.println(sharedInt);
                printingTime = System.nanoTime();

                return;

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

        private int sharedInt;
        private Object lock = new Object();
        private long incrementingTime;
        private long printingTime;

        TestC() {
            this.sharedInt = 0;
        }

        long runTest(boolean verbose) throws InterruptedException {

            Thread incrementingThread = new Thread(() -> {
                synchronized (lock) {
                    try {
                        for (int i = 0; i < 1_000_000; i++) {
                            this.sharedInt++;
                        }
                        incrementingTime = System.nanoTime();

                    } finally {
                        lock.notify();
                        return;

                    }
                }
            });

            Thread printingThread = new Thread(() -> {

                synchronized (lock) {
                    try {
                        lock.wait();
                        printingTime = System.nanoTime();
                        System.out.println("Test C prints : ");
                        System.out.println(sharedInt);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        return;
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

        System.out.println("Starting tests for task 3b");
        // WARMUP ROUNDS
        System.out.println("Warming up for test B... ");
        for (int i = 0; i < 10; i++) {

            // Create
            TestB testB = new TestB();

            // Run (without outputs)
            try {
                testB.runTest(false);
            } catch (InterruptedException e) {
                return;
            }
        }

        // TESTING
        System.out.println("Starting tests...");
        for (int i = 0; i < 30; i++) {

            System.out.println("Run " + (i + 1) + "....");
            // Create
            TestB testB = new TestB();

            // Run (printing outputs)
            try {
                testB.runTest(true);
            } catch (InterruptedException e) {
                return;

            }

            System.out.println("--------------");

        }

        System.out.println("");
        System.out.println("Starting tests for task 3c");
        // WARMUP ROUNDS
        System.out.println("Warming up for test C... ");
        for (int i = 0; i < 10; i++) {

            // Create
            TestC testC = new TestC();

            // Run (without outputs)
            try {
                testC.runTest(false);
            } catch (InterruptedException e) {
                return;
            }
        }

        // TESTING
        System.out.println("Starting tests...");
        for (int i = 0; i < 30; i++) {

            System.out.println("Run " + (i + 1) + "....");
            // Create
            TestC testC = new TestC();

            // Run (printing outputs)
            try {
                testC.runTest(true);
            } catch (InterruptedException e) {
                return;
            }

            System.out.println("--------------");
        }

    }

}