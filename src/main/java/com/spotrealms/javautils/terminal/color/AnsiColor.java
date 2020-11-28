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

import com.spotrealms.javautils.misc.ColorUtil;

/**
 * A series of methods that can be used to color the
 * terminal using ANSI escape sequences. Sometimes the 
 * generic &quot;white-on-black&quot; terminal style gets 
 * old, or the output of a Java program is hard to read. 
 * This class allows color to be added using ANSI escape
 * sequences, a relic from the text terminal days. Nowadays, 
 * these sequences aren't as well known, but most terminals
 * fully support their use. This class includes the following
 * methods for generating color in the terminal based on the
 * ANSI specifications laid out <a href="https://en.wikipedia.org/wiki/ANSI_escape_code">here</a>:
 * <ul>
 *  <li>4-Bit Color</li>
 *  <li>8-Bit Color</li>
 *  <li>24-Bit Color</li>
 * </ul>
 * Additional options for background and formatting are 
 * provided for each of the three generation methods.
 *
 * @author Spotrealms
 * @see NColor
 */
public final class AnsiColor {
	/** The "ESC" character that forms the basis of an ANSI escape sequence. **/
	private static final char ESC_CODE = '\033';

	/**
	 * Prevents instantiation of the utility class AnsiColor.
	 *
	 * @throws RuntimeException If instantiation occurs
	 */
	private AnsiColor(){ throw new RuntimeException("No " + this.getClass().getSimpleName() + " instance for you :)"); }

