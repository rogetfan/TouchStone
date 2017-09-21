package org.elise.test.framework.transaction;

import org.elise.test.exception.ExecutorException;
import org.elise.test.framework.transaction.future.FutureLevel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Glenn on  2017/9/21 0021 11:35.
 */


public class FutureExecutor {

    private ThreadPoolExecutor innerExecutor;

    private BlockingQueue<Runnable> queue;

    public FutureExecutor(int corePoolSize, int maximumPoolSize, final int maxTaskQueueCapacity) {
        this.queue = new LinkedBlockingQueue<>(maxTaskQueueCapacity);
        this.innerExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                60L,
                TimeUnit.SECONDS,
                this.queue,
                ThreadFactorys.forApp("FutureExecutor"), (r, executor) -> {
            throw new ExecutorException("Task " + r.toString() + " rejected ", executor);
        });
    }

    public void exec(final Transaction transaction, final FutureLevel level, final Throwable throwable) {
        innerExecutor.execute(() -> {
            try {
                 switch(level){
                     case UNREACHABLE:transaction.future.unreachable();break;
                     case SUCCESS:transaction.future.success(transaction.response);break;
                     case ERROR:transaction.future.error(transaction.response);break;
                     case FAILED: transaction.future.failed(throwable);
                     default:throw new ExecutorException("Future level not support");
                 }
            } catch (Throwable t) {
                transaction.future.failed(t);
            }
        });
    }

    public void shutDown() {
        innerExecutor.shutdown();
    }

}
