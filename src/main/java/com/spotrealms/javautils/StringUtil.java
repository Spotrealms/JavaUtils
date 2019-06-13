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