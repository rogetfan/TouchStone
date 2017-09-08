package org.elise.test.framework.usergroup;

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
        items.put(keyName,object);
    }
}
