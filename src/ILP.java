import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

import ilog.concert.*;
import ilog.cplex.*;

public class ILP {

	Network network;
	boolean used = false;
	JTextArea textArea;
	
	public ILP(Network network, JTextArea textArea){
		this.network = network;
		this.textArea = textArea;
	}
	
	public void run() throws IloException{
		/////////////////////////////////////////////////////////////////
		//						MODEL
		/////////////////////////////////////////////////////////////////
		int num_base = network.getBaseStations().size();
		int num_service = network.getNetworkServices().size();
		int num_data = network.getDataCenters().size();
		int L = 100000;
		
		
//		textArea.append("Start of the ILP program\n");
//		textArea.append("Creating Variables...\n");
		IloCplex cplex = new IloCplex();
		IloIntVar[] D = cplex.intVarArray(num_data,0,1);
		IloIntVar[][][] X = new IloIntVar[num_base][num_service][num_data];
		for(int i = 0; i < num_base; i++) {
			for(int j = 0; j < num_service; j++) {			      
				for(int k = 0; k < num_data; k++) {	
						X[i][j][k] = cplex.intVar(0, 1); 
				}
			}
		}
	
		IloIntVar[][][] N = new IloIntVar[num_base][num_service][num_data];
		for(int i = 0; i < num_base; i++) {
			for(int j = 0; j < num_service; j++) {			      
				for(int k = 0; k < num_data; k++) {		
						N[i][j][k] = cplex.intVar(0, Integer.MAX_VALUE); 
				}
			}
		}
//		textArea.append("Objective is function is being added...\n");
		// OBJECTIVE		
		IloLinearNumExpr objective = cplex.linearNumExpr();
		for(int j = 0; j < num_base; j++) {
			for(int c = 0; c < num_service; c++) {
				for(int i = 0; i < num_data; i++) {		
					objective.addTerm(network.getNetworkService(c).getPriority()*network.getRTT(j,i), N[j][c][i]);
				}
			}
		}
		
		for(int i = 0; i < num_data; i++) {
			objective.addTerm(1, D[i]);
		}
		
		cplex.addMinimize(objective);
		
		// Constraints
		List<IloRange> constraints = new ArrayList<IloRange>();
		
		IloLinearNumExpr expr7; 
		for(int i = 0; i < num_base; i++) {
			for(int j = 0; j < num_service; j++) {
				for(int k = 0; k < num_data; k++) {
					expr7 = cplex.linearNumExpr();
					expr7.addTerm(1, D[k]);
					expr7.addTerm(-1,X[i][j][k]);
					constraints.add(cplex.addGe(expr7, 0));
				}
			}
		}
		
		
		//Constraint 1
		IloLinearNumExpr expr1; 
		for(int i = 0; i < num_base; i++) {
			for(int j = 0; j < num_service; j++) {
				for(int k = 0; k < num_data; k++) {
					expr1 = cplex.linearNumExpr();
					expr1.addTerm(L, X[i][j][k]);
					expr1.addTerm(-1,N[i][j][k]);
					constraints.add(cplex.addGe(expr1, 0));
				}
			}
		}
		
		// Constraint 2
		IloLinearNumExpr expr2;
		for(int i = 0; i < num_data; i++) {
			expr2 = cplex.linearNumExpr();
			for(int j = 0; j < num_base; j++) {
				for(int c = 0; c < num_service; c++) {
					expr2.addTerm(network.getNetworkService(c).getRAM(), N[j][c][i]);							
				}
			}
			constraints.add(cplex.addGe(network.getDataCenter(i).getRAM(),expr2));
		}
		
		//Constraint 3
		IloLinearNumExpr expr3;
		for(int i = 0; i < num_data; i++) {
			expr3 = cplex.linearNumExpr();
			for(int j = 0; j < num_base; j++) {
				for(int c = 0; c < num_service; c++) {
					expr3.addTerm(network.getNetworkService(c).getCPU(), N[j][c][i]);						
				}
			}
			constraints.add(cplex.addGe(network.getDataCenter(i).getCPU(), expr3));
		}
		
		//Constraint 4
		IloLinearNumExpr expr4;
		for(int i = 0; i < num_data; i++) {
			expr4 = cplex.linearNumExpr();
			for(int j = 0; j < num_base; j++) {
				for(int c = 0; c < num_service; c++) {
					expr4.addTerm(network.getNetworkService(c).getStorage(), N[j][c][i]);						
				}
			}
			constraints.add(cplex.addGe(network.getDataCenter(i).getStorage(),expr4));
		}
		
		//Constraint 5
		IloLinearNumExpr expr5;
		for(int j = 0; j < num_base; j++) {
			for(int c = 0; c < num_service; c++) {		
				expr5 = cplex.linearNumExpr();
				for(int i = 0; i < num_data; i++) {
					expr5.addTerm(1,N[j][c][i]);
				}
				constraints.add(cplex.addGe(expr5,network.getBaseStation(j).getDemand(c)));
			}
		}
		
		// Constraint
		IloLinearNumExpr expr6;
		for(int i = 0; i < num_data; i++){
			if(!network.getDataCenter(i).getZARA()){
				for(int j = 0; j < num_base; j++){
					for(int c = 0; c < num_service; c++){
						if(network.getNetworkService(c).getCentralized()){
							expr6 = cplex.linearNumExpr();
							expr6.addTerm(1, X[j][c][i]);
							constraints.add(cplex.addLe(expr6, 0));
						}
					}
				}
			}
		}
		
		
			
		if (cplex.solve()) {
			//System.out.println("Objective Value: " +  cplex.getObjValue());
			Double value = 0.0;
			for(int i = 0; i < num_base; i++) {
				for(int j = 0; j < num_service; j++) {
					for(int k = 0; k < num_data; k++) {
						value = value + network.getRTT(i,k)*cplex.getValue(N[i][j][k]);
					}
				}
			}
			
			// Calculates how many datacenters are used.
			int totalNode = 0;
			for(int i = 0; i < num_data; i++){
				used = false;
				for(int k = 0; k < num_base; k++){
					for(int l = 0; l < num_service; l++){
						if(cplex.getValue(N[k][l][i]) > 0){
							used = true;
							break;
						}
					}
				}				
				if(used){
					totalNode = totalNode + 1;
				}
			}
			System.out.println("OBJECTIVE: " + value);
			System.out.println("TOTAL NUMBER OF NODES USED ILP: " + totalNode);
//			textArea.append("End of the ILP program\n");
		}else {
			System.out.println("Model not solved");		
		}
		cplex.end();
	}	
}