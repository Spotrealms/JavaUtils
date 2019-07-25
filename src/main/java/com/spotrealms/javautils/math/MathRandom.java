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

package com.spotrealms.javautils.math;

//TODO: Finish up JavaDoc
@Deprecated
public class MathRandom {
	/**
	 * Get a random boolean value using
	 * {@code Math.random()}
	 *
	 * @return <b>boolean</b> - A random boolean
	 */
	public static boolean getRandomBool(){
		return Math.random() >= 0.5;
	}
	
	/**
	 * Get a random double between a
	 * minimum and max number using
	 * {@code Math.random()}
	 *
	 * @param min min number
	 * @param max max number
	 * @return <b>double</b> A random double
	 */
	public static double getRandomDouble(double min, double max){
		return Math.random() * (max - min) + min;
	}
	
	/**
	 * Get a random float between a
	 * minimum and max number using
	 * {@code Math.random()}
	 *
	 * @param min min number
	 * @param max max number
	 * @return <b>float</b> A random double
	 */
	public static float getRandomFloat(float min, float max){
		return (float) Math.random() * (max - min) + min;
	}

	/**
	 * Get a random integer between a
	 * minimum and max number using
	 * {@code Math.random()}
	 *
	 * @param min min number
	 * @param max max number
	 * @return <b>int</b> A random integer
	 */
	public static int getRandomInt(int min, int max){
		return (int) Math.floor(Math.random() * (max - min + 1) + min);
	}
	
	/**
	 * Get a random long between a
	 * minimum and max number using
	 * {@code Math.random()}
	 *
	 * @param min min number
	 * @param max max number
	 * @return <b>long</b> A random long
	 */
	public static long getRandomLong(long min, long max){
		return (long) Math.floor(Math.random() * (max - min + 1) + min);
	}
}