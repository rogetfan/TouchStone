package org.elise.test.framework.transaction;

import org.elise.test.exception.ExecutorException;
import org.elise.test.tracer.Tracer;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Glenn on 2017/5/31 0031.
 */
public class TransactionDispatcher {

    private ThreadPoolExecutor dispatcher;
    private static Tracer tracer = Tracer.getInstance(TransactionDispatcher.class);

    public TransactionDispatcher(Integer dispatcherCount, Integer maxTransactionCount) {

        dispatcher = new ThreadPoolExecutor(
                dispatcherCount,
                dispatcherCount,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(maxTransactionCount),
                new ThreadFactory() {
                    private AtomicInteger count = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        t.setDaemon(false);
                        t.setDaemon(true);
                        t.setName("TransactionDispatcher-" + count.addAndGet(1));
                        return t;
                    }
                },
                new RejectedExecutionHandler() {

                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        tracer.writeError("TransactionDispatcher Has Been Out Of Queue");
                        throw new ExecutorException(r.toString(), executor);
                    }
                }
        );
    }

    public void dispatch(Transaction transaction){
        dispatcher.execute(transaction);
    }

    public void stop(){
        dispatcher.shutdown();
    }
}
