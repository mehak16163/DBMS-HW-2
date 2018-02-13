import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

abstract class Transaction implements Runnable{
	flight_info_db db;
	public Transaction(flight_info_db _db){
		db = _db;
	}
}

class Reserve extends Transaction{
	int f;
	int p;
	public Reserve(flight_info_db _db , int _f , int _p){
		super(_db);
		f = _f;
		p = _p;
	}
	private Object lock = new Object();
	public void run() {
		flight _f = db.fl[f];
		passenger _p = db.pass[p];
		synchronized(lock){
			_p.fl.add(_f);
			_f.pass.add(_p);
		}
	}
	
}



public class flight_info_db {
	
	static passenger[] pass;
	static flight[] fl;
		
	public static void main(String[] args) throws NumberFormatException, IOException{
		BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter no. of flights: ");
		int  flights = Integer.parseInt(rd.readLine());
		System.out.println("Enter no. of passengers: ");
		int passengers = Integer.parseInt(rd.readLine());
		pass = new passenger[passengers+1];
		fl = new flight[flights+1];
		for (int i=0;i<=passengers; i++){
			pass[i] = new passenger();
		}
		for (int i=0;i<=flights;i++){
			fl[i]= new flight();
		}
		System.out.println("Enter no. of transactions: ");
		int t = Integer.parseInt(rd.readLine());
		System.out.println("Menu:\n1. Reserve(F,i)\n2. Cancel(F,i)\n3.My_Flights(id)\n4.Total_Reservations()\n5.Transfer(F1,F2,i)\nPlease Enter transaction option and correct arguments.");
		ExecutorService exec = Executors.newFixedThreadPool(5);
		for (int i=0;i<t;i++){
			
		}
	}
}
