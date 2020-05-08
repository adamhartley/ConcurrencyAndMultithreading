package org.concurrencyandmultithreading.forkjoinpool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/*
 * When a task does not return a result, RecursiveAction is preferred
 */
public class PickApplesWithRecursiveAction {

    public static class PickApplesAction extends RecursiveAction {
        private final AppleTree[] appleTrees;
        private final int startInclusive;
        private final int endInclusive;

        private final int taskThreshold = 4;

        public PickApplesAction(final AppleTree[] appleTrees, final int startInclusive, final int endInclusive) {
            this.appleTrees = appleTrees;
            this.startInclusive = startInclusive;
            this.endInclusive = endInclusive;
        }

        protected Integer doCompute() {
            return IntStream.rangeClosed(startInclusive, endInclusive)
                    .map(i -> appleTrees[i].pickApples())
                    .sum();
        }

        @Override protected void compute() {
            // if the current task is small enough, execute the task immediately
            if (endInclusive - startInclusive < taskThreshold) {
                doCompute();
                return;
            }

            int midpoint = startInclusive + (endInclusive - startInclusive)/2;
            // split the work into two subtasks
            PickApplesAction leftSubtask = new PickApplesAction(appleTrees, startInclusive, midpoint);
            PickApplesAction rightSubtask = new PickApplesAction(appleTrees, midpoint + 1, endInclusive);

            /*
             * Delegate one subtask asynchronously using fork(), and continue working on the other synchronously using compute()
             *      compute() - execution waits here until completion
             *      join() - will wait until the right subtask is done
             * The order in which these are called is important!!!
             */
            rightSubtask.fork(); // computed asynchronously
            leftSubtask.compute(); // computed synchronously: immediately and in the current thread
            rightSubtask.join(); // although join returns null in RecursiveAction, we still need it -- it waits until the task is complete
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AppleTree[] appleTrees = AppleTree.newTreeGarden(12);
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        long startTime = System.currentTimeMillis();

        PickApplesAction task = new PickApplesAction(appleTrees, 0, appleTrees.length - 1);
        forkJoinPool.execute(task);
        forkJoinPool.awaitTermination(10, TimeUnit.SECONDS);

        long endTime = System.currentTimeMillis();
        System.out.println("\nDone in " + (endTime - startTime));
    }
}
