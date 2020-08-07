package com.spotrealms.javautils.terminal;

//Import first-party classes
import com.spotrealms.javautils.JPlaceholder;
import com.spotrealms.javautils.StringUtil;

//Import Java classes and dependencies
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
//TODO: Add JavaDoc

@Deprecated
public class SimpleLogger {
	//Setup class variables
	private final SimpleLoggerConfig simpleLoggerConfig;
	private final String appHeader;
	private final boolean globalNoColors;
	private final String logFormat;
	private final boolean suppressOutput;
	
	//Class constructor
	public SimpleLogger(SimpleLoggerConfig simpleLoggerConfig){	
		//Assign the class variables from the constructor's parameters
		this.simpleLoggerConfig = simpleLoggerConfig;
				
		//Pull out the other variables from the config
		this.appHeader = simpleLoggerConfig.getAppHeader();
		this.globalNoColors = simpleLoggerConfig.isGlobalNoColors();
		this.logFormat = simpleLoggerConfig.getLogFormat();
		this.suppressOutput = simpleLoggerConfig.isSuppressOutput();
	}

	//Setup available log types for easier integration with extraneous logging methods
	public enum Log {
		DEBUG,
		INFO,
		WARN,
		ERROR,
		SUCCESS;
	}
	
	public void submitLog(String logMessage, Log logType, boolean removeColors, String formatOverride){
		//Check if the output should be suppressed (if this conditional is false, nothing will be shown to the user)
		if(!(suppressOutput)){
			//Generate the log message to send via genLogOut()
			String outputMsg = genLogOut(logMessage, logType, formatOverride);
		
			//Check if color should be excluded from the output (either because this log shouldn't have color or colors were overridden globally)
			if(removeColors || globalNoColors){
				//Output the log message without the color
				System.out.println(StringUtil.cleanText(outputMsg, "COLOR"));
			}
			else {
				//Output the log message as usual
				System.out.println(outputMsg);
			}
		}
	}
	
	public void submitLog(String logMessage, Log logType, String formatOverride){
		//Redirect to the overloaded method
		submitLog(logMessage, logType, false, formatOverride);
	}
	
	public void submitLog(String logMessage, Log logType, boolean removeColors){
		//Redirect to the overloaded method
		submitLog(logMessage, logType, removeColors, logFormat);
	}
	
	public void submitLog(String logMessage, Log logType){
		//Redirect to the overloaded method
		submitLog(logMessage, logType, false, logFormat);
	}
	
	public void submitLog(String logMessage){
		//Redirect to the overloaded method
		submitLog(logMessage, Log.INFO, false, logFormat);
	}
	
	protected String genLogOut(String logMessage, Log logType, String formatOverride){
		//Create a throwaway variable for the header to avoid changing the original
		String aHeader = appHeader;
				
		//Create the variables for the header and body colors (info colors are the default)
		String lHeaderColor = simpleLoggerConfig.getInfoHeaderColor();
		String lBodyColor = simpleLoggerConfig.getInfoBodyColor();
		final String lResetColor = simpleLoggerConfig.getResetColor();
				
		//Determine which log type to use
		switch(logType){
			case INFO:
				//Set console message colors
				lHeaderColor = simpleLoggerConfig.getInfoHeaderColor();
				lBodyColor = simpleLoggerConfig.getInfoBodyColor();
			break;
					
			case WARN:
				//Set console message colors
				lHeaderColor = simpleLoggerConfig.getWarnHeaderColor();
				lBodyColor = simpleLoggerConfig.getWarnBodyColor();
			break;
					
			case ERROR:
				//Set console message colors
				lHeaderColor = simpleLoggerConfig.getErrorHeaderColor();
				lBodyColor = simpleLoggerConfig.getErrorBodyColor();
			break;
					
			case SUCCESS:
				//Set console message colors
				lHeaderColor = simpleLoggerConfig.getSuccessHeaderColor();
				lBodyColor = simpleLoggerConfig.getSuccessBodyColor();
			break;
					
			case DEBUG:
				//Set console message colors
				lHeaderColor = simpleLoggerConfig.getDebugHeaderColor();
				lBodyColor = simpleLoggerConfig.getDebugBodyColor();
			break;
		}
				
		//Construct the color array to be sent to the formatter
		String[] logColors = {lHeaderColor, lBodyColor, lResetColor};
				
		//Check if the log format is unfilled
		if(formatOverride != null){
			//Format and return the completed log message with the default log format syntax
			return (formatLogOutput(logMessage, aHeader, logColors, logType, logFormat));
		}
		else {
			//Format and return the completed log message with the user specified log format syntax (if it was provided)
			return (formatLogOutput(logMessage, aHeader, logColors, logType, formatOverride));
		}
	}
	
