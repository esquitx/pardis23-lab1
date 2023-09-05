package task4;

public class Main {

    public static Buffer buffer;

    public static class Producer implements Runnable {
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                try {
                    buffer.add(i);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ClosedException e) {
                    System.out.println("Buffer is closed!");
                    return;
                }

            }

            try {
                buffer.close();
            } catch (ClosedException e) {
                // close thread
                System.out.println("Buffer is closed!");
                return;
            }

        }
    }

    public static class Consumer implements Runnable {

        public void run() {

            while (buffer.isOpen) {
                try {
                    int i = buffer.remove();
                    // System.out.println(i + " removed from buffer");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return;
        }
    }

    public static void main(String[] args) {

        buffer = new Buffer(Integer.parseInt(args[0]));

        System.out.println("Buffer started with " + Integer.parseInt(args[0]) + " threads");

        Thread producer = new Thread(new Producer());
        Thread consumer = new Thread(new Consumer());
        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
