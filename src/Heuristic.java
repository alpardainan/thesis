
import java.util.ArrayList;

import javax.swing.JTextArea;

public class Heuristic {
	
	Network network;
	JTextArea textArea;
	double totalRTT = 0.0;
	double alpha = 1;
	
	public Heuristic(Network network, JTextArea textArea){
		this.network = network;
		this.textArea = textArea;
	}
	
	public void run(){
		textArea.append("Start of the program\n");
		for(int i = 0; i < network.getBaseStations().size(); i++)
		{			
			textArea.append("Locating Demands of Base Station ID " + i + "\n");
			ArrayList<Integer> demands = network.getBaseStation(i).getDemands();
			for(int j = 0; j < demands.size(); j++){
				if(demands.get(j) != 0 && network.getNetworkService(j).getCentralized()){
					// Put it to the nearest ZARA location that has enough capacity.
					putNearestZARA(network.getBaseStation(i), j);					
				}
			}
			// SOLVE THE KNAPSACK PROBLEM FOR THE REST
			knapsack(network.getBaseStation(i), alpha);		
		}
		textArea.append("End of the program\n");
	}
	
	public void knapsack(BaseStation bs, double alpha){
		ArrayList<DataCenter> list = new ArrayList<DataCenter>(); // Holds the datacenters that are full.
		ArrayList<Integer> eff_capacity = new ArrayList<Integer>();
		DataCenter nearest;
		int minCapacity; // The number of service that DataCenter can store.
		int maxEffCap; // variable for showing the max number of instances that can be packed into the node.
		int maxIndex;
		int instance;
		while(!bs.isDemandsEmpty()){
			nearest = network.findNearestDC_RTT(bs, list);
			eff_capacity = new ArrayList<Integer>();
			for(int i = 0; i < network.getNetworkServices().size(); i++){
				minCapacity = Math.min(nearest.getCPU()/network.getNetworkService(i).getCPU(), nearest.getRAM()/network.getNetworkService(i).getRAM());
				minCapacity = Math.min(minCapacity, nearest.getStorage()/network.getNetworkService(i).getStorage());
				if(bs.getDemand(i) != 0)
					eff_capacity.add(minCapacity*network.getNetworkService(i).getPriority());
				else
					eff_capacity.add(0);
			}
			maxIndex = findMaxEffCap(eff_capacity, bs); // Index of the NetworkService
			maxEffCap = (eff_capacity.get(maxIndex)/network.getNetworkService(maxIndex).getPriority()); // Divides the maxEffCap by the priority level of the service to find the instance number.
			maxEffCap = (int) Math.floor(maxEffCap*alpha);
			
			// if none can be placed into that DataCenter put it into the list of full's.
			if(maxEffCap == 0){
				list.add(nearest);
			}
			else{
				instance = Math.min(maxEffCap, bs.getDemand(maxIndex));
				nearest.setAssignmentListValue(bs.getId(), network.getNetworkService(maxIndex).getId(), nearest.getAssignmentListValue(bs.getId(), network.getNetworkService(maxIndex).getId()) + instance); // Sets the decision variable.
				totalRTT = totalRTT + network.getRTT(bs.getId(), nearest.getId())*instance;
				nearest.setRAM(nearest.getRAM() - instance*network.getNetworkService(maxIndex).getRAM());
				nearest.setCPU(nearest.getCPU() - instance*network.getNetworkService(maxIndex).getCPU());
				nearest.setStorage(nearest.getStorage() - instance*network.getNetworkService(maxIndex).getStorage());
				bs.setDemand(maxIndex, bs.getDemand(maxIndex) - instance);				
			}
		}
	}
	
	// Finds the index of the NetworkService which has the largest effective capacity.
	public static int findMaxEffCap(ArrayList<Integer> list, BaseStation bs){
		int maxValue = 0;
		int maxIndex = 0;
		for (int i = 0; i < list.size(); i++){
		    if (list.get(i) > maxValue && bs.getDemand(i) != 0) {
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
			System.out.println("Assignment List Length: " + n.getDataCenters().get(i).assignment_list.length);
			System.out.println();
		}
	}
	
	public static void printAssignmentValues(Network n){
		for(int i = 0; i < n.getDataCenters().size(); i++)
		{
			System.out.println("DataCenter " + i);
			for(int k = 0; k < n.getBaseStations().size(); k++)
			{
				for(int l = 0; l < n.getNetworkServices().size(); l++)
				{
					System.out.println("(" + k + ", " + l + "): " + n.getDataCenter(i).getAssignmentListValue(k, l));
				}
			}
		}
	}
	
	public double printTotalRTT(){
		System.out.println("Heuristic TOTAL RTT:" + totalRTT);
		return totalRTT;
	}
	
	public static void printDemands(Network n){
		for(int i = 0; i < n.getBaseStations().size(); i++){
			System.out.println("Base Station " + n.getBaseStation(i).getId() + " : " );
			for(int k = 0; k < n.getNetworkServices().size(); k++){
				System.out.println("Service " + n.getNetworkService(k).getId() + " : " + n.getBaseStation(i).getDemand(k));
			}				
		}
	}
	
	public static void printNodeNumber(Network n){
		int totalNumber = 0;
		boolean used = false;
		for(int i = 0; i < n.getDataCenters().size(); i++){
			for(int k = 0; k < n.getBaseStations().size(); k++){
				for(int l = 0; l < n.getNetworkServices().size(); l++){
					if(n.getDataCenter(i).getAssignmentListValue(k, l) > 0){
						used = true;
					}
				}
			}
			if(used){
				totalNumber = totalNumber + 1;
			}
			used = false;
		}
		System.out.println("TOTAL USED DATACENTERS: " + totalNumber);			
	}
	
	public void putNearestZARA(BaseStation bs, int serviceIndex){
		DataCenter nearestZARA;
		ArrayList<DataCenter> list = new ArrayList<DataCenter>();
		int numInstance;
		int instance;
		while(bs.getDemand(serviceIndex) != 0){
			nearestZARA = network.findNearestZARA_RTT(bs, list);
			numInstance = Math.min(nearestZARA.getCPU()/network.getNetworkService(serviceIndex).getCPU(), nearestZARA.getRAM()/network.getNetworkService(serviceIndex).getRAM());
			numInstance = Math.min(numInstance, nearestZARA.getStorage()/network.getNetworkService(serviceIndex).getStorage());
			
			if(numInstance == 0)
				list.add(nearestZARA);
			else{
				instance = Math.min(numInstance, bs.getDemand(serviceIndex));
				nearestZARA.setAssignmentListValue(bs.getId(), network.getNetworkService(serviceIndex).getId(), nearestZARA.getAssignmentListValue(bs.getId(), network.getNetworkService(serviceIndex).getId()) + instance); // Sets the decision variable.
				totalRTT = totalRTT + network.getRTT(bs.getId(), nearestZARA.getId())*instance;
				nearestZARA.setRAM(nearestZARA.getRAM() - instance*network.getNetworkService(serviceIndex).getRAM());
				nearestZARA.setCPU(nearestZARA.getCPU() - instance*network.getNetworkService(serviceIndex).getCPU());
				nearestZARA.setStorage(nearestZARA.getStorage() - instance*network.getNetworkService(serviceIndex).getStorage());
				bs.setDemand(serviceIndex, bs.getDemand(serviceIndex) - instance);
			}
		}
	}
}