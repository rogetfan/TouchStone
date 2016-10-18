package org.elise.test.lr;
/**
 * LoadRunner Java script. (Build: _build_number_)
 * 
 * Script Description: 
 *                     
 */


public class Actions {

	public int init() throws Throwable 
	{
		return 0;
	}

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
	}// end of transaction

	public int end() throws Throwable {
		return 0;
	}
}
