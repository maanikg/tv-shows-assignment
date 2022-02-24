/*Maanik Gogna
 * My TV Shows Collection
 * April 12, 2021
 * 
 * Program allows user to store a database of tv shows and modify it in a variety of ways.
 */
import java.io.*;
import java.util.*;
public class Driver {

	public static String validInputStringMenuOne(int subMenu, String input, Scanner userInput, ArrayList<TVShow> shows) {
		/* Purpose: Ensures that string input under the 1st menu is valid for each submenu.
		 * Params: an integer representing the current submenu(ie. the current action), a string representing the input ready to be validated, a scanner that takes in user input as needed, and an arraylist of the current shows. 
		 * Returns: the validated string
		 */
		if (subMenu == 2 || subMenu == 4 || subMenu == 5) {
			//When displaying info of a show, showing the status of a show, or removing a show
			if (input.length()!=0) {
				Collections.sort(shows);
				int tvShowLocation = Collections.binarySearch(shows, new TVShow(input, "", userInput));
				if (tvShowLocation < 0) {
					System.out.println("\nSorry, the TV show you entered does not exist in the database. Please check to ensure you typed the TV show name correctly.");
					if (subMenu != 0)
						System.out.println("You can also enter the number assigned to the TV show (see options 1, 2, or 5 to view the number assigned to each show).");
					else
						System.out.println("You can also enter the number assigned to the TV show (see options 1, 2, or 5 under submenu 1 to view the number assigned to each show).");
					System.out.println("You can also enter a blank field at any time to leave.");
				}
				return input;
			}else {
				System.out.println("Blank field entered. Returning to submenu 1.");
				return "";
			}
		}else if (subMenu == 3) {
			//When adding a tv show
			while (input.length()!=0)
				try {
					Scanner addShowFileInput = new Scanner(new File(input));
					Collections.sort(shows, new sortByFileName());
					TVShow tempShow = new TVShow("","", userInput);
					tempShow.setFileName(input);
					int fileIndex = Collections.binarySearch(shows, tempShow, new sortByFileName());
					if (fileIndex<0)
						return input;
					else {
						System.out.println("\nSorry, you have already entered in information from this file before.");
						System.out.println("What is the name of the file that you would like to read in? Do not forget any filename extensions such as 'txt'.");
						System.out.println("You can also enter a blank field at any time to leave.");
						input = userInput.nextLine();
						if (input.length()==0) {
							System.out.println("Blank field entered. Returning to submenu 1.");
							return "";
						}
					}
				}catch (FileNotFoundException e) {
					System.out.println("\nSorry, the file you entered does not exist. Please check to ensure you typed the file name correctly.");
					System.out.println("What is the name of the file that you would like to read in? Do not forget any filename extensions such as 'txt'.");
					System.out.println("You can also enter a blank field at any time to leave.");
					input = userInput.nextLine();
					if (input.length()==0) {
						System.out.println("Blank field entered. Returning to submenu 1.");
						return "";
					}
				}
			System.out.println("Blank field entered. Returning to submenu 1.");
			return "";
		}
		return "";
	}

	public static String validInputStringMenuTwo(int subMenu, int showIndex, String input, Scanner userInput, ArrayList<TVShow> shows) {
		/* Purpose: Ensures that string input under the 2nd menu is valid for each submenu.
		 * Params: an integer representing the current submenu(ie. the current action), an integer representing the current show index 
		 * 		a string representing the input ready to be validated, a scanner that takes in user input as needed, and an arraylist of the current shows. 
		 * Returns: the validated string
		 */
		if (subMenu == 0) {
			//when asking for show information each time submenu 2 is accessed
			if (input.length()!=0) {
				Collections.sort(shows);
				int tvShowLocation = Collections.binarySearch(shows, new TVShow(input, "", userInput));
				if (tvShowLocation < 0) {
					System.out.println("\nSorry, the TV show you entered does not exist in the database. Please check to ensure you typed the TV show name correctly.");
					System.out.println("You can also enter the number assigned to the TV show (see options 1, 2, or 5 under submenu 1 to view the number assigned to each show).");
					System.out.println("You can also enter a blank field at any time to leave.");
				}
				return input;
			}else {
				System.out.println("Blank field entered. Returning to the main menu.");
				return "";
			}
		}else if (subMenu == 2 || subMenu == 3) {
			//When displaying the info of a specific episode, or setting an episode to be watched
			Collections.sort(shows.get(showIndex).getEpisodes());
			int index = Collections.binarySearch(shows.get(showIndex).getEpisodes(), new Episode(input, 0, 0, new Time("00:00:00")));
			if (index>=0) {
				return input;
			}else if (input.length()== 0) {
				System.out.println("Blank field entered. Returning to submenu 2.");
				return "";
			}else{
				System.out.println("\nSorry, the episode name you entered does not exist in the database. Please check to ensure you typed the episode name correctly.");
				System.out.println("You can also enter the episode number of the episode.");
				System.out.println("You can also enter a blank field at any time to leave.");
				return input;
			}
		}else if (subMenu == 4) {
			//When adding an episode
			do {
				boolean errorMessage = false;
				try {
					String hoursStr = "", minutesStr = "", secondsStr = "";
					int hours = 0, minutes = 0, seconds = 0;
					hoursStr = input.substring(0, input.indexOf(":"));
					minutesStr = input.substring(input.indexOf(":")+1, input.lastIndexOf(":"));
					secondsStr = input.substring(input.lastIndexOf(":")+1);

					hours = Integer.parseInt(hoursStr);
					minutes = Integer.parseInt(minutesStr);
					seconds = Integer.parseInt(secondsStr);
					if ((seconds > 59 || minutes > 59) || (hours < 0 || minutes < 0 || seconds < 0) || (secondsStr.length()<2 || minutesStr.length()<2 || hoursStr.length()<2)) 
						errorMessage = true;
					else
						return input;
				}catch (NumberFormatException e){
					errorMessage = true;
				}catch (StringIndexOutOfBoundsException e) {
					errorMessage = true;
				}
				if (errorMessage) 
					if (input.length()!=0) {
						System.out.println("\nERROR - Please enter the length of the episode in the specified format (00:00:00).");
						System.out.println("There must exactly be two digits representing the seconds and minutes, and at least two digits representing the hours.");
						input = userInput.nextLine();
					}else {
						System.out.println("Blank field entered. Returning to submenu 2");
						return "";
					}
			}while (true);
		}
		else if (subMenu == 5) {
			//When removing an episode
			do {
				Collections.sort(shows.get(showIndex).getEpisodes());
				int index = Collections.binarySearch(shows.get(showIndex).getEpisodes(), new Episode(input, 0, 0, new Time("00:00:00")));
				if (index>=0)
					return input;
				else if (input.length()== 0) {
					System.out.println("Blank field entered. Returning to submenu 2.");
					return "";
				}else{
					System.out.println("\nSorry, the episode name you entered does not exist in the database. Please check to ensure you typed the episode name correctly.");
					System.out.println("You can also enter a blank field at any time to leave.");
					input = userInput.nextLine().trim();
				}
			}while (true);
		}
		return "";
	}

