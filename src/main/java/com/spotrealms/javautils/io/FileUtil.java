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

package com.spotrealms.javautils.io;

//Import first-party classes
import com.spotrealms.javautils.StringUtil;

//Import Java classes and dependencies
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

/**
 * A series of methods for working with files on the system.
 * Working with files in Java can be useful if a file 
 * needs to be written to or read from. 
 * <br>This class has the following file management and
 * file utility features:
 * <ul>
 * 	<li>File line counter ({@code countfileLines})</li>
 * 	<li>File line deleter at position x ({@code delAtPos})</li>
 * 	<li>File existence checking ({@code fileExists})</li>
 * 	<li>Class execution path getter ({@code getExecPath})</li>
 * 	<li>File extension parsing ({@code getExtension} / <b>Deprecated!</b> Use {@code getFileProps} instead.)</li>
 * 	<li>File property parsing ({@code getFileProps})</li>
 * 	<li>File relative path getter ({@code getRelativePath})</li>
 * 	<li>File line matcher ({@code locateMatchingLines})</li>
 * 	<li>File path normalizer ({@code normalizePath})</li>
 * 	<li>UNIX file path converter ({@code unixToWinPath})</li>
 * 	<li>Windows file path converter ({@code winToUnixPath})</li>
 * 	<li>File line writer to end ({@code writeAtEnd})</li>
 * 	<li>File line writer at position x ({@code writeAtPos})</li>
 * </ul>
 * 
 * Additionally, this class contains methods for loading generic
 * files that aren't resource files (files that are in the JAR or 
 * on the classpath).
 * <br>This class includes the following file loading options:
 * <ul>
 * 	<li>{@link File}</li>
 * 	<li>{@link FileInputStream}</li>
 * 	<li>{@link InputStream}</li>
 * 	<li>{@link String}</li>
 * 	<li>{@link URL}</li>
 * </ul>
 * 
 * @author Spotrealms &amp; Contributors
 */
public class FileUtil {
	/**
	 * Defines comment line markers used by a
	 * variety of different programming and markup 
	 * languages. <br>Defined at the following page:
	 * <a href="https://en.wikipedia.org/wiki/Comment_(computer_programming)#Examples" target="_blank">
	 *  https://en.wikipedia.org/wiki/Comment_(computer_programming)#Examples
	 * </a>
	 */
	public enum commentLines {
		//Define the comment line sequences
		ADA("--"),
		BASIC("REM"),
		BATCH("::"),
		CISCO("!"),
		CPP("//"),
		FORTRAN4("C"),
		LATEX("%"),
		PERL("#"),
		PYTHON("\"\"\"");
		
		//Get the enum values
		private String cLine;
		commentLines(String envCLine){
			//Convert the enum value to a string
			this.cLine = envCLine;
		}
	 
		//Output the string
		@Override
	    public String toString(){
	        return cLine;
	    }
	}
	
	/**
	 * Count the number of lines a given {@code File}
	 * contains
	 * @param tFile The {@code File} to count the lines in
	 * @return <b>int</b> The number of lines in the {@code File}
	 * @throws IOException If the path to the {@code File} is invalid
	 */
	public static int countFileLines(File tFile) throws IOException {
		//Make sure the file exists
		if(tFile.exists()){
			//Ensure the file is indeed a file
			if(tFile.isFile()){
				//Get the absolute path to the file
				Path filePath = Paths.get(normalizePath(tFile.getAbsolutePath()));
						
				//Read in the file lines to a List
				List<String> fileLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
				
				//Return the number of lines in the file
				return (fileLines.size() + 1);
			}
			else {
				//Throw an IOException because the file is a directory
				throw new IOException("The file " + tFile.getName() + " is a directory.");
			}
		}
		else {
			//Throw an IOException because no file exists at the provided path
			throw new IOException("The file at " + tFile.getPath() + " doesn't exist.");
		}
	}
	
