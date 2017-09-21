package org.elise.test.framework.stack;

import org.elise.test.framework.transaction.Transaction;

/**
 * Created by Glenn on  2017/9/18 0018 21:25.
 */

public interface Connection {

     void invoke(Object request,Object attachment);

}