	public static Integer validInputIntMenuOne(int subMenu, Scanner userInput, String strInput, int operation, String title, ArrayList<TVShow> shows) {
		/* Purpose: Ensures that integer input under the 1st menu is valid for each submenu.
		 * Params: an integer representing the current submenu(ie. the current action), a scanner that takes in user input as needed, a string representing the input ready to be validated, 
		 *		an integer representing the specific action within each submenu(used when multiple inputs need to be validated for a specific submenu), the title of the current show, and an arraylist of the current shows. 
		 * Returns: the validated integer
		 */
		if (subMenu == 2 || subMenu == 5) {
			//When displaying the info of a specific show or showing the status of a show
			int showNum = Integer.parseInt(strInput);
			if (showNum <= 0 || showNum > shows.size()) {
				System.out.println("\nSorry, a TV show assigned to this number does not exist in the database.");
				System.out.println("You can also enter the title of the TV show you would like to see the information of. Do not forget spacing or spelling of words.");
				System.out.println("You can also enter a blank field at any time to leave.");
				return -1;
			}else
				return showNum;
		}else if (subMenu == 4) {
			//When removing a show or season of a show
			int choiceInputInt = 0;
			if (operation == 1)
				//If user chooses to input the episode by its episode number
				return validInputIntMenuOne(2, userInput, strInput, 0, "", shows);
			else if (operation == 2){
				//When asking for whether user wants to remove the full show or just a season
				do {
					try {
						choiceInputInt = Integer.parseInt(strInput);
						if (choiceInputInt == 1 || choiceInputInt == 2) 
							return choiceInputInt;
						else {
							System.out.printf("\nERROR - Please enter either 1 if you would like to completely remove %s, or 2 if you would like to remove a season.%n", title);
							System.out.println("You can also enter a blank field at any time to leave.");
							strInput = userInput.nextLine().trim();
						}
					}catch (NumberFormatException e) {
						if (strInput.length()!=0) {
							System.out.println("\nERROR - Please enter a valid integer.");
							System.out.println("Enter 1 if you would like to completely remove the show, or 2 if you would like to remove a season.");
							System.out.println("You can also enter a blank field at any time to leave.");
							strInput = userInput.nextLine().trim();
						}else {
							System.out.println("Blank field entered. Returning to submenu 1");
							return -1;
						}
					}
				}while (true);
			}else if (operation == 3) {
				//If removing a season of a show
				ArrayList<Integer> numSeasonsList = new ArrayList<Integer>();
				Collections.sort(shows);
				do {
					try {
						choiceInputInt = Integer.parseInt(strInput);
						int index = Collections.binarySearch(shows, new TVShow(title, "", new Scanner(System.in)));
						for (int count = 0; count < shows.get(index).getEpisodes().size(); count++) {
							Collections.sort(numSeasonsList); 
							int currSeason = shows.get(index).getEpisodes().get(count).getSeasonNum();
							if (Collections.binarySearch(numSeasonsList, currSeason)<0)
								numSeasonsList.add(currSeason);
						}
						for (int count = 0; count < numSeasonsList.size(); count++)
							if (choiceInputInt == numSeasonsList.get(count))
								return choiceInputInt;
						System.out.println("\nERROR - the season you entered does not exist in the database under this show.");
						System.out.println("Please enter the season # that you would like to remove from the show.");
						System.out.println("You can also enter a blank field at any time to leave.");
						strInput = userInput.nextLine().trim();
					}catch (NumberFormatException e) {
						if (strInput.length()!=0) {
							System.out.println("\nERROR - Please enter a valid integer.");
							System.out.println("Please enter the season # that you would like to remove from the show.");
							System.out.println("You can also enter a blank field at any time to leave.");
							strInput = userInput.nextLine().trim();
						}else {
							System.out.println("Blank field entered. Returning to submenu 1");
							return -1;
						}
					}
				}while (true);
			}
		}
		return null;
	}

