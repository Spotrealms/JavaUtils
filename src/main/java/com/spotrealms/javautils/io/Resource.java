/*
 * JavaUtils: A collection of utility methods and classes for your Java programs
 *   Copyright (C) 2015-2021 Spotrealms Network
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A series of methods for working with resource files inside JAR files. 
 * Resource files are any file inside a JAR file or on the classpath that 
 * may or may not be utilized by the entire program. Working with resource 
 * files can be useful if hard-coded data must be supplied. Examples may 
 * include localization strings, default configurations, fonts, images, etc.
 * The methods included can both import and export data from the classpath. This
 * class is not meant to replace Java's builtin {@link ClassLoader#getResource(String)}
 * or {@link ClassLoader#getResourceAsStream(String)}, {@link Class#getResource(String)},
 * or {@link Class#getResourceAsStream(String)} methods but rather complement
 * them, as the methods in this class can infer the target class without having
 * to use reflection via {@code MyClass.class}, for example, using Java's
 * {@link SecurityManager} via {@code SecurityManager.getClassContext()} among
 * other benefits besides the aforementioned.
 * <br>This class includes the following export options:
 * <ul>
 * 	<li>{@link File} via {@link Resource#export(Path, Path, StandardCopyOption)}</li>
 * </ul>
 * This class also includes the following import options:
 * <ul>
 * 	<li>{@link File} via {@link Resource#asFile(Path, StandardCopyOption)}</li>
 * 	<li>
 * 	    Non system linked {@link File} (ie: a {@link File} object with no reference to a file on the disk)
 * 	    via {@code new File(Resource.asUrl(path).getPath())}
 * 	</li>
 * 	<li>{@link FileInputStream} via {@code new FileInputStream(Resource.asFile(Path, StandardCopyOption))}</li>
 * 	<li>{@link InputStream} via {@link Resource#asStream(Path)}</li>
 * 	<li>Normalized {@link Path} via {@code Paths.get(Resource.asUrl(Path).toURI())}</li>
 * 	<li>
 * 	    Normalized path {@code String} via {@code Resource.asUrl(Path).getPath()},
 * 	    utilizing {@link StringUtil#decodeUrl} to construct a valid string representation
 * 	    without any URL encoded characters
 * 	</li>
 * 	<li>Normalized {@link URL} via {@link Resource#asUrl(Path)}</li>
 * </ul>
 * Finally, the following generic helper methods also exist that relate 
 * to classpath files:
 * <ul>
 * 	<li>{@link Resource#classpathRoot()} to get the base URL of the classpath</li>
 * 	<li>{@link Resource#exists(Path)} to check if a resource file exists</li>
 * 	<li>{@link Resource#typeof(Path)} to get the MIME type of an existing resource file</li>
 * 	<li>{@link Resource#walk(Path, boolean)} to get a directory listing of the resources from a starting directory</li>
 * </ul>
 * Each of the above methods allow either a relative lookup or an
 * absolute lookup to occur via {@link Class#getResource(String)}
 * and {@link Class#getResourceAsStream(String)} respectively via
 * {@code ...Rel} for the relative method or the standard name
 * for absolute via {@link ClassLoader#getResource(String)} or
 * {@link ClassLoader#getResourceAsStream(String)} respectively
 * . The "relative" and "absolute" terms are in relation
 * to the JAR, rather than the whole filesystem. The term "absolute"
 * in this context refers to the resource being at the root directory
 * of the JAR (eg: {@code MyJar!/my-resource} and the term "relative"
 * in this context refers to the resource being located relative to a
 * specific class by package (eg: if the class is named
 * {@code com.example.MyClass}, then the relevant resource
 * will be located in {@code MyJar!/com/example/my-resource}.
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
	 * Imports a resource from within a JAR file
	 * ({@code src/main/resources}) as a {@code File}. <br>
	 * NOTE: The parameter {@code path} is absolute. Use
	 * {@link Resource#asFileRel(Class, Path, StandardCopyOption)}
	 * to access the resource relative to the executing class.
	 *
	 * @param relLoader The {@link ClassLoader} that the resource should be loaded relative to (instantiated using
	 *     {@code ClassName.class.getClassLoader()} or {@code this.getClass().getClassLoader()}). Also accepts other
	 *     {@link ClassLoader} types such as {@code Thread.currentThread().getContextClassLoader()}
	 * @param path The absolute path to the resource and its name
	 * @param copyOption What to do if there's a conflict issue
	 * @return <b>File</b> The resource as a {@code File}
	 * @throws IOException If an error occurred while fetching the resource (usually an invalid resource path)
	 */
	public static File asFile(@NotNull final ClassLoader relLoader, @NotNull final Path path,
			@NotNull final StandardCopyOption copyOption) throws IOException {
		return asFile(asStream(relLoader, path), copyOption);
	}

	/**
	 * Imports a resource from within a JAR file
	 * ({@code src/main/resources}) as a {@code File}. <br>
	 * NOTE: The parameter {@code path} is absolute. Use
	 * {@link Resource#asFileRel(Path, StandardCopyOption)}
	 * to access the resource relative to the executing class.
	 *
	 * @param path The absolute path to the resource and its name
	 * @param copyOption What to do if there's a conflict issue
	 * @return <b>File</b> The resource as a {@code File}
	 * @throws IOException If an error occurred while fetching the resource (usually an invalid resource path)
	 */
	public static File asFile(@NotNull final Path path, @NotNull final StandardCopyOption copyOption) throws IOException {
		return asFile(asStream(path), copyOption);
	}

	/**
	 * Imports a resource from within a JAR file
	 * ({@code src/main/resources}) as a {@code File}. <br>
	 * NOTE: The parameter {@code path} is absolute. Use
	 * {@link Resource#asFileRel(Class, String, StandardCopyOption)}
	 * to access the resource relative to the executing class.
	 *
	 * @param relLoader The {@link ClassLoader} that the resource should be loaded relative to (instantiated using
	 *     {@code ClassName.class.getClassLoader()} or {@code this.getClass().getClassLoader()}). Also accepts other
	 *     {@link ClassLoader} types such as {@code Thread.currentThread().getContextClassLoader()}
	 * @param path The absolute path to the resource and its name
	 * @param copyOption What to do if there's a conflict issue
	 * @return <b>File</b> The resource as a {@code File}
	 * @throws IOException If an error occurred while fetching the resource (usually an invalid resource path)
	 */
	public static File asFile(@NotNull final ClassLoader relLoader, @NotNull final String path,
			@NotNull final StandardCopyOption copyOption) throws IOException {
		return asFile(asStream(relLoader, path), copyOption);
	}

	/**
	 * Imports a resource from within a JAR file
	 * ({@code src/main/resources}) as a {@code File}. <br>
	 * NOTE: The parameter {@code path} is absolute. Use
	 * {@link Resource#asFileRel(String, StandardCopyOption)}
	 * to access the resource relative to the executing class.
	 *
	 * @param path The absolute path to the resource and its name
	 * @param copyOption What to do if there's a conflict issue
	 * @return <b>File</b> The resource as a {@code File}
	 * @throws IOException If an error occurred while fetching the resource (usually an invalid resource path)
	 */
	public static File asFile(@NotNull final String path, @NotNull final StandardCopyOption copyOption) throws IOException {
		return asFile(asStream(path), copyOption);
	}

	/**
	 * Imports a resource from within a JAR file
	 * ({@code src/main/resources}) as a {@code File}. <br>
	 * NOTE: The parameter {@code path} is relative. Use
	 * {@link Resource#asFile(ClassLoader, Path, StandardCopyOption)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param relClass The class that the resource should be loaded relative to (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param path The relative path to the resource and its name
	 * @param copyOption What to do if there's a conflict issue
	 * @return <b>File</b> The resource as a {@code File}
	 * @throws IOException If an error occurred while fetching the resource (usually an invalid resource path)
	 */
	public static File asFileRel(@NotNull final Class<?> relClass, @NotNull final Path path,
			@NotNull final StandardCopyOption copyOption) throws IOException {
		return asFile(asStreamRel(relClass, path), copyOption);
	}

	/**
	 * Imports a resource from within a JAR file
	 * ({@code src/main/resources}) as a {@code File}. <br>
	 * NOTE: The parameter {@code path} is relative. Use
	 * {@link Resource#asFile(Path, StandardCopyOption)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param path The relative path to the resource and its name
	 * @param copyOption What to do if there's a conflict issue
	 * @return <b>File</b> The resource as a {@code File}
	 * @throws IOException If an error occurred while fetching the resource (usually an invalid resource path)
	 */
	public static File asFileRel(@NotNull final Path path, @NotNull final StandardCopyOption copyOption) throws IOException {
		return asFile(asStreamRel(path), copyOption);
	}

	/**
	 * Imports a resource from within a JAR file
	 * ({@code src/main/resources}) as a {@code File}. <br>
	 * NOTE: The parameter {@code path} is relative. Use
	 * {@link Resource#asFile(ClassLoader, String, StandardCopyOption)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param relClass The class that the resource should be loaded relative to (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param path The relative path to the resource and its name
	 * @param copyOption What to do if there's a conflict issue
	 * @return <b>File</b> The resource as a {@code File}
	 * @throws IOException If an error occurred while fetching the resource (usually an invalid resource path)
	 */
	public static File asFileRel(@NotNull final Class<?> relClass, @NotNull final String path,
			@NotNull final StandardCopyOption copyOption) throws IOException {
		return asFile(asStreamRel(relClass, path), copyOption);
	}

	/**
	 * Imports a resource from within a JAR file
	 * ({@code src/main/resources}) as a {@code File}. <br>
	 * NOTE: The parameter {@code path} is relative. Use
	 * {@link Resource#asFile(String, StandardCopyOption)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param path The relative path to the resource and its name
	 * @param copyOption What to do if there's a conflict issue
	 * @return <b>File</b> The resource as a {@code File}
	 * @throws IOException If an error occurred while fetching the resource (usually an invalid resource path)
	 */
	public static File asFileRel(@NotNull final String path, @NotNull final StandardCopyOption copyOption) throws IOException {
		return asFile(asStreamRel(path), copyOption);
	}

	/**
	 * Imports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) as an {@link InputStream}
	 * via Java's {@link ClassLoader#getResourceAsStream}.
	 * This method is not much different than Java's
	 * inbuilt version seen earlier, and should generally
	 * not be used instead of it, but this method also has
	 * an alternate version, {@link Resource#asStream(Path)},
	 * which infers the caller class on runtime via {@code SecurityManager.getClassContext()}.
	 * That version would lead to a more simplified call, which is
	 * {@code asStream(Path)}. <br>
	 * <b>WARNING: </b> This method's {@code path} parameter is
	 * an ABSOLUTE PATH, not a relative one (ie: the resource path starts
	 * at the root directory of the JAR). To use relative paths via
	 * {@link Class#getResourceAsStream} for resources located in the same
	 * directory as the given class or in a similar directory
	 * structure in {@code src/main/resources}, use
	 * {@link Resource#asStreamRel(Class, Path)}.
	 *
	 * @param relLoader The {@link ClassLoader} that the resource should be loaded relative to (instantiated using
	 *     {@code ClassName.class.getClassLoader()} or {@code this.getClass().getClassLoader()}). Also accepts other
	 *     {@link ClassLoader} types such as {@code Thread.currentThread().getContextClassLoader()}
	 * @param path The absolute path to the resource, including its filename
	 * @return <b>InputStream</b> The resource as an {@link InputStream}
	 */
	public static @Nullable InputStream asStream(@NotNull final ClassLoader relLoader, @NotNull final Path path){
		//Open the resource file as an InputStream and return the resulting resource as an InputStream
		return relLoader.getResourceAsStream(FileUtil.winToUnixPath(path.toString()));
	}

	/**
	 * Imports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) as an {@link InputStream}
	 * via Java's {@link ClassLoader#getResourceAsStream}.
	 * This method simplifies calls to the aforementioned
	 * method, as the actual class to which the resource
	 * is located relative to is inferred on runtime rather
	 * than on compile-time via a call such as
	 * {@code MyClass.class.getClassLoader().getResourceAsStream(String)}, which
	 * would get simplified down to {@code asStream(Path)}.
	 * Other versions of this method exist such as
	 * {@link Resource#asStream(ClassLoader, Path)}, but this
	 * is not much different than simply using this method version,
	 * which infers the class, meaning in those cases, Java's inbuilt
	 * resource loader should be used instead. <br>
	 * <b>WARNING: </b> This method's {@code path} parameter is
	 * an ABSOLUTE PATH, not a relative one (ie: the resource path starts
	 * at the root directory of the JAR). To use relative paths via
	 * {@link Class#getResourceAsStream} for resources located in the same
	 * directory as the caller class or in a similar directory
	 * structure in {@code src/main/resources}, use
	 * {@link Resource#asStreamRel(Path)}.
	 *
	 * @param path The absolute path to the resource, including its filename
	 * @return <b>InputStream</b> The resource as an {@link InputStream}
	 */
	public static @Nullable InputStream asStream(@NotNull final Path path){
		//Infer the caller class via the security manager (faster than reflection) and pass that to the overloaded method
		return asStream(new SecManCallerIDer().identify().getClassLoader(), path);
	}

	/**
	 * Imports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) as an {@link InputStream}
	 * via Java's {@link ClassLoader#getResourceAsStream}.
	 * This method is not much different than Java's
	 * inbuilt version seen earlier, and should generally
	 * not be used instead of it, but this method also has
	 * an alternate version, {@link Resource#asStream(String)},
	 * which infers the caller class on runtime via {@code SecurityManager.getClassContext()}.
	 * That version would lead to a more simplified call, which is
	 * {@code asStream(String)}. <br>
	 * <b>WARNING: </b> This method's {@code path} parameter is
	 * an ABSOLUTE PATH, not a relative one (ie: the resource path starts
	 * at the root directory of the JAR). To use relative paths via
	 * {@link Class#getResourceAsStream} for resources located in the same
	 * directory as the given class or in a similar directory
	 * structure in {@code src/main/resources}, use
	 * {@link Resource#asStreamRel(Class, String)}.
	 *
	 * @param relLoader The {@link ClassLoader} that the resource should be loaded relative to (instantiated using
	 *     {@code ClassName.class.getClassLoader()} or {@code this.getClass().getClassLoader()}). Also accepts other
	 *     {@link ClassLoader} types such as {@code Thread.currentThread().getContextClassLoader()}
	 * @param path The absolute path to the resource, including its filename
	 * @return <b>InputStream</b> The resource as an {@link InputStream}
	 */
	public static @Nullable InputStream asStream(@NotNull final ClassLoader relLoader, @NotNull final String path){
		//Open the resource file as an InputStream and return the resulting resource as an InputStream
		return asStream(relLoader, Paths.get(path));
	}

	/**
	 * Imports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) as an {@link InputStream}
	 * via Java's {@link ClassLoader#getResourceAsStream}.
	 * This method simplifies calls to the aforementioned
	 * method, as the actual class to which the resource
	 * is located relative to is inferred on runtime rather
	 * than on compile-time via a call such as
	 * {@code MyClass.class.getClassLoader().getResourceAsStream(String)}, which
	 * would get simplified down to {@code asStream(String)}.
	 * Other versions of this method exist such as
	 * {@link Resource#asStream(ClassLoader, String)}, but this
	 * is not much different than simply using this method version,
	 * which infers the class, meaning in those cases, Java's inbuilt
	 * resource loader should be used instead. <br>
	 * <b>WARNING: </b> This method's {@code path} parameter is
	 * an ABSOLUTE PATH, not a relative one (ie: the resource path starts
	 * at the root directory of the JAR). To use relative paths via
	 * {@link Class#getResourceAsStream} for resources located in the same
	 * directory as the caller class or in a similar directory
	 * structure in {@code src/main/resources}, use
	 * {@link Resource#asStreamRel(String)}.
	 *
	 * @param path The absolute path to the resource, including its filename
	 * @return <b>InputStream</b> The resource as an {@link InputStream}
	 */
	public static @Nullable InputStream asStream(@NotNull final String path){
		//Infer the caller class via the security manager (faster than reflection) and pass that to the overloaded method
		return asStream(Paths.get(path));
	}

	/**
	 * Imports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) as an {@link InputStream}
	 * via Java's {@link Class#getResourceAsStream}.
	 * This method is not much different than Java's
	 * inbuilt version seen earlier, and should generally
	 * not be used instead of it, but this method also has
	 * an alternate version, {@link Resource#asStream(Path)},
	 * which infers the caller class on runtime via {@code SecurityManager.getClassContext()}.
	 * That version would lead to a more simplified call, which is
	 * {@code asStream(Path)}. <br>
	 * <b>WARNING: </b> This method's {@code path} parameter is
	 * a RELATIVE PATH, not an absolute one. Thus, any resource paths,
	 * given the specified class' package is {@code com.example},
	 * will resolve to {@code com/example/my-resource}, where the
	 * base location of the resource starts in the directory
	 * {@code com/example} in the JAR. To use absolute paths
	 * via {@link ClassLoader}, use {@link Resource#asStream(ClassLoader, Path)}.
	 *
	 * @param relClass The class that the resource should be loaded relative to (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param path The relative path to the resource, including its filename
	 * @return <b>InputStream</b> The resource as an {@link InputStream}
	 */
	public static @Nullable InputStream asStreamRel(@NotNull final Class<?> relClass, @NotNull final Path path){
		//Open the resource file as an InputStream and return the resulting resource as an InputStream
		return relClass.getResourceAsStream(FileUtil.winToUnixPath(path.toString()));
	}

	/**
	 * Imports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) as an {@link InputStream}
	 * via Java's {@link Class#getResourceAsStream}.
	 * This method simplifies calls to the aforementioned
	 * method, as the actual class to which the resource
	 * is located relative to is inferred on runtime rather
	 * than on compile-time via a call such as
	 * {@code MyClass.class.getResourceAsStream(String)}, which
	 * would get simplified down to {@code asStreamRel(Path)}.
	 * Other versions of this method exist such as
	 * {@link Resource#asStreamRel(Class, Path)}, but this
	 * is not much different than simply using this method version,
	 * which infers the class, meaning in those cases, Java's inbuilt
	 * resource loader should be used instead. <br>
	 * <b>WARNING: </b> This method's {@code path} parameter is
	 * a RELATIVE PATH, not an absolute one. Thus, any resource paths,
	 * given the caller class' package is {@code com.example},
	 * will resolve to {@code com/example/my-resource}, where the
	 * base location of the resource starts in the directory
	 * {@code com/example} in the JAR. To use absolute paths
	 * via {@link ClassLoader}, use {@link Resource#asStream(Path)}.
	 *
	 * @param path The relative path to the resource, including its filename
	 * @return <b>InputStream</b> The resource as an {@link InputStream}
	 */
	public static @Nullable InputStream asStreamRel(@NotNull final Path path){
		//Infer the caller class via the security manager (faster than reflection) and pass that to the overloaded method
		return asStreamRel(new SecManCallerIDer().identify(), path);
	}

	/**
	 * Imports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) as an {@link InputStream}
	 * via Java's {@link Class#getResourceAsStream}.
	 * This method is not much different than Java's
	 * inbuilt version seen earlier, and should generally
	 * not be used instead of it, but this method also has
	 * an alternate version, {@link Resource#asStreamRel(String)},
	 * which infers the caller class on runtime via {@code SecurityManager.getClassContext()}.
	 * That version would lead to a more simplified call, which is
	 * {@code asStreamRel(String)}. <br>
	 * <b>WARNING: </b> This method's {@code path} parameter is
	 * a RELATIVE PATH, not an absolute one. Thus, any resource paths,
	 * given the specified class' package is {@code com.example},
	 * will resolve to {@code com/example/my-resource}, where the
	 * base location of the resource starts in the directory
	 * {@code com/example} in the JAR. To use absolute paths
	 * via {@link ClassLoader}, use {@link Resource#asStream(ClassLoader, String)}.
	 *
	 * @param relClass The class that the resource should be loaded relative to (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param path The relative path to the resource, including its filename
	 * @return <b>InputStream</b> The resource as an {@link InputStream}
	 */
	public static @Nullable InputStream asStreamRel(@NotNull final Class<?> relClass, @NotNull final String path){
		//Open the resource file as an InputStream and return the resulting resource as an InputStream
		return asStreamRel(relClass, Paths.get(path));
	}

	/**
	 * Imports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) as an {@link InputStream}
	 * via Java's {@link Class#getResourceAsStream}.
	 * This method simplifies calls to the aforementioned
	 * method, as the actual class to which the resource
	 * is located relative to is inferred on runtime rather
	 * than on compile-time via a call such as
	 * {@code MyClass.class.getResourceAsStream(String)}, which
	 * would get simplified down to {@code asStreamRel(String)}.
	 * Other versions of this method exist such as
	 * {@link Resource#asStreamRel(Class, String)}, but this
	 * is not much different than simply using this method version,
	 * which infers the class, meaning in those cases, Java's inbuilt
	 * resource loader should be used instead. <br>
	 * <b>WARNING: </b> This method's {@code path} parameter is
	 * a RELATIVE PATH, not an absolute one. Thus, any resource paths,
	 * given the caller class' package is {@code com.example},
	 * will resolve to {@code com/example/my-resource}, where the
	 * base location of the resource starts in the directory
	 * {@code com/example} in the JAR. To use absolute paths
	 * via {@link ClassLoader}, use {@link Resource#asStream(String)}.
	 *
	 * @param path The relative path to the resource, including its filename
	 * @return <b>InputStream</b> The resource as an {@link InputStream}
	 */
	public static @Nullable InputStream asStreamRel(@NotNull final String path){
		//Infer the caller class via the security manager (faster than reflection) and pass that to the overloaded method
		return asStreamRel(Paths.get(path));
	}

	/**
	 * Imports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) as a {@link URL}
	 * via Java's {@link ClassLoader#getResource}.
	 * This method is not much different than Java's
	 * inbuilt version seen earlier, and should generally
	 * not be used instead of it, but this method also has
	 * an alternate version, {@link Resource#asUrl(Path)},
	 * which infers the caller class on runtime via {@code SecurityManager.getClassContext()}.
	 * That version would lead to a more simplified call, which is
	 * {@code asUrl(Path)}. <br>
	 * <b>WARNING: </b> This method's {@code path} parameter is
	 * an ABSOLUTE PATH, not a relative one (ie: the resource path starts
	 * at the root directory of the JAR). To use relative paths via
	 * {@link Class#getResource} for resources located in the same
	 * directory as the given class or in a similar directory
	 * structure in {@code src/main/resources}, use
	 * {@link Resource#asUrlRel(Class, Path)}.
	 *
	 * @param relLoader The {@link ClassLoader} that the resource should be loaded relative to (instantiated using
	 *     {@code ClassName.class.getClassLoader()} or {@code this.getClass().getClassLoader()}). Also accepts other
	 *     {@link ClassLoader} types such as {@code Thread.currentThread().getContextClassLoader()}
	 * @param path The absolute path to the resource, including its filename
	 * @return <b>URL</b> The resource location, normalized to a {@link URL}
	 */
	public static @Nullable URL asUrl(@NotNull final ClassLoader relLoader, @NotNull final Path path){
		//Check if the input path is empty (indicates the desire to get the base path of the classpath)
		if(path.toString().isEmpty()){
			//Get the root URL of the classpath and return it
			return classpathRoot();
		}
		else {
			//Open the resource file as a URL and return the resulting resource reference as a URL, which is the normal behavior
			return relLoader.getResource(FileUtil.winToUnixPath(path.toString()));
		}
	}

	/**
	 * Imports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) as a {@link URL}
	 * via Java's {@link ClassLoader#getResource}.
	 * This method simplifies calls to the aforementioned
	 * method, as the actual class to which the resource
	 * is located relative to is inferred on runtime rather
	 * than on compile-time via a call such as
	 * {@code MyClass.class.getClassLoader().getResource(String)}, which
	 * would get simplified down to {@code asUrl(Path)}.
	 * Other versions of this method exist such as
	 * {@link Resource#asUrl(ClassLoader, Path)}, but this
	 * is not much different than simply using this method version,
	 * which infers the class, meaning in those cases, Java's inbuilt
	 * resource loader should be used instead. <br>
	 * <b>WARNING: </b> This method's {@code path} parameter is
	 * an ABSOLUTE PATH, not a relative one (ie: the resource path starts
	 * at the root directory of the JAR). To use relative paths via
	 * {@link Class#getResource} for resources located in the same
	 * directory as the caller class or in a similar directory
	 * structure in {@code src/main/resources}, use
	 * {@link Resource#asUrlRel(Path)}.
	 *
	 * @param path The absolute path to the resource, including its filename
	 * @return <b>URL</b> The resource location, normalized to a {@link URL}
	 */
	public static @Nullable URL asUrl(@NotNull final Path path){
		//Infer the caller class via the security manager (faster than reflection) and pass that to the overloaded method
		return asUrl(new SecManCallerIDer().identify().getClassLoader(), path);
	}

	/**
	 * Imports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) as a {@link URL}
	 * via Java's {@link ClassLoader#getResource}.
	 * This method is not much different than Java's
	 * inbuilt version seen earlier, and should generally
	 * not be used instead of it, but this method also has
	 * an alternate version, {@link Resource#asUrl(String)},
	 * which infers the caller class on runtime via {@code SecurityManager.getClassContext()}.
	 * That version would lead to a more simplified call, which is
	 * {@code asUrl(String)}. <br>
	 * <b>WARNING: </b> This method's {@code path} parameter is
	 * an ABSOLUTE PATH, not a relative one (ie: the resource path starts
	 * at the root directory of the JAR). To use relative paths via
	 * {@link Class#getResource} for resources located in the same
	 * directory as the given class or in a similar directory
	 * structure in {@code src/main/resources}, use
	 * {@link Resource#asUrlRel(Class, String)}.
	 *
	 * @param relLoader The {@link ClassLoader} that the resource should be loaded relative to (instantiated using
	 *     {@code ClassName.class.getClassLoader()} or {@code this.getClass().getClassLoader()}). Also accepts other
	 *     {@link ClassLoader} types such as {@code Thread.currentThread().getContextClassLoader()}
	 * @param path The absolute path to the resource, including its filename
	 * @return <b>URL</b> The resource location, normalized to a {@link URL}
	 */
	public static @Nullable URL asUrl(@NotNull final ClassLoader relLoader, @NotNull final String path){
		//Open the resource file as a URL and return the resulting resource as a URL
		return asUrl(relLoader, Paths.get(path));
	}

	/**
	 * Imports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) as a {@link URL}
	 * via Java's {@link ClassLoader#getResource}.
	 * This method simplifies calls to the aforementioned
	 * method, as the actual class to which the resource
	 * is located relative to is inferred on runtime rather
	 * than on compile-time via a call such as
	 * {@code MyClass.class.getClassLoader().getResource(String)}, which
	 * would get simplified down to {@code asUrl(String)}.
	 * Other versions of this method exist such as
	 * {@link Resource#asUrl(ClassLoader, String)}, but this
	 * is not much different than simply using this method version,
	 * which infers the class, meaning in those cases, Java's inbuilt
	 * resource loader should be used instead. <br>
	 * <b>WARNING: </b> This method's {@code path} parameter is
	 * an ABSOLUTE PATH, not a relative one (ie: the resource path starts
	 * at the root directory of the JAR). To use relative paths via
	 * {@link Class#getResource} for resources located in the same
	 * directory as the caller class or in a similar directory
	 * structure in {@code src/main/resources}, use
	 * {@link Resource#asUrlRel(String)}.
	 *
	 * @param path The absolute path to the resource, including its filename
	 * @return <b>URL</b> The resource location, normalized to a {@link URL}
	 */
	public static @Nullable URL asUrl(@NotNull final String path){
		//Infer the caller class via the security manager (faster than reflection) and pass that to the overloaded method
		return asUrl(Paths.get(path));
	}

	/**
	 * Imports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) as a {@link URL}
	 * via Java's {@link Class#getResource}.
	 * This method is not much different than Java's
	 * inbuilt version seen earlier, and should generally
	 * not be used instead of it, but this method also has
	 * an alternate version, {@link Resource#asUrlRel(Path)},
	 * which infers the caller class on runtime via {@code SecurityManager.getClassContext()}.
	 * That version would lead to a more simplified call, which is
	 * {@code asUrlRel(Path)}. <br>
	 * <b>WARNING: </b> This method's {@code path} parameter is
	 * a RELATIVE PATH, not an absolute one. Thus, any resource paths,
	 * given the specified class' package is {@code com.example},
	 * will resolve to {@code com/example/my-resource}, where the
	 * base location of the resource starts in the directory
	 * {@code com/example} in the JAR. To use absolute paths
	 * via {@link ClassLoader}, use {@link Resource#asUrl(ClassLoader, Path)}.
	 *
	 * @param relClass The class that the resource should be loaded relative to (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param path The relative path to the resource, including its filename
	 * @return <b>URL</b> The resource location, normalized to a {@link URL}
	 */
	public static @Nullable URL asUrlRel(@NotNull final Class<?> relClass, @NotNull final Path path){
		//Check if the path points to the head of the classpath
		if(FileUtil.winToUnixPath(path.toString()).equals("/")){
			//Get the root URL of the classpath and return it
			return classpathRoot();
		}

		//Open the resource file as a URL and return the resulting resource reference as a URL
		return relClass.getResource(FileUtil.winToUnixPath(path.toString()));
	}

	/**
	 * Imports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) as a {@link URL}
	 * via Java's {@link Class#getResource}.
	 * This method simplifies calls to the aforementioned
	 * method, as the actual class to which the resource
	 * is located relative to is inferred on runtime rather
	 * than on compile-time via a call such as
	 * {@code MyClass.class.getResource(String)}, which
	 * would get simplified down to {@code asUrlRel(Path)}.
	 * Other versions of this method exist such as
	 * {@link Resource#asUrlRel(Class, Path)}, but this
	 * is not much different than simply using this method version,
	 * which infers the class, meaning in those cases, Java's inbuilt
	 * resource loader should be used instead. <br>
	 * <b>WARNING: </b> This method's {@code path} parameter is
	 * a RELATIVE PATH, not an absolute one. Thus, any resource paths,
	 * given the caller class' package is {@code com.example},
	 * will resolve to {@code com/example/my-resource}, where the
	 * base location of the resource starts in the directory
	 * {@code com/example} in the JAR. To use absolute paths
	 * via {@link ClassLoader}, use {@link Resource#asUrl(Path)}.
	 *
	 * @param path The relative path to the resource, including its filename
	 * @return <b>URL</b> The resource location, normalized to a {@link URL}
	 */
	public static @Nullable URL asUrlRel(@NotNull final Path path){
		//Infer the caller class via the security manager (faster than reflection) and pass that to the overloaded method
		return asUrlRel(new SecManCallerIDer().identify(), path);
	}

	/**
	 * Imports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) as a {@link URL}
	 * via Java's {@link Class#getResource}.
	 * This method is not much different than Java's
	 * inbuilt version seen earlier, and should generally
	 * not be used instead of it, but this method also has
	 * an alternate version, {@link Resource#asUrlRel(String)},
	 * which infers the caller class on runtime via {@code SecurityManager.getClassContext()}.
	 * That version would lead to a more simplified call, which is
	 * {@code asUrlRel(String)}. <br>
	 * <b>WARNING: </b> This method's {@code path} parameter is
	 * a RELATIVE PATH, not an absolute one. Thus, any resource paths,
	 * given the specified class' package is {@code com.example},
	 * will resolve to {@code com/example/my-resource}, where the
	 * base location of the resource starts in the directory
	 * {@code com/example} in the JAR. To use absolute paths
	 * via {@link ClassLoader}, use {@link Resource#asUrl(ClassLoader, String)}.
	 *
	 * @param relClass The class that the resource should be loaded relative to (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param path The relative path to the resource, including its filename
	 * @return <b>URL</b> The resource location, normalized to a {@link URL}
	 */
	public static @Nullable URL asUrlRel(@NotNull final Class<?> relClass, @NotNull final String path){
		//Open the resource file as a URL and return the resulting resource as a URL
		return asUrlRel(relClass, Paths.get(path));
	}

	/**
	 * Imports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) as a {@link URL}
	 * via Java's {@link Class#getResource}.
	 * This method simplifies calls to the aforementioned
	 * method, as the actual class to which the resource
	 * is located relative to is inferred on runtime rather
	 * than on compile-time via a call such as
	 * {@code MyClass.class.getResource(String)}, which
	 * would get simplified down to {@code asUrlRel(String)}.
	 * Other versions of this method exist such as
	 * {@link Resource#asUrlRel(Class, String)}, but this
	 * is not much different than simply using this method version,
	 * which infers the class, meaning in those cases, Java's inbuilt
	 * resource loader should be used instead. <br>
	 * <b>WARNING: </b> This method's {@code path} parameter is
	 * a RELATIVE PATH, not an absolute one. Thus, any resource paths,
	 * given the caller class' package is {@code com.example},
	 * will resolve to {@code com/example/my-resource}, where the
	 * base location of the resource starts in the directory
	 * {@code com/example} in the JAR. To use absolute paths
	 * via {@link ClassLoader}, use {@link Resource#asUrl(String)}.
	 *
	 * @param path The relative path to the resource, including its filename
	 * @return <b>URL</b> The resource location, normalized to a {@link URL}
	 */
	public static @Nullable URL asUrlRel(@NotNull final String path){
		//Infer the caller class via the security manager (faster than reflection) and pass that to the overloaded method
		return asUrlRel(Paths.get(path));
	}

	/**
	 * Fetches the root directory of the classpath given a
	 * specific class on the desired classpath by finding its
	 * "package depth" and returning the URL to the directory
	 * that's at depth level 0, which would be the root of the
	 * classpath.
	 *
	 * @param relClass The class that should be used as the reference point to begin
	 * @return <b>URL</b> The location of the root of the classpath, normalized to a {@link URL}
	 */
	public static @Nullable URL classpathRoot(final Class<?> relClass){
		//Get the "package depth" of the target class
		int depth = (int) relClass.getName().codePoints().filter(ch -> ch == '.').count();

		//Get the URL of the target class
		URL callerUrl = relClass.getResource("/" + relClass.getName().replaceAll("\\.", "/") + ".class");

		//Derive a file from the URL and get the base of the classpath using the depth guide, getting the parent file until the depth is 0
		File basePath = new File(Objects.requireNonNull(callerUrl).toString());
		for(int i = depth; i >= 0; i--) basePath = basePath.getParentFile();

		//Get the URL of the base path and return it
		try {
			//Return the base path, appending a trailing slash to the end of the URL
			return new URL(FileUtil.winToUnixPath(basePath.toString()) + "/");
		}
		catch(MalformedURLException ignored){
			//Simply return null in the event of an error, though this is unlikely to occur
			return null;
		}
	}

	/**
	 * Fetches the root directory of the current classpath
	 * via inferring the caller class, finding its "package
	 * depth" and returning the URL to the directory that's at
	 * depth level 0, which would be the root of the
	 * classpath.
	 *
	 * @return <b>URL</b> The location of the root of the classpath, normalized to a {@link URL}
	 */
	public static @Nullable URL classpathRoot(){
		//Call the original method with the caller class being inferred
		return classpathRoot(new SecManCallerIDer().identify());
	}
	
	/**
	 * Checks if a resource file exists on the classpath
	 * of a JAR or set of class files via {@link Resource#asUrl(ClassLoader, Path)}. <br>
	 * NOTE: The parameter {@code path} is absolute. Use
	 * {@link Resource#existsRel(Class, Path)}
	 * to access the resource relative to the executing class.
	 *
	 * @param relLoader The {@link ClassLoader} that the resource should be loaded relative to (instantiated using
	 *     {@code ClassName.class.getClassLoader()} or {@code this.getClass().getClassLoader()}). Also accepts other
	 *     {@link ClassLoader} types such as {@code Thread.currentThread().getContextClassLoader()}
	 * @param path The absolute path to the resource, including its filename
	 * @return <b>boolean</b> The status of whether or not the input resource file exists
	 */
	public static boolean exists(@NotNull final ClassLoader relLoader, @NotNull final Path path){
		//Check if the resource file exists by trying to derive a URL to the resource
		return asUrl(relLoader, path) != null;
	}

	/**
	 * Checks if a resource file exists on the classpath
	 * of a JAR or set of class files via {@link Resource#asUrl(Path)}. <br>
	 * NOTE: The parameter {@code path} is absolute. Use
	 * {@link Resource#existsRel(Path)}
	 * to access the resource relative to the executing class.
	 *
	 * @param path The absolute path to the resource, including its filename
	 * @return <b>boolean</b> The status of whether or not the input resource file exists
	 */
	public static boolean exists(@NotNull final Path path){
		//Check if the resource file exists by trying to derive a URL to the resource
		return asUrl(path) != null;
	}

	/**
	 * Checks if a resource file exists on the classpath
	 * of a JAR or set of class files via {@link Resource#asUrl(ClassLoader, String)}. <br>
	 * NOTE: The parameter {@code path} is absolute. Use
	 * {@link Resource#existsRel(Class, String)}
	 * to access the resource relative to the executing class.
	 *
	 * @param relLoader The {@link ClassLoader} that the resource should be loaded relative to (instantiated using
	 *     {@code ClassName.class.getClassLoader()} or {@code this.getClass().getClassLoader()}). Also accepts other
	 *     {@link ClassLoader} types such as {@code Thread.currentThread().getContextClassLoader()}
	 * @param path The absolute path to the resource, including its filename
	 * @return <b>boolean</b> The status of whether or not the input resource file exists
	 */
	public static boolean exists(@NotNull final ClassLoader relLoader, @NotNull final String path){
		//Check if the resource file exists by trying to derive a URL to the resource
		return asUrl(relLoader, path) != null;
	}

	/**
	 * Checks if a resource file exists on the classpath
	 * of a JAR or set of class files via {@link Resource#asUrl(String)}. <br>
	 * NOTE: The parameter {@code path} is absolute. Use
	 * {@link Resource#existsRel(String)}
	 * to access the resource relative to the executing class.
	 *
	 * @param path The absolute path to the resource, including its filename
	 * @return <b>boolean</b> The status of whether or not the input resource file exists
	 */
	public static boolean exists(@NotNull final String path){
		//Check if the resource file exists by trying to derive a URL to the resource
		return asUrl(path) != null;
	}

	/**
	 * Checks if a resource file exists on the classpath
	 * of a JAR or set of class files via {@link Resource#asUrlRel(Class, Path)}. <br>
	 * NOTE: The parameter {@code path} is relative. Use
	 * {@link Resource#exists(ClassLoader, Path)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param relClass The class that the resource should be loaded relative to (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param path The relative path to the resource and its name
	 * @return <b>boolean</b> The status of whether or not the input resource file exists
	 */
	public static boolean existsRel(@NotNull final Class<?> relClass, @NotNull final Path path){
		//Check if the resource file exists by trying to derive a URL to the resource
		return asUrlRel(relClass, path) != null;
	}

	/**
	 * Checks if a resource file exists on the classpath
	 * of a JAR or set of class files via {@link Resource#asUrlRel(Path)}. <br>
	 * NOTE: The parameter {@code path} is relative. Use
	 * {@link Resource#exists(Path)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param path The relative path to the resource and its name
	 * @return <b>boolean</b> The status of whether or not the input resource file exists
	 */
	public static boolean existsRel(@NotNull final Path path){
		//Check if the resource file exists by trying to derive a URL to the resource
		return asUrlRel(path) != null;
	}

	/**
	 * Checks if a resource file exists on the classpath
	 * of a JAR or set of class files via {@link Resource#asUrlRel(Class, String)}. <br>
	 * NOTE: The parameter {@code path} is relative. Use
	 * {@link Resource#exists(ClassLoader, String)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param relClass The class that the resource should be loaded relative to (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param path The relative path to the resource and its name
	 * @return <b>boolean</b> The status of whether or not the input resource file exists
	 */
	public static boolean existsRel(@NotNull final Class<?> relClass, @NotNull final String path){
		//Check if the resource file exists by trying to derive a URL to the resource
		return asUrlRel(relClass, path) != null;
	}

	/**
	 * Checks if a resource file exists on the classpath
	 * of a JAR or set of class files via {@link Resource#asUrlRel(String)}. <br>
	 * NOTE: The parameter {@code path} is relative. Use
	 * {@link Resource#exists(String)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param path The relative path to the resource and its name
	 * @return <b>boolean</b> The status of whether or not the input resource file exists
	 */
	public static boolean existsRel(@NotNull final String path){
		//Check if the resource file exists by trying to derive a URL to the resource
		return asUrlRel(path) != null;
	}

	/**
	 * Exports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) to a specified
	 * path either relative to the program JAR or to an
	 * absolute path elsewhere on the system. <br>
	 * NOTE: The parameter {@code resourcePath} is absolute. Use
	 * {@link Resource#exportRel(Class, Path, Path, StandardCopyOption)}
	 * to access the resource relative to the executing class.
	 *
	 * @param relLoader The {@link ClassLoader} that the resource should be loaded relative to (instantiated using
	 *     {@code ClassName.class.getClassLoader()} or {@code this.getClass().getClassLoader()}). Also accepts other
	 *     {@link ClassLoader} types such as {@code Thread.currentThread().getContextClassLoader()}
	 * @param resourcePath The absolute path to the resource in the JAR, including its filename
	 * @param dropPath The location to drop the extracted resource, including its filename (can be absolute or relative to the program JAR)
	 * @param copyOption What to do if there's a file conflict issue
	 * @throws IOException If an error occurred while fetching or dropping the resource (usually an invalid resource path)
	 */
	public static void export(@NotNull final ClassLoader relLoader, @NotNull final Path resourcePath,
			@NotNull final Path dropPath, @NotNull final StandardCopyOption copyOption) throws IOException {
		export(asStream(relLoader, resourcePath), resourcePath, dropPath, copyOption);
	}

	/**
	 * Exports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) to a specified
	 * path either relative to the program JAR or to an
	 * absolute path elsewhere on the system. <br>
	 * NOTE: The parameter {@code resourcePath} is absolute. Use
	 * {@link Resource#exportRel(Path, Path, StandardCopyOption)}
	 * to access the resource relative to the executing class.
	 *
	 * @param resourcePath The absolute path to the resource in the JAR, including its filename
	 * @param dropPath The location to drop the extracted resource, including its filename (can be absolute or relative to the program JAR)
	 * @param copyOption What to do if there's a file conflict issue
	 * @throws IOException If an error occurred while fetching or dropping the resource (usually an invalid resource path)
	 */
	public static void export(@NotNull final Path resourcePath, @NotNull final Path dropPath,
			@NotNull final StandardCopyOption copyOption) throws IOException {
		export(asStream(resourcePath), resourcePath, dropPath, copyOption);
	}

	/**
	 * Exports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) to a specified
	 * path either relative to the program JAR or to an
	 * absolute path elsewhere on the system. <br>
	 * NOTE: The parameter {@code resourcePath} is relative. Use
	 * {@link Resource#export(ClassLoader, Path, Path, StandardCopyOption)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param relClass The class that the resource should be loaded relative to (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param resourcePath The relative path to the resource, including its filename
	 * @param dropPath The location to drop the extracted resource, including its filename (can be absolute or relative to the program JAR)
	 * @param copyOption What to do if there's a file conflict issue
	 * @throws IOException If an error occurred while fetching or dropping the resource (usually an invalid resource path)
	 */
	public static void exportRel(@NotNull final Class<?> relClass, @NotNull final Path resourcePath,
			@NotNull final Path dropPath, @NotNull final StandardCopyOption copyOption) throws IOException {
		export(asStreamRel(relClass, resourcePath), resourcePath, dropPath, copyOption);
	}

	/**
	 * Exports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) to a specified
	 * path either relative to the program JAR or to an
	 * absolute path elsewhere on the system. <br>
	 * NOTE: The parameter {@code resourcePath} is relative. Use
	 * {@link Resource#export(ClassLoader, Path, Path, StandardCopyOption)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param resourcePath The relative path to the resource, including its filename
	 * @param dropPath The location to drop the extracted resource, including its filename (can be absolute or relative to the program JAR)
	 * @param copyOption What to do if there's a file conflict issue
	 * @throws IOException If an error occurred while fetching or dropping the resource (usually an invalid resource path)
	 */
	public static void exportRel(@NotNull final Path resourcePath, @NotNull final Path dropPath,
			final StandardCopyOption copyOption) throws IOException {
		export(asStreamRel(resourcePath), resourcePath, dropPath, copyOption);
	}

	/**
	 * Exports a resource directory from within a JAR file
	 * (ie: {@code src/main/resources}) to a specified
	 * path either relative to the program JAR or to an
	 * absolute path elsewhere on the system. <br>
	 * NOTE: The parameter {@code resourcePath} is absolute. Use
	 * {@link Resource#exportDirectoryRel(Class, Path, Path, StandardCopyOption)}
	 * to access the resource directory relative to the executing class.
	 *
	 * @param relLoader The {@link ClassLoader} that the resource directory should be loaded relative to (instantiated using
	 *     {@code ClassName.class.getClassLoader()} or {@code this.getClass().getClassLoader()}). Also accepts other
	 *     {@link ClassLoader} types such as {@code Thread.currentThread().getContextClassLoader()}
	 * @param resourceDirectoryPath The absolute path to the resource directory in the JAR
	 * @param dropPath The location to drop the extracted resource directory (can be absolute or relative to the program JAR)
	 * @param copyOption What to do if there's a file conflict issue
	 * @throws IOException If an error occurred while fetching or dropping the resource directory (usually an invalid resource directory path)
	 */
	public static void exportDirectory(@NotNull final ClassLoader relLoader, @NotNull final Path resourceDirectoryPath,
			@NotNull final Path dropPath, @NotNull final StandardCopyOption copyOption) throws IOException {
		exportDirectory(asStream(relLoader, resourceDirectoryPath), resourceDirectoryPath, dropPath, copyOption);
	}

	/**
	 * Exports a resource directory from within a JAR file
	 * (ie: {@code src/main/resources}) to a specified
	 * path either relative to the program JAR or to an
	 * absolute path elsewhere on the system. <br>
	 * NOTE: The parameter {@code resourcePath} is absolute. Use
	 * {@link Resource#exportDirectoryRel(Path, Path, StandardCopyOption)}
	 * to access the resource directory relative to the executing class.
	 *
	 * @param resourceDirectoryPath The absolute path to the resource directory in the JAR
	 * @param dropPath The location to drop the extracted resource directory (can be absolute or relative to the program JAR)
	 * @param copyOption What to do if there's a file conflict issue
	 * @throws IOException If an error occurred while fetching or dropping the resource directory (usually an invalid resource directory path)
	 */
	public static void exportDirectory(@NotNull final Path resourceDirectoryPath, @NotNull final Path dropPath,
			@NotNull final StandardCopyOption copyOption) throws IOException {
		exportDirectory(asStream(resourceDirectoryPath), resourceDirectoryPath, dropPath, copyOption);
	}

	/**
	 * Exports a resource directory from within a JAR file
	 * (ie: {@code src/main/resources}) to a specified
	 * path either relative to the program JAR or to an
	 * absolute path elsewhere on the system. <br>
	 * NOTE: The parameter {@code resourcePath} is relative. Use
	 * {@link Resource#export(ClassLoader, Path, Path, StandardCopyOption)}
	 * to access the resource directory absolute to the executing class.
	 *
	 * @param relClass The class that the directory should be loaded relative to (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param resourceDirectoryPath The relative path to the resource directory in the JAR
	 * @param dropPath The location to drop the extracted resource (can be absolute or relative to the program JAR)
	 * @param copyOption What to do if there's a file conflict issue
	 * @throws IOException If an error occurred while fetching or dropping the resource directory (usually an invalid resource directory path)
	 */
	public static void exportDirectoryRel(@NotNull final Class<?> relClass, @NotNull final Path resourceDirectoryPath,
			@NotNull final Path dropPath, @NotNull final StandardCopyOption copyOption) throws IOException {
		export(asStreamRel(relClass, resourceDirectoryPath), resourceDirectoryPath, dropPath, copyOption);
	}

	/**
	 * Exports a resource directory from within a JAR file
	 * (ie: {@code src/main/resources}) to a specified
	 * path either relative to the program JAR or to an
	 * absolute path elsewhere on the system. <br>
	 * NOTE: The parameter {@code resourcePath} is relative. Use
	 * {@link Resource#exportDirectory(ClassLoader, Path, Path, StandardCopyOption)}
	 * to access the resource directory absolute to the executing class.
	 *
	 * @param resourceDirectoryPath The relative path to the resource directory
	 * @param dropPath The location to drop the extracted resource directory (can be absolute or relative to the program JAR)
	 * @param copyOption What to do if there's a file conflict issue
	 * @throws IOException If an error occurred while fetching or dropping the resource directory (usually an invalid resource directory path)
	 */
	public static void exportDirectoryRel(@NotNull final Path resourceDirectoryPath, @NotNull final Path dropPath,
			final StandardCopyOption copyOption) throws IOException {
		export(asStreamRel(resourceDirectoryPath), resourceDirectoryPath, dropPath, copyOption);
	}
	
	/**
	 * Determines the MIME type of a classpath resource if it exists. <br>
	 * NOTE: The parameter {@code path} is absolute. Use
	 * {@link Resource#typeofRel(Class, Path)}
	 * to access the resource relative to the executing class.
	 *
	 * @param relLoader The {@link ClassLoader} that the resource should be loaded relative to (instantiated using
	 *     {@code ClassName.class.getClassLoader()} or {@code this.getClass().getClassLoader()}). Also accepts other
	 *     {@link ClassLoader} types such as {@code Thread.currentThread().getContextClassLoader()}
	 * @param path The absolute path to the resource, including its filename
	 * @return <b>Type</b> The MIME type of a resource as referenced by a {@link Resource.Type} object
	 */
	public static Type typeof(@NotNull final ClassLoader relLoader, @NotNull final Path path){
		return typeof(asUrl(relLoader, path));
	}

	/**
	 * Determines the MIME type of a classpath resource if it exists. <br>
	 * NOTE: The parameter {@code path} is absolute. Use
	 * {@link Resource#typeofRel(Path)}
	 * to access the resource relative to the executing class.
	 *
	 * @param path The absolute path to the resource, including its filename
	 * @return <b>Type</b> The MIME type of a resource as referenced by a {@link Resource.Type} object
	 */
	public static Type typeof(@NotNull final Path path){
		return typeof(asUrl(path));
	}

	/**
	 * Determines the MIME type of a classpath resource if it exists. <br>
	 * NOTE: The parameter {@code path} is absolute. Use
	 * {@link Resource#typeofRel(Class, String)}
	 * to access the resource relative to the executing class.
	 *
	 * @param relLoader The {@link ClassLoader} that the resource should be loaded relative to (instantiated using
	 *     {@code ClassName.class.getClassLoader()} or {@code this.getClass().getClassLoader()}). Also accepts other
	 *     {@link ClassLoader} types such as {@code Thread.currentThread().getContextClassLoader()}
	 * @param path The absolute path to the resource, including its filename
	 * @return <b>Type</b> The MIME type of a resource as referenced by a {@link Resource.Type} object
	 */
	public static Type typeof(@NotNull final ClassLoader relLoader, @NotNull final String path){
		return typeof(asUrl(relLoader, path));
	}

	/**
	 * Determines the MIME type of a classpath resource if it exists. <br>
	 * NOTE: The parameter {@code path} is absolute. Use
	 * {@link Resource#typeofRel(String)}
	 * to access the resource relative to the executing class.
	 *
	 * @param path The absolute path to the resource, including its filename
	 * @return <b>Type</b> The MIME type of a resource as referenced by a {@link Resource.Type} object
	 */
	public static Type typeof(@NotNull final String path){
		return typeof(asUrl(path));
	}

	/**
	 * Determines the MIME type of a classpath resource if it exists. <br>
	 * NOTE: The parameter {@code path} is relative. Use
	 * {@link Resource#typeof(ClassLoader, Path)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param relClass The class that the resource should be loaded relative to (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param path The relative path to the resource and its name
	 * @return <b>Type</b> The MIME type of a resource as referenced by a {@link Resource.Type} object
	 */
	public static Type typeofRel(@NotNull final Class<?> relClass, @NotNull final Path path){
		return typeof(asUrlRel(relClass, path));
	}

	/**
	 * Determines the MIME type of a classpath resource if it exists. <br>
	 * NOTE: The parameter {@code path} is relative. Use
	 * {@link Resource#typeof(Path)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param path The relative path to the resource and its name
	 * @return <b>Type</b> The MIME type of a resource as referenced by a {@link Resource.Type} object
	 */
	public static Type typeofRel(@NotNull final Path path){
		return typeof(asUrlRel(path));
	}

	/**
	 * Determines the MIME type of a classpath resource if it exists. <br>
	 * NOTE: The parameter {@code path} is relative. Use
	 * {@link Resource#typeof(ClassLoader, String)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param relClass The class that the resource should be loaded relative to (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param path The relative path to the resource and its name
	 * @return <b>Type</b> The MIME type of a resource as referenced by a {@link Resource.Type} object
	 */
	public static Type typeofRel(@NotNull final Class<?> relClass, @NotNull final String path){
		return typeof(asUrlRel(relClass, path));
	}

	/**
	 * Determines the MIME type of a classpath resource if it exists. <br>
	 * NOTE: The parameter {@code path} is relative. Use
	 * {@link Resource#typeof(String)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param path The relative path to the resource and its name
	 * @return <b>Type</b> The MIME type of a resource as referenced by a {@link Resource.Type} object
	 */
	public static Type typeofRel(@NotNull final String path){
		return typeof(asUrlRel(path));
	}

	/**
	 * Walks a classpath resource file tree, cataloging the
	 * list of files and folders that are contained in the
	 * folder specified at the given path, going recursively
	 * into sub-folders if told to do so. <br>
	 * NOTE: The parameter {@code path} is absolute. Use
	 * {@link Resource#walkRel(Class, Path, boolean)}
	 * to access the resource relative to the executing class.
	 *
	 * @param relLoader The {@link ClassLoader} that the resource should be loaded relative to (instantiated using
	 *     {@code ClassName.class.getClassLoader()} or {@code this.getClass().getClassLoader()}). Also accepts other
	 *     {@link ClassLoader} types such as {@code Thread.currentThread().getContextClassLoader()}
	 * @param path The absolute starting path from which to start the file tree walk
	 * @param recursive Whether or not the operation should be recursive
	 * @return <b>ArrayList&lt;Path&gt;</b> The list of found paths on the classpath
	 * @throws IllegalArgumentException If the resource path is invalid
	 * @throws IOException If an error occurs during the operation
	 */
	public static ArrayList<Path> walk(@NotNull final ClassLoader relLoader, @NotNull final Path path, final boolean recursive) throws IOException {
		try {
			return walk(Objects.requireNonNull(asUrl(relLoader, path)).toURI(), recursive);
		}
		catch(URISyntaxException ignored){} //Will never be thrown
		catch(NullPointerException ex){
			throw new IllegalArgumentException("Path cannot be null or reference a non-existent resource directory.");
		}

		return new ArrayList<>();
	}

	/**
	 * Walks a classpath resource file tree, cataloging the
	 * list of files and folders that are contained in the
	 * folder specified at the given path, going recursively
	 * into sub-folders if told to do so. <br>
	 * NOTE: The parameter {@code path} is absolute. Use
	 * {@link Resource#walkRel(Path, boolean)}
	 * to access the resource relative to the executing class.
	 *
	 * @param path The absolute starting path from which to start the file tree walk
	 * @param recursive Whether or not the operation should be recursive
	 * @return <b>ArrayList&lt;Path&gt;</b> The list of found paths on the classpath
	 * @throws IllegalArgumentException If the resource path is invalid
	 * @throws IOException If an error occurs during the operation
	 */
	public static ArrayList<Path> walk(@NotNull final Path path, final boolean recursive) throws IOException {
		try {
			return walk(Objects.requireNonNull(asUrl(path)).toURI(), recursive);
		}
		catch(URISyntaxException ignored){} //Will never be thrown
		catch(NullPointerException ex){
			throw new IllegalArgumentException("Path cannot be null or reference a non-existent resource directory.");
		}

		return new ArrayList<>();
	}

	/**
	 * Walks a classpath resource file tree, cataloging the
	 * list of files and folders that are contained in the
	 * folder specified at the given path, going recursively
	 * into sub-folders if told to do so. <br>
	 * NOTE: The parameter {@code path} is absolute. Use
	 * {@link Resource#walkRel(Class, String, boolean)}
	 * to access the resource relative to the executing class.
	 *
	 * @param relLoader The {@link ClassLoader} that the resource should be loaded relative to (instantiated using
	 *     {@code ClassName.class.getClassLoader()} or {@code this.getClass().getClassLoader()}). Also accepts other
	 *     {@link ClassLoader} types such as {@code Thread.currentThread().getContextClassLoader()}
	 * @param path The absolute starting path from which to start the file tree walk
	 * @param recursive Whether or not the operation should be recursive
	 * @return <b>ArrayList&lt;Path&gt;</b> The list of found paths on the classpath
	 * @throws IllegalArgumentException If the resource path is invalid
	 * @throws IOException If an error occurs during the operation
	 */
	public static ArrayList<Path> walk(@NotNull final ClassLoader relLoader, @NotNull final String path, final boolean recursive) throws IOException {
		return walk(relLoader, Paths.get(path), recursive);
	}

	/**
	 * Walks a classpath resource file tree, cataloging the
	 * list of files and folders that are contained in the
	 * folder specified at the given path, going recursively
	 * into sub-folders if told to do so. <br>
	 * NOTE: The parameter {@code path} is absolute. Use
	 * {@link Resource#walkRel(String, boolean)}
	 * to access the resource relative to the executing class.
	 *
	 * @param path The absolute starting path from which to start the file tree walk
	 * @param recursive Whether the operation should be recursive
	 * @return <b>ArrayList&lt;Path&gt;</b> The list of found paths on the classpath
	 * @throws IllegalArgumentException If the resource path is invalid
	 * @throws IOException If an error occurs during the operation
	 */
	public static ArrayList<Path> walk(@NotNull final String path, final boolean recursive) throws IOException {
		return walk(Paths.get(path), recursive);
	}

	/**
	 * Walks a resource file tree given a starting directory, returning
	 * a list of found paths. <br>
	 * NOTE: The parameter {@code path} is relative. Use
	 * {@link Resource#walk(ClassLoader, Path, boolean)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param relClass The class that the resource should be loaded relative to (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param path The relative starting path from which to start the file tree walk
	 * @param recursive Whether the operation should be recursive
	 * @return <b>ArrayList&lt;Path&gt;</b> The list of found paths on the classpath
	 * @throws IllegalArgumentException If the resource path is invalid
	 * @throws IOException If an error occurs during the operation
	 */
	public static ArrayList<Path> walkRel(@NotNull final Class<?> relClass, @NotNull final Path path, final boolean recursive) throws IOException {
		try {
			return walk(Objects.requireNonNull(asUrlRel(relClass, path)).toURI(), recursive);
		}
		catch(URISyntaxException ignored){} //Will never be thrown
		catch(NullPointerException ex){
			throw new IllegalArgumentException("Path cannot be null or reference a non-existent resource directory.");
		}

		return new ArrayList<>();
	}

	/**
	 * Walks a resource file tree given a starting directory, returning
	 * a list of found paths. <br>
	 * NOTE: The parameter {@code path} is relative. Use
	 * {@link Resource#walk(Path, boolean)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param path The relative starting path from which to start the file tree walk
	 * @param recursive Whether the operation should be recursive
	 * @return <b>ArrayList&lt;Path&gt;</b> The list of found paths on the classpath
	 * @throws IllegalArgumentException If the resource path is invalid
	 * @throws IOException If an error occurs during the operation
	 */
	public static ArrayList<Path> walkRel(@NotNull final Path path, final boolean recursive) throws IOException {
		try {
			return walk(Objects.requireNonNull(asUrlRel(path)).toURI(), recursive);
		}
		catch(URISyntaxException ignored){} //Will never be thrown
		catch(NullPointerException ex){
			throw new IllegalArgumentException("Path cannot be null or reference a non-existent resource directory.");
		}

		return new ArrayList<>();
	}

	/**
	 * Walks a resource file tree given a starting directory, returning
	 * a list of found paths. <br>
	 * NOTE: The parameter {@code path} is relative. Use
	 * {@link Resource#walk(ClassLoader, String, boolean)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param relClass The class that the resource should be loaded relative to (instantiated using {@code ClassName.class} or {@code this.getClass()})
	 * @param path The relative starting path from which to start the file tree walk
	 * @param recursive Whether the operation should be recursive
	 * @return <b>ArrayList&lt;Path&gt;</b> The list of found paths on the classpath
	 * @throws IllegalArgumentException If the resource path is invalid
	 * @throws IOException If an error occurs during the operation
	 */
	public static ArrayList<Path> walkRel(@NotNull final Class<?> relClass, @NotNull final String path, final boolean recursive) throws IOException {
		return walkRel(relClass, Paths.get(path), recursive);
	}

	/**
	 * Walks a resource file tree given a starting directory, returning
	 * a list of found paths. <br>
	 * NOTE: The parameter {@code path} is relative. Use
	 * {@link Resource#walk(String, boolean)}
	 * to access the resource absolute to the executing class.
	 *
	 * @param path The relative starting path from which to start the file tree walk
	 * @param recursive Whether the operation should be recursive
	 * @return <b>ArrayList&lt;Path&gt;</b> The list of found paths on the classpath
	 * @throws IllegalArgumentException If the resource path is invalid
	 * @throws IOException If an error occurs during the operation
	 */
	public static ArrayList<Path> walkRel(@NotNull final String path, final boolean recursive) throws IOException {
		return walkRel(Paths.get(path), recursive);
	}

	/**
	 * Imports a resource from within a JAR file
	 * ({@code src/main/resources}) as a {@code File}.
	 *
	 * @param resStream The target resource as an {@link InputStream}
	 * @param copyOption What to do if there's a conflict issue
	 * @return <b>File</b> The resource as a {@code File}
	 * @throws IllegalArgumentException If the resource path is invalid
	 * @throws IOException If an error occurred while copying the resource
	 */
	private static File asFile(final InputStream resStream, final StandardCopyOption copyOption) throws IOException {
		//Ensure the args aren't null
		if(resStream == null) throw new IllegalArgumentException("Path cannot be null or reference a non-existent resource.");
		if(copyOption == null) throw new IllegalArgumentException("Standard Copy Option cannot be null.");

		//Create the output file in the temp directory
		Path tempFile = Files.createTempFile(Integer.toHexString(resStream.hashCode()), ".tmp");

		//Convert the InputStream to file and copy its contents to the temporary file
		Files.copy(resStream, tempFile, copyOption);

		//Return the resulting file object
		return tempFile.toFile();
	}

	/**
	 * Exports a resource directory from within a JAR file
	 * (ie: {@code src/main/resources}) to a specified
	 * path either relative to the program JAR or to an
	 * absolute path elsewhere on the system.
	 *
	 * @param resStream The target resource as an {@link InputStream}
	 * @param origPath The original path to the resource in the JAR, including its filename
	 * @param dropPath The location to drop the extracted resource, including its filename (can be absolute or relative to the program JAR)
	 * @param copyOption What to do if there's a file conflict issue
	 * @throws IllegalArgumentException If the resource path is invalid
	 * @throws IOException If an error occurred while fetching or dropping the resource (usually an invalid resource path)
	 */
	private static void exportDirectory(final InputStream resStream, final Path origPath,
			final Path dropPath, final StandardCopyOption copyOption) throws IOException {
		//Ensure the args aren't null
		if(resStream == null || origPath == null || dropPath == null){
			throw new IllegalArgumentException("Path(s) cannot be null or reference a non-existent resource.");
		}
		if(copyOption == null){
			throw new IllegalArgumentException("Standard Copy Option cannot be null.");
		}

		//Ensure the target is a directory and not a file
		if(typeof(origPath).getCategory().equals(Category.FILE)){
			throw new IllegalArgumentException("Resource path must reference a directory. Use Resource#export to export files.");
		}

		//Read the inputstream as a list of strings (this stream contains the directory listing for the resource directory)
		List<String> directoryContents = new BufferedReader(new InputStreamReader(resStream, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());

		//Loop over the contents of the directory
		for(String entry : directoryContents){
			//Get the full path of the file relative to the classpath as well as if it's a directory or file
			Path fullPath = origPath.resolve(entry);
			Category entryType = typeof(fullPath).category;

			//Derive the resulting path to drop the contents of the directory into
			Path fullDropPath = dropPath.resolve(entry);

			//Switch over the category
			switch(entryType){
				case FILE:{
					//Export the resource with the paths and the same copy option as the user provided
					export(fullPath, fullDropPath, copyOption);

					//Nothing left to do so break out
					break;
				}
				case DIRECTORY:{
					//Create the drop directory in the destination
					Files.createDirectory(fullDropPath);

					//Recursively run the method to export the subdirectory (may or may not cause locks if there's a lot to unpack)
					exportDirectory(asStream(fullPath), fullPath, fullDropPath, copyOption);

					//Nothing left to do so break out
					break;
				}

				default: break; //Urecognized filetype, so leave it be
			}
		}
	}

	/**
	 * Exports a resource from within a JAR file
	 * (ie: {@code src/main/resources}) to a specified
	 * path either relative to the program JAR or to an
	 * absolute path elsewhere on the system.
	 *
	 * @param resStream The target resource as an {@link InputStream}
	 * @param origPath The original path to the resource in the JAR, including its filename
	 * @param dropPath The location to drop the extracted resource, including its filename (can be absolute or relative to the program JAR)
	 * @param copyOption What to do if there's a file conflict issue
	 * @throws IllegalArgumentException If the resource path is invalid
	 * @throws IOException If an error occurred while fetching or dropping the resource (usually an invalid resource path)
	 */
	private static void export(final InputStream resStream, final Path origPath,
			final Path dropPath, final StandardCopyOption copyOption) throws IOException {
		//Ensure the args aren't null
		if(resStream == null || origPath == null || dropPath == null){
			throw new IllegalArgumentException("Path(s) cannot be null or reference a non-existent resource.");
		}
		if(copyOption == null) throw new IllegalArgumentException("Standard Copy Option cannot be null.");

		//Ensure the target is a file and not a directory
		if(typeof(origPath).getCategory().equals(Category.DIRECTORY)){
			throw new IllegalArgumentException("Resource path must reference a file. Use Resource#exportDirectory to export entire directories.");
		}

		//Check if the drop path is relative
		Path absDropPath = dropPath;
		if(!dropPath.isAbsolute()){
			//Get the input path as an absolute path, but in relation to the JAR
			absDropPath = dropPath.toAbsolutePath();
		}

		//Check if the drop path is a directory (assumes the user just specified a directory to copy the resources to and not the filename)
		if(Files.isDirectory(absDropPath)){
			//Derive the correct file drop path using the JAR resource name as the filename of the file to drop
			absDropPath = absDropPath.resolve(origPath.getFileName()).toAbsolutePath();
		}

		//Check if the drop path is blank (assumes the user wants to copy to the same directory as the runtime with the same filename)
		if(dropPath.toString().isEmpty()){
			//Get the filename of the original path
			String filename = origPath.getFileName() == null || origPath.getFileName().toString().isEmpty() ? ""
				: origPath.getFileName().toString();

			//Get the referenced resource filename and concat it onto the end of the drop path
			absDropPath = Paths.get(absDropPath.toString(), filename);
		}

		//Copy the resource to the output destination via Files.copy()
		Files.copy(resStream, absDropPath, copyOption);
	}

	/**
	 * Determines the MIME type of a classpath resource if it exists.
	 *
	 * @param resUrl The target resource as a {@link URL}
	 * @return <b>Type</b> The MIME type of a resource as referenced by a {@link Resource.Type} object
	 */
	private static Type typeof(@Nullable final URL resUrl){
		//Try to get the resource type (accounts for both null URLs, null paths, and bad URLs) of the given filesystem (JAR or a simple filesystem)
		try(FileSystem ignored = Objects.requireNonNull(resUrl).toURI().getScheme().equals("jar")
				? FileSystems.newFileSystem(Objects.requireNonNull(resUrl).toURI(), Collections.emptyMap()) : null){
			//Derive an absolute path object from the URL
			Path absolutePath = Paths.get(Objects.requireNonNull(resUrl).toURI());

			//Return a file type, inferring the MIME type of the file via Files.probeContentType if the path references a file
			if(Files.isRegularFile(absolutePath)) return new Type(Category.FILE, Files.probeContentType(absolutePath));

			//Return a directory type, setting the MIME type as inode/directory if the path references
			//directory, though this is Unix only and deprecated by the IANA
			else if(Files.isDirectory(absolutePath)){
				return new Type(Category.DIRECTORY, "inode/directory");
			}
		}
		catch(NullPointerException | URISyntaxException | IOException ignored){} //Ignore exceptions, jumping straight to the default return branch

		//Return a nonexistent type with a null category
		return new Type(Category.NONEXISTENT, null);
	}

	/**
	 * Walks a classpath resource file tree, cataloging the
	 * list of files and folders that are contained in the
	 * folder specified at the given URI, going recursively
	 * into sub-folders if told to do so.
	 *
	 * @author fge, Pino, Somaiah Kumbera
	 * @param uri The starting resource folder URI for the file tree walk
	 * @param recursive Whether the operation should be recursive
	 * @return <b>ArrayList&lt;Path&gt;</b> The list of found paths on the classpath
	 * @throws IllegalArgumentException If the resource path is invalid
	 * @throws IOException If an error occurs during the operation
	 * @see <a href="https://stackoverflow.com/a/22605905">https://stackoverflow.com/a/22605905</a>
	 * @see <a href="https://stackoverflow.com/a/39974405">https://stackoverflow.com/a/39974405</a>
	 * @see <a href="https://stackoverflow.com/a/62999111">https://stackoverflow.com/a/62999111</a>
	 */
	private static ArrayList<Path> walk(@NotNull final URI uri, final boolean recursive) throws IOException {
		//Attempt to get the filesystem of either a JAR or the simple filesystem if running in an IDE
		try(FileSystem ignored = uri.getScheme().equals("jar") ? FileSystems.newFileSystem(uri, Collections.emptyMap()) : null){
			//Derive a path from the uri
			Path uriPath = Paths.get(uri);

			//Check if the walk should be recursive
			if(recursive){
				//Create a list to hold the found paths
				ArrayList<Path> found = new ArrayList<>();

				//Begin walking the file tree of the JAR via Files.walkFileTree
				Files.walkFileTree(uriPath, new SimpleFileVisitor<Path>(){
					@Override
					public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs){
						//Add the current path to the found paths list
						found.add(file);

						//Continue the walk if there's files left
						return FileVisitResult.CONTINUE;
					}
				});

				//Return the list
				return found;
			}
			else {
				//Run Files.walk with a max depth of 1 on the directory and return the resulting list
				try(Stream<Path> walk = Files.walk(uriPath, 1)){
					return (ArrayList<Path>) walk.filter(p -> !p.equals(uriPath)).collect(Collectors.toList());
				}
			}
		}
	}

	/**
	 * Defines the category of a resource (eg: {@link Category#DIRECTORY},
	 * {@link Category#FILE}, or {@link Category#NONEXISTENT}).
	 */
	public enum Category {
		/** Resource is a directory. */
		DIRECTORY,
		/** Resource is a file. */
		FILE,
		/** Non-existent resource. */
		NONEXISTENT
	}

	/**
	 * Small adapter class off of Java's {@link SecurityManager}
	 * that enables the identification of a caller
	 * class. This method is up to 9x faster than a
	 * stack trace walk, though this does not take
	 * the performance of Java 9's {@code StackWalker}
	 * API into account. This method, however, is
	 * probably the best pre-Java 9 method of getting
	 * the caller class.
	 *
	 * @author TransLunarInjection
	 * @see <a href="https://github.com/TransLunarInjection/WhoCalled">
	 *          https://github.com/TransLunarInjection/WhoCalled
	 *      </a>
	 */
	private static class SecManCallerIDer extends SecurityManager {
		/** Default constructor. */
		SecManCallerIDer(){ super(); }

		/**
		 * Identifies the calling class via {@link SecurityManager#getClassContext()}.
		 *
		 * @return <b>Class&lt;?&gt;</b> The relevant caller class
		 */
		public Class<?> identify(){
			//Get the last reference class in the context and return it
			Class<?>[] secTrace = getClassContext();
			return secTrace[secTrace.length - 1];
		}
	}

	/**
	 * Defines the type of a resource, along with the inferred MIME
	 * type via {@link Files#probeContentType} given the resource is
	 * a {@link Category#FILE}.
	 */
	public static final class Type {
		/** The type of the resource. */
		private final Category category;
		/** The MIME type of the resource, if applicable. */
		private final String mime;

		/**
		 * Constructs a new {@link Type} object.
		 *
		 * @param category The type of the resource
		 * @param mime The MIME type of the resource, if applicable
		 */
		public Type(final Category category, @Nullable final String mime){
			this.category = category;
			this.mime = mime;
		}

		/**
		 * Fetches the category of the {@link Type} object.
		 *
		 * @return <b>Category</b> The category of the {@link Type} object
		 */
		public Category getCategory(){
			return category;
		}

		/**
		 * Fetches the MIME string of the {@link Type} object.
		 *
		 * @return <b>String</b> The MIME string of the {@link Type} object
		 */
		public String getMime(){
			return mime;
		}

		/**
		 * Returns a string representation of the {@link Type} object.
		 *
		 * @return <b>String</b> The object as a string
		 */
		@Override
		public String toString(){
			return category + " (" + (mime != null ? mime : "<none>") + ")";
		}
	}
}
