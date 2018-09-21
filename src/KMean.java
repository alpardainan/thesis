
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class KMean {
	
	public static Network network;
	public static ArrayList<Cluster> clusters;
	public static int num_cluster;
	
	public KMean(Network network, int num_cluster){
		KMean.network = network;
		KMean.num_cluster = num_cluster;
		clusters = new ArrayList<Cluster>();
	}
	
	// Run method for KMean. Returns Clustered Network.
	public ClusteredNetwork run() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		ArrayList<BaseStation> lastCentroids;
		ArrayList<BaseStation> currentCentroids;
		double distance;
		int index;
		boolean check = false; 
		
		
		// Create 'num_cluster number' of clusters with different id's.
		for(int i = 0; i < num_cluster; i++){
			Cluster cluster = new Cluster(i, network.getNetworkServices().size());
			do
			{
				index = ThreadLocalRandom.current().nextInt(0, network.getBaseStations().size());
			}while(list.contains(index));			
			cluster.setCentroid(network.getBaseStation(index));
			clusters.add(cluster);
			list.add(index);
//			System.out.println("Cluster " + i + " :" + cluster.getCentroid().getId());
		}
		
		while(!check){	
			clearClusters();
			//System.out.println("Last:");
			lastCentroids = getCentroids();
//			System.out.println();
			
			findClusters();
            
            //Calculate new centroids.
        	calculateCentroids();
        	
        	//System.out.println("Current:");
        	currentCentroids = getCentroids();
//			System.out.println();

        	//Calculates total distance between new and old Centroids
        	distance = 0;
        	for(int i = 0; i < lastCentroids.size(); i++) {
        		distance += BaseStation.findDistance(lastCentroids.get(i),currentCentroids.get(i));
        	}
//        	System.out.println("Distance " + distance);
        	
        	if(distance == 0) {
        		check = true;		
        	}	
		}
		sumDemands(); // Sum the NetworkService demands in the base stations for each cluster.
		return new ClusteredNetwork(network, clusters);		
	}
	
	// Returns an ArrayList containing the centroids of the existing clusters.
	public static ArrayList<BaseStation> getCentroids(){
		ArrayList <BaseStation> centroids = new ArrayList<BaseStation>();
		for(int i = 0; i < num_cluster; i++){
			centroids.add(clusters.get(i).getCentroid());
//			System.out.print(clusters.get(i).getCentroid().getId() + "  ");
		}
		return centroids;
	}
	
	// Finds and assigns clusters to each BaseStation based on their distances.
	public static void findClusters(){
		double min_dist = Double.MAX_VALUE;
		double dist;
		int cluster_no = 0;
		for(int i = 0; i < network.getBaseStations().size(); i++){
			min_dist = Double.MAX_VALUE;
			for(int k = 0; k < num_cluster; k++){
				dist = BaseStation.findDistance(network.getBaseStation(i), clusters.get(k).getCentroid());
				if(dist < min_dist){
					min_dist = dist;
					cluster_no = k;
				}
			}
			network.getBaseStation(i).setCluster(cluster_no);
			clusters.get(cluster_no).addStation(network.getBaseStation(i));
		}
	}
	
	// Clears the BaseStations existing in the clusters.
	public static void clearClusters(){
		for(int i = 0; i < num_cluster; i++){
			clusters.get(i).clear();
		}
	}
	
	// Calculates the centroids of the assigned clusters.
	public static void calculateCentroids(){
		double sum_longitude = 0.0; 
		double sum_latitude = 0.0;	
		int cluster_size;
		BaseStation bs;
		
		for(int i = 0; i < num_cluster; i++){
			sum_longitude = 0.0; 
			sum_latitude = 0.0;
//			System.out.println("Cluster " + i);
			cluster_size = clusters.get(i).getStations().size();
			for(int k = 0; k < cluster_size; k++){
				sum_longitude = sum_longitude + clusters.get(i).getStations().get(k).getLongitude();
				sum_latitude = sum_latitude + clusters.get(i).getStations().get(k).getLatitude();	
			}
			if(cluster_size > 0){
				bs = findClosestBaseStation(sum_longitude/cluster_size, sum_latitude/cluster_size, i);
				clusters.get(i).setCentroid(bs);
				bs.setCluster(i);
//				System.out.println(sum_latitude/cluster_size);
//				System.out.println("Cluster " + i + " :" + cluster_size);
			}
			else
				clusters.get(i).setCentroid(null);				
		}
	}
	
	public static void sumDemands(){
		int tempDemand = 0;
		ArrayList<Integer> tempList = new ArrayList<Integer>(Collections.nCopies(network.getNetworkServices().size(), 0));
		for(int i = 0; i < num_cluster; i++){
			tempList = new ArrayList<Integer>(Collections.nCopies(network.getNetworkServices().size(), 0));
			for(int l = 0; l < network.getNetworkServices().size(); l++){
				for(int k = 0; k < clusters.get(i).getStations().size(); k++){
					tempDemand = tempDemand + clusters.get(i).getStation(k).getDemand(l);
				}
				tempList.set(l, tempDemand);			
				tempDemand = 0;
			}
			clusters.get(i).setDemands(tempList);
		}
		
	}
	
	// Finds the closest Base Station to the given point as longitude and latitude in a given cluster. 
	public static BaseStation findClosestBaseStation(double longitude, double latitude, int index){
		double tempDistance = 0.0;
		BaseStation tempCentroid = new BaseStation(longitude, latitude);
		double minDistance = BaseStation.findDistance(tempCentroid, clusters.get(index).getStation(0));
		tempCentroid = clusters.get(index).getStation(0);
		for(int i = 0; i < clusters.get(index).getStations().size(); i++){
			tempDistance = BaseStation.findDistance(new BaseStation(longitude, latitude), clusters.get(index).getStation(i));
			if(tempDistance < minDistance){
				tempCentroid = clusters.get(index).getStation(i);
				minDistance = tempDistance;
			}
		}
		return tempCentroid;
	}
}