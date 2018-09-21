import java.util.ArrayList;

public class Cluster {

	public int cluster_id;
	public ArrayList<BaseStation> base_stations;
	public BaseStation centroid;
	public ArrayList<Integer> service_demands;
	
	public Cluster(int cluster_id, int num_service){
		this.cluster_id = cluster_id;
		this.base_stations = new ArrayList<BaseStation>();
		this.centroid = null;
		this.setDemands(new ArrayList<Integer>(num_service));
	}
	
	public ArrayList<BaseStation> getStations() {
		return base_stations;
	}
	public BaseStation getStation(int index) {
		return base_stations.get(index);
	}
	
	public void addStation(BaseStation bs) {
		base_stations.add(bs);
	}
 
	public void setStations(ArrayList<BaseStation> base_stations) {
		this.base_stations = base_stations;
	}
 
	public BaseStation getCentroid() {
		return centroid;
	}
 
	public void setCentroid(BaseStation centroid) {
		this.centroid = centroid;
	}
 
	public int getId() {
		return cluster_id;
	}
	
	public void setDemands(ArrayList<Integer> service_demands){
    	this.service_demands = service_demands;
    }
    
    public ArrayList<Integer> getDemands(){
    	return service_demands;
    }
	
	public void setDemand(int index, int value){
    	service_demands.set(index, value);
    }
	
	public int getDemand(int index){
    	return service_demands.get(index);
    }
	
	public void clear() {
		base_stations.clear();
	}
	
	public boolean isDemandsEmpty(){
		for(int i = 0; i < getDemands().size(); i++){
			if(getDemands().get(i) != 0)
				return false;
		}
		return true;
	}	
}