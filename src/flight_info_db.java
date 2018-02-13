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
		for (int i=0;i<passengers; i++){
			pass[i] = new passenger();
		}
		for (int i=0;i<flights;i++){
			fl[i]= new flight();
		}
		System.out.println("Enter no. of transactions: ");
		int t = Integer.parseInt(rd.readLine());
		System.out.println("Menu:\n1. Reserve(F,i)\n2. Cancel(F,i)\n3.My_Flights(id)\n4.Total_Reservations()\n5.Transfer(F1,F2,i)\nPlease Enter transaction option and correct arguments.");
		for (int i=0;i<t;i++){
			
		}
	}
}
