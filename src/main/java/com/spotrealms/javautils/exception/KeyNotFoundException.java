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

/**
 * An exception thrown when an item in a
 * {@code Map} was not found.
 *
 * @author Spotrealms
 */
public class KeyNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -9141966215259779964L;

	/**
	 * An exception thrown when an item in a
	 * {@code Map} was not found.
	 *
	 * @param errorMessage The message to display to the user when the error is thrown
	 */
	public KeyNotFoundException(final String errorMessage){
		//Invoke the runtime exception
		super(errorMessage);
	}
}
