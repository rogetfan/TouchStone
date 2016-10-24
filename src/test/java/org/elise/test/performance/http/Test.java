package org.elise.test.performance.http;

import org.elise.test.lr.Actions;

public class Test {

	public static void main(String[] args) 
	{
		final Actions a  = new Actions();
		try {
			a.init();
			//a.action();
			Thread.sleep(100*1000);
			a.end();
			System.out.println("Service End");
			Thread.sleep(4*1000);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
