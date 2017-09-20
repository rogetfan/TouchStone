package org.elise.test.framework.transaction.http;

import org.elise.test.framework.stack.http.HttpClient;
import org.elise.test.framework.transaction.Transaction;

/**
 * Created by Glenn on  2017/9/20 0020 17:34.
 */


public class TransactionManager {

    private HttpClient client;

    public TransactionManager(HttpClient client){
         this.client = client;
    }

    public Transaction create(Integer uniqueId){
          return new  HttpTransaction(client,uniqueId);
    }
}
