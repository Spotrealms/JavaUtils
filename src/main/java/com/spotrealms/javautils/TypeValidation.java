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
import com.spotrealms.javautils.math.MathUtil;

//Import Java classes and dependencies
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A series of methods for ensuring that a 
 * {@code String} validates to a specific 
 * data type. <br>The following data type
 * validation methods are available:
 * <ul>
 *  <li>{@code boolean} validation</li>
 *  <li>{@code byte} validation</li>
 *  <li>{@code char} validation</li>
 *  <li>{@code double} validation</li>
 *  <li>{@code float} validation</li>
 *  <li>{@code int} validation</li>
 *  <li>{@code long} validation</li>
 *  <li>{@code short} validation</li>
 *  <li>{@code JSON} validation</li>
 * </ul>
 * Additionally, an {@code enum} containing all of the
 * available types that can be checked is also
 * available, which is useful for determining 
 * which of these methods to use when validating
 * a {@code String} that is expected to be of an 
 * arbitrary type. This arbitrary type validation
 * is available in this class in the method
 * {@code strValidates}. Data type assumption is also
 * available via the previously defined data type 
 * validation methods.
 * @author Spotrealms
 */
public class TypeValidation {
	/**
	 * Provides a list of all data types that can
	 * be validated using methods from the validation
	 * class. Useful when determining what a {@code String}
	 * should be expected to validate to. Additionally, the
	 * value {@code CUSTOMTYPE} is available, which allows 
	 * the programmer to specify a custom validation method
	 * to execute in that case (this is done via a functional
	 * interface, which requires Java SE &gt;= 1.8).
	 * @see arbitraryType
	 */
	public static enum allValidationTypes {
		/**
		 * Represents a boolean
		 * @see allValidationTypes
		 */
		BOOLEAN {
			/**
			 * {@inheritDoc}
			 * @see allValidationTypes
			 */
			@Override
			public boolean validate(String targetStr, arbitraryType customType){
				//Return the result of the corresponding validation method
				return isBool(targetStr);
			}
		},

		/**
		 * Represents a byte
		 * @see allValidationTypes
		 */
		BYTE {
			/**
			 * {@inheritDoc}
			 * @see allValidationTypes
			 */
			@Override
			public boolean validate(String targetStr, arbitraryType customType){
				//Return the result of the corresponding validation method
				return isByte(targetStr);
			}
		},

		/**
		 * Represents a character
		 * @see allValidationTypes
		 */
		CHAR {
			/**
			 * {@inheritDoc}
			 * @see allValidationTypes
			 */
			@Override
			public boolean validate(String targetStr, arbitraryType customType){
				//Return the result of the corresponding validation method
				return isChar(targetStr);
			}
		},

		/**
		 * Represents a double
		 * @see allValidationTypes
		 */
		DOUBLE {
			/**
			 * {@inheritDoc}
			 * @see allValidationTypes
			 */
			@Override
			public boolean validate(String targetStr, arbitraryType customType){
				//Return the result of the corresponding validation method
				return isDouble(targetStr);
			}
		},

		/**
		 * Represents a float
		 * @see allValidationTypes
		 */
		FLOAT {
			/**
			 * {@inheritDoc}
			 * @see allValidationTypes
			 */
			@Override
			public boolean validate(String targetStr, arbitraryType customType){
				//Return the result of the corresponding validation method
				return isFloat(targetStr);
			}
		},

		/**
		 * Represents an integer
		 * @see allValidationTypes
		 */
		INT {
			/**
			 * {@inheritDoc}
			 * @see allValidationTypes
			 */
			@Override
			public boolean validate(String targetStr, arbitraryType customType){
				//Return the result of the corresponding validation method
				return isInt(targetStr);
			}
		},

		/**
		 * Represents a long integer
		 * @see allValidationTypes
		 */
		LONG {
			/**
			 * {@inheritDoc}
			 * @see allValidationTypes
			 */
			@Override
			public boolean validate(String targetStr, arbitraryType customType){
				//Return the result of the corresponding validation method
				return isLong(targetStr);
			}
		},

		/**
		 * Represents a short integer
		 * @see allValidationTypes
		 */
		SHORT {
			/**
			 * {@inheritDoc}
			 * @see allValidationTypes
			 */
			@Override
			public boolean validate(String targetStr, arbitraryType customType){
				//Return the result of the corresponding validation method
				return isShort(targetStr);
			}
		},

