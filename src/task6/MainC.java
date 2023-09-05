package task6;

import java.util.Random;

public class MainC {

    private static int NUM_PHILOSOPHERS = 5;

    int x = 9;

    public static class ForkSemaphore extends CountingSemaphore {

        ForkSemaphore() {
            this.resource_count = 1;
        }
    }

    public static class DiningPhilosopher implements Runnable {

        private ForkSemaphore leftFork;
        private ForkSemaphore rightFork;
        private Random random = new Random();

        public DiningPhilosopher(ForkSemaphore leftFork, ForkSemaphore rightFork) {

            this.leftFork = leftFork;
            this.rightFork = rightFork;
        }

        synchronized void think() throws InterruptedException {
            System.out.println(Thread.currentThread().getName() + " is thinking.");
            Thread.sleep(500 + random.nextInt(6000 - 500));

        }

        synchronized void eat() throws InterruptedException {
            System.out.println(Thread.currentThread().getName() + " wants to eat.");

            // First : pick forks (left, then right)

            synchronized (leftFork) {
                System.out
                        .println(Thread.currentThread().getName() + " has the left fork");
            }
            synchronized (rightFork) {
                System.out
                        .println(Thread.currentThread().getName() + " has the right fork");
            }
            // Second : EAT!
            System.out.println(Thread.currentThread().getName() + " is now eating!");

            Thread.sleep(500 + random.nextInt(3000 - 500));

            // Third : leave forks (right, then left)
            System.out
                    .println(Thread.currentThread().getName() + " is now finished eating.");
            System.out
                    .println(Thread.currentThread().getName() + " is leaving the right fork");

            System.out
                    .println(Thread.currentThread().getName() + " is leaving the left fork");

            // Last : go back to thinking...
            System.out
                    .println(Thread.currentThread().getName() + " is back to thinking");

        }

        @Override
        public void run() {

            try {
                while (true) {
                    think();
                    eat();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

        }

    }

    public static void main(String[] args) {
        DiningPhilosopher[] ph = new DiningPhilosopher[NUM_PHILOSOPHERS];
        ForkSemaphore[] forks = new ForkSemaphore[NUM_PHILOSOPHERS];

        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            forks[i] = new ForkSemaphore();
        }

        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            ForkSemaphore leftFork = forks[i];
            ForkSemaphore rightFork = forks[(i + 1) % forks.length];

            if (i == forks.length - 1) {
                // Last philosopher switches order when picking up fork
                ph[i] = new DiningPhilosopher(rightFork, leftFork);
            } else {
                ph[i] = new DiningPhilosopher(leftFork, rightFork);
            }

            Thread t = new Thread(ph[i], "Philosopher " + i);

            t.start();
        }
    }
}