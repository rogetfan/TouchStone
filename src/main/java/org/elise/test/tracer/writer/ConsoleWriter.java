package org.elise.test.tracer.writer;

import java.util.concurrent.LinkedBlockingQueue;
import org.elise.test.tracer.TracerObject;

public class ConsoleWriter {

	private Runnable writeToConsole;
	private LinkedBlockingQueue<TracerObject> queue;

	public ConsoleWriter() {
		queue  = new LinkedBlockingQueue<TracerObject>();
		writeToConsole = new Runnable() {

			public void run() {

			}

		};
	}

	public void write(TracerObject log)
	{

	}

	public void start() {

	}

}
