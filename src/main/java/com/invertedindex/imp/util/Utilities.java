package com.invertedindex.imp.util;

public final class Utilities {
	
	
	
	private Utilities() {
		
	}
	

	
	
	public static<T> boolean isNull(T reference) {
		if (reference == null || reference.equals("")) {
			return true;
		}
		return false;
	}
	
		
}
