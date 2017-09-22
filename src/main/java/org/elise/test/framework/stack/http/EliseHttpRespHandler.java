package org.elise.test.framework.stack.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.ReferenceCountUtil;
import org.elise.test.exception.InvalidResponseException;
import org.elise.test.framework.transaction.Response;
import org.elise.test.framework.transaction.Transaction;
import org.elise.test.framework.transaction.TransactionExecutor;
import org.elise.test.framework.transaction.future.FutureLevel;
import org.elise.test.framework.transaction.http.EliseHttpResponse;
import org.elise.test.tracer.Tracer;
import org.elise.test.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Glenn on 2017/9/8.
 */
public class EliseHttpRespHandler extends ChannelInboundHandlerAdapter {

    public static final Tracer TRACER = Tracer.getInstance(EliseHttpRespHandler.class);
    private TransactionExecutor executor = null;

    public EliseHttpRespHandler(TransactionExecutor executor) {
        this.executor = executor;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Transaction transaction = EliseHttpClient.getInstance().getConnection(ctx.channel().id().asShortText()).getTransaction();
        Long usedTimeStamp = System.currentTimeMillis()-transaction.getTransBeginTime();
        try {
            if (msg instanceof FullHttpResponse) {
                FullHttpResponse response = (FullHttpResponse) msg;
                // Write response log
                byte[] httpContent = new byte[response.content().readableBytes()];
                response.content().readBytes(httpContent);
                HashMap<String, String> headers = new HashMap<>();
                for (Map.Entry<String, String> entry : response.headers().entries())
                    headers.put(entry.getKey(), entry.getValue());
                Response resp = new EliseHttpResponse(response.status().code(),httpContent,headers);
                transaction.setResponse(resp);
                if (TRACER.isInfoAvailable()) {
                    writeResponseLog(ctx, transaction.getSequenceNum(),transaction.getStrResponse());
                }
                EliseHttpStatusHelper helper = EliseHttpStatusHelper.valueOf(response.status().code());
                switch (helper) {
                    case INFORMATIONAL:
                    case SUCCESSFUL:
                    case REDIRECTION:
                        executor.execFuture(transaction, FutureLevel.SUCCESS,null,usedTimeStamp);
                        break;
                    case CLIENT_ERROR:
                    case SERVER_ERROR:
                        executor.execFuture(transaction, FutureLevel.SUCCESS,null,usedTimeStamp);
                        break;
                    default:
                        executor.execFuture(transaction, FutureLevel.FAILED,new Exception("Unsupported HTTP Status"),usedTimeStamp);
                }
            }else{
                throw new InvalidResponseException("msg is not a FullHttpResponse");
            }
        } catch (Throwable t) {
            TRACER.writeError("Unknown Exception take place when handle response",t);
            executor.execFuture(transaction, FutureLevel.FAILED,t,usedTimeStamp);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void writeResponseLog(ChannelHandlerContext ctx, long sequenceNum, String response) {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------- ");
        sb.append("Channel Id:");
        sb.append(ctx.channel().id().asShortText());
        sb.append(" Sequence:");
        sb.append(sequenceNum);
        sb.append(" ---------------------");
        sb.append(StringUtil.ENDLINE);
        sb.append(response);
        TRACER.writeInfo(sb.toString());
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Transaction transaction = EliseHttpClient.getInstance().getConnection(ctx.channel().id().asShortText()).getTransaction();
        Long usedTimeStamp = System.currentTimeMillis()-transaction.getTransBeginTime();
        executor.execFuture(transaction,FutureLevel.FAILED,cause,usedTimeStamp);
        ctx.close().sync();
    }
}