		/**
		 * Represents a JSON object
		 * @see allValidationTypes
		 */
		JSON {
			/**
			 * {@inheritDoc}
			 * @see allValidationTypes
			 */
			@Override
			public boolean validate(String targetStr, arbitraryType customType){
				//Return the result of the corresponding validation method
				return isValidJSON(targetStr);
			}
		},

		/**
		 * Represents a custom object type that can 
		 * be specified by the programmer
		 * @see arbitraryType
		 * @see allValidationTypes
		 */
		CUSTOMTYPE {
			/**
			 * {@inheritDoc}
			 * @see allValidationTypes
			 */
			@Override
			public boolean validate(String targetStr, arbitraryType customType){
				//Check if the interface exists (was passed)
				if(customType != null){
					//Return either true or false depending on the output of the validation method
					return customType.strIsValid(targetStr);
				}
				else {
					//Return false because the interface to check the validity was not specified or not passed
					return false;
				}
			}
		};

		/**
		 * Defines a validation method that is
		 * associated with a specific data type.
		 * The {@code customType} parameter is used
		 * to pass custom validation methods, but will
		 * only be used when running {@code CUSTOMTYPE.validate}.
		 * Due a limitation in Java regarding the 
		 * overloading of abstract methods, this parameter 
		 * will be wasted in every other case.
		 * @param targetStr The input {@code String} to check
		 * @param customType A custom validation method specified by the user
		 * @return <b>boolean</b> The status of whether or not the input {@code String} is valid for that type
		 * @see arbitraryType
		 */
		public abstract boolean validate(String targetStr, arbitraryType customType);
	}
	
	/**
	 * Defines a list of all possible floating-point data
	 * types that can be validated using the 
	 * {@code strValidates} method
	 * @see strValidates
	 */
	public static final ArrayList<allValidationTypes> allFloatTypes = new ArrayList<>(Arrays.asList(allValidationTypes.DOUBLE, allValidationTypes.FLOAT));
	
	/**
	 * Defines a list of all possible integer data types
	 * that can be validated using the {@code strValidates} 
	 * method
	 * @see strValidates
	 */
	public static final ArrayList<allValidationTypes> allIntegerTypes = new ArrayList<>(Arrays.asList(allValidationTypes.BYTE, allValidationTypes.INT, allValidationTypes.LONG, allValidationTypes.SHORT));
	
	/**
	 * Check whether or not a {@code String} validates 
	 * to a {@code boolean}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code boolean}
	 */
	public static boolean isBool(String strIn){
		//Check if the string equals true or false (booleans are case sensitive, and MUST be all lower-case)
		if(strIn.equals("true") || strIn.equals("false")){
			//The string is a valid boolean, so return true
			return true;
		}
		else {
			//The string isn't valid, so return false
			return false;
		}
	}
	
	/**
	 * Check whether or not a {@code String} validates 
	 * to a {@code byte}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code byte}
	 */
	public static boolean isByte(String strIn){
		try {
			//Convert the string to an integer
			int testInt = Integer.decode(strIn);
		
			//Try to parse the string as a byte and check if it's in range, then return the result
			return MathUtil.numberInRange(testInt, Byte.MIN_VALUE, Byte.MAX_VALUE);
		}
		catch(Exception e){
			//Return false, as there was an issue parsing the string as an integer
			return false;
		}
	}
	
	/**
	 * Check whether or not a {@code String} validates 
	 * to a {@code char}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code char}
	 */
	public static boolean isChar(String strIn){
		//Check if the string validates to an integer
		if(isInt(strIn)){
			//Try to parse the string as an integer and check if it's in range given it's a char value, then return the result
			return MathUtil.numberInRange(Integer.parseInt(strIn), Character.MIN_VALUE, Character.MAX_VALUE);
		}
		else {	
			//Test if the string is of length 1
			if(strIn.length() == 1){
				//Return true because the string contains one and only one character, therefore it can be casted as a character
				return true;
			}
			else {
				//Return false because the string can't be casted to a character, as its length is not equal to that of a single character
				return false;
			}
		}
	}
	
	/**
	 * Check whether or not a {@code String} validates 
	 * to a {@code double}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code double}
	 */
	public static boolean isDouble(String strIn){
		try {
			//Try to parse the string as a double and check if it's in range, then return the result
			return MathUtil.numberInRange(Double.parseDouble(strIn), Double.MIN_VALUE, Double.MAX_VALUE);
		}
		catch(NumberFormatException invalidDouble){
			//The double isn't valid, so return false
			return false;
		}
	}
	
