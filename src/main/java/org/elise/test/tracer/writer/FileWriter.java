package org.elise.test.tracer.writer;

import org.elise.test.tracer.TracerObject;
import java.util.concurrent.LinkedBlockingQueue;



/**
 * Created by Glenn on 2016/10/25.
 */
public class FileWriter {

    Runnable writer;
    private  static LinkedBlockingQueue<TracerObject> container;
    public FileWriter(){

    }

}
