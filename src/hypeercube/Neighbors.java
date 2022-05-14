package hypeercube;

import java.util.ArrayList;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class Neighbors implements Control{
	
	 
	/** Stringa per recuperare il nome del protocollo dal file di conf */
	  private static final String PAR_PROT = "protocol";
	 /** Pid del protocollo */
	  private int pid;
		    
	  private ArrayList<Node> Neighbors;
		   
    /** Dimensione dell'ipercubo */
	  private int Hypercube;
	
	  public Neighbors(String prefix) {
		   this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
	  }


		   @Override
		   public boolean execute() {
			   
			   Hypercube = createBinary();
			   
			for(int j=0;j<Network.size();j++) {
				Node node = (Node) Network.get(j);
				EProtocol ep = ((EProtocol) node.getProtocol(this.pid));
				setNeighbors(node);
				ep.addNeigh(Neighbors);
				
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
