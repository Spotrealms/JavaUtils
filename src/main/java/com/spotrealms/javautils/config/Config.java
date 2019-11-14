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

package com.spotrealms.javautils.config_indev;

//Import first-party classes
import com.spotrealms.javautils.StringUtil;
import com.spotrealms.javautils.io.DirectoryUtil;
import com.spotrealms.javautils.io.FileUtil;
import com.spotrealms.javautils.io.Resource;

//Import Java classes and dependencies
import java.io.IOException;

/**
 * An abstract class containing the 
 * barebones of a typical configuration 
 * wrapper class. See the {@code PropConfig} 
 * class in the {@code ConfigKit} project
 * for a complete example of how to implement 
 * this class in your own configuration 
 * classes.
 * @author Spotrealms
 */
public abstract class Config {
	//--- INITIALIZATION ROUTINES ---
	/**
	 * Initializes the directory for a program's 
	 * configuration. Meant to be run as the first 
	 * step in the initialization routine for a 
	 * configuration class, which will usually occur 
	 * in the class constructor.
	 * @param extConfPath The path to create or check for
	 * @return <b>boolean</b> The status as to whether or not the directory initialization routine was successful
	 */
	protected boolean initPath(String extConfPath){
		//Check if the path is blank
		if(!StringUtil.isNullOrVoid(extConfPath)){
			//Check if the configuration folder exists
			if(!(DirectoryUtil.dirExists(extConfPath))){
				//Create the configuration directory
				DirectoryUtil.createDirectory(extConfPath, false);
							
				//Check if the folder exists again (roots out file permission issues) and return the status as a boolean
				return (DirectoryUtil.dirExists(extConfPath));
			}
			else {
				//The folder already exists, so return true
				return true;
			}
		}
		else {
			//No need to do anything, as there's no directory to create, so return true
			return true;
		}
	}
	
	/**
	 * Initializes the program's configuration,
	 * which includes checking if the file exists
	 * in the configuration folder and dropping the 
	 * configuration from the program's binary if 
	 * it doesn't exist in the given folder. Meant
	 * to be run as the second step in the initialization 
	 * routine for a configuration class, which will 
	 * usually occur in the class constructor.
	 * @param intConfPath The internal path in the binary where the default configuration can be found
	 * @param extConfPath The external path to which the user's configuration will be looked for or dropped to
	 * @param intConfName The filename of the configuration as it is in the binary
	 * @param extConfName The filename of the configuration in the user's configuration folder
	 * @return <b>boolean</b> The status as to whether or not the configuration initialization routine was successful
	 * @throws IOException If the export of the default configuration fails
	 */
	protected boolean initConf(String intConfPath, String extConfPath, String intConfName, String extConfName) throws IOException {
		//Check if the configuration file exists in the configuration folder
		if(!(FileUtil.fileExists(extConfPath + extConfName))){
			//Copy the configuration file from inside the JAR/WAR file
			Resource.exportResource(this.getClass(), (intConfPath + intConfName), (extConfPath + extConfName));

			//Check if the configuration exists again (roots out issues relating to file permissions) and return the status as a boolean
			return (FileUtil.fileExists(extConfPath + extConfName));
		}
		else {				
			//Return true because the configuration already exists
			return true;
		}
	}
	
	/**
	 * Loads the user's configuration file into
	 * the desired configuration object depending
	 * on what type of configuration is being loaded.
	 * This method assumes that the configuration 
	 * folder already exists and that the configuration
	 * already exists in the specified folder. Otherwise, 
	 * this method will load the default configuration
	 * from the binary or do so from the start depending on
	 * the state of the {@code isExternal} parameter. Meant
	 * to be run as the third and final step in the 
	 * initialization routine for a configuration class,
	 * which will usually occur in the class constructor.
	 * @param intConfPath The internal path in the binary where the default configuration can be found
	 * @param extConfPath The external path to which the user's configuration will be looked for or dropped to
	 * @param intConfName The filename of the configuration as it is in the binary
	 * @param extConfName The filename of the configuration in the user's configuration folder
	 * @param isExternal Determines whether to load the configuration from inside or outside the program's binary
	 */
	protected abstract void loadConfig(String intConfPath, String extConfPath, String intConfName, String extConfName, boolean isExternal);
	
