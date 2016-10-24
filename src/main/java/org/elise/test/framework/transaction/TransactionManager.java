package org.elise.test.framework.transaction;

/**
 * Created by huxuehan on 2016/10/19.
 * This interface is designed for defining a global class
 * for VirtualUser to manage connection,manufacture and manipulate transaction
 *
 */
public abstract class TransactionManager {
      public abstract void start() throws Throwable;
      public abstract void stop() throws Throwable;
}
