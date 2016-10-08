package org.elise.test.tracer.writer;

import java.util.concurrent.LinkedBlockingQueue;

import org.elise.test.tracer.TracerObject;

public class ConsoleWriter {

	private Runnable writeToConsole;
	private LinkedBlockingQueue<TracerObject> queue;

	public ConsoleWriter() {
		writeToConsole = new Runnable() {

			public void run() {
				// TODO Auto-generated method stub

			}

		};
	}

	public void start() {

	}

}
