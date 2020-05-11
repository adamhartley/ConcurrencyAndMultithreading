package org.concurrencyandmultithreading.forkjoinpool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class PickApplesRecursiveTaskHandleExceptions {

    public static final int NUMBER_OF_TREES = 12;

    public static class PickApplesTask extends RecursiveTask<Integer> {
        private final AppleTree[] appleTrees;
        private final int startInclusive;
        private final int endInclusive;

        private final int taskThreshold = 4;

        public PickApplesTask(final AppleTree[] appleTrees, final int startInclusive, final int endInclusive) {
            this.appleTrees = appleTrees;
            this.startInclusive = startInclusive;
            this.endInclusive = endInclusive;
        }

        protected Integer doCompute() {
            return IntStream.rangeClosed(startInclusive, endInclusive)
                    .map(i -> appleTrees[i].pickApples())
                    .sum();
        }

        @Override protected Integer compute() {
           // throw an exception for any task from the right side of the array
            if (startInclusive >= NUMBER_OF_TREES / 2) {
                // instead of throwing and exception, feed it to completeExceptionally()
                completeExceptionally(new SomethingWentWrongException());
            }

            // if the current task is small enough, execute the task immediately
            if (endInclusive - startInclusive < taskThreshold) {
                return doCompute();
            }

            int midpoint = startInclusive + (endInclusive - startInclusive)/2;
            // split the work into two subtasks
            PickApplesTask leftSubtask = new PickApplesTask(appleTrees, startInclusive, midpoint);
            PickApplesTask rightSubtask = new PickApplesTask(appleTrees, midpoint + 1, endInclusive);

            /*
             * Delegate one subtask asynchronously using fork(), and continue working on the other synchronously using compute()
             *      compute() - execution waits here until completion
             *      join() - will wait until the right subtask is done
             * The order in which these are called is important!!!
             */
            rightSubtask.fork();
            return leftSubtask.compute() + rightSubtask.join();
        }
    }

    public static class SomethingWentWrongException extends Exception {

    }

    public static void main(String[] args) {
        AppleTree[] appleTrees = AppleTree.newTreeGarden(NUMBER_OF_TREES);
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

        PickApplesTask task = new PickApplesTask(appleTrees, 0, appleTrees.length - 1);
        int result = forkJoinPool.invoke(task);

        System.out.println("Total apples picked: " + result);
    }
}
