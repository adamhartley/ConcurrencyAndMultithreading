package org.concurrencyandmultithreading.parallelfunctions;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ThreadAndThreadPool {

    private static final Runnable HELLO_TASK = () -> System.out.printf("Hello from %s \n", Thread.currentThread().getName());

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //        exCreatingThreadsWithThreadPool();
        //        exCreatingThreadsWithScheduledExecutorService();
        exCreatingThreadsWithThreadFactory();
    }

    public static void exCreatingThreadsWithThreadPool() throws ExecutionException, InterruptedException {

        // run the helloTask from a thread pool
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            pool.submit(HELLO_TASK);
        }
        // Submitting a Callable to the thead pool returns a Future
        Future<Integer> randomNumber = pool.submit(() -> new Random().nextInt());
        System.out.printf("Random number: %d \n", randomNumber.get());
        pool.shutdown(); // make sure to shutdown the pool when it is no longer needed
    }

    public static void exCreatingThreadsWithScheduledExecutorService() throws InterruptedException {
        System.out.println("Example creating threads with ScheduledExecutorService");
        // Scheduled thread pool
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);

        ScheduledFuture<?> waterReminder = scheduledExecutorService.scheduleAtFixedRate(
                () -> System.out.println("REMINDER: Time to drink a glass of water"),
                0, 1, TimeUnit.SECONDS);

        ScheduledFuture<?> exerciseReminder = scheduledExecutorService.scheduleAtFixedRate(
                () -> System.out.println("REMINDER: Time to exercise!"),
                0, 12, TimeUnit.SECONDS);

        // to cancel the tasks after certain amount of time
        Runnable canceller = () -> {
            exerciseReminder.cancel(false);
            waterReminder.cancel(false);
        };
        scheduledExecutorService.schedule(canceller, 15, TimeUnit.SECONDS);
    }

    public static void exCreatingThreadsWithThreadFactory() {
        ThreadFactory threadFactory = r -> {
            Thread thread = new Thread(r);
            thread.setPriority(Thread.MAX_PRIORITY);
            return thread;
        };

        ExecutorService pool = Executors.newFixedThreadPool(5, threadFactory);
        for (int i = 0; i < 10; i++) {
            pool.submit(HELLO_TASK);
        }
        pool.shutdown();
    }
}
