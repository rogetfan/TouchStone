package org.elise.test.tracer.writer;

import org.elise.test.tracer.TracerObject;
import java.util.concurrent.LinkedBlockingQueue;

public class FileWriter {

    Runnable writer;
    private  static LinkedBlockingQueue<TracerObject> container;
    public FileWriter(){

    }

}
