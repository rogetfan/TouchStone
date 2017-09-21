package org.elise.test.framework.transaction.http;

import org.elise.test.framework.stack.http.EliseHttpMethod;
import org.elise.test.framework.transaction.Request;
import org.elise.test.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by Glenn on  2017/9/18 0018 21:32.
 */

public class EliseHttpRequest implements Request {

    private URI uri;
    private Map<String,String> headers;
    private EliseHttpMethod method;
    private byte[] httpContent;

    public EliseHttpRequest(URI uri, Map<String,String> headers, byte[] httpContent){
        this.uri = uri;
        this.headers = headers;
        this.httpContent = httpContent;
        this.method = EliseHttpMethod.GET;
    }

    public EliseHttpRequest(URI uri, Map<String,String> headers, String httpContent) {
        this(uri,headers, httpContent != null ?httpContent.getBytes(Charset.forName("UTF-8")):new byte[0]);
    }

    public EliseHttpRequest(URI uri, Map<String,String> headers){
        this(uri,headers, new byte[0]);
    }

    protected URI getUri() {
        return uri;
    }

    protected Map<String, String> getHeaders() {
        return headers;
    }

    protected EliseHttpMethod getMethod() {
        return method;
    }

    protected byte[] getHttpContent() {
        return httpContent;
    }

    public void setMethodPost(){
        this.method = EliseHttpMethod.POST;
    }

    public void setMethodGet(){
        this.method = EliseHttpMethod.GET;
    }

    public void setMethodPut(){
        this.method = EliseHttpMethod.PUT;
    }

    public void setMethodDelete(){
        this.method = EliseHttpMethod.DELETE;
    }

    @Override
    public String toString(){
        StringBuilder request = new StringBuilder();
        request.append(method);
        request.append(StringUtil.SPACE);
        request.append(uri.getPath());
        if(uri.getQuery() != null){
            request.append("?");
            request.append(uri.getQuery());
        }
        request.append(StringUtil.SPACE);
        request.append("HTTP/1.1");
        request.append(StringUtil.ENDLINE);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.append(entry.getKey()).append(":").append(entry.getValue());
            request.append(StringUtil.ENDLINE);
        }
        request.append(StringUtil.ENDLINE);
        request.append(StringUtil.ENDLINE);
        if (httpContent == null) {
            request.append("");
        } else if (httpContent.length > 8 * 1024) {
            request.append("REQUEST BOOOOOOODY　TOOOOOO LARGE");
        } else {
            request.append(new String(httpContent, Charset.forName("UTF-8")));
        }
        return request.toString();
    }
}
