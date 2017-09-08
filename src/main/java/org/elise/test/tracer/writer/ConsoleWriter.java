package org.elise.test.tracer.writer;

import org.elise.test.tracer.TracerObject;


/**
 * Created by Glenn on 2016/10/25.
 */
public class ConsoleWriter {

	public ConsoleWriter() {
	}
	public void write(TracerObject log) {
		System.out.println(log.toString());
	}
}
