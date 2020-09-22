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

import com.spotrealms.javautils.exception.NotDeletedException;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Locale;

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
 *
 * @author Spotrealms &amp; Contributors
 */
public final class DirectoryUtil {
	/**
	 * Prevents instantiation of the utility class DirectoryUtil.
	 *
	 * @throws RuntimeException If instantiation occurs
	 */
	private DirectoryUtil(){ throw new RuntimeException("No " + this.getClass().getSimpleName() + " instance for you :)"); }

	/**
	 * Count the number of items in a given directory.
	 *
	 * @param dirLocation The relative path of the directory to operate in
	 * @return <b>int</b> The number of files and sub-directories in the given directory
	 * @deprecated Use {@code new File(myDirectory).list().length}
	 */
	@Deprecated
	public static int countInDir(final String dirLocation){
		//Create a file object to represent the directory
		File cDir = new File(dirLocation);

		//Get the list of files
		String[] list = cDir.list();

		//Ensure the list isn't null
		if(list != null){
			//Return the number of items
			return list.length;
		}
		else {
			//Return 0 because the array is null
			return 0;
		}
	}

	//Possible replacement with SLF4J here
	/**
	 * Creates a directory at the specified relative path.
	 *
	 * @param dirPath The relative path of the directory to create
	 * @param debugOut Show debug messages relating to the status of the directory creation operation
	 */
	public static void createDirectory(final String dirPath, final boolean debugOut){
		//Initialization by normalizing the file path
		String normedPath = FileUtil.normalizePath(dirPath);
		
		//Check if the directory exists
		if(!dirExists(normedPath)){
			//Create a directory object
			File dirPathObj = new File(normedPath);
			
			//Create the directory and assign it to a boolean for testing
			boolean mkdirSuccess = dirPathObj.mkdirs();
			
			//Test if the directory was successfully created
			if(mkdirSuccess){
				//The directory was created successfully
				if(debugOut){
					System.out.println("Directory \"" + normedPath + "\" was created successfully.");
				}
			}
			else {
				//The directory wasn't created
				if(debugOut){
					System.out.println("Directory \"" + normedPath + "\" could not be created.");
				}
			}
		}
		else {
			//The directory already exists
			if(debugOut){
				System.out.println("Directory \"" + normedPath + "\" already exists.");
			}
		}
	}
	
	/**
	 * Remove an entire directory or file along with its contents.
	 *
	 * @param dirPath The target directory's relative path
	 * @return <b>boolean</b> Whether or not the directory was successfully deleted
	 */
	public static boolean deleteDir(final String dirPath){
		//Initialization by normalizing the file path
		String normedPath = FileUtil.normalizePath(dirPath);
		
		//Create a file object to represent the directory
		File tDir = new File(normedPath);
		
		//Get the contents of the item if it's a directory
		File[] contents = tDir.listFiles();
		
		//Check if the directory contains any files
		if(contents != null){
			//Loop over each file in the directory
			for(File fileInDir : contents){
				//Check if the file is a symbolic link (UNIX/Linux/MacOS only) with a valid path
				//(ensures symbolic links' pointers don't get deleted, as they can be outside the scope of the intended delete operation)
				if(!(Files.isSymbolicLink(fileInDir.toPath()))){
					//Delete the file
					deleteDir(fileInDir.getPath());
				}
			}
		}

		//Delete the targeted item and return the status of whether the item was deleted
		return tDir.delete();
	}
	
	/**
	 * Check if a directory exists at the relative path specified.
	 *
	 * @param dirLocation The relative path of the directory to check
	 * @return <b>boolean</b> The status of whether the directory exists or not
	 */
	public static boolean dirExists(final String dirLocation){
		//Initialization by normalizing the file path
		String normedPath = FileUtil.normalizePath(dirLocation);
		
		//Create a temporary file object for testing
		File dirPath = new File(normedPath);
		
		//Check if the directory exists and is a valid directory and return the result
		return dirPath.exists() && dirPath.isDirectory();
	}
	
	/**
	 * Remove the contents of a directory, with extension exclusions.
	 *
	 * @param tDir The target directory's relative path
	 * @param excludeExt A primitive array of Strings specifying any extensions to exclude (EXAMPLE: <code>new String[]{"jar", "log"}</code>)
	 * @param recursiveDel Traverse sub-directories in addition to the target directory
	 */
	@SuppressWarnings("PMD.CollapsibleIfStatements") //PMD suppression for line 197-200 as well as 204-207
	public static void purgeDir(final String tDir, final String[] excludeExt, final boolean recursiveDel){
		//Initialization by normalizing the file path
		String normedPath = FileUtil.normalizePath(tDir);
		
		//Create the file object with the location of the target directory
		File tFile = new File(normedPath);

		//Get the file list
		File[] fileList = tFile.listFiles();

		//Ensure the list has at least one item in it
		if(fileList != null && fileList.length >= 1){
			//Loop through each file in the directory
			for(File cFile : fileList){
				//Check if the current file is another directory
				if(!(cFile.isDirectory())){
					//Get the current file's extension
					String cFileExt = FileUtil.getFileProps(cFile.getName()).get(1).toLowerCase(Locale.ROOT);

					//Check if the file has an excluded extension via stream ( >= JAVA SE 1.8 is required)
					if(Arrays.stream(excludeExt).noneMatch(cFileExt::equals)){
						//Delete the file and check if the file was actually deleted and throw an exception otherwise
						if(!cFile.delete()) throw new NotDeletedException("File " + cFile + " couldn't be deleted");
					}
				}
				else {
					//Check if recursive deletion is enabled
					if(recursiveDel){
						//Delete the files and directories in sub-directories (if present) and ensure the directory was deleted
						if(!deleteDir(cFile.getPath())) throw new NotDeletedException("Directory " + cFile + " couldn't be deleted");
					}
				}
			}
		}
	}
	
	/**
	 * Remove the contents of a directory, with extension exclusions.
	 *
	 * @param tDir The target directory's relative path
	 * @param recursiveDel Traverse sub-directories in addition to the target directory
	 */
	public static void purgeDir(final String tDir, final boolean recursiveDel){
		//Redirect to the overloaded method with the missing parameters being filled by empty sets
		purgeDir(tDir, new String[]{}, recursiveDel);
	}
}
