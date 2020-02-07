/**
 * JavaUtils: A collection of utility methods and classes for your Java programs
 *   Copyright (C) 2015-2018  Spotrealms Network
 *
 *	This library is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as
 *	published by the Free Software Foundation, either version 3 of the 
 *	License, or (at your option) any later version.
 *
 *	This library is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *	Lesser General Public License for more details.
 *
 *	You should have received a copy of the GNU Lesser General Public
 *	License along with this library.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.spotrealms.javautils;

//Import Java classes and dependencies
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO: Finish up JavaDoc

public class StringUtil {
	/**
	 * Clean strings of certain types of characters
	 * <br>The following options are available:
	 * <ul>
	 * 	<li>Remove all non-ASCII ({@code ASCII})</li>
	 * 	<li>Remove all color codes ({@code COLOR})</li>
	 * 	<li>Remove all ASCII control codes ({@code CONTROL})</li>
	 * 	<li>Remove all non-printable Unicode characters ({@code PRINT})</li>
	 * 	<li>Use all of the above filters on the string ({@code ALL})</li>
	 * </ul>
	 * @param textIn - The string to filter
	 * @param cleanMode - The mode to use on the string (see above list of valid codes)
	 * @return <b>String</b> - The resulting cleaned string
	 */
	public static String cleanText(String textIn, String cleanMode){
		//Variable declaration
		String asciiRegex = "[^\\x00-\\x7F]";
		String colorRegex = "\u001B\\[[;\\d]*m";
		String controlRegex = "[\\\\p{Cntrl}&&[^\\r\\n\\t]]";
		String printRegex = "\\\\p{C}\"";
		boolean cleanAll = false;
		
		//Initialization
		cleanMode = cleanMode.toUpperCase();
		
		//Decide which characters to remove
		switch(cleanMode){
			//Use all regexes in the method
			case "ALL":
				cleanAll = true;
			break;
			//Strips off all non-ASCII characters
			case "ASCII":
				textIn = textIn.replaceAll(asciiRegex, "");	
			break;
			//Remove all color codes
			case "COLOR":
				textIn = textIn.replaceAll(colorRegex, "");	
			break;
			//Erases all the ASCII control characters
			case "CONTROL":
				textIn = textIn.replaceAll(controlRegex, "");	
			break;
			//Removes non-printable characters from Unicode
			case "PRINT":
				textIn = textIn.replaceAll(printRegex, "");	
			break;
			//Default to "ALL"
			default:
				cleanAll = true;
			break;
		}
		
		//Check if all types should be checked for
		if(cleanAll){
			//Use every regex in the method to clean the string
			textIn = textIn.replaceAll(asciiRegex, "");	
			textIn = textIn.replaceAll(colorRegex, "");	
			textIn = textIn.replaceAll(controlRegex, "");	
			textIn = textIn.replaceAll(printRegex, "");	
		}
		
		//Return the resulting string
		return textIn.trim();
	}
	
	public static String cloneStr(String cloneTarget, int cloneCount){
		//Initialization
		String outStr = "";
		
		//Check if the clone count is greater than or equal to one
		if(cloneCount >= 1){
			//Dupe the string
			outStr = String.format("%0" + cloneCount + "d", 0).replace("0", cloneTarget);
		}
		
		//Return the string with the number of desired clones
		return outStr;
	}
	
	public static int countInStr(String huntStr, String targetStr){
		//Count the number of times targetStr appears in huntStr
		return (huntStr.length() - huntStr.replace(targetStr, "").length()) / targetStr.length();
	}
	
	/**
	 * Decode a URL-encoded String into standard UTF-8 characters
	 * @param encodedURL - The string to be decoded
	 * @return <b>String</b> - The resulting decoded string
	 * @throws UnsupportedEncodingException If the encoded URL passed to the method is of an invalid encoding
	 */
	public static String decodeURL(String encodedURL) throws UnsupportedEncodingException {
		//Assign the URLs to temporary variables
		String prevURL = "";
		String decodedURL = encodedURL;  

		//Loop through the URL until it can no longer be decoded
		while(!prevURL.equals(decodedURL)){
			prevURL = decodedURL;
			//Decode the URL
			decodedURL = URLDecoder.decode(decodedURL, "UTF-8");  
		}
		
		//Return the decoded URL
		return decodedURL;  
	}
	
	/**
	 * Encode a UTF-8 String into valid characters for URLs and forms
	 * @param decodedURL - The string to be encoded
	 * @return <b>String</b> - The resulting encoded string
	 * @throws UnsupportedEncodingException If the decoded URL passed to the method is of an invalid encoding
	 */
	public static String encodeURL(String decodedURL) throws UnsupportedEncodingException {
		//Decode the URL and return it as a string
		String encodeURL = URLEncoder.encode(decodedURL, "UTF-8");
		return encodeURL;
	}
	
	/**
	 * Check if a {@code String} equals any element in
	 * a given {@code ArrayList}. Useful in cases where
	 * a {@code String} can equal multiple objects.
	 * @param tStr The {@code String} to use in the operation
	 * @param equalTargets A list of all objects that the input {@code String} can equal
	 * @param ignoreCase Specifies whether or not to ignore case in the comparison operation
	 * @param useRegex Specifies whether or not to match lines by using the {@code ArrayList} as regex patterns
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>boolean</b> The status as to whether or not the input {@code String} equals any object in the passed {@code ArrayList}
	 */
	public static <T> boolean equalsAny(String tStr, ArrayList<T> equalTargets, boolean ignoreCase, boolean useRegex){
		//Loop through the ArrayList of targets
		for(Object curTarget : equalTargets){
			//Check if case should be ignored
			if(ignoreCase){
				//Transform both the target element and the input string to lowercase
				tStr = tStr.toLowerCase();
				curTarget = curTarget.toString().toLowerCase();
			}
			
			//Check if regex should be used
			if(useRegex){
				//Check if there was a match
				if(matchesRegex(tStr, curTarget.toString())){
					//Return true because the string matches at least one regex
					return true;
				}
			}
			else {
				//Check if the input string equals the target element
				if(tStr.equals(curTarget)){
					//Return true because the string matches at least one equal target
					return true;
				}
			}
		}
		
		//Return false because the input string doesn't equal any of the equal targets
		return false;
	}
	
	/**
	 * Check if a {@code String} equals any element in
	 * a given {@code ArrayList}. Useful in cases where
	 * a {@code String} can equal multiple objects.
	 * @param tStr The {@code String} to use in the operation
	 * @param equalTarget A {@code String} that the input {@code String} should equal
	 * @param ignoreCase Specifies whether or not to ignore case in the comparison operation
	 * @param useRegex Specifies whether or not to match lines by using the {@code ArrayList} as regex patterns
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>boolean</b> The status as to whether or not the input {@code String} equals any object in the passed {@code ArrayList}
	 */
	public static <T> boolean equalsAny(String tStr, String equalTarget, boolean ignoreCase, boolean useRegex){
		//Redirect to the overloaded method
		return equalsAny(tStr, new ArrayList<String>(Arrays.asList(equalTarget)), ignoreCase, useRegex);
	}
	
	public static String escAllChars(String escStr){
		//Set the characters to insert
		String insertStr = "\\";
	  
		//Loop through every char in the string
		for(int i=0; i<escStr.length(); i=i+(insertStr.length()+1)){
			//Add the escape sequence at position i in the substring
			escStr = (escStr.substring(0, i) + insertStr + escStr.substring(i));
		}
	  
		//Return the final string
		return escStr;
	}
	
	/**
	 * Check if a given {@code String} is of a 
	 * null value. This is different from 
	 * {@code isNullOrVoid}, as this method will
	 * return {@code true} if and ONLY IF the input
	 * {@code String} is null.
	 * @param tStr The target {@code String}
	 * @return <b>boolean</b> The status of whether or 
	 * not the given {@code String} is null
	 * @see isNullOrVoid
	 */
	public static boolean isNull(String tStr){
		//Check if the string is null
		if(tStr != null){
			//String has a value tied to it or is simply blank
			return false;
		}
		else {
			//String is null
			return true;
		}
	}
	
	/**
	 * Check if a given {@code String} is of a 
	 * null value or is simply blank. This is 
	 * different from {@code isNull}, as this
	 * method will also return {@code true} if 
	 * the input {@code String} is empty.
	 * @param tStr The target {@code String}
	 * @return <b>boolean</b> The status of whether or 
	 * not the given {@code String} is null or empty
	 * @see isNull
	 */
	public static boolean isNullOrVoid(String tStr){
		//Check if the string is null
		if(tStr != null && !tStr.isEmpty()){
			//String has a value tied to it
			return false;
		}
		else {
			//String is null or blank
			return true;
		}
	}

	/**
	 * Version of lastIndexOf that uses regular 
	 * expressions (regex) for searching.
	 * @param huntStr The {@code String} in which to search for the pattern
	 * @param toFind The REGEX pattern to use on the input {@code String}
	 * @return <b>int</b> The index of the requested pattern, if found; returns "-1" otherwise
	 */
	public static int lastIndexOfRegex(String huntStr, String toFind){
		try {
			//Get all indexes of the regex matches
			int[] allMatches = regexIndexes(huntStr, toFind);
		
			//Return only the final element in the array
			return allMatches[(allMatches.length - 1)];
		}
		catch(IndexOutOfBoundsException e){
			//Return -1, since the element wasn't found in the string
			return -1;
		}
	}
	
	/**
	 * Finds the last index of the given regular 
	 * expression pattern (regexp) in the given 
	 * {@code String}, starting from the given 
	 * index (and conceptually going backwards).
	 * @param huntStr The {@code String} in which to search for the pattern
	 * @param toFind The REGEX pattern to use on the input {@code String}
	 * @param fromIndex Set the staring point in the input {@code String}
	 * @return <b>int</b> The index of the requested pattern, if found; returns "-1" otherwise
	 */
	public static int lastIndexOfRegex(String huntStr, String toFind, int fromIndex){
		//Return the last matched index in the input String
		return lastIndexOfRegex(huntStr.substring(0, fromIndex), toFind);
	}
	
	public String native2Ascii(String text) {
		if(text == null) {
			return text;
		}
		StringBuilder sb = new StringBuilder();
		for(char ch : text.toCharArray()){
			sb.append(native2Ascii(ch));
		}
		return sb.toString();
	}
	
	/**
	 * Check if an input {@code String} contains
	 * a given regex pattern
	 * @param tStr tStr The {@code String} to use in the operation
	 * @param regexStr tStr The regex to use in the operation
	 * @return <b>boolean</b> The status as to whether or not the input {@code String} matches the input regex
	 */
	public static boolean matchesRegex(String tStr, String regexStr){
		//Get all indexes of the regex matches
		int[] allMatches = regexIndexes(tStr, regexStr);
		
		//Check if the length is at least one and return the status as a boolean
		return (allMatches.length >= 1);
	}

	public String native2Ascii(char ch){
		if(ch > '\u007f'){
			StringBuilder sb = new StringBuilder();
			// write \udddd
			sb.append("\\u");
			StringBuffer hex = new StringBuffer(Integer.toHexString(ch));
			hex.reverse();
			int length = 4 - hex.length();
			for(int j = 0; j < length; j++){
				hex.append('0');
			}
			for(int j = 0; j < 4; j++){
				sb.append(hex.charAt(3 - j));
			}
			return sb.toString();
		} 
		else {
			return Character.toString(ch);
		}
	}
	
	/**
	 * Compiles a list of the indexes in a 
	 * given {@code String} in which a given
	 * regular expression pattern (regexp) 
	 * is found. The number of regex matches
	 * can also be found simply by using
	 * {@code regexIndexes.length}
	 * @param huntStr The {@code String} in which to search for the pattern
	 * @param toFind The REGEX pattern to use on the input {@code String}
	 * @return <b>int[]</b> A list of indexes in the input {@code String} where the regex matches
	 */
	public static int[] regexIndexes(String huntStr, String toFind){
		//Create an ArrayList to hold the matcher finds
		ArrayList<Integer> matchedIndexes = new ArrayList<>();
		
		//Set the regex pattern to use on the input String
		Pattern regexPattern = Pattern.compile(toFind);
		
		//Set the matcher to use
		Matcher patternMatcher = regexPattern.matcher(huntStr);
		
		//Loop while the regex pattern is located
		while(patternMatcher.find()){
			//Get the last index of the matched regex
			matchedIndexes.add(patternMatcher.start());
		}

		//Return the ArrayList as a primitive array of ints (Java SE 8 is required, as it uses stream() to achieve this)
		return matchedIndexes.stream().mapToInt(Integer::intValue).toArray();
	}
	
	/**
	 * Splits a {@code String} according to a regex, 
	 * keeping the delimiters after the elements 
	 * containing each of the split {@code String}
	 * elements
	 * @author Luigi Plinge
	 * @see <a href="https://coderanch.com/t/545716/java/split-string-delimiters#2476300">https://coderanch.com/t/545716/java/split-string-delimiters#2476300</a>
	 * @param inputStr The input {@code String} to split
	 * @param regexStr The regular expression upon which to split the input
	 * @param strOffset Shifts the split point by this number of characters to the left: should be equal or less than the splitter length
	 * @return <b>String[]</b> An array of {@code Strings}
	 */
	public static String[] splitAndKeep(String inputStr, String regexStr, int strOffset){
		//Create an ArrayList to hold the resulting array
		ArrayList<String> splitStr = new ArrayList<String>();
			
		//Compile the regex pattern and create a matcher to find the regex matches in the string
		Matcher regMatcher = Pattern.compile(regexStr).matcher(inputStr);
			
		//Create an integer to keep track of the position in the string
		int strPos = 0;
			
		//Loop while a match is found
		while(regMatcher.find()){
			//Add the found item from the regex matcher onto the ArrayList (from the current position to the difference of the match end and the offset) and remove all of the regex matches to avoid duplicating the delimiter
			splitStr.add(inputStr.substring(strPos, regMatcher.end() - strOffset).replaceAll(regexStr, ""));
				
			//Add the delimiter onto the ArrayList (from the start of the match to the difference of the match end and the offset)
			splitStr.add(inputStr.substring(regMatcher.start(), regMatcher.end() - strOffset));
				
			//Set the string position to be the difference of the match end and the offset
			strPos = (regMatcher.end() - strOffset);
		}
			
		//Check if the position is less than the length of the input
		if(strPos < inputStr.length()){
			//Add the input string to the array starting at the current string position
			splitStr.add(inputStr.substring(strPos));
		}
			
		//Convert the ArrayList to an array of strings and return it
		return splitStr.toArray(new String[splitStr.size()]);
	}
		 
	/**
	 * Splits a {@code String} according to a regex, 
	 * keeping the delimiters after the elements 
	 * containing each of the split {@code String}
	 * elements
	 * @author Luigi Plinge
	 * @see <a href="https://coderanch.com/t/545716/java/split-string-delimiters#2476300">https://coderanch.com/t/545716/java/split-string-delimiters#2476300</a>
	 * @param inputStr The input {@code String} to split
	 * @param regexStr The regular expression upon which to split the input
	 * @return <b>String[]</b> An array of {@code Strings}
	 */
	public static String[] splitAndKeep(String inputStr, String regexStr){
		//Redirect to the overloaded method
		return splitAndKeep(inputStr, regexStr, 0);
	}
	
	/**
	 * Add ordinal suffixes to numbers (1st, 2nd, 3rd, etc)
	 * @param intIn The number to add the suffix to
	 * @return <b>String</b> The resulting number with ordinal suffix
	 */
	public static String suffixOrdinalNum(int intIn){
		//Set the list of valid ordinal suffixes
		String[] suffixList = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
		
		//Decide which suffix to add
		switch(intIn % 100){
			//11, 12, 13 do not follow typical conventions (1st, 2nd, 3rd), so they always get "th"
			case 11:
			case 12:
			case 13:
				return intIn + "th";
			//Return the corresponding ordinal suffix depending on the number's ones place digit
			default:
				return intIn + suffixList[intIn % 10];
		}
	}
	
	/**
	 * Safely encode a {@code String} into
	 * UTF-8 (takes care of any special 
	 * characters)
	 * @param strIn The target {@code String}
	 * @return <b>String</b> The resulting UTF-8 encoded {@code String}
	 * @throws UnsupportedEncodingException If the {@code String} passed to the method is of an invalid encoding
	 */
	public static String unicodeStr(String strIn) throws UnsupportedEncodingException {
		//Create the resulting string
		String resText = null;
		
		//Create a byte array from the input string
		byte[] utf8Bytes = strIn.getBytes("UTF8");
		
		//Create the new string from the byte array
		resText = new String(utf8Bytes,"UTF8");
		
		//Return the resulting text
		return resText;
	}
}