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

package com.spotrealms.javautils.math;

import java.util.Random;

/**
 * A series of methods for working with random
 * values. Provides value randomization via both
 * seeded and non-seeded methods for all eight of
 * the primitive data types. In addition, each of
 * the random generators allows the passing of
 * already defined {@link Random} objects for
 * special cases where, for example, an alternate
 * random number generator may be necessary like
 * {@link java.util.concurrent.ThreadLocalRandom},
 * which makes each of these random number generators
 * threadsafe, as by default, these methods are NOT
 * threadsafe.
 *
 * @author Spotrealms
 */
public final class RandomValue {
	/**
	 * Prevents instantiation of the utility class RandomValue.
	 *
	 * @throws RuntimeException If instantiation occurs
	 */
	private RandomValue(){ throw new RuntimeException("No " + this.getClass().getSimpleName() + " instance for you :)"); }

	/**
	 * Generates a random boolean value.
	 *
	 * @return <b>boolean</b> A random boolean value
	 */
	public static boolean randomBool(){
		//Create a new random number generator and return the resulting boolean
		return new Random().nextBoolean();
	}

	/**
	 * Generates a random boolean value using a
	 * given seed.
	 *
	 * @param seed The seed to feed to the random number generator
	 * @return <b>boolean</b> A random boolean value
	 */
	public static boolean randomBool(final long seed){
		//Create a random number generator using the given seed and return the random boolean
		return new Random(seed).nextBoolean();
	}

	/**
	 * Generates a random byte value.
	 *
	 * @param min The lowest byte in the range
	 * @param max The highest byte in the range
	 * @return <b>byte</b> A random byte value in the given range
	 */
	public static byte randomByte(final byte min, final byte max){
		//Create a new random number generator and return the resulting byte
		return randomByte(min, max, new Random());
	}

	/**
	 * Generates a random byte value using a
	 * given random number generator instance.
	 *
	 * @param min The lowest byte in the range
	 * @param max The highest byte in the range
	 * @param randGen The random number generator to use when generating the byte
	 * @return <b>byte</b> A random byte value in the given range
	 */
	public static byte randomByte(final byte min, final byte max, final Random randGen){
		//Assert that min is less than or equal to max
		assertMaxGtMin(min, max);

		//Calculate the random byte and return it
		return (byte) Math.floor(randGen.nextFloat() * (max - min + 1) + min);
	}

	/**
	 * Generates a random byte value using a
	 * given seed.
	 *
	 * @param min The lowest byte in the range
	 * @param max The highest byte in the range
	 * @param seed The seed to feed to the random number generator
	 * @return <b>byte</b> A random byte value in the given range
	 */
	public static byte randomByte(final byte min, final byte max, final long seed){
		//Create a random number generator using the given seed and return the random byte
		return randomByte(min, max, new Random(seed));
	}

	/**
	 * Generates a random char value.
	 *
	 * @param min The lowest char in the range
	 * @param max The highest char in the range
	 * @return <b>char</b> A random char value in the given range
	 */
	public static char randomChar(final char min, final char max){
		//Create a new random number generator and return the resulting char
		return randomChar(min, max, new Random());
	}

	/**
	 * Generates a random char value using a
	 * given random number generator instance.
	 *
	 * @param min The lowest char in the range
	 * @param max The highest char in the range
	 * @param randGen The random number generator to use when generating the char
	 * @return <b>char</b> A random char value in the given range
	 */
	public static char randomChar(final char min, final char max, final Random randGen){
		//Assert that min is less than or equal to max
		assertMaxGtMin((int) min, (int) max);

		//Calculate the random char and return it
		return (char) Math.floor(randGen.nextFloat() * (max - min + 1) + min);
	}

	/**
	 * Generates a random char value using a
	 * given seed.
	 *
	 * @param min The lowest char in the range
	 * @param max The highest char in the range
	 * @param seed The seed to feed to the random number generator
	 * @return <b>char</b> A random char value in the given range
	 */
	public static char randomChar(final char min, final char max, final long seed){
		//Create a random number generator using the given seed and return the random char
		return randomChar(min, max, new Random(seed));
	}

	/**
	 * Generates a random double value.
	 *
	 * @param min The lowest double in the range
	 * @param max The highest double in the range
	 * @return <b>double</b> A random double value in the given range
	 */
	public static double randomDouble(final double min, final double max){
		//Create a new random number generator and return the resulting double
		return randomDouble(min, max, new Random());
	}

	/**
	 * Generates a random double value using a
	 * given random number generator instance.
	 *
	 * @param min The lowest double in the range
	 * @param max The highest double in the range
	 * @param randGen The random number generator to use when generating the double
	 * @return <b>double</b> A random double value in the given range
	 */
	public static double randomDouble(final double min, final double max, final Random randGen){
		//Assert that min is less than or equal to max
		assertMaxGtMin(min, max);

		//Calculate the random double and return it
		return randGen.nextFloat() * (max - min) + min;
	}

	/**
	 * Generates a random double value using a
	 * given seed.
	 *
	 * @param min The lowest double in the range
	 * @param max The highest double in the range
	 * @param seed The seed to feed to the random number generator
	 * @return <b>double</b> A random double value in the given range
	 */
	public static double randomDouble(final double min, final double max, final long seed){
		//Create a random number generator using the given seed and return the random double
		return randomDouble(min, max, new Random(seed));
	}

	/**
	 * Generates a random float value.
	 *
	 * @param min The lowest float in the range
	 * @param max The highest float in the range
	 * @return <b>float</b> A random float value in the given range
	 */
	public static float randomFloat(final float min, final float max){
		//Create a new random number generator and return the resulting float
		return randomFloat(min, max, new Random());
	}

