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

package com.spotrealms.javautils.misc;

import java.util.Arrays;
import java.util.Locale;

/**
 * A series of methods for working with colors in
 * Java (eg: rgba, hsla, hex representations, etc).
 *
 * @author Spotrealms
 */
public final class ColorUtil {
	/** The maximum value for the alpha channel. */
	private static final int ALPHA_MAX = 255;
	/** The radix for a hexadecimal number. */
	private static final int HEX_RADIX = 16;
	/** Indicates that the alpha channel should be omitted. */
	private static final int NO_ALPHA = -1;

	/**
	 * Prevents instantiation of the utility class ColorUtil.
	 *
	 * @throws RuntimeException If instantiation occurs
	 */
	private ColorUtil(){ throw new RuntimeException("No " + this.getClass().getSimpleName() + " instance for you :)"); }

	/**
	 * Gets the foreground color as either white or
	 * black depending on the background, according
	 * to the W3C spec which can be found below
	 * (see Appendix A: relative luminance). The
	 * luminance is calculated via the ITU-R's BT.709
	 * recommendation which can also be found below.
	 *
	 * @param backgroundHex The color of the background
	 * @return The best fitting color for the foreground
	 * @see <a href="https://www.w3.org/TR/WCAG20/#glossary">https://www.w3.org/TR/WCAG20/#glossary</a>
	 */
	public static String getForeground(final String backgroundHex){
		//Get the input color as an rgb array
		int[] rgb = ColorUtil.rgbaColor(backgroundHex);

		//Compute the luminances of each color (gamma adjusted normalized rgb values) via Java streams
		double[] lumas = Arrays.stream(rgb).mapToDouble(d -> d).map(e -> Math.pow(e / 255.0, 2.2)).toArray();

		//Compute the overall luminance of the color via ITU-R BT.709
		double luma = 0.2126 * lumas[0] + 0.7152 * lumas[1] + 0.0722 * lumas[2];

		//Apply the W3C's suggested algorithm and return black if true
		return luma > Math.sqrt(1.05 * 0.05) - 0.05 ? "#000000" : "#FFFFFF";
	}

	/**
	 * Converts an input color hex string to a normalized
	 * hexadecimal representation.
	 *
	 * @param hexIn The "dirty" color hex string
	 * @param alpha The alpha value (eg: transparency where 0 is fully transparent and 255 is fully opaque)
	 * @param postfixAlpha Whether or not to prepend or append the alpha value
	 * @return The corrected color hex string
	 */
	public static String hexColor(final String hexIn, final int alpha, final boolean postfixAlpha){
		//Check if the alpha is out of bounds for an acceptable alpha value (0-255) (indicates alpha should be omitted)
		boolean noAlpha = alpha < 0 || alpha > ALPHA_MAX;

		//Set the "magic numbers"
		final int HALF_HEX = 3; //The length of a half hex color (eg: #F00)
		final int FULL_HEX = 6; //The length of a full hex code (eg: #25F3B9)

		//Create a new code string from the input
		String code = hexIn;

		//Remove any chars that aren't hexadecimal digits (includes the pound sign denoting that the string is a color code)
		code = code.replaceAll("(?i)[^0-9A-F]+", "");

		//Check if the length of the code is 3 (codes are still valid if they are 3 chars long)
		if(code.length() == HALF_HEX){
			//Create a StringBuilder for temporary storage of the color code
			StringBuilder tempCode = new StringBuilder(code);

			//Create an integer to keep track of the position in the StringBuilder
			int codePos = 0;

			//Loop through the code
			for(int i = 0; i < code.length(); i++){
				//Get the current char
				String curDigit = String.valueOf(code.charAt(i));

				//Dupe the char and append the digit and its duplicate onto the StringBuilder
				tempCode.insert(codePos, StringUtil.cloneStr(curDigit, 1));

				//Increment the position integer by two (one for the new char and one for the increment of i
				codePos += 2;
			}

			//Replace the color code with the temporary color code string
			code = tempCode.toString();
		}

		//Check if the code is longer than 6
		if(code.length() > FULL_HEX){
			//Chop off every char past the 6th one
			code = code.substring(0, FULL_HEX);
		}

		//Check if the code is not 3 or 6 chars long
		if(code.length() != HALF_HEX && code.length() != FULL_HEX){
			//Create a StringBuilder for temporary storage of the color code
			StringBuilder tempCode = new StringBuilder(code);

			//Loop until the code equals 6 chars long
			while(tempCode.length() != FULL_HEX){
				//Add a 0 onto the end of the code
				tempCode.append("0");
			}

			//Replace the color code with the temporary color code string
			code = tempCode.toString();
		}

		//Convert the alpha value to hexadecimal, prepending a padding 0 if the value is less than 15 if alpha is enabled that is
		String alphaCode = noAlpha ? "" : alpha < HEX_RADIX ? "0" + Long.toHexString(alpha) : Long.toHexString(alpha);

		//Assemble the final code and return it
		return "#" + (postfixAlpha ? code + alphaCode.toUpperCase(Locale.ENGLISH) : alphaCode.toUpperCase(Locale.ENGLISH) + code);
	}

