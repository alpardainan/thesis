import java.util.ArrayList;

import javax.swing.JTextArea;

public class ClusteredHeuristic {
	
	 ClusteredNetwork cNetwork;
	 JTextArea textArea;
	 double totalRTT = 0.0;
	 double total_RTT = 0.0;
	
	public ClusteredHeuristic(ClusteredNetwork cNetwork, JTextArea textArea){
		this.cNetwork = cNetwork;
		this.textArea = textArea;
	}
	
	public void run(){
		textArea.append("Start of the Clustered Heuristic program\n");
		for(int i = 0; i < cNetwork.getClusters().size(); i++)
		{			
			textArea.append("Locating Demands of CLUSTER ID " + i + "\n");
			ArrayList<Integer> demands = cNetwork.getCluster(i).getDemands();
			for(int j = 0; j < demands.size(); j++){
				if(demands.get(j) != 0 && cNetwork.getNetworkService(j).getCentralized()){
					// Put it to the nearest ZARA location that has enough capacity.
					putNearestZARA(cNetwork.getCluster(i), j);					
				}
			}
			// SOLVE THE KNAPSACK PROBLEM FOR THE REST OF THE SERVICES
			knapsack(cNetwork.getCluster(i));		
		}
		textArea.append("End of the Clustered Heuristic program\n");
	}
	
	public void knapsack(Cluster cluster){
		ArrayList<DataCenter> list = new ArrayList<DataCenter>();
		ArrayList<Integer> eff_capacity = new ArrayList<Integer>();
		DataCenter nearest;
		int minCapacity;
		int maxEffCap; // variable for showing the max number of instances that can be packed into the node.
		int maxIndex;
		int instance;
		while(!cluster.isDemandsEmpty()){
			nearest = cNetwork.findNearestDC_RTT(cluster.getCentroid(), list);
			eff_capacity = new ArrayList<Integer>();
			for(int i = 0; i < cNetwork.getNetworkServices().size(); i++){
				minCapacity = Math.min(nearest.getCPU()/cNetwork.getNetworkService(i).getCPU(), nearest.getRAM()/cNetwork.getNetworkService(i).getRAM());
				minCapacity = Math.min(minCapacity, nearest.getStorage()/cNetwork.getNetworkService(i).getStorage());
				if(cluster.getDemand(i) != 0)
					eff_capacity.add(minCapacity*cNetwork.getNetworkService(i).getPriority());
				else
					eff_capacity.add(0);
			}
			maxIndex = findMaxEffCap(eff_capacity, cluster); // Index of the NetworkService
			maxEffCap = (eff_capacity.get(maxIndex)/cNetwork.getNetworkService(maxIndex).getPriority());
			
			// if none can be placed put into the list of full.
			if(maxEffCap == 0){
				list.add(nearest);
			}
			else{
				instance = Math.min(maxEffCap, cluster.getDemand(maxIndex));
				nearest.setAssignmentListValue(cluster.getId(), cNetwork.getNetworkService(maxIndex).getId(), nearest.getAssignmentListValue(cluster.getId(), cNetwork.getNetworkService(maxIndex).getId()) + instance); // Sets the decision variable.
				totalRTT = totalRTT + cNetwork.getRTT(cluster.getCentroid().getId(), nearest.getId())*instance;
				nearest.setRAM(nearest.getRAM() - instance*cNetwork.getNetworkService(maxIndex).getRAM());
				nearest.setCPU(nearest.getCPU() - instance*cNetwork.getNetworkService(maxIndex).getCPU());
				nearest.setStorage(nearest.getStorage() - instance*cNetwork.getNetworkService(maxIndex).getStorage());
				cluster.setDemand(maxIndex, cluster.getDemand(maxIndex) - instance);				
			}
		}
	}
	
	// Finds the index of the NetworkService which has the largest effective capacity.
	public static int findMaxEffCap(ArrayList<Integer> list, Cluster cluster){
		int maxValue = 0;
		int maxIndex = 0;
		for (int i = 0; i < list.size(); i++){
		    if (list.get(i) > maxValue && cluster.getDemand(i) != 0) {
		        maxValue = list.get(i);
		        maxIndex = i;
		    }
		}
		return maxIndex;
	}
	
	public static void printCapacity(Network n){		
		for(int i = 0; i < n.getDataCenters().size(); i++){
			System.out.print("DataCenter " + i + " ");
			System.out.print("RAM " + n.getDataCenters().get(i).getRAM() + " ");
			System.out.print("CPU " + n.getDataCenters().get(i).getCPU() + " ");
			System.out.print("Storage " + n.getDataCenters().get(i).getStorage() + " ");
			System.out.println();
		}
	}
	
