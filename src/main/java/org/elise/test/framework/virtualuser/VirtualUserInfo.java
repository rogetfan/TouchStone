package org.elise.test.framework.virtualuser;

import org.elise.test.framework.stack.http.EliseHttpClient;
import org.elise.test.framework.transaction.Transaction;
import org.elise.test.framework.transaction.http.TransactionManager;

import java.util.HashMap;

/**
 * Created by Glenn on 2016/10/19.
 */
public abstract class VirtualUserInfo {
    private HashMap<String,Object> items = new HashMap<>();

    public Object getItem(String keyName){
        return items.get(keyName);
    }

    public void putItem(String keyName,Object object){
        ;
        TransactionManager manager = new TransactionManager(EliseHttpClient.getInstance());
        Transaction transaction = manager.create(1);
        items.put(keyName,object);
    }
}
