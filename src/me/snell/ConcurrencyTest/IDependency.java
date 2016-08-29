package me.snell.ConcurrencyTest;

/**
 * Created by oliversnell on 8/29/16.
 * Interface for classes offering callbacks on completion
 */
public interface IDependency {

    String getId();
    void addCompletionHook(IDependent depends);
}
