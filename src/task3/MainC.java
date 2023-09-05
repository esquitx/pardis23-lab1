package task3;

public class MainC {

    private static volatile int sharedInt = 0;
    private static volatile boolean done = false;

    synchronized public void increment() {

        try {
            for (int i = 0; i < 1000000; i++) {
                sharedInt++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    synchronized void print() {

    };

    public static void main(String[] args) {



        Thread incrementingThread = new Thread(synchronized () ->  {

            try {

                 for (int i = 0; i < 1000000; i++) {
                sharedInt++;

            } finally
            }
            done = true;
        });

        Thread printingThread = new Thread(() -> {

            while (!done) {
            }
            System.out.println(sharedInt);

        });

        incrementingThread.start();
        printingThread.start();

    }

}
