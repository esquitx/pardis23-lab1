package task2;

public class MainA {

    private static volatile int counter = 0;

    public class MyThreadedCode implements Runnable {

        public void run() {

            for (int i = 0; i < 1000000; i++) {
                counter = counter + 1;
            }

        }
    }

    public static void main(String[] args) {

        int n = 4;

        Thread[] threads = new Thread[n];
        MainA mainA = new MainA();
        MyThreadedCode ex = mainA.new MyThreadedCode();
        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(ex);
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

        System.out.println("Counter value: " + counter);

    }

}
