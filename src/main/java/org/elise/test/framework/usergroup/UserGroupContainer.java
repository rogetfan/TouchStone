//package org.elise.test.framework.usergroup;
//
//import org.elise.test.tracer.Tracer;
//import org.elise.test.util.StringUtil;
//
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * Created by Glenn on 2017/5/31 0031.
// */
//public class UserGroupContainer {
//
//    private static final Tracer tracer = Tracer.getInstance(UserGroupContainer.class);
//    private Integer userCount;
//    private UserGroup[] groups;
//    public UserGroupContainer(Integer userCount) {
//        this.userCount = userCount;
//    }
//
//    public void setUserGroups(UserGroup[] groups) {
//        this.groups = groups;
//    }
//
//    public void start() throws Throwable {
//        Thread[] threads = new Thread[userCount];
//        for (int count = 0; count < userCount; count++) {
//            threads[count] = new Thread(groups[count]);
//            groups[count].start();
//            threads[count].setName(groups[count].getGroupName());
//            threads[count].setDaemon(true);
//            threads[count].start();
//            tracer.writeSpecial(groups[count].getGroupName()+" start successfully");
//        }
//    }
//
//    public void stop() throws Throwable {
//        for(int count = 0; count < userCount; count++){
//            groups[count].stop();
//        }
//    }
//}
