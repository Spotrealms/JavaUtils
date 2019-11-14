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

package com.spotrealms.javautils.config;

//Import first-party classes
import com.spotrealms.javautils.StringUtil;
import com.spotrealms.javautils.TypeValidation;
import com.spotrealms.javautils.TypeValidation.AllValidationTypes;
import com.spotrealms.javautils.TypeValidation.ArbitraryType;
import com.spotrealms.javautils.io.DirectoryUtil;
import com.spotrealms.javautils.io.FileUtil;
import com.spotrealms.javautils.io.Resource;

//Import Java classes and dependencies
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

public abstract class PropConfig extends Config {
	//Set class variables
	private String intConfPath;
	private String extConfPath;
	private String intConfName;
	private String extConfName;
	private Properties defProps;
	private Properties usrProps;
	
	//Class constructor
	public PropConfig(PropConfigFactory configFactory){
		//Pull out the variables from the config
		this.intConfPath = configFactory.getIntConfPath();
		this.extConfPath = configFactory.getExtConfPath();
		this.intConfName = configFactory.getConfName();
		this.extConfName = configFactory.getConfName();
		
		//Start to load the config
		try {
			//Create a boolean to select whether or not to load from the external config
			boolean loadExt = true;
			
			//Check if the external config path exists and get the result as a boolean (used only for determining what to say)
			boolean preConfPathExists = (DirectoryUtil.dirExists(extConfPath));
			
			//Check if the initialize path operation was successful
			if(super.initPath(extConfPath)){
				//Check if the directory did not exist at first, but exists now
				if(!(preConfPathExists)){
					//Announce that the directory creation operation was successful
					notifyConfDirCreated(extConfPath);
				}
				
				//Check if the external config exists and get the result as a boolean (used only for determining what to say)
				boolean preConfExists = (FileUtil.fileExists(extConfPath + extConfName));
				
				//Copy the default config and check if the operation was successful
				if(super.initConf(intConfPath, extConfPath, intConfName, extConfName)){
					//Check if the external config did not exist at first, but exists now
					if(!(preConfExists)){
						//Announce that the creation of the config was successful
						notifyConfCopy(extConfName);
					}
				}
				else {
					//Set the load external boolean to be false
					loadExt = false;
				}
			}
			else {
				//Announce that there was an error creating the directory
				errorPathCreationFail(extConfPath);
				
				//Set the load external boolean to be false
				loadExt = false;
			}
			
			//Check if the config should be loaded externally
			if(!(loadExt)){
				//Warn that the internal config will be loaded instead
				warnLoadIntConf();
			}
			
			//Load the config
			loadConfig(intConfPath, extConfPath, intConfName, extConfName, loadExt);
		}
		catch(IOException e){
			//Announce that an error occurred while copying the default config
			errorConfCopy(extConfName, e);
		}
		catch(Exception e){
			//Error out and print the stack trace
			errorResolvePath(e);
		}
	}
	
