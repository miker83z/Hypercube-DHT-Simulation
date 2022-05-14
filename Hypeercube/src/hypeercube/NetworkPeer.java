package hypeercube;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class NetworkPeer implements Control{
	
	 
	/** Stringa per recuperare il nome del protocollo dal file di conf */
	   private static final String PAR_PROT = "protocol";
	   
	   /** Stringa per recuperare il numero massimo di nodi della rete dal file di conf */
	   private static final String PAR_MAXSIZE = "maxsize";
	   
	   /** Pid del protocollo */
	   private int pid;
	   
	   private int maxsize;
	   
	   /** Dimensione dell'ipercubo */
	   private int Hypercube;
	   
	   
	   public NetworkPeer(String prefix) {
		   this.pid = Configuration.getPid(prefix + "." + PAR_PROT);
		   this.maxsize = Configuration.getInt(prefix + "." + PAR_MAXSIZE);
		}


	  /** Metodo principale che permette di inizializzare i valori assegnati ad ogni istanza 
	   * del protocollo ED EProtocol associato ad ogni nodo */
	@Override
	public boolean execute() {

		this.Hypercube=createBinary();
	
		for(int j=0;j<Network.size();j++) {
			Node node = (Node) Network.get(j);
			EProtocol ep = ((EProtocol) node.getProtocol(this.pid));
			ep.Hyper(Hypercube);
			String bin = createBinaryID((int) node.getID());
			ep.binarID(bin);
			ep.type(true);
		}
		
		return false;
	}
	
	
	/** Metodo utilizzato per calcolare la lunghezza dei bit del numero dei nodi della rete */
	 public Integer createBinary() {
		 String binarID = Integer.toBinaryString(maxsize-1);
		 return binarID.length();
	 }	
	 
	 /** ritorno la dimensione dell'ipercubo */
	 public Integer getDimenH() {
		 return Hypercube;
	 }
	 
	/** trasformo il numero del nodo nel corrispondente in binario */
		 public String createBinaryID(int n) {
			 String IDBinario = Integer.toBinaryString(n);
	/** se il codice binario del nodo ottenuto non � della lunghezza r 
	 * (dimensione dell'ipercubo) allora aggiungo gli zeri necessari */
		    while (IDBinario.length() < getDimenH()){
		    	  IDBinario = "0" + IDBinario;
		        }
			 return IDBinario;
			 
		 }
		 
}
