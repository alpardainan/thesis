import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ilog.concert.IloException;

import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ilog.concert.IloException;

public class HeuristicApp{
		
	JTextField tf1,tf2,tf3, tf4, tf5, tf6;
	JLabel lb1,lb2,lb3, lb4, lb5, lb6;
	JButton button1,button2,button3,button4, button5, button6;
	static JTextArea textArea, textArea1;
	final static int extraWindowWidth = 100;
	JScrollPane scrollPane, scrollPane1;
	static int num_data, num_base, num_service, num_ZARA, num_cluster;
	long startTime, finishTime;
	Network network, network2, network3;
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, IloException {
		createAndShowGUI();
	}
	
	public void addComponentToPane(Container pane) throws IOException {
        JTabbedPane tabbedPane = new JTabbedPane();
 
        //Create the "cards".
        @SuppressWarnings("serial")
		JPanel card1 = new JPanel(new GridLayout(1,2)) {
            //Make the panel wider than it really needs, so
            //the window's wide enough for the tabs to stay
            //in one row.
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        
        JPanel panel = new JPanel();
        BoxLayout box1 = new BoxLayout(panel,BoxLayout.PAGE_AXIS);
        panel.setLayout(box1);
        button2 = new JButton("Add Base Stations");
        button2.setMaximumSize(new Dimension(150,100));
        button2.setAlignmentX(Component.CENTER_ALIGNMENT);
        button2.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	JFileChooser chooser= new JFileChooser();
            	int choice = chooser.showOpenDialog(pane);
            	if (choice != JFileChooser.APPROVE_OPTION) return;
            	File chosenFile = chooser.getSelectedFile();
            	//textArea1.setDocument((Document) chosenFile);
            	textArea1.setText("");
            	network = new Network();
            	try {
					readBaseStationsFile(chosenFile.getPath(), network);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					textArea1.setText("Please choose an Excel File that confronts to the pattern!");
					e1.printStackTrace();
				} 
            	/*catch(NotOLE2FileException e2){
					textArea1.setText("Please choose an Excel File that confronts to the pattern!");
				}*/
            	catch(IllegalStateException e3){
					textArea1.setText("Please choose an Excel File that confronts to the pattern!");
				}
            }
        });
        panel.add(Box.createRigidArea(new Dimension(30, 30)));
        panel.add(button2);
        panel.add(Box.createRigidArea(new Dimension(30, 30)));
                
        button3 = new JButton("Add Data Centers");
        button3.setMaximumSize(new Dimension(150,100));
        button3.setAlignmentX(Component.CENTER_ALIGNMENT);
        button3.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){

            	JFileChooser chooser= new JFileChooser();
            	int choice = chooser.showOpenDialog(pane);
            	if (choice != JFileChooser.APPROVE_OPTION) return;
            	File chosenFile = chooser.getSelectedFile();
            	//textArea1.setDocument((Document) chosenFile);
            	textArea1.setText("");
            	try {
					readDataCentersFile(chosenFile.getPath(), network);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					textArea1.setText("Please choose an Excel File that confronts to the pattern!");
					e1.printStackTrace();
				}/*catch(NotOLE2FileException e2){
					textArea1.setText("Please choose an Excel File that confronts to the pattern!");
				}*/catch(IllegalStateException e3){
					textArea1.setText("Please choose an Excel File that confronts to the pattern!");
				}
            }
        });
        panel.add(button3);
        panel.add(Box.createRigidArea(new Dimension(30, 30)));

        button4 = new JButton("Add Network Services");
        button4.setMaximumSize(new Dimension(150,100));
        button4.setAlignmentX(Component.CENTER_ALIGNMENT);
        button4.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){

            	JFileChooser chooser= new JFileChooser();
            	int choice = chooser.showOpenDialog(pane);
            	if (choice != JFileChooser.APPROVE_OPTION) return;
            	File chosenFile = chooser.getSelectedFile();
            	//textArea1.setDocument((Document) chosenFile);
            	textArea1.setText("");
            	try {
					readNetworkServicesFile(chosenFile.getPath(), network);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					textArea1.setText("Please choose an Excel File that confronts to the pattern!");
					e1.printStackTrace();
				}/*catch(NotOfficeXmlFileException e2){
					textArea1.setText("Please choose an Excel File that confronts to the pattern!");
				}*/catch(IllegalStateException e3){
					textArea1.setText("Please choose an Excel File that confronts to the pattern!");
				}
            }
        });
        panel.add(button4);
        panel.add(Box.createRigidArea(new Dimension(30, 30)));
        
        button5 = new JButton("Add RTTs");
        button5.setMaximumSize(new Dimension(150,100));
        button5.setAlignmentX(Component.CENTER_ALIGNMENT);
        button5.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){

            	JFileChooser chooser= new JFileChooser();
            	int choice = chooser.showOpenDialog(pane);
            	if (choice != JFileChooser.APPROVE_OPTION) return;
            	File chosenFile = chooser.getSelectedFile();
            	//textArea1.setDocument((Document) chosenFile);
            	textArea1.setText("");
            	try {
            		readRTTFile(chosenFile.getPath(), network);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					textArea1.setText("Please choose an Excel File that confronts to the pattern!");
					e1.printStackTrace();
				}/*catch(NotOfficeXmlFileException e2){
					textArea1.setText("Please choose an Excel File that confronts to the pattern!");
				}*/catch(IllegalStateException e3){
					textArea1.setText("Please choose an Excel File that confronts to the pattern!");
				}
            }
        });
        panel.add(button5);
        panel.add(Box.createRigidArea(new Dimension(30, 30)));
        
        lb6 = new JLabel("Cluster No:");
        lb6.setAlignmentX(Component.CENTER_ALIGNMENT);
        lb6.setAlignmentY(Component.CENTER_ALIGNMENT);
        panel.add(lb6);
        tf6 = new JTextField(20);
        tf6.setPreferredSize(new Dimension(200, 20));
        tf6.setMaximumSize( new Dimension(300, 20));
        tf6.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(tf6);
        
        panel.add(Box.createRigidArea(new Dimension(30, 30)));

        button6 = new JButton("Optimize");
        button6.setMaximumSize(new Dimension(200,100));
        button6.setAlignmentX(Component.CENTER_ALIGNMENT);
        button6.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	network2 = new Network(network);
            	network3 = new Network(network);
            	
            	num_cluster = Integer.parseInt(tf6.getText());
            	System.out.println("Cluster no: " + num_cluster);
            	
            	KMean kmean = new KMean(network, num_cluster);
	            ClusteredNetwork c_network = kmean.run();
	            
            	textArea1.setText("Optimizing...");
            	Heuristic myHeuristic = new Heuristic(network3, textArea);
            	
            	
            	
            	
	            ClusteredHeuristic my2ClusteredHeuristic = new ClusteredHeuristic(c_network, textArea);
                my2ClusteredHeuristic.run();
                my2ClusteredHeuristic.printTOTALRTT_cluster();
	            
            	textArea1.append("\nRunning Heuristic...");
            	myHeuristic.run();
            	myHeuristic.printTotalRTT();
            	ILP ilp = new ILP(network2, textArea1);
            	try {
            		textArea1.append("\nRunning Integer Linear Program...");
					ilp.run();
					textArea1.append("\nOptimization is completed.");
				} catch (IloException e1) {
					// TODO Auto-generated catch block
					textArea1.setText("Something went wrong with the optimizer!");
					e1.printStackTrace();
				}            
            }
        });
        panel.add(button6);          
        card1.add(panel);
        
        JPanel panel1 = new JPanel(new GridLayout(1,2));
        
        textArea1 = new JTextArea(30,30);
        
        textArea1.setMinimumSize(new Dimension(30, 30));
        textArea1.setEditable(false);
        scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1);
        
        //scrollPane.setBounds(10, 11, 455, 249);
        panel1.add(textArea1);
        scrollPane1.setViewportView(textArea1);
        
        card1.add(panel1);
 
        JPanel card2 = new JPanel(new GridLayout(1,2));
        
        JPanel temp = new JPanel();
        BoxLayout box = new BoxLayout(temp,BoxLayout.PAGE_AXIS);
        temp.setLayout(box);
        lb1 = new JLabel("Data Center No:");
        lb1.setAlignmentX(Component.CENTER_ALIGNMENT);
        lb1.setAlignmentY(Component.CENTER_ALIGNMENT);
        temp.add(lb1);
        tf1 = new JTextField(20);
        tf1.setPreferredSize(new Dimension(200, 20));
        tf1.setMaximumSize( new Dimension(300, 20));
        tf1.setAlignmentX(Component.CENTER_ALIGNMENT);
        tf1.setAlignmentY(Component.CENTER_ALIGNMENT);
        temp.add(tf1);
        
        temp.add(Box.createRigidArea(new Dimension(30, 30)));
        
        lb2 = new JLabel("Base Station No:");
        lb2.setAlignmentX(Component.CENTER_ALIGNMENT);
        lb2.setAlignmentY(Component.CENTER_ALIGNMENT);
        temp.add(lb2);
        tf2 = new JTextField(20);
        tf2.setPreferredSize(new Dimension(200, 20));
        tf2.setMaximumSize( new Dimension(300, 20));
        tf2.setAlignmentX(Component.CENTER_ALIGNMENT);
        temp.add(tf2);
        
        temp.add(Box.createRigidArea(new Dimension(30, 30)));
        
        lb3 = new JLabel("Network Service No:");
        lb3.setAlignmentX(Component.CENTER_ALIGNMENT);
        lb3.setAlignmentY(Component.CENTER_ALIGNMENT);
        temp.add(lb3);
        tf3 = new JTextField(20);
        tf3.setPreferredSize(new Dimension(20, 20));
        tf3.setMaximumSize( new Dimension(300, 20));
        tf3.setAlignmentX(Component.CENTER_ALIGNMENT);
        temp.add(tf3);
        
        temp.add(Box.createRigidArea(new Dimension(30, 30)));
        
        lb4 = new JLabel("ZARA No:");
        lb4.setAlignmentX(Component.CENTER_ALIGNMENT);
        lb4.setAlignmentY(Component.CENTER_ALIGNMENT);
        temp.add(lb4);
        tf4 = new JTextField(20);
        tf4.setPreferredSize(new Dimension(20, 20));
        tf4.setMaximumSize( new Dimension(300, 20));
        tf4.setAlignmentX(Component.CENTER_ALIGNMENT);
        temp.add(tf4);
        
        temp.add(Box.createRigidArea(new Dimension(30, 30)));
        
        lb5 = new JLabel("Cluster No:");
        lb5.setAlignmentX(Component.CENTER_ALIGNMENT);
        lb5.setAlignmentY(Component.CENTER_ALIGNMENT);
        temp.add(lb5);
        tf5 = new JTextField(20);
        tf5.setPreferredSize(new Dimension(20, 20));
        tf5.setMaximumSize( new Dimension(300, 20));
        tf5.setAlignmentX(Component.CENTER_ALIGNMENT);
        temp.add(tf5);
        
        
        temp.add(Box.createRigidArea(new Dimension(20, 20)));
        
        button1 = (new JButton("Create and Optimize"));
        button1.setMaximumSize(new Dimension(200,100));
        button1.setAlignmentX(Component.CENTER_ALIGNMENT);
        button1.setAlignmentY(Component.CENTER_ALIGNMENT);
        temp.add(button1);
        button1.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	textArea.setText("");
            	try{
            		double heuristic, heuristic_c;
	                num_data = Integer.parseInt(tf1.getText());
	                num_base = Integer.parseInt(tf2.getText());
	                num_service = Integer.parseInt(tf3.getText());
	                num_ZARA = Integer.parseInt(tf4.getText());
	                num_cluster = Integer.parseInt(tf5.getText());
	                
	                network = new Network(num_base, num_data, num_ZARA, num_service);
	                network2 = new Network(network);
	                network3 = new Network(network);
	                
	        		Heuristic myHeuristic = new Heuristic(network, textArea);
	        		ILP ilp = new ILP(network2, textArea);
	        		
	 	            KMean kmean = new KMean(network3, num_cluster);
	                ClusteredNetwork c_network = kmean.run();
	                
	        		//network2.printBaseStationDemands();
	        		ilp.run();
	                startTime = System.currentTimeMillis();
	        		myHeuristic.run();	
	        		finishTime = System.currentTimeMillis();
	        		myHeuristic.printCapacity(network);
	        		heuristic = myHeuristic.printTotalRTT();
	        		Heuristic.printNodeNumber(network);
	                ClusteredHeuristic myClusteredHeuristic = new ClusteredHeuristic(c_network, textArea);
	                myClusteredHeuristic.run();
	                heuristic_c = myClusteredHeuristic.printTOTALRTT_cluster();
	                
	        		System.out.println("Total execution time: " + (finishTime - startTime));       		
            	}catch(NumberFormatException | IloException e1){
            		textArea.setText("Please enter integer!");
            	}
            }
        });
        
        JPanel temp2 = new JPanel(new GridLayout(1,1));       
    
        textArea = new JTextArea(30, 30);
        //textArea.setPreferredSize(new Dimension(50, 30));
        textArea.setMinimumSize(new Dimension(30, 30));
        textArea.setEditable(false);
        scrollPane = new JScrollPane();
        temp2.add(scrollPane);
        temp2.add(textArea);
        scrollPane.setViewportView(textArea);
 
        card2.add( temp, BorderLayout.WEST );
        card2.add( temp2, BorderLayout.EAST );
        
        tabbedPane.addTab("Create Random Network", card2);
        tabbedPane.addTab("Add New Network", card1);
        
 
        pane.add(tabbedPane, BorderLayout.CENTER);
    }
	
	public static void createAndShowGUI() throws IOException {

	        //Create and set up the window.
	        JFrame frame = new JFrame("VNF Optimizer");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setMinimumSize(new Dimension(60, 30));
	 
	        //Create and set up the content pane.
	        HeuristicApp demo = new HeuristicApp();
	        demo.addComponentToPane(frame.getContentPane());
	 
	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	    }
	 
	 @SuppressWarnings("deprecation")
	public static void readBaseStationsFile(String fileName, Network network) throws IOException{
	        FileInputStream inputStream = new FileInputStream(new File(fileName));
	        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	        Sheet firstSheet = workbook.getSheetAt(0);
	        Iterator<Row> iterator = firstSheet.iterator();
	         
	        while (iterator.hasNext()) {
	            Row nextRow = iterator.next();
	            Iterator<Cell> cellIterator = nextRow.cellIterator();
	            BaseStation bs = new BaseStation();
	            while (cellIterator.hasNext()) {
	            	Cell cell = cellIterator.next();
	            	int columnIndex = cell.getColumnIndex();
                   
	            	switch (columnIndex) {
	                case 0:
	                    bs.setId((int)cell.getNumericCellValue());
	                    textArea1.append("Base Station " + bs.getId() + ":");
	                    break;
	                case 1:
	                	bs.setLongitude(cell.getNumericCellValue());
	                	textArea1.append("\nLong: " +String.valueOf(bs.getLongitude()));
	                    break;
	                case 2:
	                	bs.setLatitude(cell.getNumericCellValue());
	                	textArea1.append("\nLat: " + String.valueOf(bs.getLatitude()) + "\n");
	                	textArea1.append("-------------------\n");
	                    break;
	                 default:
	                	 bs.addDemand((int)cell.getNumericCellValue()); 
	                	 break;
	                }
	        } 
	        network.addBaseStation(bs);  
	        workbook.close();
	        inputStream.close();
	        }
	 }

	public static void readDataCentersFile(String fileName, Network network) throws IOException{

	        FileInputStream inputStream = new FileInputStream(new File(fileName));
	        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	        Sheet firstSheet = workbook.getSheetAt(0);
	        Iterator<Row> iterator = firstSheet.iterator();
	        int id = 0;
	    	double longitude = 0;
	        double latitude = 0;
	        int ram = 0;
	        int storage = 0;
	        int cpu = 0;
	        boolean zara = false;
	        int[][] assignment_list;
	        int num_base;
	        int num_service;
	         
	        while (iterator.hasNext()) {
	            Row nextRow = iterator.next();
	            Iterator<Cell> cellIterator = nextRow.cellIterator();
	            DataCenter dc = new DataCenter();
	            while (cellIterator.hasNext()) {
	            	Cell cell = cellIterator.next();
	            	int columnIndex = cell.getColumnIndex();
                
	            	switch (columnIndex) {
	                case 0:
	                    id = (int)cell.getNumericCellValue();
	                    textArea1.append("Data Center " + id + ":");
	                    break;
	                case 1:
	                	longitude = cell.getNumericCellValue();
	                	textArea1.append("\nLong: " + longitude);
	                    break;
	                case 2:
	                	latitude = cell.getNumericCellValue();
	                	textArea1.append("\nLat: " + latitude);
	                    break;
	                case 3:
	                	ram = (int)cell.getNumericCellValue();
	                	textArea1.append("\nRAM: " + ram);
	                	break;
	                case 4: 
	                	cpu = (int)cell.getNumericCellValue();
	                	textArea1.append("\nCPU: " + cpu);
	                	break;
	                case 5:
	                	storage = (int)cell.getNumericCellValue();
	                	textArea1.append("\nStorage: " + storage);
	                	break;
	                case 6:
	                	zara = cell.getBooleanCellValue();
	                	textArea1.append("\nZARA: " + zara + "\n");
	                	textArea1.append("-------------------\n");
	                	break;
	                	//System.out.println(cell.getNumericCellValue());       	
	                }
	            	try{
		            	num_base = network.getBaseStations().size();
	            		num_service = network.getBaseStation(0).getDemands().size();
	            		dc = new DataCenter(id,longitude, latitude, ram, cpu, storage, zara, num_base, num_service);
	        	         
	            	}catch(IndexOutOfBoundsException e1){
	            		textArea1.setText("Please first load the Base Stations!");
	            	}catch(NullPointerException e2){
	            		textArea1.setText("Please first load the Base Stations!");
	            	}     	
	        } 
	        try{
		        network.addDataCenter(dc); 
	        }catch(NullPointerException e2){
	        	textArea1.setText("Please first load the Base Stations!");
	        }
	        // dc.num_service = network.getNetworkServices().size();
	        workbook.close();
	        inputStream.close();
	        }
	 }
	
	public static void readNetworkServicesFile(String fileName, Network network) throws IOException{

		FileInputStream inputStream = new FileInputStream(new File(fileName));
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();
         
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            NetworkService ns = new NetworkService();
            while (cellIterator.hasNext()) {
            	Cell cell = cellIterator.next();
            	int columnIndex = cell.getColumnIndex();
            
            	switch (columnIndex) {
                case 0:
                    ns.setName(cell.getStringCellValue());
                    break;
                case 1:
                	ns.setId((int)cell.getNumericCellValue());
                	textArea1.append("Network Service " + ns.getId() + ":");
                	textArea1.append("\nName: " + ns.getName());
                    break;
                case 2:
                	ns.setRAM((int)cell.getNumericCellValue());
                	textArea1.append("\nRAM: " + ns.getRAM());
                    break;
                case 3:
                	ns.setCPU((int)cell.getNumericCellValue());
                	textArea1.append("\nCPU: " + ns.getCPU());
                	break;
                case 4: 
                	ns.setStorage((int)cell.getNumericCellValue());
                	textArea1.append("\nStorage: " + ns.getStorage());
                	break;
                case 5:
                	ns.setPriority((int)cell.getNumericCellValue());
                	textArea1.append("\nPriority: " + ns.getPriority());
                	break;
                case 6:
                	ns.setCentralized(cell.getBooleanCellValue());
                	textArea1.append("\nCentralized: " + ns.getCentralized() );
                	break;
                case 7:
                	ns.setLicense((int)cell.getNumericCellValue());
                	textArea1.append("\nAvailable License Number: " + ns.getLicense());
                	break;
                case 8:
                	ns.setReliability(cell.getNumericCellValue());
                	textArea1.append("\nReliability Requirement: " + ns.getReliability()+ "\n");
                	textArea1.append("-------------------\n");
                	break;                	
                }
        }      
        try{
            network.addNetworkService(ns);  
        }catch(NullPointerException e1){
        	textArea1.setText("Please first Load Base Stations and Data Centers!");
        }
        workbook.close();
        inputStream.close();
        }
	}

	public static void readRTTFile(String fileName, Network network) throws IOException{
		FileInputStream inputStream = new FileInputStream(new File(fileName));
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();
         
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            ArrayList<Double> tempRTT = new ArrayList<Double>();
            while (cellIterator.hasNext()) {
            	Cell cell = cellIterator.next();
            	tempRTT.add(cell.getNumericCellValue());
        }   
        try{
            network.RTTs.add(tempRTT);  
        }catch(NullPointerException e1){
        	textArea1.setText("Please first Load Base Stations, Data Centers and Network Services!");
        }
        workbook.close();
        inputStream.close();
        }
	}	 
	public static double sum (List<Double> a){
        if (a.size() > 0) {
            double sum = 0;
 
            for (Double i : a) {
                sum += i;
            }
            return sum;
        }
        return 0;
    }
    public static double mean (List<Double> a){
        double sum = sum(a);
        double mean = 0;
        mean = sum / (a.size() * 1.0);
        return mean;
    }
    public static double sd (List<Double> a){
        double sum = 0;
        double mean = mean(a);
 
        for (Double i : a)
            sum += Math.pow((i - mean), 2);
        return Math.sqrt( sum / ( a.size() - 1 ) ); // sample
    }
}	