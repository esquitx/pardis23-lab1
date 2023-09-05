package task6;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CountingSemaphore {

    volatile int resource_count;
    ReentrantLock lock = new ReentrantLock();
    Condition availability = lock.newCondition();

    CountingSemaphore() {
        this.resource_count = 5;
    }

    CountingSemaphore(int resources) {
        this.resource_count = resources;

    }

    public synchronized void s_wait() throws InterruptedException {

        try {

            if (this.resource_count <= 0)
                wait();

            resource_count--;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    synchronized void signal() throws InterruptedException {

        try {

            if (resource_count == 0)
                notify();
            resource_count++;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
