package org.elise.test.framework.user;

import org.elise.test.tracer.Tracer;

/**
 * Created by huxuehan on 2016/10/19.
 */
public class UserContainer {

    private static final Tracer tracer = Tracer.getInstance(UserContainer.class);
    private Integer userCount;
    private VirtualUser[] users;
    public UserContainer(Integer userCount) {
        this.userCount = userCount;
    }

    public void setUsers(VirtualUser[] users) {
        this.users = users;
    }

    public void start() throws Throwable {
        Thread[] threads = new Thread[userCount];
        for (int count = 0; count < userCount; count++) {
            threads[count] = new Thread(users[count]);
            users[count].start();
            threads[count].setName("VirtualUser-" + count);
            threads[count].setDaemon(true);
            threads[count].start();
            tracer.writeSpecial("VirtualUser-" + count+" start successfully");
        }
    }

    public void stop() throws Throwable {
        for(int count = 0; count < userCount; count++){
            users[count].stop();
        }
    }
}
