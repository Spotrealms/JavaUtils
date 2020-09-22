/*
 * JavaUtils: A collection of utility methods and classes for your Java programs
 *   Copyright (C) 2015-2020  Spotrealms Network
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

import com.spotrealms.javautils.misc.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

/**
 * A series of methods for working with files on the system.
 * Working with files in Java can be useful if a file 
 * needs to be written to or read from. 
 * <br>This class has the following file management and
 * file utility features:
 * <ul>
 * 	<li>File copying utility ({@code copy})</li>
 * 	<li>File line counter ({@code countfileLines})</li>
 * 	<li>File line deleter at position x ({@code delAtPos})</li>
 * 	<li>File existence checking ({@code fileExists})</li>
 * 	<li>Class execution path getter ({@code getExecPath})</li>
 * 	<li>File extension parsing ({@code getExtension} / <b>Deprecated!</b> Use {@code getFileProps} instead.)</li>
 * 	<li>File property parsing ({@code getFileProps})</li>
 *  <li>File hash computator ({@code getHash})</li>
 * 	<li>File relative path getter ({@code getRelativePath})</li>
 * 	<li>File line matcher ({@code findLines})</li>
 * 	<li>File path normalizer ({@code normalizePath})</li>
 *  <li>File line replacement ({@code replaceLines})</li>
 *  <li>File path piece derivation ({@code splitPathPieces})</li>
 * 	<li>UNIX file path converter ({@code unixToWinPath})</li>
 * 	<li>Windows file path converter ({@code winToUnixPath})</li>
 * 	<li>File line writer to end ({@code writeAtEnd})</li>
 * 	<li>File line writer at position x ({@code writeAtPos})</li>
 * </ul>
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
public final class FileUtil {
	/**
	 * Defines a list of all available comment
	 * line markers that are "regex safe", or
	 * their usage in files isn't ambiguous to
	 * regex matchers.
	 *
	 * @see CommentLines
	 */
	public static final ArrayList<String> COMMENT_LINES = (ArrayList<String>) Collections.unmodifiableList(Arrays.asList(
		CommentLines.ADA.toString(),
		CommentLines.BASIC.toString(),
		CommentLines.BATCH.toString(),
		CommentLines.CISCO.toString(),
		CommentLines.CPP.toString(),
		CommentLines.LATEX.toString(),
		CommentLines.PERL.toString(),
		CommentLines.PYTHON.toString()
	));

	/**
	 * Prevents instantiation of the utility class FileUtil.
	 *
	 * @throws RuntimeException If instantiation occurs
	 */
	private FileUtil(){ throw new RuntimeException("No " + this.getClass().getSimpleName() + " instance for you :)"); }

	/**
	 * Defines comment line markers used by a
	 * variety of different programming and markup
	 * languages. <br>Defined at the following page:
	 * <a href="https://en.wikipedia.org/wiki/Comment_(computer_programming)#Examples" target="_blank">
	 *  https://en.wikipedia.org/wiki/Comment_(computer_programming)#Examples
	 * </a>
	 */
	public enum CommentLines {
		//Define the comment line sequences
		/** Comment line in ADA. **/
		ADA("--"),
		/** Comment line in BASIC. **/
		BASIC("REM"),
		/** Comment line in BATCH. **/
		BATCH("::"),
		/** Comment line in CISCO. **/
		CISCO("!"),
		/** Comment line in C++ and languages with similar syntax (eg: Java, JS, C#, etc). **/
		CPP("//"),
		/** Comment line in FORTRAN. **/
		FORTRAN4("C"),
		/** Comment line in LATEX. **/
		LATEX("%"),
		/** Comment line in PERL and plaintext files. **/
		PERL("#"),
		/** Comment line in PYTHON. **/
		PYTHON("\"\"\"");

		//Get the enum values
		private final String cLine;
		CommentLines(final String envCLine){
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
	 * Wrapper for the built-in {@code Files.copy()} method
	 * that adds error checks with a unified exception if
	 * anything goes south. NOTE: This method requires 
	 * Java SE &gt;= 1.7 to run
	 *
	 * @param sourceFile The path to the input {@code File} to copy, including the filename
	 * @param destFile The path to the destination {@code File}, including the filename
	 * @throws IOException If the path to the {@code File} is invalid
	 */
	public static void copy(final String sourceFile, final String destFile) throws IOException {
		//Create file objects for each file represented by the input strings
		File srcFile = new File(sourceFile);
		File destiFile = new File(destFile);
		
		//Try to copy the input file
		try {
			//Copy the file using Files.copy() (Java SE 1.7+ is required)
			Files.copy(Paths.get(srcFile.getPath()), Paths.get(destiFile.getPath()), StandardCopyOption.REPLACE_EXISTING);
		} 
		catch(Exception ex){
			//Throw an IOException containing the details of whatever exception was caught
			throw new IOException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Count the number of lines a given {@code File}
	 * contains.
	 *
	 * @param tFile The {@code File} to count the lines in
	 * @return <b>int</b> The number of lines in the {@code File}
	 * @throws IOException If the path to the {@code File} is invalid
	 */
	public static int countFileLines(final File tFile) throws IOException {
		//Make sure the file exists
		if(tFile.exists()){
			//Ensure the file is indeed a file
			if(tFile.isFile()){
				//Get the absolute path to the file
				Path filePath = Paths.get(normalizePath(tFile.getAbsolutePath()));
						
				//Read in the file lines to a List
				List<String> fileLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
				
				//Return the number of lines in the file
				return fileLines.size() + 1;
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
	 * point in that {@code File}.
	 *
	 * @param tFile The {@code File} to write the line to
	 * @param lineNum The position in the {@code File} to delete
	 * @throws IOException If the path to the {@code File} is invalid
	 */
	public static void delAtPos(final File tFile, final int lineNum) throws IOException {
		//Make sure the file exists
		if(tFile.exists()){
			//Ensure the file is indeed a file
			if(tFile.isFile()){
				//Get the absolute path to the file
				Path filePath = Paths.get(normalizePath(tFile.getAbsolutePath()));
				
				//Read in the file lines to a List
				List<String> fileLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
				
				//Remove the line from the List at position x
				fileLines.remove(lineNum - 1);
				
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
	 * Check if a file exists at the relative path specified.
	 *
	 * @deprecated Use File.exists() instead
	 * @param fileLocation The relative path of the file to check
	 * @return <b>boolean</b> The status of whether the file exists or not
	 */
	@Deprecated
	public static boolean fileExists(final String fileLocation){
		//Initialization by normalizing the file path
		String normedPath = FileUtil.normalizePath(fileLocation);
		
		//Create a temporary file object for testing
		File filePath = new File(normedPath);
		
		//Check if the file exists and is a valid file and return the result
		return filePath.exists() && filePath.isFile();
	}
	
	/**
	 * Get the execution directory of the class as a {@code String}.
	 *
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
		if(os.contains("Windows")){
			//Properly escape the backslash path separator
			configPath = configPath.replace("/", "\\\\");

			//Check if the path is a URL
			if(configPath.contains("file:\\\\")){
				//Escape the backslash path separator
				configPath = configPath.replace("file:\\\\", "");
			}
		}
		//Check if the path is a URL
		else if(configPath.contains("file:")){
			//Get rid of "file:"
			configPath = configPath.replace("file:", "");
		}
		
		//Return the resulting absolute path
		return StringUtil.decodeUrl(configPath);
	}

	//TODO: Tuples here???
	/**
	 * Get the name and extension of a file from its name
	 * or path on the file system.
	 *
	 * @param filePath The name of the file or the path to
	 *      it on the file system
	 * @return <b>ArrayList&lt;String&gt;</b> The name and 
	 *      extension of the file, with the name being at position
	 *      0 and the extension being at position 1 in the array.
	 */
	public static ArrayList<String> getFileProps(final String filePath){
		//Create the container ArrayList
		ArrayList<String> fileProps = new ArrayList<>();
		
		//Create a new file object to represent the file path
		File fileObj = new File(filePath);
		
		//Get the name of the file
		String fileName = fileObj.getName();
		
		//Check if the length of the name is 0
		if(fileName.length() < 1){
			//Return a zero-length ArrayList because the file has a blank name (an empty or null path was supplied)
			return fileProps;
		}
		
		//Get the location of the last dot in the filename (if there is one)
		int dotInd = fileName.lastIndexOf('.');
			
		//Check if there is no dot in the filename and if any dot is not the end of the filename
		if(dotInd != -1 && fileName.charAt(fileName.length() - 1) != '.'){
			//Separate the filename from the extension and add it to the ArrayList at position 0
			//(found by getting the string just before any dot in the filename)
			fileProps.add(0, fileName.substring(0, dotInd));
			
			//Get the file's extension (found by getting the string past the last dot in the path)
			String fileExt = fileName.substring(dotInd + 1).toLowerCase(Locale.ROOT);
			
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
	 * Computes the hash of a {@code File} given a specific
	 * {@code MessageDigest} type.
	 *
	 * @param mDigest The hashing algorithm to use on the file
	 * @param targetFile The target {@code File} to compute the hash of
	 * @return <b>String</b> The resulting hash of the input {@code File}
	 * @throws IOException Thrown if there was an error reading the {@code File}
	 */
	public static String getHash(final MessageDigest mDigest, final File targetFile) throws IOException {
		//Define the "magic numbers"
		final int KB_SIZE = 1024;
		final int DIGEST_UP = 0xff;
		final int DIGEST_DOWN = 0x100;
		final int STRING_RADIX = 16;

		//Get file input stream for reading the file content
		try(FileInputStream fileIn = new FileInputStream(targetFile)){

			//Create byte array to read data in chunks
			byte[] byteArray = new byte[KB_SIZE];
			int bytesCount;

			//Read file data and update in message digest
			while((bytesCount = fileIn.read(byteArray)) != -1){
				//Update the digest's file bytes
				mDigest.update(byteArray, 0, bytesCount);
			}
		}
		 
		//Get the hash's bytes
		byte[] digestBytes = mDigest.digest();
		 
		//Create a StringBuilder to hold the file's hash
		StringBuilder fileHash = new StringBuilder();
		
		//Loop over the message digest byte array
		for(byte digestByte : digestBytes){
			//Convert the byte to hexadecimal and append it onto the StringBuilder
			fileHash.append(Integer.toString((digestByte & DIGEST_UP) + DIGEST_DOWN, STRING_RADIX).substring(1));
		}
		 
		//Return complete hash as a string
		return fileHash.toString();
	}
	
	/**
	 * Get the relative path of a {@code File} based on its
	 * absolute path.
	 *
	 * @param absPath The relative path to the {@code File}
	 * @return <b>String</b> The resulting relative path
	 * @throws UnsupportedEncodingException If the URL generated from {@code Paths.get} is of an unsupported encoding
	 */
	public static String getRelativePath(final String absPath) throws UnsupportedEncodingException {
		//Get the path from the input String and current execution directly
		Path absP = Paths.get(absPath);
		Path curDir = Paths.get(getExecPath());

		//Return the relative file path
		return curDir.relativize(absP).normalize().toString();
	}
	
	/**
	 * Load a generic non-resource file from the relative path specified as a {@code FileInputStream}.
	 *
	 * @param filePath The relative path of the file to load
	 * @return <b>File</b> The file as a {@code FileInputStream}
	 * @throws IOException If an error occurred while fetching the file (usually an invalid path)
	 * @see Resource#getResourceAsFile
	 */
	public static FileInputStream loadFileAsFileStream(final String filePath) throws IOException {
		//Initialization by normalizing the file path
		String normedPath = FileUtil.normalizePath(filePath);

		//Return the resulting FileInputStream
		return new FileInputStream(new File(normedPath));
	}
	
	/**
	 * Load a generic non-resource file from the relative path specified as an {@code InputStream}.
	 *
	 * @param filePath The relative path of the file to load
	 * @return <b>InputStream</b> The file as an {@code InputStream}
	 * @throws FileNotFoundException If the file path is invalid or the file wasn't found
	 */
	public static InputStream loadFileAsStream(final String filePath) throws FileNotFoundException {
		//Initialization by normalizing the file path
		String normedPath = FileUtil.normalizePath(filePath);
		
		//Check if the file exists on the system
		if(fileExists(normedPath)){
			//Load the file
			File loadedFile = new File(normedPath);

			//Output the InputStream
			return new FileInputStream(loadedFile);
		}
		else {
			//Return null since the file wasn't found
			return null;
		}
	}

	/**
	 * Retrieve the URL of a file as a {@code String}.
	 *
	 * @param filePath The relative path of the file to load
	 * @return <b>String</b> The file's absolute URL as a {@code String}
	 * @throws UnsupportedEncodingException If the URL retrieved from resolving the file path contains unsupported encoding
	 */
	public static String loadFileAsStr(final String filePath) throws UnsupportedEncodingException {
		//Initialization by normalizing the file path
		String normedPath = FileUtil.normalizePath(filePath);
		
		//Get the file path as a URL, then convert the URL to a string and return it
		return StringUtil.decodeUrl(String.valueOf(loadFileAsUrl(normedPath)));
	}
	
	/**
	 * Retrieve the absolute URL of a file.
	 *
	 * @param filePath The relative path of the file to load
	 * @return <b>URL</b> The file's absolute URL
	 */
	public static URL loadFileAsUrl(final String filePath){
		//Initialization by normalizing the file path
		String normedPath = FileUtil.normalizePath(filePath);
		
		//Get the URL of the file
		return FileUtil.class.getResource("/" + normedPath);
	}
	
	/**
	 * Find lines in a {@code File} that match an input 
	 * {@code String}, and return all matching lines in
	 * a {@code HashMap} (line number, line value).
	 *
	 * @param tFile The {@code File} to find the lines in
	 * @param lineContent The line content to look for in the {@code File}
	 * @param useRegex Set whether or not {@code lineContent} should be treated as a regex
	 * @param ignoreComments Set whether or not to include comments in the output {@code HashMap}
	 * @return <b>HashMap&lt;Integer, String&gt;</b> The matched lines in the {@code File}
	 * @throws IOException If an error occurred while fetching the file (usually an invalid path)
	 */
	public static HashMap<Integer, String> findLines(
			final File tFile,
			final String lineContent,
			final boolean useRegex,
			final boolean ignoreComments
	) throws IOException {
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
				for(int i = 0; i < fileLines.size(); i++){
					//Get the status as to whether the current line is an exact match
					boolean exactMatch = fileLines.get(i).equals(lineContent);

					//Get the status as to whether the current line is a regex match
					boolean regexMatch = StringUtil.matchesRegex(fileLines.get(i), lineContent);

					//Get the status as to whether the current line is a comment line
					boolean commentLine = StringUtil.equalsAny(fileLines.get(i), COMMENT_LINES, false, true);

					//Check if regexes should be used
					if(useRegex){
						//Check if the current file line has a regex match
						if(regexMatch){
							//Add the matched line and its position in the file to the HashMap
							matchedLines.put(i + 1, fileLines.get(i));
						}
					}
					else {
						//Check if current the file line is an exact match
						if(exactMatch){
							//Add the matched line and its position in the file to the HashMap
							matchedLines.put(i + 1, fileLines.get(i));
						}
					}
					//Check if the current line is a comment line and that comments shouldn't be ignored
					if(commentLine && ignoreComments){
						//Remove the corresponding element from the HashMap
						matchedLines.remove(i + 1);
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
	 * expected by the target OS.
	 *
	 * @author GrzegorzDrozd
	 * @param pathIn The relative or absolute path to normalize
	 * @return <b>String</b> The normalized path
	 * @see <a href="https://gist.github.com/GrzegorzDrozd/8433939">https://gist.github.com/GrzegorzDrozd/8433939</a>
	 */
	public static String normalizePath(final String pathIn){
		//Get the system path separator
		String sysSep = System.getProperty("file.separator");
		
		//Check if the separator is a backslash
		if(sysSep.equalsIgnoreCase("\\")){
			//Properly escape the separator to avoid NPEs in file copy operations
			sysSep = "\\\\";
		}
		
		//Replace both types of slash to the appropriate separator
		return pathIn.replaceAll("(\\|/)+", Matcher.quoteReplacement(sysSep));
	}
	
	/**
	 * Replace all lines in a {@code File} that match
	 * an input {@code String}.
	 *
	 * @param tFile The {@code File} to find the lines in
	 * @param charset The charset to use when loading the files for reading and writing
	 * @param replaceTarget The line content to look for in the {@code File}
	 * @param replaceWith The line content to replace the matches with
	 * @param useRegex Set whether or not {@code lineContent} should be treated as a regex
	 * @param ignoreComments Set whether or not to include comments in the output {@code HashMap}
	 * @throws IOException If an error occurred while fetching the file (usually an invalid path)
	 */
	public static void replaceLines(
			final File tFile,
			final Charset charset,
			final String replaceTarget,
			final String replaceWith,
			final boolean useRegex,
			final boolean ignoreComments
	) throws IOException {
		//Start the replace process
		try(FileOutputStream fileOut = new FileOutputStream(tFile.getPath())){
			//Create a BufferedReader for the original file
			//BufferedReader origFile = Files.newBufferedReader(Paths.get(tFile.getPath()), charset);

			//Create a StringBuffer to hold the new file
			StringBuilder inputBuffer = new StringBuilder();

			//Get a HashMap of all the matches
			HashMap<Integer, String> allMatches = findLines(tFile, replaceTarget, useRegex, ignoreComments);

			//Read in the target file lines to a List
			List<String> fileLines = Files.readAllLines(Paths.get(tFile.getPath()), charset);

			//Loop over the file lines
			for(int i = 0; i < fileLines.size(); i++){
				//Get the current file line
				String fileLine = fileLines.get(i);

				//Check if the current file line is listed in the match HashMap
				if(allMatches.get(i + 1) != null){
					//Replace the regex targets with the string to replace it with
					fileLine = fileLine.replaceAll(replaceTarget, replaceWith);
				}

				//Append the current line along with a line break onto the new file StringBuffer
				inputBuffer.append(fileLine).append('\n');
			}

			//Overwrite the input file
			fileOut.write(inputBuffer.toString().getBytes(charset));
		}
	}
	
	/**
	 * Derive path information such as filename and
	 * containing directory from a full file path.
	 *
	 * @param pathIn The path to derive the info from
	 * @return <b>ArrayList&lt;String&gt;</b> The {@code ArrayList} holding the path information, with the first element holding
	 *      the containing directory and the second element holding the filename
	 */
	public static ArrayList<String> splitPathPieces(final String pathIn){
		//Get the last index of any forward slash in the path and convert all backward slashes to forward slashes (removes unnecessary overhead)
		int lastSlash = FileUtil.winToUnixPath(pathIn).lastIndexOf('/');

		//Create strings to hold the containing directory for the given path
		String pathDir = "";
		
		//Check if the last slash location is greater than 0
		if(lastSlash >= 0){
			//Add the containing directory onto its string ending at the last slash in the path
			pathDir = pathIn.substring(0, lastSlash);
		}
		
		//Get the end file in the path from the last slash to the end of the path
		String pathFile = pathIn.substring(lastSlash + 1);
		
		//Return an ArrayList containing the elements
		return new ArrayList<>(Arrays.asList(pathDir, pathFile));
	}
	
	/**
	 * Convert a file path to the standard path
	 * expected by Windows.
	 *
	 * @param pathIn The relative or absolute path to convert
	 * @return <b>String</b> The resulting Windows path
	 */
	public static String unixToWinPath(final String pathIn){
		//Replace all forward slashes to backward slashes, as Windows directories use a backward slash as the separator and return the Windows path
		return pathIn.replaceAll("(/)+", "\\\\");
	}
	
	/**
	 * Convert a file path to the standard path
	 * expected by UNIX.
	 *
	 * @param pathIn The relative or absolute path to convert
	 * @return <b>String</b> The resulting UNIX path
	 */
	public static String winToUnixPath(final String pathIn){
		//Replace all backslashes to forward slashes, as UNIX directories use a forward slash as the separator and return the UNIX path
		return pathIn.replaceAll("(\\\\)+", "/");
	}
	
	/**
	 * Write a line to a {@code File} at the very end.
	 *
	 * @param tFile The {@code File} to write the line to
	 * @param lineContent The line content to write
	 * @throws IOException If an error occurred while fetching the file (usually an invalid path)
	 */
	public static void writeAtEnd(final File tFile, final String lineContent) throws IOException {
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
	 * point in that {@code File}.
	 *
	 * @param tFile The {@code File} to write the line to
	 * @param lineContent The line content to write
	 * @param lineNum The position in the {@code File} to write the line
	 * @throws IOException If an error occurred while fetching the file (usually an invalid path)
	 */
	public static void writeAtPos(final File tFile, final String lineContent, final int lineNum) throws IOException {
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
					for(int i = fileLines.size() + 1; i < lineNum; i++){
						//Append a blank line to the end of the file
						fileLines.add("");
					}
				}

				//Add the line to the List at position x
				fileLines.add(lineNum - 1, lineContent);
				
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
