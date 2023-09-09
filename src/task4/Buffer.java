package task4;

// PACKAGES
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Buffer {
    int head, tail;
    boolean isOpen;
    ReentrantLock lock;
    Condition notFull, notEmpty;
    int[] storage;

    Buffer(int N) {
        head = 0;
        tail = 0;
        isOpen = true;
        lock = new ReentrantLock();
        notFull = lock.newCondition();
        notEmpty = lock.newCondition();
        storage = (int[]) new int[N];
    }

    void add(int i) throws InterruptedException, ClosedException {

        // Check if closed
        if (!isOpen) {
            throw new ClosedException();
        }

        // Queing code
        lock.lock();
        try {

            // Checks if full, waits until it is not
            if (tail - head == storage.length) {
                notFull.await();
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

            // Check if closed AND empty
            if ((tail == head) && !isOpen) {
                throw new ClosedException("The buffer is closed and empty");
            }
            // Checks if empty, waits until it is not
            if (tail == head) {
                notEmpty.await();
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
        if (!isOpen) {
            isOpen = false;
        } else {
            throw new ClosedException();
        }
    }
}
