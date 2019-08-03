package us.timinc.interactions.util;

import us.timinc.interactions.Interactions;

/**
 * A util class for generating random numbers.
 * 
 * @author Tim
 *
 */
public class RandUtil {
	/**
	 * Rolls a dice and attempts to get under a given value.
	 * 
	 * @param diceSides
	 *            The number of sides on the dice.
	 * @param rollUnder
	 *            The number to roll lower than to succeed.
	 * @return Whether or not the roll was successful.
	 */
	public static boolean rollSuccess(int diceSides, int rollUnder) {
		return rollDice(diceSides) < rollUnder;
	}

	/**
	 * Rolls a dice with the number of sides.
	 * 
	 * @param max
	 *            The number of sides on the dice.
	 * @return A random int between 1 and max.
	 */
	public static int rollDice(int max) {
		return roll(1, max);
	}

	/**
	 * Rolls a number between min and max.
	 * 
	 * @param min
	 *            The minimum number to roll.
	 * @param max
	 *            The maximum number to roll.
	 * @return A random int between min and max.
	 */
	public static int roll(int min, int max) {
		int range = max - min + 1;
		return (int) (int) (Math.random() * range) + min;
	}
}