	/**
	 * Delete a line in a {@code File} at a specific
	 * point in that {@code File}
	 * @param tFile The {@code File} to write the line to
	 * @param lineNum The position in the {@code File} to delete
	 * @throws IOException If the path to the {@code File} is invalid
	 */
	public static void delAtPos(File tFile, int lineNum) throws IOException {
		//Make sure the file exists
		if(tFile.exists()){
			//Ensure the file is indeed a file
			if(tFile.isFile()){
				//Get the absolute path to the file
				Path filePath = Paths.get(normalizePath(tFile.getAbsolutePath()));
				
				//Read in the file lines to a List
				List<String> fileLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
				
				//Remove the line from the List at position x
				fileLines.remove((lineNum - 1));
				
				//Write the lines to the file
				Files.write(filePath, fileLines, StandardCharsets.UTF_8);
			}
			else {
				//Throw an IOException because the file is a directory
				throw new IOException("The file " + tFile.getName() + " is a directory.");
			}
		}
		else {
			//Throw an IOException because no file exists at the provided path
			throw new IOException("The file at " + tFile.getPath() + " doesn't exist.");
		}
	}
	
	
	/**
	 * Check if a file exists at the relative path specified
	 * @param fileLocation The relative path of the file to check
	 * @return <b>boolean</b> The status of whether the file exists or not
	 */
	public static boolean fileExists(String fileLocation){
		//Normalize the file path
		fileLocation = normalizePath(fileLocation);
		
		//Create a temporary file object for testing
		File filePath = new File(fileLocation);
		
		//Check if the file exists and is a valid file
		if(filePath.exists() && filePath.isFile()){
			//The file exists and it is valid
			return true;
		}
		else {
			//The file doesn't exist or it isn't valid
			return false;
		}
	}
	
	/**
	 * Get the execution directory of the class as a {@code String}
	 * @return <b>String</b> The absolute path to the execution directory
	 * @throws UnsupportedEncodingException If the URL to the path of the class is of an unsupported encoding
	 */
	public static String getExecPath() throws UnsupportedEncodingException {
		//Get the path to the class		
		String absPath = FileUtil.class.getProtectionDomain().getCodeSource().getLocation().toExternalForm();
		
		//Prepare the absolute path for processing
		absPath = absPath.substring(0, absPath.length() - 1);
		absPath = absPath.substring(0, absPath.lastIndexOf("/") + 1);
		String configPath = absPath;
		
		//Get the OS of the execution environment
		String os = System.getProperty("os.name");
		
		//Check if the execution OS is Windows
		if(os.indexOf("Windows") != -1){
			//Properly escape the backslash path separator
			configPath = configPath.replace("/", "\\\\");

			//Check if the path is a URL
			if(configPath.indexOf("file:\\\\") != -1){
				//Escape the backslash path separator
				configPath = configPath.replace("file:\\\\", "");
			}
		}
		//Check if the path is a URL
		else if(configPath.indexOf("file:") != -1){
			//Get rid of "file:"
			configPath = configPath.replace("file:", "");
		}
		
		//Return the resulting absolute path
		return StringUtil.decodeURL(configPath);
	}
	
	/**
	 * Get the extension of a file from its name
	 * @param fileName The name of the file
	 * @return <b>String</b> The extension of the file
	 * @deprecated This method has been rendered obsolete 
	 * by {@code getFileProps}, which adds more options
	 * for getting file info from its name or path.
	 */
	@Deprecated
	public static String getExtension(String fileName){
		//Get the length of the file
		int fileLength = fileName.length();
		//Get the last character in the file name
		char fileCharPrefix = fileName.charAt(fileLength-1);
		
		//Check if that last character is null, a slash, or a dot or if the length is 0
		if(fileName==null || fileLength==0 || fileCharPrefix=='/' || fileCharPrefix=='\\' || fileCharPrefix=='.'){
			//Return nothing because the file has no extension, is a directory, or has no name at all
			return "";
		}
		
		//Get the location of the dot in the filename
		int dotInd = fileName.lastIndexOf('.'),
		//Get the location of the last slash in the filename
		sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

		//Check if the location of the dot is before any slash
		if(dotInd<=sepInd){
			//Return nothing because the file is most likely a directory
			return "";
		}
		else {
			//Return the extension of the file
			return fileName.substring(dotInd+1).toLowerCase();
		}
	}
	