	private String formatLogOutput(
		String inputMsg, 
		String logHeader,
		String[] logColors,
		Log logType,
		String formatSyntax
	){
		//Setup the placeholder
		Map<String, Object> possFormats = new HashMap<>();
		
		//Get the format for the date timestamp
		String timestampFormat = (String) JPlaceholder.getPlValues(formatSyntax).get("date");
		
		//Create a timestamp string for later
		String logTimestamp;
		
		//Check if the timestamp format is empty
		if(timestampFormat.isEmpty()){
			//Get the date format from the user's locale
			DateFormat dateFormat = DateFormat.getDateTimeInstance(
				DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.getDefault()
			);
				
			//Populate with the format from the user's inferred locale
			logTimestamp = dateFormat.format(new Date());
		}
		else {	
			//Populate the timestamp formatter with the format from the placeholder
			SimpleDateFormat timestampFormatter = new SimpleDateFormat(timestampFormat);
			
			//Create the date via the passed date format in the ${date} placeholder
			logTimestamp = timestampFormatter.format(new Date());
		}
		
		//Populate the hashmap with the available placeholders
		//These placeholders also include the following syntax:
		//${date <a valid date format>}
		//everything else: ${<var name>}
		possFormats.put("appheader", logHeader);
		possFormats.put("date", logTimestamp);
		possFormats.put("logtype", logType.toString());
		possFormats.put("headercolor", logColors[0].toString());
		possFormats.put("bodycolor", logColors[1].toString());
		possFormats.put("resetcolor", logColors[2].toString());
		possFormats.put("message", inputMsg);

		//Replace the placeholders
		String formattedMsg = JPlaceholder.format((logColors[2] + formatSyntax + logColors[2]), possFormats);
		
		//Return the formatted log message
		return formattedMsg;
	}
	
	//TODO: Move to JUnit test instead of declaring it here in the class itself
	
	/*
	 * TESTS
	public static void main(String[] args){
		//Simple example
		SimpleLogger log1 = new SimpleLogger(new SimpleLoggerConfig());
		
		log1.submitLog("This is a simple log test", Log.INFO);		
		log1.submitLog("This is a simple log warning", Log.WARN);		
		log1.submitLog("This is a simple log error", Log.ERROR);		
		log1.submitLog("This is a simple log success", Log.SUCCESS);		
		log1.submitLog("This is a simple log debug", Log.DEBUG);
		
		System.out.println("\n");
		
		//Advanced example
		SimpleLoggerConfig l2c = new SimpleLoggerConfig();
		l2c.setAppHeader("Advanced Log Example");
		l2c.setLogFormat("${headercolor}[${appheader} - ${logtype}]${resetcolor}: ${bodycolor}${message}");
		
		SimpleLogger log2 = new SimpleLogger(l2c);
		
		log2.submitLog("This is a simple log test", Log.INFO);		
		log2.submitLog("This is a simple log warning", Log.WARN);		
		log2.submitLog("This is a simple log error", Log.ERROR);		
		log2.submitLog("Woah. This is different!", Log.DEBUG, (TF.Color.BLUE_BACKGROUND_BRIGHT + "${message} ${date yyy} ${logtype} ${appheader}"));
		log2.submitLog("Awww man. No more colors!", Log.DEBUG, true, (TF.Color.BLUE_BACKGROUND_BRIGHT + "${message} ${date yyy} ${logtype} ${appheader}"));
	
		//Compressed method
		SimpleLogger log3 = new SimpleLogger(
			new SimpleLoggerConfig(
				"Constructor Parameter Assignment",
				"${headercolor}[${appheader} - ${logtype}]${resetcolor}: ${bodycolor}${message}",
				false
			)
		);
				
		log3.submitLog("This is a simple log test", Log.INFO);		
		log3.submitLog("This is a simple log warning", Log.WARN);		
	}
	*/
}