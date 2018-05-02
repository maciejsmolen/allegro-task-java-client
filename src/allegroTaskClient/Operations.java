package allegroTaskClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.*;

/**
 * Class containing all methods and variables related to performing operations on the RESTful web service.
 * Based on user command line input, different actions, like listing movies or adding new ones, are performed.
 * 
 * @author Maciej Smolen
 */
public final class Operations {
	
	public static Scanner scanner = new Scanner(System.in);
	
	public static final String URL_SHAWSHANK = "http://localhost:8080/addMovie?name=Shawshank+Redemption&year=1994&director=Frank+Darabont&country=USA";
	public static final String URL_KNIGHT = "http://localhost:8080/addMovie?name=The+Dark+Knight&year=2008&director=Christopher+Nolan&country=USA";
	public static final String URL_INCEPTION = "http://localhost:8080/addMovie?name=Inception&year=2010&director=Christopher+Nolan&country=USA";
	public static final String URL_FIGHTCLUB = "http://localhost:8080/addMovie?name=Fight+Club&year=1999&director=David+Fincher&country=USA";
	public static final String URL_TPB = "http://localhost:8080/addMovie?name=Trailer+Park+Boys:+The+Movie&year=2006&director=Mike+Clattenburg&country=Canada";
	
	/**
	 * Method responsible for displaying main menu to the user.
	 */
	static void displayMenu(){
		System.out.println("");
		System.out.println("Select operation:");
		System.out.println("1-List movies");
		System.out.println("2-Show movie details");
		System.out.println("3-Add a movie");
		System.out.println("4-Add an example set of 5 movies");
		System.out.println("5-Exit");
	}
	
	/**
	 * Method responsible for processing user input and invoking other methods
	 * 
	 * @param option Option chosen by user
	 */
	static void processInput(int option){
		
		switch (option){
			case 1:		listMovies();
						break;
			case 2:		showMovieDetails();
						break;
			case 3:		addMovie();
						break;
			case 4:		addExampleSet();
						break;
			case 5:		exit();
						break;
			default: 	System.out.println("Wrong input");
						break;
		}
	}
	
	/**
	 * Method responsible for displaying a list of movies (ID + movie title)
	 */
	private static void listMovies(){

		try{
			URL url = new URL("http://localhost:8080/listMovies");
			String rawJSONList = getHTTPResponse(url);
			JSONArray JSONList = new JSONArray(rawJSONList);
			StringBuilder list = new StringBuilder();
			
			for (int i=0 ; i<JSONList.length() ; i++){
				StringBuilder sb = new StringBuilder();
				JSONObject temp = JSONList.getJSONObject(i);
				sb.append(temp.getInt("id"));
				sb.append(". ");
				sb.append(temp.getString("name"));
				sb.append("\n");
				
				list.append(sb.toString());
			}
			System.out.println(list.toString());
		}catch(Exception e){System.out.println("Error - wrong input or service down");}
		
	}
	
	/**
	 * Method responsible for displaying movie details (ID, title, director, year and country)
	 */
	private static void showMovieDetails(){
		
		try{
			URL url = new URL("http://localhost:8080/getSize");
			String listSize = getHTTPResponse(url);
			int size = Integer.parseInt(listSize);
			
			if (size==0)
				System.out.println("There are no movies on the list");
			else
				System.out.println("Select movie id 1-"+size);
			
			int choice = Integer.parseInt(scanner.next());
			
			url = new URL("http://localhost:8080/getMovieDetails?id="+choice);
			String rawJSONObject = getHTTPResponse(url);
			JSONObject movieDetails = new JSONObject(rawJSONObject);
			StringBuilder details = new StringBuilder();
			
			details.append("ID: ");
			details.append(movieDetails.getInt("id"));
			details.append("\n");
			details.append("Name: ");
			details.append(movieDetails.getString("name"));
			details.append("\n");
			details.append("Year: ");
			details.append(movieDetails.getInt("year"));
			details.append("\n");
			details.append("Directed by: ");
			details.append(movieDetails.getString("director"));
			details.append("\n");
			details.append("Country: ");
			details.append(movieDetails.getString("country"));
			details.append("\n");
			
			System.out.println(details.toString());		
			
		}catch(Exception e){
			System.out.println("Error - wrong input or service down");
		}
		
	}
	
	/**
	 * Method responsible for adding a new movie to the set
	 */
	private static void addMovie(){
		scanner = new Scanner(System.in);
		
		StringBuilder parameters = new StringBuilder();
		System.out.println("Enter movie name");
		parameters.append("?name=");
		parameters.append(Operations.scanner.nextLine().replaceAll(" ","+"));
		System.out.println("\nEnter movie release year");
		parameters.append("&year=");
		parameters.append(Operations.scanner.nextLine().replaceAll(" ","+"));
		System.out.println("\nEnter movie director");
		parameters.append("&director=");
		parameters.append(Operations.scanner.nextLine().replaceAll(" ","+"));
		System.out.println("\nEnter movie production country");
		parameters.append("&country=");
		parameters.append(Operations.scanner.nextLine().replaceAll(" ","+"));
		
		
		String finalURL = "http://localhost:8080/addMovie"+parameters.toString();
		//System.out.println(finalURL);
		
		
		try{
			URL url = new URL(finalURL);
			String rawJSONObject = getHTTPResponse(url);
			JSONObject addedMovie = new JSONObject(rawJSONObject);
			StringBuilder info = new StringBuilder();
			
			info.append("Movie: ");
			info.append(addedMovie.getString("name"));
			info.append("/");
			info.append(addedMovie.getInt("year"));
			info.append("/");
			info.append(addedMovie.getString("director"));
			info.append("/");
			info.append(addedMovie.getString("country"));
			info.append(" added successfully with ID ");
			info.append(addedMovie.getInt("id"));
			
			System.out.println(info.toString());
		}catch(Exception e){System.out.println("Error - wrong input or service down");}
	}
	
	/**
	 * Method responsible for adding an example set of five movies (to quickly test other features)
	 */
	private static void addExampleSet(){
		
		String[] exampleMovies = {URL_SHAWSHANK, URL_KNIGHT, URL_INCEPTION, URL_FIGHTCLUB, URL_TPB};
		for (int i=0; i<exampleMovies.length; i++){
			try{
				URL temp = new URL(exampleMovies[i]);
				URLConnection connection = temp.openConnection();
				BufferedReader res = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				
			}catch(Exception e){System.out.println("Error - wrong input or service down");}
		}
		System.out.println("Example set of 5 movies added");
	}
	
	/**
	 * Method responsible for ending the execution of the application
	 */
	private static void exit(){
		
		Main.run=false;
		
	}
	
	/**
	 * Method responsible for sending HTTP requests and receiving responses
	 * 
	 * @param url URL used for sending a HTTP request
	 * @return HTTP response
	 */
	private static String getHTTPResponse(URL url){
		
		StringBuilder response = new StringBuilder();
		
		try{
			URLConnection connection = url.openConnection();
			BufferedReader res = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String responseLine;
			
			while((responseLine = res.readLine()) != null)
				response.append(responseLine);
			
			
		}catch(Exception e){System.out.println("Error - wrong input or service down");}
		
		return response.toString();
	}

}
