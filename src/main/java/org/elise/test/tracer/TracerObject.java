package org.elise.test.tracer;

import org.elise.test.util.DateUtil;
import org.elise.test.util.StringUtil;

public class TracerObject {
	private long timeStamp;
	private String className;
	private String threadName;
	private TracerLevelEnum logLevel;
	private Throwable t;
	private String message;

	public TracerObject(long timeStamp, String className, String threadName, TracerLevelEnum logLevel, Throwable t,
						String message) {
		this.timeStamp = timeStamp;
		this.className = className;
		this.threadName = threadName;
		this.logLevel = logLevel;
		this.t = t;
		this.message = message;
	}

	public TracerLevelEnum getLogLevel(){
		return logLevel;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(DateUtil.getDateTimeString(this.timeStamp));
			sb.append(StringUtil.SPACE);
			sb.append(StringUtil.SPACE);
			sb.append(StringUtil.LEFT_BRACKET);
			sb.append(logLevel.getLevelName());
			sb.append(StringUtil.RIGHT_BRACKET);
			sb.append(StringUtil.HYPHEN);
			sb.append(StringUtil.LEFT_BRACKET);
			sb.append(className);
			sb.append(StringUtil.RIGHT_BRACKET);
			sb.append(StringUtil.HYPHEN);
			sb.append(StringUtil.LEFT_BRACKET);
			sb.append(threadName);
			sb.append(StringUtil.RIGHT_BRACKET);
			sb.append(StringUtil.ENDLINE);

			if (!StringUtil.isNullOrEmpty(message)) {
				sb.append(message);
				sb.append(StringUtil.ENDLINE);
				sb.append(StringUtil.ENDLINE);
			}
			if (t != null) {
				Throwable tempT = t;
				while (tempT != null) {
					sb.append(tempT.toString());
					sb.append(StringUtil.ENDLINE);
					for (StackTraceElement e : tempT.getStackTrace()) {
						sb.append(e.toString());
						sb.append(StringUtil.ENDLINE);
					}
					sb.append(StringUtil.ENDLINE);
					tempT = tempT.getCause();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
