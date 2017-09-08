package org.elise.test.lr;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * Created by Glenn on 2016/10/25.
 */
public class LrTransStatusManager {
	private static final LinkedBlockingQueue<LrTransStatus> CONTAINER = new LinkedBlockingQueue<LrTransStatus>();
	
	public static LrTransStatus takeTransaction() throws InterruptedException {
		//Get LrTransaction at once if object is not null,otherwise wait for 1 second at most.
		return CONTAINER.poll(1000, TimeUnit.MILLISECONDS);
	}

	public static void addStatus(String name, Double duration, Boolean pass) {
		LrTransStatus status = new LrTransStatus(name, duration, pass);
		CONTAINER.add(status);
	}
}
