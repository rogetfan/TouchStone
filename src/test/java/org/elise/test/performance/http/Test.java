package org.elise.test.performance.http;

import org.elise.test.lr.Actions;

public class Test {

	public static void main(String[] args) 
	{
		Actions a  = new Actions();
		try {
			a.init();
			//a.action();
			Thread.sleep(60*60*1000);
			a.end();
		} catch (Throwable e) {
			
			e.printStackTrace();
		}
	}
}
