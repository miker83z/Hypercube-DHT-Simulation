package hypeercube;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    
    private String PAR_MAXSIZE = "maxsize";
    
    private String PAR_LAP = "lap";
    
    /** Pid del protocollo */
    public int pid;
    
    public String dec;
    
    public Node node;
    
    public EProtocol e;
    
    public int base;
    
	private String[] abc123 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

	private int rete;
	
	private int maxsize;
	
	private int lap;
	
	private int kw;
	
	private int request;
	
	private ArrayList<String> cont = new ArrayList<String>();
    
    private ArrayList<Node> Neighbors;
    
    public String exc;
    
    public int max;
    
    public ArrayList<String> exchange = new ArrayList<String>();
    
    public Result resultP = new Result();
    
    public Uxer(String prefix) {
    	this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
    	this.maxsize = Configuration.getInt(prefix + "." + PAR_MAXSIZE); 
    	this.lap = Configuration.getInt(prefix + "." + PAR_LAP);
    }

	@Override
	public boolean execute() {
		
		//stampa inserita
				for(int i=0; i<Network.size();i++) {
							
					Node node = (Node) Network.get(i);	
					EProtocol ne = ((EProtocol) node.getProtocol(this.pid));
					base=ne.getHyper();
					//System.out.println("Id del nodo: " + node.getID());
					//System.out.println("BitId del nodo: " + createBinaryID((int)node.getID()));
					Neighbors = ne.Neighbors;

					for(int j=0; j< Neighbors.size();j++) {
						String id = createBinaryID((int)Neighbors.get(j).getID());
						//System.out.println("ecco i vicini :" + id);
					}	
					//System.out.println("#############");
				}
		
		
				
		System.out.println("Ciao! Benvenuto nella Hypeercube DHT!" + "\n");
		System.out.println("Digita il numero di oggetti che vuoi inserire" + "\n" );	
				
		boolean resp = false;
		while(resp==false) {
    		Scanner scan = new Scanner(System.in);
        	dec = scan.nextLine();
        	if (dec.matches("[0-9]+")) resp = true;
        	if(resp==false) System.out.println("Inserire un numero");           		
    	}
		
		 request = Integer.parseInt(dec);
		//sceglie un nodo con type true cioe quelli di size
			boolean type2=false;
			while(!type2) {
			Random rand1 = new Random();
			int casuale1=rand1.nextInt(Network.size());
			node = (Node) Network.get(casuale1);
			e = ((EProtocol) node.getProtocol(this.pid));
			type2 = e.getType();
			}
		
		for(int i=0; i<abc123.length;i++) {
			cont.add(abc123[i]);
		}
		
		rete=0;
		
		while(rete!=request) {
			
			int longString = (int) (Math.random() * 31);
			String obj = "";
			int q=0;
			
			while(q!=longString) {
				String e; 
				Random rand = new Random();
				int casuale=rand.nextInt(cont.size());
				e = cont.get(casuale);
				obj = obj + e;
				q++;
			}
			//System.out.println("ecco la stringa dell'oggetto: " + obj);
			String key = "";
			int k=0;
			while(k!=base) {
				int keyword = getRandomN(0, 2);
				key = key + keyword;
				k++;
			}
			
			//System.out.println("ecco la keyword dell'oggetto: " + key);
			String hashObj = Sha256(obj);
			Insert insert = new Insert(hashObj, key);
			//System.out.println("ecco l'hash dell'oggetto: " + hashObj);
			e.processEvent(node, pid, insert);
			rete++;
		}
		System.out.println("\n" + "Oggetti e relative Keywords inserite correttamente!" + "\n");
		
		//mostra tutti i nodi della rete e in quale di questi è presente l'oggetto e in quale la keyword inserita
		for(int j=0; j<Network.size();j++) {
			Node node1 = (Node) Network.get(j);
			EProtocol ne = ((EProtocol) node1.getProtocol(this.pid));
			//System.out.println("ecco la lista keyword e oggetti: "+ createBinaryID((int) node1.getID()) + " = "+ ne.getKeyHash());
			//System.out.println("-------------------------------------");
			}   
		
		System.out.println( "Selezionare il tipo di ricerca da effettuare: " + "\n" +
							"1: Ricerca PIN SEARCH;" + "\n" + 
							"2: Ricerca SUPERSET SEARCH." +"\n" +
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
	        			System.out.println("Ricerca PIN SEARCH:");
	        		
	        		int turn = 0;
	        		exchange.add(String.valueOf(maxsize));
        			exchange.add(String.valueOf(request));
        			
	        		while (turn<lap) {
	        			
	        			Random r = new Random();
	        			int kw1 = r.nextInt(Network.size());
	        			
	        			int cMex = 0;
	       				Search pin = new Search(kw1, cMex);
	       			
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
	        			max=0;
	        			for(int j=0; j<Network.size();j++) {
	        				
	            			Node node2 = (Node) Network.get(j);
	            			EProtocol ep = ((EProtocol) node2.getProtocol(this.pid));
	            			
	            			if(ep.getContMex()>0) {
	            				if(ep.getContMex()> max) {
		            				max = ep.getContMex();
		            			}
	            				ep.removeCont();
	            			}
	            			
	            			if(!(ep.getListObHash().isEmpty())) {
	            				System.out.println("Ecco gli hash degli oggetti: " + ep.getListObHash());
	                			ep.removeListObHash();
	                			result=true;
	            				}
	            			
	            			}
	        			if(result==false) System.out.println("Non ci sono riferimenti ad oggetti con la keyword inserita.");
	        			System.out.println("Ecco il numero degli scambi: " + max);
	        			exc = String.valueOf(max);
        				exchange.add(exc);
	        			turn++;
	        		}
	        		
	        		resultP.printResult(exchange);
    				exchange.clear();
	        		
	        		break;
	      
	        		case (2):
	        			System.out.println("Ricerca SUPERSET SEARCH:");
	        			
	        			int turnS = 0;
	        			exchange.add(String.valueOf(maxsize));
	        			exchange.add(String.valueOf(request));
	        			
	        			
	        			while (turnS<lap) {

	        				Random s = new Random();
		        			int kwS = s.nextInt(Network.size());
	        				
		        			//int numb = getRandomN(1, 21);
		        			int numb = 10;
		        			
		        			int cMex = 0;
	       				Search superset = new Search(kwS, numb, cMex);
	       				
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
	        			max=0;
	        			for(int j=0; j<Network.size();j++) {
	        				
	            			Node node3 = (Node) Network.get(j);
	            				
	            			EProtocol ep1 = ((EProtocol) node3.getProtocol(this.pid));
	            			
	            			if(ep1.getContMex()>0) {
	            				if(ep1.getContMex()> max) {
		            				max = ep1.getContMex();
		            			}
	            				ep1.removeCont();
	            			}
	            			
	            			
	            			if(!(ep1.getListObHash().isEmpty())) {
	            				System.out.println("Ecco gli hash degli oggetti: " + ep1.getListObHash());
	                			ep1.removeListObHash();
	                			result1=true;
	            				}
	            			
	            			}
	        			if(result1==false) System.out.println("Non ci sono riferimenti ad oggetti con la keyword inserita.");
		        			System.out.println("Ecco il numero degli scambi: " + max);
		        			exc = String.valueOf(max);
	        				exchange.add(exc);
	        				//exchange.add(String.valueOf(numb));
		        			turnS++;
	        			}
	        			resultP.printResultS(exchange);
	    				exchange.clear();
		        		
	        			break;
	        			
	        		case (0):
	        			System.out.println("Grazie, arrivederci!");
	        			exit=true;
	        			check=true;
	        			break;
	        			
	        		default:
	        			def=true;
	        			System.out.println("Prego inserire un numero presente nella lista.");
	        			System.out.println("1: Ricerca PIN SEARCH;" + "\n" + 
	        							   "2: Ricerca SUPERSET SEARCH." +"\n" +
	        							   "0: EXIT" + "\n");
	        			
	        	}
	        	if(exit==false && def == false) {
	        	System.out.println("Vuoi continuare a effettuare operazioni? Yes/No");
	        	Scanner scan3 = new Scanner(System.in);
				String in = scan3.nextLine().toLowerCase();
				switch(in) {
				case("yes"):
					System.out.println("1: Ricerca PIN SEARCH;" + "\n" + 
									   "2: Ricerca SUPERSET SEARCH." +"\n" +
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
	
	
	/** Trasforma una stringa nel corrispettivo in hash Sha256 */
	public String Sha256(String object){
		try {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		
		byte[] hash = digest.digest(object.getBytes(StandardCharsets.UTF_8));

        BigInteger convert = new BigInteger(1, hash); 

        String Hashtext = convert.toString(16); 
        while (Hashtext.length() < 32) { 
            Hashtext = "0" + Hashtext; 
        } 
        
        return Hashtext;
		
		}catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        } 
	}
	
		/** trasformo il numero del nodo nel corrispondente in binario */
		 public String createBinaryID(int n) {
			 String IDBinario = Integer.toBinaryString(n);
	/** se il codice binario del nodo ottenuto non è della lunghezza r (dimensione dell'ipercubo) allora aggiungo gli zeri necessari */
		    while (IDBinario.length() < base){
		    	  IDBinario = "0" + IDBinario;
		        }
			 return IDBinario;
		 }
		 
		 public int getRandomN(int min, int max) {
			    Random random = new Random();
			    return random.nextInt(max - min) + min;
			}

}
