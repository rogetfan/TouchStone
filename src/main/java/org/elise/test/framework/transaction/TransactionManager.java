package org.elise.test.framework.transaction;

/**
 * Created by huxuehan on 2016/10/19.
 */
public abstract class TransactionManager {
      public abstract void start() throws Throwable;
      public abstract void stop() throws Throwable;
     // public abstract Transaction createHttpTransaction(Object ... args);
}
