/**
 * JavaUtils: A collection of utility methods and classes for your Java programs
 *   Copyright (C) 2015-2019  Spotrealms Network
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

package com.spotrealms.javautils.math;

//Import Java classes and dependencies
import java.math.BigInteger;

//TODO: Add JavaDoc

public class NumberSystem {
	public static String decToHex(long decIn){
		//Derive the class-form of the input long and convert it to hexadecimal, then return the hex number
		return (Long.toHexString(Long.valueOf(decIn)));
	}
	
	public static long hexToDec(String hexIn){
		//Convert the hex string to a decimal and return the parsed long
		return (Long.parseLong(hexIn, 16));
	}
	
	private static BigInteger strToBigInt(String strIn, int numBase){
		//Convert the input to a BigInteger and return it
		return new BigInteger(strIn, numBase);
	}
	
	public static String strDecToHex(String decIn){
		//Remove all non-numeric chars from the string
		decIn = decIn.replaceAll("[^0-9]", "");
		
		//Get the BigInteger form of the string and return the result as a hexadecimal number
		return (strToBigInt(decIn, 10).toString(16));
	}
	
	public static String strHexToDec(String hexIn){
		//Remove all non-numeric/non-hex chars from the string
		hexIn = hexIn.replaceAll("(?i)[^0-9A-F]+", "");
		
		//Get the BigInteger form of the string and return the result as a decimal number
		return (strToBigInt(hexIn, 16).toString(10));
	}
}
