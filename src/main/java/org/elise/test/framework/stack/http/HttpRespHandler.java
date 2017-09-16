package org.elise.test.framework.stack.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.ReferenceCountUtil;
import org.elise.test.exception.InvalidResponseException;
import org.elise.test.framework.transaction.http.HttpResultCallBack;
import org.elise.test.tracer.Tracer;
import org.elise.test.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Glenn on 2017/9/8.
 */
public class HttpRespHandler extends ChannelInboundHandlerAdapter {

    public static final Tracer TRACER = Tracer.getInstance(HttpRespHandler.class);

    public HttpRespHandler() {
    }


    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
    }

    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HttpResultCallBack callBack = HttpClient.getCallBack(ctx.channel().id().asLongText());
        try {
            if (msg instanceof FullHttpResponse) {
                FullHttpResponse response = (FullHttpResponse) msg;
                // Write response log
                byte[] httpContent = new byte[response.content().readableBytes()];
                response.content().readBytes(httpContent);
                if (TRACER.isInfoAvailable()) {
                    writeResponseLog(ctx, callBack, httpContent, response.status().code(), response.protocolVersion().toString(), response.headers());
                }
                HashMap<String, String> headers = new HashMap<>();
                for (Map.Entry<String, String> entry : response.headers().entries())
                    headers.put(entry.getKey(), entry.getValue());
                HttpStatusHelper helper = HttpStatusHelper.valueOf(response.status().code());
                switch (helper) {
                    case INFORMATIONAL:
                    case SUCCESSFUL:
                        callBack.success(response.status().code(), httpContent, headers);
                        break;
                    case REDIRECTION:
                        callBack.redirect(response.status().code(), httpContent, headers);
                        break;
                    case CLIENT_ERROR:
                    case SERVER_ERROR:
                        callBack.error(response.status().code(), httpContent, headers);
                        break;
                    default:
                        callBack.failed(new Exception("Unsupported HTTP Status"));
                }
            }else{
                throw new InvalidResponseException("msg is not a FullHttpResponse");
            }
        } catch (Throwable t) {
            TRACER.writeError("Unknown Exception take place when handle response",t);
            callBack.failed(t);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void writeResponseLog(ChannelHandlerContext ctx, HttpResultCallBack callBack, byte[] httpContent, Integer status, String protocolVersion, HttpHeaders headers) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------- ");
        sb.append("Channel Id:");
        sb.append(ctx.channel().id().asLongText());
        sb.append(" Sequence:");
        sb.append(callBack.getSequenceNum());
        sb.append(" ---------------------");
        sb.append(StringUtil.ENDLINE);
        sb.append(protocolVersion);
        sb.append(StringUtil.SPACE);
        sb.append(status);
        sb.append(StringUtil.ENDLINE);
        for (Map.Entry<String, String> entry : headers.entries()) {
            sb.append(entry.getKey());
            sb.append(":");
            sb.append(entry.getValue());
            sb.append(StringUtil.ENDLINE);
        }
        sb.append(StringUtil.ENDLINE);
        sb.append(StringUtil.ENDLINE);
        if (httpContent.length > 128 * 1024) {
            sb.append("REQUEST BOOOOOOODY　TOOOOOO LARGE");
        } else {
            sb.append(new String(httpContent, "UTF-8"));
        }
        TRACER.writeInfo(sb.toString());
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        ctx.fireUserEventTriggered(evt);
    }

    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelWritabilityChanged();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        HttpResultCallBack callBack = HttpClient.getCallBack(ctx.channel().id().asLongText());
        callBack.failed(cause);
        ctx.close();
    }

}