	/**
	 * Defines the steps to take if there is a missing
	 * keypair from the user's configuration that is 
	 * present in the default configuration. Meant to be 
	 * run as part of the configuration load step in the 
	 * initialization routine for a configuration class,
	 * which will usually occur in the class constructor.
	 */
	protected abstract void fixMissing();
	
	
	//--- CONFIGURATION KEY/VALUE GETTERS ---
	/**
	 * Get a configuration value given an existing
	 * key as a {@code boolean}. If the key doesn't
	 * exist in the user's configuration or it can't
	 * be casted to a {@code boolean}, it will be 
	 * fetched from the default configuration instead.
	 * If this value doesn't cast from either 
	 * configuration or it doesn't exist in either 
	 * configuration, a {@code ConfigException}
	 * will be thrown depending on whether the
	 * value is the incorrect type or it doesn't
	 * exist.
	 * @param confKey The key to get the associated value for
	 * @return <b>boolean</b> The value associated with the passed key as a {@code boolean}
	 * @throws ConfigException If the value from both the default and user configurations can't be casted to a {@code boolean} or aren't present in the first place
	 */
	public abstract boolean getBool(String confKey) throws ConfigException;
	
	/**
	 * Get a configuration value given an existing
	 * key as a {@code byte}. If the key doesn't
	 * exist in the user's configuration or it can't
	 * be casted to a {@code byte}, it will be 
	 * fetched from the default configuration instead.
	 * If this value doesn't cast from either 
	 * configuration or it doesn't exist in either 
	 * configuration, a {@code ConfigException}
	 * will be thrown depending on whether the
	 * value is the incorrect type or it doesn't
	 * exist.
	 * @param confKey The key to get the associated value for
	 * @return <b>byte</b> The value associated with the passed key as a {@code byte}
	 * @throws ConfigException If the value from both the default and user configurations can't be casted to a {@code byte} or aren't present in the first place
	 */
	public abstract byte getByte(String confKey) throws ConfigException;
	
	/**
	 * Get a configuration value given an existing
	 * key as a {@code char}. If the key doesn't
	 * exist in the user's configuration or it can't
	 * be casted to a {@code char}, it will be 
	 * fetched from the default configuration instead.
	 * If this value doesn't cast from either 
	 * configuration or it doesn't exist in either 
	 * configuration, a {@code ConfigException}
	 * will be thrown depending on whether the
	 * value is the incorrect type or it doesn't
	 * exist.
	 * @param confKey The key to get the associated value for
	 * @return <b>char</b> The value associated with the passed key as a {@code char}
	 * @throws ConfigException If the value from both the default and user configurations can't be casted to a {@code char} or aren't present in the first place
	 */
	public abstract char getChar(String confKey) throws ConfigException;
	
	/**
	 * Get a configuration value given an existing
	 * key as a {@code double}. If the key doesn't
	 * exist in the user's configuration or it can't
	 * be casted to a {@code double}, it will be 
	 * fetched from the default configuration instead.
	 * If this value doesn't cast from either 
	 * configuration or it doesn't exist in either 
	 * configuration, a {@code ConfigException}
	 * will be thrown depending on whether the
	 * value is the incorrect type or it doesn't
	 * exist.
	 * @param confKey The key to get the associated value for
	 * @return <b>double</b> The value associated with the passed key as a {@code double}
	 * @throws ConfigException If the value from both the default and user configurations can't be casted to a {@code double} or aren't present in the first place
	 */
	public abstract double getDouble(String confKey) throws ConfigException;
	
