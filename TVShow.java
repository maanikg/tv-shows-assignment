import java.util.*;

public class TVShow implements Comparable<TVShow>{
	//TVShow Class: used for initializing and modifying tv show objects
	//Variable initialization
	private String title, genre, fileName;
	private int assignedNum;
	private ArrayList <Episode> episodes = new ArrayList <Episode>();
	private static int sortCriteriaStatic = 0;
	private ArrayList <Integer> seasons = new ArrayList<Integer>();

	public TVShow(String titleInput, String genreInput, Scanner fileInput) {
		/* Purpose: intializes a tv show object
		 * Params: the title of the show(String), the genre of the show(String), and a scanner to take in further input from the file
		 * No return
		 */
		title = titleInput;
		genre = genreInput;
		String line = "";
		if (genreInput.length()!=0) {
			do {
				line = fileInput.nextLine();
				int seasonNum = Integer.parseInt(line.substring(line.indexOf(' ')+1));
				seasons.add(seasonNum);
				int numEpisodes = Integer.parseInt(fileInput.nextLine());
				for (int count = 1; count <= numEpisodes; count++) {
					line = fileInput.nextLine().trim();
					int episodeNum = Integer.parseInt(line.substring(line.indexOf(' ')+1));
					String episodeTitle = fileInput.nextLine().trim();
					String episodeTimeInput = fileInput.nextLine().trim();
					episodes.add(new Episode(episodeTitle, seasonNum, episodeNum, new Time(episodeTimeInput)));
				}
			}while (fileInput.hasNextLine());
		}
		//		totalSeasons = totalSeasonsInput;
	}

	public static void displayEpisodes(int showIndex, int seasonNum, ArrayList<TVShow> shows) {
		/* Purpose: displays the titles of all the episodes in a season in a show
		 * Params: the index of the current show(int), an int representing the current season, and the arraylist of the current database of tv shows
		 * Returns void
		 */
		String showName = shows.get(showIndex).title;
		System.out.printf("\nThe episodes stored in the database under season %d of the show '%s' are listed below:%n", seasonNum, showName);
		for (int count = 0; count < shows.get(showIndex).episodes.size(); count++) {
			if (shows.get(showIndex).episodes.get(count).getSeasonNum()==seasonNum)
				System.out.println("(Episode " + shows.get(showIndex).episodes.get(count).getEpisodeNum() + ") " + shows.get(showIndex).episodes.get(count).getTitle());
		}
	}

	public static void displayEpisodeInfo(int showIndex, int seasonNum, int episodeNum, ArrayList<TVShow> shows) {
		/* Purpose: displays comprehensive info about one episode in a show
		 * Params: the index of the current show(int), an int representing the current season, an int representing the episode number, and the arraylist of the current database of tv shows
		 * Returns void
		 */
		ArrayList<Episode> tempArray = new ArrayList<Episode>(shows.get(showIndex).episodes);
		Collections.sort(tempArray, new sortByEpisodeNum());
		int index = 0;
		for (int count = 0; count < tempArray.size(); count++)
			if (tempArray.get(count).getSeasonNum()==seasonNum && tempArray.get(count).getEpisodeNum()==episodeNum)
				index = count;
		System.out.println("\n" + tempArray.get(index).getTitle() + ":");
		System.out.println("Season " + tempArray.get(index).getSeasonNum() + ", Episode " + episodeNum);
		System.out.println("Running Time: " + tempArray.get(index).getTime());
		if (tempArray.get(index).isWatched())
			System.out.println("This episode has been watched.");
		else
			System.out.println("This episode has not been watched.");
	}

	public static void watchEpisode(int showIndex, int seasonNum, int episodeNum, ArrayList<TVShow> shows) {
		/* Purpose: sets a specific episode in a show to 'watched'
		 * Params: the index of the current show(int), an int representing the current season, an int representing the episode number, and the arraylist of the current database of tv shows
		 * Returns void
		 */
		//		ArrayList<Episode> tempArray = new ArrayList<Episode>(shows.get(showIndex).episodes);
		//		Collections.sort(tempArray, new sortByEpisodeNum());
		int index = 0;
		for (int count = 0; count < shows.get(showIndex).episodes.size(); count++)
			if (shows.get(showIndex).episodes.get(count).getSeasonNum()==seasonNum && shows.get(showIndex).episodes.get(count).getEpisodeNum()==episodeNum)
				index = count;
		shows.get(showIndex).episodes.get(index).setWatched(true);
		System.out.printf("The episode '%s' has been set to 'watched'.%n", shows.get(showIndex).episodes.get(index).getTitle());
	}

	public static void removeEpisode(int showIndex, int seasonNum, int episodeNum, ArrayList<TVShow> shows) {
		/* Purpose: removes an episode from a specific season in a show
		 * Params: the index of the current show(int), an int representing the current season, an int representing the episode number, and the arraylist of the current database of tv shows
		 * Returns void
		 */
		for (int count = 0; count < shows.get(showIndex).episodes.size(); count++) {
			if (shows.get(showIndex).episodes.get(count).getSeasonNum()==seasonNum && shows.get(showIndex).episodes.get(count).getEpisodeNum()==episodeNum) {
				shows.get(showIndex).episodes.remove(count);
				break;
			}
		}
//		Episode.setIndexChanger(Episode.getIndexChanger()-1);
		//above doesn't work bc it assumes that all episodes have been shifted
		System.out.printf("Episode %d has been removed from this season.%n", episodeNum);
	}

