package task5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CountingSemaphore {

    volatile int resource_count;
    final int resources_available;
    ReentrantLock lock = new ReentrantLock();
    Condition availability = lock.newCondition();

    CountingSemaphore(int resources) {
        this.resource_count = resources;
        this.resources_available = resources;

    }

    synchronized void s_wait() throws InterruptedException {

        lock.lock();
        try {
            resource_count--;
            if (resource_count < 0) {
                availability.await();
            }
        } finally {
            lock.unlock();
        }

    }

    synchronized void signal() throws InterruptedException {

        lock.lock();
        try {
            if (resource_count < resources_available) {
                resource_count++;
            }
            if (resource_count - 1 < 0) {
                availability.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}
