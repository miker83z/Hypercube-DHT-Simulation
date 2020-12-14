package hypeercube;

import java.util.ArrayList;
import java.util.Random;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class NetworkPeer implements Control{
	
	 /** Stringa per recuperare il nome del protocollo dal file di conf */
	   private static final String PAR_PROT = "protocol";
	   /** Pid del protocollo */
	   private final int pid;
	   
	   private ArrayList<Node> Neighbors;
	  
	   private int rete;
	   
	   private int alfax;
	   
	   /** Array contenente tutti i possibili valori iniziali dell'hash di un oggetto. */
	   private String[] abc123 = {"a", "b", "c", "d", "e", "f", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
	  
	   /** Array contenente tutti i possibili valori iniziali delle keyword inseribili dall'utente. */
	   private String[] initKey = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "\\", "^", "$", "€", "%", "&", "[", "!", ".", "?", ":", ";", "\\-", "_", "]"};
	  
	   private ArrayList<String> cont = new ArrayList<String>();
	   private ArrayList<String> univ = new ArrayList<String>();
	   private String[] references;
	      
	   /** Dimensione dell'ipercubo */
	   private int Hypercube;
	   
	   public NetworkPeer(String prefix){
			this.pid = Configuration.getPid(prefix + "." + PAR_PROT);	
		}

	   
	  /** Metodo principale che permette di inizializzare i valori assegnati ad ogni istanza 
	   * del protocollo ED EProtocol associato ad ogni nodo */
	@Override
	public boolean execute() {
		this.Hypercube=createBinary();
		
		DivReferences(initKey, Hypercube);
		
		for(int i=0; i< references.length; i++) {
			System.out.println("ecco posizione n " + i + ": " + references[i]);
		}
		
		rete=0;
		int checkk = 0;
		int q=0;
		int t=0;
		
		for(int i=0; i<abc123.length;i++) {
			univ.add(abc123[i]);
		}
		
		while(rete!=Network.size()) {
			
			for(int i=0; i<abc123.length;i++) {
				cont.add(abc123[i]);
			}	
			
			if(checkk>=1) {
				q+=16;
			}
			
			alfax=0;
			t=q;

			while(alfax!=abc123.length) {
				
				String add = "";
				
				if(rete == Network.size()) return false;
				
				Node node = (Node) Network.get(rete);
				EProtocol ep = ((EProtocol) node.getProtocol(this.pid));
				ep.Hyper(Hypercube);
				String bin = createBinaryID((int) node.getID());
				ep.binarID(bin);
				setNeighbors(node);
				ep.addNeigh(Neighbors);
				ep.addRef(references);
				ep.type(true);
				
				String e; 
				Random rand = new Random();
				int casuale=rand.nextInt(cont.size());
				e = cont.get(casuale);
				cont.remove(e);
				
				if(checkk==0)  add = e;

				if(checkk>0) {
					 add = univ.get(t) + e;
					 if(add.length() > 10)
					  add = add.substring(0, 10);
				}

				univ.add(add);
			
				ep.porzHash(add);
				t++;
				rete++;
				alfax++;
			
			}
			checkk++;
			
		}
				
		return false;
	}
	
	
	/** Metodo utilizzato per calcolare la lunghezza dei bit del numero dei nodi della rete */
	 public Integer createBinary() {
		 String binarID = Integer.toBinaryString(Network.size()-1);
		 return binarID.length();
	 }	
	 
	 /** ritorno la dimensione dell'ipercubo */
	 public Integer getDimenH() {
		 return Hypercube;
	 }
	 
	/** trasformo il numero del nodo nel corrispondente in binario */
		 public String createBinaryID(int n) {
			 String IDBinario = Integer.toBinaryString(n);
	/** se il codice binario del nodo ottenuto non è della lunghezza r 
	 * (dimensione dell'ipercubo) allora aggiungo gli zeri necessari */
		    while (IDBinario.length() < getDimenH()){
		    	  IDBinario = "0" + IDBinario;
		        }
			 return IDBinario;
			 
		 }
	 
	 
	 /** Questo metodo associa delle lettere ad ogni posizione i dei bit rappresentanti la 
	  * dimensione {0 <= i <= r-1} dell'ipercubo r.  */
	 public void DivReferences(String initK[], int Hyper) {
			references = new String[Hyper];
			int cont = 0;
			while(initK.length != cont) {
				
				for(int i=0; i<Hyper;i++) {
					if(cont < Hyper) {
						references[i] =initK[cont];
						cont++;
					}
					if(cont >= Hyper) {
						references[i] = references[i] + "," + initK[cont];
						cont++;
					}
					
					if(cont == initK.length) break;
				}
			}
		}
	 
		 /** Metodo per settare i vicini del nodo dato in input */
		 public ArrayList<Node> setNeighbors(Node node){
			 Neighbors = new ArrayList<Node>();

			 for(int i=0; i<Network.size();i++) {
				if(diffOneBit(createBinaryID((int) node.getID()), createBinaryID((int) Network.get(i).getID()))) {
					Neighbors.add(Network.get(i));
				}
			 }
			 return Neighbors;
		 }
		 
		 /** Metodo utilizzato per verificare se due numeri differiscono tra loro solo di un bit */
		 public boolean diffOneBit(String node, String Neighbor) {
			   int cont=0;
			
			 for (int i=0; i<node.length();i++) {
				 String nod = String.valueOf(node.charAt(i));
				 String neigh = String.valueOf(Neighbor.charAt(i));
				 if(!(nod.equals(neigh))) {
					 cont++;
				 }
			 }
			 if(cont==1) {return true;}
			 else {
			 return false;
			 }
		 }

}
