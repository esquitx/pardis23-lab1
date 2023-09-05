package task5;

public class Main {

    public static class Worker implements Runnable {
        private CountingSemaphore semaphore;

        public Worker(CountingSemaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                System.out.println("Thread " + Thread.currentThread().getId() + " is trying to acquire the semaphore");
                semaphore.s_wait();
                System.out.println("Thread " + Thread.currentThread().getId() + " has acquired semaphore");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();

            } finally {
                try {
                    System.out.println("Thread " + Thread.currentThread().getId() + " has released semaphore");
                    semaphore.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {

        // Test with 10 available "tools"
        int resources = 5;
        CountingSemaphore semaphore = new CountingSemaphore(resources);

        for (int i = 0; i < 10; i++) {
            new Thread(new Worker(semaphore)).start();
        }

    }
}
