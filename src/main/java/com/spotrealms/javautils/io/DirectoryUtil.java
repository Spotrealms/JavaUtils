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

//Import Java classes and dependencies
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * A series of methods for working with directories on the system.
 * Directories in Java are simply {@code File} objects that can hold
 * files and other directories. Working with directories in Java 
 * can be useful if a file or series of files need to be written to 
 * or read from. 
 * <br>This class has the following directory management features:
 * <ul>
 * 	<li>Directory creation ({@code createDirectory})</li>
 * 	<li>Directory existence checking ({@code dirExists})</li>
 * 	<li>Directory purging ({@code purgeDir})</li>
 * 	<li>Directory deletion ({@code purgeDir})</li>
 * </ul>
 * @author Spotrealms &amp; Contributors
 */
public class DirectoryUtil {
	/**
	 * Count the number of items in a given directory
	 * @param dirLocation The relative path of the directory to operate in
	 * @return <b>int</b> The number of files and sub-directories in the given directory
	 */
	public static int countInDir(String dirLocation){
		//Create a file object to represent the directory
		File cDir = new File(dirLocation);
		
		//Count the number of items in the directory
		int fileCount = cDir.list().length;
		
		//Return the number of items
		return fileCount;
	}
	
	/**
	 * Creates a directory at the specified relative path
	 * @param dirPath The relative path of the directory to create
	 * @param debugOut Show debug messages relating to the status of the directory creation operation
	 */
	public static void createDirectory(String dirPath, boolean debugOut){
		//Normalize the file path
		dirPath = FileUtil.normalizePath(dirPath);
		
		//Check if the directory exists
		if (!dirExists(dirPath)){
			//Create a directory object
			File dirPathObj = new File(dirPath);
			
			//Create the directory and assign it to a boolean for testing
			boolean mkdirSuccess = dirPathObj.mkdirs();
			
			//Test if the directory was successfully created
			if(mkdirSuccess){
				//The directory was created successfully
				if(debugOut){
					System.out.println("Directory \"" + dirPath + "\" was created successfully.");
				}
			}
			else {
				//The directory wasn't created
				if(debugOut){
					System.out.println("Directory \"" + dirPath + "\" could not be created.");
				}
			}
		}
		else {
			//The directory already exists
			if(debugOut){
				System.out.println("Directory \"" + dirPath + "\" already exists.");
			}
		}
	}
	
	/**
	 * Remove an entire directory or file along with its contents
	 * @param dirPath The target directory's relative path
	 */
	public static void deleteDir(String dirPath){
		//Normalize the file path
		dirPath = FileUtil.normalizePath(dirPath);
		
		//Create a file object to represent the directory
		File tDir = new File(dirPath);
		
		//Get the contents of the item if it's a directory
		File[] contents = tDir.listFiles();
		
		//Check if the directory contains any files
		if(contents != null){
			//Loop over each file in the directory
			for(File fileInDir : contents){
				//Check if the file is a symbolic link (UNIX/Linux/MacOS only) with a valid path (ensures symbolic links' pointers don't get deleted, as they can be outside the scope of the intended delete operation) 
				if(!(Files.isSymbolicLink(fileInDir.toPath()))){
					//Delete the file
					deleteDir(fileInDir.getPath());
				}
			}
		}
		//Delete the targeted item
		tDir.delete();
	}
	
	/**
	 * Check if a directory exists at the relative path specified
	 * @param dirLocation The relative path of the directory to check
	 * @return <b>boolean</b> The status of whether the directory exists or not
	 */
	public static boolean dirExists(String dirLocation){
		//Normalize the file path
		dirLocation = FileUtil.normalizePath(dirLocation);
		
		//Create a temporary file object for testing
		File dirPath = new File(dirLocation);
		
		//Check if the directory exists and is a valid directory
		if(dirPath.exists() && dirPath.isDirectory()){
			//The directory exists and it is valid
			return true;
		}
		else {
			//The directory doesn't exist or it isn't valid
			return false;
		}
	}
	
	/**
	 * Remove the contents of a directory, with extension exclusions
	 * @param tDir The target directory's relative path
	 * @param excludeExt A primitive array of Strings specifying any extensions to exclude (EXAMPLE: <code>new String[]{"jar", "log"}</code>)
	 * @param recursiveDel Traverse sub-directories in addition to the target directory
	 */
	public static void purgeDir(String tDir, String[] excludeExt, boolean recursiveDel){
		//Normalize the file path
		tDir = FileUtil.normalizePath(tDir);
		
		//Create the file object with the location of the target directory
		File tFile = new File(tDir);

		//Loop through each file in the directory
		for(File cFile: tFile.listFiles()){	
			//Check if the current file is another directory
			if(!(cFile.isDirectory())){
				//Get the current file's extension
				String cFileExt = FileUtil.getExtension(cFile.getName()).toLowerCase();

				//Check if the file has an excluded extension via stream ( >= JAVA SE 1.8 is required)
				if(!(Arrays.stream(excludeExt).anyMatch(cFileExt::equals))){
					//Delete the file
					cFile.delete();
				}
			}
			else {	
				//Check if recursive deletion is enabled
				if(recursiveDel){
					//Delete the files and directories in sub-directories (if present)
					deleteDir(cFile.getPath());
				}
			}
		}
	}
}