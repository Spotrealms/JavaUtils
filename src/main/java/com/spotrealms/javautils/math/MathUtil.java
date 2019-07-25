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

//TODO: Finish up JavaDoc
//TODO: Add proper random API with seed selection

public class MathUtil {
	/**
	 * Get a random boolean value using
	 * {@code Math.random()}
	 * @return <b>boolean</b> A random boolean value
	 */
	public static boolean getRandomBool(){
		return Math.random() >= 0.5;
	}
	
	/**
	 * Get a random double between a
	 * minimum and max number using
	 * {@code Math.random()}
	 * @param min The lowest number that can be selected
	 * @param max The highest number that can be selected
	 * @return <b>double</b> A random double value
	 */
	public static double getRandomDouble(double min, double max){
		return Math.random() * (max - min) + min;
	}
	
	/**
	 * Get a random float between a
	 * minimum and max number using
	 * {@code Math.random()}
	 * @param min The lowest number that can be selected
	 * @param max The highest number that can be selected
	 * @return <b>float</b> A random float value
	 */
	public static float getRandomFloat(float min, float max){
		return (float) Math.random() * (max - min) + min;
	}

	/**
	 * Get a random integer between a
	 * minimum and max number using
	 * {@code Math.random()}
	 * @param min The lowest number that can be selected
	 * @param max The highest number that can be selected
	 * @return <b>int</b> A random integer
	 */
	public static int getRandomInt(int min, int max){
		return (int) Math.floor(Math.random() * (max - min + 1) + min);
	}
	
	/**
	 * Get a random long between a
	 * minimum and max number using
	 * {@code Math.random()}
	 * @param min The lowest number that can be selected
	 * @param max The highest number that can be selected
	 * @return <b>long</b> A random long
	 */
	public static long getRandomLong(long min, long max){
		return (long) Math.floor(Math.random() * (max - min + 1) + min);
	}
	
	/**
	 * Check if a given double or float is in 
	 * the range of two given values
	 * @param numIn The number to check the range of
	 * @param minVal The lower bound for the range check
	 * @param maxVal The upper bound for the range check
	 * @return <b>boolean</b>The status as to whether or not the input number is in range
	 */
	public static boolean numberInRange(double numIn, double minVal, double maxVal){
		//Check if the input double is in range and return the result
		return (numIn >= minVal && numIn <= maxVal);
	}
	
	/**
	 * Check if a given integer or smaller is 
	 * in the range of two given values
	 * @param numIn The number to check the range of
	 * @param minVal The lower bound for the range check
	 * @param maxVal The upper bound for the range check
	 * @return <b>boolean</b>The status as to whether or not the input number is in range
	 */
	public static boolean numberInRange(long numIn, long minVal, long maxVal){
		//Check if the input long is in range and return the result
		return (numIn >= minVal && numIn <= maxVal);
	}
}
