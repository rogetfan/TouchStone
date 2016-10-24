package org.elise.test.lr;
import lrapi.lr;
import org.elise.test.tracer.Tracer;

public class LrTransHelper {

    private static final Tracer tracer = Tracer.getInstance(LrTransHelper.class);
	private static boolean isEnable = false;

	private LrTransHelper() {

	}

	public static void setEnable(){
		isEnable = true;
	}

	public static void setDisable(){
        isEnable = false;
	}

	public static void startTransaction(String transactionName){
		if(isEnable) {
			lr.start_transaction(transactionName);
		}
	}

	public static void endTransaction(String transactionName,Boolean isSuccessful){
		if(isEnable) {
			lr.end_transaction(transactionName, isSuccessful ? lr.PASS : lr.FAIL);
		}
	}

	public static void set_transaction(LrTransStatus status) {
		if(isEnable) {
			lr.set_transaction(status.transactionName, status.duration, status.status);
		}
	}

	public static void error_message(Throwable t) {
		if(isEnable) {
			if (t != null) {
				tracer.writeError("Error take place when write error message",t);
				lr.error_message("Error take place when write error message");
			} else {
				tracer.writeInfo("Error Stack Is Null");
				lr.error_message(null);
			}
		}
	}

	public static void error_message(String message) {
		if(isEnable) {
			if (message != null && !message.equals("")) {
				tracer.writeError(message);
				lr.error_message(message);
			} else {
				tracer.writeInfo("Error Message Is Null");
				lr.error_message(null);
			}
		}
	}
}
