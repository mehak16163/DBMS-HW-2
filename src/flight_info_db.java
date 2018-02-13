import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

class flight{//flight class
	int lmode =-1; //-1 means no lock , 0 means shared lock , 1 means exclusive lock 
	int id ;
	int scount=0;
	ReentrantLock lock = new ReentrantLock();//for exclusive locks
	static int count=1;
	ArrayList<passenger> pass = new ArrayList<passenger>();//passengers of that flight
	public flight(){
		id = count;
		count++;
	}
}

class passenger{//passenger class
	int lmode = -1; //-1 means no lock, 0 means shared lock , 1 means exclusive lock 
	ReentrantLock lock = new ReentrantLock();
	int scount =0;
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
	boolean mode;
	public Transaction(flight_info_db _db , boolean m ){
		db = _db;
		mode = m;
	}
}

class Reserve extends Transaction{//Transaction of type reserve
	int f;
	int p;
	public Reserve(flight_info_db _db , int _f , int _p , boolean m){//constructor of the transaction
		super(_db,m);
		f = _f;
		p = _p;
	}
	public void run() {
		flight _f = db.fl[f];
		passenger _p = db.pass[p];
		if (mode){
			int i=0;
			while(_p.lmode!=-1 && _f.lmode!= -1 && i<=10000)//checking for shared locks if acquired.
				i++;
			if (i==10001){
				System.out.println("Deadlock on Reserve("+f+","+p+")");
				return;
			}
			_p.lock.lock();
			_f.lock.lock();
			_p.lmode = 1;
			_f.lmode = 1;
			_p.fl.add(_f);
			_f.pass.add(_p);
			_p.lock.unlock();
			_f.lock.unlock();
			_p.lmode=-1; _f.lmode=-1;
			//releasing the locks
		}
		else{
			_p.fl.add(_f);
			_f.pass.add(_p);
		}
	}
	
}

class Cancel extends Transaction
{
	int f;
	int p;
	public Cancel(flight_info_db _db , int _f , int _p , boolean m)
	{
		super(_db, m);
		f = _f;
		p = _p;
	}
	private Object lock = new Object();
	@Override
	public void run() 
	{
		flight _f = db.fl[f];
		passenger _p = db.pass[p];
		if(_f.pass.contains(_p) == true) // check if passenger had reserved that flight or not
		{
			if(mode)
			{
				_p.lock.lock();
				_p.fl.remove(_f);
				_f.lock.lock();
				_f.pass.remove(_p);
				_p.lock.unlock();
				_f.lock.unlock();
			}
			else
			{
				_p.fl.remove(_f);
				_f.pass.remove(_p);
			}
		}
	}
}

public class flight_info_db {//database class
	static passenger[] pass;//list of passengers
	static flight[] fl;//list of flights
	static int scount=0;
	static int lmode =-1; //-1 for no lock , 0 for shared, 1 for exclusive. 
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
		System.out.println("Enter Mode of execution:\n1. Serially\n2. Concurrently");
		int m = Integer.parseInt(rd.readLine());
		boolean mode=false;
		if (m==2)
			mode = true;
		System.out.println("Enter no. of transactions: ");
		int t = Integer.parseInt(rd.readLine());
		System.out.println("Menu:\n1. Reserve(F,i)\n2. Cancel(F,i)\n3.My_Flights(id)\n4.Total_Reservations()\n5.Transfer(F1,F2,i)\nPlease Enter transaction option and correct arguments.");
		ExecutorService exec = Executors.newFixedThreadPool(5);//creating threadpool for concurrency.
		for (int i=0;i<t;i++){
			
		}
	}
}
