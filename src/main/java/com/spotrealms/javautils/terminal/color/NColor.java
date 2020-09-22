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
 * @author Spotrealms &amp; shakram02
 * @see AnsiColor
 * @see <a href="http://ascii-table.com/ansi-escape-sequences.php">http://ascii-table.com/ansi-escape-sequences.php</a>
 * @see <a href="https://stackoverflow.com/a/45444716">https://stackoverflow.com/a/45444716</a>
 */
public enum NColor {
	/** ANSI reset. **/
	RESET("\033[0m"),
	
	//Text modification
	/** ANSI bold. **/
	BOLD("\033[1m"),
	/** ANSI italic. **/
	ITALIC("\033[3m"),
	/** ANSI underline. **/
	UNDERLINE("\033[4m"),
	/** ANSI strikethrough. **/
	STRIKE("\033[9m"),

	//Regular Colors
	/** ANSI black. **/
	BLACK("\033[0;30m"),
	/** ANSI red. **/
	RED("\033[0;31m"),
	/** ANSI green. **/
	GREEN("\033[0;32m"),
	/** ANSI yellow/gold. **/
	YELLOW("\033[0;33m"),
	/** ANSI blue. **/
	BLUE("\033[0;34m"),
	/** ANSI magenta/purple. **/
	MAGENTA("\033[0;35m"),
	/** ANSI cyan. **/
	CYAN("\033[0;36m"),
	/** ANSI white/grey. **/
	WHITE("\033[0;37m"),

	//Background
	/** ANSI black applied to the background. **/
	BLACK_BACKGROUND("\033[40m"),
	/** ANSI red applied to the background. **/
	RED_BACKGROUND("\033[41m"),
	/** ANSI green applied to the background. **/
	GREEN_BACKGROUND("\033[42m"),
	/** ANSI yellow/gold applied to the background. **/
	YELLOW_BACKGROUND("\033[43m"),
	/** ANSI blue applied to the background. **/
	BLUE_BACKGROUND("\033[44m"),
	/** ANSI magenta/purple applied to the background. **/
	MAGENTA_BACKGROUND("\033[45m"),
	/** ANSI cyan applied to the background. **/
	CYAN_BACKGROUND("\033[46m"),
	/** ANSI white/grey applied to the background. **/
	WHITE_BACKGROUND("\033[47m"),

	//High intensity
	/** ANSI high intensity black/dark grey. **/
	BLACK_BRIGHT("\033[0;90m"),
	/** ANSI high intensity red. **/
	RED_BRIGHT("\033[0;91m"),
	/** ANSI high intensity green. **/
	GREEN_BRIGHT("\033[0;92m"),
	/** ANSI high intensity yellow/gold. **/
	YELLOW_BRIGHT("\033[0;93m"),
	/** ANSI high intensity blue. **/
	BLUE_BRIGHT("\033[0;94m"),
	/** ANSI high intensity magenta/purple. **/
	MAGENTA_BRIGHT("\033[0;95m"),
	/** ANSI high intensity cyan. **/
	CYAN_BRIGHT("\033[0;96m"),
	/** ANSI high intensity white/grey. **/
	WHITE_BRIGHT("\033[0;97m"),

	//High intensity backgrounds
	/** ANSI high intensity black/dark grey applied to the background. **/
	BLACK_BACKGROUND_BRIGHT("\033[0;100m"),
	/** ANSI high intensity red applied to the background. **/
	RED_BACKGROUND_BRIGHT("\033[0;101m"),
	/** ANSI high intensity green applied to the background. **/
	GREEN_BACKGROUND_BRIGHT("\033[0;102m"),
	/** ANSI high intensity yellow/gold applied to the background. **/
	YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),
	/** ANSI high intensity blue applied to the background. **/
	BLUE_BACKGROUND_BRIGHT("\033[0;104m"),
	/** ANSI high intensity magenta/purple applied to the background. **/
	MAGENTA_BACKGROUND_BRIGHT("\033[0;105m"),
	/** ANSI high intensity cyan applied to the background. **/
	CYAN_BACKGROUND_BRIGHT("\033[0;106m"),
	/** ANSI high intensity white/grey applied to the background. **/
	WHITE_BACKGROUND_BRIGHT("\033[0;107m");

	//Enum routines
	private final String namedColor;
	NColor(final String namedColor){ this.namedColor = namedColor; }
	
	@Override
	public String toString(){
		return namedColor;
	}
}
