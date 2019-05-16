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

//TODO: Add JavaDoc

public class NanoBenchmark {
	private long starterMillis;
	private long elapsedMillis;

	//Class constructor
	public NanoBenchmark(){}
	
	public void start(){
		//Get the current millis and begin the process of benchmarking
		starterMillis = System.currentTimeMillis();
	}
	
	public void stop(){
		//Calculate the elapsed amount of time since the start of the benchmark (current millis - initial millis)
		elapsedMillis = (System.currentTimeMillis() - starterMillis);
	}

	//Getters
	public long getStarterMillis(){
		//Return the time the benchmark started at
		return starterMillis;
	}

	public long getElapsedMillis(){
		//Return the time it took for the benchmark to complete
		return elapsedMillis;
	}
}
