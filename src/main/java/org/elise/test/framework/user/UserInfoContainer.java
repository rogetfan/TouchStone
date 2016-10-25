package org.elise.test.framework.user;

import org.elise.test.util.StringUtil;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huxuehan on 2016/10/25.
 */
public class UserInfoContainer {
    private static ConcurrentHashMap<String,UserInfo> container = new ConcurrentHashMap<String,UserInfo>();

    public static void put(VirtualUser user,UserInfo userInfo) throws NullPointerException {
        if(StringUtil.isNullOrEmpty(user.getUserStamp())|| userInfo == null){
            throw new NullPointerException();
        }else{
            container.put(user.getUserStamp(),userInfo);
        }
    }

    public static UserInfo get(VirtualUser user){
        if(StringUtil.isNullOrEmpty(user.getUserStamp())){
            return null;
        }else{
           return container.get(user.getUserStamp());
        }
    }
}
