package org.elise.test.framework.transaction;

import org.elise.test.config.TransactionConfiguration;
import org.elise.test.exception.ExecutorException;
import org.elise.test.framework.stack.VirtualClient;
import org.elise.test.framework.transaction.future.FutureLevel;

import java.util.concurrent.*;

/**
 * Created by Glenn on  2017/9/18 0018 17:51.
 */


public class TransactionExecutor {

    private ThreadPoolExecutor innerExecutor;

    private BlockingQueue<Runnable> queue;

    private VirtualClient client;

    public TransactionExecutor(VirtualClient client) {
        this.queue = new LinkedBlockingQueue<>(TransactionConfiguration.getInstance().getMaxTaskQueueCapacity());
        this.innerExecutor = new ThreadPoolExecutor(
                TransactionConfiguration.getInstance().getCorePoolSize(),
                TransactionConfiguration.getInstance().getMaximumPoolSize(),
                60L,
                TimeUnit.SECONDS,
                this.queue,
                ThreadFactorys.forApp("TransactionExecutor"), (r, executor) -> {
            throw new ExecutorException("Task " + r.toString() + " rejected ", executor);
        });
        this.client = client;
    }

    public void execFuture(final Transaction transaction, final FutureLevel level, final Throwable throwable,long usedTimeStamp) {
        innerExecutor.execute(() -> {
            try {
                switch(level){
                    case UNREACHABLE:transaction.future.unreachable(usedTimeStamp);break;
                    case SUCCESS:transaction.future.success(transaction.response,usedTimeStamp);
                                 break;
                    case ERROR:transaction.future.error(transaction.response,usedTimeStamp);break;
                    case FAILED: transaction.future.failed(throwable,usedTimeStamp);break;
                    default:throw new ExecutorException("Future level not support");
                }
                Thread.sleep(transaction.getSleepTimeStamp());
            } catch (Throwable t) {
                transaction.future.failed(t,usedTimeStamp);
            }
        });
    }

    public void execTransaction(final Transaction transaction) {
        innerExecutor.execute(() -> {
            try {
                transaction.sendRequest();
                transaction.timeStampBegin = System.currentTimeMillis();
            } catch (Throwable t) {
                transaction.future.failed(t,0L);
            }
        });
    }

    public void shutDown() {
        innerExecutor.shutdown();
    }
}
