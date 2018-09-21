import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

// This class defines the object of DataCenter which has properties of longitude, latitude, and zara which shows if it is centralized or not.
public class DataCenter {
	
    private int id = 0;
    private double longitude = 0;
    private double latitude = 0;
    private int ram = 0;
    private int storage = 0;
    private int cpu = 0;
    private boolean zara = false;
    int[][] assignment_list;
    public int num_base;
    public int num_service;
    public DataCenter(){};
    
    public DataCenter(int id, double longitude, double latitude,int ram, int storage, int cpu, boolean zara, int num_base, int num_service){  
    	this.setId(id);
    	this.setLongitude(longitude);
    	this.setLatitude(latitude);
    	this.setRAM(ram);
    	this.setStorage(storage);
    	this.setCPU(cpu);
    	this.setZARA(zara);
    	this.assignment_list = new int[num_base][num_service];
    	this.num_base = num_base;
    	this.num_service = num_service;  			
    }
    
    // Copy Constructor
    public DataCenter(DataCenter dc){
    	this.id = dc.getId();
    	this.longitude = dc.getLongitude();
    	this.latitude = dc.getLatitude();
    	this.ram = dc.getRAM();
    	this.storage = dc.getStorage();
    	this.cpu = dc.getCPU();
    	this.zara = dc.getZARA();
    	this.assignment_list = new int[dc.num_base][dc.num_service];
    }
    
    public void setId(int id){
    	this.id = id;
    }
    
    public int getId(){
    	return this.id;
    }
    
    public void setLongitude(double longitude){  	
    	this.longitude = longitude;
    }
    
    public double getLongitude(){
    	return this.longitude;
    }
    
    public void setLatitude(double latitude){
    	this.latitude = latitude;
    }
    
    public double getLatitude(){
    	return this.latitude;
    }
    
    public void setRAM(int ram){
    	this.ram = ram;
    }
    
    public int getRAM(){
    	return this.ram;
    }
    
    public void setStorage(int storage){
    	this.storage = storage;
    }
    
    public int getStorage(){
    	return this.storage;
    }
    
    public void setCPU(int cpu){
    	this.cpu = cpu;
    }
    
    public int getCPU(){
    	return this.cpu;
    }
    
    public void setZARA(boolean zara){
    	this.zara = zara;
    }
    
    // checks whether that data center is ZARA or not.
    public boolean getZARA(){
    	return this.zara;
    }
    
    // Sets the Assignment List Value which shows which service demand is met by which data center.
    public void setAssignmentListValue(int base, int service, int value){
    	this.assignment_list[base][service] = value;
    }
    
    // Returns the Assignment List Value which shows which service demand is met by which data center.
    public int getAssignmentListValue(int base, int service){
    	return this.assignment_list[base][service];
    }
    
    // Sets the dimensions of the Assignment List array for initialization.
    public void setAssignmentList(int num_cluster, int num_service){
    	this.assignment_list = new int[num_cluster][num_service];
    }
    
    public void setAssignmentList(int[][] assignment_list){
    	this.assignment_list = assignment_list;
    }
    
    // Creates a random data center
    public static DataCenter createRandomDataCenter(boolean zara, int id, int num_base, int num_service){
    	double latitude = ThreadLocalRandom.current().nextDouble(50, 60 + 1);
    	double longitude = ThreadLocalRandom.current().nextDouble(50, 60 + 1);
    	
    	if(!zara){
    		int ram = ThreadLocalRandom.current().nextInt(300, 600 + 1); 
        	int storage = ThreadLocalRandom.current().nextInt(600, 700 + 1);
        	int cpu = ThreadLocalRandom.current().nextInt(150, 180 + 1);
        	return new DataCenter(id, longitude, latitude, ram, storage, cpu, false,num_base, num_service); 
    	}
    	else
    	{
    		int ram = ThreadLocalRandom.current().nextInt(3000, 6000 + 1); 
        	int storage = ThreadLocalRandom.current().nextInt(5000, 10000 + 1); 
        	int cpu = ThreadLocalRandom.current().nextInt(1500, 1800 + 1); 
        	return new DataCenter(id, longitude, latitude, ram, storage, cpu, true, num_base, num_service); 
    	}  	
    }
  
    // Creates a list of random data centers
    public static ArrayList<DataCenter> createRandomDataCenters(int num, int num_zara, int num_base, int num_service){
    	ArrayList<DataCenter> data_centers = new ArrayList<DataCenter>();
    	for(int i = 0; i < num-num_zara; i++) {
    		data_centers.add(createRandomDataCenter(false, i, num_base, num_service));
    	}
    	int index = num-num_zara;
    	for(int i = 0; i < num_zara; i++,index++){
    		data_centers.add(createRandomDataCenter(true, index, num_base, num_service));
    	}  		
    	return data_centers;
    } 
}