	/**
	 * Get a configuration value given an existing
	 * key as a {@code float}. If the key doesn't
	 * exist in the user's configuration or it can't
	 * be casted to a {@code float}, it will be 
	 * fetched from the default configuration instead.
	 * If this value doesn't cast from either 
	 * configuration or it doesn't exist in either 
	 * configuration, a {@code ConfigException}
	 * will be thrown depending on whether the
	 * value is the incorrect type or it doesn't
	 * exist.
	 * @param confKey The key to get the associated value for
	 * @return <b>float</b> The value associated with the passed key as a {@code float}
	 * @throws ConfigException If the value from both the default and user configurations can't be casted to a {@code float} or aren't present in the first place
	 */
	public abstract float getFloat(String confKey) throws ConfigException;
	
	/**
	 * Get a configuration value given an existing
	 * key as an {@code int}. If the key doesn't
	 * exist in the user's configuration or it can't
	 * be casted to an {@code int}, it will be 
	 * fetched from the default configuration instead.
	 * If this value doesn't cast from either 
	 * configuration or it doesn't exist in either 
	 * configuration, a {@code ConfigException}
	 * will be thrown depending on whether the
	 * value is the incorrect type or it doesn't
	 * exist.
	 * @param confKey The key to get the associated value for
	 * @return <b>int</b> The value associated with the passed key as an {@code int}
	 * @throws ConfigException If the value from both the default and user configurations can't be casted to an {@code int} or aren't present in the first place
	 */
	public abstract int getInt(String confKey) throws ConfigException;
	
	/**
	 * Get a configuration value given an existing
	 * key as a {@code long}. If the key doesn't
	 * exist in the user's configuration or it can't
	 * be casted to a {@code long}, it will be 
	 * fetched from the default configuration instead.
	 * If this value doesn't cast from either 
	 * configuration or it doesn't exist in either 
	 * configuration, a {@code ConfigException}
	 * will be thrown depending on whether the
	 * value is the incorrect type or it doesn't
	 * exist.
	 * @param confKey The key to get the associated value for
	 * @return <b>long</b> The value associated with the passed key as a {@code long}
	 * @throws ConfigException If the value from both the default and user configurations can't be casted to a {@code long} or aren't present in the first place
	 */
	public abstract long getLong(String confKey) throws ConfigException;
	
	/**
	 * Get a configuration value given an existing
	 * key as a {@code short}. If the key doesn't
	 * exist in the user's configuration or it can't
	 * be casted to a {@code short}, it will be 
	 * fetched from the default configuration instead.
	 * If this value doesn't cast from either 
	 * configuration or it doesn't exist in either 
	 * configuration, a {@code ConfigException}
	 * will be thrown depending on whether the
	 * value is the incorrect type or it doesn't
	 * exist.
	 * @param confKey The key to get the associated value for
	 * @return <b>short</b> The value associated with the passed key as a {@code short}
	 * @throws ConfigException If the value from both the default and user configurations can't be casted to a {@code short} or aren't present in the first place
	 */
	public abstract short getShort(String confKey) throws ConfigException;
	
	/**
	 * Get a configuration value given an existing
	 * key as a {@code String}. If the key doesn't
	 * exist in the user's configuration or it can't
	 * be casted to a {@code String}, it will be 
	 * fetched from the default configuration instead.
	 * If this value doesn't cast from either 
	 * configuration or it doesn't exist in either 
	 * configuration, a {@code ConfigException}
	 * will be thrown depending on whether the
	 * value is the incorrect type or it doesn't
	 * exist.
	 * @param confKey The key to get the associated value for
	 * @return <b>String</b> The value associated with the passed key as a {@code String}
	 * @throws ConfigException If the value from both the default and user configurations can't be casted to a {@code String} or aren't present in the first place
	 */
	public abstract String getString(String confKey) throws ConfigException;
	
	/**
	 * Fetches all corresponding keys in a
	 * {@code String[]} given an input value.
	 * If this value wasn't found in the user's
	 * configuration, an empty or {@code null}
	 * array can be returned instead.
	 * @param <T> Allows any type of value to be used
	 * @param confVal The value to search for
	 * @return <b>String[]</b> The corresponding keys as an array of strings
	 */
	public abstract <T> String[] getKeysForValue(T confVal);
	
	
	//---CONFIGURATION KEY/VALUE SETTERS
	/**
	 * Inserts or replaces a key/value pair given 
	 * an input key and a corresponding value
	 * @param <T> Allows any type of value to be used
	 * @param keyName The name of the key to insert or replace
	 * @param keyValue The value that this key should have
	 */
	public abstract <T> void setKV(String keyName, T keyValue);
	
