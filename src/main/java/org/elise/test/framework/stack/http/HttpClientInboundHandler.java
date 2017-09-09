package org.elise.test.framework.stack.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.elise.test.framework.transaction.http.HttpResultCallBack;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by huxuehan on 2017/9/8.
 */
public class HttpClientInboundHandler  extends ChannelInboundHandlerAdapter {

    private HttpResultCallBack callBack;

    public void setCallBack(HttpResultCallBack callBack){
        this.callBack = callBack;
    }

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
        if (msg instanceof FullHttpResponse)
        {
            FullHttpResponse response = (FullHttpResponse) msg;
            for(Map.Entry<String, String> entry:response.headers().entries()){
                System.out.println(entry.getKey()+":"+entry.getValue());
            }
            ByteBuf buf = response.content();
            callBack.success(buf.toString(io.netty.util.CharsetUtil.UTF_8),new Object());
            buf.release();
        }
        System.out.println(ctx.channel().id());

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