	/**
	 * Check whether or not a {@code String} validates 
	 * to a {@code float}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code float}
	 */
	public static boolean isFloat(String strIn){
		try {
			//Try to parse the string as a float and check if it's in range, then return the result
			return MathUtil.numberInRange(Float.parseFloat(strIn), Float.MIN_VALUE, Float.MAX_VALUE);
		}
		catch(NumberFormatException invalidFloat){
			//The float isn't valid, so return false
			return false;
		}
	}
	
	/**
	 * Check whether or not a {@code String} validates 
	 * to an {@code int}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code int}
	 */
	public static boolean isInt(String strIn){
		try {
			//Try to parse the string as an integer and check if it's in range, then return the result
			return MathUtil.numberInRange(Integer.parseInt(strIn), Integer.MIN_VALUE, Integer.MAX_VALUE);
		}
		catch(NumberFormatException invalidInt){
			//The integer isn't valid, so return false
			return false;
		}
	}
	
	/**
	 * Check whether or not a {@code String} validates 
	 * to a {@code long}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code long}
	 */
	public static boolean isLong(String strIn){
		try {
			//Try to parse the string as a long and check if it's in range, then return the result
			return MathUtil.numberInRange(Long.parseLong(strIn), Long.MIN_VALUE, Long.MAX_VALUE);
		}
		catch(NumberFormatException invalidLong){
			//The long isn't valid, so return false
			return false;
		}
	}
	
	/**
	 * Check whether or not a {@code String} validates 
	 * to a {@code short}
	 * @param strIn The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is a valid {@code short}
	 */
	public static boolean isShort(String strIn){
		try {
			//Try to parse the string as a short and check if it's in range, then return the result
			return MathUtil.numberInRange(Short.parseShort(strIn), Short.MIN_VALUE, Short.MAX_VALUE);
		}
		catch(NumberFormatException invalidShort){
			//The short isn't valid, so return false
			return false;
		}
	}
	

	/**
	 * Check whether or not a {@code String} validates 
	 * to {@code JSON}
	 * @param testJSON The input {@code String} to check
	 * @return <b>boolean</b> The status of whether or not the input {@code String} is valid {@code JSON}
	 * @see <a href="https://stackoverflow.com/questions/10174898/how-to-check-whether-a-given-string-is-valid-json-in-java">https://stackoverflow.com/questions/10174898/how-to-check-whether-a-given-string-is-valid-json-in-java</a>
	 */
	public static boolean isValidJSON(String testJSON){
		try {
			//Make a new JSON object from the string
			new JSONObject(testJSON);
		} 
		catch(JSONException ex){
			//Try to create a JSON array instead
			try {
				//Make a new JSONArray object instead
				new JSONArray(testJSON);
			} 
			catch(JSONException ex1){
				//Return false because the string isn't valid JSON
				return false;
			}
		}
		
		//Return true because the string is valid JSON
		return true;
	}
	
	/**
	 * Defines a method for testing if a {@code String} 
	 * validates to an arbitrary type (types that aren't 
	 * defined in any methods in this class). This option
	 * can be selected by passing {@code allValidationTypes.CUSTOMTYPE}
	 * to the {@code strValidates} method in the {@code expectedType}
	 * parameter. <br>
	 * After creating a class that implements this interface, 
	 * the defined validation method can be passed to the
	 * {@code strValidates} method in the {@code customValidator}
	 * parameter. <br>
	 * Example of creating a class that implements 
	 * this interface:
	 * <pre>
	 * public static class testValidator implements arbitraryType {
	 *  &#64;Override
     *  public boolean strIsValid(String strIn){
	 *   //Do stuff
	 *   return true;
	 *  }
	 * }
	 * </pre>
	 * The custom class then can be used when calling the 
	 * method like the following:
	 * <pre>
	 * TypeValidation.strValidates("test", expectedType.CUSTOMTYPE, new testValidator());
	 * </pre>
	 * NOTE: The class <b>MUST</b> be static in order to
	 * avoid having to instantiate it (instantiation is 
	 * not needed for this class and method, as it simply
	 * only exists to validate input {@code Strings}, and
	 * requires no instance variables to operate.
	 * @see allValidationTypes
	 */
	public interface arbitraryType {
		/**
		 * Check whether or not a {@code String} validates 
		 * to an object of an arbitrary type
		 * @param strIn The input {@code String} to check
		 * @return <b>boolean</b> The status of whether or not the input {@code String} validates
		 */
		public boolean strIsValid(String strIn);
	}
	
