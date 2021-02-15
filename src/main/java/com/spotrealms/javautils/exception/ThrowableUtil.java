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
 * Miscellaneous utilities for working with {@code Throwable}
 * and {@code Exception}.
 *
 * @author Spotrealms
 */
public final class ThrowableUtil {
	/**
	 * Prevents instantiation of the utility class ThrowableUtil.
	 *
	 * @throws RuntimeException If instantiation occurs
	 */
	private ThrowableUtil(){ throw new RuntimeException("No " + this.getClass().getSimpleName() + " instance for you :)"); }

	/**
	 * Gets the root cause of an exception.
	 *
	 * @param genericExc The exception that was thrown
	 * @return <b>Throwable</b> The root cause of the thrown exception
	 */
	public static Throwable getRootException(final Throwable genericExc){
		//Initialization
		Throwable cause = genericExc;

		//Loop until the root cause of the throwable is null
		while(cause.getCause() != null){
			//Get the cause of the throwable
			cause = cause.getCause();
		}
		
		//Return the throwable cause
		return cause;
	}
}