	/**
	 * Generates a random float value using a
	 * given random number generator instance.
	 *
	 * @param min The lowest float in the range
	 * @param max The highest float in the range
	 * @param randGen The random number generator to use when generating the float
	 * @return <b>float</b> A random float value in the given range
	 */
	public static float randomFloat(final float min, final float max, final Random randGen){
		//Assert that min is less than or equal to max
		assertMaxGtMin(min, max);

		//Calculate the random float and return it
		return randGen.nextFloat() * (max - min) + min;
	}

	/**
	 * Generates a random float value using a
	 * given seed.
	 *
	 * @param min The lowest float in the range
	 * @param max The highest float in the range
	 * @param seed The seed to feed to the random number generator
	 * @return <b>float</b> A random float value in the given range
	 */
	public static float randomFloat(final float min, final float max, final long seed){
		//Create a random number generator using the given seed and return the random float
		return randomFloat(min, max, new Random(seed));
	}

	/**
	 * Generates a random integer value.
	 *
	 * @param min The lowest integer in the range
	 * @param max The highest integer in the range
	 * @return <b>int</b> A random integer value in the given range
	 */
	public static int randomInt(final int min, final int max){
		//Create a new random number generator and return the resulting integer
		return randomInt(min, max, new Random());
	}

	/**
	 * Generates a random integer value using a
	 * given random number generator instance.
	 *
	 * @param min The lowest integer in the range
	 * @param max The highest integer in the range
	 * @param randGen The random number generator to use when generating the int
	 * @return <b>int</b> A random integer value in the given range
	 */
	public static int randomInt(final int min, final int max, final Random randGen){
		//Assert that min is less than or equal to max
		assertMaxGtMin(min, max);

		//Calculate the random integer and return it
		return (int) Math.floor(randGen.nextFloat() * (max - min + 1) + min);
	}

	/**
	 * Generates a random integer value using a
	 * given seed.
	 *
	 * @param min The lowest integer in the range
	 * @param max The highest integer in the range
	 * @param seed The seed to feed to the random number generator
	 * @return <b>int</b> A random integer value in the given range
	 */
	public static int randomInt(final int min, final int max, final long seed){
		//Create a random number generator using the given seed and return the random integer
		return randomInt(min, max, new Random(seed));
	}

	/**
	 * Generates a random long value.
	 *
	 * @param min The lowest long in the range
	 * @param max The highest long in the range
	 * @return <b>long</b> A random long value in the given range
	 */
	public static long randomLong(final long min, final long max){
		//Create a new random number generator and return the resulting long
		return randomLong(min, max, new Random());
	}

	/**
	 * Generates a random long value using a
	 * given random number generator instance.
	 *
	 * @param min The lowest long in the range
	 * @param max The highest long in the range
	 * @param randGen The random number generator to use when generating the long
	 * @return <b>long</b> A random long value in the given range
	 */
	public static long randomLong(final long min, final long max, final Random randGen){
		//Assert that min is less than or equal to max
		assertMaxGtMin(min, max);

		//Calculate the random long and return it
		return (long) Math.floor(randGen.nextFloat() * (max - min + 1) + min);
	}

	/**
	 * Generates a random long value using a
	 * given seed.
	 *
	 * @param min The lowest long in the range
	 * @param max The highest long in the range
	 * @param seed The seed to feed to the random number generator
	 * @return <b>long</b> A random long value in the given range
	 */
	public static long randomLong(final long min, final long max, final long seed){
		//Create a random number generator using the given seed and return the random long
		return randomLong(min, max, new Random(seed));
	}

	/**
	 * Generates a random short value.
	 *
	 * @param min The lowest short in the range
	 * @param max The highest short in the range
	 * @return <b>short</b> A random short value in the given range
	 */
	public static short randomShort(final short min, final short max){
		//Create a new random number generator and return the resulting short
		return randomShort(min, max, new Random());
	}

	/**
	 * Generates a random short value using a
	 * given random number generator instance.
	 *
	 * @param min The lowest short in the range
	 * @param max The highest short in the range
	 * @param randGen The random number generator to use when generating the short
	 * @return <b>short</b> A random short value in the given range
	 */
	public static short randomShort(final short min, final short max, final Random randGen){
		//Assert that min is less than or equal to max
		assertMaxGtMin(min, max);

		//Calculate the random short and return it
		return (short) Math.floor(randGen.nextFloat() * (max - min + 1) + min);
	}

	/**
	 * Generates a random short value using a
	 * given seed.
	 *
	 * @param min The lowest short in the range
	 * @param max The highest short in the range
	 * @param seed The seed to feed to the random number generator
	 * @return <b>short</b> A random short value in the given range
	 */
	public static short randomShort(final short min, final short max, final long seed){
		//Create a random number generator using the given seed and return the random short
		return randomShort(min, max, new Random(seed));
	}

	/**
	 * Asserts that {@code min} is not greater than
	 * {@code max}.
	 *
	 * @param min The lowest number in the range
	 * @param max The highest number in the range
	 * @throws IllegalArgumentException If {@code min} is greater than {@code max}
	 */
	private static void assertMaxGtMin(final Number min, final Number max) throws IllegalArgumentException {
		//Check if min is greater than max and throw an exception if it is
		if(MathUtil.compareGN(min, max) >= 1) throw new IllegalArgumentException("Minimum number can't be greater than maximum number.");
	}
}
