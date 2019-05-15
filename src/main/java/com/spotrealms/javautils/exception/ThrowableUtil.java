/**
 * JavaUtils: A collection of utility methods and classes for your Java programs
 *   Copyright (C) 2015-2019  Spotrealms Network
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
 * @author Spotrealms
 *
 */
public class ThrowableUtil {
	public static Throwable getRootException(Throwable genericExc){
		//Loop until the root cause of the throwable is null
		while(genericExc.getCause() != null){
			//Get the cause of the throwable
			genericExc = genericExc.getCause();
		}
		
		//Return the throwable cause
		return genericExc;
	}
}