package org.elise.test.tracer.writer;

import org.elise.test.tracer.TracerObject;

import java.util.concurrent.LinkedBlockingQueue;

public class RemoteWriter {

    Runnable writer;
    private  static LinkedBlockingQueue<TracerObject> container;
    public RemoteWriter(){

    }
}
