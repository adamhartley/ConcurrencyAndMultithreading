package org.concurrencyandmultithreading.forkjoinpool;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;

public class PickApplesWithInvokeAll {

    public static Callable<Void> createApplePicker(AppleTree[] appleTrees, int fromIdxInclusive, int toIdxExclusive, String workerName) {
        return () -> {
            // each apple picker iterates over the trees within the index range
            for (int i = fromIdxInclusive; i < toIdxExclusive; i++) {
                // for each tree, the worker calls the pickApples method
                appleTrees[i].pickApples(workerName);
            }
            return null;
        };
    }

    public static void main(String[] args) {
        AppleTree[] appleTrees = AppleTree.newTreeGarden(6);

        // create apple pickers, and assign tress to pick
        Callable<Void> applePicker1 = createApplePicker(appleTrees, 0, 2, "Ricky");
        Callable<Void> applePicker2 = createApplePicker(appleTrees, 2, 4, "Julian");
        Callable<Void> applePicker3 = createApplePicker(appleTrees, 4, 6, "Bubbles");

        // start the workers
        ForkJoinPool.commonPool().invokeAll(Arrays.asList(applePicker1, applePicker2, applePicker3));

        System.out.println();
        System.out.println("All apples picked!!!");
    }
}
