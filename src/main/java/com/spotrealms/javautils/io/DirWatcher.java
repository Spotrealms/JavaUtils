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

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * A wrapper class for Java 7's {@code WatchService}
 * that adds asynchronous callbacks to external methods
 * and threading. To use this class, it must be 
 * extended by another class and all defined abstract 
 * methods should be defined in the user's class. Once
 * this is done, this class's {@code watch} method
 * can be called like the following:<br>
 * In the user's constructor:
 * <pre style="margin:0; padding:0">
 *  MyClass(){
 *   super(watchPath);
 *   Thread thread = new Thread(this, this.getClass().getName());
 *   thread.start();
 *  }
 * </pre>
 * In the user class's overridden {@code run()} method:
 * <pre style="margin:0; padding:0">
 *  &#64;Override
 *  public void run(){
 *   try {
 *    super.watch();
 *   }
 *   catch(Exception e){
 *    e.printStackTrace();
 *   }
 *  }
 * </pre>
 * In each of the asynchronous event handler methods, 
 * the following objects can give useful information 
 * about the fired event:
 * <ul>
 *  <li><pre style="margin:0; padding:0; display: inline">context()</pre> - The file/directory that was modified</li>
 *  <li><pre style="margin:0; padding:0; display: inline">count()</pre> - The times this event was fired</li>
 *  <li><pre style="margin:0; padding:0; display: inline">kind()</pre> - The fired event's type (create, delete, modify)</li>
 * </ul>
 * @deprecated Better alternatives exist like "gmethvin/directory-watcher" or "darylteo/directory-watcher".
 *      This class will be removed on release of version 1.0
 * @author Spotrealms
 */
@Deprecated
public abstract class DirWatcher implements Runnable {
	//Set class variables
	private Path watchPath;
	
	//Class constructor
	/**
	 * Constructs an instance of the {@code DirWatcher}
	 * class.
	 * @param watchPath The absolute path to the directory to watch
	 */
	public DirWatcher(final String watchPath){
		//Initialization
		String wPath = watchPath;

		//Check if the watch path is blank
		if(StringUtil.isNullOrVoid(wPath)){
			//Set the watch path to be the current directory
			wPath = System.getProperty("user.dir");
		}
		
		//Check if the path points to an existent file/folder
		if(new File(wPath).exists()){
			//Derive a path object from the input string
			this.watchPath = Paths.get(wPath);
		}
		else {
			//Throw an IllegalArgumentException because the input path doesn't exist
			throw new IllegalArgumentException("Input path points to a non-existent directory");
		}
	}
	
	/**
	 * Constructs an instance of the {@code DirWatcher}
	 * class.
	 */
	public DirWatcher(){
		//Redirect to the overloaded constructor with unimplemented parameters being substituted by blanks
		this("");
	}
	
	/**
	 * Starts watching a directory given by the user
	 * for changes. These change events are sent to 
	 * abstract methods defined by the user for further
	 * processing of these events
	 * @throws Exception If a generic error occurs while watching the directory
	 */
	public void watch() throws Exception {
		//Create the watch service in a try with resources block
		try(WatchService watchService = FileSystems.getDefault().newWatchService()){
			//Register the input path with the watcher service
			watchPath.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
				 
			//Create a key to hold the finds of the service
			WatchKey watchKey;
					
			//Loop while the watch service still reports files
			while((watchKey = watchService.take()) != null){
				//Loop over the captured events
				for(WatchEvent<?> watchEvent : watchKey.pollEvents()){
					//Get the type of event that was detected
					Kind<?> eventKind = watchEvent.kind();
					
					//Check if the event kind was a create event
					if(eventKind == ENTRY_CREATE){
						//Redirect to the onCreate method defined by the user
						onCreate(watchEvent);
					}
					
					//Check if the event kind was a delete event
					if(eventKind == ENTRY_DELETE){
						//Redirect to the onDelete method defined by the user
						onDelete(watchEvent);
					}
					
					//Check if the event kind was a modify event
					if(eventKind == ENTRY_MODIFY){
						//Redirect to the onModify method defined by the user
						onModify(watchEvent);
					}
				}
							
				//Reset the key
				watchKey.reset();
			}
		}
	}
	
	/**
	 * Runs whenever the watch service detects a file
	 * creation event. This method is to be implemented
	 * by the user via class inheritance
	 * @param watchEvent The event firing cause
	 */
	public abstract void onCreate(WatchEvent<?> watchEvent);
	
	/**
	 * Runs whenever the watch service detects a file
	 * deletion event. This method is to be implemented
	 * by the user via class inheritance
	 * @param watchEvent The event firing cause
	 */
	public abstract void onDelete(WatchEvent<?> watchEvent);
	
	/**
	 * Runs whenever the watch service detects a file
	 * modification event. This method is to be implemented
	 * by the user via class inheritance
	 * @param watchEvent The event firing cause
	 */
	public abstract void onModify(WatchEvent<?> watchEvent);
}
