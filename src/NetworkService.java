import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class NetworkService {

	private String name;
	private int id;
	private int ram = 0;
    private int storage = 0;
    private int cpu = 0;
    private int priority_level = 0;
    private boolean centralized = false;
    private int license = 0;
    private double reliability = 0.0;
    
    public NetworkService(){};
    
    public NetworkService(int id,int ram, int storage, int cpu, int priority_level, boolean centralized, int license, double reliability){    
    	this.setId(id);
    	this.setRAM(ram);
    	this.setStorage(storage);
    	this.setCPU(cpu);
    	this.setPriority(priority_level);
    	this.setCentralized(centralized);
    	this.setLicense(license);
    	this.setReliability(reliability);
    }
   
    // Copy Constructor
    public NetworkService(NetworkService ns){
    	this.name = ns.getName();
    	this.id = ns.getId();
    	this.ram = ns.getRAM();
    	this.storage = ns.getStorage();
    	this.cpu = ns.getCPU();
    	this.priority_level = ns.getPriority();
    	this.centralized = ns.getCentralized();	
    	this.license = ns.getLicense();
    	this.reliability = ns.getReliability();
    }

    public void setId(int id){
    	this.id = id;
    }
    
    public int getId(){
    	return this.id;
    }
    
    public void setName(String name){
    	this.name = name;
    }
    
    public String getName(){
    	return this.name;
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
    
    public void setPriority(int priority_level){
    	this.priority_level = priority_level;
    }
    
    public int getPriority(){
    	return this.priority_level;
    }
    
    public void setCentralized(boolean centralized){
    	this.centralized = centralized;
    }
    
    public boolean getCentralized(){
    	return this.centralized;
    }
    
    public int getLicense(){
    	return this.license;
    }
    
    public void setLicense(int license){
    	this.license = license;
    }
    
    public double getReliability(){
    	return this.reliability;
    }
    
    public void setReliability(double reliability){
    	this.reliability = reliability;
    }
    
    public static NetworkService createRandomNetworkService(int id){
    	int ram = ThreadLocalRandom.current().nextInt(3, 6 + 1);
    	int storage = ThreadLocalRandom.current().nextInt(4, 10 + 1);
    	int cpu =  ThreadLocalRandom.current().nextInt(1, 3 + 1);
    	int priority_level = ThreadLocalRandom.current().nextInt(2, 5 + 1);
    boolean centralized = ThreadLocalRandom.current().nextBoolean();
    	int license = ThreadLocalRandom.current().nextInt(100000, 200000 + 1);
    	double reliability = ThreadLocalRandom.current().nextDouble(0.0, 1.0 + 1);
    	return new NetworkService(id, ram, storage, cpu, priority_level, centralized, license, reliability);
    }
    
    public static ArrayList<NetworkService> createRandomNetworkServices(int num){
    	ArrayList<NetworkService> network_services = new ArrayList<NetworkService>();
    	for(int i = 0; i < num; i++){
    		network_services.add(createRandomNetworkService(i));
    	}
    	return network_services;
    }
}