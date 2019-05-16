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

package com.spotrealms.javautils;

//Import first-party classes
import com.spotrealms.javautils.exception.KeyNotFoundException;

//Import Java classes and dependencies
import java.util.function.BiFunction;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO: Finish up JavaDoc

/**
 * A util/helper class (using jdk 8) which can format a 
 * string and replaces occurrences of variables. 
 * This class uses the "appendReplacement" method
 * which does all the substitution and loops only 
 * over the affected parts of a format string. 
 * <br>You can create a class instance for a specific 
 * Map with values (or suffix prefix or noKeyFunction)
 * like:
 * <pre>
 *  Map&lt;String, Object&gt; values = new HashMap&lt;&gt;();
 *  values.put("firstName", "Peter");
 *  values.put("lastName", "Parker");
 *  JPlaceholder JPlaceholder = new JPlaceholder(values);
 *  JPlaceholder.format("${firstName} ${lastName} is Spiderman!");
 *  // Result: "Peter Parker is Spiderman!"
 *  // Next format:
 *  JPlaceholder.format("Does ${firstName} ${lastName} works as photographer?");
 *  //Result: "Does Peter Parker works as photographer?"
 * </pre>
 * Furthermore, you can define what happens if a key in
 * the values Map is missing (works in both ways e.g. 
 * wrong variable name in format string or missing key 
 * in Map). The default behavior is an thrown unchecked 
 * exception (unchecked because I use the default jdk8 
 * Function which can't handle checked exceptions) like:
 * <pre>
 *  Map&lt;String, Object&gt; map = new HashMap&lt;&gt;();
 *  map.put("firstName", "Peter");
 *  map.put("lastName", "Parker");
 *  JPlaceholder JPlaceholder = new JPlaceholder(map);
 *  JPlaceholder.format("${missingName} ${lastName} is Spiderman!");
 *  //Result: RuntimeException: Key: missingName for variable ${missingName} not found.
 * </pre>
 * You can define a custom behavior in the constructor call like: 
 * <pre>
 *  Map&lt;String, Object&gt; values = new HashMap&lt;&gt;();
 *  values.put("firstName", "Peter");
 *  values.put("lastName", "Parker");
 *  JPlaceholder JPlaceholder = new JPlaceholder(fullMatch, variableName) -&gt; variableName.equals("missingName") ? "John": "SOMETHING_WRONG", values);
 *  JPlaceholder.format("${missingName} ${lastName} is Spiderman!");
 *  // Result: "John Parker is Spiderman!"
 * </pre>
 * or delegate it back to the default no key behavior:
 * <pre>
 *  ...
 *  JPlaceholder JPlaceholder = new JPlaceholder((fullMatch, variableName) -&gt;   variableName.equals("missingName") ? "John" :
 *  JPlaceholder.DEFAULT_NO_KEY_FUNCTION.apply(fullMatch, variableName), map);
 *  ...
 * </pre>
 * For better handling there are also static method representations like: 
 * <pre>
 *  Map&lt;String, Object&gt; values = new HashMap&lt;&gt;();
 *  values.put("firstName", "Peter");
 *  values.put("lastName", "Parker");
 *  JPlaceholder.format("${firstName} ${lastName} is Spiderman!", values);
 *  // Result: "Peter Parker is Spiderman!"
 * </pre>
 * 
 * @author schlegel11 &amp; Spotrealms
 * @see <a href="https://stackoverflow.com/a/51989500/7520602">https://stackoverflow.com/a/51989500/7520602</a>
 */
public class JPlaceholder {
	//Prefix and suffix for the enclosing variable name in the format string.
	//Replace the default values with any you need.
	public static final String DEFAULT_PREFIX = "${";
	public static final String DEFAULT_SUFFIX = "}";

	//Define dynamic function what happens if a key is not found.
	//Replace the default exception with any "unchecked" exception type you need or any other behavior.
	public static final BiFunction<String, String, String> DEFAULT_NO_KEY_FUNCTION = (fullMatch, variableName) -> {
		throw new KeyNotFoundException(String.format("Key: %s for variable %s not found.", variableName, fullMatch));
	};
	
	private final Pattern variablePattern;
	private final Map<String, Object> values;
	private final BiFunction<String, String, String> noKeyFunction;
	private final String prefix;
	private final String suffix;

	public JPlaceholder(Map<String, Object> values){
		this(DEFAULT_NO_KEY_FUNCTION, values);
	}

	public JPlaceholder(BiFunction<String, String, String> noKeyFunction, Map<String, Object> values){
		this(DEFAULT_PREFIX, DEFAULT_SUFFIX, noKeyFunction, values);
	}

	public JPlaceholder(String prefix, String suffix, Map<String, Object> values){
		this(prefix, suffix, DEFAULT_NO_KEY_FUNCTION, values);
	}

	public JPlaceholder(
		String prefix,
		String suffix,
		BiFunction<String, String, String> noKeyFunction,
		Map<String, Object> values
	){
		this.prefix = prefix;
		this.suffix = suffix;
		this.values = values;
		this.noKeyFunction = noKeyFunction;

		//Create the Pattern and quote the prefix and suffix so that the regex doesn't interpret special chars.
		//The variable name is a "\w+" in an extra capture group.
		//Placeholders also support extra data past the variable name, so (.*?) is added.
		variablePattern = Pattern.compile(Pattern.quote(prefix) + "(\\w+)(.*?)" + Pattern.quote(suffix));
	}

	public static String format(CharSequence format, Map<String, Object> values){
		return new JPlaceholder(values).format(format);
	}

