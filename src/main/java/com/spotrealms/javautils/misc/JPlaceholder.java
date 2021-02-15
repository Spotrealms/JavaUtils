/*
 * JavaUtils: A collection of utility methods and classes for your Java programs
 *   Copyright (C) 2015-2021 Spotrealms Network
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

import com.spotrealms.javautils.exception.KeyNotFoundException;
import com.spotrealms.javautils.terminal.color.AnsiColor;
import com.spotrealms.javautils.terminal.color.NColor;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
 * @see <a href="https://stackoverflow.com/a/51989500">https://stackoverflow.com/a/51989500</a>
 */
public class JPlaceholder {
	/** The default placeholder prefix. **/
	public static final String DEFAULT_PREFIX = "${";

	/** The default placeholder suffix. **/
	public static final String DEFAULT_SUFFIX = "}";

	/**
	 * Define dynamic function what happens if a key is not found.
	 * Replace the default exception with any "unchecked" exception
	 * type you need or any other behavior.
	 */
	public static final BiFunction<String, String, String> DEFAULT_NO_KEY_FUNCTION = (fullMatch, variableName) -> {
		throw new KeyNotFoundException(String.format("Key: %s for variable %s not found.", variableName, fullMatch));
	};
	
	private final Pattern variablePattern;
	private final Map<String, Object> values;
	private final BiFunction<String, String, String> noKeyFunction;
	private final String prefix;
	private final String suffix;

	public JPlaceholder(final Map<String, Object> values){
		this(DEFAULT_NO_KEY_FUNCTION, values);
	}

	public JPlaceholder(final BiFunction<String, String, String> noKeyFunction, final Map<String, Object> values){
		this(DEFAULT_PREFIX, DEFAULT_SUFFIX, noKeyFunction, values);
	}

	public JPlaceholder(final String prefix, final String suffix, final Map<String, Object> values){
		this(prefix, suffix, DEFAULT_NO_KEY_FUNCTION, values);
	}