	/**
	 * Inserts or replaces a key, but sets the value
	 * of the key {@code null}/blank depending
	 * on whether or not {@code null} is allowed.
	 * Overloads {@code setKV(String, T)}.
	 * @param keyName The name of the key to insert or replace
	 */
	public abstract void setKV(String keyName);
	
	
	//--- CONFIGURATION MANAGMENT ROUTINES ---
	/**
	 * Defines the steps to take when attempting to get
	 * changes from the user's configuration file. Assumes
	 * that the specified configuration exists in its folder.
	 * Does so by first checking if the class' configuration
	 * matches up with the one in the filesystem. If so, 
	 * then nothing will be read. Otherwise, the changes
	 * present in the filesystem configuration will overwrite
	 * the current state of the configuration object in the class.
	 * @param confPath The path to load the external configuration from
	 * @param confName The filename of the external configuration
	 * @throws IOException If there was an error reading the configuration on the filesystem
	 */
	public abstract void getChanges(String confPath, String confName) throws IOException;
	
	/**
	 * Defines the steps to take when attempting to save
	 * changes to the user's configuration file. Assumes
	 * that the specified configuration exists in its folder.
	 * Does so by first checking if the class' configuration
	 * matches up with the one in the filesystem. If so, 
	 * then nothing will be written. Otherwise, the changes
	 * will be written to the file, overwriting anything that
	 * was changed in the file itself.
	 * @param confPath The path to load the external configuration from
	 * @param confName The filename of the external configuration
	 * @throws IOException If there was an error reading the configuration on the filesystem
	 */
	public abstract void writeChanges(String confPath, String confName) throws IOException;
	
	/**
	 * Gets the status as to whether or not the class'
	 * configuration object was changed compared with 
	 * the one in the filesystem. Assumes that the
	 * specified configuration already exists in its 
	 * folder.
	 * @param confPath The path to load the external configuration from
	 * @param confName The filename of the external configuration
	 * @return <b>boolean</b> The status as to whether or not the configurations match (true if they do, false if they don't)
	 * @throws IOException If there was an error reading the configuration on the filesystem
	 */
	public abstract boolean usrConfMatchesFS(String confPath, String confName) throws IOException;
	

	//--- KEY/VALUE PRESCENCE CHECKS ---
	/**
	 * Checks if a key by the name given exists in
	 * the configuration. Can be either from the 
	 * default or user's configuration depending on
	 * the value of {@code useExternal}.
	 * @param keyIn The key to search for by name
	 * @param useExternal Sets whether or not to use the default or user's configuration
	 * @return <b>boolean</b> The status as to whether or not this key exists in the selected configuration
	 */
	public abstract boolean keyExists(String keyIn, boolean useExternal);
	
	/**
	 * Checks if a key by the name given has
	 * a value tied to it that isn't {@code null}.
	 * Can be either from the default or user's 
	 * configuration depending on the value of
	 * {@code useExternal}.
	 * @param keyIn The key to search for by name
	 * @param useExternal Sets whether or not to use the default or user's configuration
	 * @return <b>boolean</b> The status as to whether or not this key has a value in the selected configuration
	 */
	public abstract boolean keyHasValue(String keyIn, boolean useExternal);
	
	/**
	 * Checks if a value given by the input 
	 * exists in the configuration. Can be 
	 * either from the default or user's 
	 * configuration depending on the value of
	 * {@code useExternal}.
	 * @param <T> Allows any type of value to be used
	 * @param valueIn The value to search for by data or name
	 * @param useExternal Sets whether or not to use the default or user's configuration
	 * @return <b>boolean</b> The status as to whether or not this value exists in the selected configuration
	 */
	public abstract <T> boolean valueExists(T valueIn, boolean useExternal);
}