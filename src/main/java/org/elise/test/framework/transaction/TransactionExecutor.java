package org.elise.test.framework.transaction;

import org.elise.test.exception.ExecutorException;
import org.elise.test.exception.NullRequestException;

import java.util.concurrent.*;

/**
 * Created by Glenn on  2017/9/18 0018 17:51.
 */


public class TransactionExecutor  {

    private ThreadPoolExecutor innerExecutor;

    private BlockingQueue<Runnable> queue;

    private String name;

    public TransactionExecutor(String name, int corePoolSize, int maximumPoolSize, final int maxTaskQueueCapacity){
        this.name = name;
        this.queue = new LinkedBlockingQueue<>(maxTaskQueueCapacity);
        this.innerExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                60L,
                TimeUnit.SECONDS,
                this.queue,
                InnerThreadFactory.forApp(name), (r, executor) -> {
                    throw new ExecutorException("Task " + r.toString() + " rejected ", executor);
                });
    }
    public void exec(final Transaction transaction){
        innerExecutor.execute(() -> {
            try{
                 transaction.sendRequest();
            }catch(Throwable t){
                transaction.getCallBack().failed(t);
            }
        });
    }

    public void shutDown(){
        innerExecutor.shutdown();
    }

   static class InnerThreadFactory{

        private static final ThreadFactory DEFAULT = Executors.defaultThreadFactory();

        public static ThreadFactory forApp(String name) {
            return create("p", name,true);
        }

        public static ThreadFactory forIO(String name) {
            return create("io", name,true);
        }

        public static ThreadFactory create(final String perfix, final String name,final boolean daemon) {
            return new ThreadFactory()
            {
                private int index = 0;

                @Override
                public Thread newThread(Runnable r)
                {
                    Thread t = DEFAULT.newThread(r);
                    t.setName(String.format("%s-%s-%s", perfix, name, index));
                    t.setDaemon(daemon);
                    index++;
                    return t;
                }
            };
        }
    }
}
