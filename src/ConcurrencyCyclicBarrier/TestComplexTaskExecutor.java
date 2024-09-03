package ConcurrencyCyclicBarrier;

import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

class ComplexTask {
    private final int taskId;
    private int result;

    public ComplexTask(int taskId) {
        this.taskId = taskId;
    }

    public void execute() {
        result = taskId * 2;
        System.out.println("Task " + taskId + " executed with result: " + result);
    }

    public int getResult() {
        return result;
    }
}

class ComplexTaskExecutor {
    private final int numberOfTasks;
    private final List<Integer> results;

    public ComplexTaskExecutor(int numberOfTasks) {
        this.numberOfTasks = numberOfTasks;
        this.results = new ArrayList<>();
    }

    public void executeTasks(int numberOfTasks) {
        CyclicBarrier barrier = new CyclicBarrier(numberOfTasks, () -> {
            System.out.println("All tasks completed. Combining results.");
            combineResults();
        });
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfTasks);

        try {
            for (int i = 1; i <= numberOfTasks; i++) {
                int taskId = i;
                executorService.submit(() -> {
                    ComplexTask task = new ComplexTask(taskId);
                    task.execute();
                    results.add(task.getResult());
                    try {
                        barrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                });
            }
        } finally {
            executorService.shutdown();
        }
    }

    private void combineResults() {
        int combinedResult = results.stream().mapToInt(Integer::intValue).sum();
        System.out.println(Thread.currentThread().getName() + " completed. Combined result: " + combinedResult);
    }
}

public class TestComplexTaskExecutor {

    public static void main(String[] args) {
        Runnable testRunnable = () -> {
            ComplexTaskExecutor taskExecutor = new ComplexTaskExecutor(5);

            System.out.println(Thread.currentThread().getName() + " started the test.");

            taskExecutor.executeTasks(5);

            System.out.println(Thread.currentThread().getName() + " completed the test.");
        };

        Thread thread1 = new Thread(testRunnable, "TestThread-1");
        Thread thread2 = new Thread(testRunnable, "TestThread-2");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
