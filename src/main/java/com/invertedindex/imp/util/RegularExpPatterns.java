package com.invertedindex.imp.util;



import java.util.regex.Pattern;

public interface RegularExpPatterns {
	

	
	public static final Pattern NUM_PATTERN 			=  Pattern.compile("[0-9]+");
	
	public static final Pattern AMOUNT_PATTERN 			=  Pattern.compile("\\d+(\\.\\d{1,3})?");
	
	public static final Pattern ALPHA_NUMERIC_PATTERN = Pattern.compile("[A-Za-z0-9\\._ ]+");
	
	public static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@ [A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	
	
}
