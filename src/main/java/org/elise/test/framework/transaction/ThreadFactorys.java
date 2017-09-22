package org.elise.test.framework.transaction;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Glenn on  2017/9/21 0021 11:36.
 */


public class ThreadFactorys {

    private static final ThreadFactory DEFAULT = Executors.defaultThreadFactory();

    public static ThreadFactory forApp(String name) {
        return create("p", name,true);
    }

    public static ThreadFactory forIO(String name) {
        return create("io", name,true);
    }

    public static ThreadFactory create(final String prefix, final String name,final boolean daemon) {
        return new ThreadFactory()
        {
            private int index = 0;

            @Override
            public Thread newThread(Runnable r)
            {
                Thread t = DEFAULT.newThread(r);
                t.setName(String.format("%s-%s-%s", prefix, name, index));
                t.setDaemon(daemon);
                index++;
                return t;
            }
        };
    }

}
