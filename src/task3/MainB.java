package task3;

public class MainB {

    private static volatile int sharedInt = 0;
    private static volatile boolean done = false;

    public static void main(String[] args) {

        Thread incrementingThread = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                sharedInt++;
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
