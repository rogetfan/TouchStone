package org.elise.test.framework.transaction.http;

import org.elise.test.framework.transaction.Response;
import org.elise.test.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by Glenn on  2017/9/20 0020 11:47.
 */


public class EliseHttpResponse implements Response {

     private Integer status;
     private byte[] httpContent;
     private Map<String,String> headers;

     public EliseHttpResponse(Integer status, byte[] httpContent, Map<String,String> headers){
         this.status = status;
         this.httpContent = httpContent;
         this.headers = headers;
     }

    public EliseHttpResponse(Integer code, String httpContent, Map<String,String> httpHeaders) {
        this(code,httpContent.getBytes(Charset.forName("UTF-8")),httpHeaders);
    }

    protected Integer getStatus() {
        return status;
    }

    protected byte[] getBinaryContent() {
        return httpContent;
    }

    protected String getStringContent() {
        return new String(httpContent,Charset.forName("UTF-8"));
    }

    protected Map<String, String> getHttpHeaders() {
        return headers;
    }

    @Override
    public String toString(){
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1");
        response.append(StringUtil.SPACE);
        response.append(status);
        response.append(StringUtil.ENDLINE);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            response.append(entry.getKey());
            response.append(":");
            response.append(entry.getValue());
            response.append(StringUtil.ENDLINE);
        }
        response.append(StringUtil.ENDLINE);
        response.append(StringUtil.ENDLINE);
        if (httpContent.length > 128 * 1024) {
            response.append("REQUEST BOOOOOOODY　TOOOOOO LARGE");
        } else {
            response.append(new String(httpContent, Charset.forName("UTF-8")));
        }
        return response.toString();
    }
}
