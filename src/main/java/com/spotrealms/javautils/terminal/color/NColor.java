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

package com.spotrealms.javautils.terminal.color;

/**
 * Colors the terminal text using ANSI escape sequences.
 * This class is an alternative to the {@code get4Color}
 * method from the {@code ANSIColor} class that uses named 
 * enums as opposed to codes.
 * <br>This class provides the following colors:
 * <ul>
 * 	<li>Black</li>
 * 	<li>Red</li>
 * 	<li>Green</li>
 * 	<li>Yellow</li>
 * 	<li>Blue</li>
 * 	<li>Magenta</li>
 * 	<li>Cyan</li>
 * 	<li>White</li>
 * </ul>
 * Additionally, the following text and color modifications 
 * can be added (their support depends on the terminal used):
 * <ul>
 * 	<li>Bold</li>
 * 	<li>Italic</li>
 * 	<li>Underline</li>
 * 	<li>Strikethrough</li>
 * 	<li>High Intensity</li>
 * 	<li>Bold High Intensity</li>
 * </ul>
 * The background can also be colored and use high intensity modification.
 * The color can also be reset to the default simply by using {@code RESET}.
 * 
 * @author Spotrealms &amp; Contributors
 * @see ANSIColor
 * @see <a href="http://ascii-table.com/ansi-escape-sequences.php">http://ascii-table.com/ansi-escape-sequences.php</a>
 * @see <a href="https://stackoverflow.com/a/51944613/7520602">https://stackoverflow.com/a/51944613/7520602</a>
 */
public enum NColor {
	//Reset
	RESET("\033[0m"),
	
	//Text modification
	BOLD("\033[1m"),
	ITALIC("\033[3m"),
	UNDERLINE("\033[4m"),
	STRIKE("\033[9m"),

	//Regular Colors
	BLACK("\033[0;30m"),
	RED("\033[0;31m"),
	GREEN("\033[0;32m"),
	YELLOW("\033[0;33m"),
	BLUE("\033[0;34m"),
	MAGENTA("\033[0;35m"),
	CYAN("\033[0;36m"),
	WHITE("\033[0;37m"),

	//Background
	BLACK_BACKGROUND("\033[40m"),
	RED_BACKGROUND("\033[41m"),
	GREEN_BACKGROUND("\033[42m"),
	YELLOW_BACKGROUND("\033[43m"),
	BLUE_BACKGROUND("\033[44m"),
	MAGENTA_BACKGROUND("\033[45m"),
	CYAN_BACKGROUND("\033[46m"),
	WHITE_BACKGROUND("\033[47m"),

	//High intensity
	BLACK_BRIGHT("\033[0;90m"),
	RED_BRIGHT("\033[0;91m"),
	GREEN_BRIGHT("\033[0;92m"),
	YELLOW_BRIGHT("\033[0;93m"),
	BLUE_BRIGHT("\033[0;94m"),
	MAGENTA_BRIGHT("\033[0;95m"),
	CYAN_BRIGHT("\033[0;96m"),
	WHITE_BRIGHT("\033[0;97m"),

	//High intensity backgrounds
	BLACK_BACKGROUND_BRIGHT("\033[0;100m"),
	RED_BACKGROUND_BRIGHT("\033[0;101m"),
	GREEN_BACKGROUND_BRIGHT("\033[0;102m"),
	YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),
	BLUE_BACKGROUND_BRIGHT("\033[0;104m"),
	MAGENTA_BACKGROUND_BRIGHT("\033[0;105m"),
	CYAN_BACKGROUND_BRIGHT("\033[0;106m"),
	WHITE_BACKGROUND_BRIGHT("\033[0;107m");

	private final String namedColor;
	NColor(String namedColor){
		this.namedColor = namedColor;
	}
	
	@Override
	public String toString(){
		return namedColor;
	}
}