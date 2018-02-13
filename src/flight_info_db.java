import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class flight{
	int id ;
	static int count=1;
	ArrayList<passenger> pass = new ArrayList<passenger>();
	public flight(){
		id = count;
		count++;
	}
}

class passenger{
	int id;
	static int count=1;
	ArrayList<flight> fl = new ArrayList<flight>(); 
	public passenger(){
		id = count;
		count++;
	}
}

public class flight_info_db {
	public static void main(String[] args) throws NumberFormatException, IOException{
		BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter no. of flights: ");
		int  flights = Integer.parseInt(rd.readLine());
		System.out.println("Enter no. of passengers: ");
		int passengers = Integer.parseInt(rd.readLine());
		passenger[] pass = new passenger[passengers];
		flight[] fl = new flight[flights];
		System.out.println("Enter no. of transactions: ");
		int t = Integer.parseInt(rd.readLine());
		
	}
}
