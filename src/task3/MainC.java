package task3;

public class MainC {

    private static volatile int sharedInt = 0;
    private static boolean done = false;
    private static final Object lock = new Object();

    public static void main(String[] args) {

        Thread incrementingThread = new Thread(() -> {

            synchronized (lock) {
                for (int i = 0; i < 1_000_000; i++) {
                    sharedInt++;
                }
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
                    System.out.println(sharedInt);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        incrementingThread.start();
        printingThread.start();

    }

}
