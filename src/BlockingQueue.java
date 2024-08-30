import java.util.ArrayDeque;
import java.util.Deque;

public class BlockingQueue<E> {
    private final Deque<E> queue = new ArrayDeque<>();
    private final int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void enqueue(E e) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }
        queue.add(e);
        notifyAll();
    }

    public synchronized E dequeue() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        E e = queue.remove();
        notifyAll();
        return e;
    }

    public synchronized int size() {
        return queue.size();
    }

    public static void main(String[] args) {
        BlockingQueue<Integer> blockingQueue = new BlockingQueue<>(5);

        Runnable producer = () -> {
            try {
                for (int i = 0; i < 10; i++) {
                    blockingQueue.enqueue(i);
                    System.out.println(i);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable consumer = () -> {
            try {
                for (int i = 0; i < 10; i++) {
                    int item = blockingQueue.dequeue();
                    System.out.println("Element " + item);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();
    }
}