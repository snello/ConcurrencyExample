package me.snell.ConcurrencyTest;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Main {

    private static int taskCount = 5;

    public static void main(String[] args) {

        /**
         * Create the task list
         */
        List<ITask> pA = Collections.synchronizedList(new ArrayList<>());
        List<ITask> pB = Collections.synchronizedList(new ArrayList<>());

        System.out.println("Creating task lists for Process A and B");
        IntStream.range(0, taskCount).forEach(
                i -> {
                    String taskId = "Task-" + (i+1);
                    pA.add(new SleepTask("A-" + taskId, i * 1));
                    pB.add(new SleepTask("B-" + taskId, i * 5));
                }
        );

        /**
         * I've implemented the dependency via the consumers (i.e. Processes A and B) as the problem never
         * specified a requirement to pause one particular task in Process A while Process B(Task 2) completed,
         * only that Process A couldn't complete without PB-2 first.
         */
        ExecutorService myExecutor = Executors.newCachedThreadPool();

        System.out.println("Executing process A and B in new threads");
        myExecutor.execute(new Consumer("Process A", pA, (IDependency)pB.get(1)));  // Process A with dependency on Process B task 2
        myExecutor.execute(new Consumer("Process B", pB));    // Process B

    }
}
