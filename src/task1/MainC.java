package task1;

public class MainC {

    public static class MyThreadedCode implements Runnable {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " says: Hello World!");
        }
    }

    public static void main(String[] args) {

        int nthreads = 5;
        Thread[] threads = new Thread[nthreads];
        for (int i = 0; i < nthreads; i++) {
            threads[i] = new Thread(new MyThreadedCode(), "Thread " + (i + 1));
            threads[i].start();
        }

        for (int i = 0; i < nthreads; i++) {

            // Try except block to avoid InterruptedException with join method
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Goodbye!");
    }
}
