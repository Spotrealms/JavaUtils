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

import java.util.ArrayList;

/**
 * A series of methods for working with even and
 * odd integers as well as ranges of numbers. This
 * includes methods for calculating every n odd/even.
 *
 * @author Spotrealms
 */
public final class EvenOdd {
	/**
	 * Prevents instantiation of the utility class EvenOdd.
	 *
	 * @throws RuntimeException If instantiation occurs
	 */
	private EvenOdd(){ throw new RuntimeException("No " + this.getClass().getSimpleName() + " instance for you :)"); }

	/**
	 * Get an {@code ArrayList} holding every whole
	 * number in a given range.
	 *
	 * @param sNum The lowest number in the range
	 * @param eNum The highest number in the range
	 * @return <b>ArrayList&lt;Integer&gt;</b> The {@code ArrayList} holding the numbers
	 */
	public static ArrayList<Integer> everyNum(final int sNum, final int eNum){
		//Make the arraylist to hold the numbers
		ArrayList<Integer> rangeArr = new ArrayList<>();

		//Start looping through the provided range of numbers
		for(int i = sNum; i <= eNum; i++){
			rangeArr.add(i);
		}

		//Return the resulting arraylist
		return rangeArr;
	}

	/**
	 * Get an {@code ArrayList} holding every even
	 * whole number in a given range.
	 *
	 * @param sNum The lowest number in the range
	 * @param eNum The highest number in the range
	 * @return <b>ArrayList&lt;Integer&gt;</b> The {@code ArrayList} holding the evens
	 */
	public static ArrayList<Integer> everyEven(final int sNum, final int eNum){
		//Make the arraylist to hold the numbers
		ArrayList<Integer> evenArr = new ArrayList<>();

		//Start looping through the provided range of numbers
		for(int i = sNum; i <= eNum; i++){
			//Check if the number is divisible by two
			if(i % 2 == 0){
				evenArr.add(i);
			}
		}

		//Return the resulting arraylist
		return evenArr;
	}

	/**
	 * Get an {@code ArrayList} holding every odd
	 * whole number in a given range.
	 *
	 * @param sNum The lowest number in the range
	 * @param eNum The highest number in the range
	 * @return <b>ArrayList&lt;Integer&gt;</b> The {@code ArrayList} holding the odds
	 */
	public static ArrayList<Integer> everyOdd(final int sNum, final int eNum){
		//Make the arraylist to hold the numbers
		ArrayList<Integer> oddArr = new ArrayList<>();

		//Start looping through the provided range of numbers
		for(int i = sNum; i <= eNum; i++){
			//Check if the number isn't divisible by two
			if(i % 2 != 0){
				oddArr.add(i);
			}
		}

		//Return the resulting arraylist
		return oddArr;
	}

	/**
	 * Get an {@code ArrayList} holding every other
	 * even whole number in a given range.
	 *
	 * @param sNum The lowest number in the range
	 * @param eNum The highest number in the range
	 * @return <b>ArrayList&lt;Integer&gt;</b> - The {@code ArrayList} holding the evens
	 */
	public static ArrayList<Integer> everyOtherEven(final int sNum, final int eNum){
		//Make the arraylist to hold the numbers
		ArrayList<Integer> eoeArr = new ArrayList<>();

		//Start looping through the provided range of numbers
		for(int i = sNum - 2; i < eNum - 1; i++){
			//Check if the number is divisible by two
			if(i % 2 == 0){
				//Increment by 2 and add the number to the arraylist
				i += 2;
				eoeArr.add(i);
			}
		}

		//Return the resulting arraylist
		return eoeArr;
	}

	/**
	 * Get an {@code ArrayList} holding every other
	 * odd whole number in a given range.
	 *
	 * @param sNum The lowest number in the range
	 * @param eNum The highest number in the range
	 * @return <b>ArrayList&lt;Integer&gt;</b> The {@code ArrayList} holding the odds
	 */
	public static ArrayList<Integer> everyOtherOdd(final int sNum, final int eNum){
		//Make the arraylist to hold the numbers
		ArrayList<Integer> eooArr = new ArrayList<>();

		//Start looping through the provided range of numbers
		for(int i = sNum - 2; i < eNum - 1; i++){
			//Check if the number isn't divisible by two
			if(i % 2 != 0){
				//Increment by 2 and add the number to the arraylist
				i += 2;
				eooArr.add(i);
			}
		}

		//Return the resulting arraylist
		return eooArr;
	}

	/**
	 * Generates an {@code ArrayList} holding every
	 * nth even whole number in a given range.
	 *
	 * @param start The lowest number in the range
	 * @param end The highest number in the range
	 * @param nthEven The amount of evens to skip (eg: every even is 1, every other is 2, etc)
	 * @return <b>ArrayList&lt;Integer&gt;</b> The {@code ArrayList} holding the generated even
	 * @throws IllegalArgumentException If nthEven is less than 1
	 */
	public static ArrayList<Integer> everyNthEven(final int start, final int end, final int nthEven){
		//Ensure that the nthOdd integer is greater than or equal to 1
		if(nthEven < 1) throw new IllegalArgumentException("nth term variable can't be less than 1");

		//Make the arraylist to hold the numbers
		ArrayList<Integer> eneArr = new ArrayList<>();

		//Create a number to keep track of when to add a number to the array
		int nextAddIter = 0;

		//Start looping through the provided range of numbers
		for(int i = start; i <= end; i++){
			//Check if the number is divisible by two (even numbers are divisible by 2)
			if(i % 2 == 0){
				//Check if the add variable is less than or equal to 0 (indicates there are 0 iterations before another add should occur)
				if(nextAddIter <= 0){
					//Add the current element to the array
					eneArr.add(i);

					//Set the add variable to one less of the nth variable
					nextAddIter += nthEven - 1;
				}
				else {
					//Decrement the add variable
					nextAddIter--;
				}
			}
		}

		//Return the filled arraylist
		return eneArr;
	}

	/**
	 * Generates an {@code ArrayList} holding every
	 * nth odd whole number in a given range.
	 *
	 * @param start The lowest number in the range
	 * @param end The highest number in the range
	 * @param nthOdd The amount of odds to skip (eg: every odd is 1, every other is 2, etc)
	 * @return <b>ArrayList&lt;Integer&gt;</b> The {@code ArrayList} holding the generated odds
	 * @throws IllegalArgumentException If nthOdd is less than 1
	 */
	public static ArrayList<Integer> everyNthOdd(final int start, final int end, final int nthOdd){
		//Ensure that the nthOdd integer is greater than or equal to 1
		if(nthOdd < 1) throw new IllegalArgumentException("nth term variable can't be less than 1");

		//Make the arraylist to hold the numbers
		ArrayList<Integer> enoArr = new ArrayList<>();

		//Create a number to keep track of when to add a number to the array
		int nextAddIter = 0;

		//Start looping through the provided range of numbers
		for(int i = start; i <= end; i++){
			//Check if the number isn't divisible by two (odd numbers aren't divisible by 2)
			if(i % 2 != 0){
				//Check if the add variable is less than or equal to 0 (indicates there are 0 iterations before another add should occur)
				if(nextAddIter <= 0){
					//Add the current element to the array
					enoArr.add(i);

					//Set the add variable to one less of the nth variable
					nextAddIter += nthOdd - 1;
				}
				else {
					//Decrement the add variable
					nextAddIter--;
				}
			}
		}

		//Return the filled arraylist
		return enoArr;
	}
}
