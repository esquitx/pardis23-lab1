package task5;

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

    synchronized void s_wait() throws InterruptedException {

        lock.lock();
        try {
            this.resource_count--;
            if (this.resource_count < 0) {
                System.out.println("Waiting...");
                availability.await();
            }
        } finally {
            System.out.println("Resources available: " + resource_count);
            lock.unlock();
        }

    }

    synchronized void signal() throws InterruptedException {

        lock.lock();
        try {
            this.resource_count++;
            System.out.println("Resources available: " + this.resource_count);
            if (this.resource_count > 0) {
                availability.signal();
                System.out.println("Signalled");
            }
        } finally {
            lock.unlock();
        }
    }
}
