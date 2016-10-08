package org.elise.test.lr;

import java.util.concurrent.LinkedBlockingQueue;

public class LrTransManager {
	private static final LinkedBlockingQueue<LrTransStatus> CONTAINER = new LinkedBlockingQueue<LrTransStatus>();
	
	public static LrTransStatus takeTransaction() throws InterruptedException {
		return CONTAINER.take();
	}

	public static void addTransaction(String name, Double duration, Boolean pass) {
		LrTransStatus status = new LrTransStatus(name, duration, pass);
		CONTAINER.add(status);
	}
}
