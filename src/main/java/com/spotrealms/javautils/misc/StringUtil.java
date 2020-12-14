/*
 * JavaUtils: A collection of utility methods and classes for your Java programs
 *   Copyright (C) 2015-2020  Spotrealms Network
 *
 *    This library is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as
 *    published by the Free Software Foundation, either version 3 of the 
 *    License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.spotrealms.javautils.misc;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A collection of methods for working
 * with strings.
 *
 * @author Spotrealms
 */
public final class StringUtil {
	/**
	 * Prevents instantiation of the utility class StringUtil.
	 *
	 * @throws RuntimeException If instantiation occurs
	 */
	private StringUtil(){ throw new RuntimeException("No " + this.getClass().getSimpleName() + " instance for you :)"); }

	//TODO: Use enums instead of characters
	/**
	 * Clean strings of certain types of characters.
	 * <br>The following options are available:
	 * <ul>
	 * 	<li>Remove all non-ASCII ({@code ASCII})</li>
	 * 	<li>Remove all color codes ({@code COLOR})</li>
	 * 	<li>Remove all ASCII control codes ({@code CONTROL})</li>
	 * 	<li>Remove all non-printable Unicode characters ({@code PRINT})</li>
	 * 	<li>Use all of the above filters on the string ({@code ALL})</li>
	 * </ul>
	 *
	 * @author Lokesh Gupta
	 * @param textIn The string to filter
	 * @param cleanMode The mode to use on the string (see above list of valid codes)
	 * @return <b>String</b> The resulting cleaned string
	 * @see <a href="https://howtodoinjava.com/java/regex/java-clean-ascii-text-non-printable-chars/">
	 *          https://howtodoinjava.com/java/regex/java-clean-ascii-text-non-printable-chars/
	 *      </a>
	 */
	public static String cleanText(final String textIn, final String cleanMode){
		//Variable declaration
		String asciiRegex = "[^\\x00-\\x7F]";
		String colorRegex = "\u001B\\[[;\\d]*m";
		String controlRegex = "[\\p{Cntrl}&&[^\r\n\t]]";
		String printRegex = "\\p{C}";

		//Initialization
		String cleanOut = textIn.toUpperCase(Locale.ROOT);

		//Decide which characters to remove
		switch(cleanMode.toUpperCase(Locale.ROOT)){
			//Strips off all non-ASCII characters
			case "ASCII":{
				cleanOut = cleanOut.replaceAll(asciiRegex, "");
				break;
			}
			//Remove all color codes
			case "COLOR":{
				cleanOut = cleanOut.replaceAll(colorRegex, "");
				break;
			}
			//Erases all the ASCII control characters
			case "CONTROL":{
				cleanOut = cleanOut.replaceAll(controlRegex, "");
				break;
			}
			//Removes non-printable characters from Unicode
			case "PRINT":{
				cleanOut = cleanOut.replaceAll(printRegex, "");
				break;
			}
			//Default to "ALL"
			default:{
				//Use every regex in the method to clean the string
				cleanOut = cleanOut.replaceAll(asciiRegex, "");
				cleanOut = cleanOut.replaceAll(colorRegex, "");
				cleanOut = cleanOut.replaceAll(controlRegex, "");
				cleanOut = cleanOut.replaceAll(printRegex, "");
				break;
			}
		}

		//Return the resulting string
		return cleanOut.trim();
	}
	
