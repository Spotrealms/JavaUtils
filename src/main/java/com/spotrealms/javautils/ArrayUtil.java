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

//Import first-party classes
import com.spotrealms.javautils.math.MathUtil;

//Import Java classes and dependencies
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO: Finish up JavaDoc

public class ArrayUtil {
	/**
	 * Generates a list of all the indexes in a given 
	 * {@code ArrayList} where an element matches a given 
	 * regex {@code String}
	 * @param tArray The {@code ArrayList} to search through
	 * @param huntTarget The {@code String} containing the regex to use on the input {@code ArrayList}
	 * @param ignoreCase Set whether or not to ignore the case of hunt
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>int[]</b> A list of all the indexes of the matched elements
	 */
	public static <T> int[] allIndexesOf(ArrayList<T> tArray, String huntTarget, boolean ignoreCase){
		//Create an ArrayList to hold the indexes
		ArrayList<Integer> allIndexes = new ArrayList<>();
		
		//Loop over the input ArrayList
		for(int i=0; i<tArray.size(); i++){
			//Check if the current element equals the input string
			if(StringUtil.equalsAny(tArray.get(i).toString(), huntTarget, ignoreCase, true)){
				//Add the current index to the index ArrayList
				allIndexes.add(i);
			}
		}
		
		//Check if the index list is empty
		if(allIndexes.isEmpty()){
			//Add -1 into the index ArrayList
			allIndexes.add(-1);
		}
		
		//Convert the index ArrayList to an array of ints and return it (Java >= 8 is required)
		return allIndexes.stream().mapToInt(Integer::intValue).toArray();
	}
	
	/**
	 * Generates a list of all the indexes in a given 
	 * {@code Array} where an element matches a given 
	 * regex {@code String}
	 * @param tArray The {@code Array} to search through
	 * @param huntTarget The {@code String} containing the regex to use on the input {@code Array}
	 * @param ignoreCase Set whether or not to ignore the case of hunt
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>int[]</b> A list of all the indexes of the matched elements
	 */
	public static <T> int[] allIndexesOf(T[] tArray, String huntTarget, boolean ignoreCase){
		//Convert the input array to an ArrayList
		ArrayList<T> convertedList = new ArrayList<>(Arrays.asList(tArray));
		
		//Redirect to the overloaded method
		return allIndexesOf(convertedList, huntTarget, ignoreCase);
	}
	
	/**
	 * Create a grammatical list in a {@code String} from a 
	 * primitive array of arbitrary types
	 * @param tArr The primitive array of arbitrary values to create the list from
	 * @param listType A character representing the list type (A - and, N - nor, O - or)
	 * @param listSep The separator to use in the final list (commas are usually preferred)
	 * @param spChar The whitespace character to use in the list (a space is usually preferred)
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>String</b> The resulting list in a {@code String}
	 */
	public static <T> String createListFromArr(T[] tArr, char listType, char listSep, char spChar){
		//Create a StringBuilder to hold the list and a String to hold the list type
		StringBuilder finalList = new StringBuilder("");
		String listTypeStr = "";
		
		//Determine the type of list to create
		switch(Character.toUpperCase(listType)){
			//"AND" list
			case 'A':
				listTypeStr = "and";
			break;
			//"NOR" list
			case 'N':	
				listTypeStr = "nor";
			break;
			//"OR" list
			case 'O':	
				listTypeStr = "or";
			break;
		}
		
		//Check if the array length is more than two
		if(tArr.length > 2){
			//Loop through the entire array
			for(int i=0; i < tArr.length; i++){
				//Check if the iterator is not at the end of the array
				if(i < (tArr.length - 1)){
					//Append the element at position i to the string
					finalList.append(tArr[i]); 
				
					//Check if the next element isn't the last
					if(!(i == (tArr.length - 2))){
						//Add the separator followed by a space
						finalList.append(String.valueOf(listSep) + String.valueOf(spChar));
					}
				}
				//Check if the iterator is at the end of the array
				else if(i == (tArr.length - 1)){
					//Append the final element of the list and the list type indication
					finalList.append(String.valueOf(listSep) + String.valueOf(spChar) + listTypeStr + String.valueOf(spChar) + tArr[i]);
				}
			}
		}
		//Check if the array length is two
		else if(tArr.length == 2){
			//Add the first and then the second element with the list type in between
			finalList.append(tArr[0] + String.valueOf(spChar) + listTypeStr + String.valueOf(spChar) + tArr[1]);
		}
		//Default to just adding the first array element
		else {
			finalList.append(tArr[0]);
		}
		
		//Return the resulting StringBuilder as a String
		return finalList.toString();
	}
	
