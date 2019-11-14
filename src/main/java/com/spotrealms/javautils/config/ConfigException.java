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

package com.spotrealms.javautils.config_indev;

/**
 * A runtime exception thrown when a non-IO
 * issue occurs in a configuration access or
 * modification operation
 * @author Spotrealms
 */
public class ConfigException extends Exception {
	/**
	 * Defines the ID of this class when
	 * serialize and deserialize operations
	 * occur
	 */
	private static final long serialVersionUID = -2333712764705995270L;

	/**
	 * Constructs a ConfigException object to
	 * be thrown when an error occurs in a
	 * configuration class
	 * @param errorMessage The message to be displayed when this exception is thrown
	 */
	public ConfigException(String errorMessage){
		//Call the superclass with the user provided message
		super(errorMessage);
	}
}
