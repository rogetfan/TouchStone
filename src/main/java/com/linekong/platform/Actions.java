package com.linekong.platform;
/**
 * LoadRunner Java script. (Build: _build_number_)
 * 
 * Script Description: 
 *                     
 */

import com.linekong.platform.lr.LrTransHelper;
import com.linekong.platform.lr.LrTransManager;
import com.linekong.platform.lr.LrTransStatus;

import lrapi.lr;

public class Actions {

	public int init() throws Throwable {
		System.out.println("dassdadada");
		return 0;
	}// end of init

	public int action() throws Throwable {
		while (true) {
			try {
				LrTransStatus status = LrTransManager.takeTransaction();
				if (status == null) {
					continue;
				}
				LrTransHelper.set_transaction(status);
				
			} catch (Throwable t) {
				LrTransHelper.error_message(t);
			}

		}
	}// end of action

	public int end() throws Throwable {
		return 0;
	}// end of end
}
