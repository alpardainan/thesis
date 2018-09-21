import java.util.ArrayList;

public class ClusteredNetwork extends Network{
	
	private static ArrayList<Cluster> clusters;
	
	public ClusteredNetwork(Network network, ArrayList<Cluster> clusters){
		super(network, clusters.size());
		this.setClusters(clusters);
	}
	
	public void setClusters(ArrayList<Cluster> clusters){
		ClusteredNetwork.clusters = clusters;
	}
	
	public ArrayList<Cluster> getClusters(){
		return ClusteredNetwork.clusters;
	}
	
	public void setCluster(int index, Cluster cluster){
		ClusteredNetwork.clusters.set(index, cluster);
	}
	
	public Cluster getCluster(int index){
		return ClusteredNetwork.clusters.get(index);
	}
	
	public void printClusteredNetwork(){
		for(int i = 0; i < clusters.size(); i++){
			System.out.println("Cluster " + i + ": ");
			System.out.print("Base Stations: ");
			for(int k = 0 ; k < clusters.get(i).getStations().size(); k++){
				System.out.print(clusters.get(i).getStation(k).getId() + " ");
			}
			System.out.println();
		}
	}
	
	public void printClusters(){
		for(int i = 0; i < clusters.size(); i++){
			System.out.println("Cluster " + i + " Centroid: " + clusters.get(i).getCentroid().getClass());
		}
	}
}