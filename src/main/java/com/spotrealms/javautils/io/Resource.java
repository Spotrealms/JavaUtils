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
 * 	<li>{@link Resource#resourceFileExists}</li>
 * </ul>
 *
 * @author Spotrealms
 */
public final class Resource {
	/**
	 * Prevents instantiation of the utility class Resource.
	 *
	 * @throws RuntimeException If instantiation occurs
	 */
	private Resource(){ throw new RuntimeException("No " + this.getClass().getSimpleName() + " instance for you :)"); }

	/**
	 * Export a resource from within a JAR file ({@code src/main/resources}) to a specified path.
	 *
	 * @param execClass The class that this method is being executed from (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param resourcePath The relative path to the resource and its name
	 * @param resourceDropLocation The relative location to drop the extracted resource and its file name
	 * @param copyOption What to do if there's a conflict issue
	 * @throws IOException If an error occurred while fetching the resource (usually an invalid resource path)
	 */
	public static void exportResource(
			final Class<?> execClass,
			final String resourcePath,
			final String resourceDropLocation,
			final StandardCopyOption copyOption
	) throws IOException {
		//Get the current working directory
		String curDir = new File("").getAbsolutePath();

		//Get the resource as an InputStream from inside the JAR file using the classloader
		InputStream resStreamIn = execClass.getResourceAsStream("/" + FileUtil.winToUnixPath(resourcePath));
		
		//Create the output file
		File fileOut = new File(FileUtil.normalizePath(curDir + "/" + resourceDropLocation));
		
		//Copy the resource to the output destination
		Files.copy(resStreamIn, fileOut.toPath(), copyOption);
	}
	
	/**
	 * Import a resource from within a JAR file ({@code src/main/resources}) as a {@code File}.
	 *
	 * @param execClass The class that this method is being executed from (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param resourcePath The relative path to the resource and its name
	 * @param copyOption What to do if there's a conflict issue
	 * @return <b>File</b> The resource as a {@code File}
	 * @throws IOException If an error occurred while fetching the resource (usually an invalid resource path)
	 * @see Resource#getResourceAsFileStream
	 */
	public static File getResourceAsFile(final Class<?> execClass, final String resourcePath, final StandardCopyOption copyOption) throws IOException {
		//Create a file object to store the temporary file's data
		File tempFile;

		//Derive an InputStream from the class resource and begin the export process
		try(InputStream streamIn = getResourceAsStream(execClass, FileUtil.winToUnixPath(resourcePath))){
			//Create the file, but mark it for deletion on exit because the file is only temporary
			tempFile = File.createTempFile(Integer.toHexString(streamIn.hashCode()), ".tmp");
			tempFile.deleteOnExit();

			//Convert the InputStream to file and copy its contents to the temporary file
			Files.copy(streamIn, Paths.get(tempFile.getAbsolutePath()), copyOption);
		}

		//Return the resulting file
		return tempFile;
	}
	
	/**
	 * Import a resource from within a JAR file ({@code src/main/resources}) as a {@code FileInputStream}.
	 *
	 * @param execClass The class that this method is being executed from (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param resourcePath The relative path to the resource and its name
	 * @param copyOption What to do if there's a conflict issue
	 * @return <b>File</b> The resource as a {@code FileInputStream}
	 * @throws IOException If an error occurred while fetching the resource (usually an invalid resource path)
	 * @see Resource#getResourceAsFile
	 */
	public static FileInputStream getResourceAsFileStream(
			final Class<?> execClass,
			final String resourcePath,
			final StandardCopyOption copyOption
	) throws IOException {
		//Create a FileInputStream from the file and return the resulting FileInputStream
		return new FileInputStream(getResourceAsFile(execClass, FileUtil.winToUnixPath(resourcePath), copyOption));
	}
	
	/**
	 * Import a resource from within a JAR file ({@code src/main/resources}) as an {@code InputStream}.
	 *
	 * @param execClass The class that this method is being executed from (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param resourcePath The relative path to the resource and its name
	 * @return <b>InputStream</b> The resource as an {@code InputStream}
	 */
	public static InputStream getResourceAsStream(final Class<?> execClass, final String resourcePath){
		//Open the resource file as an InputStream and return the resulting resource as an InputStream
		return execClass.getResourceAsStream("/" + FileUtil.winToUnixPath(resourcePath));
	}
	
	/**
	 * Retrieve the URL of a resource from within a JAR file ({@code src/main/resources}) as a {@code String}.
	 *
	 * @param execClass The class that this method is being executed from (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param resourcePath The relative path to the resource and its name
	 * @return <b>String</b> The resource file's absolute URL inside the JAR file as a {@code String}
	 * @throws UnsupportedEncodingException If the URL used inside the method is encoded with an unsupported encoding scheme
	 */
	public static String getResourceAsStr(final Class<?> execClass, final String resourcePath) throws UnsupportedEncodingException {
		//Get the resource path as a URL, then convert the URL to a string and return the resulting string
		return StringUtil.decodeUrl(String.valueOf(getResourceAsUrl(execClass, FileUtil.winToUnixPath(resourcePath))));
	}
	
	/**
	 * Retrieve the URL of a resource from within a JAR file ({@code src/main/resources}).
	 *
	 * @param execClass The class that this method is being executed from (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param resourcePath The relative path to the resource and its name
	 * @return <b>URL</b> The resource file's absolute URL inside the JAR file
	 */
	public static URL getResourceAsUrl(final Class<?> execClass, final String resourcePath){
		//Get the URL of the resource and return it
		return execClass.getResource("/" + FileUtil.winToUnixPath(resourcePath));
	}
	
	/**
	 * Check if a resource file exists on the classpath of a JAR or set of class files.
	 *
	 * @param execClass The class that this method is being executed from (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param resourcePath The relative path to the resource and its name
	 * @param copyOption What to do if there's a conflict issue
	 * @return <b>boolean</b> The status of whether or not the input {@code File} exists
	 */
	public static boolean resourceFileExists(final Class<?> execClass, final String resourcePath, final StandardCopyOption copyOption){
		//Try to get the resource as a file
		try {
			//Get the resource as a file
			File resFile = getResourceAsFile(execClass, resourcePath, copyOption);
			
			//Check if the file is an actual file and not a directory and return the status
			return resFile.isFile();
		}
		catch(IOException notFound){
			//Return false because an error occurred
			return false;
		}
	}
}
