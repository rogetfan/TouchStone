package com.linekong.platform.util;

public class StringUtil {
	 
	public static final String EmptyString = "";
	public static final String ENDLINE = "/r/n";
	public static final String TAB = "        ";
	public static final String SHARP = "#";
	public static final String COMMA =  ",";
	public static final String SPACE =" ";
	public static final String LEFT_BRACE = "{";
	public static final String RIGHT_BRACE = "}";
	public static final String LEFT_BRACKET = "[";
	public static final String RIGHT_BRACKET = "]";
	public static final String LEFT_PARENTHESIS = "(";
	public static final String RIGHT_PARENTHESIS = ")";
	public static final String UNDER_SCORE = "_";
	public static final String HYPHEN = "-";
	
	public static String getThreadName(Thread t)
	{
		if (t == null)
			return EmptyString;

		if (t.getName().length() > 64)
			return t.getName().substring(0, 63);
		else
			return t.getName();
	}
	
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.isEmpty();
	}

	public static boolean compare(String s1, String s2) {
		return (s1 == s2) || (s1 != null && s2 != null & s1.equals(s2));
	}
}
