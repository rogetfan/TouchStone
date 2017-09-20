package org.elise.test.framework.transaction.http;

import org.elise.test.framework.transaction.Response;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Glenn on  2017/9/20 0020 11:47.
 */


public class EliseHttpResponse implements Response {

     private Integer code;
     private byte[] httpContent;
     private Map<String,String> httpHeaders;

     public EliseHttpResponse(Integer code, byte[] httpContent, Map<String,String> httpHeaders){
         this.code = code;
         this.httpContent = httpContent;
         this.httpHeaders = httpHeaders;
     }

    public EliseHttpResponse(Integer code, String httpContent, Map<String,String> httpHeaders) throws UnsupportedEncodingException {
        this(code,httpContent.getBytes("UTF-8"),httpHeaders);
    }

    protected Integer getCode() {
        return code;
    }

    protected byte[] getBinaryContent() {
        return httpContent;
    }

    protected String getStringContent() throws UnsupportedEncodingException {
        return new String(httpContent,"UTF-8");
    }

    protected Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }


}
