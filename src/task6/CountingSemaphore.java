package task6;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CountingSemaphore {

    volatile int resource_count;
    private ReentrantLock lock = new ReentrantLock();
    private Condition availability = lock.newCondition();

    CountingSemaphore() {
        this.resource_count = 5;
    }

    CountingSemaphore(int resource_count) {
        this.resource_count = resource_count;

    }

    void s_wait() throws InterruptedException {

        lock.lock();
        try {
            while (resource_count == 0) {
                availability.await();
            }
            resource_count--;
            // System.out.println("Resources available: " + resource_count);

        } finally {
            lock.unlock();
        }

    }

    void signal() throws InterruptedException {

        lock.lock();
        try {
            resource_count++;
            // System.out.println("Resources available: " + resource_count);
            availability.signal();
        } finally {
            lock.unlock();
        }
    }
}