	/**
	 * Check if a {@code String} validates to an
	 * arbitrary type based on an expected type.
	 * Also allows the programmer to specify their
	 * own validation method via the {@code customType}
	 * parameter. Set one of the entries in the 
	 * {@code expectedTypes} {@code ArrayList} to be
	 * {@code CUSTOMTYPE} to use this custom method.
	 * @param targetStr The input {@code String} to check
	 * @param expectedTypes An {@code ArrayList} containing the type(s) the input {@code String} is expected to be
	 * @param customTypes An {@code ArrayList} containing custom validation method(s) specified by the user
	 * @return <b>boolean</b> The status of whether or not the input {@code String} validates
	 */
	public static boolean strValidates(String targetStr, ArrayList<allValidationTypes> expectedTypes, ArrayList<arbitraryType> customTypes){
		//Loop through all entries in the provided ArrayList
		for(allValidationTypes currentExpectedType : expectedTypes){
			//Check if the current type to check is the custom type
			if(currentExpectedType == allValidationTypes.CUSTOMTYPE){
				//Loop through the ArrayList containing the custom validation methods
				for(arbitraryType customType : customTypes){
					//Run the current custom validation method against the input string and get the result as a boolean
					boolean currentCustomMethodResult = currentExpectedType.validate(targetStr, customType);
					
					//Check if the result is false (indicates that the string doesn't validate to the current custom type)
					if(!currentCustomMethodResult){
						//Return false because even one failed validation test will render the string incapable of validating/casting to all of the expected types
						return false;
					}
				}
			}
			else {
				//Setup a boolean for later
				boolean currentResult;
				
				//Check if at least one custom type exists (prevents NPEs)
				if(customTypes.size() > 0){
					//Run the corresponding method for the current expected type against the input string
					currentResult = currentExpectedType.validate(targetStr, customTypes.get(0));
				}
				else {
					//Run the corresponding method for the current expected type against the input string, but don't pass a custom type, as none exist
					currentResult = currentExpectedType.validate(targetStr, null);
				}
				
				//Check if the result is false (indicates that the string doesn't validate to the current type)
				if(!currentResult){
					//Return false because even one failed validation test will render the string incapable of validating/casting to all of the expected types
					return false;
				}
			}
		}

		//Return true because none of the validation checks failed, which means that the string can be casted to any of the specified types without issue
		return true;
	}
	
	/**
	 * Check if a {@code String} validates to an
	 * arbitrary type based on an expected type.
	 * Also allows the programmer to specify their
	 * own validation method via the {@code customType}
	 * parameter. Set one of the entries in the 
	 * {@code expectedTypes} {@code ArrayList} to be
	 * {@code CUSTOMTYPE} to use this custom method.
	 * @param targetStr The input {@code String} to check
	 * @param expectedTypes An {@code ArrayList} containing the type(s) the input {@code String} is expected to be
	 * @param customType A custom validation method specified by the user
	 * @return <b>boolean</b> The status of whether or not the input {@code String} validates
	 */
	public static boolean strValidates(String targetStr, ArrayList<allValidationTypes> expectedTypes, arbitraryType customType){
		//Create a new ArrayList to hold the singular custom validation method
		ArrayList<arbitraryType> customTypes = new ArrayList<>();
		
		//Add the singular expected type to the ArrayList
		customTypes.add(customType);
		
		//Redirect to the overloaded method
		return strValidates(targetStr, expectedTypes, customTypes);
	}
	
	/**
	 * Check if a {@code String} validates to an
	 * arbitrary type based on an expected type.
	 * Also allows the programmer to specify their
	 * own validation method via the {@code customType}
	 * parameter. Set the {@code expectedType} value to
	 * be {@code CUSTOMTYPE} to use this custom method.
	 * @param targetStr The input {@code String} to check
	 * @param expectedType The type the input {@code String} is expected to be
	 * @param customTypes An {@code ArrayList} containing custom validation method(s) specified by the user
	 * @return <b>boolean</b> The status of whether or not the input {@code String} validates
	 */
	public static boolean strValidates(String targetStr, allValidationTypes expectedType, ArrayList<arbitraryType> customTypes){
		//Create a new ArrayList to hold the singular expected type passed
		ArrayList<allValidationTypes> expectedTypes = new ArrayList<>();
		
		//Add the singular expected type to the ArrayList
		expectedTypes.add(expectedType);
		
		//Redirect to the overloaded method
		return strValidates(targetStr, expectedTypes, customTypes);
	}
	