	//--- INITIALIZATION ROUTINES ---
	@Override
	protected void loadConfig(String intConfPath, String extConfPath, String intConfName, String extConfName, boolean isExternal){
		//Begin load the config using Java's Properties object
		try {
			//Create the Properties object for the default config
			defProps = new Properties();
					
			//Load the contents of the default config file into its corresponding object
			defProps.load(Resource.getResourceAsStream(this.getClass(), (intConfPath + intConfName)));
			
			//Check if the external config should be loaded too
			if(isExternal){
				//Create the Properties object for the user's config
				usrProps = new Properties();
				
				//Load the contents of the user config file into its corresponding object
				usrProps.load(FileUtil.loadFileAsFileStream(extConfPath + extConfName));
			}
			else {
				//Announce that the internal config will be loaded
				notifyUsingIntConf();
				
				//Set the user Properties object to reference the internal config instead
				usrProps = defProps;
			}
					
			//Announce that the load was successful
			notifyConfLoaded(extConfName);
				
			//Fix empty keys and/or values
			fixMissing();
		}
		//Interference with loading (catch-all)
		catch(Exception e){
			//Announce that an error occurred
			 errorLoadFailed(extConfPath, extConfName, e);
		}
	}	
	@Override
	protected void fixMissing(){
		//Loop through the program provided config (default)
		for(String curDefKey : defProps.stringPropertyNames()){
			//Get the current value
			String curDefVal = defProps.getProperty(curDefKey);
			
			//Attempt to get the same key from the user provided properties object
			if(usrProps.containsKey(curDefKey)){
				//Check if the current value isn't null
				if(StringUtil.isNull(usrProps.getProperty(curDefKey))){
					//Assign the default value to the corresponding key in the properties object
					usrProps.setProperty(curDefKey, String.valueOf(curDefVal));
				
					//Announce that a default replacement occurred
					notifyDefReplaceNull(curDefKey, curDefVal, extConfName);
				}
			}
			else {
				//Append the default key and value onto the user properties object
				usrProps.setProperty(curDefKey, curDefVal);
				
				//Announce that a default replacement occurred
				notifyDefReplaceMissing(curDefKey, extConfName);
			}
		}
	}
	
	
	//--- CONFIGURATION KEY/VALUE GETTERS ---
	@Override
	public boolean getBool(String confKey){
		//Return a boolean based on the output of the validator
		return Boolean.valueOf(validateArbitraryValue(confKey, AllValidationTypes.BOOLEAN).toString());
	}
	@Override
	public byte getByte(String confKey){
		//Return a byte based on the output of the validator
		return Byte.valueOf(validateArbitraryValue(confKey, AllValidationTypes.BYTE).toString());
	}
	@Override
	public char getChar(String confKey){
		//Return a character based on the output of the validator
		return Character.valueOf((char) validateArbitraryValue(confKey, AllValidationTypes.CHAR));
	}
	@Override
	public double getDouble(String confKey){
		//Return a double based on the output of the validator
		return Float.valueOf(validateArbitraryValue(confKey, AllValidationTypes.DOUBLE).toString());
	}
	@Override
	public float getFloat(String confKey){
		//Return a float based on the output of the validator
		return Float.valueOf(validateArbitraryValue(confKey, AllValidationTypes.FLOAT).toString());
	}
	@Override
	public int getInt(String confKey){
		//Return a integer based on the output of the validator
		return Integer.valueOf(validateArbitraryValue(confKey, AllValidationTypes.INT).toString());
	}
	@Override
	public long getLong(String confKey){
		//Return a long based on the output of the validator
		return Long.valueOf(validateArbitraryValue(confKey, AllValidationTypes.LONG).toString());
	}
	@Override
	public short getShort(String confKey){
		//Return a short based on the output of the validator
		return Short.valueOf(validateArbitraryValue(confKey, AllValidationTypes.SHORT).toString());
	}
	@Override
	public String getString(String confKey){
		//Return a string based on the output of the validator
		return (validateArbitraryValue(confKey, AllValidationTypes.CUSTOMTYPE, new VString()).toString());
	}
	@Override
	public <T> String[] getKeysForValue(T confVal){
		//Create an ArrayList to hold the found keys
		ArrayList<String> foundKeys = new ArrayList<>();
		
		//Loop over the keys for the user's config
		for(final String curKey : usrProps.stringPropertyNames()){
			//Check if the current key has the value provided
			if(usrProps.getProperty(curKey).equals(confVal)){
				//Add this key to the found keys ArrayList
				foundKeys.add(curKey);
			}
		}
		
		//Return the filled ArrayList as an array of strings
		return (foundKeys.toArray(new String[0]));
	}
	
	//Utilities for the value getters
	private Object validateArbitraryValue(String confKey, AllValidationTypes checkType, ArbitraryType customType){
		//Attempt to get the corresponding value from the user provided config
		if(getVTypeData((usrProps.get(confKey).toString()), checkType, customType).getKey()){
			//Return the value from the user config
			return (usrProps.get(confKey).toString());
		}
				
		//Attempt to get the same value from the default config instead
		else if(getVTypeData((defProps.get(confKey).toString()), checkType, customType).getKey()){
			//Return the value from the default config and warn the user
			warnUVBadTypeDefInstead(confKey, (checkType.toString()));
			return (defProps.get(confKey).toString());
		}
				
		else {
			//Return the default value for the given data type and warn the user
			errorUVBadType(confKey, (checkType.toString()), (TypeValidation.assumeType(defProps.get(confKey).toString()).getClass().getSimpleName()));
			return (getVTypeData("", checkType, customType).getValue());
		}
	}
	private Object validateArbitraryValue(String confKey, AllValidationTypes checkType){
		//Call the overloaded method, with the custom type being null and return the result
		return (validateArbitraryValue(confKey, checkType, null));
	}
	private SimpleEntry<Boolean, Object> getVTypeData(String valIn, AllValidationTypes checkType, ArbitraryType customType){
		//Create a boolean to hold the result of the type validation operation
		boolean typeValid = false;
				
		//Create an Object to hold the default value for the given type
		Object defaultVal = null;
				
		//Check if the given type is a custom type (indicates a string)
		if(checkType == AllValidationTypes.CUSTOMTYPE){
			//Validate the input string and pass the result to the boolean, using the VString class as the validator
			typeValid = checkType.validate(valIn, customType);
					
			//Get the default value for the input data type
			defaultVal = customType.getDefaultValue();
		}
		else {
			//Validate the input string and pass the result to the boolean
			typeValid = checkType.validate(valIn, null);
					
			//Get the default value for the input data type
			defaultVal = checkType.defaultValue(null);
		}
		
		//Construct and return a SimpleEntry containing the results of the type validator and the default value for the given type
		return (new SimpleEntry<Boolean, Object>(typeValid, defaultVal));
	}
	private static class VString implements ArbitraryType {
		@Override
		public boolean strIsValid(String strIn){
			//Check if a string is null and return the result as a boolean
			return (!(StringUtil.isNull(strIn)));
		}