	/**
	 * Get a random element from a generic 
	 * arbitrary-dimensional {@code List} using 
	 * a random number generator
	 * @param tArray The {@code List} to look through
	 * @param recMode Set whether or not to also pick elements from any nested {@code Lists}
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>T</b> The resulting random element
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getRandomArrayElem(List<T> tArray, boolean recMode){
		//Get the size of the List
		int listSize = tArray.size();
		
		//Pick a random integer in a range (from 0 to the list size)
		int randInt = MathUtil.getRandomInt(0, (listSize - 1));
		
		//Get the element at the index of the random long
		T randomElem = tArray.get(randInt);
		
		//Check if recursion is allowed
		if(recMode){
			//Check if this element is another list
			if(randomElem instanceof ArrayList || randomElem instanceof List){
				//Recursively run the method on the nested List
				return (T) getRandomArrayElem((List<?>) randomElem, true);
			}
		}
		
		//Return the random element
		return randomElem;
	}
	
	/**
	 * Get a random element from a generic 
	 * arbitrary-dimensional primitive {@code Array} 
	 * using a random number generator
	 * @param tArray The {@code Array} to look through
	 * @param recMode Set whether or not to also pick elements from any nested {@code Arrays} 
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>T</b> The resulting random element
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getRandomPrimArrayElem(T[] tArray, boolean recMode){
		//Get the size of the Array
		int arraySize = tArray.length;
		
		//Pick a random integer in a range (from 0 to the array size)
		int randInt = MathUtil.getRandomInt(0, (arraySize - 1));
		
		//Get the element at the index of the random long
		T randomElem = tArray[randInt];
		
		//Check if recursion is allowed
		if(recMode){
			//Check if this element is another array
			if(randomElem instanceof Object[]){
				//Recursively run the method on the nested Array
				return getRandomPrimArrayElem((T[]) randomElem, true);
			}
		}
		
		//Return the random element
		return randomElem;
	}
	
	/**
	 * Check if an input generic object is a
	 * primitive array
	 * @param objToCheck The object to check
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>boolean</b> The status of whether or not the object is an array
	 */
	public static <T> boolean isPrimArr(T objToCheck){
		//Return true if the input object is an instance of an array and isn't null
		return (objToCheck != null && objToCheck.getClass().isArray());
	}
	
	public static <T> String listToStr(List<T> targetList, String arrElemDelimiter){
		//Create a StringBuilder for later
		StringBuilder outputStr = new StringBuilder();
		
		//Loop through the list
		for(int i=0; i<targetList.size(); i++){
			//Append the current element to the StringBuilder (must be converted to a string first)
			outputStr.append(targetList.get(i).toString());
			
			//Check if the current element in the List is not the last one
			if(i < (targetList.size() - 1)){
				//Append the delimiter on the end of the StringBuilder
				outputStr.append(arrElemDelimiter);
			}
		}
		
		//Output the StringBuilder as a string
		return outputStr.toString();
	}
	
	public static <T> String listToStr(List<T> targetList){
		//Redirect to the overloaded method
		return listToStr(targetList, "");
	}
	
