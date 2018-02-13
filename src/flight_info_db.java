import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class flight{//flight class
	int id ;
	static int count=1;
	ArrayList<passenger> pass = new ArrayList<passenger>();//passengers of that flight
	public flight(){
		id = count;
		count++;
	}
}

class passenger{//passenger class
	int id;
	static int count=1;
	ArrayList<flight> fl = new ArrayList<flight>();//flights of that passenger 
	public passenger(){
		id = count;
		count++;
	}
}

abstract class Transaction implements Runnable{//transacction class which is abstract
	flight_info_db db;//database of the transaction
	public Transaction(flight_info_db _db){
		db = _db;
	}
}

class Reserve extends Transaction{//Transaction of type reserve
	int f;
	int p;
	public Reserve(flight_info_db _db , int _f , int _p){//constructor of the transaction
		super(_db);
		f = _f;
		p = _p;
	}
	private Object lock = new Object();//for implementing 2PL
	public void run() {
		flight _f = db.fl[f];
		passenger _p = db.pass[p];
		synchronized(lock){//giving access to locks
			_p.fl.add(_f);
			_f.pass.add(_p);
		}//releasing the locks
	}
	
}



public class flight_info_db {//database class
	
	static passenger[] pass;//list of passengers
	static flight[] fl;//list of flights
		
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
		ExecutorService exec = Executors.newFixedThreadPool(5);//creating threadpool for concurrency.
		for (int i=0;i<t;i++){
			
		}
	}
}
