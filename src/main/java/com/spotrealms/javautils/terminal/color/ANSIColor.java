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

//Import first-party classes
import com.spotrealms.javautils.StringUtil;
import com.spotrealms.javautils.math.NumberSystem;

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
public class ANSIColor {
	//Set default class variables
	private static final char escCode = '\033';

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
	 * @param ansiCode The ANSI code that will be used to generate the color
	 * @param textMod The format the text should be in (0=nothing, 1=bold, 3=italic, 4=underline, 9=strikethrough)
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get4Color(int ansiCode, int textMod){
		//Construct and return the corresponding ANSI escape sequence
		return (escCode + "[" + textMod + ";" + ansiCode + "m");
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
	 * @param ansiCode The ANSI code that will be used to generate the color
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get4Color(int ansiCode){
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
	 * @param ansiCode The ANSI code that will be used to generate the color (0-15)
	 * @param textMod The format the text should be in (0=nothing, 1=bold, 3=italic, 4=underline, 9=strikethrough)
	 * @param isBackground Set whether or not to apply the color to the background
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get4Color(int ansiCode, int textMod, boolean isBackground){
		//Add 30 to the ANSI code to get it in range for the color selection
		ansiCode += 30;
		
		//Check if the code is greater than 7
		if(ansiCode > 7){
			//Subtract 8 from the code to get it back into the proper range
			ansiCode -= 8;
			
			//Add 60 to the code to get it in range for the bright color selection
			ansiCode += 60;
		}
		
		//Check if the color should be applied to the background instead
		if(isBackground){
			//Add 10 to the code to get it in range for the background color selection
			ansiCode += 10;
		}
		
		//Redirect back to the overloaded method and return the output
		return get4Color(ansiCode, textMod);
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
	 * @param ansiCode The ANSI code that will be used to generate the color (0-15)
	 * @param isBackground Set whether or not to apply the color to the background
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get4Color(int ansiCode, boolean isBackground){
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
	 * @param ansiCode The ANSI code that will be used to generate the color (0-255)
	 * @param isBackground Set whether or not to apply the color to the background
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get8Color(int ansiCode, boolean isBackground){
		//Create an integer to hold the value to choose between foreground or background
		//38 = foreground, 48 = background
		int groundSelect = 38;
		
		//Check if the background should be manipulated instead
		if(isBackground){
			//Set the ground selector to be 48, which tells the terminal to apply the color to the background instead
			groundSelect = 48;
		}
		
		//Construct and return the corresponding ANSI escape sequence
		return (escCode + "[" + groundSelect + ";5;" + ansiCode + "m");
	}
	
	/**
	 * Generates color in the terminal via the 8-bit
	 * color method. Any text that follows this method 
	 * will have the corresponding color applied that 
	 * is set via the parameters for this method.
	 * See <a href="https://jonasjacek.github.io/colors/">this site</a>
	 * for a complete list of codes that can be used in 
	 * this method.
	 * @param ansiCode The ANSI code that will be used to generate the color (0-255)
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get8Color(int ansiCode){
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
	 * @param foregroundAnsiCode The ANSI code that will be used to generate the foreground color (0-255)
	 * @param backgroundAnsiCode The ANSI code that will be used to generate the background color (0-255)
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get8Color(int foregroundAnsiCode, int backgroundAnsiCode){
		//Get the foreground color sequence as a string
		String foreSeq = get8Color(foregroundAnsiCode, false);
		
		//Get the background color sequence as a string
		String backSeq = get8Color(backgroundAnsiCode, true);
		
		//Concat both sequences and return them as a string
		return (foreSeq + backSeq);
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
	 * @param red The amount of red in the output (0-255)
	 * @param green The amount of green in the output (0-255)
	 * @param blue The amount of blue in the output (0-255)
	 * @param isBackground Set whether or not to apply the color to the background
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get24Color(int red, int green, int blue, boolean isBackground){
		//Create an integer to hold the value to choose between foreground or background
		//38 = foreground, 48 = background
		int groundSelect = 38;

		//Check if the background should be manipulated instead
		if(isBackground){
			//Set the ground selector to be 48, which tells the terminal to apply the color to the background instead
			groundSelect = 48;
		}

		//Construct and return the corresponding ANSI escape sequence
		return (escCode + "[" + groundSelect + ";2;" + red + ";" + green + ";" + blue + "m");
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
	 * @param colorCode The CSS code that will be used to generate the color (#000000-#FFFFFF/#000-#FFF)
	 * @param isBackground Set whether or not to apply the color to the background
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get24Color(String colorCode, boolean isBackground){
		//Remove any chars that aren't hexadecimal digits (includes the pound sign denoting that the string is a color code)
		colorCode = colorCode.replaceAll("(?i)[^0-9A-F]+", "");
		
		//Check if the length of the code is 3 (codes are still valid if they are 3 chars long)
		if((colorCode.length() == 3)){
			//Create a StringBuilder for temporary storage of the color code
			StringBuilder tempCode = new StringBuilder(colorCode);
			
			//Loop through the code
			for(int i=0; i<colorCode.length(); i++){
				//Get the current char
				String curDigit = String.valueOf(colorCode.charAt(i));
				
				//Dupe the char and append the digit and its duplicate onto the StringBuilder
				tempCode.append(StringUtil.cloneStr(curDigit, 1));
			}
			
			//Replace the color code with the temporary color code string
			colorCode = tempCode.toString();	
		}
		
		//Check if the code is longer than 6
		if(colorCode.length() > 6){
			//Chop off every char past the 6th one
			colorCode = colorCode.substring(0, 6);
		}
		
		//Check if the code is not 3 or 6 chars long
		if((colorCode.length() != 3) && (colorCode.length() != 6)){
			//Create a StringBuilder for temporary storage of the color code
			StringBuilder tempCode = new StringBuilder(colorCode);
			
			//Loop until the code equals 6 chars long
			while(tempCode.length() != 6){
				//Add a 0 onto the end of the code
				tempCode.append("0");
			}
			
			//Replace the color code with the temporary color code string
			colorCode = tempCode.toString();
		}
		
		//Split the code into its red, green, and blue parts
		String[] rgbParts = colorCode.split("(?<=\\G..)");
		
		//Get the red, green, and blue parts of the input and convert the hexadecimal strings to decimal ones
		String redVal = NumberSystem.strHexToDec((rgbParts[0]));
		String greenVal = NumberSystem.strHexToDec((rgbParts[1]));
		String blueVal = NumberSystem.strHexToDec((rgbParts[2]));
		
		//Redirect back to the overloaded method and return the output
		return get24Color(Integer.parseInt(redVal), Integer.parseInt(greenVal), Integer.parseInt(blueVal), isBackground);
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
	 * @param colorCode The CSS code that will be used to generate the color (#000000-#FFFFFF/#000-#FFF)
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get24Color(String colorCode){
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
	 * @param foregroundColorCode The CSS code that will be used to generate the foreground color (#000000-#FFFFFF/#000-#FFF)
	 * @param backgroundColorCode The CSS code that will be used to generate the background color (#000000-#FFFFFF/#000-#FFF)
	 * @return <b>String</b> The assembled ANSI escape sequence
	 */
	public static String get24Color(String foregroundColorCode, String backgroundColorCode){
		//Get the foreground color sequence as a string
		String foreSeq = get24Color(foregroundColorCode, false);
		
		//Get the background color sequence as a string
		String backSeq = get24Color(backgroundColorCode, true);
		
		//Concat both sequences and return them as a string
		return (foreSeq + backSeq);
	}
}
