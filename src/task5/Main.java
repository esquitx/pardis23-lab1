package task5;

import java.util.Random;

public class Main {

    static Random random = new Random();

    public static class Worker implements Runnable {
        private CountingSemaphore semaphore;

        public Worker(CountingSemaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {

            try {
                while (true) {
                    Thread.sleep(random.nextInt(2_000));

                    System.out.println(
                            Thread.currentThread().getName() + " is trying to acquire the semaphore");
                    this.semaphore.s_wait();
                    System.out.println(Thread.currentThread().getName() + " has acquired the semaphore");
                    Thread.sleep(random.nextInt(5_000));
                    this.semaphore.signal();

                    System.out.println(Thread.currentThread().getName() + " has released the semaphore");

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

        }

        public static void main(String[] args) {

            // Test with 10 available "tools"
            int resources = 5;
            CountingSemaphore semaphore = new CountingSemaphore(resources);

            for (int i = 0; i < 10; i++) {
                new Thread(new Worker(semaphore), "Thread " + (i + 1)).start();
            }

        }
    }
}