	/**
	 * Generates color in the terminal via the 4-bit
	 * color method. Any text that follows this method 
	 * will have the corresponding color applied that 
	 * is set via the parameters for this method. An 
	 * alternative to this method exists in the class 
	 * {@code NColor} if named colors are preferred.
	 * See <a href="https://en.wikipedia.org/wiki/ANSI_escape_code#3/4_bit">this site</a>
	 * for a complete list of codes that can be used in 
	 * this method.
	 *
	 * @param ansiCode The ANSI code that will be used to generate the color
	 * @param textMod The format the text should be in (0=nothing, 1=bold, 3=italic, 4=underline, 9=strikethrough)
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get4Color(final int ansiCode, final int textMod){
		//Construct and return the corresponding ANSI escape sequence
		return ESC_CODE + "[" + textMod + ";" + ansiCode + "m";
	}
	
	/**
	 * Generates color in the terminal via the 4-bit
	 * color method. Any text that follows this method 
	 * will have the corresponding color applied that 
	 * is set via the parameters for this method. An 
	 * alternative to this method exists in the class 
	 * {@code NColor} if named colors are preferred.
	 * See <a href="https://en.wikipedia.org/wiki/ANSI_escape_code#3/4_bit">this site</a>
	 * for a complete list of codes that can be used in 
	 * this method.
	 *
	 * @param ansiCode The ANSI code that will be used to generate the color
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get4Color(final int ansiCode){
		//Redirect back to the overloaded method and return the output
		return get4Color(ansiCode, 0);
	}
	
	/**
	 * Generates color in the terminal via the 4-bit
	 * color method. Any text that follows this method 
	 * will have the corresponding color applied that 
	 * is set via the parameters for this method. An 
	 * alternative to this method exists in the class 
	 * {@code NColor} if named colors are preferred.
	 * See <a href="https://en.wikipedia.org/wiki/ANSI_escape_code#8-bit">this site</a>
	 * (first 15 only) for a complete list of codes that can be used in 
	 * this method.
	 *
	 * @param ansiCode The ANSI code that will be used to generate the color (0-15)
	 * @param textMod The format the text should be in (0=nothing, 1=bold, 3=italic, 4=underline, 9=strikethrough)
	 * @param isBackground Set whether or not to apply the color to the background
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get4Color(final int ansiCode, final int textMod, final boolean isBackground){
		//Set the "magic numbers"
		final int RANGE_START = 30; //Range start for ANSI codes
		final int BASE_COLOR_COUNT = 8; //Number of base colors in the ANSI spec
		final int BRIGHT_START = 60; //Range start for bright colors
		final int BACKGROUND_START = 10; //Background range start

		//Create a new code integer from the input
		int ansi = ansiCode + RANGE_START;
		
		//Check if the code is greater than 7
		if(ansiCode >= BASE_COLOR_COUNT){
			//Subtract 8 from the code to get it back into the proper range
			ansi -= BASE_COLOR_COUNT;
			
			//Add 60 to the code to get it in range for the bright color selection
			ansi += BRIGHT_START;
		}
		
		//Check if the color should be applied to the background instead
		if(isBackground){
			//Add 10 to the code to get it in range for the background color selection
			ansi += BACKGROUND_START;
		}
		
		//Redirect back to the overloaded method and return the output
		return get4Color(ansi, textMod);
	}
	
	/**
	 * Generates color in the terminal via the 4-bit
	 * color method. Any text that follows this method 
	 * will have the corresponding color applied that 
	 * is set via the parameters for this method. An 
	 * alternative to this method exists in the class 
	 * {@code NColor} if named colors are preferred.
	 * See <a href="https://en.wikipedia.org/wiki/ANSI_escape_code#8-bit">this site</a>
	 * (first 15 only) for a complete list of codes that can be used in 
	 * this method.
	 *
	 * @param ansiCode The ANSI code that will be used to generate the color (0-15)
	 * @param isBackground Set whether or not to apply the color to the background
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get4Color(final int ansiCode, final boolean isBackground){
		//Redirect back to the overloaded method and return the output
		return get4Color(ansiCode, 0, isBackground);
	}

	/**
	 * Generates color in the terminal via the 8-bit
	 * color method. Any text that follows this method 
	 * will have the corresponding color applied that 
	 * is set via the parameters for this method.
	 * See <a href="https://jonasjacek.github.io/colors/">this site</a>
	 * for a complete list of codes that can be used in 
	 * this method.
	 *
	 * @param ansiCode The ANSI code that will be used to generate the color (0-255)
	 * @param isBackground Set whether or not to apply the color to the background
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get8Color(final int ansiCode, final boolean isBackground){
		//Set the "magic numbers"
		final int FOREGROUND = 38; //Foreground code according to the ANSI spec
		final int BACKGROUND = 48; //Background code according to the ANSI spec

		//Select the "ground" that should be used
		int groundSelect = isBackground ? BACKGROUND : FOREGROUND;
		
		//Construct and return the corresponding ANSI escape sequence
		return ESC_CODE + "[" + groundSelect + ";5;" + ansiCode + "m";
	}
	
	/**
	 * Generates color in the terminal via the 8-bit
	 * color method. Any text that follows this method 
	 * will have the corresponding color applied that 
	 * is set via the parameters for this method.
	 * See <a href="https://jonasjacek.github.io/colors/">this site</a>
	 * for a complete list of codes that can be used in 
	 * this method.
	 *
	 * @param ansiCode The ANSI code that will be used to generate the color (0-255)
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get8Color(final int ansiCode){
		//Redirect back to the overloaded method and return the output
		return get8Color(ansiCode, false);
	}
	
	/**
	 * Generates color in the terminal via the 8-bit
	 * color method. Any text that follows this method 
	 * will have the corresponding color applied that 
	 * is set via the parameters for this method.
	 * See <a href="https://jonasjacek.github.io/colors/">this site</a>
	 * for a complete list of codes that can be used in 
	 * this method.
	 *
	 * @param foregroundAnsiCode The ANSI code that will be used to generate the foreground color (0-255)
	 * @param backgroundAnsiCode The ANSI code that will be used to generate the background color (0-255)
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get8Color(final int foregroundAnsiCode, final int backgroundAnsiCode){
		//Get the foreground color sequence as a string
		String foreSeq = get8Color(foregroundAnsiCode, false);
		
		//Get the background color sequence as a string
		String backSeq = get8Color(backgroundAnsiCode, true);
		
		//Concat both sequences and return them as a string
		return foreSeq + backSeq;
	}

	/**
	 * Generates color in the terminal via the 24-bit
	 * color method. Any text that follows this method 
	 * will have the corresponding color applied that 
	 * is set via the parameters for this method.
	 * Standard CSS RGB color syntax can be used with 
	 * this method. See <a href="https://www.w3schools.com/colors/colors_picker.asp">this site</a>
	 * for a color picker that can generate RGB colors 
	 * for use in this method.
	 *
	 * @param red The amount of red in the output (0-255)
	 * @param green The amount of green in the output (0-255)
	 * @param blue The amount of blue in the output (0-255)
	 * @param isBackground Set whether or not to apply the color to the background
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get24Color(final int red, final int green, final int blue, final boolean isBackground){
		//Set the "magic numbers"
		final int FOREGROUND = 38; //Foreground code according to the ANSI spec
		final int BACKGROUND = 48; //Background code according to the ANSI spec

		//Select the "ground" that should be used
		int groundSelect = isBackground ? BACKGROUND : FOREGROUND;

		//Construct and return the corresponding ANSI escape sequence
		return ESC_CODE + "[" + groundSelect + ";2;" + red + ";" + green + ";" + blue + "m";
	}
	
	/**
	 * Generates color in the terminal via the 24-bit
	 * color method. Any text that follows this method 
	 * will have the corresponding color applied that 
	 * is set via the parameters for this method.
	 * Standard 6, as well as 3, digit CSS color codes
	 * can be used with this method. See <a href="https://www.w3schools.com/colors/colors_picker.asp">this site</a>
	 * for a color picker that can generate CSS
	 * colors for use in this method.
	 *
	 * @param colorCode The CSS code that will be used to generate the color (#000000-#FFFFFF/#000-#FFF)
	 * @param isBackground Set whether or not to apply the color to the background
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get24Color(final String colorCode, final boolean isBackground){
		//Get the input color code as an rgb array
		int[] rgb = ColorUtil.rgbaColor(colorCode);
		
		//Redirect back to the overloaded method and return the output
		return get24Color(rgb[0], rgb[1], rgb[2], isBackground);
	}
	
	/**
	 * Generates color in the terminal via the 24-bit
	 * color method. Any text that follows this method 
	 * will have the corresponding color applied that 
	 * is set via the parameters for this method.
	 * Standard 6, as well as 3, digit CSS color codes
	 * can be used with this method. See <a href="https://www.w3schools.com/colors/colors_picker.asp">this site</a>
	 * for a color picker that can generate CSS
	 * colors for use in this method.
	 *
	 * @param colorCode The CSS code that will be used to generate the color (#000000-#FFFFFF/#000-#FFF)
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get24Color(final String colorCode){
		//Redirect back to the overloaded method and return the output
		return get24Color(colorCode, false);
	}
	
	/**
	 * Generates color in the terminal via the 24-bit
	 * color method. Any text that follows this method 
	 * will have the corresponding color applied that 
	 * is set via the parameters for this method.
	 * Standard 6, as well as 3, digit CSS color codes
	 * can be used with this method. See <a href="https://www.w3schools.com/colors/colors_picker.asp">this site</a>
	 * for a color picker that can generate CSS
	 * colors for use in this method.
	 *
	 * @param foregroundColorCode The CSS code that will be used to generate the foreground color (#000000-#FFFFFF/#000-#FFF)
	 * @param backgroundColorCode The CSS code that will be used to generate the background color (#000000-#FFFFFF/#000-#FFF)
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get24Color(final String foregroundColorCode, final String backgroundColorCode){
		//Get the foreground color sequence as a string
		String foreSeq = get24Color(foregroundColorCode, false);
		
		//Get the background color sequence as a string
		String backSeq = get24Color(backgroundColorCode, true);
		
		//Concat both sequences and return them as a string
		return foreSeq + backSeq;
	}
}
