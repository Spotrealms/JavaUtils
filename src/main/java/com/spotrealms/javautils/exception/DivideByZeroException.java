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

/**
 *
 * @author Spotrealms Network
 * @website https://spotrealms.com
 * @website https://github.com/spotrealms
 */

package com.spotrealms.javautils.exception;

/**
 * An exception thrown when division by zero
 * occurs or is likely to occur when computing
 * any sort of division.
 * @author Spotrealms Network
 */
public class DivideByZeroException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * An exception thrown when division by zero
	 * occurs or is likely to occur when computing
	 * any sort of division.
	 * @param errorMessage - The message to display to the user when the exception is thrown
	 */
	public DivideByZeroException(String errorMessage){
		//Invoke the runtime exception
		super(errorMessage);
	}
}
