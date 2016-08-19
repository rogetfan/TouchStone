package com.linekong.platform.test;

import com.linekong.platform.lr.Actions;

public class Test {

	public static void main(String[] args) 
	{
		Actions a  = new Actions();
		try {
			a.init();
			a.action();
			Thread.sleep(100*1000);
			a.end();
		} catch (Throwable e) {
			
			e.printStackTrace();
		}
	}
}
