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
import com.spotrealms.javautils.ArrayUtil;
import com.spotrealms.javautils.io.FileUtil;

public class PropConfigFactory {
	//Set default class variables
	//private static final ArrayList<DefaultKeyPair> defaultKPArr = new ArrayList<>();
	private static final String defaultFullIntConfPath = "config.properties";
	private static final String defaultFullExtConfPath = defaultFullIntConfPath;
	private static final String defaultPlaceholderPrefix = "${";
	private static final String defaultPlaceholderSuffix = "}";
	private static final String defaultEmptyValuePlaceholder = "empty";
		
	//Set class variables
	//private ArrayList<DefaultKeyPair> defaultKPs = new ArrayList<>();
	private String intConfPath;
	private String extConfPath;
	private String confName;
	private String placeholderPrefix;
	private String placeholderSuffix;
	private String emptyValuePlaceholder;

	//Class constructor
	public PropConfigFactory(
		String fullIntConfPath,
		String fullExtConfPath,
		String placeholderPrefix,
		String placeholderSuffix,
		String emptyValuePlaceholder
	){
		//Assign the standard class variables from the constructor's parameters
		this.placeholderPrefix = placeholderPrefix;
		this.placeholderSuffix = placeholderSuffix;
		this.emptyValuePlaceholder = emptyValuePlaceholder;
			
		//Get the config name from the full internal path (join the file name and extension using a period as the delimiter)
		this.confName = ArrayUtil.listToStr(FileUtil.getFileProps(fullIntConfPath), ".");
		
		//Resolve the config path for the internal config
		this.intConfPath = resolvePath(fullIntConfPath);
		
		//Resolve the config path for the external config
		this.extConfPath = resolvePath(fullExtConfPath);
	}
	
	public PropConfigFactory(
		String fullIntConfPath,
		String fullExtConfPath,
		String placeholderPrefix,
		String placeholderSuffix
	){
		//Redirect to the overloaded constructor with unimplemented parameters being substituted by their default values
		this(
			fullIntConfPath,
			fullExtConfPath,
			placeholderPrefix,
			placeholderSuffix,
			defaultEmptyValuePlaceholder
		);
	}
	
	public PropConfigFactory(String fullIntConfPath, String fullExtConfPath){
		//Redirect to the overloaded constructor with unimplemented parameters being substituted by their default values
		this(
			fullIntConfPath,
			fullExtConfPath,
			defaultPlaceholderPrefix,
			defaultPlaceholderSuffix,
			defaultEmptyValuePlaceholder
		);
	}
	
	public PropConfigFactory(String fullIntConfPath){
		//Redirect to the overloaded constructor with unimplemented parameters being substituted by their default values
		this(
			fullIntConfPath,
			fullIntConfPath, //Will be the same as the internal config path by default
			defaultPlaceholderPrefix,
			defaultPlaceholderSuffix,
			defaultEmptyValuePlaceholder
		);
	}
	
	public PropConfigFactory(){
		//Redirect to the overloaded constructor with unimplemented parameters being substituted by their default values
		this(
			defaultFullIntConfPath,
			defaultFullExtConfPath,
			defaultPlaceholderPrefix,
			defaultPlaceholderSuffix,
			defaultEmptyValuePlaceholder
		);
	}

	//Getters
	public String getIntConfPath(){
		return intConfPath;
	}
	
	public String getExtConfPath(){
		return extConfPath;
	}

	public String getConfName(){
		return confName;
	}

	public String getPlaceholderPrefix(){
		return placeholderPrefix;
	}

	public String getPlaceholderSuffix(){
		return placeholderSuffix;
	}

	public String getEmptyValuePlaceholder(){
		return emptyValuePlaceholder;
	}

	//Setters
	public void setIntConfPath(String intConfPath){
		this.intConfPath = intConfPath;
	}
	
	public void setExtConfPath(String extConfPath){
		this.extConfPath = extConfPath;
	}

	public void setConfName(String confName){
		this.confName = confName;
	}

	public void setPlaceholderPrefix(String placeholderPrefix){
		this.placeholderPrefix = placeholderPrefix;
	}

	public void setPlaceholderSuffix(String placeholderSuffix){
		this.placeholderSuffix = placeholderSuffix;
	}

	public void setEmptyValuePlaceholder(String emptyValuePlaceholder){
		this.emptyValuePlaceholder = emptyValuePlaceholder;
	}
	
	private String resolvePath(String unresFullPath){
		//Check if the full path is just the name of the config to load
		if(unresFullPath.equals(confName)){
			//Set the path to be blank and return it, as the config is in the same folder as the execution directory
			return "";
		}
		else {
			//Pull out and return the config path from the fullpath by using the name as a regex for String.split
			return (unresFullPath.split(confName)[0] + "/");
		}
	}
}