	/**
	 * Converts an input color hex string to a normalized
	 * hexadecimal representation.
	 *
	 * @param hexIn The "dirty" color hex string
	 * @return The corrected color hex string
	 */
	public static String hexColor(final String hexIn){
		//Pass to the overloaded method, but indicate the desire to not have alpha
		return hexColor(hexIn, NO_ALPHA, true);
	}

	/**
	 * Converts an input color hex string to an acceptable
	 * integer representation.
	 *
	 * @param hexIn The "dirty" color hex string
	 * @param alpha The alpha value (eg: transparency where 0 is fully transparent and 255 is fully opaque)
	 * @param postfixAlpha Whether or not to prepend or append the alpha value
	 * @return The corrected color hex string as an integer
	 */
	public static int intColor(final String hexIn, final int alpha, final boolean postfixAlpha){
		//Get the string representation of the hex code and parse the code as an integer
		return (int) Long.parseLong(hexColor(hexIn, alpha, postfixAlpha).substring(1), HEX_RADIX);
	}

	/**
	 * Converts an input color hex string to an acceptable
	 * integer representation.
	 *
	 * @param hexIn The "dirty" color hex string
	 * @return The corrected color hex string as an integer
	 */
	public static int intColor(final String hexIn){
		//Get the string representation of the hex code without the alpha and parse the code as an integer
		return (int) Long.parseLong(hexColor(hexIn).substring(1), HEX_RADIX);
	}

	/**
	 * Converts an input color hex string to an array
	 * of integers, where the order is as follows:
	 * {@code [a, r, g, b]} if {@code postFixAlpha} is
	 * false and {@code [r, g, b, a]} if {@code postFixAlpha}
	 * is true.
	 *
	 * @param hexIn The "dirty" color hex string
	 * @param alpha The alpha value (eg: transparency where 0 is fully transparent and 255 is fully opaque)
	 * @param postfixAlpha Whether or not to prepend or append the alpha value
	 * @return The corrected color hex string as an integer array with order of either {@code [a, r, g, b]} or {@code [r, g, b, a]}
	 */
	public static int[] rgbaColor(final String hexIn, final int alpha, final boolean postfixAlpha){
		//Get the string representation of the hex code
		String code = hexColor(hexIn, alpha, postfixAlpha).substring(1);

		//Split the code into its red, green, blue, and alpha parts and return it as an int array via Java 8 streams
		return Arrays.stream(code.split("(?<=\\G..)")).map(e -> Integer.parseInt(e, HEX_RADIX)).mapToInt(Integer::intValue).toArray();
	}

	/**
	 * Converts an input color hex string to an array
	 * of integers, where the order is @code [r, g, b]}.
	 *
	 * @param hexIn The "dirty" color hex string
	 * @return The corrected color hex string as an integer array with order of {@code [r, g, b]}
	 */
	public static int[] rgbaColor(final String hexIn){
		//Get the string representation of the hex code without the alpha
		String code = hexColor(hexIn).substring(1);

		//Split the code into its red, green, blue, and alpha parts and return it as an int array via Java 8 streams
		return Arrays.stream(code.split("(?<=\\G..)")).map(e -> Integer.parseInt(e, HEX_RADIX)).mapToInt(Integer::intValue).toArray();
	}
}
