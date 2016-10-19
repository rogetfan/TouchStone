package org.elise.test.framework.user;

import org.elise.test.framework.FrameworkConfig;
import org.elise.test.framework.transaction.TransactionManager;
import org.elise.test.tracer.Tracer;

import java.util.Random;

/**
 * Created by huxuehan on 2016/10/18.
 */
public abstract class VirtualUser<T extends UserInfo> implements Runnable {
    private T userInfo;
    private boolean isRunnable;
    private TransactionManager manager;
    private Tracer tracer = Tracer.getInstance(this.getClass());

    public VirtualUser()
    {

    }

    public void setUserInfo(T userInfo) {
        this.userInfo = userInfo;
    }

    public T getUserInfo(){
        return userInfo;
    }

    public void setTransactionManager(TransactionManager manager){
        this.manager = manager;
    }

    public TransactionManager getTransactionManager(){
        return manager;
    }

    void start() throws Throwable {
        isRunnable = true;
        manager.start();
    }

    void stop() throws Throwable {
        isRunnable = false;
        manager.stop();
    }

    public abstract void action();

    @Override
    public void run() {
        while(isRunnable){
            action();
            try {
                Random random = new Random();
                long timeSeed = random.nextLong()&Long.MAX_VALUE;
                Thread.sleep(timeSeed % FrameworkConfig.getInstance().getMaxIntervalTimeStamp());
            } catch (InterruptedException e) {
                tracer.writeError("Sleeping has been interrupted",e);
            }
        }
    }
}
