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

package com.spotrealms.javautils.misc;

import com.spotrealms.javautils.math.RandomValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO: Finish up JavaDoc

public final class ArrayUtil {
	/**
	 * Prevents instantiation of the utility class ArrayUtil.
	 *
	 * @throws RuntimeException If instantiation occurs
	 */
	private ArrayUtil(){ throw new RuntimeException("No " + this.getClass().getSimpleName() + " instance for you :)"); }

	/**
	 * Generates a list of all the indexes in a given 
	 * {@code ArrayList} where an element matches a given 
	 * regex {@code String}.
	 *
	 * @param tArray The {@code ArrayList} to search through
	 * @param huntTarget The {@code String} containing the regex to use on the input {@code ArrayList}
	 * @param ignoreCase Set whether or not to ignore the case of hunt
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>int[]</b> A list of all the indexes of the matched elements
	 */
	public static <T> int[] allIndexesOf(final ArrayList<T> tArray, final String huntTarget, final boolean ignoreCase){
		//Create an ArrayList to hold the indexes
		ArrayList<Integer> allIndexes = new ArrayList<>();
		
		//Loop over the input ArrayList
		for(int i = 0; i < tArray.size(); i++){
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
	 * regex {@code String}.
	 *
	 * @param tArray The {@code Array} to search through
	 * @param huntTarget The {@code String} containing the regex to use on the input {@code Array}
	 * @param ignoreCase Set whether or not to ignore the case of hunt
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>int[]</b> A list of all the indexes of the matched elements
	 */
	public static <T> int[] allIndexesOf(final T[] tArray, final String huntTarget, final boolean ignoreCase){
		//Convert the input array to an ArrayList
		ArrayList<T> convertedList = new ArrayList<>(Arrays.asList(tArray));
		
		//Redirect to the overloaded method
		return allIndexesOf(convertedList, huntTarget, ignoreCase);
	}
	
	/**
	 * Generates a {@code String} containing all
	 * of the elements of a primitive {@code Array}
	 * in the same style as printing an {@code ArrayList}.
	 *
	 * @param arrayIn The {@code Array} to create the {@code String} from
	 * @param elemDelim The separator to use in the resulting {@code String}
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>String</b> The resulting list of {@code Array} elements
	 */
	public static <T> String arrayToStr(final T[] arrayIn, final String elemDelim){
		//Create a StringBuilder for later
		StringBuilder arrayToStr = new StringBuilder();
		
		//Append the opening bracket onto the StringBuilder
		arrayToStr.append("[");
		
		//Loop over the input array
		for(int i = 0; i < arrayIn.length; i++){
			//Append the current term onto the StringBuilder
			arrayToStr.append(arrayIn[i]);
			
			//Check if the iterator is at the end of the list
			if(i < (arrayIn.length - 1)){
				//Append the delimiter onto the StringBuilder
				arrayToStr.append(elemDelim);
			}
		}
		
		//Append the closing bracket onto the StringBuilder
		arrayToStr.append("]");
		
		//Return the StringBuilder as a string
		return arrayToStr.toString();
	}
	
	/**
	 * Counts the number of times an item
	 * appears in an {@code ArrayList}.
	 *
	 * @param tArrList The ArrayList to search the target element for
	 * @param arrItem The item to search for
	 * @param <T> Allow generic types to be used
	 * @return <b>int</b> The number of times the array item appears in the ArrayList
	 */
	public static <T> int dupeCount(final ArrayList<T> tArrList, final T arrItem){
		//Create an integer to keep track of the match count
		int matched = 0;
		
		//Loop over the input ArrayList
		for(T curObj : tArrList){
			//Check if the current element equals the target item
			if(curObj.equals(arrItem)){
				//Increment the matched counter
				matched++;
			}
		}
			
		//Return the number of matches there were
		return matched;
	}
	
	/**
	 * Counts the number of times an item
	 * appears in an array.
	 *
	 * @param tArr The array to search the target element for
	 * @param arrItem The item to search for
	 * @param <T> Allow generic types to be used
	 * @return <b>int</b> The number of times the array item appears in the array
	 */
	public static <T> int dupeCount(final T[] tArr, final T arrItem){
		//Convert the generic array to a generic ArrayList
		ArrayList<T> genArrList = new ArrayList<>(Arrays.asList(tArr));
				
		//Pass the ArrayList to the overloaded dupeCount method and return the result
		return dupeCount(genArrList, arrItem);
	}
	
	/**
	 * Get a random element from a generic 
	 * arbitrary-dimensional {@code List} using 
	 * a random number generator.
	 *
	 * @param tArray The {@code List} to look through
	 * @param recMode Set whether or not to also pick elements from any nested {@code Lists}
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>T</b> The resulting random element
	 * @deprecated To be completely removed in version 1.0
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public static <T> T getRandomArrayElem(final List<T> tArray, final boolean recMode){
		//Get the size of the List
		int listSize = tArray.size();
		
		//Pick a random integer in a range (from 0 to the list size)
		int randInt = RandomValue.randomInt(0, listSize - 1);
		
		//Get the element at the index of the random long
		T randomElem = tArray.get(randInt);
		
		//Check if recursion is allowed and this element is another list
		if(recMode && randomElem instanceof List){
			//Recursively run the method on the nested List
			return (T) getRandomArrayElem((List<?>) randomElem, true);
		}
		
		//Return the random element
		return randomElem;
	}
	
	/**
	 * Get a random element from a generic 
	 * arbitrary-dimensional primitive {@code Array} 
	 * using a random number generator.
	 *
	 * @param tArray The {@code Array} to look through
	 * @param recMode Set whether or not to also pick elements from any nested {@code Arrays} 
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>T</b> The resulting random element
	 * @deprecated To be completely removed in version 1.0
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public static <T> T getRandomPrimArrayElem(final T[] tArray, final boolean recMode){
		//Get the size of the Array
		int arraySize = tArray.length;
		
		//Pick a random integer in a range (from 0 to the array size)
		int randInt = RandomValue.randomInt(0, arraySize - 1);

		//Get the element at the index of the random long
		T randomElem = tArray[randInt];
		
		//Check if recursion is allowed and this element is another array
		if(recMode && randomElem instanceof Object[]){
			//Recursively run the method on the nested Array
			return getRandomPrimArrayElem((T[]) randomElem, true);
		}
		
		//Return the random element
		return randomElem;
	}
	
	/**
	 * Check if an input generic object is a
	 * primitive array.
	 *
	 * @param objToCheck The object to check
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>boolean</b> The status of whether or not the object is an array
	 * @deprecated To be completely removed in version 1.0
	 */
	@Deprecated
	public static <T> boolean isPrimArr(final T objToCheck){
		//Return true if the input object is an instance of an array and isn't null
		return objToCheck != null && objToCheck.getClass().isArray();
	}
	
	/**
	 * Deletes certain elements from a given {@code ArrayList}
	 * given an array of the indexes of those items.
	 *
	 * @param tArr The {@code ArrayList} to remove the elements from
	 * @param remIndexes An {@code Array} containing the indexes of the elements to remove
	 * @param <T> Allow generic types and objects to be used
	 */
	public static <T> void removeAtIndexes(final ArrayList<T> tArr, final int[] remIndexes){
		//Loop over the index array
		for(int x = 0; x < remIndexes.length; x++){
			//Remove the current element from the input array
			tArr.remove(remIndexes[x]);
			
			//Loop over the index array again
			for(int y = 0; y < remIndexes.length; y++){
				//Decrement each index by 1
				remIndexes[x] = remIndexes[y]--;
			}
		}
	}
	
	/**
	 * Deletes certain elements from a given {@code Array}
	 * given an array of the indexes of those items.
	 *
	 * @param tArr The {@code Array} to remove the elements from
	 * @param remIndexes An {@code Array} containing the indexes of the elements to remove
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>Object[]</b> The filtered {@code Array} (can be of type Object because ArrayLists can't hold primitive types)
	 */
	public static <T> Object[] removeAtIndexes(final T[] tArr, final int[] remIndexes){
		//Convert the input array to an ArrayList
		ArrayList<T> convArr = new ArrayList<>(Arrays.asList(tArr));
		
		//Remove the elements from the ArrayList
		removeAtIndexes(convArr, remIndexes);
		
		//Create an array that is the same size as the ArrayList
		Object[] newPrimArray = new Object[convArr.size()];
		
		//Loop over the new array
		for(int i = 0; i < newPrimArray.length; i++){
			//Add the current ArrayList element to the array
			newPrimArray[i] = convArr.get(i);
		}
		
		//Return the filled array
		return newPrimArray;
	}
	
	/**
	 * Creates a grammatical list in a {@code String} from a 
	 * primitive array of arbitrary types.
	 *
	 * @param tArr The primitive array of arbitrary values to create the list from
	 * @param listType A character representing the list type (A - and, N - nor, O - or)
	 * @param listSep The separator to use in the final list (commas are usually preferred)
	 * @param spChar The whitespace character to use in the list (a space is usually preferred)
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>String</b> The resulting list in a {@code String}
	 */
	public static <T> String toGrammaticalList(final T[] tArr, final char listType, final char listSep, final char spChar){
		//Create a StringBuilder to hold the list and a String to hold the list type
		StringBuilder finalList = new StringBuilder();
		String listTypeStr = "";
		
		//Determine the type of list to create
		switch(Character.toUpperCase(listType)){
			//"AND" list
			case 'A':{
				listTypeStr = "and";
				break;
			}
			//"NOR" list
			case 'N':{
				listTypeStr = "nor";
				break;
			}
			//"OR" list
			case 'O':{
				listTypeStr = "or";
				break;
			}
			default:{
				break;
			} //Don't do anything
		}
		
		//Check if the array length is more than two
		if(tArr.length > 2){
			//Loop through the entire array
			for(int i = 0; i < tArr.length; i++){
				//Check if the iterator is not at the end of the array
				if(i < tArr.length - 1){
					//Append the element at position i to the string
					finalList.append(tArr[i]); 
				
					//Check if the next element isn't the last
					if(!(i == tArr.length - 2)){
						//Add the separator followed by a space
						finalList.append(listSep).append(spChar);
					}
				}
				//Check if the iterator is at the end of the array
				else if(i == tArr.length - 1){
					//Append the final element of the list and the list type indication
					finalList.append(listSep).append(spChar).append(listTypeStr).append(spChar).append(tArr[i]);
				}
			}
		}
		//Check if the array length is two
		else if(tArr.length == 2){
			//Add the first and then the second element with the list type in between
			finalList.append(tArr[0]).append(spChar).append(listTypeStr).append(spChar).append(tArr[1]);
		}
		//Default to just adding the first array element
		else {
			finalList.append(tArr[0]);
		}
		
		//Return the resulting StringBuilder as a String
		return finalList.toString();
	}
	
	/**
	 * Shuffle a generic arbitrary-dimensional {@code List}
	 * using the Fisher-Yates shuffling algorithm.
	 *
	 * @deprecated To be completely removed in version 1.0
	 * @param tArray The {@code List} to shuffle
	 * @param recMode Set whether or not to also shuffle any nested {@code Lists}
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>List&lt;T&gt;</b> The resulting shuffled {@code List}
	 * @see <a href="https://bost.ocks.org/mike/shuffle/">https://bost.ocks.org/mike/shuffle/</a>
	 */
	@Deprecated
	public static <T> List<T> yatesArrShuffle(final List<T> tArray, final boolean recMode){
		//Create the integers that will hold the list length and current index 
		int remElem = tArray.size();
		int remIndex;
			
		//Create the temporary Object to hold the current element in the list
		T remSwapper;
			
		//Loop until all elements have been shuffled
		while(remElem > 0){
			//Get a random index in the list and subtract once from the remainder variable
			remIndex = (int) Math.floor(Math.random() * remElem--);
				
			//Assign the temporary Object to the element at position remElem
			remSwapper = tArray.get(remElem);
				
			//Check if recursion is allowed and this element is another list
			if(recMode && remSwapper instanceof List){
				//Recursively run the method on the nested List
				yatesArrShuffle((List<?>) remSwapper, true);
			}
							
			//Swap the elements at position remElem and remIndex
			tArray.set(remElem, tArray.get(remIndex));
				
			//Assign the first element in the temporary Object to position remIndex
			tArray.set(remIndex, remSwapper);
		}
		System.out.println("e");
				
		//Return the resulting shuffled list
		return tArray;
	}
	
	/**
	 * Shuffle a generic arbitrary-dimensional primitive {@code Array}
	 * using the Fisher-Yates shuffling algorithm.
	 *
	 * @see <a href="https://bost.ocks.org/mike/shuffle/">https://bost.ocks.org/mike/shuffle/</a>
	 * @param tArray The {@code Array} to shuffle
	 * @param recMode Set whether or not to also shuffle any nested {@code Arrays}
	 * @param <T> Allow generic types and objects to be used
	 * @deprecated To be completely removed in version 1.0
	 * @return <b>T[]</b> The resulting shuffled {@code Array}
	 */
	@Deprecated
	public static <T> T[] yatesPrimArrShuffle(final T[] tArray, final boolean recMode){
		//Create the integers that will hold the array length and current index 
		int remElem = tArray.length;
		int remIndex;

		//Create the temporary Object to hold the current element in the array
		T remSwapper;
		
		//Loop until all elements have been shuffled
		while(remElem > 0){
			//Get a random index in the array and subtract once from the remainder variable
			remIndex = (int) Math.floor(Math.random() * remElem--);
			
			//Assign the temporary Object to the element at position remElem
			remSwapper = tArray[remElem];
			
			//Check if recursion is allowed and this element is another array
			if(recMode && remSwapper instanceof Object[]){
				//Recursively run the method on the nested Array
				yatesPrimArrShuffle((Object[]) remSwapper, true);
			}
						
			//Swap the elements at position remElem and remIndex
			tArray[remElem] = tArray[remIndex];
			
			//Assign the first element in the temporary Object to position remIndex
			tArray[remIndex] = remSwapper;
		}
			
		//Return the resulting shuffled array
		return tArray;
	}
}
