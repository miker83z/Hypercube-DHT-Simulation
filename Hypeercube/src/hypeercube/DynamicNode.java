package hypeercube;

import peersim.config.Configuration;
import peersim.core.Network;
import peersim.core.Node;
import peersim.dynamics.NodeInitializer;

public class DynamicNode implements NodeInitializer{
	
	private static final String PAR_PROT = "protocol";
	
	private static final String PAR_MAXSIZE = "maxsize";

	private int pid = 0;
	
	private EProtocol ep;
	
	public String id;
	
	private int Hypercube;
	
	private int maxsize;
	

	public DynamicNode(String prefix) {
		pid = Configuration.getPid(prefix + "." + PAR_PROT);
		maxsize = Configuration.getInt(prefix + "." + PAR_MAXSIZE);
	}

	@Override
	public void initialize(Node n) {
		this.Hypercube=createBinary();
		ep = (EProtocol) n.getProtocol(pid);
		
		start(n);
		
	}
	
	/** Inizializzazione nodi "fittizi" */
	public boolean start(Node n) {
		
		ep.Hyper(Hypercube);
		String bin = createBinaryID((int) n.getID());
		ep.binarID(bin);
		ep.type(false);	

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
	/** se il codice binario del nodo ottenuto non è della lunghezza r 
	 * (dimensione dell'ipercubo) allora aggiungo gli zeri necessari */
		    while (IDBinario.length() < getDimenH()){
		    	  IDBinario = "0" + IDBinario;
		        }
			 return IDBinario;
			 
		 }

}