	/**
	 * Get the name and extension of a file from its name
	 * or path on the file system. 
	 * @param filePath The name of the file or the path to
	 * it on the file system
	 * @return <b>ArrayList&lt;String&gt;</b> The name and 
	 * extension of the file, with the name being at position 
	 * 0 and the extension being at position 1 in the array.
	 */
	public static ArrayList<String> getFileProps(String filePath){
		//Create the container ArrayList
		ArrayList<String> fileProps = new ArrayList<>();
		
		//Create a new file object to represent the file path
		File fileObj = new File(filePath);
		
		//Get the name of the file
		String fileName = fileObj.getName();
		
		//Check if the length of the name is 0
		if(fileName.length() < 1){
			//Return a zero-length ArrayList because the file has a blank name (an empty or null path was supplied)
			//System.out.println("null file");
			return fileProps;
		}
		
		//Get the location of the last dot in the filename (if there is one)
		int dotInd = fileName.lastIndexOf('.');
			
		//Check if there is no dot in the filename and if any dot is not the end of the filename
		if((dotInd != -1) && (fileName.charAt(fileName.length() - 1) != '.')){
			//Separate the filename from the extension and add it to the ArrayList at position 0 (found by getting the string just before any dot in the filename)
			fileProps.add(0, fileName.substring(0, (dotInd)));
			
			//Get the file's extension (found by getting the string past the last dot in the path)
			String fileExt = (fileName.substring(dotInd + 1).toLowerCase());
			
			//Add the extension to the ArrayList at position 1
			fileProps.add(1, fileExt);
		}
		else {
			//Add the filename to the ArrayList (won't need to undergo separation from the extension since there is none)
			fileProps.add(0, fileName);
			
			//Set the extension as null, as there is no extension
			fileProps.add(1, null);
		}
		
		//Return the file properties as an ArrayList
		return fileProps;
	}
	
	/**
	 * Get the relative path of a {@code File} based on its
	 * absolute path
	 * @param absPath The relative path to the {@code File}
	 * @return <b>String</b> The resulting relative path
	 * @throws UnsupportedEncodingException If the URL generated from {@code Paths.get} is of an unsupported encoding
	 */
	public static String getRelativePath(String absPath) throws UnsupportedEncodingException {
		//Normalize the file path
		absPath = normalizePath(absPath);
		
		//Get the path from the input String and current execution directly
		Path absP = Paths.get(absPath);
		Path curDir = Paths.get(getExecPath());
		
		//Get the relative path using File and relativize
		String relPath = curDir.relativize(absP).normalize().toString();
		
		//Return the relative file path
		return relPath;
	}
	
	/**
	 * Load a generic non-resource file from the relative path specified as a {@code File}
	 * @param filePath The relative path of the file to load
	 * @return <b>File</b> The file as a {@code File} object
	 * @throws FileNotFoundException If the file path is invalid or the file wasn't found
	 * @throws IOException If there was a miscellaneous IO error while retrieving the {@code File}
	 * @see Resource#getResourceAsFileStream
	 */
	public static File loadFileAsFile(String filePath) throws FileNotFoundException, IOException {
		//Normalize the file path
		filePath = normalizePath(filePath);
		
		//Get the file as an InputStream
		InputStream streamIn = loadFileAsStream(filePath);
			
		//Check if the stream is null (file not found)
		if(streamIn == null){
			return null;
		}

		//Create the file, but mark it for deletion on exit because the file is only temporary
		File tempFile = File.createTempFile(String.valueOf(streamIn.hashCode()), ".tmp");
		tempFile.deleteOnExit();

		//Create a FileOutputStream
		FileOutputStream contentsOut = new FileOutputStream(tempFile);
		
		//Create the buffer and byte counter for the file writing process
		byte[] buffer = new byte[1024];
		int bytesRead;
		
		//Loop through the InputStream's contents
		while((bytesRead = streamIn.read(buffer)) != -1){
			//Write the contents of the stream byte-by-byte to the file
			contentsOut.write(buffer, 0, bytesRead);
		}
		
		//Return the resulting file and close the FileOutputStream
		contentsOut.close();
		return tempFile;
	}
	
