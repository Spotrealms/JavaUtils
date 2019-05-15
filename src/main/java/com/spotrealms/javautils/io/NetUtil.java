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

package com.spotrealms.javautils.io;

//Import first-party classes
import com.spotrealms.javautils.StringUtil;

//Import Java classes and dependencies
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class NetUtil {
	public static URL strToURL(String tString) throws MalformedURLException {
		//Make the URL object
		URL resURL = new URL(tString);
		return resURL;
	}
	
	public static String urlToStr(URL tURL) throws UnsupportedEncodingException {
		//Create the string from the URL
		String resStr = String.valueOf(tURL);
		
		//Decode the URL
		resStr = StringUtil.decodeURL(resStr);
		
		//Return the resulting string
		return resStr;
	}
}