	public JPlaceholder(
		final String prefix,
		final String suffix,
		final BiFunction<String, String, String> noKeyFunction,
		final Map<String, Object> values
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

	public static String format(final CharSequence format, final Map<String, Object> values){
		return new JPlaceholder(values).format(format);
	}

	public static String format(
		final CharSequence format,
		final BiFunction<String, String, String> noKeyFunction,
		final Map<String, Object> values
	){
		return new JPlaceholder(noKeyFunction, values).format(format);
	}

	public static String format(
		final String prefix,
		final String suffix,
		final CharSequence format,
		final Map<String, Object> values
	){
		return new JPlaceholder(prefix, suffix, values).format(format);
	}

	public static String format(
		final String prefix,
		final String suffix,
		final BiFunction<String, String, String> noKeyFunction,
		final CharSequence format,
		final Map<String, Object> values
	){
		return new JPlaceholder(prefix, suffix, noKeyFunction, values).format(format);
	}

	public String format(final CharSequence format){
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
	
	public static String isolatePName(final String placeholderFull, final String prefix, final String suffix){
		//Create the final string to hold the placeholder
		String placeholderName = placeholderFull;
				
		//Remove the prefix
		placeholderName = placeholderName.replace(prefix, "");
						
		//Remove the suffix
		placeholderName = placeholderName.replace(suffix, "");
						
		//Return the placeholder name
		return placeholderName;
	}
	
	public String isolatePName(final String placeholderFull){
		//Redirect to the overloaded method
		return isolatePName(placeholderFull, this.prefix, this.suffix);
	}
	
	public LinkedHashMap<String, Object> getPlVals(final String inputStr){
		//Call the original method with the prefix and suffix set as the default values for each
		return getPlValues(inputStr, this.prefix, this.suffix);
		
	}
	
	public static LinkedHashMap<String, Object> getPlValues(final String inputStr){
		//Call the original method with the prefix and suffix set as the default values for each
		return getPlValues(inputStr, DEFAULT_PREFIX, DEFAULT_SUFFIX);
	}
	
	public static LinkedHashMap<String, Object> getPlValues(final String inputStr, final String plPrefix, final String plSuffix){
		//Setup the variable to hold the placeholder values and embedded data
		LinkedHashMap<String, Object> placeholderData = new LinkedHashMap<>();
		
		//Set the pattern to use on the string based on the expected syntax of the placeholder (${placeholder-name <placeholder data>})
		//String plRegex = ("\\$\\{|\\}");
		String plRegex = StringUtil.escAllChars(plPrefix) + "|" + StringUtil.escAllChars(plSuffix);
				
		//Get the input string as an ArrayList, keeping the delimiters in the array
		ArrayList<String> splitStr = new ArrayList<>(Arrays.asList(StringUtil.splitAndKeep(inputStr, plRegex)));
				
		//Get the indexes of the prefixes and suffixes
		SimpleEntry<int[], int[]> psIndexes = getPreSuffIndexes(inputStr, plRegex, plPrefix, plSuffix);
				
		//Pull out each of the embedded arrays
		int[] allPrefixes = psIndexes.getKey();
		int[] allSuffixes = psIndexes.getValue();
		
		//Check if the prefix and suffix arrays are equal in length
		if(allPrefixes.length == allSuffixes.length){
			//Create strings to hold the current and previous results
			String prevStr = "";
			StringBuilder curStr = new StringBuilder();
			
			//Loop over the list of prefixes and suffixes (both should be the same, so it doesn't matter which one gets looped over)
			for(int x = 0; x < allPrefixes.length; x++){
				//Check if the current prefix and suffix index is at least 0
				if(allPrefixes[x] >= 0 && allSuffixes[x] >= 0){
					//Loop over the split string, starting at the current index of a prefix and ending at the p current index of a suffix
					for(int y = allPrefixes[x] + 1; y < allSuffixes[x]; y++){
						//Concat the previous element and the current element in the split string
						curStr.append(splitStr.get(y));
					}
					
					//Check if the current string equals the previous string, starting from the first space char
					if(!(curStr.toString().replaceAll(plRegex, "").equals(prevStr.substring(prevStr.indexOf(" ") + 1).replaceAll(plRegex, "")))){
						//Create strings to hold the current key and value
						String curKey;
						String curVal = "";

						//Check if the current string contains a space (indicates the existence of a keypair)
						if(curStr.toString().contains(" ")){
							//Get the current key from current string, starting from the beginning and going to the index of the first space char
							curKey = curStr.substring(0, curStr.indexOf(" "));
							
							//Get the current value from the current string starting at the next char after the index of the first space char
							curVal = curStr.substring(curStr.indexOf(" ") + 1);
							
							//Check if the value string starts with the placeholder and a letter (indicates the presence of nested placeholders)
							if(curVal.substring(0, plPrefix.length() + 1).matches("^" + StringUtil.escAllChars(plPrefix) + "[A-Za-z]" + "$")){
								//Count the number of regex matches in the string
								int prefixCount = StringUtil.regexIndexes(curVal, StringUtil.escAllChars(plPrefix) + "[A-Za-z]{1}").length;

								//Add the suffix onto the end of the value however many times the prefix was found (balances everything out)
								curVal += StringUtil.cloneStr(plSuffix, prefixCount);
							}
						}
						else {
							//Set the key to be the entirety of the current string instead
							curKey = curStr.toString();
						}
						
						//Add the keypair to the placeholder data LinkedHashMap
						placeholderData.put(curKey, curVal);
					}

					//Replace the previous result with the current result
					prevStr = curStr.toString();
					
					//Empty the current string in preparation for the next iteration
					curStr = new StringBuilder();
				}
			}
		}

		//Return the filled placeholder data LinkedHashMap
		return placeholderData;
	}
	
	/**
	 * Highlights all of the placeholders that have
	 * been found in a given {@code String}, with the
	 * prefixes being highlighted in green and the 
	 * suffixes being highlighted in red. This method
	 * is mostly for debugging purposes
	 *
	 * @param inputStr The {@code String} to highlight the prefixes and suffixes in
	 * @param plPrefix The prefix to highlight
	 * @param plSuffix The suffix to highlight
	 * @return <b>@code String</b> The input containing the highlighted prefixes and suffixes
	 */
	public static String highPlaceholders(final String inputStr, final String plPrefix, final String plSuffix){
		//Set the pattern to use on the string based on the expected syntax of the placeholder (${placeholder-name <placeholder data>})
		//String plRegex = ("\\$\\{|\\}");
		String plRegex = StringUtil.escAllChars(plPrefix) + "|" + StringUtil.escAllChars(plSuffix);
		
		//Get the input string as an ArrayList, keeping the delimiters in the array
		ArrayList<String> splitStr = new ArrayList<>(Arrays.asList(StringUtil.splitAndKeep(inputStr, plRegex)));
		
		//Get the indexes of the prefixes and suffixes
		SimpleEntry<int[], int[]> psIndexes = getPreSuffIndexes(inputStr, plRegex, plPrefix, plSuffix);
		
		//Pull out each of the embedded arrays
		int[] allPrefixes = psIndexes.getKey();
		int[] allSuffixes = psIndexes.getValue();

		//Loop over the list of prefixes
		for(int allPrefix : allPrefixes){
			//Check if the current index is at least 0
			if(allPrefix >= 0){
				//Color the current prefix green
				splitStr.set(allPrefix, AnsiColor.get24Color("#0F0") + splitStr.get(allPrefix) + NColor.RESET);
			}
		}
		
		//Loop over the list of suffixes
		for(int allSuffix : allSuffixes){
			//Check if the current index is at least 0
			if(allSuffix >= 0){
				//Color the current suffix red
				splitStr.set(allSuffix, AnsiColor.get24Color("#F00") + splitStr.get(allSuffix) + NColor.RESET);
			}
		}

		//Create a StringBuilder for later
		StringBuilder recoloredStr = new StringBuilder();
		
		//Loop through the ArrayList
		for(String arrayElem : splitStr){
			//Add on the current element
			recoloredStr.append(arrayElem);
		}
		
		//Return the StringBuilder as a string
		return recoloredStr.toString();
	}
	
	/**
	 * Fetches a list of all of the indexes of 
	 * the placeholder prefixes and suffixes in 
	 * a given {@code String}.
	 *
	 * @param inputStr The {@code String} to highlight the prefixes and suffixes in
	 * @param plRegex The regex to use when finding the placeholders
	 * @param plPrefix The prefix to highlight
	 * @param plSuffix The suffix to highlight
	 * @return <b>SimpleEntry&lt;int[], int[]&gt;</b> A {@code SimpleEntry} containing an array of
	 *      the prefix indexes in the key and an array of the suffix indexes in the value
	 */
	private static SimpleEntry<int[], int[]> getPreSuffIndexes(final String inputStr, final String plRegex, final String plPrefix, final String plSuffix){
		//Get the input string as an ArrayList, keeping the delimiters in the array
		ArrayList<String> splitStr = new ArrayList<>(Arrays.asList(StringUtil.splitAndKeep(inputStr, plRegex)));
				
		//Get the indexes of all prefixes and suffixes as ArrayLists	
		ArrayList<Integer> allPrefixes = Arrays.stream(ArrayUtil.allIndexesOf(splitStr, StringUtil.escAllChars(plPrefix), true))
				.boxed().collect(Collectors.toCollection(ArrayList::new));
		ArrayList<Integer> allSuffixes = Arrays.stream(ArrayUtil.allIndexesOf(splitStr, StringUtil.escAllChars(plSuffix), true))
				.boxed().collect(Collectors.toCollection(ArrayList::new));
		
		//Check if any number mismatches should be corrected and that there are inconsistencies to begin with
		if(allPrefixes.size() != allSuffixes.size()){
			//Create an integer to hold the balance status
			int balVar = 0;
			
			//Loop over the split string array
			for(int i = 0; i < splitStr.size(); i++){
				//Check if the current element is a prefix
				if(splitStr.get(i).equalsIgnoreCase(plPrefix)){
					//Increment the balance variable
					balVar++;
				}
				
				//Check if the current element is a suffix
				if(splitStr.get(i).equalsIgnoreCase(plSuffix)){
					//Decrement the balance variable
					balVar--;
				}
				
				//Loop while the balance indicates a prefix skew, the current element is another prefix,
				// and the element two places ago is not another prefix (follows pattern of a placeholder)
				while(balVar > 1 && splitStr.get(i).equalsIgnoreCase(plPrefix) && !(splitStr.get(i - 2).equalsIgnoreCase(plPrefix))){
					//Remove the second prefix
					allPrefixes.remove(1);
					
					//Balance everything out
					balVar--;
				}
				
				//Loop while the balance indicates a suffix skew and the current element is another suffix (follows pattern of a placeholder)
				while(balVar < 0 && splitStr.get(i).equalsIgnoreCase(plSuffix)){
					//Remove the second-to-last suffix
					allSuffixes.remove(allSuffixes.size() - 2);
					
					//Balance everything out
					balVar++;
				}	
			}
		}
			
		//Sort the prefix and suffix ArrayLists in ascending order
		Collections.sort(allPrefixes);
		Collections.sort(allSuffixes);
		
		//Create a SimpleEntry, populate it, and return it
		return new SimpleEntry<>(allPrefixes.stream().filter(Objects::nonNull).mapToInt(Integer::intValue).toArray(),
				allSuffixes.stream().filter(Objects::nonNull).mapToInt(Integer::intValue).toArray());
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
