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
	int capacity;
	ReentrantLock lock = new ReentrantLock();//for exclusive locks
	static int count=1;
	ArrayList<passenger> pass = new ArrayList<passenger>();//passengers of that flight
	public flight(int c){
		id = count;
		count++;
		capacity = c;
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

abstract class Transaction implements Runnable{//transaction class which is abstract
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
		if (_f.pass.contains(_p)){
			System.out.println("Passenger "+p+" is already in flight "+f+".");
			return;
		}
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
			if (_f.pass.size()<_f.capacity){
				System.out.println("Passenger "+p+" added to flight "+ f+".");
				_p.lmode = 1;
				_f.lmode = 1;
				_p.fl.add(_f);
				_f.pass.add(_p);
			}
			else{
				System.out.println("Capacity of flight "+f+" is full. Passenger "+p+" could not be added.");
			}
			_p.lock.unlock();
			_f.lock.unlock();
			_p.lmode=-1; _f.lmode=-1;
			//releasing the locks
		}
		else{
			if (_f.pass.size()<_f.capacity){
				_p.fl.add(_f);
				_f.pass.add(_p);
				System.out.println("Passenger "+p+" added to flight "+ f+".");
			}
			else{
				System.out.println("Capacity of flight "+f+" is full. Passenger "+p+" could not be added.");
			}
		}
	}
	
}

class My_Flight extends Transaction{
	int id;
	public My_Flight(flight_info_db db , int _id, boolean mode){
		super(db, mode);
		id = _id;
	}
	
	public void run() {
		passenger _p = db.pass[id];// TODO Auto-generated method stub
		if(mode){
			int i=0;
			while(_p.lmode==1 && i<10000){
				i++;
			}
			if(i==10000){
				System.out.println("Deadlock condition on Transaction My_Flight("+id+").");
				return;
			}
			if(_p.lmode==-1 || _p.lmode==0){
				_p.scount++;
				_p.lmode=0;
				for(int i=0;i<_p.fl.size();i++){
					System.out.print(_p.fl.get(i)+" ");
				}
				//String fl="";
				
			}
		}
		else{
			
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
				int i=0;
				while(_p.lmode != -1 && _f.lmode != -1 && i<=10000)
				{
					i = i + 1;
				}
				if (i == 10001){
					System.out.println("Deadlock on Cancel("+f+","+p+")");
					return;
				}
				_p.lock.lock();
				_f.lock.lock();
				_p.lmode = 1;
				_f.lmode = 1;
				_p.fl.remove(_f);
				_f.pass.remove(_p);
				_f.capacity = _f.capacity + 1;
				_p.lock.unlock();
				_f.lock.unlock();
				_p.lmode = -1;
				_f.lmode = -1;
			}
			else
			{
				_p.fl.remove(_f);
				_f.pass.remove(_p);
				_f.capacity = _f.capacity + 1;
			}
		}
		else
		{
			System.out.println("Passenger " + _p + "did not reserve Flight " + _f);
		}
	}
}

class Transfer extends Transaction
{
	int f1;
	int f2;
	int p;
	public Transfer(flight_info_db _db , int _f1 , int _f2, int _p , boolean m)
	{
		super(_db, m);
		f1 = _f1;
		f2 = _f2;
		p = _p;
	}
	private Object lock = new Object();
	@Override
	public void run() {
		flight _f1 = db.fl[f1];
		flight _f2 = db.fl[f2];
		passenger _p = db.pass[p];
		if(_f1.pass.contains(p) == true && _f2.capacity != 0)
		{
			if(mode)
			{
				int i=0;
				while(_p.lmode != -1 && _f1.lmode != -1 && _f2.lmode != -1 && i<=10000)
				{
					i = i + 1;
				}
				if (i == 10001){
					System.out.println("Deadlock on Transfer("+_f1+","+_f2+","+p+")");
					return;
				}
				_p.lock.lock();
				_f1.lock.lock();
				_f2.lock.lock();
				_p.lmode = 1;
				_f1.lmode = 1;
				_f2.lmode = 1;
				_p.fl.remove(_f1);
				_p.fl.add(_f2);
				_f1.pass.remove(_p);
				_f1.capacity = _f1.capacity + 1;
				_f2.pass.add(_p);
				_f2.capacity = _f2.capacity - 1;
				_p.lock.unlock();
				_f1.lock.unlock();
				_f2.lock.unlock();
				_p.lmode = -1;
				_f1.lmode = -1;
				_f2.lmode = -1;
			}
			else
			{
				_p.fl.remove(_f1);
				_p.fl.add(_f2);
				_f1.pass.remove(_p);
				_f1.capacity = _f1.capacity + 1;
				_f2.pass.add(_p);
				_f2.capacity = _f2.capacity - 1;
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
		System.out.println("Enter flight capacities:");
		String[] cap = rd.readLine().split(" ");
		for (int i=1;i<=passengers; i++){
			pass[i] = new passenger();
		}
		for (int i=1;i<=flights;i++){
			fl[i]= new flight(Integer.parseInt(cap[i-1]));
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
