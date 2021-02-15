/*
 * JavaUtils: A collection of utility methods and classes for your Java programs
 *   Copyright (C) 2015-2021 Spotrealms Network
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

import java.math.BigInteger;

/**
 * A series of methods for working with
 * different number systems.
 *
 * @author Spotrealms
 */
public final class NumberSystem {
	/**
	 * Prevents instantiation of the utility class NumberSystem.
	 *
	 * @throws RuntimeException If instantiation occurs
	 */
	private NumberSystem(){ throw new RuntimeException("No " + this.getClass().getSimpleName() + " instance for you :)"); }

	/**
	 * Converts a decimal {@code long} to a
	 * hexadecimal {@code String}.
	 *
	 * @param decIn The number to convert to hexadecimal
	 * @return <b>String</b> The resulting hexadecimal string
	 */
	public static String decToHex(final long decIn){
		//Derive the class-form of the input long and convert it to hexadecimal, then return the hex number
		return Long.toHexString(decIn);
	}

	/**
	 * Converts a hexadecimal {@code String} to a
	 * decimal {@code long}.
	 *
	 * @param hexIn The number to convert to decimal
	 * @return <b>long</b> The resulting decimal long
	 */
	public static long hexToDec(final String hexIn){
		//Set "magic numbers"
		final int HEX_RADIX = 16;

		//Convert the hex string to a decimal and return the parsed long
		return Long.parseLong(hexIn, HEX_RADIX);
	}

	/**
	 * Converts a decimal {@code String} to a
	 * hexadecimal {@code String}.
	 *
	 * @param decIn The number to convert to hexadecimal
	 * @return <b>String</b> The resulting hexadecimal string
	 */
	public static String strDecToHex(final String decIn){
		//Set "magic numbers"
		final int DEC_RADIX = 10;
		final int HEX_RADIX = 16;

		//Remove all non-numeric chars from the string
		String decimal = decIn.replaceAll("[^0-9]", "");

		//Get the BigInteger form of the string and return the result as a hexadecimal number
		return new BigInteger(decimal, DEC_RADIX).toString(HEX_RADIX);
	}

	/**
	 * Converts a hexadecimal {@code String} to a
	 * decimal {@code String}.
	 *
	 * @param hexIn The number to convert to decimal
	 * @return <b>String</b> The resulting decimal string
	 */
	public static String strHexToDec(final String hexIn){
		//Set "magic numbers"
		final int DEC_RADIX = 10;
		final int HEX_RADIX = 16;

		//Remove all non-numeric/non-hex chars from the string
		String hexadecimal = hexIn.replaceAll("(?i)[^0-9A-F]+", "");

		//Get the BigInteger form of the string and return the result as a decimal number
		return new BigInteger(hexadecimal, HEX_RADIX).toString(DEC_RADIX);
	}
}