	public static Integer validInputIntMenuTwo(int subMenu, int showNum, int seasonNum, Scanner userInput, String strInput, int operation, ArrayList<TVShow> shows) {
		/* Purpose: Ensures that integer input under the 2st menu is valid for each submenu.
		 * Params: an integer representing the current submenu(ie. the current action), an integer representing the current show, an integer representing the current season number, 
		 * 		a scanner that takes in user input as needed, a string representing the input ready to be validated, 
		 *		an integer representing the specific action within each submenu(used when multiple inputs need to be validated for a specific submenu), the title of the current show, and an arraylist of the current shows. 
		 * Returns: the validated integer
		 */
		if (subMenu == 0) {
			//when asking for show information each time submenu 2 is accessed
			if (operation == 1) {
				//If user chooses to input the show by its assigned number
				showNum = Integer.parseInt(strInput);
				if (showNum <= 0 || showNum > shows.size()) {
					System.out.println("\nSorry, a TV show assigned to this number does not exist in the database.");
					System.out.println("You can also enter the title of the TV show you would like to see the information of. Do not forget spacing or spelling of words.");
					System.out.println("You can also enter a blank field at any time to leave.");
					return -1;
				}else
					return showNum;
			}else if (operation == 2) {
				//When user inputs the specified season number
				do {
					try {
						seasonNum = Integer.parseInt(strInput);
						ArrayList<Integer> numSeasonsList = new ArrayList<Integer>();
						Collections.sort(shows, new sortByAssignedNum());
						for (int count = 0; count < shows.get(showNum).getEpisodes().size(); count++) {
							Collections.sort(numSeasonsList);
							int currSeason = shows.get(showNum).getEpisodes().get(count).getSeasonNum();
							if (Collections.binarySearch(numSeasonsList, currSeason)<0)
								numSeasonsList.add(currSeason);
						}
						for (int count = 0; count < shows.get(showNum).getSeasons().size(); count++) 
							if (seasonNum == shows.get(showNum).getSeasons().get(count))
								return seasonNum;

						if (seasonNum <=0) {
							System.out.println("\nERROR - Please enter a number greater than 0.");
							System.out.println("You can also enter a blank field at any time to leave.");
							strInput = userInput.nextLine().trim();
						}else {
							System.out.printf("Season %d does not exist in the database, so it has been created.%n", seasonNum);
							shows.get(showNum).getSeasons().add(seasonNum);
							return seasonNum;
						}
					}catch (NumberFormatException e) {
						if (strInput.length()!=0) {
							System.out.println("\nERROR - Please enter a valid integer.");
							System.out.printf("Please enter the season # that you would like to modify.");
							System.out.println("You can also enter a blank field at any time to leave.");
							strInput = userInput.nextLine().trim();
						}else {
							System.out.println("Blank field entered. Returning to the main menu.");
							return -1;
						}
					}
				}while (true);
			}
		}else if (subMenu == 2 || subMenu == 3) {
			//When user is displaying the info of a specific episode or when user is setting an episode to 'watched' 
			int episodeNum = Integer.parseInt(strInput);
			Collections.sort(shows);
			boolean validEpisode = false;
			int seasonSize = 0;
			for (int count = 0; count < shows.get(showNum).getEpisodes().size(); count++) {
				if (shows.get(showNum).getEpisodes().get(count).getEpisodeNum() == episodeNum)
					validEpisode = true;
				if (shows.get(showNum).getEpisodes().get(count).getSeasonNum()==seasonNum)
					seasonSize++;
			}

			if (!validEpisode || episodeNum > seasonSize) {
				System.out.println("\nSorry, no episode that is currently in the database under this season is assigned to this episode number. Please try again.");
				System.out.println("You can also enter the title of the episode you would like to see the information of.");
				System.out.println("You can also enter a blank field at any time to leave.");
				return -1;
			}else if (episodeNum <= 0) {// check if episode is even in season || episodeNum > shows.get(index)) {
				System.out.println("\nERROR - Please enter a number greater than 0.");
				System.out.println("You can also enter the title of the episode you would like to see the information of.");
				System.out.println("You can also enter a blank field at any time to leave.");
				return -1;
			}else
				return episodeNum;
		}else if (subMenu == 4) {
			//When user is adding an episode
			boolean validInt = false;
			while (!validInt) {
				try {
					boolean validEpisodeNum = true;
					int episodeNum = Integer.parseInt(strInput);
					for (int count = 0; count < shows.get(showNum).getEpisodes().size(); count++)
						if (shows.get(showNum).getEpisodes().get(count).getEpisodeNum() == episodeNum && shows.get(showNum).getEpisodes().get(count).getSeasonNum()==seasonNum)
							validEpisodeNum = false;
					if (!validEpisodeNum) {
						System.out.println("\nSorry, another episode in this season already has this episode number. Please try again.");
						System.out.println("You can also enter a blank field at any time to leave.");
						strInput = userInput.nextLine().trim();
					} else if (episodeNum<1) {
						System.out.println("\nERROR - Please enter a valid integer.");
						System.out.println("Enter the episode number that is associated with this episode.");
						System.out.println("You can also enter a blank field at any time to leave.");
						strInput = userInput.nextLine().trim();
					}else
						return episodeNum;
				}catch (NumberFormatException e) {
					if (strInput.length()!=0) {
						System.out.println("\nERROR - Please enter a valid integer.");
						System.out.println("Enter the episode number that is associated with this episode.");
						System.out.println("You can also enter a blank field at any time to leave.");
						strInput = userInput.nextLine().trim();
					}else {
						System.out.println("Blank field entered. Returning to submenu 2.");
						return -1;
					}		
				}
			}
		}else if (subMenu == 5) {
			//When user is removing an episode
			if (operation == 0) {
				//When user is selecting which option to remove(title, ep. number, etc.)
				do {
					try {
						int choiceInputInt = Integer.parseInt(strInput);
						if (choiceInputInt > 4 || choiceInputInt < 1) {
							System.out.println("\nERROR - Please enter a number between 1 and 4 inclusive.");
							System.out.println("Enter 1(title), 2(episode number), 3(range of episodes), or 4(all watched episodes) to indicate your choice.");
							System.out.println("You can also enter a blank field at any time to leave.");
							strInput = userInput.nextLine().trim();
						}else
							return choiceInputInt;
					}catch (NumberFormatException e) {
						if (strInput.length()!=0) {
							System.out.println("\nERROR - Please enter a valid integer.");
							System.out.println("Enter 1(title), 2(episode number), 3(range of episodes), or 4(all watched episodes) to indicate your choice.");
							System.out.println("You can also enter a blank field at any time to leave.");
							strInput = userInput.nextLine().trim();
						}else {
							System.out.println("Blank field entered. Returning to submenu 2.");
							return -1;
						}

					}
				}while (true);
			}else if (operation == 2) {
				//When user is removing a specific episode
				do {
					try {
						int episodeNum = Integer.parseInt(strInput);
						Collections.sort(shows);
						boolean validEpisode = false;
						int seasonSize = 0;
						for (int count = 0; count < shows.get(showNum).getEpisodes().size(); count++) {
							if (shows.get(showNum).getEpisodes().get(count).getEpisodeNum() == episodeNum)
								validEpisode = true;
							if (shows.get(showNum).getEpisodes().get(count).getSeasonNum()==seasonNum)
								seasonSize++;
						}

						if (!validEpisode || episodeNum > seasonSize) {
							System.out.println("\nSorry, no episode that is currently in the database under this season is assigned to this episode number. Please try again.");
							System.out.println("You can also enter a blank field at any time to leave.");
							strInput = userInput.nextLine().trim();
						}else if (episodeNum <= 0) {
							System.out.println("\nERROR - Please enter a number greater than 0.");
							System.out.println("You can also enter a blank field at any time to leave.");
							strInput = userInput.nextLine().trim();
						}else
							return episodeNum;
					}catch(NumberFormatException e) {
						if (strInput.length()!=0) {
							System.out.println("\nERROR - Please enter a valid integer.");
							System.out.println("You can also enter a blank field at any time to leave.");
							strInput = userInput.nextLine().trim();
						}else {
							System.out.println("Blank field entered. Returning to submenu 2.");
							return -1;
						}
					}
				}while (true);
			}else if (operation == 3 || operation == 4) {
				//When user is removing a range of episodes
				int startingNum = 0;
				if (operation == 4) {
					startingNum = Integer.parseInt(strInput.substring(strInput.length()-1));
					strInput = strInput.substring(0, strInput.length()-1);
				}
				do {
					try {
						int episodeNum = Integer.parseInt(strInput);
						if (episodeNum < 1) {
							System.out.println("\nERROR - Please enter a number greater than 0.");
							System.out.println("Please enter the starting range of the episodes you would like to remove.");
							System.out.println("You can also enter a blank field at any time to leave.");
							strInput = userInput.nextLine().trim();
						}else if (operation == 4 && episodeNum <= startingNum) {
							System.out.println("\nERROR - Please enter a number representing the ending range that is greater than your starting range.");
							System.out.println("You can also enter a blank field at any time to leave.");
							strInput = userInput.nextLine().trim();
						}else 
							return episodeNum;
					}catch(NumberFormatException e) {
						if (strInput.length()!=0) {
							System.out.println("\nERROR - Please enter a valid integer.");
							if (operation == 3)
								System.out.println("Please enter the starting range of the episodes you would like to remove.");
							else
								System.out.println("Please enter the ending range of the episodes you would like to remove.");
							System.out.println("You can also enter a blank field at any time to leave.");
							strInput = userInput.nextLine().trim();
						}else {
							System.out.println("Blank field entered. Returning to submenu 2.");
							return -1;
						}
					}
				}while (true);
			}
		}else if (subMenu == 6) {
			//When user is sorting episodes
			if (operation == 1) {
				//When user is selecting which option to sort by
				do {
					try {
						int choiceInputInt = Integer.parseInt(strInput);
						if (choiceInputInt > 3 || choiceInputInt < 1) {
							System.out.println("\nERROR - Please enter a number between 1 and 3 inclusive to indicate your choice.");
							System.out.println("Enter 1 to sort by episode number, 2 to sort by episode title, and 3 to sort by time.");
							System.out.println("You can also enter a blank field at any time to leave.");
							strInput = userInput.nextLine();
						}else
							return choiceInputInt;
					}catch (NumberFormatException e) {
						if (strInput.length()!=0) {
							System.out.println("\nERROR - Please enter a valid integer.");
							System.out.println("Enter 1 to sort by episode number, 2 to sort by episode title, and 3 to sort by time.");
							System.out.println("You can also enter a blank field at any time to leave.");
							strInput = userInput.nextLine();
						}else {
							System.out.println("Blank field entered. Returning to submenu 2.");
							return -1;
						}
					}
				}while (true);
			}
		}
		return null;
	}

	public static void displayShows(ArrayList<TVShow> shows) {
		/* Purpose: Displays a list of all show titles
		 * Params: the arraylist of all the current shows
		 * Returns void
		 */
		String suffix, verbIs;
		if (shows.size()==1) {
			suffix = "";
			verbIs = "is";
		}else {
			suffix = "s";
			verbIs = "are";
		}
		System.out.printf("\nSo far, your database of TV shows contains %d show%s. The title%s of the show%s %s as follows:%n", shows.size(), suffix, suffix, suffix, verbIs);
		for (int count = 0; count < shows.size(); count++) 
			System.out.println("(" + shows.get(count).getAssignedNum() + ") " + shows.get(count).getTitle());
	}

