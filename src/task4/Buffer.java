package task4;

// PACKAGES
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Buffer {
    int head, tail;
    boolean isOpen;
    Lock lock;
    Condition notFull, notEmpty, notOpen;
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
            if (tail - head == storage.length) {
                notFull.await();
            }
            storage[tail % storage.length] = i;
            tail++;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    int remove() throws InterruptedException {

        lock.lock();
        try {
            if (tail == head)
                notEmpty.await();
            int i = storage[head % storage.length];
            head++;
            notFull.signal();
            return i;
        } finally {
            lock.unlock();
        }
    }

    void close() throws ClosedException {
        if (!isOpen) {
            isOpen = false;
        } else {
            throw new ClosedException();
        }
    }
}
