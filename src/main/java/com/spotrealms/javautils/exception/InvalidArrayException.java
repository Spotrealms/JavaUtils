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

package com.spotrealms.javautils.exception;

import com.spotrealms.javautils.misc.ArrayUtil;

/**
 * An exception thrown when a primitive array,
 * {@code List}, {@code ArrayList}, etc is
 * not valid for the given type expected.
 *
 * @author Spotrealms
 */
public class InvalidArrayException extends RuntimeException {
	private static final long serialVersionUID = -4309694208041447398L;

	/**
	 * An exception thrown when a primitive array,
	 * {@code List}, {@code ArrayList}, etc is
	 * not valid for the given type expected.
	 *
	 * @param errorMessage The message to display to the user when the exception is thrown
	 * @param expectedType A primitive array of {@code String} variables that show the valid array types expected by the method or class
	 */
	public InvalidArrayException(final String errorMessage, final String[] expectedType){
		//Invoke the runtime exception
		super(generateResponse(errorMessage, expectedType));
	}
	
	/**
	 * Generate the response message for the constructor.
	 *
	 * @param errorMessage The message to display to the user when the exception is thrown
	 * @param expectedType A primitive array of {@code String} variables that show the valid array types expected by the method or class
	 * @return <b>String</b> The final response to show in the thrown exception
	 */
	private static String generateResponse(final String errorMessage, final String[] expectedType){
		//Collect the valid array types into a String for processing
		String validList = ArrayUtil.toGrammaticalList(expectedType, 'O', ',', ' ');

		//Return the resulting message as a String
		return errorMessage + " The expected array must be of the following types: " + validList + ".";
	}
}