	/**
	 * Load a generic non-resource file from the relative path specified as a {@code FileInputStream}
	 * @param filePath The relative path of the file to load
	 * @return <b>File</b> The file as a {@code FileInputStream}
	 * @throws IOException If an error occurred while fetching the file (usually an invalid path)
	 * @see Resource#getResourceAsFile
	 */
	public static FileInputStream loadFileAsFileStream(String filePath) throws IOException {
		//Normalize the file path
		filePath = normalizePath(filePath);
		
		//Create a FileInputStream from the file
		FileInputStream streamOut = new FileInputStream(loadFileAsFile(filePath));
	
		//Return the resulting FileInputStream
		return streamOut;
	}
	
	/**
	 * Load a generic non-resource file from the relative path specified as an {@code InputStream}
	 * @param filePath The relative path of the file to load
	 * @return <b>InputStream</b> The file as an {@code InputStream}
	 * @throws FileNotFoundException If the file path is invalid or the file wasn't found
	 */
	public static InputStream loadFileAsStream(String filePath) throws FileNotFoundException {
		//Normalize the file path
		filePath = normalizePath(filePath);
		
		//Check if the file exists on the system
		if(fileExists(filePath)){
			//Load the file
			File loadedFile = new File(filePath);
			
			//Create the InputStream
			InputStream fileStream = new FileInputStream(loadedFile);
			
			//Output the InputStream
			return fileStream;
		}
		else {
			//Return null since the file wasn't found
			return null;
		}
	}

	/**
	 * Retrieve the URL of a file as a {@code String}
	 * @param filePath The relative path of the file to load
	 * @return <b>String</b> The file's absolute URL as a {@code String}
	 * @throws UnsupportedEncodingException If the URL retrieved from resolving the file path contains unsupported encoding
	 */
	public static String loadFileAsStr(String filePath) throws UnsupportedEncodingException {
		//Normalize the file path
		filePath = normalizePath(filePath);
		
		//Get the file path as a URL, then convert the URL to a string
		String fileURL = NetUtil.urlToStr(loadFileAsURL(filePath));
		
		//Return the resulting string
		return fileURL;
	}
	
	/**
	 * Retrieve the absolute URL of a file
	 * @param filePath The relative path of the file to load
	 * @return <b>URL</b> The file's absolute URL
	 */
	public static URL loadFileAsURL(String filePath){
		//Normalize the file path
		filePath = normalizePath(filePath);
		
		//Get the URL of the file
		URL fileURL = FileUtil.class.getResource("/" + filePath);
		
		//Return the URL
		return fileURL;
	}
	
	/**
	 * Find lines in a {@code File} that match an input 
	 * {@code String}, and return all matching lines in
	 * a {@code HashMap} (line number, line value)
	 * @param tFile The {@code File} to find the lines in
	 * @param lineContent The line content to look for in the {@code File}
	 * @return <b>HashMap&lt;Integer, String&gt;</b> The matched lines in the {@code File}
	 * @throws IOException If an error occurred while fetching the file (usually an invalid path)
	 */
	public static HashMap<Integer, String> locateMatchingLines(File tFile, String lineContent) throws IOException {
		//Make sure the file exists
		if(tFile.exists()){
			//Ensure the file is indeed a file
			if(tFile.isFile()){
				//Get the absolute path to the file
				Path filePath = Paths.get(normalizePath(tFile.getAbsolutePath()));
						
				//Read in the file lines to a List
				List<String> fileLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
				
				//Create a HashMap that will eventually contain the matched lines
				HashMap<Integer, String> matchedLines = new HashMap<>();
				
				//Loop over the file lines
				for(int i=0; i<fileLines.size(); i++){
					//Check if current the file line matches the given string
					if(fileLines.get(i).equals(lineContent)){
						//Add the matched line and its position in the file to the HashMap
						matchedLines.put((i + 1), fileLines.get(i));
					}
				}
				
				//Return the populated HashMap
				return matchedLines;
			}
			else {
				//Throw an IOException because the file is a directory
				throw new IOException("The file " + tFile.getName() + " is a directory.");
			}
		}
		else {
			//Throw an IOException because no file exists at the provided path
			throw new IOException("The file at " + tFile.getPath() + " doesn't exist.");
		}
	}
	
