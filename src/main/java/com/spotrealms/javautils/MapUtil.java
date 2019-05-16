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
import com.spotrealms.javautils.math.MathRandom;

//Import Java classes and dependencies
import java.util.Map;

//TODO: Finish up JavaDoc

public class MapUtil {
	/**
	 * Get a random element from a generic 
	 * arbitrary-dimensional {@code Map} using 
	 * a random number generator
	 * @param tMap The {@code Map} to look through
	 * @param recMode Set whether or not to also pick elements from any nested {@code Maps} 
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>T</b> The resulting random element
	 */
	public static <T> T getRandomMapElem(Map<T,T> tMap, boolean recMode){
		//Get the size of the map
		int mapSize = tMap.size();
		
		//Convert the map to a generic primitive array
		@SuppressWarnings("unchecked")
		T[] primMap = (T[]) tMap.keySet().toArray();
		
		//Pick a random integer in a range (from 0 to the map size)
		int randInt = MathRandom.getRandomInt(0, (mapSize - 1));
				
		//Get the element at the index of the random long
		T randomMapElem = primMap[randInt];
		
		//Return the resulting shuffled list
		return randomMapElem;
	}
	
	//TODO: Move to JUnit test instead of declaring it here in the class itself

	/*
	public static void main(String[] args){
		final java.util.Map<String, String> m = new java.util.HashMap<String, String>();
		m.put("01", "Jan");
		m.put("02", "Feb");
		m.put("03", "Mar");
		m.put("04", "Apr");
		m.put("05", "May");
		m.put("06", "Jun");
		m.put("07", "Jul");
		m.put("08", "Aug");
		m.put("09", "Sep");
		m.put("10", "Oct");
		m.put("11", "Nov");
		m.put("12", "Dec");
		
		Map<Object, Map<Object, Object>> gm = new java.util.HashMap<>();

		
    	Object randomName = getRandomMapElem(m, false);
    	System.out.println(randomName.toString()+"-"+m.get(randomName));  
		
		///System.out.println(java.util.Arrays.deepToString((Object[]) fin));
	}
	*/
}
