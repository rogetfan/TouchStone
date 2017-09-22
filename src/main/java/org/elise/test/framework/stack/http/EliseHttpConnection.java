package org.elise.test.framework.stack.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.elise.test.framework.stack.Connection;
import org.elise.test.framework.transaction.Transaction;
import org.elise.test.framework.transaction.TransactionExecutor;
import org.elise.test.framework.transaction.future.FutureLevel;
import org.elise.test.tracer.Tracer;
import org.elise.test.util.StringUtil;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Glenn on  2017/9/14 0014 10:56.
 */


public class EliseHttpConnection implements Connection {

    public static final Tracer TRACER = Tracer.getInstance(EliseHttpConnection.class);

    private final ConcurrentLinkedQueue<Transaction> callBackQueue = new ConcurrentLinkedQueue<>();
    private final AtomicLong counter = new AtomicLong();
    private Channel channel;
    private final Object connLock = new Object();
    private EliseHttpClient client;
    private Integer index;
    private SocketAddress address;
    private TransactionExecutor executor = null;

    public EliseHttpConnection(EliseHttpClient client, Integer index, SocketAddress address, TransactionExecutor executor) {
        this.index = index;
        this.client = client;
        this.address = address;
        this.executor = executor;
    }

    private void reset() {
        counter.getAndSet(0);
        callBackQueue.clear();
    }

    public Transaction getTransaction() {
        return callBackQueue.poll();
    }

    @Override
    public void invoke(Object request, final Object attachment) {
        Transaction transaction = (Transaction) attachment;
        channel.writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> {
            Long usedTimeStamp = System.currentTimeMillis() - transaction.getTransBeginTime();
            try {
                if (channelFuture.isDone()) {
                    if (channelFuture.isSuccess()) {
                        transaction.setSequenceNum(counter.incrementAndGet());
                        // Write request log
                        if (TRACER.isInfoAvailable()) {
                            writeRequestLog(transaction.getStrRequest(), transaction.getSequenceNum());
                        }
                        callBackQueue.add(transaction);
                    } else if (channelFuture.isCancelled()) {
                        close();
                        TRACER.writeError("Send request to remote " + channelFuture.channel().remoteAddress().toString() + " has been canceled");
                        executor.execFuture(transaction, FutureLevel.UNREACHABLE, null, usedTimeStamp);
                    } else if (channelFuture.cause() != null) {
                        TRACER.writeError("Exception take place when send request to remote " + channelFuture.channel().remoteAddress());
                        close();
                        executor.execFuture(transaction, FutureLevel.FAILED, channelFuture.cause(), usedTimeStamp);
                    }
                } else {
                    TRACER.writeError("Send request to remote " + channelFuture.channel().remoteAddress().toString() + " failed");
                    executor.execFuture(transaction, FutureLevel.UNREACHABLE, null, usedTimeStamp);
                }
            } catch (Throwable t) {
                TRACER.writeError("Unknown Exception take place when send request");
                executor.execFuture(transaction, FutureLevel.FAILED, t, usedTimeStamp);
            }
        });
    }


    public void close() throws InterruptedException {
        channel.close().sync();
    }

    public void connect() throws IOException, InterruptedException {
        synchronized (connLock) {
            if (channel == null) {
                client.getBootstrap().connect(address).sync();
                ChannelFuture future = client.getBootstrap().connect(address).sync();
                channel = future.channel();
                client.register(getKey(), this);
                TRACER.writeInfo("Channel is null and connect to " + address.toString() + " successfully");
            } else if (!channel.isRegistered() || !channel.isActive()) {
                if (callBackQueue != null && !callBackQueue.isEmpty()) {

                    for (Transaction transaction : callBackQueue) {
                        Long usedTimeStamp = System.currentTimeMillis() - transaction.getTransBeginTime();
                        executor.execFuture(transaction, FutureLevel.FAILED, new Exception("Channel has been destroy"), usedTimeStamp);
                    }
                }
                client.unregister(getKey());
                ChannelFuture future = client.getBootstrap().connect(address).sync();
                channel = future.channel();
                reset();
                TRACER.writeInfo("Channel is abandon and then connect to " + address.toString() + " successfully");
                client.register(getKey(), this);
            } else {
                TRACER.writeInfo("Channel is useful and active");
            }
        }
    }

    private String getKey() {
        return channel.id().asShortText();
    }

    private void writeRequestLog(String request, long sequenceId) {
        StringBuilder req = new StringBuilder();
        req.append("--------------------- ");
        req.append("Channel Id:");
        req.append(getKey());
        req.append(" Sequence:");
        req.append(sequenceId);
        req.append(" ---------------------");
        req.append(StringUtil.ENDLINE);
        req.append(request);
        TRACER.writeInfo(req.toString());
    }
}
