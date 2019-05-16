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

//Import Java classes and dependencies
import java.util.ArrayList;

//TODO: Finsh up JavaDoc

public class EveryOther {
	/**
	 * Get an {@code ArrayList} holding every even 
	 * whole number in a given range
	 * @param sNum The lowest number in the range
	 * @param eNum The highest number in the range
	 * @return <b>ArrayList&lt;Integer&gt;</b> The {@code ArrayList} holding the evens
	 */
	public static ArrayList<Integer> everyEven(int sNum, int eNum){
		//Make the arraylist to hold the numbers
		ArrayList<Integer> evenArr = new ArrayList<>();	
		
		//Start looping through the provided range of numbers
		for(int i=sNum; i<=eNum; i++){
			//Check if the number is divisible by two
			if(i%2 == 0){
				evenArr.add(i);
			}
		}
	
		//Return the resulting arraylist
		return evenArr;
	}
	
	/**
	 * Get an {@code ArrayList} holding every whole
	 * number in a given range
	 * @param sNum The lowest number in the range
	 * @param eNum The highest number in the range
	 * @return <b>ArrayList&lt;Integer&gt;</b> The {@code ArrayList} holding the numbers
	 */
	public static ArrayList<Integer> everyNumInRange(int sNum, int eNum){
		//Make the arraylist to hold the numbers
		ArrayList<Integer> rangeArr = new ArrayList<>();	
				
		//Start looping through the provided range of numbers
		for(int i=sNum; i<=eNum; i++){
			rangeArr.add(i);
		}
		
		//Return the resulting arraylist
		return rangeArr;
	}
	
	/**
	 * Get an {@code ArrayList} holding every odd
	 * whole number in a given range
	 * @param sNum The lowest number in the range
	 * @param eNum The highest number in the range
	 * @return <b>ArrayList&lt;Integer&gt;</b> The {@code ArrayList} holding the odds
	 */
	public static ArrayList<Integer> everyOdd(int sNum, int eNum){
		//Make the arraylist to hold the numbers
		ArrayList<Integer> oddArr = new ArrayList<>();	
		
		//Start looping through the provided range of numbers
		for(int i=sNum; i<=eNum; i++){
			//Check if the number isn't divisible by two
			if(i%2 != 0){
				oddArr.add(i);
			}
		}
	
		//Return the resulting arraylist
		return oddArr;
	}
	
	
	/**
	 * Get an {@code ArrayList} holding every other
	 * even whole number in a given range
	 * @param sNum The lowest number in the range
	 * @param eNum The highest number in the range
	 * @return <b>ArrayList&lt;Integer&gt;</b> - The {@code ArrayList} holding the evens
	 */
	public static ArrayList<Integer> everyOtherEven(int sNum, int eNum){
		//Make the arraylist to hold the numbers
		ArrayList<Integer> eoeArr = new ArrayList<>();	
		
		//Start looping through the provided range of numbers
		for(int i=(sNum-2); i<(eNum-1); i++){
			//Check if the number is divisible by two
			if(i%2 == 0){
				//Check if the number two places ahead is divisible by 0
				if(((i+=2)%2) == 0){
					//Add the number to the arraylist
					eoeArr.add(i);
				}
			}
		}
	
		//Return the resulting arraylist
		return eoeArr;
	}
	
	/**
	 * Get an {@code ArrayList} holding every other
	 * odd whole number in a given range
	 * @param sNum The lowest number in the range
	 * @param eNum The highest number in the range
	 * @return <b>ArrayList&lt;Integer&gt;</b> The {@code ArrayList} holding the odds
	 */
	public static ArrayList<Integer> everyOtherOdd(int sNum, int eNum){
		//Make the arraylist to hold the numbers
		ArrayList<Integer> eooArr = new ArrayList<>();	
		
		//Start looping through the provided range of numbers
		for(int i=(sNum-2); i<(eNum-1); i++){
			//Check if the number isn't divisible by two
			if(i%2 != 0){
				//Check if the number two places ahead isn't divisible by 0
				if(((i+=2)%2) != 0){
					//Add the number to the arraylist
					eooArr.add(i);
				}
			}
		}
	
		//Return the resulting arraylist
		return eooArr;
	}
}