	public static String format(
		CharSequence format,
		BiFunction<String, String, String> noKeyFunction,
		Map<String, Object> values
	){
		return new JPlaceholder(noKeyFunction, values).format(format);
	}

	public static String format(
		String prefix, 
		String suffix, 
		CharSequence format, 
		Map<String, Object> values
	){
		return new JPlaceholder(prefix, suffix, values).format(format);
	}

	public static String format(
		String prefix,
		String suffix,
		BiFunction<String, String, String> noKeyFunction,
		CharSequence format,
		Map<String, Object> values
	){
		return new JPlaceholder(prefix, suffix, noKeyFunction, values).format(format);
	}

	public String format(CharSequence format){
		//Create matcher based on the init pattern for variable names.
		Matcher matcher = variablePattern.matcher(format);

		//This buffer will hold all parts of the formatted finished string.
		StringBuffer formatBuffer = new StringBuffer();

		//loop while the matcher finds another variable (prefix -> name <- suffix) match
		while(matcher.find()){
			//The root capture group with the full match e.g ${variableName}
			String fullMatch = matcher.group();

			//The capture group for the variable name resulting from "(\w+)" e.g. variableName
			String variableName = matcher.group(1);

			//Get the value in our Map so the Key is the used variable name in our "format" string. The associated value will replace the variable.
			//If key is missing (absent) call the noKeyFunction with parameters "fullMatch" and "variableName" else return the value.
			String value = values.computeIfAbsent(variableName, key -> noKeyFunction.apply(fullMatch, key)).toString();

			//Escape the Map value because the "appendReplacement" method interprets the $ and \ as special chars.
			String escapedValue = Matcher.quoteReplacement(value);

			//The "appendReplacement" method replaces the current "full" match (e.g. ${variableName}) with the value from the "values" Map.
			//The replaced part of the "format" string is appended to the StringBuffer "formatBuffer".
			matcher.appendReplacement(formatBuffer, escapedValue);
		}

		//The "appendTail" method appends the last part of the "format" String which has no regex match.
		//That means if e.g. our "format" string has no matches the whole untouched "format" string is appended to the StringBuffer "formatBuffer".
		//Further more the method return the buffer.
		return matcher.appendTail(formatBuffer).toString();
	}

	public String getPrefix(){
		return prefix;
	}

	public String getSuffix(){
		return suffix;
	}

	public Map<String, Object> getValues(){
		return values;
	}
	
	public static String isolatePName(String placeholderFull, String prefix, String suffix){
		//Create the final string to hold the placeholder
		String placeholderName = placeholderFull;
				
		//Remove the prefix
		placeholderName = placeholderName.replace(prefix, "");
						
		//Remove the suffix
		placeholderName = placeholderName.replace(suffix, "");
						
		//Return the placeholder name
		return placeholderName;
	}
	
	public String isolatePName(String placeholderFull){
		//Redirect to the overloaded method
		return isolatePName(placeholderFull, this.prefix, this.suffix);
	}
	
	public LinkedHashMap<String, Object> getPlVals(String inputStr){
		//Call the original method with the prefix and suffix set as the default values for each
		return getPlValues(inputStr, this.prefix, this.suffix);
		
	}
	
	public static LinkedHashMap<String, Object> getPlValues(String inputStr){
		//Call the original method with the prefix and suffix set as the default values for each
		return getPlValues(inputStr, DEFAULT_PREFIX, DEFAULT_SUFFIX);
	}
	
	public static LinkedHashMap<String, Object> getPlValues(String inputStr, String plPrefix, String plSuffix){
		//Setup the variable to hold the placeholder values and embedded data
		LinkedHashMap<String, Object> placeholderData = new LinkedHashMap<>();

		//Set the pattern to use on the string based on the expected syntax of the placeholder (${placeholder-name <placeholder data>})
		//String plRegex = "\\$\\{(\\w+)(.*?)\\}";
		String plRegex = (StringUtil.escAllChars(plPrefix) + "(\\w+)(.*?)" + StringUtil.escAllChars(plSuffix));

		//Setup the pattern to use for the matcher
		Pattern plPtn = Pattern.compile(plRegex);
		    
		//Create the matcher to use on the input string
		Matcher plMatch = plPtn.matcher(inputStr);
		    
		//Loop through the matcher finds
		while(plMatch.find()){
			//Get the current matched key
			String curKey = plMatch.group(1);

			//Get the current matched value
			String curValue = (plMatch.group(2));

			//Check if the value is empty (avoids out of bounds exceptions when trying to remove the leading space in the value)
			if(!(curValue.isEmpty())){
				//Remove the space before the value
				curValue = curValue.substring(1, curValue.length());

				//Check if the value contains a nested placeholder (a prefix will indicate this)
				if(curValue.contains(plPrefix)){
					//Count the number of occurrences in the string
					int prefixOccurrances = StringUtil.countInStr(curValue, plPrefix);

					//Append the suffix to the end of the value for however many times the prefix appears
					curValue = (curValue + StringUtil.cloneStr(plSuffix, prefixOccurrances));
				}
			}

			//Populate the final HashMap with the found values in the placeholder
			placeholderData.put(curKey, curValue);
		}

		//Return the populated HashMap
		return placeholderData;
	}
	
	//TODO: Move to JUnit test instead of declaring it here in the class itself
	
	//TESTING
	/*
	public static void main(String[] args){
		Map<String, Object> values = new java.util.HashMap<>();
		values.put("firstName", "Peter");
		values.put("lastName", "Parker");
		System.out.println(
				JPlaceholder.format("${firstName} ${lastName} is Spiderman!", values)
		);
		// Result: "Peter Parker is Spiderman!"
	}
	*/
}
