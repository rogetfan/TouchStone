package com.linekong.platform;

public class Main {

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
