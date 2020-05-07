package org.concurrencyandmultithreading.forkjoinpool;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class AppleTree {

    private final String treeLabel;
    private final int numberOfApples;

    public AppleTree(String treeLabel) {
        this.treeLabel = treeLabel;
        this.numberOfApples = 3;
    }

    /*
     * Helper method to create an array of AppleTree objects
     */
    public static AppleTree[] newTreeGarden(int size) {
        AppleTree[] appleTrees = new AppleTree[size];
        for (int i = 0; i < appleTrees.length; i++) {
            appleTrees[i] = new AppleTree("Tree # " + i);
        }
        return appleTrees;
    }

    public int pickApples(String workerName) {
        try {
            System.out.printf("%s started picking apples from %s \n", workerName, treeLabel);
            TimeUnit.SECONDS.sleep(1); // imitating doing some work

            System.out.printf("%s picked %d apples from %s \n", workerName, numberOfApples, treeLabel);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return numberOfApples;
    }

    public int pickApples() {
        return pickApples(toLabel(Thread.currentThread().getName()));
    }

    private String toLabel(String threadName) {
        HashMap<String, String> threadNameToLabel = new HashMap<>();
        threadNameToLabel.put("ForkJoinPool.commonPool-worker-1", "Ricky");
        threadNameToLabel.put("ForkJoinPool.commonPool-worker-2", "Julian");
        threadNameToLabel.put("ForkJoinPool.commonPool-worker-3", "Bubbles");
        threadNameToLabel.put("ForkJoinPool.commonPool-worker-4", "Randy");

        return threadNameToLabel.getOrDefault(threadName, threadName);
    }
}
