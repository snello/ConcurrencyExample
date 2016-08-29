package me.snell.ConcurrencyTest;

/**
 * Created by oliversnell on 8/29/16.
 * Interface for classes wishing to be notified of completion
 */
public interface IDependent {

    void complete(IDependency depends);


}
