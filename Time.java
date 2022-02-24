import java.util.*;

public class Time {
	//Time Class: used for initializing time objects
	//Variable initialization
	private int hours, minutes, seconds;

	public Time(String timeInput) {
		hours = Integer.parseInt(timeInput.substring(0, timeInput.indexOf(":")));
		minutes = Integer.parseInt(timeInput.substring(timeInput.indexOf(":")+1, timeInput.lastIndexOf(":")));
		seconds = Integer.parseInt(timeInput.substring(timeInput.lastIndexOf(":")+1));
	}

	public String toString() {
		/* Purpose: outputs the info of a time object
		 * Params: none
		 * Returns the properly formatted info(string)
		 */
		String output = String.format("%d hours, %d minutes, %d seconds", getHours(), minutes, seconds);
		return output;
	}
	
	//GETTER AND SETTER METHODS: Used to retrieve values of private variables in other classes
	public int getHours() {
		return hours;
	}
	
	public int getMinutes() {
		return minutes;
	}

	public int getSeconds() {
		return seconds;
	}
}
