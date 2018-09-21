// This class defines the object of BaseStation which has properties of longitude, latitude, cluster_number and demands for each service.
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class BaseStation {
	
	private int id = 0;
	private double longitude = 0;
    private double latitude = 0;
    private int cluster_number = 0;
    private ArrayList<Integer> service_demands;
       
    public BaseStation(){
    	service_demands = new ArrayList<Integer>();
    };
    
    public BaseStation(double longitude, double latitude){
	   this.setLongitude(longitude);
   	   this.setLatitude(latitude);
    }
    
    public BaseStation(int id, double longitude, double latitude, ArrayList<Integer> service_demands){ 
    	this.setId(id);
    	this.setLongitude(longitude);
    	this.setLatitude(latitude);
    	this.setDemands(service_demands);
    }
    
    // Copy Constructor
    public BaseStation(BaseStation bs){
    	this.id = bs.getId();
    	this.longitude = bs.getLongitude();
    	this.latitude = bs.getLatitude();
    	this.service_demands = new ArrayList<Integer>();
    	for(int i = 0; i < bs.getDemands().size(); i++){
    		service_demands.add(bs.getDemand(i));
    	}
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
    
    public void setCluster(int cluster_number){
    	this.cluster_number = cluster_number;
    }
    
    public int getCluster(){
    	return this.cluster_number;
    }
    
    public void setDemands(ArrayList<Integer> service_demands){
    	this.service_demands = service_demands;
    }
    
    public ArrayList<Integer> getDemands(){
    	return this.service_demands;
    }
    
    public int getDemand(int index){
    	return this.service_demands.get(index);
    }
    
    public void setDemand(int index, int value){
    	this.service_demands.set(index, value);
    }
    
    public void addDemand(int demand){
    	this.service_demands.add(demand);
    }
    
    // Finds distance between two BaseStations
    public static double findDistance(BaseStation bs1, BaseStation bs2){
    		return Math.sqrt(Math.pow((bs1.getLongitude() - bs2.getLongitude()), 2) + Math.pow((bs1.getLatitude() - bs2.getLatitude()), 2));
    }
    
    // Creates a random base station
    public static BaseStation createRandomBaseStation(int id, int num_service){
    	double longitude = ThreadLocalRandom.current().nextDouble(50, 60 + 1);
    	double latitude = ThreadLocalRandom.current().nextDouble(50, 60 + 1);
    	ArrayList<Integer> service_demands = new ArrayList<Integer>();
    	for(int i = 0; i < num_service; i++){
    		service_demands.add(getPoisson(2));
    	}
    	return new BaseStation(id, longitude, latitude, service_demands);
    }
    
    // Creates a list of random base stations
    public static ArrayList<BaseStation> createRandomBaseStations(int num, int num_service){
    	ArrayList<BaseStation> base_stations = new ArrayList<BaseStation>();
    	for(int i = 0; i < num; i++){
    		base_stations.add(createRandomBaseStation(i, num_service));
    	}
    	return base_stations;
    }
    
    // Checks if all the demands are empty.
    public boolean isDemandsEmpty(){
		for(int i = 0; i < getDemands().size(); i++){
			if(getDemands().get(i) != 0)
				return false;
		}
		return true;
	}
    
    public static int getPoisson(double lambda) {
    	  double L = Math.exp(-lambda);
    	  double p = 1.0;
    	  int k = 0;

    	  do {
    	    k++;
    	    p *= Math.random();
    	  } while (p > L);

    	  return k - 1;
    	}
}