package hypeercube;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class Uxer implements Control{
	
	/** Stringa per recuperare il nome del protocollo dal file di conf */
    private String PAR_PROT = "protocol";
    
    /** Pid del protocollo */
    public int pid;
    
    public String dec;
    
    public Node node;
    
    public EProtocol e;
    
    public int base;
    
    private ArrayList<Node> Neighbors;
    
    public Uxer(String prefix) {
    	this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

	@Override
	public boolean execute() {
		
		//stampa inserita
		for(int i=0; i<Network.size();i++) {
					
			Node node = (Node) Network.get(i);	
			EProtocol ne = ((EProtocol) node.getProtocol(this.pid));
			base=ne.getHyper();
			System.out.println("Id del nodo: " + node.getID());
			System.out.println("BitId del nodo: " + createBinaryID((int)node.getID()));
			Neighbors = ne.Neighbors;
			
			System.out.println("Porzione Hash del nodo: " + ne.getHash());
	
			for(int j=0; j< Neighbors.size();j++) {
				String id = createBinaryID((int)Neighbors.get(j).getID());
				System.out.println("ecco i vicini :" + id);
			}	
			System.out.println("#############");
		}
		//
		
		System.out.println("Ciao! Benvenuto nella Hypeercube DHT!" + "\n" + "Premi il tasto relativo all'operazione che intendi effettuare.");
		System.out.println("1: Inserimento oggetto e relative keyword;" + "\n" +
						   "2: Ricerca PIN SEARCH;" + "\n" + 
						   "3: Ricerca SUPERSET SEARCH." +"\n" +
						   "0: EXIT" + "\n");	
		
		 boolean check=false;
		 while(!check) {
	        	boolean def = false;
	        	boolean exit = false;
	        	boolean c = false;
	        	
	        	while(c==false) {
	        		Scanner scan = new Scanner(System.in);
	            	dec = scan.nextLine();
	            	if (dec.matches("[0-9]+")) c = true;
	            	if(c==false) System.out.println("Inserire un numero");           		
	        	}
	        	
	        	int decision = Integer.parseInt(dec);
	        	
	        	switch(decision) {
	        	
	        		case (1):
	        			System.out.println("Inserisci un oggetto");
	        			Scanner scan1 = new Scanner(System.in);
	        			String in = scan1.nextLine();
	        			
	        			System.out.println("\n" +"Inserisci delle parole chiavi associate all'oggetto");
	        			Scanner scan2 = new Scanner(System.in);
	       				String in2 = scan2.nextLine();
	       				
	       				in2 = in2.trim().replaceAll("[ ]{2,}", " ");
	       				String[] store = in2.split("[\\. ]");
	       				String[] ref = new String[store.length];
	       				String storeKey="";
	       				for(int i=0; i<store.length;i++) {
	       					ref[i] = store[i].substring(0,1);
	       					storeKey = storeKey + store[i];
	       					if(i!=store.length-1) {
	       						storeKey= storeKey + " ";
	       					}
	       				}
	       				       				
	        			InsertObject i = new InsertObject(in);
	        			
	        			boolean type2=false;
	       				while(!type2) {
	       				Random rand1 = new Random();
	    				int casuale1=rand1.nextInt(Network.size());
	        			node = (Node) Network.get(casuale1);
	        			e = ((EProtocol) node.getProtocol(this.pid));
	        			type2 = e.getType();
	       				}

	        			InsertKeyword kw = new InsertKeyword(ref, storeKey, in);
	        			
	        			boolean verific = false;
	        			for(int j=0; j<Network.size();j++) {
	        				Node node1 = (Node) Network.get(j);
	            			EProtocol ep = ((EProtocol) node1.getProtocol(this.pid));
	            			if(ep.getListKey().containsKey(storeKey)) {
	            				System.out.println("Keyword già presente, prego utilizzarne un'altra");
	            				verific=true;
	            			}
	        			}
			
	        			if(verific==false) {
	        			e.processEvent(node, pid, i);
	        			e.processEvent(node, pid, kw);
	        			System.out.println("\n" + "Oggetto e relative Keywords inserite correttamente!" + "\n");
	        			
	   //mostra tutti i nodi della rete e in quale di questi è presente l'oggetto e in quale la keyword inserita
	        			for(int j=0; j<Network.size();j++) {
	            			Node node1 = (Node) Network.get(j);
	            			EProtocol ne = ((EProtocol) node1.getProtocol(this.pid));
	            			System.out.println("ecco la lista oggetti: " + ne.getObjects());
	            			System.out.println("ecco la lista keyword: " + ne.getListKey());
	            			System.out.println("-------------------------------------");
	            			}    
	        			}
	        			break;
	        			
	        		case (2):
	        			System.out.println("Ricerca PIN SEARCH:");
	        			System.out.println("Inserisci un insieme di Keyword al fine di recuperare oggetti descrivibili dal set stesso:");
	        			Scanner scan3 = new Scanner(System.in);
	       				String kw1 = scan3.nextLine();
	       				kw1 = kw1.trim().replaceAll("[ ]{2,}", " ");
	       				System.out.println("Keywords: "+ kw1);
	       				String[] store1 = kw1.split("[\\. ]");
	       				
	       				HashSet<String> hashKw = new HashSet<String>();
	       				String[] ref1 = new String[store1.length];
	       				
	       				for(int j=0; j<store1.length;j++) {
	       					hashKw.add(store1[j]);
	       					ref1[j] = store1[j].substring(0,1);
	       				}

	       				Search pin = new Search(ref1);
	       				
	       				boolean type=false;
	       				while(!type) {
	       				Random rand1 = new Random();
	    				int casuale1=rand1.nextInt(Network.size());
	        			node = (Node) Network.get(casuale1);
	        			e = ((EProtocol) node.getProtocol(this.pid));
	        			type = e.getType();
	       				}
	        			
	       
	        			e.processEvent(node, pid, pin);
	        			
	        			boolean result=false;
	        			for(int j=0; j<Network.size();j++) {
	        				
	            			Node node2 = (Node) Network.get(j);
	            			EProtocol ep = ((EProtocol) node2.getProtocol(this.pid));
	            			if(!(ep.getListOb().isEmpty())) {
	            				System.out.println("Oggetto: " + ep.getListOb());
	                			ep.removeListOb();
	                			result=true;
	            			}
	            			
	            			}       		
	        			
	        			if(result==false) System.out.println("Le Keywords inserite non risultano presenti");
	        			break;
	        			
	        		case (3):
	        			System.out.println("Ricerca SUPERSET SEARCH:");
	        			System.out.println("Inserisci un insieme di Keyword al fine di recuperare oggetti descrivibili dal set stesso e un limite massimo di risultati ottenibili:");
	        			System.out.println("Inserisci le Keyword: "); 
	        			Scanner scan4 = new Scanner(System.in);
	        			String kw2 = scan4.nextLine();
	        			kw2 = kw2.trim().replaceAll("[ ]{2,}", " ");
	       				System.out.println("Keywords: "+ kw2);
	       				String[] store2 = kw2.split("[\\. ]");
	       				
	       				HashSet<String> hashKw1 = new HashSet<String>();
	       				String[] ref2 = new String[store2.length];
	       				
	       				for(int j=0; j<store2.length;j++) {
	       					hashKw1.add(store2[j]);
	       					ref2[j] = store2[j].substring(0,1);
	       				}
	        		
	       				System.out.println("Inserisci il numero massimo di risultati che intendi ottenere: ");
	       				
	       				boolean num = false;
	       				String number="";
	       				while(num==false) {
	       					Scanner scan5 = new Scanner(System.in);
	       					number = scan5.nextLine();
	       					if (number.matches("[0-9]+")) num = true;
	       	            	if(num==false) System.out.println("Inserire un numero");
	       				}
	       				
	       				int numb = Integer.parseInt(number);
	       				
	       				Search superset = new Search(ref2, numb);
	       				
	       				boolean type1=false;
	       				while(!type1) {
	       				Random rand1 = new Random();
	    				int casuale1=rand1.nextInt(Network.size());
	        			node = (Node) Network.get(casuale1);
	        			e = ((EProtocol) node.getProtocol(this.pid));
	        			type1 = e.getType();
	       				}
	        			
	       
	        			e.processEvent(node, pid, superset);
	        			
	        			boolean result1=false;
	        			for(int j=0; j<Network.size();j++) {
	        				
	            			Node node3 = (Node) Network.get(j);
	            				
	            			EProtocol ep1 = ((EProtocol) node3.getProtocol(this.pid));
	            			if(!(ep1.getListOb().isEmpty())) {
	            				System.out.println("Oggetto: " + ep1.getListOb());
	                			ep1.removeListOb();
	                			result1=true;
	            				}
	            			
	            			}       		
	        			
	        			if(result1==false) System.out.println("Le Keyword inserite non risultano presenti");
	        			break;
	        			
	        		case (0):
	        			System.out.println("Grazie, arrivederci!");
	        			exit=true;
	        			check=true;
	        			break;
	        			
	        		default:
	        			def=true;
	        			System.out.println("Prego inserire un numero presente nella lista.");
	        			System.out.println("1: Inserimento oggetto e relative keyword;" + "\n" +
	        							   "2: Ricerca PIN SEARCH;" + "\n" + 
	        							   "3: Ricerca SUPERSET SEARCH." +"\n" +
	        							   "0: EXIT" + "\n");
	        			
	        	}
	        	if(exit==false && def == false) {
	        	System.out.println("Vuoi continuare a effettuare operazioni? Yes/No");
	        	Scanner scan3 = new Scanner(System.in);
				String in = scan3.nextLine().toLowerCase();
				switch(in) {
				case("yes"):
					System.out.println("1: Inserimento oggetto e relative keyword;" + "\n" +
									   "2: Ricerca PIN SEARCH;" + "\n" + 
									   "3: Ricerca SUPERSET SEARCH." +"\n" +
									   "0: EXIT" + "\n");	
					break;
				case("no"):
					check=true;
					System.out.println("Grazie, arrivederci!");
					break;
				default:
	    			System.out.println("Prego inserire yes or no");  
				}
	        }
	        	
	        }
		 
		return false;
	}
	
	//trasformo il numero del nodo nel corrispondente in binario
		 public String createBinaryID(int n) {
			 String IDBinario = Integer.toBinaryString(n);
		//se il codice binario del nodo ottenuto non è della lunghezza r (dimensione dell'ipercubo) allora aggiungo gli zeri necessari
		    while (IDBinario.length() < base){
		    	  IDBinario = "0" + IDBinario;
		        }
			 return IDBinario;
			 
		 }

}
