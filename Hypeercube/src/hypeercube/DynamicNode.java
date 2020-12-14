package hypeercube;

import java.util.ArrayList;
import java.util.Random;

import peersim.config.Configuration;
import peersim.core.Network;
import peersim.core.Node;
import peersim.dynamics.NodeInitializer;

public class DynamicNode implements NodeInitializer{
	
	private String PAR_PROT = "protocol";

	private int pid = 0;
	
	private EProtocol ep;
	
	public String id;
	
	private int Hypercube;
	
	private static ArrayList<Node> Neighbors;

	private String[] abc123 = {"a", "b", "c", "d", "e", "f", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
	
	private String[] ArrayRef = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "\\", "^", "$", "€", "%", "&", "[", "!", ".", "?", ":", ";", "\\-", "_", "]"};
	
	private ArrayList<String> cont = new ArrayList<String>();
	  
	private String[] references;
	
	public DynamicNode(String prefix) {
		pid = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	@Override
	public void initialize(Node n) {
		this.Hypercube=createBinary();
		ep = (EProtocol) n.getProtocol(pid);
		
		start(n);
		
	}
	
	/** Inizializzazione nodi "fittizi" */
	public boolean start(Node n) {
		int index = (int) n.getID();
		Node nodePre = (Node) Network.get(index-1);
		EProtocol epPre = (EProtocol) nodePre.getProtocol(pid);
		
		String hasher = epPre.getHash();
		DivReferences(ArrayRef, Hypercube);

		String add = "";

		ep.Hyper(Hypercube);
		String bin = createBinaryID((int) n.getID());
		ep.binarID(bin);
		setNeighbors(n);
		ep.addNeigh(Neighbors);
		ep.addRef(references);
		ep.type(false);	
				
		String e; 
		Random rand = new Random();
		int conto=0;
		
		while(conto!=hasher.length()) {
			if(cont.isEmpty()) {
				for(int i=0; i<abc123.length;i++) {
					cont.add(abc123[i]);
					}
				}
						
			int casuale=rand.nextInt(cont.size());
			e = cont.get(casuale);
			cont.remove(e);	
			 add += e;
			conto++;
		}

		 if(add.length() > 10)
		  add = add.substring(0, 10);
			
		ep.porzHash(add);

		return false;
		
	}
	
	/** Metodo utilizzato per calcolare la lunghezza dei bit del numero dei nodi della rete */
	 public Integer createBinary() {
		 String binarID = Integer.toBinaryString(Network.size()-1);
		 return binarID.length();
	 }	
	 
	 /** Questo metodo associa delle lettere ad ogni posizione i dei bit rappresentanti la 
	  * dimensione {0 <= i <= r-1} dell'ipercubo r.  */
	 public void DivReferences(String arr[], int Hyper) {
			references = new String[Hyper];
			int cont = 0;
			while(arr.length != cont) {
				
				for(int i=0; i<Hyper;i++) {
					if(cont < Hyper) {
						references[i] =arr[cont];
						cont++;
					}
					if(cont >= Hyper) {
						references[i] = references[i] + "," + arr[cont];
						cont++;
					}
					
					if(cont == arr.length) break;
				}
			}
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
