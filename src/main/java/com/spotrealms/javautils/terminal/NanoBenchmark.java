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

package com.spotrealms.javautils.terminal;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * A simple inline benchmarking class used to
 * determine the time it takes for a series of
 * operations to complete. Adapted from kangcz's
 * TimperImpl class.
 *
 * @author Spotrealms &amp; kangcz
 * @see <a href="https://stackoverflow.com/a/44036528">https://stackoverflow.com/a/44036528</a>
 */
public class NanoBenchmark {
	private final long nanoOffset;
	private final TimeUnit precision;

	private long startTime;
	private long elapsedTime;

	/**
	 * Creates a new instance of {@code NanoBenchmark} given
	 * a time precision to use.
	 *
	 * @param benchPrecision The precision the benchmark should run at
	 */
	public NanoBenchmark(final TimeUnit benchPrecision){
		//Assign the class variables from the constructor's parameters
		this.precision = benchPrecision;
		
		//Create a counter for the for loop (to get the nanosecond offset)
		final int LOOP_COUNT = 500;
		
		//Zero the nanosecond offset (more precision)
		BigDecimal offsetSum = BigDecimal.ZERO;
		
		for(int i = 0; i < LOOP_COUNT; i++){
			//Calculate the nanosecond offset
			offsetSum = offsetSum.add(BigDecimal.valueOf(calculateNanoOffset()));
		}
		
		//Get the nanosecond offset by dividing the offset sum by the loop count
		nanoOffset = offsetSum.divide(BigDecimal.valueOf(LOOP_COUNT)).longValue();
	}

	/**
	 * Creates a new instance of {@code NanoBenchmark} using
	 * {@code TimeUnit.MILLISECONDS} as the precision.
	 */
	public NanoBenchmark(){
		//Redirect to the overloaded constructor with unimplemented parameters being substituted by their default values
		this(TimeUnit.MILLISECONDS);
	}

	/** Starts the benchmark and logs the start time to {@code startTime}. */
	public void start(){
		//Get the current system timestamp using the precision given upon instantiation
		startTime = getCurrentTimestamp();
	}

	/** Stops the benchmark and logs the time taken to {@code elapsedTime}. **/
	public void stop(){
		//Calculate the elapsed amount of time since the start of the benchmark (current timestamp - initial timestamp)
		elapsedTime = getCurrentTimestamp() - startTime;
	}
	
	private static long calculateNanoOffset(){
		//Get the current timestamp in nanoseconds
		final long nanoTime = System.nanoTime();
		
		//Attempt to get the equivalent time in milliseconds (less accurate)
		final long nanoFromMilli = System.currentTimeMillis() * 1_000_000;
		
		//Calculate the difference between the two measurements
		return nanoFromMilli - nanoTime;
	}
	
	private long getCurrentTimestamp(){
		//Set the "magic numbers"
		final int TIME_MULTI = 1000;

		//Check the precision level
		switch(precision){
			case NANOSECONDS:{
				//Return the current system timestamp, in nanoseconds, with offset
				return nanoOffset + System.nanoTime();
			}
			
			case MICROSECONDS:{
				//Return the current system timestamp, in microseconds, with offset
				return nanoOffset + (System.nanoTime() / TIME_MULTI);
			}
				
			case SECONDS:{
				//Return the current system timestamp, in seconds
				return System.currentTimeMillis() / TIME_MULTI;
			}

			case MILLISECONDS:default:{
				//Return the current system timestamp, in milliseconds
				return System.currentTimeMillis();
			}
		}
	}

	/**
	 * Returns the time the benchmark was started at.
	 *
	 * @return <b>long</b> The time the benchmark to started at
	 */
	public long getStartTime(){
		//Return the time the benchmark started at
		return startTime;
	}

	/**
	 * Returns the time it took to run the benchmark.
	 *
	 * @return <b>long</b> The time it took for the benchmark to run
	 */
	public long getElapsedTime(){
		//Return the time it took for the benchmark to complete
		return elapsedTime;
	}
}