	public static void removeEpisode(int showIndex, int seasonNum, int start, int end, ArrayList<TVShow> shows) {
		/* Purpose: removes a range of episodes from a specific season in a show
		 * Params: the index of the current show(int), an int representing the current season, an int representing the starting range of episodes, an int representing the ending range of episodes, and the arraylist of the current database of tv shows
		 * Returns void
		 */
		for (int count = 0; count < shows.get(showIndex).episodes.size(); count++) {
			if (shows.get(showIndex).episodes.get(count).getSeasonNum()==seasonNum && shows.get(showIndex).episodes.get(count).getEpisodeNum()>=start && shows.get(showIndex).episodes.get(count).getEpisodeNum()<=end) {
				shows.get(showIndex).episodes.remove(count);
				count--;
			}
		}
		System.out.printf("All episodes from %d to %d from this season have been removed.", start, end);
	}

	public static void removeEpisode(int showIndex, int seasonIndex, ArrayList<TVShow> shows) {
		/* Purpose: removes all watched episodes from a specific season in a show
		 * Params: the index of the current show(int), an int representing the current season, and the arraylist of the current database of tv shows
		 * Returns void
		 */
		for (int count = 0; count < shows.get(showIndex).getEpisodes().size(); count++) {
			if (shows.get(showIndex).episodes.get(count).getSeasonNum() == seasonIndex && shows.get(showIndex).episodes.get(count).isWatched()) {
				shows.get(showIndex).episodes.remove(count);
				count--;
			}
		}
		System.out.println("All watched episodes have been removed from this season.");
	}

	public static void sortEpisodes(int showIndex, int seasonNum, int sortCriteria, ArrayList<TVShow> shows, boolean displayInfo) {
		/* Purpose: sorts the episodes in a current season according to the criteria set by a user
		 * Params: the index of the current show(int), an int representing the current season, an int representing the sort criteria, and the arraylist of the current database of tv shows
		 * Returns void
		 */
		Collections.sort(shows, new sortByAssignedNum());

		if (sortCriteria == 1) //Sorting by episode number
			Collections.sort(shows.get(showIndex).episodes, new sortByEpisodeNum());
		else if (sortCriteria == 2) //Sorting by titles of episodes alphabetically
			Collections.sort(shows.get(showIndex).episodes);
		else if (sortCriteria == 3)  //Sorting by times of episodes
			Collections.sort(shows.get(showIndex).episodes, new sortByTime());
		sortCriteriaStatic = sortCriteria;
		if (displayInfo) {
			System.out.println("\nYour sorted season now looks like this:");
			System.out.printf(shows.get(showIndex).getTitle() + " (Season %d):%n", seasonNum);
			for (int count = 0; count < shows.get(showIndex).episodes.size(); count++) 
				if (shows.get(showIndex).episodes.get(count).getSeasonNum() == seasonNum) {
					System.out.print("(Episode " + shows.get(showIndex).episodes.get(count).getEpisodeNum() + ") ");
					System.out.print(shows.get(showIndex).episodes.get(count).getTitle() + ": ");
					System.out.println(shows.get(showIndex).episodes.get(count).getTime());
				}
		}

	}

	public static void addEpisode(int showIndex, int seasonNum, int episodeNum, String time, String episodeTitle, ArrayList<TVShow> shows) {
		/* Purpose: adds an episode to the current season of the show
		 * Params: the index of the current show(int), an int representing the current season, an int representing the episode number, a string representing the time input of the episode, and the arraylist of the current database of tv shows
		 * Returns void
		 */
		shows.get(showIndex).episodes.add(new Episode(episodeTitle, seasonNum, episodeNum, new Time(time)));
		System.out.printf("The episode %s(season %d, episode %d), with a running time of %s has been added to the database.%n", episodeTitle, seasonNum, episodeNum, time);
	}

	public int compareTo(TVShow show) {
		/* Purpose: sorts shows by their titles alphabetically
		 * Params: the current tv show
		 * Returns an int equal to the difference in both titles
		 */
		return this.title.compareToIgnoreCase(show.title);
	}

	//GETTER AND SETTER METHODS: Used to retrieve values of private variables in other classes 
	public String getTitle() {
		return this.title;
	}

	public String getGenre() {
		return genre;
	}

	public int getAssignedNum() {
		return this.assignedNum;
	}

	public static int getSortCriteria() {
		return sortCriteriaStatic;
	}

	public ArrayList <Episode> getEpisodes() {
		return episodes;
	}

	public ArrayList <Integer> getSeasons(){
		return seasons;
	}

	public String fileName() {
		return fileName;
	}

	public void setAssignedNum(int assignedNum) {
		this.assignedNum = assignedNum;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String toString() {
		return "";
	}
}

class sortByFileName implements Comparator<TVShow>{
	//sortByFileName class: used to sort shows by their file names alphabetically
	public int compare(TVShow showOne, TVShow showTwo) {
		return showOne.fileName().compareToIgnoreCase(showTwo.fileName());
	}
}

class sortByAssignedNum implements Comparator<TVShow>{
	//sortByAssignedNum class: used to sort shows by their assigned numbers
	public int compare(TVShow showOne, TVShow showTwo) {
		return showOne.getAssignedNum()-showTwo.getAssignedNum();
	}
}

class sortBySeasonNum implements Comparator<Integer>{
	//sortBySeasonNum class: used to sort shows by their seasons
	public int compare(Integer seasonOne, Integer seasonTwo) {
		return seasonOne - seasonTwo;
	}
}