	/**
	 * Clones a {@code String} x amount of times.
	 *
	 * @param cloneTarget The string to clone
	 * @param cloneCount The amount of times to clone the target
	 * @return <b>String</b> The new cloned string
	 */
	public static String cloneStr(final String cloneTarget, final int cloneCount){
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
	
	/**
	 * Check if a {@code String} contains any element in
	 * a given {@code ArrayList}. Useful in cases where
	 * a {@code String} can contain multiple objects.
	 *
	 * @param tStr The {@code String} to use in the operation
	 * @param contTargets A list of all objects that the input {@code String} can contain
	 * @param ignoreCase Specifies whether or not to ignore case in the comparison operation
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>boolean</b> The status as to whether or not the input {@code String} contains any object in the passed {@code ArrayList}
	 */
	public static <T> boolean containsAny(final String tStr, final List<T> contTargets, final boolean ignoreCase){
		//Initialization
		String hunteeStr = tStr;

		//Loop through the ArrayList of targets
		for(Object curTarget : contTargets){
			//Check if case should be ignored
			if(ignoreCase){
				//Transform both the target element and the input string to lowercase
				hunteeStr = hunteeStr.toLowerCase(Locale.ROOT);
				curTarget = curTarget.toString().toLowerCase(Locale.ROOT);
			}
			
			//Check if the input string contains the target element
			if(hunteeStr.contains(curTarget.toString())){
				//Return true because the string contains at least one contain target
				return true;
			}
		}
		
		//Return false because the input string doesn't contain any of the contain targets
		return false;
	}
	
	/**
	 * Check if a {@code String} contains any element in
	 * a given array. Useful in cases where a {@code String} 
	 * can contain multiple objects.
	 *
	 * @param tStr The {@code String} to use in the operation
	 * @param contTargets An array of all objects that the input {@code String} can contain
	 * @param ignoreCase Specifies whether or not to ignore case in the comparison operation
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>boolean</b> The status as to whether or not the input {@code String} contains any object in the passed {@code ArrayList}
	 */
	public static <T> boolean containsAny(final String tStr, final T[] contTargets, final boolean ignoreCase){
		//Convert the generic array to a generic ArrayList
		ArrayList<T> genArrList = new ArrayList<>(Arrays.asList(contTargets));
		
		//Pass the ArrayList to the overloaded equalsAny method and return the result
		return containsAny(tStr, genArrList, ignoreCase);
	}
	
	/**
	 * Counts the number of times a {@code String}
	 * appears inside of a bigger {@code String}.
	 *
	 * @param huntStr The string to test
	 * @param targetStr The string to look for
	 * @return <b>int</b> The number of times the target string appears in the hunt string
	 */
	public static int countInStr(final String huntStr, final String targetStr){
		//Count the number of times targetStr appears in huntStr
		return (huntStr.length() - huntStr.replace(targetStr, "").length()) / targetStr.length();
	}
	
	/**
	 * Decode a URL-encoded String into standard UTF-8 characters.
	 *
	 * @param encodedUrl The string to be decoded
	 * @return <b>String</b> The resulting decoded string
	 * @throws UnsupportedEncodingException If the encoded URL passed to the method is of an invalid encoding
	 */
	public static String decodeUrl(final String encodedUrl) throws UnsupportedEncodingException {
		//Assign the URLs to temporary variables
		String prevUrl = "";
		String decodedUrl = encodedUrl;

		//Loop through the URL until it can no longer be decoded
		while(!prevUrl.equals(decodedUrl)){
			prevUrl = decodedUrl;
			//Decode the URL
			decodedUrl = URLDecoder.decode(decodedUrl, "UTF-8");
		}
		
		//Return the decoded URL
		return decodedUrl;
	}
	
	/**
	 * Encode a UTF-8 String into valid characters for URLs and forms.
	 *
	 * @param decodedUrl The string to be encoded
	 * @return <b>String</b> The resulting encoded string
	 * @throws UnsupportedEncodingException If the decoded URL passed to the method is of an invalid encoding
	 */
	public static String encodeUrl(final String decodedUrl) throws UnsupportedEncodingException {
		//Decode the URL and return it as a string
		return URLEncoder.encode(decodedUrl, "UTF-8");
	}
	
	/**
	 * Check if a {@code String} equals any element in
	 * a given {@code ArrayList}. Useful in cases where
	 * a {@code String} can equal multiple objects.
	 *
	 * @param tStr The {@code String} to use in the operation
	 * @param equalTargets A list of all objects that the input {@code String} can equal
	 * @param ignoreCase Specifies whether or not to ignore case in the comparison operation
	 * @param useRegex Specifies whether or not to match lines by using the {@code ArrayList} as regex patterns
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>boolean</b> The status as to whether or not the input {@code String} equals any object in the passed {@code ArrayList}
	 */
	public static <T> boolean equalsAny(final String tStr, final List<T> equalTargets, final boolean ignoreCase, final boolean useRegex){
		//Initialization
		String strOut = tStr;

		//Loop through the ArrayList of targets
		for(Object curTarget : equalTargets){
			//Check if case should be ignored
			if(ignoreCase){
				//Transform both the target element and the input string to lowercase
				strOut = strOut.toLowerCase(Locale.ROOT);
				curTarget = curTarget.toString().toLowerCase(Locale.ROOT);
			}
			
			//Check if regex should be used
			if(useRegex){
				//Check if there was a match
				if(matchesRegex(strOut, curTarget.toString())){
					//Return true because the string matches at least one regex
					return true;
				}
			}
			else {
				//Check if the input string equals the target element
				if(strOut.equals(curTarget)){
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
	 * a given array. Useful in cases where a {@code String}
	 * can equal multiple objects.
	 *
	 * @param tStr The {@code String} to use in the operation
	 * @param equalTargets An array of all objects that the input {@code String} can equal
	 * @param ignoreCase Specifies whether or not to ignore case in the comparison operation
	 * @param useRegex Specifies whether or not to match lines by using the {@code ArrayList} as regex patterns
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>boolean</b> The status as to whether or not the input {@code String} equals any object in the passed {@code ArrayList}
	 */
	public static <T> boolean equalsAny(final String tStr, final T[] equalTargets, final boolean ignoreCase, final boolean useRegex){
		//Convert the generic array to a generic ArrayList
		ArrayList<T> genArrList = new ArrayList<>(Arrays.asList(equalTargets));
		
		//Pass the ArrayList to the overloaded equalsAny method and return the result
		return equalsAny(tStr, genArrList, ignoreCase, useRegex);
	}
	
	/**
	 * Check if a {@code String} equals any element in
	 * a given {@code ArrayList}. Useful in cases where
	 * a {@code String} can equal multiple objects.
	 *
	 * @param tStr The {@code String} to use in the operation
	 * @param equalTarget A {@code String} that the input {@code String} should equal
	 * @param ignoreCase Specifies whether or not to ignore case in the comparison operation
	 * @param useRegex Specifies whether or not to match lines by using the {@code ArrayList} as regex patterns
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>boolean</b> The status as to whether or not the input {@code String} equals any object in the passed {@code ArrayList}
	 */
	public static <T> boolean equalsAny(final String tStr, final String equalTarget, final boolean ignoreCase, final boolean useRegex){
		//Redirect to the overloaded method
		return equalsAny(tStr, new ArrayList<>(Collections.singletonList(equalTarget)), ignoreCase, useRegex);
	}
	
	public static String escAllChars(final String escStr){
		//Initialization
		String escedStr = escStr;

		//Set the characters to insert
		String insertStr = "\\";

		//Loop through every char in the string
		for(int i = 0; i < escedStr.length(); i = i + (insertStr.length() + 1)){
			//Add the escape sequence at position i in the substring
			escedStr = escedStr.substring(0, i) + insertStr + escedStr.substring(i);
		}

		//Return the final string
		return escedStr;
	}
	
	/**
	 * Check if a given {@code String} is of a 
	 * null value or is simply empty.
	 *
	 * @param str The target {@code String}
	 * @return <b>boolean</b> The status of whether or 
	 *      not the given {@code String} is null or empty
	 */
	public static boolean isNullOrVoid(final String str){
		//Check if the string is null or empty and return the result
		return str == null || str.isEmpty();
	}

	/**
	 * Version of lastIndexOf that uses regular 
	 * expressions (regex) for searching.
	 *
	 * @param huntStr The {@code String} in which to search for the pattern
	 * @param toFind The REGEX pattern to use on the input {@code String}
	 * @return <b>int</b> The index of the requested pattern, if found; returns "-1" otherwise
	 */
	public static int lastIndexOfRegex(final String huntStr, final String toFind){
		try {
			//Get all indexes of the regex matches
			int[] allMatches = regexIndexes(huntStr, toFind);
		
			//Return only the final element in the array
			return allMatches[allMatches.length - 1];
		}
		catch(IndexOutOfBoundsException iobe){
			//Return -1, since the element wasn't found in the string
			return -1;
		}
	}
	
	/**
	 * Finds the last index of the given regular 
	 * expression pattern (regexp) in the given 
	 * {@code String}, starting from the given 
	 * index (and conceptually going backwards).
	 *
	 * @param huntStr The {@code String} in which to search for the pattern
	 * @param toFind The REGEX pattern to use on the input {@code String}
	 * @param fromIndex Set the staring point in the input {@code String}
	 * @return <b>int</b> The index of the requested pattern, if found; returns "-1" otherwise
	 */
	public static int lastIndexOfRegex(final String huntStr, final String toFind, final int fromIndex){
		//Return the last matched index in the input String
		return lastIndexOfRegex(huntStr.substring(0, fromIndex), toFind);
	}

	/**
	 * Check if an input {@code String} contains
	 * a given regex pattern.
	 *
	 * @param tStr tStr The {@code String} to use in the operation
	 * @param regexStr tStr The regex to use in the operation
	 * @return <b>boolean</b> The status as to whether or not the input {@code String} matches the input regex
	 */
	public static boolean matchesRegex(final String tStr, final String regexStr){
		//Get all indexes of the regex matches
		int[] allMatches = regexIndexes(tStr, regexStr);
		
		//Check if the length is at least one and return the status as a boolean
		return allMatches.length >= 1;
	}
	
	/**
	 * Compiles a list of the indexes in a 
	 * given {@code String} in which a given
	 * regular expression pattern (regexp) 
	 * is found. The number of regex matches
	 * can also be found simply by using
	 * {@code regexIndexes.length}.
	 *
	 * @param huntStr The {@code String} in which to search for the pattern
	 * @param toFind The REGEX pattern to use on the input {@code String}
	 * @return <b>int[]</b> A list of indexes in the input {@code String} where the regex matches
	 */
	public static int[] regexIndexes(final String huntStr, final String toFind){
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
	 * elements.
	 *
	 * @author Luigi Plinge
	 * @param inputStr The input {@code String} to split
	 * @param regexStr The regular expression upon which to split the input
	 * @param strOffset Shifts the split point by this number of characters to the left: should be equal or less than the splitter length
	 * @return <b>String[]</b> An array of {@code Strings}
	 * @see <a href="https://coderanch.com/t/545716/java/split-string-delimiters#2476300">
	 *          https://coderanch.com/t/545716/java/split-string-delimiters#2476300
	 *      </a>
	 */
	public static String[] splitAndKeep(final String inputStr, final String regexStr, final int strOffset){
		//Create an ArrayList to hold the resulting array
		ArrayList<String> splitStr = new ArrayList<>();
			
		//Compile the regex pattern and create a matcher to find the regex matches in the string
		Matcher regMatcher = Pattern.compile(regexStr).matcher(inputStr);
			
		//Create an integer to keep track of the position in the string
		int strPos = 0;
			
		//Loop while a match is found
		while(regMatcher.find()){
			//Add the found item from the regex matcher onto the ArrayList
			//(from the current position to the difference of the match end and the offset)
			//and remove all of the regex matches to avoid duplicating the delimiter
			splitStr.add(inputStr.substring(strPos, regMatcher.end() - strOffset).replaceAll(regexStr, ""));
				
			//Add the delimiter onto the ArrayList (from the start of the match to the difference of the match end and the offset)
			splitStr.add(inputStr.substring(regMatcher.start(), regMatcher.end() - strOffset));

			//Set the string position to be the difference of the match end and the offset
			strPos = regMatcher.end() - strOffset;
		}
			
		//Check if the position is less than the length of the input
		if(strPos < inputStr.length()){
			//Add the input string to the array starting at the current string position
			splitStr.add(inputStr.substring(strPos));
		}
			
		//Convert the ArrayList to an array of strings and return it
		return splitStr.toArray(new String[0]);
	}
		 
	/**
	 * Splits a {@code String} according to a regex, 
	 * keeping the delimiters after the elements 
	 * containing each of the split {@code String}
	 * elements.
	 *
	 * @author Luigi Plinge
	 * @param inputStr The input {@code String} to split
	 * @param regexStr The regular expression upon which to split the input
	 * @return <b>String[]</b> An array of {@code Strings}
	 * @see <a href="https://coderanch.com/t/545716/java/split-string-delimiters#2476300">
	 *          https://coderanch.com/t/545716/java/split-string-delimiters#2476300
	 *      </a>
	 */
	public static String[] splitAndKeep(final String inputStr, final String regexStr){
		//Redirect to the overloaded method
		return splitAndKeep(inputStr, regexStr, 0);
	}
	
	/**
	 * Add ordinal suffixes to numbers (1st, 2nd, 3rd, etc).
	 *
	 * @param intIn The number to add the suffix to
	 * @return <b>String</b> The resulting number with ordinal suffix
	 */
	public static String suffixOrdinalNum(final int intIn){
		//Set the list of valid ordinal suffixes
		String[] suffixList = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };

		//Initialization of "magic numbers" (avoids CheckStyle errors)
		final int CLAMP_100 = 100;
		final int SPECIAL_SUFFIX_11 = 11;
		final int SPECIAL_SUFFIX_12 = 12;
		final int SPECIAL_SUFFIX_13 = 13;

		//Decide which suffix to add
		switch(intIn % CLAMP_100){
			//11, 12, 13 do not follow typical conventions (1st, 2nd, 3rd), so they always get "th"
			case SPECIAL_SUFFIX_11:
			case SPECIAL_SUFFIX_12:
			case SPECIAL_SUFFIX_13:
				return intIn + "th";
			//Return the corresponding ordinal suffix depending on the number's ones place digit
			default:
				return intIn + suffixList[intIn % suffixList.length];
		}
	}
	
	/**
	 * Safely encode a {@code String} into
	 * UTF-8 (takes care of any special 
	 * characters).
	 *
	 * @param strIn The target {@code String}
	 * @return <b>String</b> The resulting UTF-8 encoded {@code String}
	 */
	public static String unicodeStr(final String strIn){
		//Create the resulting string
		String resText;
		
		//Create a byte array from the input string
		byte[] utf8Bytes = strIn.getBytes(StandardCharsets.UTF_8);
		
		//Create the new string from the byte array
		resText = new String(utf8Bytes, StandardCharsets.UTF_8);
		
		//Return the resulting text
		return resText;
	}
}
