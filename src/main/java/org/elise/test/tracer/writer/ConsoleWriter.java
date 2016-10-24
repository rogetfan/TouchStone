package org.elise.test.tracer.writer;

import org.elise.test.tracer.TracerObject;

public class ConsoleWriter {

	public ConsoleWriter() {
	}
	public void write(TracerObject log) {
		System.out.println(log.toString());
	}
}
