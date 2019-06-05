/**
 * JavaUtils: A collection of utility methods and classes for your Java programs
 *   Copyright (C) 2015-2018  Spotrealms Network
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

package com.spotrealms.javautils;

//Import Java classes and dependencies
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A series of methods for ensuring that a 
 * {@code String} validates to a specific 
 * data type. <br>The following data type
 * validation methods are available:
 * <ul>
 *  <li>{@code boolean} validation</li>
 *  <li>{@code byte} validation</li>
 *  <li>{@code char} validation</li>
 *  <li>{@code double} validation</li>
 *  <li>{@code float} validation</li>
 *  <li>{@code int} validation</li>
 *  <li>{@code long} validation</li>
 *  <li>{@code short} validation</li>
 *  <li>{@code JSON} validation</li>
 * </ul>
 * Additionally, an enum containing all of the
 * available types that can be checked is also
 * available, which is useful for determining 
 * which of these methods to use when validating
 * a {@code String} that is expected to be of an 
 * arbitrary type.
 * @author Spotrealms
 */
public class TypeValidation {
	/**
	 * Provides a list of all data types that can
	 * be validated using methods from the validation
	 * class. Useful when determining what a {@code String}
	 * should be expected to validate to.
	 */
	public static enum allValidationTypes {
		BOOLEAN, BYTE, CHAR, DOUBLE, FLOAT, INT, LONG, SHORT, JSON;
	}
	
	/**
	 * Check whether or not a {@code String} validates 
	 * to a {@code boolean}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code boolean}
	 */
	public static boolean isBool(String strIn){
		//Check if the string equals true or false
		if(strIn.equals("true") || strIn.equals("false")){
			//The string is a valid boolean, so return true
			return true;
		}
		else {
			//The boolean isn't valid, so return false
			return false;
		}
	}
	
	/**
	 * Check whether or not a {@code String} validates 
	 * to a {@code byte}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code byte}
	 */
	public static boolean isByte(String strIn){
		try {
			//Convert the string to an integer
			int testInt = Integer.decode(strIn);
		
			//Check if the integer is out of range (between -128 and 127), which prevents buffer overflows/underflows
			if((testInt > Byte.MAX_VALUE) || (testInt < Byte.MIN_VALUE)){
				//Return false, as the integer is out of range of the max and min values for bytes
				return false;
			}
			
			//Return true, as the integer parsed from the string is a valid byte that's in range
			return true;
		}
		catch(Exception e){
			//Return false, as there was an issue parsing the string as an integer
			return false;
		}
	}
	
	/**
	 * Check whether or not a {@code String} validates 
	 * to a {@code char}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code char}
	 */
	public static boolean isChar(String strIn){
		//Test if the string is of length 1
		if(strIn.length() == 1){
			//Return true because the string contains one and only one character, therefore it can be casted as a character
			return true;
		}
		else {
			//Return false because the string can't be casted to a character, as its length is not equal to that of a single character
			return false;
		}
	}
	
	/**
	 * Check whether or not a {@code String} validates 
	 * to a {@code double}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code double}
	 */
	public static boolean isDouble(String strIn){
		try {
			//Try to parse the string as a double
			Double.parseDouble(strIn);
			
			//The string is a valid double, so return true
			return true;
		}
		catch(NumberFormatException invalidDouble){
			//The double isn't valid, so return false
			return false;
		}
	}
	
	/**
	 * Check whether or not a {@code String} validates 
	 * to a {@code float}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code float}
	 */
	public static boolean isFloat(String strIn){
		try {
			//Try to parse the string as a float
			Float.parseFloat(strIn);
			
			//The string is a valid float, so return true
			return true;
		}
		catch(NumberFormatException invalidFloat){
			//The float isn't valid, so return false
			return false;
		}
	}
	
	/**
	 * Check whether or not a {@code String} validates 
	 * to an {@code int}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code int}
	 */
	public static boolean isInt(String strIn){
		try {
			//Try to parse the string as an integer
			Integer.parseInt(strIn);
			
			//The string is a valid integer, so return true
			return true;
		}
		catch(NumberFormatException invalidInt){
			//The integer isn't valid, so return false
			return false;
		}
	}
	
	/**
	 * Check whether or not a {@code String} validates 
	 * to a {@code long}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code long}
	 */
	public static boolean isLong(String strIn){
		try {
			//Try to parse the string as a long
			Long.parseLong(strIn);
			
			//The string is a valid long, so return true
			return true;
		}
		catch(NumberFormatException invalidLong){
			//The long isn't valid, so return false
			return false;
		}
	}
	
	/**
	 * Check whether or not a {@code String} validates 
	 * to a {@code short}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code short}
	 */
	public static boolean isShort(String strIn){
		try {
			//Try to parse the string as a short
			Short.parseShort(strIn);
			
			//The string is a valid short, so return true
			return true;
		}
		catch(NumberFormatException invalidShort){
			//The short isn't valid, so return false
			return false;
		}
	}
	

	/**
	 * Check whether or not a {@code String} validates 
	 * to {@code JSON}
	 * @param testJSON The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is valid {@code JSON}
	 * @see <a href="https://stackoverflow.com/questions/10174898/how-to-check-whether-a-given-string-is-valid-json-in-java">https://stackoverflow.com/questions/10174898/how-to-check-whether-a-given-string-is-valid-json-in-java</a>
	 */
	public static boolean isValidJSON(String testJSON){
		try {
			//Make a new JSON object from the string
			new JSONObject(testJSON);
		} 
		catch(JSONException ex){
			//Try to create a JSON array instead
			try {
				//Make a new JSONArray object instead
				new JSONArray(testJSON);
			} 
			catch(JSONException ex1){
				//Return false because the string isn't valid JSON
				return false;
			}
		}
		
		//Return true because the string is valid JSON
		return true;
	}
}