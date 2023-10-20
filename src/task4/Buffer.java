package task4;

// PACKAGES
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Buffer {
    volatile int head, tail;
    volatile boolean isOpen;
    ReentrantLock lock;
    Condition notFull, notEmpty, notOpen;
    int[] storage;

    Buffer(int N) {
        this.head = 0;
        this.tail = 0;
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
        this.isOpen = true;
        this.notOpen = lock.newCondition();
        this.storage = (int[]) new int[N];
    }

    void add(int i) throws InterruptedException, ClosedException {

        // Queing code
        lock.lock();
        try {
            // Checks if full, waits until it is not
            while (tail - head == storage.length && isOpen) {
                notFull.await();
            }

            // If closed before being full, throw exception
            if (!isOpen) {
                throw new ClosedException("Tried to add integer, but buffer is closed");
            }

            // When not full, add integer and update tail
            storage[tail % storage.length] = i;
            tail++;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    int remove() throws InterruptedException, ClosedException {

        lock.lock();
        try {

            // Wait for an element to be added while open
            while (tail == head && isOpen) {
                notEmpty.wait();
            }

            // Check if closed before removing
            if (!isOpen) {
                throw new ClosedException("Buffer is closed!");
            }

            // When not empty, retrieve integer, update head
            int i = storage[head % storage.length];
            head++;
            notFull.signal();
            return i;
        } finally {
            lock.unlock();
        }
    }

    void close() throws ClosedException {

        // Closes the storage
        lock.lock();
        try {

            // If closed, throw exception
            if (!isOpen) {
                throw new ClosedException("Close attempted, but buffer already closed!");
            }

            // Close buffer and notify threads so they are not stuck waiting
            isOpen = false;
            notFull.signalAll();
            notEmpty.signalAll();

        } finally {
            lock.unlock();
        }
    }
}
