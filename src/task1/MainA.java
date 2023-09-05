package task1;

public class MainA {

    public static class MyThreadedCode implements Runnable {

        @Override
        public void run() {
            System.out.println("Hello world!");
        }
    }

    public static void main(String[] args) {

        Thread t = new Thread(new MyThreadedCode());
        t.start();

    }

}