		@Override
		public Object getDefaultValue(){
			//Return the default value for for a string (by default, it's a blank string)
			return null;
		}
	}
	
	
	//---CONFIGURATION KEY/VALUE SETTERS
	@Override
	public <T> void setKV(String keyName, T keyValue){
		//Create a new key and set its contents in the user's config
		usrProps.setProperty(keyName, keyValue.toString());
	}
	@Override
	public void setKV(String keyName){
		//Redirect to the key/value creator method, but set the key's value to a blank string
		setKV(keyName, "");
	}
		
	
	//--- CONFIGURATION MANAGMENT ROUTINES ---
	@Override
	public void getChanges(String confPath, String confName) throws IOException {
		//Start to load the current state of the user config
		try {
			//Create a Properties object to represent the user's config file
			Properties usrPropsFile = new Properties();
					
			//Load the contents of the file into the object
			usrPropsFile.load(FileUtil.loadFileAsFileStream(confPath + confName));
	
			//Check if the file representation and the class representation of the user's config match
			if(!(usrConfMatchesFS(confPath, confName))){
				//Save the contents of the grabbed Properties object to the class' properties object
				usrProps = usrPropsFile;
			}
			else {
				//Objects match, so don't do anything, but announce that they match
				notifySaveMatch(confName, confPath);
			}
		} 
		catch(IOException e){
			//Announce that an error occurred
			errorLoadFailed(confName, confPath, e);
		}
	}
	@Override
	public void writeChanges(String confPath, String confName){
		//Start to load the current state of the user config
		try {
			//Create a Properties object to represent the user's config file
			Properties usrPropsFile = new Properties();
			
			//Load the contents of the file into the object
			usrPropsFile.load(FileUtil.loadFileAsFileStream(confPath + confName));
			
			//Check if the file representation and the class representation of the user's config match
			if(!(usrConfMatchesFS(confPath, confName))){
				//Save the contents of the Properties object to the file that was grabbed earlier using store()
				usrProps.store((new FileOutputStream(confPath + confName)), null);
				
				//Announce the successful save
				notifySaved(confName, confPath);
			}
			else {
				//Objects match, so don't do anything, but announce that they match
				notifySaveMatch(confName, confPath);
			}
		} 
		catch(IOException e){
			//Announce that an error occurred
			errorSaveFailed(confName, confPath, e);
		}
	}
	@Override
	public boolean usrConfMatchesFS(String confPath, String confName) throws IOException {
		//Start to load the current state of the user config
		try {
			//Create a Properties object to represent the user's config file
			Properties usrPropsFile = new Properties();
		
			//Load the contents of the file into the object
			usrPropsFile.load(FileUtil.loadFileAsFileStream(confPath + confName));
		
			//Check if the file representation and the class representation of the user's config match and return the result as a boolean
			return (usrProps.equals(usrPropsFile));
		}
		catch(IOException e){
			//Announce that an error occurred
			errorLoadFailed(extConfName, extConfPath, e);
			
			//Return false, because an error occurred
			return false;
		}
	}
	
	
	//--- KEY/VALUE PRESCENCE CHECKS ---
	@Override
	public boolean keyExists(String keyIn, boolean useExternal){
		//Check if the key exists in the default config object and get the result as a boolean
		boolean existsDef = (defProps.containsKey(keyIn));
		
		//Check if the key exists in the user config object and get the result as a boolean
		boolean existsUsr = (usrProps.containsKey(keyIn));
		
		//Decide which boolean to output
		if(useExternal){
			//Output the status for the user's config
			return existsUsr;
		}
		else {
			//Output the status for the default config
			return existsDef;
		}
	}
	@Override
	public boolean keyHasValue(String keyIn, boolean useExternal){
		//Check if the key in the default config object has a value and get the result as a boolean
		boolean valueExistsDef = (defProps.get(keyIn) != null);
		
		//Check if the key in the user's config object has a value and get the result as a boolean
		boolean valueExistsUsr = (usrProps.get(keyIn) != null);
		
		//Decide which boolean to output
		if(useExternal){
			//Output the status for the user's config
			return valueExistsUsr;
		}
		else {
			//Output the status for the default config
			return valueExistsDef;
		}
	}
	@Override
	public <T> boolean valueExists(T valueIn, boolean useExternal){
		//Check if the value exists in the default config object and get the result as a boolean
		boolean existsDef = (defProps.containsValue(valueIn));
		
		//Check if the value exists in the user config object and get the result as a boolean
		boolean existsUsr = (usrProps.containsValue(valueIn));
		
		//Decide which boolean to output
		if(useExternal){
			//Output the status for the user's config
			return existsUsr;
		}
		else {
			//Output the status for the default config
			return existsDef;
		}
	}
	
	
	//Properties getters and setters
	public Properties getDefConf(){
		//Return the user's properties object
		return defProps;
	}
	public Properties getUsrConf(){
		//Return the user's properties object
		return usrProps;
	}
	public void setUsrConf(Properties usrProps){
		//Replace the user's config object with the provided one
		this.usrProps = usrProps;
	}
	
	
	//HashMap getters and setters
	public HashMap<String, Object> getDefMap(){
		//Return the result of filling a HashMap with the default Properties object's data
		return (propToHMap(defProps));
	}
	public HashMap<String, Object> getUsrMap(){
		//Return the result of filling a HashMap with the user Properties object's data
		return (propToHMap(usrProps));
	}
	public void setUsrMap(HashMap<String, Object> usrMap){
		//Set the user Properties object's data with the contents of a HashMap
		this.usrProps = (hMapToProp(usrMap));
	}
	
