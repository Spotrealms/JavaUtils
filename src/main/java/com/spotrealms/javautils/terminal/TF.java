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

package com.spotrealms.javautils.terminal;

/**
 * Colors the terminal text using ASCII escape sequences.
 * Sometimes the generic &quot;white-on-black&quot; terminal
 * style gets old, or the output of a Java program is hard to 
 * read. This class allows color to be added using ANSI escape
 * sequences, a relic from the text terminal days. Nowadays, 
 * these sequences aren't as well known, but Java fully supports
 * their use in the terminal. 
 * <br>This class provides the following colors
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
 * Additionally, the following color modifications can be added 
 * (their support depends on the terminal used):
 * <ul>
 * 	<li>Bold</li>
 * 	<li>Underline
 * 	<li>High Intensity</li>
 * 	<li>Bold High Intensity</li>
 * </ul>
 * The background can also be colored and use high intensity modification.
 * The color can also be reset to the default simply by using {@code RESET}.
 * 
 * @author Spotrealms Network & Contributors
 * @website http://ascii-table.com/ansi-escape-sequences.php
 * @website https://stackoverflow.com/a/51944613/7520602
 */
public class TF {
	public static enum Color {
		//Reset
		RESET("\033[0m"),

		// Regular Colors
		BLACK("\033[0;30m"),    // BLACK
		RED("\033[0;31m"),      // RED
		GREEN("\033[0;32m"),    // GREEN
		YELLOW("\033[0;33m"),   // YELLOW
		BLUE("\033[0;34m"),     // BLUE
		MAGENTA("\033[0;35m"),  // MAGENTA
		CYAN("\033[0;36m"),     // CYAN
		WHITE("\033[0;37m"),    // WHITE

		// Bold
		BLACK_BOLD("\033[1;30m"),   // BLACK
		RED_BOLD("\033[1;31m"),     // RED
		GREEN_BOLD("\033[1;32m"),   // GREEN
		YELLOW_BOLD("\033[1;33m"),  // YELLOW
		BLUE_BOLD("\033[1;34m"),    // BLUE
		MAGENTA_BOLD("\033[1;35m"), // MAGENTA
		CYAN_BOLD("\033[1;36m"),    // CYAN
		WHITE_BOLD("\033[1;37m"),   // WHITE

		// Underline
		BLACK_UNDERLINED("\033[4;30m"),     // BLACK
		RED_UNDERLINED("\033[4;31m"),       // RED
		GREEN_UNDERLINED("\033[4;32m"),     // GREEN
		YELLOW_UNDERLINED("\033[4;33m"),    // YELLOW
		BLUE_UNDERLINED("\033[4;34m"),      // BLUE
		MAGENTA_UNDERLINED("\033[4;35m"),   // MAGENTA
		CYAN_UNDERLINED("\033[4;36m"),      // CYAN
		WHITE_UNDERLINED("\033[4;37m"),     // WHITE

		// Background
		BLACK_BACKGROUND("\033[40m"),   // BLACK
		RED_BACKGROUND("\033[41m"),     // RED
		GREEN_BACKGROUND("\033[42m"),   // GREEN
		YELLOW_BACKGROUND("\033[43m"),  // YELLOW
		BLUE_BACKGROUND("\033[44m"),    // BLUE
		MAGENTA_BACKGROUND("\033[45m"), // MAGENTA
		CYAN_BACKGROUND("\033[46m"),    // CYAN
		WHITE_BACKGROUND("\033[47m"),   // WHITE

		// High Intensity
		BLACK_BRIGHT("\033[0;90m"),     // BLACK
		RED_BRIGHT("\033[0;91m"),       // RED
		GREEN_BRIGHT("\033[0;92m"),     // GREEN
		YELLOW_BRIGHT("\033[0;93m"),    // YELLOW
		BLUE_BRIGHT("\033[0;94m"),      // BLUE
		MAGENTA_BRIGHT("\033[0;95m"),   // MAGENTA
		CYAN_BRIGHT("\033[0;96m"),      // CYAN
		WHITE_BRIGHT("\033[0;97m"),     // WHITE

		// Bold High Intensity
		BLACK_BOLD_BRIGHT("\033[1;90m"),    // BLACK
		RED_BOLD_BRIGHT("\033[1;91m"),      // RED
		GREEN_BOLD_BRIGHT("\033[1;92m"),    // GREEN
		YELLOW_BOLD_BRIGHT("\033[1;93m"),   // YELLOW
		BLUE_BOLD_BRIGHT("\033[1;94m"),     // BLUE
		MAGENTA_BOLD_BRIGHT("\033[1;95m"),  // MAGENTA
		CYAN_BOLD_BRIGHT("\033[1;96m"),     // CYAN
		WHITE_BOLD_BRIGHT("\033[1;97m"),    // WHITE

		// High Intensity Backgrounds
		BLACK_BACKGROUND_BRIGHT("\033[0;100m"),     // BLACK
		RED_BACKGROUND_BRIGHT("\033[0;101m"),       // RED
		GREEN_BACKGROUND_BRIGHT("\033[0;102m"),     // GREEN
		YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),    // YELLOW
		BLUE_BACKGROUND_BRIGHT("\033[0;104m"),      // BLUE
		MAGENTA_BACKGROUND_BRIGHT("\033[0;105m"),   // MAGENTA
		CYAN_BACKGROUND_BRIGHT("\033[0;106m"),      // CYAN
		WHITE_BACKGROUND_BRIGHT("\033[0;107m");     // WHITE

		private final String code;
		Color(String code){
			this.code = code;
		}
		
		@Override
		public String toString(){
			return code;
		}
	}
	
	/*
	//EXAMPLE: 
	public static void main(String[] args){
		System.out.println(TF.Color.RED_BRIGHT + "Y" + TF.Color.WHITE_BRIGHT + "E" + TF.Color.CYAN_BRIGHT + "E" + TF.Color.RESET);
		System.out.println(TF.Color.RED_BACKGROUND + "YEE" + TF.Color.RESET);
		System.out.println(TF.Color.RED_UNDERLINED + "YEE" + TF.Color.RESET);
	}
	*/
}