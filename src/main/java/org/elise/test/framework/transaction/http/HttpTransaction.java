package org.elise.test.framework.transaction.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.handler.codec.http.*;
import org.elise.test.exception.NullRequestException;
import org.elise.test.framework.stack.Connection;
import org.elise.test.framework.stack.http.EliseHttpClient;
import org.elise.test.framework.stack.http.EliseHttpConnection;
import org.elise.test.framework.transaction.Transaction;
import org.elise.test.util.GlobalId;
import org.elise.test.util.StringUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by Glenn on  2017/9/20 0020 11:56.
 */


public class HttpTransaction extends Transaction {

    private String globalId;

    protected HttpTransaction(EliseHttpClient client, Integer connToken) {
        this.client = client;
        this.globalId = GlobalId.generate16();
        this.connToken = connToken;
        this.request = null;
        this.response = null;
    }

    @Override
    public void sendRequest() throws Throwable {
        if (request == null) {
            throw new NullRequestException("the request is null has been discovered when it will be sent");
        } else {
            EliseHttpRequest httpRequest = (EliseHttpRequest) request;
            Connection httpConnection = client.getConnection(connToken, httpRequest.getUri());
            httpConnection.connect();
            StringBuilder uriBuilder = new StringBuilder();
            uriBuilder.append(httpRequest.getUri().getPath());
            if(httpRequest.getUri().getQuery() != null){
                uriBuilder.append("?");
                uriBuilder.append(httpRequest.getUri().getQuery());
            }
            DefaultHttpHeaders headers = new DefaultHttpHeaders();
            headers.set(HttpHeaderNames.CONTENT_LENGTH, httpRequest.getHttpContent() == null ? 0 : httpRequest.getHttpContent().length);
            headers.set(HttpHeaderNames.HOST, httpRequest.getUri().getHost());
            headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            headers.set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP_DEFLATE);
            headers.set(HttpHeaderNames.USER_AGENT, "IAmFuckingYourMind/6.3.2");
            for(Map.Entry<String,String> set : httpRequest.getHeaders().entrySet())
                headers.set(set.getKey(),set.getValue());
            ByteBuf body;
            if (httpRequest.getHttpContent() == null) {
                body = PooledByteBufAllocator.DEFAULT.buffer().writeBytes(new byte[0]);
            } else {
                body = PooledByteBufAllocator.DEFAULT.buffer().writeBytes(httpRequest.getHttpContent());
            }
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                    HttpMethod.valueOf(httpRequest.getMethod().toString()),
                    uriBuilder.toString(), body);
            request.headers().set(headers);
            request.setProtocolVersion(HttpVersion.HTTP_1_1);
            httpConnection.invoke(request,this);
        }

    }

    @Override
    public Long getSleepTimeStamp() {
        return 10L;
    }

    @Override
    public String getStrResponse() {
        return this.response.toString();
    }

    @Override
    public String getStrRequest() {

        EliseHttpRequest httpRequest = (EliseHttpRequest) request;
        StringBuilder request = new StringBuilder();
        request.append(httpRequest.getMethod().toString());
        request.append(StringUtil.SPACE);
        request.append(httpRequest.getUri().getPath());
        if(httpRequest.getUri().getQuery() != null){
            request.append("?");
            request.append(httpRequest.getUri().getQuery());
        }
        request.append(StringUtil.SPACE);
        request.append("HTTP/1.1");
        request.append(StringUtil.ENDLINE);
        for (Map.Entry<String, String> entry : httpRequest.getHeaders().entrySet()) {
            request.append(entry.getKey()).append(":").append(entry.getValue());
            request.append(StringUtil.ENDLINE);
        }
        request.append("content-length").append(":").append(httpRequest.getHttpContent() == null ? 0 : httpRequest.getHttpContent().length);
        request.append(StringUtil.ENDLINE);
        request.append("host").append(":").append(httpRequest.getUri().getHost());
        request.append(StringUtil.ENDLINE);
        request.append("connection").append(":").append("keep-alive");
        request.append(StringUtil.ENDLINE);
        request.append("accept-encoding").append(":").append("gzip,deflate");
        request.append(StringUtil.ENDLINE);
        request.append("user-agent").append(":").append("IAmFuckingYourMind/6.3.2");
        request.append(StringUtil.ENDLINE);
        request.append(StringUtil.ENDLINE);
        if (httpRequest.getHttpContent() == null) {
            request.append("");
        } else if (httpRequest.getHttpContent().length > 8 * 1024) {
            request.append("REQUEST BOOOOOOODY　TOOOOOO LARGE");
        } else {
            request.append(new String(httpRequest.getHttpContent(), Charset.forName("UTF-8")));
        }
        return request.toString();
    }

}
