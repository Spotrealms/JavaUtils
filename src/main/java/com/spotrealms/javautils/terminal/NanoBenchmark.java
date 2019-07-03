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

//Import Java classes and dependencies
import java.math.BigDecimal;

//TODO: Add JavaDoc

//Adapted from kangcz's TimperImpl class (https://stackoverflow.com/a/44036528/7520602)
public class NanoBenchmark {
	private long starterTime;
	private long elapsedTime;
	private long nanoOffset;
	private measPrecision benchPrecision;

	//Class constructor
	public NanoBenchmark(measPrecision benchPrecision){
		//Assign the class variables from the constructor's parameters
		this.benchPrecision = benchPrecision;
		
		//Create a counter for the for loop (to get the nanosecond offset)
		final int loopCount = 500;
		
		//Zero the nanosecond offset (more precision)
		BigDecimal offsetSum = BigDecimal.ZERO;
		
		for(int i=0; i < loopCount; i++){
			//Calculate the nanosecond offset
			offsetSum = offsetSum.add(BigDecimal.valueOf(calculateNanoOffset()));
		}
		
		//Get the nanosecond offset by dividing the offset sum by the loop count
		nanoOffset = (offsetSum.divide(BigDecimal.valueOf(loopCount))).longValue();
	}
	
	public NanoBenchmark(){
		//Redirect to the overloaded constructor with unimplemented parameters being substituted by their default values
		this(measPrecision.MILLISEC);
	}
	
	//Define precision levels
	enum measPrecision {
		NANOSEC, MICROSEC, MILLISEC, SEC;
	}
	
	public void start(){
		//Get the current system timestamp using the precision given upon instantiation
		starterTime = getCurrentTimestamp();
	}
	
	public void stop(){
		//Calculate the elapsed amount of time since the start of the benchmark (current timestamp - initial timestamp)
		elapsedTime = (getCurrentTimestamp() - starterTime);
	}
	
	private static long calculateNanoOffset(){
		//Get the current timestamp in nanoseconds
		final long nanoTime = System.nanoTime();
		
		//Attempt to get the equivalent time in milliseconds (less accurate)
		final long nanoFromMilli = System.currentTimeMillis() * 1_000_000;
		
		//Calculate the difference between the two measurements
		return (nanoFromMilli - nanoTime);
	}
	
	private long getCurrentTimestamp(){
		//Check the precision level
		switch(benchPrecision){
			case NANOSEC:
				//Return the current system timestamp, in nanoseconds, with offset
				return (nanoOffset + System.nanoTime());
			
			case MICROSEC:
				//Return the current system timestamp, in microseconds, with offset
				return ((nanoOffset + System.nanoTime()) / 1000);
			
			case MILLISEC:
				//Return the current system timestamp, in milliseconds
				return System.currentTimeMillis();
				
			case SEC:
				//Return the current system timestamp, in seconds
				return (System.currentTimeMillis() / 1000);
				
			default:
				//Return the current system timestamp, in milliseconds
				return System.currentTimeMillis();
		}
	}

	//Getters
	public long getStarterTime(){
		//Return the time the benchmark started at
		return starterTime;
	}

	public long getElapsedTime(){
		//Return the time it took for the benchmark to complete
		return elapsedTime;
	}
}