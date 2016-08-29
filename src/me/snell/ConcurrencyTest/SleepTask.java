package me.snell.ConcurrencyTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by oliversnell on 8/28/16.
 */
public class SleepTask implements ITask, IDependency, Callable {

    private List<IDependent> dependents;
    private boolean complete = false;
    private int sleepTime;
    private String id;

    public SleepTask(String id, int sleepTime) {
        this.sleepTime = sleepTime;
        this.id = id;

        // track dependents wanting to hear about our completion
        this.dependents = new ArrayList<>();
    }

    private void printStatus(String output) {
        System.out.println("[" + this.id + "]: " + output);
    }


    @Override
    public Object call() throws Exception {

        try {
            this.printStatus("Starting my sleep for " +  this.sleepTime + " seconds");
            TimeUnit.SECONDS.sleep(this.sleepTime);
        } catch(InterruptedException e) {
            this.printStatus("I was interrupted");
            return false;
        }

        this.complete = true;
        this.printStatus("Completed sleep");

        // notify our dependents
        this.dependents.stream().forEach(
                dependent -> {
                    dependent.complete(this);
                }
        );

        return complete;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isComplete() {
        return this.complete;
    }

    @Override
    public void addCompletionHook(IDependent consumer) {
        this.dependents.add(consumer);
    }
}
