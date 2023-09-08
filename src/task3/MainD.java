package task3;

public class MainD {

    static class TestB {

        private volatile int sharedInt;
        private volatile boolean done;
        private long printingTime;
        private long incrementingTime;

        void test(boolean verbose) {

            sharedInt = 0;
            done = false;

            Thread incrementingThread = new Thread(() -> {
                for (int i = 0; i < 1000000; i++) {
                    sharedInt++;
                }
                incrementingTime = System.nanoTime();
                done = true;
            });

            Thread printingThread = new Thread(() -> {

                while (!done) {
                }
                printingTime = System.nanoTime();
                System.out.println(sharedInt);

            });

            incrementingThread.start();
            printingThread.start();

            if (verbose) {
                System.out.println("Ellapsed time: " + (printingTime - incrementingTime));
                System.out.println("------------------------");
            }
        }
    }

    static class TestC {

        private volatile int sharedInt = 0;
        private boolean done = false;
        private Object lock = new Object();
        private long printingTime;
        private long incrementingTime;

        void test(boolean verbose) {

            Thread incrementingThread = new Thread(() -> {

                synchronized (lock) {
                    for (int i = 0; i < 1_000_000; i++) {
                        sharedInt++;
                    }
                    incrementingTime = System.nanoTime();
                    done = true;
                    lock.notify();
                }

            });

            Thread printingThread = new Thread(() -> {

                try {
                    synchronized (lock) {
                        while (!done) {
                            lock.wait();
                        }
                        printingTime = System.nanoTime();
                        System.out.println(sharedInt);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });

            incrementingThread.start();
            printingThread.start();

            if (verbose) {
                System.out.println("Ellapsed time: " + (printingTime - incrementingTime));
                System.out.println("------------------------");
            }

        }
    }

    public static void main(String[] args) {

        // TEST 3b
        System.out.println("Starting tests for C...");

        // Warmup
        System.out.println("First a warm up");

        for (int i = 0; i < 10; i++) {
            TestB test = new TestB();
            test.test(false);
        }

        // Tests
        System.out.println("Now the tests for B...");
        for (int i = 0; i < 30; i++) {
            TestB test = new TestB();
            test.test(true);
        }

        // TEST 3c
        System.out.println("Starting tests for C...");

        // Warmup
        System.out.println("First a warm up");
        for (int i = 0; i < 10; i++) {
            TestC test = new TestC();
            test.test(false);
        }

        // Tests
        System.out.println("Now the tests for C...");

        for (int i = 0; i < 10; i++) {
            TestC test = new TestC();
            test.test(false);
        }

    }

}