	/**
	 * Normalize a file path to the standard path
	 * expected by the target OS
	 * @param pathIn The relative or absolute path to normalize
	 * @return <b>String</b> The normalized path
	 * @author GrzegorzDrozd
	 * @see <a href="https://gist.github.com/GrzegorzDrozd/8433939">https://gist.github.com/GrzegorzDrozd/8433939</a>
	 */
	public static String normalizePath(String pathIn){
		//Get the system path separator
		String sysSep = System.getProperty("file.separator");
		
		//Check if the separator is a backslash
		if(sysSep.equalsIgnoreCase("\\")){
			//Properly escape the separator to avoid NPEs in file copy operations
			sysSep = "\\\\";
		}
		
		//Replace both types of slash to the appropriate separator
		return pathIn.replaceAll("(\\\\|/){1,}", Matcher.quoteReplacement(sysSep));
	}
	
	/**
	 * Convert a file path to the standard path
	 * expected by Windows
	 * @param pathIn The relative or absolute path to convert
	 * @return <b>String</b> The resulting Windows path
	 */
	public static String unixToWinPath(String pathIn){
		//Replace all forward slashes to backward slashes, as Windows directories use a backward slash as the separator
		pathIn = pathIn.replaceAll("(/){1,}", "\\\\");
		
		//Return the UNIX path
		return pathIn;
	}
	
	/**
	 * Convert a file path to the standard path
	 * expected by UNIX
	 * @param pathIn The relative or absolute path to convert
	 * @return <b>String</b> The resulting UNIX path
	 */
	public static String winToUnixPath(String pathIn){
		//Replace all backslashes to forward slashes, as UNIX directories use a forward slash as the separator
		pathIn = pathIn.replaceAll("(\\\\){1,}", "/");
		
		//Return the UNIX path
		return pathIn;
	}
	
	/**
	 * Write a line to a {@code File} at the very end
	 * @param tFile The {@code File} to write the line to
	 * @param lineContent The line content to write
	 * @throws IOException If an error occurred while fetching the file (usually an invalid path)
	 */
	public static void writeAtEnd(File tFile, String lineContent) throws IOException {
		//Make sure the file exists
		if(tFile.exists()){
			//Ensure the file is indeed a file
			if(tFile.isFile()){				
				//Get the number of lines in the file
				int lastLine = countFileLines(tFile);
				
				//Run the writeAtPos method with position x being the end of the file
				writeAtPos(tFile, lineContent, lastLine);
			}
			else {
				//Throw an IOException because the file is a directory
				throw new IOException("The file " + tFile.getName() + " is a directory.");
			}
		}
		else {
			//Throw an IOException because no file exists at the provided path
			throw new IOException("The file at " + tFile.getPath() + " doesn't exist.");
		}
	}
	
	/**
	 * Write a line to a {@code File} at a specific
	 * point in that {@code File}
	 * @param tFile The {@code File} to write the line to
	 * @param lineContent The line content to write
	 * @param lineNum The position in the {@code File} to write the line
	 * @throws IOException If an error occurred while fetching the file (usually an invalid path)
	 */
	public static void writeAtPos(File tFile, String lineContent, int lineNum) throws IOException {
		//Make sure the file exists
		if(tFile.exists()){
			//Ensure the file is indeed a file
			if(tFile.isFile()){
				//Get the absolute path to the file
				Path filePath = Paths.get(normalizePath(tFile.getAbsolutePath()));
				
				//Read in the file lines to a List
				List<String> fileLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
				
				//Check if the line number is greater than the size of the List with the addition at position x accounted for
				if(lineNum > (fileLines.size() + 1)){
					//Add blank lines to the file to accommodate for the addition being out of bounds
					for(int i=(fileLines.size() + 1); i < lineNum; i++){
						//Append a blank line to the end of the file
						fileLines.add("");
					}
				}

				//Add the line to the List at position x
				fileLines.add((lineNum - 1), lineContent);
				
				//Write the lines to the file
				Files.write(filePath, fileLines, StandardCharsets.UTF_8);
			}
			else {
				//Throw an IOException because the file is a directory
				throw new IOException("The file " + tFile.getName() + " is a directory.");
			}
		}
		else {
			//Throw an IOException because no file exists at the provided path
			throw new IOException("The file at " + tFile.getPath() + " doesn't exist.");
		}
	}
}