	/**
	 * Deletes certain elements from a given {@code ArrayList}
	 * given an array of the indexes of those items
	 * @param tArr The {@code ArrayList} to remove the elements from
	 * @param remIndexes An {@code Array} containing the indexes of the elements to remove
	 * @param <T> Allow generic types and objects to be used
	 */
	public static <T> void removeAtIndexes(ArrayList<T> tArr, int[] remIndexes){
		//Loop over the index array
		for(int i=0; i<remIndexes.length; i++){
			//Remove the current element from the input array
			tArr.remove(remIndexes[i]);
			
			//Loop over the index array again
			for(int ii=0; ii<remIndexes.length; ii++){
				//Decrement each index by 1
				remIndexes[i] = (remIndexes[ii]--);
			}
		}
	}
	
	/**
	 * Shuffle a generic arbitrary-dimensional {@code List}
	 * using the Fisher-Yates shuffling algorithm
	 * @see <a href="https://bost.ocks.org/mike/shuffle/">https://bost.ocks.org/mike/shuffle/</a>
	 * @param tArray The {@code List} to shuffle
	 * @param recMode Set whether or not to also shuffle any nested {@code Lists}
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>List&lt;T&gt;</b> The resulting shuffled {@code List}
	 */
	public static <T> List<T> yatesArrShuffle(List<T> tArray, boolean recMode){
		//Create the array object
		List<T> shuffArr = (List<T>) tArray;
			
		//Create the integers that will hold the list length and current index 
		int remElem = shuffArr.size(), remIndex;
			
		//Create the temporary Object to hold the current element in the list
		T remSwapper;
			
		//Loop until all elements have been shuffled
		while(remElem > 0){
			//Get a random index in the list and subtract once from the remainder variable
			remIndex = (int) Math.floor(Math.random() * remElem--);
				
			//Assign the temporary Object to the element at position remElem
			remSwapper = shuffArr.get(remElem);
				
			//Check if recursion is allowed
			if(recMode){
				//Check if this element is another list
				if(remSwapper instanceof ArrayList || remSwapper instanceof List){
					//Recursively run the method on the nested List
					yatesArrShuffle((List<?>) remSwapper, true);
				}
			}
							
			//Swap the elements at position remElem and remIndex
			shuffArr.set(remElem, shuffArr.get(remIndex));
				
			//Assign the first element in the temporary Object to position remIndex
			shuffArr.set(remIndex, remSwapper);
		}
				
		//Return the resulting shuffled list
		return shuffArr;
	}
	
	/**
	 * Shuffle a generic arbitrary-dimensional primitive {@code Array}
	 * using the Fisher-Yates shuffling algorithm
	 * @see <a href="https://bost.ocks.org/mike/shuffle/">https://bost.ocks.org/mike/shuffle/</a>
	 * @param tArray The {@code Array} to shuffle
	 * @param recMode Set whether or not to also shuffle any nested {@code Arrays}
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>T[]</b> The resulting shuffled {@code Array}
	 */
	public static <T> T[] yatesPrimArrShuffle(T[] tArray, boolean recMode){
		//Create the array object
		T[] shuffArr = tArray;
		
		//Create the integers that will hold the array length and current index 
		int remElem = shuffArr.length, remIndex;
		
		//Create the temporary Object to hold the current element in the array
		T remSwapper;
		
		//Loop until all elements have been shuffled
		while(remElem > 0){
			//Get a random index in the array and subtract once from the remainder variable
			remIndex = (int) Math.floor(Math.random() * remElem--);
			
			//Assign the temporary Object to the element at position remElem
			remSwapper = shuffArr[remElem];
			
			//Check if recursion is allowed
			if(recMode){
				//Check if this element is another array
				if(remSwapper instanceof Object[]){
					//Recursively run the method on the nested Array
					yatesPrimArrShuffle((Object[]) remSwapper, true);
				}
			}
						
			//Swap the elements at position remElem and remIndex
			shuffArr[remElem] = shuffArr[remIndex];
			
			//Assign the first element in the temporary Object to position remIndex
			shuffArr[remIndex] = remSwapper;
		}
			
		//Return the resulting shuffled array
		return shuffArr;
	}
}