	public static void printAssignmentValues(ClusteredNetwork n){
		for(int i = 0; i < n.getDataCenters().size(); i++)
		{
			System.out.println("DataCenter " + i);
			for(int k = 0; k < n.getClusters().size(); k++)
			{
				for(int l = 0; l < n.getNetworkServices().size(); l++)
				{
					System.out.println("(Cluster " + k + ",Service " + l + "): " + n.getDataCenter(i).getAssignmentListValue(k, l));
				}
			}
		}
	}
	
	public static void printDemands(ClusteredNetwork n){
		for(int i = 0; i < n.getClusters().size(); i++){
			System.out.println("Cluster " + n.getCluster(i).getId() + " : " );
			for(int k = 0; k < n.getNetworkServices().size(); k++){
				System.out.println("Service " + n.getNetworkService(k).getId() + " : " + n.getCluster(i).getDemand(k));
			}				
		}
	}
	
	public void printTotalRTT(){
		System.out.println("TOTAL RTT CLUSTERED HEURISTIC:" + totalRTT);
	}
	
	public void putNearestZARA(Cluster cluster, int serviceIndex){
		DataCenter nearestZARA;
		BaseStation bs = cluster.getCentroid(); 
		ArrayList<DataCenter> list = new ArrayList<DataCenter>();
		int numInstance, instance;
		while(cluster.getDemand(serviceIndex) != 0){
			nearestZARA = cNetwork.findNearestZARA_RTT(bs, list);
			numInstance = Math.min(nearestZARA.getCPU()/cNetwork.getNetworkService(serviceIndex).getCPU(), nearestZARA.getRAM()/cNetwork.getNetworkService(serviceIndex).getRAM());
			numInstance = Math.min(numInstance, nearestZARA.getStorage()/cNetwork.getNetworkService(serviceIndex).getStorage());
			if(numInstance == 0)
				list.add(nearestZARA);
			else{
				instance = Math.min(numInstance, cluster.getDemand(serviceIndex));
				nearestZARA.setAssignmentListValue(bs.getCluster(), cNetwork.getNetworkService(serviceIndex).getId(), nearestZARA.getAssignmentListValue(bs.getCluster(), cNetwork.getNetworkService(serviceIndex).getId()) + instance); // Sets the decision variable.
				totalRTT = totalRTT + cNetwork.getRTT(cluster.getCentroid().getId(), nearestZARA.getId())*instance;
				nearestZARA.setRAM(nearestZARA.getRAM() - instance*cNetwork.getNetworkService(serviceIndex).getRAM());
				nearestZARA.setCPU(nearestZARA.getCPU() - instance*cNetwork.getNetworkService(serviceIndex).getCPU());
				nearestZARA.setStorage(nearestZARA.getStorage() - instance*cNetwork.getNetworkService(serviceIndex).getStorage());
				cluster.setDemand(serviceIndex, cluster.getDemand(serviceIndex) - instance);
			}
		}
	}
	
	public double printTOTALRTT_cluster(){
//		
		for(int i = 0; i < cNetwork.getClusters().size(); i++){
			for(int k = 0; k < cNetwork.getDataCenters().size(); k++){
				// find the max RTT in the cluster to the datacenter
				for(int c = 0; c < cNetwork.getNetworkServices().size(); c++){
					double max_RTT = findAverageRTT(i,k, c);
					total_RTT = total_RTT + cNetwork.getDataCenter(k).getAssignmentListValue(i, c)*max_RTT;
				}
			}
		}
			//System.out.println("CLUSTERED HEURISTIC MAX RTT: " + total_RTT);
			return total_RTT;
	}
	
	public double findAverageRTT(int cluster, int data, int service){
		double maxRTT = 0;
		double temp;
		int total_Demand = 0;
		for(int i = 0; i < cNetwork.getCluster(cluster).getStations().size(); i++){
			temp = Network.findDistance(cNetwork.getCluster(cluster).getStation(i),cNetwork.getDataCenter(data));
			temp = cNetwork.getRTT(cNetwork.getCluster(cluster).getStation(i).getId(),cNetwork.getDataCenter(data).getId());

		//	maxRTT = maxRTT + temp*cNetwork.getCluster(cluster).getStation(i).getDemand(service);
			maxRTT = maxRTT + temp*cNetwork.getCluster(cluster).getStation(i).getDemand(service);

			total_Demand = total_Demand + cNetwork.getCluster(cluster).getStation(i).getDemand(service);
		}
//		System.out.println("total_demand: " + total_Demand);
		if(total_Demand != 0)
			return maxRTT/total_Demand;
		else 
			return 0;
	}
}