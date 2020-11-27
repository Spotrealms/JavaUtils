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

package com.spotrealms.javautils.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A series of math-related operations and
 * calculators.
 *
 * @author Spotrealms
 */
public final class MathUtil {
	/**
	 * Prevents instantiation of the utility class MathUtil.
	 *
	 * @throws RuntimeException If instantiation occurs
	 */
	private MathUtil(){ throw new RuntimeException("No " + this.getClass().getSimpleName() + " instance for you :)"); }

	/**
	 * Performs a comparison between one generic number and another.
	 *
	 * @author rolve
	 * @param x The first number to compare
	 * @param y The second number to compare
	 * @return <b>int</b> The difference between the two numbers
	 * @see <a href="https://stackoverflow.com/a/12884075">https://stackoverflow.com/a/12884075</a>
	 */
	public static int compareGN(final Number x, final Number y){
		//Check if the number is "special" (eg: infinity, NaN, etc)
		if(isSpecial(x) || isSpecial(y)){
			//Compare using the Double wrapper
			return Double.compare(x.doubleValue(), y.doubleValue());
		}
		else {
			//Convert to BigDecimal and compare
			return toBigDecimal(x).compareTo(toBigDecimal(y));
		}
	}

	/**
	 * Determines if a generic number is "special",
	 * or has the value {@code +/- infinity} or {@code NaN}.
	 *
	 * @author rolve
	 * @param num The number to check
	 * @return <b>boolean</b> The status as to whether the input number is special
	 * @see <a href="https://stackoverflow.com/a/12884075">https://stackoverflow.com/a/12884075</a>
	 */
	public static boolean isSpecial(final Number num){
		//Check if a number is a "special double" (eg: infinity or NaM)
		boolean specialDouble = num instanceof Double && (Double.isNaN((Double) num) || Double.isInfinite((Double) num));

		//Check if a number is a "special float" (eg: infinity or NaM)
		boolean specialFloat = num instanceof Float && (Float.isNaN((Float) num) || Float.isInfinite((Float) num));

		//OR the booleans and return
		return specialDouble || specialFloat;
	}

	/**
	 * Checks if a given double or float is in
	 * the range of two given values.
	 *
	 * @param num The number to check the range of
	 * @param min The lower bound for the range check
	 * @param max The upper bound for the range check
	 * @return <b>boolean</b>The status as to whether or not the input number is in range
	 */
	public static boolean numberInRange(final double num, final double min, final double max){
		//Check if the input double is in range and return the result
		return num >= min && num <= max;
	}
	
	/**
	 * Checks if a given integer or smaller is
	 * in the range of two given values.
	 *
	 * @param num The number to check the range of
	 * @param min The lower bound for the range check
	 * @param max The upper bound for the range check
	 * @return <b>boolean</b>The status as to whether or not the input number is in range
	 */
	public static boolean numberInRange(final long num, final long min, final long max){
		//Check if the input long is in range and return the result
		return num >= min && num <= max;
	}

	/**
	 * Rounds a number to the xth decimal place using
	 * {@link BigDecimal#setScale}.
	 *
	 * @param number The number to round
	 * @param places The number of places to round to
	 * @param mode The mode that should be used when rounding
	 * @return <b>BigDecimal</b> The rounded result
	 */
	public static BigDecimal round(final BigDecimal number, final int places, final RoundingMode mode){
		//Create a BigDecimal to store the result
		BigDecimal rounded = new BigDecimal(String.valueOf(number));

		//Set the sale, rounding mode, and return
		return rounded.setScale(places, mode);
	}

	/**
	 * Rounds a number to the xth decimal place using
	 * {@link BigDecimal#setScale}. Assumes the rounding
	 * mode to use is {@link RoundingMode#HALF_UP}, as this
	 * is the most commonly used method for rounding (ie:
	 * 5 or more let it soar; 4 or less let it rest).
	 *
	 * @param number The number to round
	 * @param places The number of places to round to
	 * @return <b>BigDecimal</b> The rounded result
	 */
	public static BigDecimal round(final BigDecimal number, final int places){
		//Select mode ROUND_UP when running the overloaded method
		return round(number, places, RoundingMode.HALF_UP);
	}

	/**
	 * Rounds a number to the xth decimal place using
	 * {@link BigDecimal#setScale}.
	 *
	 * @param number The number to round
	 * @param places The number of places to round to
	 * @param mode The mode that should be used when rounding
	 * @return <b>double</b> The rounded result
	 */
	public static double round(final double number, final int places, final RoundingMode mode){
		//Convert the input to a BigDecimal and convert the resulting BigDecimal back to a double
		return round(BigDecimal.valueOf(number), places, mode).doubleValue();
	}

	/**
	 * Rounds a number to the xth decimal place using
	 * {@link BigDecimal#setScale}. Assumes the rounding
	 * mode to use is {@link RoundingMode#HALF_UP}, as this
	 * is the most commonly used method for rounding (ie:
	 * 5 or more let it soar; 4 or less let it rest).
	 *
	 * @param number The number to round
	 * @param places The number of places to round to
	 * @return <b>BigDecimal</b> The rounded result
	 */
	public static double round(final double number, final int places){
		//Select mode ROUND_UP when running the overloaded method
		return round(number, places, RoundingMode.HALF_UP);
	}

	/**
	 * Converts a generic number to {@code BigDecimal}.
	 *
	 * @author rolve
	 * @param num The number to convert to BigDecimal
	 * @return <b>BigDecimal</b> The resulting BigDecimal representation of the input number
	 * @throws RuntimeException If the number can't be parsed to BigDecimal
	 * @see <a href="https://stackoverflow.com/a/12884075">https://stackoverflow.com/a/12884075</a>
	 */
	public static BigDecimal toBigDecimal(final Number num){
		//Switch over the classname (avoids a bunch of ifs with instanceof)
		switch(num.getClass().getSimpleName()){
			//Number is an integer-type so derive a BigDecimal from the long value and return it
			case "Byte": case "Integer": case "Long": case "Short": return new BigDecimal(num.longValue());

			//Number is a float-type so derive a BigDecimal from the double value and return it
			case "Double": case "Float": return BigDecimal.valueOf(num.doubleValue());

			//By default, treat the number as a string
			default:{
				//Attempt to derive a BigDecimal from the string representation
				try {
					return new BigDecimal(num.toString());
				}
				catch(final NumberFormatException ex){
					throw new RuntimeException("The given number (\"" + num + "\" of class " + num.getClass().getName()
							+ ") does not have a parsable string representation", ex);
				}
			}
		}
	}
}
