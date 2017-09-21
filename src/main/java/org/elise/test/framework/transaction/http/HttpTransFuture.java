package org.elise.test.framework.transaction.http;

import org.elise.test.framework.stack.http.EliseHttpStatusHelper;
import org.elise.test.framework.transaction.Response;
import org.elise.test.framework.transaction.future.TransactionFuture;

import java.util.Map;

/**
 * Created by Glenn on  2017/9/20 0020 11:39.
 */


public abstract class HttpTransFuture implements TransactionFuture {

    @Override
    public void success(Response response){
        EliseHttpResponse httpResponse = (EliseHttpResponse) response;
        EliseHttpStatusHelper helper = EliseHttpStatusHelper.valueOf(httpResponse.getStatus());
        if(helper.isRedirection()){
            redirect(httpResponse.getStatus(),httpResponse.getBinaryContent(),httpResponse.getHttpHeaders());
        }else if(helper.isSuccessful()){
            success(httpResponse.getStatus(),httpResponse.getBinaryContent(),httpResponse.getHttpHeaders());
        }
    }

    @Override
    public void error(Response response){
        EliseHttpResponse httpResponse = (EliseHttpResponse) response;
        error(httpResponse.getStatus(),httpResponse.getBinaryContent(),httpResponse.getHttpHeaders());
    }

    /**
     * Send request successfully,and get response which contains 2XX
     * */

    public abstract void redirect(Integer statusCode,byte[] httpContent,Map<String,String> headers);

    /**
     * Send request successfully,and get response which contains 3XX
     * */
    public abstract void success(Integer statusCode,byte[] httpContent,Map<String,String> headers);

    /**
     * Send request successfully,but not get response which contains 2xx or 3XX
     * For example 404,501
     * */
    public abstract void error(Integer statusCode,Object httpContent,Map<String,String> headers);



}