	public static void displayShowInfo(ArrayList<TVShow> shows, int showIndex) {
		/* Purpose: Displays comprehensive info about a specific show.
		 * Params: the arraylist of all the current shows, and the title of the show
		 * Returns void
		 */
		int hours = 0, minutes = 0, seconds = 0, numEpisodes;
		String genre, title;
		String suffixStr = "";

		Collections.sort(shows);
		title = shows.get(showIndex).getTitle();
		genre = shows.get(showIndex).getGenre();
		numEpisodes = shows.get(showIndex).getEpisodes().size();

		for (int count = 0; count < shows.get(showIndex).getEpisodes().size(); count++)
			seconds+=shows.get(showIndex).getEpisodes().get(count).getSeconds();

		Collections.sort(shows.get(showIndex).getSeasons(), new sortBySeasonNum());

		if (shows.get(showIndex).getSeasons().size()==1)
			suffixStr = "";
		else 
			suffixStr = "s";

		hours = seconds/3600;
		seconds%=3600;
		minutes = seconds/60;
		seconds%=60;
		if (shows.get(showIndex).getSeasons().size()!=0) {
			System.out.printf("%n%s (%d):%n", title, shows.get(showIndex).getAssignedNum());
			System.out.printf("%s genre%n", genre);
			System.out.printf("%d season%s (Season%s ", shows.get(showIndex).getSeasons().size(), suffixStr, suffixStr);
			for (int count = 0; count < shows.get(showIndex).getSeasons().size()-1; count++) 
				System.out.print(shows.get(showIndex).getSeasons().get(count) + ", ");
			System.out.println(shows.get(showIndex).getSeasons().get(shows.get(showIndex).getSeasons().size()-1) + ")");

			if (numEpisodes>1)
				suffixStr = "s";
			System.out.printf("%d episode%s%n", numEpisodes, suffixStr);
			System.out.printf("Total running time: %02d:%02d:%02d%n", hours, minutes, seconds);
		}else {
			System.out.printf("%n%s (%d):%n", title, shows.get(showIndex).getAssignedNum());
			System.out.printf("%s genre%n", genre);
			System.out.println("0 seasons");
		}
	}

	public static void addShow(ArrayList<TVShow> shows, String fileName, int additionInt) throws FileNotFoundException {
		/* Purpose: adds a show to the database based on input from a file specified by a user
		 * Params: the arraylist of all the current shows, the name of the file that information will be inputted from(String), and an addition counter that is used to assign a different number to each show(integer).  
		 * Returns void
		 */
		Scanner fileInput = new Scanner(new File(fileName));

		String showTitle = fileInput.nextLine().trim();
		String genre = fileInput.nextLine().trim();
		shows.add(new TVShow(showTitle, genre, fileInput));
		shows.get(shows.size()-1).setFileName(fileName);

		shows.get(shows.size()-1).setAssignedNum(1 + additionInt);
		System.out.println("Information was successfully entered from the file.");
	}

	public static void removeShow(ArrayList<TVShow> shows, int showIndex, int seasonNum) {
		/* Purpose: removes a specific season from a show from the database
		 * Params: the arraylist of all the current shows, the index of the current show(int), and an integer representing the current season  
		 * Returns void
		 */
		String title = shows.get(showIndex).getTitle();
		Collections.sort(shows);
		for (int count = 0; count < shows.get(showIndex).getEpisodes().size(); count++) {
			if (count>=shows.get(showIndex).getEpisodes().size())
				break;
			if (shows.get(showIndex).getEpisodes().get(count).getSeasonNum() == seasonNum) {
				shows.get(showIndex).getEpisodes().remove(count);
				count--;
			}
		}
		Collections.sort(shows.get(showIndex).getSeasons());
		shows.get(showIndex).getSeasons().remove(Collections.binarySearch(shows.get(showIndex).getSeasons(), seasonNum));

		ArrayList<Integer> numSeasons = new ArrayList<Integer>();
		for (int count = 0; count < shows.get(showIndex).getEpisodes().size();count++) {
			Collections.sort(numSeasons);
			if (Collections.binarySearch(numSeasons, shows.get(showIndex).getEpisodes().get(count).getSeasonNum())<0) 
				numSeasons.add(shows.get(showIndex).getEpisodes().get(count).getSeasonNum());
		}

		System.out.printf("Season %d from the show %s has been removed from the database.%n", seasonNum, title);
	}

	public static void removeShow(ArrayList<TVShow> shows, int showIndex) {
		/* Purpose: removes an entire show from the database
		 * Params: the arraylist of all the current shows, and the index of the current show(int)  
		 * Returns void
		 */
		Collections.sort(shows);
		String title = shows.get(showIndex).getTitle();
		int showLocation = Collections.binarySearch(shows, new TVShow(title, "", new Scanner(System.in)));
		int removedAssignedNum = shows.get(showLocation).getAssignedNum();
		String showTitleCaseSensitive = shows.get(showLocation).getTitle();
		shows.remove(shows.get(showLocation));

		Collections.sort(shows, new sortByAssignedNum());
		for (int count = removedAssignedNum-1; count < shows.size(); count++) 
			shows.get(count).setAssignedNum(count+1);
		System.out.printf("The show %s has been removed from the database.%n", showTitleCaseSensitive);
	}

	public static void showStatus(ArrayList<TVShow> shows, int showIndex) {
		/* Purpose: displays how much of the show(episodes, seasons) have been watched
		 * Params: the arraylist of all the current shows, and the index of the current show(int)  
		 * Returns void
		 */
		Collections.sort(shows);
		int totalUnwatchedEpisodes = 0;
		String titleCaseSensitive = shows.get(showIndex).getTitle();
		ArrayList<Integer> seasonsList = new ArrayList<Integer>();
		ArrayList<Integer >unwatchedSeasons = new ArrayList<Integer>();
		String suffix, verbIs;

		System.out.printf("%n%s (%d): %n", titleCaseSensitive, shows.get(showIndex).getAssignedNum());

		Collections.sort(shows.get(showIndex).getSeasons());
		for (int count = 0; count < shows.get(showIndex).getSeasons().size(); count++) {
			int sameSeasonWatchedCounter = 0, sameSeasonTotalCounter = 0;
			System.out.printf("Season %d - ", shows.get(showIndex).getSeasons().get(count));
			for (int subCount = 0; subCount < shows.get(showIndex).getEpisodes().size(); subCount++) {
				if (shows.get(showIndex).getEpisodes().get(subCount).getSeasonNum() == shows.get(showIndex).getSeasons().get(count)) {
					sameSeasonTotalCounter++;
					if (shows.get(showIndex).getEpisodes().get(subCount).isWatched())
						sameSeasonWatchedCounter++;
					else
						totalUnwatchedEpisodes++;
				}
				if (subCount+1==shows.get(showIndex).getEpisodes().size() && sameSeasonWatchedCounter == 0)  
					unwatchedSeasons.add(shows.get(showIndex).getSeasons().get(count));
			}
			System.out.printf("%d episodes watched out of %d total episodes%n", sameSeasonWatchedCounter, sameSeasonTotalCounter);
		}

		if (unwatchedSeasons.size()==1) {
			suffix = "";
			verbIs = "is";
		}else {
			suffix = "s";
			verbIs = "are";
		}
		System.out.printf("There %s %d season%s that %s completely unwatched out of the entire show.%n", verbIs, unwatchedSeasons.size(), suffix, verbIs);	
		if (totalUnwatchedEpisodes==1) {
			suffix = "";
			verbIs = "is";
		}else {
			suffix = "s";
			verbIs = "are";
		}
		System.out.printf("There %s %d episode%s that %s unwatched out of the entire show.%n", verbIs, totalUnwatchedEpisodes, suffix, verbIs);
	}

