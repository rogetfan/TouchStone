package org.elise.test.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	private static final String COMPLETEDATEFORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final String SIMPLEDATEFORMAT = "yyyyMM";
	private static final String SIMPLEDATEFORMAT_DAY = "yyyyMMdd";

	private static ThreadLocal<SimpleDateFormat> threadLocalcompletedate = new ThreadLocal<SimpleDateFormat>() {
		protected synchronized SimpleDateFormat initialValue() {
			return new SimpleDateFormat(COMPLETEDATEFORMAT);
		}
	};

	private static ThreadLocal<SimpleDateFormat> threadLocalsimpledate = new ThreadLocal<SimpleDateFormat>() {
		protected synchronized SimpleDateFormat initialValue() {
			return new SimpleDateFormat(SIMPLEDATEFORMAT);
		}
	};

	private static ThreadLocal<SimpleDateFormat> threadLocalsimpledate_day = new ThreadLocal<SimpleDateFormat>() {
		protected synchronized SimpleDateFormat initialValue() {
			return new SimpleDateFormat(SIMPLEDATEFORMAT_DAY);
		}
	};

	public static String getDateTimeString(Long dateTime) {
		return threadLocalcompletedate.get().format(new Date(dateTime));
	}

	public static String getMonthTablePrefix(Date date) {
		return threadLocalsimpledate.get().format(date);
	}

	public static String getTablePrefix_day(Date date) {
		return threadLocalsimpledate_day.get().format(date);
	}

}
