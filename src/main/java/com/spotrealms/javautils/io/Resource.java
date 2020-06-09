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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * A series of methods for working with resource files inside JAR files. 
 * Resource files are any file inside a JAR file or on the classpath that 
 * may or may not be utilized by the entire program. Working with resource 
 * files can be useful if hard-coded data must be supplied. Examples may 
 * include localization strings, default configurations, fonts, images, etc.
 * The methods included can both import and export data from JAR files.
 * <br>This class includes the following export options:
 * <ul>
 * 	<li>{@link File}</li>
 * </ul>
 * This class also includes the following import options:
 * <ul>
 * 	<li>{@link File}</li>
 * 	<li>{@link FileInputStream}</li>
 * 	<li>{@link InputStream}</li>
 * 	<li>{@link String}</li>
 * 	<li>{@link URL}</li>
 * </ul>
 * Finally, the following generic helper methods also exist that relate 
 * to classpath files:
 * <ul>
 * 	<li>{@link resouceFileExists}</li>
 * </ul>
 * 
 * @author Spotrealms
 */
public class Resource {		
	/**
	 * Export a resource from within a JAR file ({@code src/main/resources}) to a specified path
	 * @param execClass The class that this method is being executed from (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param resourcePath The relative path to the resource and its name
	 * @param resourceDropLocation The relative location to drop the extracted resource and its file name
	 * @param <T> Allow generic types and objects to be used
	 * @throws IOException If an error occurred while fetiching the resource (usually an invalid resource path)
	 */
	public static <T> void exportResource(Class<T> execClass, String resourcePath, String resourceDropLocation) throws IOException {
		//Initialization
		InputStream resStreamIn = null;
		
		//Get the current working directory
		String curDir = new File("").getAbsolutePath();
		
		//Convert the resource file path to its UNIX counterpart, as getResourceAsStream will be null if the path includes backslashes
		resourcePath = FileUtil.winToUnixPath(resourcePath);
		
		//Normalize the file path of the drop location
		resourceDropLocation = FileUtil.normalizePath(curDir + "/" + resourceDropLocation);
		
		//Get the resource as an InputStream from inside the JAR file using the classloader
		resStreamIn = execClass.getResourceAsStream("/" + resourcePath);
		
		//Create the output file
		File fileOut = new File(resourceDropLocation);
		
		//Copy the resource to the output destination
		Files.copy(resStreamIn, fileOut.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	/**
	 * Import a resource from within a JAR file ({@code src/main/resources}) as a {@code File}
	 * @param execClass The class that this method is being executed from (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param resourcePath The relative path to the resource and its name
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>File</b> The resource as a {@code File}
	 * @throws IOException If an error occurred while fetching the resource (usually an invalid resource path)
	 * @see Resource#getResourceAsFileStream
	 */
	public static <T> File getResourceAsFile(Class<T> execClass, String resourcePath) throws IOException {
		//Convert the resource file path to its UNIX counterpart, as getResourceAsStream will be null if the path includes backslashes
		resourcePath = FileUtil.winToUnixPath(resourcePath);

		//Create a file object to store the temporary file's data
		File tempFile = null;

		//Derive an InputStream from the class resource and begin the export process
		try(InputStream streamIn = getResourceAsStream(execClass, resourcePath)){
			//Create the file, but mark it for deletion on exit because the file is only temporary
			tempFile = (File.createTempFile(String.valueOf(streamIn.hashCode()), ".tmp"));
			tempFile.deleteOnExit();

			//Convert the InputStream to file and copy its contents to the temporary file
			Files.copy(streamIn, Paths.get(tempFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
		} 
		catch(IOException ex){
			//Return null if there was an error
			return null;
		}

		//Return the resulting file
		return tempFile;
	}
	
	/**
	 * Import a resource from within a JAR file ({@code src/main/resources}) as a {@code FileInputStream}
	 * @param execClass The class that this method is being executed from (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param resourcePath The relative path to the resource and its name
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>File</b> The resource as a {@code FileInputStream}
	 * @throws IOException If an error occurred while fetching the resource (usually an invalid resource path)
	 * @see Resource#getResourceAsFile
	 */
	public static <T> FileInputStream getResourceAsFileStream(Class<T> execClass, String resourcePath) throws IOException {
		//Convert the resource file path to its UNIX counterpart, as getResourceAsStream will be null if the path includes backslashes
		resourcePath = FileUtil.winToUnixPath(resourcePath);
		
		//Create a FileInputStream from the file
		FileInputStream streamOut = new FileInputStream(getResourceAsFile(execClass, resourcePath));
	
		//Return the resulting FileInputStream
		return streamOut;
	}
	
	/**
	 * Import a resource from within a JAR file ({@code src/main/resources}) as an {@code InputStream}
	 * @param execClass The class that this method is being executed from (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param resourcePath The relative path to the resource and its name
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>InputStream</b> The resource as an {@code InputStream}
	 */
	public static <T> InputStream getResourceAsStream(Class<T> execClass, String resourcePath){
		//Convert the resource file path to its UNIX counterpart, as getResourceAsStream will be null if the path includes backslashes
		resourcePath = FileUtil.winToUnixPath(resourcePath);
		
		//Open the resource file as an InputStream
		InputStream resourceFileIn = execClass.getResourceAsStream("/" + resourcePath);
		
		//Return the resulting resource as an InputStream
		return resourceFileIn;
	}
	
	/**
	 * Retrieve the URL of a resource from within a JAR file ({@code src/main/resources}) as a {@code String}
	 * @param execClass The class that this method is being executed from (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param resourcePath The relative path to the resource and its name
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>String</b> The resource file's absolute URL inside the JAR file as a {@code String}
	 * @throws UnsupportedEncodingException If the URL used inside the method is encoded with an unsupported encoding scheme
	 */
	public static <T> String getResourceAsStr(Class<T> execClass, String resourcePath) throws UnsupportedEncodingException {
		//Convert the resource file path to its UNIX counterpart, as getResourceAsStream will be null if the path includes backslashes
		resourcePath = FileUtil.winToUnixPath(resourcePath);
		
		//Get the resource path as a URL, then convert the URL to a string
		String fileURL = NetUtil.urlToStr(getResourceAsURL(execClass, resourcePath));
		
		//Return the resulting string
		return fileURL;
	}
	
	/**
	 * Retrieve the URL of a resource from within a JAR file ({@code src/main/resources})
	 * @param execClass The class that this method is being executed from (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param resourcePath The relative path to the resource and its name
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>URL</b> The resource file's absolute URL inside the JAR file
	 */
	public static <T> URL getResourceAsURL(Class<T> execClass, String resourcePath){
		//Convert the resource file path to its UNIX counterpart, as getResourceAsStream will be null if the path includes backslashes
		resourcePath = FileUtil.winToUnixPath(resourcePath);
		
		//Get the URL of the resource
		URL fileURL = execClass.getResource("/" + resourcePath);
		
		//Return the URL
		return fileURL;
	}
	
	/**
	 * Check if a resource file exists on the classpath of a JAR or set of class files
	 * @param execClass The class that this method is being executed from (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param resourcePath The relative path to the resource and its name
	 * @param <T> Allow generic types and objects to be used
	 * @return <b>boolean</b> The status of whether or not the input {@code File} exists
	 */
	public static <T> boolean resouceFileExists(Class<T> execClass, String resourcePath){
		//Create a new file object for later
		File resFile;
		
		//Try to get the resource as a file
		try {
			//Get the resource as a file
			resFile = getResourceAsFile(execClass, resourcePath);
			
			//Check if the file is an actual file and not a directory
			if(resFile.isFile()){
				//Return true because the file was found and is a file
				return true;
			}
			else {
				//Return false because the resource at the location is a directory
				return false;
			}
		}
		catch(IOException notFound){
			//Return false because an error occurred
			return false;
		}
	}
}