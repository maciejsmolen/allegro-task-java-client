package allegroTaskClient;

import java.util.Scanner;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * Main class, responsible for running the application and parsing user input
 * 
 * @author Maciej Smolen
 */
public class Main {
	
	public static boolean run = true;

	public static void main(String[] args) {
		
		int option=0;
		
		
		
		while(run){
		
		Operations.displayMenu();
		
		try{
			option = Integer.parseInt(Operations.scanner.next());
			Operations.processInput(option);
		}catch(Exception e){
			System.out.println("Wrong input");
		}
		}

	}

}
