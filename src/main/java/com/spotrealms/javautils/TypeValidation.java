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
 *  <li>{@code double} validation</li>
 *  <li>{@code float} validation</li>
 *  <li>{@code int} validation</li>
 *  <li>{@code long} validation</li>
 *  <li>{@code short} validation</li>
 * </ul>
 * @author Spotrealms
 */
public class TypeValidation {	
	/**
	 * Check whether or not a {@code String} validates 
	 * to a {@code boolean}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code boolean}
	 */
	public static boolean isBool(String strIn){
		//Check if the String equals true or false
		if(strIn.equals("true") || strIn.equals("false")){
			//The String is a valid boolean, so return true
			return true;
		}
		else {
			//The boolean isn't valid, so return false
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
			//Try to parse the String as a double
			Double.parseDouble(strIn);
			
			//The String is a valid double, so return true
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
			//Try to parse the String as a float
			Float.parseFloat(strIn);
			
			//The String is a valid float, so return true
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
			//Try to parse the String as an integer
			Integer.parseInt(strIn);
			
			//The String is a valid integer, so return true
			return true;
		}
		catch(NumberFormatException invalidInt){
			//The integer isn't valid, so return false
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
	public static boolean isJSONValid(String testJSON){
		try {
			//Make a new JSON object from the String
			new JSONObject(testJSON);
		} 
		catch(JSONException ex){
			//Try to create a JSON array instead
			try {
				//Make a new JSONArray object instead
				new JSONArray(testJSON);
			} 
			catch(JSONException ex1){
				//Return false because the String isn't valid JSON
				return false;
			}
		}
		
		//Return true because the String is valid JSON
		return true;
	}
	
	/**
	 * Check whether or not a {@code String} validates 
	 * to a {@code long}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code long}
	 */
	public static boolean isLong(String strIn){
		try {
			//Try to parse the String as a long
			Long.parseLong(strIn);
			
			//The String is a valid long, so return true
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
			//Try to parse the String as a short
			Short.parseShort(strIn);
			
			//The String is a valid short, so return true
			return true;
		}
		catch(NumberFormatException invalidShort){
			//The short isn't valid, so return false
			return false;
		}
	}
}