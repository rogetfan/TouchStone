package org.elise.test.framework.user;

/**
 * Created by huxuehan on 2016/10/18.
 */
public abstract class VirtualUser<T> {
    T userInfo;

    public VirtualUser(T userInfo)
    {
        this.userInfo = userInfo;
    }

    public abstract void action();
}
