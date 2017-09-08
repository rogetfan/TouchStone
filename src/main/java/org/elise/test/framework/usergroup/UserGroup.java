package org.elise.test.framework.usergroup;

import org.elise.test.lr.LrTransHelper;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Glenn on 2017/5/31 0031.
 */
public class UserGroup implements Runnable {

    private Integer userCount;
    private Boolean isRunning;
    private String  groupName;

    private LinkedBlockingQueue<VirtualUser> queue;

    public UserGroup(Integer userCount,Integer index){
         queue = new LinkedBlockingQueue<>(userCount);
         this.groupName = "UserGroup-"+index;
         this.userCount = userCount;

    }

    public synchronized void addUser(VirtualUser user){
         queue.add(user);
    }

    public String getGroupName(){
        return groupName;
    }

    void start() throws Throwable {
        LrTransHelper.startTransaction("virtual_user_group_start");
        isRunning = true;
        try {
            LrTransHelper.endTransaction("virtual_user_group_start", true);
        } catch (Throwable t) {
            LrTransHelper.endTransaction("virtual_user_group_start", false);
            throw t;
        }
    }

    void stop() throws Throwable {
        LrTransHelper.startTransaction("virtual_user_group_end");
        isRunning = false;
        try {
            LrTransHelper.endTransaction("virtual_user_group_end", true);
        } catch (Throwable t) {
            LrTransHelper.endTransaction("virtual_user_group_end", false);
            throw t;
        }
    }

    @Override
    public void run() {
       while(isRunning){

       }
    }
}
