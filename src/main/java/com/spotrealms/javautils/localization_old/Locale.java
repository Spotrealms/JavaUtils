/**
 * Warden: An anti-swear/anti-dox lightweight web API for applications or daemons that accept user input
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

/**
 *
 * @author Spotrealms Network
 * @website https://spotrealms.com
 * @website https://github.com/spotrealms
 */

package com.spotrealms.javautils.localization_old;

//Import Java classes and dependencies
/*
import com.spotrealms.javautils.JPlaceholder;
import com.spotrealms.javautils.TypeValidation;
import com.spotrealms.javautils.io.DirectoryUtil;
import com.spotrealms.javautils.io.FileUtil;
import com.spotrealms.javautils.io.Resource;
import com.spotrealms.javautils.localization_old.ISOLang;
import com.spotrealms.javautils.terminal.SimpleLogger;
import com.spotrealms.javautils.terminal.SimpleLogger.Log;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
*/

@Deprecated
public class Locale {
/*
	//Set class variables
	private String extLocaleFolder;
	private String extLocaleFilePath;
	private String extLocaleFile;
	private String intLocaleFolder;
	private String intLocaleFilePath;
	private String intLocaleFile;
	private ISOLang localeLang;
	private final String defaultLocaleFNPrefix = "locale-";
	private final String defaultLocaleFPPrefix = "locale";
	private SimpleLogger appLogger;
	private Config appConfig;
	public Properties appLocaleData;
	private ArrayList<ISOLang> availLangs = new ArrayList<>();
	
	//Set pre-localization status variables
	protected boolean initMkDirSuccessful;
	protected boolean initCopyFileSuccessful;
	protected boolean initLoadFileSuccessful;

	//Class constructor
	public Locale(String localeFolder, Config appConfig, SimpleLogger appLogger){
		//Assign the class variables from the constructor's parameters
		this.extLocaleFolder = FileUtil.normalizePath(localeFolder + "/");
		this.appConfig = appConfig;
		this.appLogger = appLogger;
		
		//Define available locale languages
		availLangs.add(ISOLang.EN);
		availLangs.add(ISOLang.DE);
		
		//Check if the target locale file is internal
		if(this.appConfig.appSettings.isLocaleUseInternal()){
			//Get properties of internal locale file
			getIntLocaleProps();
		}
		else {
			//Get the properties of external locale file
			getExtLocaleProps();
			
			//Initialize the app locale directory and locale file
			initExtLocaleDir();
		}
	}
	
	private boolean checkValidLang(){
		//Loop through every available language
		for(ISOLang availCode : availLangs){
			//Check if the current valid code matches the locale specified in the config
			if(localeLang == availCode){
				//Return true because the code is valid
				return true;
			}
		}
		
		//Return false because the code is invalid
		return false;
	}
	
	private void getIntLocaleProps(){
		//Get the value of the ISO lang
		localeLang = appConfig.appSettings.getLocaleLanguage();
		
		//Check if the locale specified in the config is valid
		if(checkValidLang()){
			//Set the name of the locale file based on its locale
			intLocaleFile = (defaultLocaleFNPrefix + String.valueOf(localeLang).toLowerCase() + ".properties");
		}
		else {
			//Point to the default locale file instead
			intLocaleFile = (defaultLocaleFNPrefix + String.valueOf(ConfigDefaults.localeLanguage).toLowerCase() + ".properties");
		
			System.out.println("User specified language is invalid. Using default EN");
		}
			
		//Set the path to the locale file as it is inside the JAR
		intLocaleFilePath = (defaultLocaleFPPrefix + "/" + intLocaleFile);
			
		//Get the path to the locale folder via the path to the locale file
		intLocaleFolder = String.valueOf(Paths.get(intLocaleFilePath).getParent());
		
		//DEBUG
		System.out.println("INT LOC PATH: " + intLocaleFilePath);
		System.out.println("INT LOC FOLDER: " + intLocaleFolder);
		System.out.println("INT LOC FILE: " + intLocaleFile);
		
		//Load the internal locale file
		loadLocale();
	}
	
	private void getExtLocaleProps(){
		//Grab locale file path from the existing Settings class within the app config
		extLocaleFilePath = appConfig.appSettings.getLocaleFileLocation();
	
		//Resolve the placeholder for the locale file base directory
		Map<String, String> localeFileVars = new HashMap<>();
		localeFileVars.put("appRoot", extLocaleFolder);
	
		//Replace the path with the resolved path and normalize it
		extLocaleFilePath = FileUtil.normalizePath(JPlaceholder.format(extLocaleFilePath, localeFileVars));
	
		//Get the path to the locale folder via the path to the locale file
		extLocaleFolder = FileUtil.normalizePath(String.valueOf(Paths.get(extLocaleFilePath).getParent()));
	
		//Get the name of the locale file from its path
		extLocaleFile = String.valueOf(Paths.get(extLocaleFilePath).getFileName());
	
		//DEBUG
		System.out.println("EXT LOC PATH: " + extLocaleFilePath);
		System.out.println("EXT LOC FOLDER: " + extLocaleFolder);
		System.out.println("EXT LOC FILE: " + extLocaleFile);
	}
	
	private void initExtLocaleDir(){
		//Check if the locale folder exists
		if(!(DirectoryUtil.dirExists(extLocaleFolder))){
			//Create the locale directory
			DirectoryUtil.createDirectory(extLocaleFolder, false);
				
			//Check if the folder exists (roots out file permission issues)
			if(DirectoryUtil.dirExists(extLocaleFolder)){
				//Mark that the operation was successful
				initMkDirSuccessful = true;
				appLogger.submitLog("The app locale directory (" + extLocaleFolder + ") has been created successfully", Log.INFO, true);
				
				System.out.println("yes");
					
				//Start initializing the locale file
				initExtLocaleFile();
			}
			else {
				//Mark that there was an error
				initMkDirSuccessful = false;
				appLogger.submitLog("The app locale directory (" + extLocaleFolder + ") couldn't be created. Check your file permissions to ensure the program has write access.", Log.ERROR, true);
				System.out.println("no");
			}
		}
		else {
			System.out.println("ready: " + this);
			
			//Start initializing the locale file
			initExtLocaleFile();	
		}
	}

	private void initExtLocaleFile(){
		//Check if the locale file exists in the folder
		if(!(FileUtil.fileExists(extLocaleFilePath))){
			try {
				//Get the properties of the internal locale file
				getIntLocaleProps();
				
				//Copy the default locale file from inside the JAR file
				
				System.out.println("intLocaleFilePath " + intLocaleFilePath);
				System.out.println("extLocaleFilePath " + extLocaleFilePath);
				System.out.println("this.getClass() " + this.getClass());
				
				Resource.exportResource(this.getClass(), intLocaleFilePath, extLocaleFilePath);
				
				//Mark that the creation of the locale was successful
				initCopyFileSuccessful = true;
				appLogger.submitLog("The locale file was not found in the locale folder. Creating a new one.", Log.INFO, true);
				
				//Load the locale file
				loadLocale();
			} 
			catch(IOException e){
				//Mark that an error occurred
				initCopyFileSuccessful = false;
				appLogger.submitLog("The app locale file (" + intLocaleFile + ") couldn't be created. Check your file permissions to ensure the program has write access.", Log.ERROR, true);
			}
		}
		else {				
			//Load the locale file
			loadLocale();
		}
	}

	private void loadLocale(){
		//Begin load the locale using Java's Properties object
		try {
			//Create the Properties object
			appLocaleData = new Properties();
			
			//Determine where to load the locale file from
			//Determine where to load the locale file from
			if(appConfig.appSettings.isLocaleUseInternal()){
				//Load the contents of the locale file into the object via Resource FileStream
				appLocaleData.load(Resource.getResourceAsFileStream(this.getClass(), intLocaleFilePath));
			}
			else {
				//Load the contents of the external locale file into the object via FileStream
				appLocaleData.load(FileUtil.loadFileAsFileStream(extLocaleFilePath));
			}
				
			//Mark that the load was successful
			initLoadFileSuccessful = true;
			appLogger.submitLog("The locale file was loaded successfully.", Log.INFO, true);
			
			//Parse the locale file
			//parseConfig();
			
			
			System.out.println("GLAZER");
			System.out.println(appLocaleData.get("user1"));
			System.out.println(appLocaleData.get("user2"));
			System.out.println(appLocaleData.get("user3"));
			System.out.println(appLocaleData.get("user4"));
			System.out.println(appLocaleData.get("user5"));
			System.out.println(appLocaleData.get("user6"));
			System.out.println(appLocaleData.get("user7"));
			
			
			System.out.println(getMsg("companny"));
		} 
		catch(Exception e){
			//Mark that an error occurred
			initLoadFileSuccessful = false;
			
			System.out.println("FUCK! something went wrong!");
			
			appLogger.submitLog("An error occurred while loading the locale file. Ensure the locale file exists and the application has read access.", Log.ERROR, true);
		
			e.printStackTrace();
		}
	}
	
	public String getMsg(String localeKey, Map<String, String> msgPlaceholders){
		//Check if the message exists in the locale file
		if(appLocaleData.get(localeKey) == null){
			return "nully";
		}
		else {
			return "not nully";
		}
	}
	
	public String getMsg(String localeKey){
		//Check if the message exists in the locale file
		if(appLocaleData.get(localeKey) == null){
			//Determine if the locale file is internal or external
			String errLocName;
			if(appConfig.appSettings.isLocaleUseInternal()){
				//Populate the errored file with the targeted internal locale name
				errLocName = intLocaleFile;
			}
			else {
				//Populate the errored file with the targeted external locale name
				errLocName = extLocaleFile;
			}
			
			//Create the error message
			String errMsg = ("Invalid translation key \"" + localeKey + "\" in locale file \"" + errLocName + "\".");
			
			//Send an error message to the console
			appLogger.submitLog(errMsg, Log.WARN, true);
			
			//Return null as the message is invalid
			return null;
		}
		else {
			return "not nully";
		}
	}
	
	/*
	private void parseConfig(){
		//Ensure all available entries aren't blank or of an incorrect type and fill them using the ConfigDefaults.java file if they are
		if((appConfigData.getProperty("httpd.port") == null) || (TypeValidation.isInt(appConfigData.getProperty("httpd.port")) == false)){
			appConfigData.put("httpd.port", ConfigDefaults.httpdPort);
		}
		if(appConfigData.getProperty("locale.file") == null){
			appConfigData.put("locale.file", ConfigDefaults.localeFileLocation);
		}
		if((appConfigData.getProperty("locale.language") == null) || (ISOLang.isValidLangCode(appConfigData.getProperty("locale.language")) == false)){
			appConfigData.put("locale.language", ConfigDefaults.localeFileLocation);
		}
		if((appConfigData.getProperty("locale.use-internal") == null) || (TypeValidation.isBool(appConfigData.getProperty("locale.use-internal")) == false)){
			appConfigData.put("locale.use-internal", ConfigDefaults.localeUseInternal);
		}

		
		//Set the app settings
		appSettings = new Settings(
			Integer.parseInt(appConfigData.getProperty("httpd.port")),
			appConfigData.getProperty("locale.file"), 
			ISOLang.getCode(appConfigData.getProperty("locale.language")),
			Boolean.parseBoolean(appConfigData.getProperty("locale.use-internal"))
		);
	}
	*/
}