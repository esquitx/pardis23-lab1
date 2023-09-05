package task3;

public class MainD {

    private static volatile int sharedInt = 0;
    private static final Object lock = new Object();

    public static void main(String[] args) {

        Thread incrementingThread = new Thread(() -> {
            synchronized (lock) {
                try {
                    for (int i = 0; i < 1_000_000; i++) {
                        sharedInt++;
                        System.out.println(sharedInt);
                    }

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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        incrementingThread.start();
        printingThread.start();

    }

}
