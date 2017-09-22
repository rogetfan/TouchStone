package org.elise.test.framework.transaction.http;

import org.elise.test.framework.stack.http.EliseHttpClient;
import org.elise.test.framework.transaction.Transaction;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Glenn on  2017/9/20 0020 17:34.
 */


public class TransactionManager {

    private EliseHttpClient client;

    private LinkedList<Transaction> transactionList = new LinkedList<>();

    public TransactionManager(EliseHttpClient client){
         this.client = client;
    }

    public Transaction create(Integer uniqueId){
         return new  HttpTransaction(client,uniqueId);
    }
}