	public static int displayMenu (int menuNum, Scanner input, ArrayList<TVShow> shows) throws IOException {
		/* Purpose: displays the main and submenus
		 * Params: an int that represented the previous menu option selected by the user, a scanner that allows for user input, and the arraylist of the current shows in the database
		 * Returns an integer representing the menu choice selected by the user
		 */
		int menuOptions = 0;
		if (menuNum == 0 || (menuNum == 2 && shows.size() == 0)) {
			menuOptions = 3;
			if (menuNum == 2)
				System.out.println("There are no tv shows in the database yet, so submenu 2 has no functionality. Please try again when there is at least one show stored in the database.");
			System.out.println ("\n----------  MAIN MENU  -----------");
			System.out.println ("1) Accessing your TV shows list");
			System.out.println ("2) Accessing within a particular TV show");
			System.out.println ("3) Exit");
			System.out.println ("----------------------------------");
		}
		else if (menuNum == 1) {
			menuOptions = 6;
			System.out.println ("\n---------  SUB-MENU #1  ----------");
			System.out.println ("1) Display a list of all your TV shows"); // Just the CD titles, numbered in order
			System.out.println ("2) Display info on a particular TV show"); 
			System.out.println ("3) Add a TV show"); // Adds a CD by reading from input file
			System.out.println ("4) Remove (show or season)");
			System.out.println ("5) Show status");
			System.out.println ("6) Return back to main menu.");
			System.out.println ("----------------------------------");
		}
		else if (menuNum == 2 && shows.size()!=0){
			menuOptions = 7;
			System.out.println ("\n---------  SUB-MENU #2  ----------");
			System.out.println ("1) Display all episodes (in the last sorted order) ");
			System.out.println ("2) Display info on a particular episode ");
			System.out.println ("3) Watch an episode");
			System.out.println ("4) Add an episode");
			System.out.println ("5) Remove episode(s) (4 options)");
			System.out.println ("6) Sort episodes (3 options)");
			System.out.println ("7) Return back to main menu");
			System.out.println ("----------------------------------");
		}

		int menuChoice = 0;
		System.out.println();
		while (menuChoice <1 || menuChoice > menuOptions) {
			System.out.print ("Please enter your choice:  ");
			try {
				menuChoice = Integer.parseInt(input.nextLine().trim());
				if (menuChoice < 1 || menuChoice > menuOptions)
					System.out.println("ERROR - Please enter a valid option. You can select any option from 1 to " + menuOptions + ".\n");
			}catch (NumberFormatException e) {
				System.out.println("ERROR - Please enter a valid number.\n");
			}
		}
		return menuChoice;
	}

