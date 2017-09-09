//package org.elise.test.framework.usergroup;
//
//import org.elise.test.framework.transaction.Transaction;
//
///**
// * Created by Glenn on 2016/10/18.
// */
//
//public abstract class VirtualUser <T extends Transaction> implements Runnable {
//    private VirtualUserInfo userInfo;
//    private UserGroup group;
//    private T transaction;
//
//    public VirtualUser(UserGroup group,VirtualUserInfo userInfo) {
//         this.group = group;
//         this.userInfo = userInfo;
//    }
//
//    public void clearTransaction(){
//        transaction = null;
//    }
//
//    public void setTransaction(T transaction){
//       this.transaction = transaction;
//    }
//
//    public UserGroup getGroup(){
//        return group;
//    }
//
//    public T  getTransaction(){
//        return transaction;
//    }
//
//    public Object getUserInfo(String keyName) {
//        return userInfo.getItem(keyName);
//    }
//
//    public void setUserInfo(String keyName,Object object){
//       userInfo.putItem(keyName,object);
//    }
//}