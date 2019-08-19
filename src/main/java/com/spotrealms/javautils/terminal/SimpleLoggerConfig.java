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

package com.spotrealms.javautils.terminal;

//Import first-party classes
import com.spotrealms.javautils.terminal.color.NColor;

//TODO: Add JavaDoc

public class SimpleLoggerConfig {
	//Setup default color variables (has to be strings because extending classes may use something besides ANSI escape codes)
	private static final String defaultInfoHeaderColor = NColor.WHITE_BRIGHT.toString();
	private static final String defaultInfoBodyColor = NColor.WHITE.toString();
	private static final String defaultWarnHeaderColor = NColor.YELLOW.toString();
	private static final String defaultWarnBodyColor = NColor.YELLOW_BRIGHT.toString();
	private static final String defaultErrorHeaderColor = NColor.RED.toString();
	private static final String defaultErrorBodyColor = NColor.RED_BRIGHT.toString();
	private static final String defaultSuccessHeaderColor = NColor.GREEN.toString();
	private static final String defaultSuccessBodyColor = NColor.GREEN_BRIGHT.toString();
	private static final String defaultDebugHeaderColor = NColor.MAGENTA.toString();
	private static final String defaultDebugBodyColor = NColor.MAGENTA_BRIGHT.toString();
	private static final String defaultResetColor = NColor.RESET.toString();
	
	//Setup color variables
	private String infoHeaderColor;
	private String infoBodyColor;
	private String warnHeaderColor;
	private String warnBodyColor;
	private String errorHeaderColor;
	private String errorBodyColor;
	private String successHeaderColor;
	private String successBodyColor;
	private String debugHeaderColor;
	private String debugBodyColor;
	private String resetColor;
	
	//Setup default variable values
	protected static final String defaultAppHeader = "";
	private static final boolean defaultGlobalNoColors = false;
	private static final String defaultLogFormat = "${headercolor}[${logtype}]${resetcolor}: ${bodycolor}${message}";
	private static final boolean defaultSuppressOutput = false;
	
	//Setup private variables
	private String appHeader;
	private boolean globalNoColors;
	private String logFormat;
	private boolean suppressOutput;

	//Class constructor
	public SimpleLoggerConfig(String appHeader, String logFormat, boolean globalNoColors ,boolean suppressOutput){
		//Assign the class variables from the constructor
		this.appHeader = appHeader;
		this.logFormat = logFormat;
		this.globalNoColors = globalNoColors;
		this.suppressOutput = suppressOutput;
		
		//Assign default color variables for the config object
		this.infoHeaderColor = defaultInfoHeaderColor;
		this.infoBodyColor = defaultInfoBodyColor;
		this.warnHeaderColor = defaultWarnHeaderColor;
		this.warnBodyColor = defaultWarnBodyColor;
		this.errorHeaderColor = defaultErrorHeaderColor;
		this.errorBodyColor = defaultErrorBodyColor;
		this.successHeaderColor = defaultSuccessHeaderColor;
		this.successBodyColor = defaultSuccessBodyColor;
		this.debugHeaderColor = defaultDebugHeaderColor;
		this.debugBodyColor = defaultDebugBodyColor;
		this.resetColor = defaultResetColor;
	}
	
	public SimpleLoggerConfig(String appHeader, String logFormat, boolean globalNoColors){
		//Redirect to the overloaded constructor with unimplemented parameters being substituted by their default values
		this(appHeader, logFormat, globalNoColors, defaultSuppressOutput);
	}
	
	public SimpleLoggerConfig(String appHeader, String logFormat){
		//Redirect to the overloaded constructor with unimplemented parameters being substituted by their default values
		this(appHeader, logFormat, defaultGlobalNoColors, defaultSuppressOutput);
	}
	
	public SimpleLoggerConfig(String appHeader){
		//Redirect to the overloaded constructor with unimplemented parameters being substituted by their default values
		this(appHeader, defaultLogFormat, defaultGlobalNoColors, defaultSuppressOutput);
	}
	
	public SimpleLoggerConfig(){
		//Redirect to the overloaded constructor with unimplemented parameters being substituted by their default values
		this(defaultAppHeader, defaultLogFormat, defaultGlobalNoColors, defaultSuppressOutput);
	}

	//Getters for class variables
	public String getAppHeader(){
		return appHeader;
	}
	
	public boolean isGlobalNoColors(){
		return globalNoColors;
	}

	public String getLogFormat(){
		return logFormat;
	}
	
	public boolean isSuppressOutput(){
		return suppressOutput;
	}

	//Setters for class variables
	public void setAppHeader(String appHeader){
		this.appHeader = appHeader;
	}
	
	public void setGlobalNoColors(boolean globalNoColors){
		this.globalNoColors = globalNoColors;
	}

	public void setLogFormat(String logFormat){
		this.logFormat = logFormat;
	}
	
	public void setSuppressOutput(boolean suppressOutput){
		this.suppressOutput = suppressOutput;
	}

	//Getters for colors
	public String getInfoHeaderColor(){
		return infoHeaderColor;
	}
	
	public String getInfoBodyColor(){
		return infoBodyColor;
	}
	
	public String getWarnHeaderColor(){
		return warnHeaderColor;
	}
	
	public String getWarnBodyColor(){
		return warnBodyColor;
	}
	
	public String getErrorHeaderColor(){
		return errorHeaderColor;
	}
	
	public String getErrorBodyColor(){
		return errorBodyColor;
	}
	
	public String getSuccessHeaderColor(){
		return successHeaderColor;
	}
	
	public String getSuccessBodyColor(){
		return successBodyColor;
	}
	
	public String getDebugHeaderColor(){
		return debugHeaderColor;
	}
	
	public String getDebugBodyColor(){
		return debugBodyColor;
	}
	
	public String getResetColor(){
		return resetColor;
	}

	//Setters for colors
	public void setInfoHeaderColor(String infoHeaderColor){
		this.infoHeaderColor = infoHeaderColor;
	}
	
	public void setInfoBodyColor(String infoBodyColor){
		this.infoBodyColor = infoBodyColor;
	}
	
	public void setWarnHeaderColor(String warnHeaderColor){
		this.warnHeaderColor = warnHeaderColor;
	}
	
	public void setWarnBodyColor(String warnBodyColor){
		this.warnBodyColor = warnBodyColor;
	}
	
	public void setErrorHeaderColor(String errorHeaderColor){
		this.errorHeaderColor = errorHeaderColor;
	}
	
	public void setErrorBodyColor(String errorBodyColor){
		this.errorBodyColor = errorBodyColor;
	}
	
	public void setSuccessHeaderColor(String successHeaderColor){
		this.successHeaderColor = successHeaderColor;
	}
	
	public void setSuccessBodyColor(String successBodyColor){
		this.successBodyColor = successBodyColor;
	}
	
	public void setDebugHeaderColor(String debugHeaderColor){
		this.debugHeaderColor = debugHeaderColor;
	}
	
	public void setDebugBodyColor(String debugBodyColor){
		this.debugBodyColor = debugBodyColor;
	}
	
	public void setResetColor(String resetColor){
		this.resetColor = resetColor;
	}
}