	public static void main (String[] args) throws IOException {
		//Variable initialization
		Scanner userInput = new Scanner(System.in);
		ArrayList <TVShow> tvShows = new ArrayList <TVShow>();
		int mainMenuChoice, subMenuChoice = 0, assignedNumCounter = 0, showNum = 0;
		Integer seasonNum = 0;
		boolean keepAsking = true, currentlyMenuTwo = false, notEmptySeason = false;
		String tvShowName = "", tvShowNameCaseSensitive;

		//Initial menu display
		mainMenuChoice = displayMenu (0, userInput, tvShows);
		if (mainMenuChoice == 1)
			subMenuChoice = displayMenu (1, userInput, tvShows);
		else if (mainMenuChoice == 2) {
			subMenuChoice = 0;
			mainMenuChoice = displayMenu (2, userInput, tvShows);
		}else
			keepAsking = false;

		while (keepAsking) {
			if (mainMenuChoice == 1) {
				currentlyMenuTwo = false;
				if (subMenuChoice == 1) {
					//If displaying all tv shows
					if (tvShows.size()!=0)
						displayShows(tvShows);
					else
						System.out.println("There are no tv shows in the database yet, so there is nothing to display.");
				}else if (subMenuChoice == 2) {
					//If displaying info of a specific tv show
					if (tvShows.size()!=0) {
						System.out.println("Enter the title of the TV show you would like to see the information of. Do not forget spacing or spelling of words.");
						System.out.println("You can also enter the number assigned to the TV show (see options 1, 2, or 5 to view the number assigned to each show).");
						System.out.println("You can also enter a blank field at any time to leave.");
						int index = 0;
						boolean validInput = false;
						while (!validInput) {
							tvShowName = userInput.nextLine().trim();
							try {
								showNum = validInputIntMenuOne(subMenuChoice, userInput, tvShowName, 0, "", tvShows);
								if (showNum >= 0) {
									Collections.sort(tvShows, new sortByAssignedNum());
									TVShow tempShow = new TVShow("", "", userInput);
									tempShow.setAssignedNum(showNum);
									index = Collections.binarySearch(tvShows, tempShow, new sortByAssignedNum());
									tvShowName = tvShows.get(index).getTitle();
									displayShowInfo(tvShows, index);
									validInput = true;
								}
							}catch (NumberFormatException e) {
								tvShowName = validInputStringMenuOne(subMenuChoice, tvShowName, userInput, tvShows);
								Collections.sort(tvShows);
								index = Collections.binarySearch(tvShows, new TVShow(tvShowName, "", userInput));
								if (index>=0) {
									displayShowInfo(tvShows, index);
									validInput = true;
								}else if (tvShowName.length()== 0)
									validInput = true;
							}
						}
					}else
						System.out.println("There are no tv shows in the database yet, so there is nothing to display.");
				}else if (subMenuChoice == 3) {
					//If adding a tv show
					System.out.println("What is the name of the file that you would like to read in? Do not forget any filename extensions such as 'txt'.");
					System.out.println("You can also enter a blank field at any time to leave.");
					String fileName = userInput.nextLine();
					fileName = validInputStringMenuOne(subMenuChoice, fileName, userInput, tvShows);
					if (fileName.length()!=0)
						addShow(tvShows, fileName, assignedNumCounter);
					assignedNumCounter++;
				}else if (subMenuChoice == 4) {
					//If removing a tv show
					if (tvShows.size()!=0) {
						System.out.println("Enter the name of the TV show that you would like to either completely remove, or remove a season from.");
						System.out.println("You can also enter the number assigned to the TV show (see options 1, 2, or 5 to view the number assigned to each show).");
						System.out.println("You can also enter a blank field at any time to leave.");
						//Asking for name of show
						boolean validInput = false;
						while (!validInput) {
							tvShowName = userInput.nextLine().trim();
							try {
								showNum = validInputIntMenuOne(subMenuChoice, userInput, tvShowName, 1, "", tvShows);
								if (showNum >= 0) {
									Collections.sort(tvShows, new sortByAssignedNum());
									TVShow tempShow = new TVShow("", "", userInput);
									tempShow.setAssignedNum(showNum);
									int index = Collections.binarySearch(tvShows, tempShow, new sortByAssignedNum());
									tvShowName = tvShows.get(index).getTitle();

									ArrayList<TVShow> tempArray = new ArrayList<TVShow>(tvShows);
									Collections.sort(tempArray);
									index = Collections.binarySearch(tempArray, new TVShow(tvShowName, "", userInput));
									String showNameCaseSensitive = tempArray.get(index).getTitle();

									System.out.printf("\nWould you like to completely remove %s from the database, or would you like to remove a specific season from the show?%n", showNameCaseSensitive);
									System.out.printf("Enter 1 if you would like to completely remove %s, or 2 if you would like to remove a season.%n", showNameCaseSensitive);
									System.out.println("You can also enter a blank field at any time to leave.");
									//Asking whether user wants to remove full show or just a season

									String choiceInput = userInput.nextLine().trim();
									int choiceInputInt = validInputIntMenuOne(subMenuChoice, userInput, choiceInput, 2, tvShowName, tvShows);
									if (choiceInputInt !=-1) { 
										if (choiceInputInt==1)
											removeShow(tvShows, index);
										else {
											//Asking for specified seasons
											ArrayList<Integer> numSeasonsList = new ArrayList<Integer>();

											System.out.printf("\nPlease enter the season # that you would like to remove from the show %s.%n", showNameCaseSensitive);
											System.out.print("The following seasons can be removed: ");
											for (int count = 0; count < tvShows.get(index).getEpisodes().size(); count++) {
												Collections.sort(numSeasonsList); //CHECK THIS LINE
												int currSeason = tvShows.get(index).getEpisodes().get(count).getSeasonNum();
												if (Collections.binarySearch(numSeasonsList, currSeason)<0)
													numSeasonsList.add(currSeason);
											}
											Collections.sort(numSeasonsList);
											for (int count = 0; count <numSeasonsList.size()-1; count++)
												System.out.print(numSeasonsList.get(count) + ", ");
											System.out.println(numSeasonsList.get(numSeasonsList.size()-1));

											choiceInput = userInput.nextLine().trim();
											choiceInputInt = validInputIntMenuOne(subMenuChoice, userInput, choiceInput, 3, tvShowName, tvShows);
											if (choiceInputInt!=-1)
												removeShow(tvShows, index, choiceInputInt);
										}
									}
									validInput = true;
								}
							}catch (NumberFormatException e) {
								tvShowName = validInputStringMenuOne(subMenuChoice, tvShowName, userInput, tvShows);
								Collections.sort(tvShows);
								if (Collections.binarySearch(tvShows, new TVShow(tvShowName, "", userInput))>=0) {
									ArrayList<TVShow> tempArray = new ArrayList<TVShow>(tvShows);
									Collections.sort(tempArray);
									int index = Collections.binarySearch(tempArray, new TVShow(tvShowName, "", userInput));
									String showNameCaseSensitive = tempArray.get(index).getTitle();
									System.out.printf("\nWould you like to completely remove %s from the database, or would you like to remove a specific season from the show?%n", showNameCaseSensitive);
									System.out.printf("Enter 1 if you would like to completely remove %s, or 2 if you would like to remove a season.%n", showNameCaseSensitive);
									System.out.println("You can also enter a blank field at any time to leave.");
									//Asking whether user wants to remove full show or just a season
									String choiceInput = userInput.nextLine().trim();

									int choiceInputInt = validInputIntMenuOne(subMenuChoice, userInput, choiceInput, 2, tvShowName, tvShows);
									if (choiceInputInt !=-1) { 
										if (choiceInputInt==1)
											removeShow(tvShows, index);
										else {
											//Asking for specified seasons
											ArrayList<Integer> numSeasonsList = new ArrayList<Integer>();
											System.out.printf("\nPlease enter the season # that you would like to remove from the show %s.%n", showNameCaseSensitive);
											System.out.print("The following seasons can be removed: ");
											for (int count = 0; count < tvShows.get(index).getEpisodes().size(); count++) {
												Collections.sort(numSeasonsList);
												int currSeason = tvShows.get(index).getEpisodes().get(count).getSeasonNum();
												if (Collections.binarySearch(numSeasonsList, currSeason)<0)
													numSeasonsList.add(currSeason);
											}
											Collections.sort(numSeasonsList);
											for (int count = 0; count <numSeasonsList.size()-1; count++)
												System.out.print(numSeasonsList.get(count) + ", ");
											System.out.println(numSeasonsList.get(numSeasonsList.size()-1));

											choiceInput = userInput.nextLine().trim();
											choiceInputInt = validInputIntMenuOne(subMenuChoice, userInput, choiceInput, 3, tvShowName, tvShows);
											if (choiceInputInt!=-1)
												removeShow(tvShows, index, choiceInputInt);
										}
									}
									validInput = true;
								}else if (tvShowName.length()== 0)
									validInput = true;
							}
						}
					}else
						System.out.println("There are no tv shows in the database yet, so there is nothing to remove.");
				}else if (subMenuChoice == 5) {
					//If seeing the status of a tv show
					if (tvShows.size()!=0) {
						System.out.println("Enter the title of the TV show you would like to see the information of. Do not forget spacing or spelling of words.");
						System.out.println("You can also enter the number assigned to the TV show (see options 1, 2, or 5 to view the number assigned to each show).");
						System.out.println("You can also enter a blank field at any time to leave.");
						//Asking for title/assigned number of show
						boolean validInput = false;
						while (!validInput) {
							int index = 0;
							tvShowName = userInput.nextLine().trim();
							try {
								showNum = validInputIntMenuOne(subMenuChoice, userInput, tvShowName, 0, "", tvShows);
								if (showNum >= 0) {
									Collections.sort(tvShows, new sortByAssignedNum());
									TVShow tempShow = new TVShow("", "", userInput);
									tempShow.setAssignedNum(showNum);
									index = Collections.binarySearch(tvShows, tempShow, new sortByAssignedNum());
									showStatus(tvShows, index);
									validInput = true;
								}
							}catch (NumberFormatException e) {
								tvShowName = validInputStringMenuOne(subMenuChoice, tvShowName, userInput, tvShows);
								Collections.sort(tvShows);
								if (Collections.binarySearch(tvShows, new TVShow(tvShowName, "", userInput))>=0) {
									Collections.sort(tvShows);
									index = Collections.binarySearch(tvShows, new TVShow(tvShowName, "", userInput));
									showStatus(tvShows, index);
									validInput = true;
								}else if (tvShowName.length()== 0)
									validInput = true;
							}
						}
					}else
						System.out.println("There are no tv shows in the database yet, so there is nothing to display.");
				}else if (subMenuChoice == 6)
					//If returning to main menu
					mainMenuChoice = displayMenu (0, userInput, tvShows);
			}else if (mainMenuChoice == 2){
				String episodeName = "";
				if (subMenuChoice == 1) {
					//If displaying all episodes of a show's season
					if (notEmptySeason) 
						TVShow.displayEpisodes(showNum-1, seasonNum, tvShows);
					else
						System.out.println("There are no episodes in the current season, so there is nothing to display. Please try again when there is at least one episode in this season.");
				}else if (subMenuChoice == 2) {
					//If displaying info of an episode from a show
					if (notEmptySeason) {
						System.out.println("Enter the title of the episode that you would like to see the information of.");
						System.out.println("You can also enter the episode number associated with the episode.");
						System.out.println("You can also enter a blank field at any time to leave.");
						//Asking for title of episode/episode number
						int episodeNum = 0;
						boolean validInput = false;
						while (!validInput) {
							episodeName = userInput.nextLine().trim();
							try {
								episodeNum = validInputIntMenuTwo(subMenuChoice, showNum-1, seasonNum, userInput, episodeName, 0, tvShows);
								if (episodeNum >= 0) {
									TVShow.displayEpisodeInfo(showNum-1, seasonNum, episodeNum, tvShows);
									TVShow.sortEpisodes(showNum-1, seasonNum, TVShow.getSortCriteria(), tvShows, false);
									validInput = true;
								}
							}catch (NumberFormatException e) {
								episodeName = validInputStringMenuTwo(subMenuChoice, showNum-1, episodeName, userInput, tvShows);
								Collections.sort(tvShows.get(showNum-1).getEpisodes());
								int index = Collections.binarySearch(tvShows.get(showNum-1).getEpisodes(), new Episode(episodeName, 0, 0, new Time("00:00:00")));
								if (index>=0) {
									episodeNum = tvShows.get(showNum-1).getEpisodes().get(index).getEpisodeNum();
									TVShow.displayEpisodeInfo(showNum-1, seasonNum, episodeNum, tvShows);
									TVShow.sortEpisodes(showNum-1, seasonNum, TVShow.getSortCriteria(), tvShows, false);
									
									validInput = true;
								}else if (episodeName.length()== 0)
									validInput = true;
							}
						}
					}else
						System.out.println("There are no episodes in the current season, so there is nothing to display. Please try again when there is at least one episode in this season.");
				}else if (subMenuChoice == 3) {
					//If setting an episode to 'watched'
					if (notEmptySeason) {
						System.out.println("Which episode would you like to set as 'watched'?");
						System.out.println("You can also enter the episode number associated with the episode.");
						System.out.println("You can also enter a blank field at any time to leave.");
						//Asking for title of episode/episode number
						int episodeNum = 0;
						boolean validInput = false;
						while (!validInput) {
							episodeName = userInput.nextLine().trim();
							try {
								episodeNum = validInputIntMenuTwo(subMenuChoice, showNum-1, seasonNum, userInput, episodeName, 0, tvShows);
								if (episodeNum >= 0) { 
									TVShow.watchEpisode(showNum-1, seasonNum, episodeNum, tvShows);
									TVShow.sortEpisodes(showNum-1, seasonNum, TVShow.getSortCriteria(), tvShows, false);
									validInput = true;
								}
							}catch (NumberFormatException e) {
								episodeName = validInputStringMenuTwo(subMenuChoice, showNum-1, episodeName, userInput, tvShows);
								Collections.sort(tvShows.get(showNum-1).getEpisodes());
								int index = Collections.binarySearch(tvShows.get(showNum-1).getEpisodes(), new Episode(episodeName, 0, 0, new Time("00:00:00")));
								if (index>=0) {
									episodeNum = tvShows.get(showNum-1).getEpisodes().get(index).getEpisodeNum();
									TVShow.watchEpisode(showNum-1, seasonNum, episodeNum, tvShows);
									TVShow.sortEpisodes(showNum-1, seasonNum, TVShow.getSortCriteria(), tvShows, false);
									validInput = true;
								}else if (episodeName.length()== 0)
									validInput = true;
							}
						}
					}else
						System.out.println("There are no episodes in the current season, so there is nothing to set as 'watched'. Please try again when there is at least one episode in this season.");
				}else if (subMenuChoice == 4) {
					//If adding an episode
					System.out.printf("Enter the name of the episode that you would like to add. The episode will be added to the current season, season %d.%n", seasonNum);
					System.out.println("You can also enter a blank field at any time to leave.");
					//Asking for title of episode

					episodeName = userInput.nextLine().trim();
					int hours = 0, minutes = 0, seconds = 0;
					int episodeNum = 0;
					String episodeNumStr = "", time = "";
					if (episodeName.length()!=0) {
						System.out.println("Enter the episode number that is associated with this episode.");
						System.out.println("You can also enter a blank field at any time to leave.");
						//Asking for episode number associated with episode

						episodeNumStr = userInput.nextLine().trim();
						episodeNum = validInputIntMenuTwo(subMenuChoice, showNum-1, seasonNum, userInput, episodeNumStr, 0, tvShows);
						if (episodeNum!=-1) {
							System.out.println("Enter the length of the episode you are entering, in a 00:00:00 format.");
							System.out.println("There must exactly be two digits representing the seconds and minutes, and at least two digits representing the hours.");
							System.out.println("You can also enter a blank field at any time to leave.");
							//Asking for running time of episode

							time = userInput.nextLine().trim();
							time = validInputStringMenuTwo(subMenuChoice, showNum-1, time, userInput, tvShows);

							if (time.length()!=0) {
								hours = Integer.parseInt(time.substring(0, time.indexOf(":")));
								minutes = Integer.parseInt(time.substring(time.indexOf(":")+1, time.lastIndexOf(":")));
								seconds = Integer.parseInt(time.substring(time.lastIndexOf(":")+1));
								TVShow.addEpisode(showNum-1, seasonNum, episodeNum, time, episodeName, tvShows);
							}
						}
					}else 
						System.out.printf("Blank field entered. Returning to submenu %s.%n", mainMenuChoice);
				}else if (subMenuChoice == 5) {
					//If removing episode(s)
					if (notEmptySeason) {
						System.out.println("Would you like to remove an episode by its title, an episode by its episode number, a range of episodes, or all the watched episodes?");
						System.out.println("Enter 1(title), 2(episode number), 3(range of episodes), or 4(all watched episodes) to indicate your choice.");
						System.out.println("You can also enter a blank field at any time to leave.");
						//Asking how user wants to remove episodes(title, ep. number, etc.)

						String choiceInput = userInput.nextLine().trim();
						int choiceInputInt = validInputIntMenuTwo(subMenuChoice, showNum-1, seasonNum, userInput, choiceInput, 0, tvShows);

						if (choiceInputInt == 1) {
							System.out.println("Please enter the name of the episode you would like to remove.");
							System.out.println("You can also enter a blank field at any time to leave.");
							//Asking for title of episode

							episodeName = userInput.nextLine().trim();
							episodeName = validInputStringMenuTwo(subMenuChoice, showNum-1, episodeName, userInput, tvShows);
							if (episodeName.length()!=0) {
								//								Collections.sort(tvShows.get(showNum-1).getEpisodes());
								int index = Collections.binarySearch(tvShows.get(showNum-1).getEpisodes(), new Episode(episodeName, 0, 0, new Time("00:00:00")));
								int episodeNum = tvShows.get(showNum-1).getEpisodes().get(index).getEpisodeNum();
								TVShow.removeEpisode(showNum-1, seasonNum, episodeNum, tvShows);
							}
						}else if (choiceInputInt == 2) {
							System.out.println("Please enter the episode number of the episode you would like to remove.");
							System.out.println("You can also enter a blank field at any time to leave.");
							//Asking for episode number associated with episode

							choiceInput = userInput.nextLine().trim();
							choiceInputInt = validInputIntMenuTwo(subMenuChoice, showNum-1, seasonNum, userInput, choiceInput, 2, tvShows);
							if (choiceInputInt!=-1)
								TVShow.removeEpisode(showNum-1, seasonNum, choiceInputInt, tvShows);
						}else if (choiceInputInt == 3) {
							System.out.println("Please enter the starting range of the episodes you would like to remove.");
							System.out.println("You can also enter a blank field at any time to leave.");
							//Asking for starting and ending range of episodes to be removed

							choiceInput = userInput.nextLine().trim();
							int start = validInputIntMenuTwo(subMenuChoice, showNum-1, seasonNum, userInput, choiceInput, 3, tvShows);
							if (start!=-1) {
								System.out.println("Please enter the ending range of the episodes you would like to remove.");
								System.out.println("You can also enter a blank field at any time to leave.");
								choiceInput = userInput.nextLine().trim() + start;
								int end = validInputIntMenuTwo(subMenuChoice, showNum-1, seasonNum, userInput, choiceInput, 4, tvShows);	
								if (end!=-1)
									TVShow.removeEpisode(showNum-1, seasonNum, start, end, tvShows);
							}
						}else if (choiceInputInt == 4) {
							//If removing all watched episodes
							TVShow.removeEpisode(showNum-1, seasonNum, tvShows);
						}
						TVShow.sortEpisodes(showNum-1, seasonNum, TVShow.getSortCriteria(), tvShows, false);
					}else
						System.out.println("There are no episodes in the current season, so there is nothing to remove. Please try again when there is at least one episode in this season.");
				}else if (subMenuChoice == 6) {
					//If sorting episodes
					if (notEmptySeason) {
						System.out.println("The episodes under this season can be listed by episode number, episode title, or time. Which sorting criteria would you like to implement?");
						System.out.println("Enter 1 for episode number, 2 for episode title, and 3 for time.");
						System.out.println("You can also enter a blank field at any time to leave.");

						String choiceInput = userInput.nextLine();
						int choiceInputInt = validInputIntMenuTwo(subMenuChoice, showNum-1, seasonNum, userInput, choiceInput, 1, tvShows);
						if (choiceInputInt!=-1) {
							TVShow.sortEpisodes(showNum-1, seasonNum, choiceInputInt, tvShows, true);
						}	
					}else
						System.out.println("There are no episodes in the current season, so there is nothing to sort. Please try again when there is at least one episode in this season.");
				}else if (subMenuChoice == 7) {
					//If returning to main menu
					currentlyMenuTwo = false;
					mainMenuChoice = displayMenu (0, userInput, tvShows);
				}
				if (tvShows.get(showNum-1).getEpisodes().size()==0)
					notEmptySeason = false;
			}
			if (mainMenuChoice == 1)
				//first submenu
				subMenuChoice = displayMenu (1, userInput, tvShows);
			else if (mainMenuChoice == 2 && !currentlyMenuTwo) {
				//if accessing second submenu without having selected a specific show and season 
				if (tvShows.size()!=0) {
					System.out.println("Enter the title of the TV show you would like to modify. Do not forget spacing or spelling of words.");
					System.out.println("You can also enter the number assigned to the TV show (see options 1, 2, or 5 under submenu 1 to view the number assigned to each show).");
					System.out.println("You can also enter a blank field at any time to leave.");
					Integer choiceInputInt = 0;
					//Asking for title of show/number assigned to show

					boolean validInput = false;
					while (!validInput) {
						tvShowName = userInput.nextLine().trim();
						ArrayList<Integer> numSeasonsList = new ArrayList<Integer>();
						try {
							showNum = Integer.parseInt(tvShowName);
							showNum = validInputIntMenuTwo(0, 0, 0, userInput, tvShowName, 1, tvShows);
							if (showNum > 0) {
								Collections.sort(tvShows, new sortByAssignedNum());
								TVShow tempShow = new TVShow("", "", userInput);
								tempShow.setAssignedNum(showNum);
								int index = Collections.binarySearch(tvShows, tempShow, new sortByAssignedNum());
								tvShowName = tvShows.get(index).getTitle();

								System.out.printf("\nPlease enter the season # that you would like to modify from the show %s.%n", tvShowName);
								System.out.print("The following seasons can be modified: ");


								Collections.sort(tvShows.get(index).getSeasons(), new sortBySeasonNum());
								for (int count = 0; count < tvShows.get(index).getSeasons().size()-1; count++)
									System.out.print(tvShows.get(index).getSeasons().get(count) + ", ");
								System.out.println(tvShows.get(index).getSeasons().get(tvShows.get(index).getSeasons().size()-1));

								System.out.println("You can also enter a season that isn't contained in the database, and a new season of that number will be created.");
								System.out.println("You can also enter a blank field at any time to leave.");
								//Asking for season of show that user wants to use for submenu 2

								String choiceInput = userInput.nextLine().trim();
								seasonNum = validInputIntMenuTwo(0, showNum-1, 0, userInput, choiceInput, 2, tvShows);

								if (seasonNum == -1) 
									currentlyMenuTwo = false;
								else {
									currentlyMenuTwo = true;
									notEmptySeason = false;
									for (int count = 0; count < tvShows.get(showNum-1).getEpisodes().size(); count++)
										if (tvShows.get(showNum-1).getEpisodes().get(count).getSeasonNum() == seasonNum)
											notEmptySeason = true;
								}
								validInput = true;
							}
						}catch (NumberFormatException e) {
							tvShowName = validInputStringMenuTwo(0, 0, tvShowName, userInput, tvShows);
							Collections.sort(tvShows, new sortByAssignedNum());
							showNum = Collections.binarySearch(tvShows, new TVShow(tvShowName, "", userInput));
							if (showNum>=0) {
								tvShowName = tvShows.get(showNum).getTitle();
								System.out.printf("\nPlease enter the season # that you would like to modify from the show %s.%n", tvShowName);
								System.out.print("The following seasons can be modified: ");
								for (int count = 0; count < tvShows.get(showNum).getEpisodes().size(); count++) {
									Collections.sort(numSeasonsList);
									int currSeason = tvShows.get(showNum).getEpisodes().get(count).getSeasonNum();
									if (Collections.binarySearch(numSeasonsList, currSeason)<0)
										numSeasonsList.add(currSeason);
								}
								Collections.sort(numSeasonsList);
								for (int count = 0; count <numSeasonsList.size()-1; count++)
									System.out.print(numSeasonsList.get(count) + ", ");
								System.out.println(numSeasonsList.get(numSeasonsList.size()-1));
								System.out.println("You can also enter a season that isn't contained in the database, and a new season of that number will be created.");
								System.out.println("You can also enter a blank field at any time to leave.");
								//Asking for season of show that user wants to use for submenu 2

								String choiceInput = userInput.nextLine().trim();
								seasonNum = validInputIntMenuTwo(0, showNum, 0, userInput, choiceInput, 2, tvShows);
								if (seasonNum!=-1)
									notEmptySeason = true;
								else
									notEmptySeason = false;

								if (seasonNum == -1) 
									currentlyMenuTwo = false;
								else {
									currentlyMenuTwo = true;
									notEmptySeason = false;
									for (int count = 0; count < tvShows.get(showNum-1).getEpisodes().size(); count++)
										if (tvShows.get(showNum-1).getEpisodes().get(count).getSeasonNum() == seasonNum)
											notEmptySeason = true;
								}
								validInput = true;

							}else if (tvShowName.length() == 0) {
								seasonNum = null;
								validInput = true;
								currentlyMenuTwo = false;
							}
						}
					}
					if (seasonNum == null || !currentlyMenuTwo) {
						//if user is not going to be in menu two(ie user wants to return to main menu)
						subMenuChoice = 0;
						mainMenuChoice = displayMenu (0, userInput, tvShows);
					}else {
						//if user wants to continue with submenu 2
						subMenuChoice = displayMenu (2, userInput, tvShows);
					}
				}else {
					subMenuChoice = 0;
					mainMenuChoice = displayMenu (2, userInput, tvShows);
				}
			}else if (mainMenuChoice == 2) { 
				//if accessing second submenu while having selected a specific show and season
				currentlyMenuTwo = true;
				subMenuChoice = displayMenu (2, userInput, tvShows);
			}else
				//if exiting menu
				keepAsking = false;
		}
		System.out.print("Have a nice day!");
	}
}
