package org.elise.test.lr;
import lrapi.lr;

public class LrTransHelper {

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
				StringBuilder sb = new StringBuilder();
				Throwable tempT = t;
				while (tempT != null) {
					sb.append(tempT.toString());
					sb.append("\r\n");
					for (StackTraceElement e : tempT.getStackTrace()) {
						sb.append(e.toString());
						sb.append("\r\n");
					}
					sb.append("\r\n");
					tempT = tempT.getCause();
				}
				System.err.println(sb.toString());
				lr.error_message(sb.toString());
			} else {
				System.err.println("Error Stack Is Null");
				lr.error_message(null);
			}
		}
	}

	public static void error_message(String message) {
		if(isEnable) {
			if (message != null && !message.equals("")) {
				System.err.println(message);
				lr.error_message(message);
			} else {
				System.err.println("Error Message Is Null");
				lr.error_message(null);
			}
		}
	}
}
