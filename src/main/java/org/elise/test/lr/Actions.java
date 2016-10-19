package org.elise.test.lr;

import org.elise.test.config.ConfigLoader;
import org.elise.test.config.Configuration;
import org.elise.test.exception.LoadConfigException;
import org.elise.test.framework.FrameworkConfig;
import org.elise.test.framework.transaction.TransactionManager;
import org.elise.test.framework.transaction.http.HttpTransactionManager;
import org.elise.test.framework.user.UserContainer;
import org.elise.test.framework.user.UserInfo;
import org.elise.test.framework.user.VirtualUser;
import org.elise.test.performance.http.HttpUser;
import org.elise.test.performance.http.HttpUserInfo;
import org.elise.test.tracer.TracerConfig;

/**
 * LoadRunner Java script. (Build: _build_number_)
 * <p>
 * Script Description:
 */


public class Actions {
    UserContainer container;

    public int init() {
        try {
            ConfigLoader.getInstance().loadProperties("setting.properties", FrameworkConfig.getInstance(), TracerConfig.getInstance());
        } catch (LoadConfigException e) {
            e.printStackTrace();
        }
        container = new UserContainer(FrameworkConfig.getInstance().getVirtualUserCount());
        VirtualUser[] users = new HttpUser[FrameworkConfig.getInstance().getVirtualUserCount()];
        TransactionManager manager = new HttpTransactionManager();
        for (int count =0;count<users.length;count++) {
            users[count] = new HttpUser();
            users[count].setUserInfo(new HttpUserInfo());
            users[count].setTransactionManager(manager);
        }
        container.setUsers(users);
        try {
            container.start();
        } catch (Throwable throwable) {
            System.err.println("script start failed");
            throwable.printStackTrace();
        }
        return 0;
    }

    public int action() throws Throwable {
        while (true) {
            try {
                LrTransStatus status = LrTransStatusManager.takeTransaction();
                if (status == null) {
                    continue;
                }
                LrTransHelper.set_transaction(status);
            } catch (Throwable t) {
                LrTransHelper.error_message(t);
            }
        }
    }// end of transaction

    public int end() throws Throwable {
        container.stop();
        return 0;
    }
}
