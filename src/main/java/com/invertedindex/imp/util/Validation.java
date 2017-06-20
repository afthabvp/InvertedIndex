package com.invertedindex.imp.util;


import java.util.regex.Pattern;


public class Validation {




	/**
	 * @param input.
	 * @return This method checks if input contains special characters
	 */
	public static boolean isContainsSpecialCharacter(String input) {
		String special = "?=.*[@#$%^&+=!_]";
		String pattern = ".*[" + Pattern.quote(special) + "].*";
		return input.matches(pattern);
	}


	/**
	 * @param input.
	 * @return This method checks if input is an AlphaNumeric
	 */
	public static boolean isAlphaNumeric(String input) {
		boolean gotnumber = false;
		boolean gotaplha = false;
		int length = input.length();
		for (int i = 0; i < length; i++) {
			char chr = input.charAt(i);
			if (!gotnumber && isInteger(String.valueOf(chr))) {
				gotnumber = true;
			}
			if (!gotaplha && isAlphabets(String.valueOf(chr))) {
				gotaplha = true;
			}
			if (gotaplha && gotnumber) {
				return true;
			}
		}
		return false;
	}


	/**
	 * @param input.
	 * @return This method checks if input is Alphabets
	 */
	public static boolean isAlphabets(String input) {
		String pattern = "^[a-zA-Z]*$";
		if (input.matches(pattern)) {
			return true;
		}
		return false;
	}

	public static boolean isInteger(String input) {
		try {
			String pattern = "-?[0-9]+";
			if (!Utilities.isNull(input)) {
				if (Pattern.compile(pattern).matcher(input.trim()).matches()) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	 
	   public static boolean isValidAmount(String amount) {
	     try {
        Double.valueOf(amount);
        
      } catch (Exception e) {
        return false;
      }
      
	     return true;
	   }
	   
	public static boolean isContainsUpperCase(String input) {
		return (input.matches(".*[A-Z].*"));
	}


}
