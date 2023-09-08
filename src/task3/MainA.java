package task3;

public class MainA {

    private static volatile int sharedInt = 0;

    public static void main(String[] args) {

        Thread incrementingThread = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) {
                sharedInt++;
            }
        });

        Thread printingThread = new Thread(() -> {
            System.out.println(sharedInt);
        });

        incrementingThread.start();
        printingThread.start();

    }

}
