import java.util.Comparator;

public class Episode implements Comparable <Episode>{
	//Episode Class: used for initializing and modifying episode objects
	//Variable initialization
	private String title;
	private int seasonNum, episodeNum;
	private boolean watched;
	private Time episodeTime;
	private static int indexChanger = 0;//i tried to implement indexChanger to figure out how to access episodes by number once the number of episodes in a show changed, but i couldn't figure it out in time

	public Episode(String titleInput, int seasonNumInput, int episodeNumInput, Time timeInput) {
		title = titleInput;
		seasonNum = seasonNumInput;
		episodeNum = episodeNumInput;
		episodeTime = timeInput;
		setWatched(false);
	}

	public String toString() {
		/* Purpose: outputs the info of an episode object
		 * Params: none
		 * Returns the properly formatted info(string)
		 */
		return String.format("Season %d, Episode %d: '%s'. Running time of %s.", seasonNum, episodeNum, getTitle(), episodeTime);
	}

	public int compareTo(Episode episode) {
		/* Purpose: sorts episodes by their titles alphabetically
		 * Params: the current episode
		 * Returns an int equal to the difference in both titles
		 */
		return this.title.compareToIgnoreCase(episode.title);
	}

	//GETTER AND SETTER METHODS: Used to retrieve values of private variables in other classes
	public int getEpisodeNum() {
		return episodeNum;
	}

	public int getSeasonNum() {
		return seasonNum;
	}

	public int getSeconds() {
		return (this.episodeTime.getHours()*3600)+(this.episodeTime.getMinutes()*60)+this.episodeTime.getSeconds();
	}

	public boolean isWatched() {
		return this.watched;
	}

	public void setWatched(boolean watched) {
		this.watched = watched;
	}
	
//	public static int getIndexChanger() {
//		return indexChanger;
//	}
//	
//	public static void setIndexChanger(int newIndexChanger) {
//		indexChanger = newIndexChanger;
//	}
	
	public Time getTime() {
		return episodeTime;
	}

	public String getTitle() {
		return title;
	}
}
class sortByEpisodeNum implements Comparator <Episode>{
	//sortByEpisodeNum class: used to sort episodes by their episode numbers
	public int compare(Episode episodeOne, Episode episodeTwo) {
		return episodeOne.getEpisodeNum()-episodeTwo.getEpisodeNum();
	}
}
class sortByTime implements Comparator <Episode>{
	//sortByTime class: used to sort episodes by their running times
	public int compare(Episode episodeOne, Episode episodeTwo) {
		return episodeOne.getSeconds()-episodeTwo.getSeconds();
	}
}