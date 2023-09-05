package task6;

import java.util.Random;

public class MainB {

    private static int NUM_PHILOSOPHERS = 5;

    int x = 9;

    public static class ForkSemaphore extends CountingSemaphore {

        ForkSemaphore() {
            this.resource_count = 1;
        }
    }

    public static class Servant extends CountingSemaphore {

        Servant(int numForks) {
            this.resource_count = numForks;
        }

        synchronized void s_wait(boolean isLeft) throws InterruptedException {

            lock.lock();
            try {
                resource_count--;
                if ((resource_count < 0) || ((isLeft) && (resource_count == 1))) {
                    // In addition to additional checks, blocks fork requests if only one fork is
                    // requested and it is a left chopstick
                    availability.await();
                }
            } finally {
                lock.unlock();
                System.out.println("Available forks : " + resource_count);
            }

        }
    }

    public static class DiningPhilosopher implements Runnable {

        private Servant servant;
        private ForkSemaphore leftFork;
        private ForkSemaphore rightFork;
        private Random random = new Random();

        public DiningPhilosopher(Servant servant, ForkSemaphore leftFork, ForkSemaphore rightFork) {
            this.servant = servant;
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
            servant.s_wait(true);
            leftFork.s_wait();
            System.out
                    .println(Thread.currentThread().getName() + " has the left fork");
            servant.s_wait(false);
            rightFork.s_wait();
            System.out
                    .println(Thread.currentThread().getName() + " has the right fork");

            // Second : EAT!
            System.out.println(Thread.currentThread().getName() + " is now eating!");

            Thread.sleep(500 + random.nextInt(3000 - 500));

            // Third : leave forks (right, then left)
            System.out
                    .println(Thread.currentThread().getName() + " is now finished eating.");
            System.out
                    .println(Thread.currentThread().getName() + " is leaving the right fork");
            rightFork.signal();
            servant.signal();
            System.out
                    .println(Thread.currentThread().getName() + " is leaving the left fork");
            rightFork.signal();
            servant.signal();

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
        Servant servant = new Servant(NUM_PHILOSOPHERS);
        ForkSemaphore[] forks = new ForkSemaphore[NUM_PHILOSOPHERS];

        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            forks[i] = new ForkSemaphore();
        }

        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            ForkSemaphore leftFork = forks[i];
            ForkSemaphore rightFork = forks[(i + 1) % forks.length];

            ph[i] = new DiningPhilosopher(servant, leftFork, rightFork);

            Thread t = new Thread(ph[i], "Philosopher " + i);

            t.start();
        }
    }
}