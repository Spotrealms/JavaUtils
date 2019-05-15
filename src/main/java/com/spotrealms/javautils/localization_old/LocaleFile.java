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

package com.spotrealms.javautils.localization_old;

//Import Java classes and dependencies
import com.spotrealms.javautils.io.FileUtil;
import com.spotrealms.javautils.io.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * A set of IO methods for grabbing a language {@code Properties}
 * file into objects for processing
 * @author Spotrealms
 *
 */
@Deprecated
public class LocaleFile {
	//Set class variables
	private String fileLocation;
	private String fileName;
	private boolean isResLangFile;
	protected Properties langProps;
	
	//Class constructor
	protected LocaleFile(String fileLocation, String fileName, boolean isResLangFile){
		//Assign the class variables from the constructor's parameters
		this.fileLocation = fileLocation;
		this.fileName = fileName;
		this.isResLangFile = isResLangFile;
	}
	
	/**
	 * Load a language file into a {@code Properties}
	 * object for processing
	 * @throws IOException
	 */
	protected void loadProps() throws IOException {
		//Get the full file path from the class variables and normalize it
		String fullPath =  FileUtil.normalizePath(fileLocation + fileName);
		
		//Create a file object for later
		File langFile = null;
		
		//Check if the language file should be loaded as a resource file
		if(isResLangFile){
			//Check if the file exists on the classpath
			if(Resource.resouceFileExists(this.getClass(), fullPath)){
				//Get the resource as a file
				langFile = Resource.getResourceAsFile(this.getClass(), fullPath);
			}
			else {
				//Throw an IOException because the resource file wasn't found or was invalid
				throw new IOException("Resource file was not found at " + fullPath + ".");
			}
		}
		else {
			//Check if the language file is actually a file
			if(new File(fullPath).isFile()){
				//Get the language file as a file
				langFile = new File(fullPath);
			}
		}

		//Ensure the language file is an actual file and is a properties file
		if(langFile.isFile()){
			//Create a properties object
			this.langProps = new Properties();
			
			//Load the properties file into the properties object
			this.langProps.load(new FileInputStream(langFile));
		}
		else {
			throw new IOException("The input language file at " + fullPath + " is of an unknown type.");
		}
	}
	
	/**
	 * Get the language {@code Properties} object as a 
	 * {@code HashMap}
	 * @return <b>HashMap&ltString, Object&gt;</b> - The filled {@code HashMap}
	 */
	public HashMap<String, Object> propsToHashMap(){
		//Ensure the properties file is populated
		if(this.langProps != null){
			//Create a new HashMap object
			HashMap<String, Object> propMapOut = new HashMap<>();
			
			//Loop through each entry of the properties object
			for(Entry<?, ?> propEntry : this.langProps.entrySet()){
				propMapOut.put(propEntry.getKey().toString(), propEntry.getValue());
			}
			
			//Return the filled HashMap
			return propMapOut;
		}
		else {
			//Return null because the properties object hasn't been populated
			return null;
		}
	}
}