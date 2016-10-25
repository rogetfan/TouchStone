package org.elise.test.framework.user;

import org.elise.test.framework.FrameworkConfig;
import org.elise.test.framework.transaction.TransactionManager;
import org.elise.test.lr.LrTransHelper;
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

    public VirtualUser() {

    }

    public T getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(T userInfo) {
        this.userInfo = userInfo;
    }

    public TransactionManager getTransactionManager() {
        return manager;
    }

    public void setTransactionManager(TransactionManager manager) {
        this.manager = manager;
    }

    // interface to judge if VirtualUser is runnable
    public boolean isRunnable() {
        return isRunnable;
    }

    void start() throws Throwable {
        LrTransHelper.startTransaction("virtual_user_start");
        isRunnable = true;
        try {
            manager.start();
            LrTransHelper.endTransaction("virtual_user_start", true);
        } catch (Throwable t) {
            LrTransHelper.endTransaction("virtual_user_start", false);
            throw t;
        }
    }

    void stop() throws Throwable {
        LrTransHelper.startTransaction("virtual_user_end");
        isRunnable = false;
        try {
            manager.stop();
            LrTransHelper.endTransaction("virtual_user_end", true);
        } catch (Throwable t) {
            LrTransHelper.endTransaction("virtual_user_end", false);
            throw t;
        }
    }

    // implement what you want to do here
    public abstract void action() throws Throwable;

    // generate single symbol for VirtualUser
    public abstract String getUserStamp();


    @Override
    public void run() {
        while (isRunnable) {
            //Sleeping for a while before run action()
            try {
                Random random = new Random();
                long timeSeed = random.nextLong() & Long.MAX_VALUE;
                Thread.sleep(timeSeed % FrameworkConfig.getInstance().getMaxIntervalTimeStamp());
            } catch (InterruptedException e) {
                tracer.writeError("Sleeping has been interrupted", e);
            }
            try {
                action();
            } catch (Throwable t) {
                tracer.writeError("Virtual User face an exception", t);
                isRunnable = false;
            }
        }
    }
}