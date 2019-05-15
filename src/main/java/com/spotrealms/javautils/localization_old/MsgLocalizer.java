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
import com.spotrealms.javautils.JPlaceholder;
import java.io.IOException;
import java.util.Map;

@Deprecated
public class MsgLocalizer {
	//Set class variables
	private String msgFilePath;
	private String msgFileName;
	private boolean useIntMsgFile;
	private String msgFileLanguage;
	private ISOLang msgFileRealLang;
	private LocaleFile msgFileObj;
		
	//Class constructor (if the file uses a defined language)
	public MsgLocalizer(	
		String msgFilePath,
		String msgFileName,
		boolean useIntMsgFile,
		ISOLang msgFileRealLang
	) throws IOException {
		//Assign the class variables from the constructor's parameters	
		this.msgFilePath = msgFilePath;
		this.msgFileName = msgFileName;
		this.useIntMsgFile = useIntMsgFile;
		this.msgFileLanguage = msgFileRealLang.getLang();
		this.msgFileRealLang = msgFileRealLang;
		
		//Get the language file object
		loadMsgFile();
	}
		
	//Class constructor (if the file uses a non-defined language)
	public MsgLocalizer(
		String msgFilePath,
		String msgFileName,
		boolean useIntMsgFile,
		String msgFileLanguage
	) throws IOException {
		//Assign the class variables from the constructor's parameters
		this.msgFilePath = msgFilePath;
		this.msgFileName = msgFileName;
		this.useIntMsgFile = useIntMsgFile;
		this.msgFileLanguage = msgFileLanguage;
		this.msgFileRealLang = null;
		
		//Get the language file object
		loadMsgFile();
	}
	
	public String getMsg(String keyName){
		//Get the value of the key from the properties file
		String langMsg = this.msgFileObj.langProps.get(keyName).toString();
		
		//Return the value of the key
		return langMsg;
	}
	public String getMsg(String keyName, Map<String, Object> placeholderKeypairs){
		//Get the value of the key from the properties file
		String langMsg = this.msgFileObj.langProps.get(keyName).toString();
		
		//Parse the placeholders
		langMsg = parsePlaceholders(langMsg, placeholderKeypairs);
		
		//Return the value of the key
		return langMsg;
	}
		
	private void loadMsgFile() throws IOException {
		//Create a new instance of the language file
		this.msgFileObj = new LocaleFile(this.msgFilePath, this.msgFileName, this.useIntMsgFile);
		
		//Load the properties file into the object
		this.msgFileObj.loadProps();
	}
	
	private String parsePlaceholders(String lineIn, Map<String, Object> placeholderKeypairs){
		//Create a new JPlaceholder object
		JPlaceholder JPlaceholder = new JPlaceholder(placeholderKeypairs);
		
		//Replace the placeholders in the string
		String strOut = JPlaceholder.format(lineIn);
		
		//Return the resulting string
		return strOut;
	}

	//Getters and setters
	public String getMsgFilePath(){
		return msgFilePath;
	}

	public String getMsgFileName(){
		return msgFileName;
	}

	public boolean isUseIntMsgFile(){
		return useIntMsgFile;
	}

	public String getMsgFileLanguage(){
		return msgFileLanguage;
	}

	public ISOLang getMsgFileRealLang(){
		return msgFileRealLang;
	}

	public LocaleFile getMsgFileObj(){
		return msgFileObj;
	}

	public void setMsgFilePath(String msgFilePath){
		this.msgFilePath = msgFilePath;
	}

	public void setMsgFileName(String msgFileName){
		this.msgFileName = msgFileName;
	}

	public void setUseIntMsgFile(boolean useIntMsgFile){
		this.useIntMsgFile = useIntMsgFile;
	}

	public void setMsgFileLanguage(String msgFileLanguage){
		this.msgFileLanguage = msgFileLanguage;
	}

	public void setMsgFileRealLang(ISOLang msgFileRealLang){
		this.msgFileRealLang = msgFileRealLang;
	}

	public void setMsgFileObj() throws IOException {
		//Regenerate the object
		loadMsgFile();
	}
}
