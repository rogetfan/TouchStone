package org.elise.test.framework.transaction;

import org.elise.test.exception.ExecutorException;

import java.util.concurrent.*;

/**
 * Created by Glenn on  2017/9/18 0018 17:51.
 */


public class TransactionExecutor {

    private ThreadPoolExecutor innerExecutor;

    private BlockingQueue<Runnable> queue;

    public TransactionExecutor(int corePoolSize, int maximumPoolSize, final int maxTaskQueueCapacity) {
        this.queue = new LinkedBlockingQueue<>(maxTaskQueueCapacity);
        this.innerExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                60L,
                TimeUnit.SECONDS,
                this.queue,
                ThreadFactorys.forApp("TransactionExecutor"), (r, executor) -> {
            throw new ExecutorException("Task " + r.toString() + " rejected ", executor);
        });
    }

    public void exec(final Transaction transaction) {
        innerExecutor.execute(() -> {
            try {
                transaction.sendRequest();
            } catch (Throwable t) {
                transaction.future.failed(t);
            }
        });
    }

    public void shutDown() {
        innerExecutor.shutdown();
    }
}