	//Private utilities specific to Properties configs
	private static HashMap<String, Object> propToHMap(Properties propIn){
		//Create a new HashMap object
		HashMap<String, Object> hMapOut = new HashMap<>();
		
		//Loop over the passed Properties object
		for(final String curKey : propIn.stringPropertyNames()){
			//Get the value for the current key and insert both into the HashMap
			hMapOut.put(curKey, propIn.getProperty(curKey));
		}
		
		//Return the filled HashMap
		return hMapOut;
	}
	private static Properties hMapToProp(HashMap<String, Object> mapIn){
		//Create a new Properties object
		Properties propOut = new Properties();
		
		//Loop over the passed HashMap object
		for(final Entry<?, ?> curEntry : mapIn.entrySet()){
			//Get the current entry's keypair and insert it into the Properties object
			propOut.put((curEntry.getKey()), (curEntry.getValue()));
		}
		
		//Return the filled Properties Object
		return propOut;
	}
	
	//Message callback methods
	protected abstract void warnLoadIntConf();
	protected abstract void errorResolvePath(Throwable errorCause);
	protected abstract void notifyConfDirCreated(String pathCreated);
	protected abstract void errorPathCreationFail(String pathCreated);
	protected abstract void notifyConfCopy(String confName);
	protected abstract void errorConfCopy(String confName, Throwable errorCause);
	protected abstract void notifyUsingIntConf();
	protected abstract void notifyConfLoaded(String confName);
	protected abstract void notifyDefReplaceNull(String defKey, String defVal, String confName);
	protected abstract void notifyDefReplaceMissing(String defKey, String confName);
	protected abstract void warnUVBadTypeDefInstead(String confKey, String checkType);
	protected abstract void errorUVBadType(String confKey, String checkType, String assumedType);
	protected abstract void notifySaved(String confPath, String confName);
	protected abstract void notifySaveMatch(String confPath, String confName);
	protected abstract void errorLoadFailed(String confPath, String confName, Throwable errorCause);
	protected abstract void errorSaveFailed(String confPath, String confName, Throwable errorCause);
}