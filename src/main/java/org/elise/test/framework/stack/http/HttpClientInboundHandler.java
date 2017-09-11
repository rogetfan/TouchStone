package org.elise.test.framework.stack.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.elise.test.framework.transaction.http.HttpResultCallBack;
import org.elise.test.tracer.Tracer;
import org.elise.test.util.StringUtil;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Glenn on 2017/9/8.
 */
public class HttpClientInboundHandler extends ChannelInboundHandlerAdapter {
    public static Tracer tracer = Tracer.getInstance(HttpClientInboundHandler.class);

    public HttpClientInboundHandler() {
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
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            if(tracer.isInfoAvailable()) {
                StringBuilder sb = new StringBuilder();
                sb.append(response.protocolVersion());
                sb.append(StringUtil.SPACE);
                sb.append(response.status());
                sb.append(StringUtil.ENDLINE);
                for (Map.Entry<String, String> entry : response.headers().entries()) {
                    sb.append(entry.getKey());
                    sb.append(":");
                    sb.append(entry.getValue());
                    sb.append(StringUtil.ENDLINE);
                }
                sb.append(StringUtil.ENDLINE);
                sb.append(StringUtil.ENDLINE);
                if(response.content().readableBytes() > 128*1024){
                    sb.append("REQUEST BOOOOOOODY　TOOOOOO LARGE");
                }else{
                    sb.append(response.content().toString(io.netty.util.CharsetUtil.UTF_8));
                }
                tracer.writeInfo(sb.toString());
            }
            ByteBuf buf = response.content();
            SocketAddress remoteAddress = ctx.channel().remoteAddress();
            HttpResultCallBack callBack = HttpClient.getCallBack(remoteAddress);
            callBack.success(buf.toString(io.netty.util.CharsetUtil.UTF_8), buf);
            buf.release();
        }

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
        ctx.fireExceptionCaught(cause);
    }

}
