import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Network {
	
	public  ArrayList<BaseStation> base_stations;
	public  ArrayList<DataCenter> data_centers;
	public  ArrayList<NetworkService> network_services;
	public  ArrayList<ArrayList<Double>> RTTs; // This two dimensioned ArrayList contains the RTT values between each BaseStation and DataCenter.
	
	public Network(){
		this.base_stations = new ArrayList<BaseStation>();
		this.data_centers = new ArrayList<DataCenter>();
		this.network_services = new ArrayList<NetworkService>();	
		this.RTTs = new ArrayList<ArrayList<Double>>();
	}
	
	public Network(int num_base, int num_data, int num_zara, int num_service){
		this.base_stations = BaseStation.createRandomBaseStations(num_base, num_service);
		this.data_centers = DataCenter.createRandomDataCenters(num_data, num_zara, num_base, num_service);
		this.network_services = NetworkService.createRandomNetworkServices(num_service);
		this.RTTs = createRandomRTTs(num_base, num_data);
	}
	
	// Copy constructor
	public Network(Network network, int cluster_size){
		this.base_stations = network.getBaseStations();
		this.data_centers = network.getDataCenters();
		for(int i = 0; i < network.getDataCenters().size(); i++){
			this.getDataCenter(i).setAssignmentList(cluster_size, network.getNetworkServices().size());
		}
		//this.data_centers = network.getDataCenters();
		this.network_services = network.getNetworkServices();
		this.RTTs = network.getRTTs();
	}
	
	// Deep COPY Constructor
	public Network(Network network){
		this.base_stations = new ArrayList<BaseStation>();
		for(BaseStation bs : network.getBaseStations()){
			this.base_stations.add(new BaseStation(bs));
		}
		this.data_centers = new ArrayList<DataCenter>();
		for(DataCenter dc : network.getDataCenters()){
			this.data_centers.add(new DataCenter(dc));
		}
		this.network_services = new ArrayList<NetworkService>();
		for(NetworkService ns : network.getNetworkServices()){
			this.network_services.add(new NetworkService(ns));
		}
		this.RTTs = new ArrayList<ArrayList<Double>>();
		for(int i = 0; i < network.getBaseStations().size(); i++){
			ArrayList<Double> temp = new ArrayList<Double>();
			for(int k = 0; k < network.getDataCenters().size(); k++){
				temp.add(network.getRTT(i, k));
			}
			this.RTTs.add(temp);
		}
	}
	
	public void setBaseStations(ArrayList<BaseStation> base_stations){
		this.base_stations = base_stations;
	}
	
	public ArrayList<BaseStation> getBaseStations(){
		return this.base_stations;
	}
	
	public BaseStation getBaseStation(int index){
		return this.base_stations.get(index);
	}
	
	public void setDataCenters(ArrayList<DataCenter> data_centers){
		this.data_centers = data_centers;
	}
	
	public ArrayList<DataCenter> getDataCenters(){
		return this.data_centers;
	}
	
	public DataCenter getDataCenter(int index){
		return this.data_centers.get(index);
	}
	
	public void setNetworkServices(ArrayList<NetworkService> network_services){
		this.network_services = network_services;
	}
	
	public ArrayList<NetworkService> getNetworkServices(){
		return this.network_services;
	}
	
	public NetworkService getNetworkService(int index){
	    	return this.network_services.get(index);
	    }
	 
	public void addBaseStation(BaseStation bs){
		 this.base_stations.add(bs);
	 }
	 
	public void addDataCenter(DataCenter datacenter){
		 this.data_centers.add(datacenter);
	 }
	 
	public void addNetworkService(NetworkService service){
		 this.network_services.add(service);
	 }
	 
	public ArrayList<ArrayList<Double>> getRTTs(){
		 return this.RTTs;
	 }
	 
	public Double getRTT(int base, int data){
		 return this.RTTs.get(base).get(data);
	 }
	
	// Finds the distance between a base station and data center.
	public static double findDistance(BaseStation bs1, DataCenter dc1){
    	return Math.sqrt(Math.pow((bs1.getLongitude() - dc1.getLongitude()), 2) + Math.pow((bs1.getLatitude() - dc1.getLatitude()), 2));
    }
	
	// Finds the nearest data center to the given base station.
	public  DataCenter findNearestDC(BaseStation bs){
		DataCenter nearest = data_centers.get(0);
		double dist = findDistance(bs,nearest);
		for(int i = 0; i < data_centers.size(); i++){
			if(findDistance(bs,data_centers.get(i)) < dist){
				nearest = data_centers.get(i);
				dist = findDistance(bs,data_centers.get(i));
			}				
		}
		return nearest;		
	}
	
	// Finds the nearest DC to the given base station and not on the list.
	public  DataCenter findNearestDC(BaseStation bs, ArrayList<DataCenter> list){
		int minIndex = 0;
		int randomNum = 0;
		double temp = 0;
		
		// Prints the list of full data centers
//		System.out.print("List: ");
//		for(int i = 0; i < list.size(); i++)
//		{
//			System.out.print(" " + list.get(i).getId());
//		}
//		System.out.println();
		
		do {
			randomNum = ThreadLocalRandom.current().nextInt(0, data_centers.size());
		}while(list.contains(data_centers.get(randomNum)));

		minIndex = randomNum;
		double minDistance = findDistance(bs,data_centers.get(minIndex));
		for (int i = 0; i < data_centers.size(); i++) {
			if(!list.contains(data_centers.get(i)))	{
				temp = findDistance(bs,data_centers.get(i));
				if (temp < minDistance) {
		            minDistance = temp;
		            minIndex = i;
		        }	
			}      
	    }
	    return data_centers.get(minIndex);		
	}
	
	//Finds the nearest ZARA that has the enough capacity.
	public  DataCenter findNearestZARA(BaseStation bs, ArrayList<DataCenter> list){
		
		int minIndex = 0;
		int randomNum = 0;
		double temp = 0;		
		do {
			randomNum = ThreadLocalRandom.current().nextInt(0, data_centers.size());
		}while(!data_centers.get(randomNum).getZARA() || list.contains(data_centers.get(randomNum)));

		minIndex = randomNum;
		double minDistance = findDistance(bs,data_centers.get(minIndex));
		for (int i = 0; i < data_centers.size(); i++) {
			if(!list.contains(data_centers.get(i)) && data_centers.get(i).getZARA())	{
				temp = findDistance(bs,data_centers.get(i));
				if (temp < minDistance) {
		            minDistance = temp;
		            minIndex = i;
		        }	
			}      
		}
		return data_centers.get(minIndex);
	}	
	
	// Prints the BaseStations with their id's and their longitude and langitude values.
	public void printBaseStations(){
		for(int i = 0; i < this.getBaseStations().size(); i++){
			System.out.println("Base Station " + i + "  Longitude: " + this.getBaseStation(i).getLongitude() + "  Latitude: " + this.getBaseStation(i).getLatitude());
		}
	}
	
	public void printBaseStationDemands(){
		for(int i = 0; i < this.getBaseStations().size(); i++){
			System.out.print("Base Station " + i);
			for(int k = 0; k < this.getNetworkServices().size(); k++){
				System.out.print(" " + this.getBaseStation(i).getDemand(k));
			}
			System.out.println();
		}
	}
	
	public void printNetworkServices(){
		for(int i = 0; i < this.getNetworkServices().size(); i++){
			System.out.println("Network Service " + i + "  ZARA: " + this.getNetworkService(i).getCentralized());
		}
	}
	
	public void printRTTs(){
		for(int i = 0; i < this.getBaseStations().size(); i++){
			System.out.print("Base Station " + i);
			for(int k = 0; k < this.getDataCenters().size(); k++){
				System.out.print("DC " + k + ": " + getRTT(i,k) + " ");
			}
			System.out.println();
		}
	}
	
	// Creates random RTT values between BaseStations and DataCenters.
	public  ArrayList<ArrayList<Double>> createRandomRTTs(int num_base, int num_data){
		ArrayList<ArrayList<Double>> temp = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> temp2;
		for(int i = 0; i < num_base; i++)
		{
			 temp2 = new ArrayList<Double>();
			 for(int k = 0; k < num_data; k++)
			 {
				 temp2.add(findDistance(base_stations.get(i),data_centers.get(k)));
			 }
			 temp.add(temp2);
		}
		return temp;
	}
	
	// Finds the nearest DataCenter to a given base station based on the RTT values.
	public  DataCenter findNearestDC_RTT(BaseStation bs, ArrayList<DataCenter> list) {
		int minIndex = 0;
		int randomNum = 0;
		double temp_RTT = 0;
		
		do {
			randomNum = ThreadLocalRandom.current().nextInt(0, data_centers.size());
		}while(list.contains(data_centers.get(randomNum)));
		
		minIndex = randomNum;
		double minRTT = getRTT(bs.getId(), minIndex); //double minRTT = RTTs.get(bs.getId()).get(minIndex);
		for (int i = 0; i < data_centers.size(); i++) {
			if(!list.contains(data_centers.get(i)))	{
				temp_RTT = RTTs.get(bs.getId()).get(i);
				if (temp_RTT < minRTT) {
		            minRTT = temp_RTT;
		            minIndex = i;
		        }	
			}      
	    }
	    return data_centers.get(minIndex);			
	}
	
	public  DataCenter findNearestZARA_RTT(BaseStation bs, ArrayList<DataCenter> list) {		
		int minIndex = 0;
		int randomNum = 0;
		double temp_RTT = 0;		
		do {
			randomNum = ThreadLocalRandom.current().nextInt(0, data_centers.size());
		}while(!data_centers.get(randomNum).getZARA() || list.contains(data_centers.get(randomNum)));

		minIndex = randomNum;
		double minRTT = getRTT(bs.getId(), minIndex); // double minRTT = RTTs.get(bs.getId()).get(minIndex);
		for (int i = 0; i < data_centers.size(); i++) {
			if(!list.contains(data_centers.get(i)) && data_centers.get(i).getZARA())	{
				temp_RTT = RTTs.get(bs.getId()).get(i);
				if (temp_RTT < minRTT) {
		            minRTT = temp_RTT;
		            minIndex = i;
		        }	
			}      
		}
		return data_centers.get(minIndex);
	}	
	public void printTotalDemand(){
		int totalDemand_CPU = 0;
		int totalDemand_RAM = 0;
		int totalDemand_Storage = 0;
		double percentage = 0;

		for(int i = 0; i < this.getBaseStations().size(); i++){
			for(int k = 0; k < this.getNetworkServices().size(); k++){
				 totalDemand_CPU = totalDemand_CPU + this.getBaseStation(i).getDemand(k)*this.getNetworkService(k).getCPU();
				 totalDemand_RAM = totalDemand_RAM + this.getBaseStation(i).getDemand(k)*this.getNetworkService(k).getRAM();
				 totalDemand_Storage = totalDemand_Storage + this.getBaseStation(i).getDemand(k)*this.getNetworkService(k).getStorage();
			}
		}
		int total_CPU = 0;
		int total_RAM = 0;
		int total_Storage = 0;
		for(int i = 0; i < this.getDataCenters().size(); i++){
			total_CPU = total_CPU + this.getDataCenter(i).getCPU();
			total_RAM = total_RAM + this.getDataCenter(i).getRAM();
			total_Storage = total_Storage + this.getDataCenter(i).getStorage();
		}
		percentage = Math.max((double)totalDemand_CPU/(double)total_CPU,(double)totalDemand_RAM/(double)total_RAM);
		
		percentage = Math.max((double)percentage, (double)(totalDemand_Storage/total_Storage));
		percentage = percentage*100;
		System.out.println("System Usage: " + percentage + " %");
		
	}
}