package me.snell.ConcurrencyTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by oliversnell on 8/28/16.
 */
public class Consumer implements Runnable, IDependent {

    private String id;

    private List<ITask> tasks;                  // The list of tasks for me to complete
    private List<IDependency> dependencies;     // The list of tasks I am dependent upon

    private ExecutorService myExecutor;

    public Consumer(String id, List<ITask> tasksIn, IDependency... dependencies) {
        this.id = id;
        this.tasks = tasksIn;

        // Capture our dependencies and request callbacks
        this.dependencies = Collections.synchronizedList(new ArrayList<>());
        for(IDependency dependency : dependencies) {
            this.dependencies.add(dependency);
            dependency.addCompletionHook(this);
        }

        this.myExecutor = Executors.newCachedThreadPool();

    }

    private void log(String output) {
        System.out.println("[" + this.id + "]: " + output);
    }

    @Override
    public void run() {
        tasks.parallelStream().forEach(
                task -> {
                    try {
                        Future<Boolean> b = this.myExecutor.submit((Callable)task);
                        if(b.isDone() || b.get()) {
                            this.log("[" + task.getId() + "] has completed successfully");

                            if(this.isComplete()) {
                                this.log("This process has completed all tasks!");
                            }
                        } else {
                            this.log("Task ID [" + task.getId() + "] has FAILED!");
                        }
                    } catch(InterruptedException | ExecutionException e) {
                        this.log("Process was interrupted");
                    }
                }
        );
    }

    private boolean isComplete() {
        synchronized(this.tasks) {
            for(ITask task : this.tasks) {
                if (!task.isComplete())
                    return false;
            }
        }

        synchronized(this.dependencies) {
            if(this.dependencies.size() > 0)
                return false;
        }

        // All dependencies satisfied so return true
        return true;
    }

    @Override
    public void complete(IDependency depends) {

        synchronized(this.dependencies) {
            this.log("Dependency has completed! [" + depends.getId() + "]");
            this.dependencies.remove(depends);
        }

        if(this.isComplete()) {
            this.log("This process has completed all tasks!");
        }
    }
}