	/**
	 * Check if a {@code String} validates to an
	 * arbitrary type based on an expected type.
	 * Also allows the programmer to specify their
	 * own validation method via the {@code customType}
	 * parameter. Set the {@code expectedType} value to
	 * be {@code CUSTOMTYPE} to use this custom method.
	 * @param targetStr The input {@code String} to check
	 * @param expectedType The type the input {@code String} is expected to be
	 * @param customType A custom validation method specified by the user
	 * @return <b>boolean</b> The status of whether or not the input {@code String} validates
	 */
	public static boolean strValidates(String targetStr, allValidationTypes expectedType, arbitraryType customType){
		//Redirect to the overloaded method
		return strValidates(targetStr, expectedType, customType);
	}
	
	/**
	 * Check if a {@code String} validates to an
	 * arbitrary type based on an expected type
	 * @param targetStr The input {@code String} to check
	 * @param expectedTypes An {@code ArrayList} containing the type(s) the input {@code String} is expected to be
	 * @return <b>boolean</b> The status of whether or not the input {@code String} validates
	 */
	public static boolean strValidates(String targetStr, ArrayList<allValidationTypes> expectedTypes){
		//Redirect to the overloaded method
		return strValidates(targetStr, expectedTypes, new ArrayList<arbitraryType>());
	}
	
	/**
	 * Check if a {@code String} validates to an
	 * arbitrary type based on an expected type
	 * @param targetStr The input {@code String} to check
	 * @param expectedType The type the input {@code String} is expected to be
	 * @return <b>boolean</b> The status of whether or not the input {@code String} validates
	 */
	public static boolean strValidates(String targetStr, allValidationTypes expectedType){
		//Redirect to the overloaded method
		return strValidates(targetStr, expectedType, new ArrayList<arbitraryType>());
	}
	
	/**
	 * Attempts to assume the data type of an input
	 * {@code String} given a set of pre-determined
	 * requirements that must be met for each of the 
	 * eight primitive data types. The way this happens 
	 * is via a specific order of operations in which the 
	 * type checking starts with the most restrictive types, 
	 * then moves to more permissive ones. The output of 
	 * this method, therefore, will return an instance of 
	 * the lowest possibly sized object to which the 
	 * {@code String} can validate to. If the input 
	 * {@code String} doesn't validate to a specific 
	 * primitive type, an instance of {@code String} is 
	 * returned instead.
	 * @param strIn The {@code String} to run the type assumption on
	 * @return <b>Object</b> An instance of the object that the 
	 * input {@code String} can validate to, via {@code instanceof} 
	 * or via a call such as this: {@code assumeType(strIn).getClass().getName()}
	 */
	public static Object assumeType(String strIn){
		//Check if the string can be a boolean
		if(TypeValidation.isBool(strIn)){
			//Return a new boolean object 
			return Boolean.FALSE;
		}
		
		//Check if the string can be a long (covers whole number family)
		if(TypeValidation.isLong(strIn)){
			//Check if the string can be a byte
			if(TypeValidation.isByte(strIn)){
				//Return a new byte object
				return Byte.valueOf((byte) 0);
			}
			
			//Check if the string can be a short
			else if(TypeValidation.isShort(strIn)){
				//Return a new integer object
				return Short.valueOf((short) 0);
			}
			
			//Check if the string can be an integer
			else if(TypeValidation.isInt(strIn)){
				//Return a new integer object
				return Integer.valueOf((int) 0);
			}
			
			//Return a long object, as the smaller types in the family aren't valid given the input string
			else {
				//Return a new long object
				return Long.valueOf((long) 0);
			}
		}
		
		//Check if the string can be a double (covers real number family)
		if(TypeValidation.isDouble(strIn)){
			//Check if the string can be a float
			if(TypeValidation.isFloat(strIn)){
				//Return a new float object
				return Float.valueOf((float) 0.0);
			}
			
			//Return a double object, as the smaller types in the family aren't valid given the input string
			else {
				//Return a new double object
				return Double.valueOf((double) 0.0);
			}
		}
		
		//Check if the string can be a char
		if(TypeValidation.isChar(strIn)){
			//Return a new char object
			return Character.valueOf((char) 0);
		}
		
		//Default selection option
		else {
			//Return a new string object, as the input object doesn't match one of the eight primitive types 
			return new String();
		}
	}
}