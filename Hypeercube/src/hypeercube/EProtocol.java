package hypeercube;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import peersim.config.Configuration;
import peersim.core.Node;
import peersim.edsim.EDProtocol;

public class EProtocol implements EDProtocol{
	
	private final String PAR_TRANSPORT = "transport";	
	
	public String prefix;
	
	/** stringa id del nodo, composta dal suo codice binario */
	public String id; 
	
	/** bitset del nodo, in particolare tiene traccia dei bit a 1 */
	private BitSet bitset;
	
	private Parameters p;
	
	/** dimensione dell'ipercubo */
	public int hyper;
	
	/** tiene traccia del fatto che il nodo possa essere reale o creato per tenere la dimensione corretta dell'ipercubo */
	public boolean type;
	
	/** tengo traccia dei vicini del nodo*/
	public ArrayList<Node> Neighbors;
	
	/** gli oggetti mantenuti dal nodo sono salvati in questo hashmap */
	private HashMap<String, String> objects = new HashMap<String, String>();
	
	private HashSet<String> KeyHash = new HashSet<String>();
	
	/** tiene traccia degli oggetti richiesti dall'utente e dopo ogni richiesta viene svuotato. Una specie di memoria temporanea */
	public ArrayList<String> listOb = new ArrayList<String>();
	
	public ArrayList<String> listObHash = new ArrayList<String>();
	
	public ArrayList<String> listidRef;
	
	public String keyword;
	
	public String hashObject;
	
	public boolean check;
	
	private BitSet goal = new BitSet();
	
	private Node n;
	
	public int nMex;
	
	public EProtocol(String prefix) { 
		this.prefix = prefix;
		p = new Parameters();
		p.tid = Configuration.getPid(prefix + "." + PAR_TRANSPORT);
	}
	
	public void Hyper(int h) {
		 hyper = h;
	}
	
	public int getHyper() {
		return this.hyper;
	}
	
	public void binarID(String b) {
		this.id=b;
	}
	
	public String getBinary() {
		return this.id;
	}
	
	public BitSet getOne() {
		return this.bitset;
	}
	
	public void type(boolean t) {
		this.type = t;
	}
	
	public boolean getType() {
		return this.type;
	}
	
	public int getContMex() {
		return this.nMex;
	}
	
	public void removeCont() {
		this.nMex=0;
	}
	
	public void addNeigh(ArrayList<Node> neigh) {
		this.Neighbors = neigh;
	}
	
    public ArrayList<Node> getNeighbors(){
        return this.Neighbors;
    }
    
    public HashMap<String,String> getObjects(){
		return this.objects;
	}
	
    public HashSet<String> getKeyHash(){
    	return this.KeyHash;
    }
	
	public ArrayList<String> getListObHash(){
		return this.listObHash;
	}
	
	public void removeListObHash() {
		this.listObHash.clear();
	}

	@Override
	public void processEvent(Node node, int pid, Object event) {
		p.pid = pid;
		
		/** Inserimento keyword e hash dell'oggetto */
		if(event.getClass() == Insert.class) {
			Insert insert = (Insert) event;
			EProtocol ep = ((EProtocol) node.getProtocol(p.pid));
			keyword = insert.getKeyword();
			hashObject = insert.getHashObj();
			boolean check = checkFind(keyword, ep.getBinary());
			System.out.println("ecco bitId della parola chiave: " + keyword);
			BitSet s1 = createBitset(keyword);
			System.out.println("ecco bitset della parola chiave: " + s1);
			System.out.println("ecco bitId del nodo: " + ep.getBinary());
			bitset = createBitset(ep.getBinary());
			System.out.println("ecco bitset del nodo: " + bitset);
			System.out.println("sono uguali? : " + check);
			
			if(check==true){
				KeyHash.add(hashObject);
				System.out.println("Keywords inserite!");
			}else {
				BitSet escape = getMax();
				int ix=0;
				boolean other = true;
				goal = bitset;
				for(int i=0;i<Neighbors.size();i++) {
					System.out.println("ecco i vicini del nodo: " + createBinaryID((int) Neighbors.get(i).getID()));
					BitSet nt = createBitset(createBinaryID((int) Neighbors.get(i).getID()));
					System.out.println("ecco bitset del nodo vicino: " + nt);
					/** calcolo quale dei vicini del nodo ha distanza di hamming minore rispetto al target */
		
					BitSet target = xor(s1,nt);
					
					if(target.cardinality() <= escape.cardinality()) {
						escape = target;
						ix=i;
					}

					if(target.cardinality() <= goal.cardinality()) {
						goal = target;
						n = (Node) Neighbors.get(i);
						other = false;
					}
					
					if(other==true) {
						n = (Node) Neighbors.get(ix);
					}
					System.out.println("ecco il bitset dopo lo xor: " + target.cardinality() + "   " + target);
					System.out.println("####################");
				}
				System.out.println("ecco il bitset finale: " + goal.cardinality() + "   " + goal);
				EProtocol ep1 = ((EProtocol) n.getProtocol(p.pid));
				ep1.processEvent(n, p.pid, insert);
			}
		}
	
		
		/** Ricerca all'interno dell'ipercubo */
		if(event.getClass() == Search.class) {
			Search search = (Search) event;
			EProtocol ep = ((EProtocol) node.getProtocol(p.pid));
						
			if(search.getNumber()!=0) {
				boolean control = search.getCheck();
				if(control==false) {
					int kw = search.getKey();
					listidRef = search.getListBitRefer(kw, hyper);
					 search.setCheck();
				}else if(control==true) {
					 listidRef = search.getCollect();
				}
				if(listidRef.size()!=0) {
					for(int i=0; i<listidRef.size();i++) {
						System.out.println("nodi da visitare: " +listidRef.get(i));
					}
					check = checkFind(listidRef.get(0), ep.getBinary());
					System.out.println("ecco bitId della parola chiave: " + listidRef.get(0));
					BitSet bt = createBitset(listidRef.get(0));
					System.out.println("ecco bitset della parola chiave: " + bt);
					System.out.println("ecco bitId del nodo: " + ep.getBinary());
					bitset = createBitset(ep.getBinary());
					System.out.println("ecco bitset del nodo: " + bitset);
					System.out.println("sono uguali? : " + check);
					goal = bitset;
					
					 if(search.getNumber() > search.getCont()) {
						 SuperSet(ep, node, p.pid, search, bt);
					 }
					 
				}
			}else {
				int kw = search.getKey();
				String idRef = createBinaryID(kw);
				check = checkFind(idRef, ep.getBinary());
				System.out.println("ecco bitId della parola chiave: " + idRef);
				BitSet bt = createBitset(idRef);
				System.out.println("ecco bitset della parola chiave: " + bt);
				System.out.println("ecco bitId del nodo: " + ep.getBinary());
				bitset = createBitset(ep.getBinary());
				System.out.println("ecco bitset del nodo: " + bitset);
				System.out.println("sono uguali? : " + check);
				goal = bitset;
				
					
				    PinSearch(ep, node, p.pid, search, bt);
			}
			
		}
	
	}
	
	/** Ricerca PIN search */
	 public void PinSearch(EProtocol ep, Node node, int pid, Search search, BitSet bt) {
		 	nMex = search.getNMex();
		 	
	    	if(check==true){
	    		System.out.println("Ecco il numero degli scambi: " + nMex);
				if(!(KeyHash.isEmpty())) {
					System.out.println("possiamo ritirare!");
					Iterator<String> kwt = KeyHash.iterator(); 
			        while (kwt.hasNext()) {
			        	String value = kwt.next();
			        	listObHash.add(value);
			        }
				}
			}else {
				search.addMex();
				checkNeighbors(pid, search, bt);
			}
	    }
	 
	 
	 public void SuperSet(EProtocol ep, Node node, int pid, Search search, BitSet bt) {
	    	if(check==true){		
				if(!(KeyHash.isEmpty())) {	
					System.out.println("possiamo ritirare!");
					Iterator<String> kwt = KeyHash.iterator(); 
			        while (kwt.hasNext()) {
			        	String value = kwt.next();
			        	/** aggiungiamo la lista degli hash associati agli oggetti */
					     if(search.getNumber() > search.getCont()) {
					    	 listObHash.add(value);
			        	  	 search.addCont();
					     }
			        }
				
				search.removeKey();
				if(search.getCollect().size()!=0) {
					checkNeighborsWithList(pid, search, bt);
				}
				
				}else {
					search.removeKey();
					if(search.getCollect().size()!=0) {
						checkNeighborsWithList(pid, search, bt);
					}
				}
				
			}else {
				checkNeighbors(pid, search, bt);
			}
	 }
	 
	 
	 public void checkNeighbors(int pid, Search search, BitSet bt) {
		 boolean other = true;
			BitSet escape = getMax();
			int ix=0;
			
			for(int i=0;i<Neighbors.size();i++) {
				System.out.println("ecco i vicini del nodo: " + createBinaryID((int) Neighbors.get(i).getID()));
				BitSet nt = createBitset(createBinaryID((int) Neighbors.get(i).getID()));
				System.out.println("ecco bitset del nodo vicino: " + nt);
				/** calcolo quale dei vicini del nodo ha distanza di hamming minore rispetto al target */
				BitSet target = xor(bt,nt);
				
				if(target.cardinality() <= escape.cardinality()) {
					escape = target;
					ix=i;
				}
				
				if(target.cardinality() <= goal.cardinality()) {
					goal = target;
					n = (Node) Neighbors.get(i);
					other = false;
				}
				
				if(other==true) {
					n = (Node) Neighbors.get(ix);
				}
				System.out.println("ecco il bitset dopo lo xor: " + target.cardinality() + "   " + target);
				System.out.println("####################");
			}
			System.out.println("ecco il bitset finale: " + goal.cardinality() + "   " + goal);
			EProtocol ep1 = ((EProtocol) n.getProtocol(pid));
			ep1.processEvent(n, pid, search);
	 }
	 
	 
	 public void checkNeighborsWithList(int pid, Search search, BitSet bt) {
		 
		 boolean other = true;	
			BitSet escape = getMax();
			int ix=0;
			
			for(int i=0;i<Neighbors.size();i++) {
				System.out.println("ecco i vicini del nodo: " + createBinaryID((int) Neighbors.get(i).getID()));
				BitSet nt = createBitset(createBinaryID((int) Neighbors.get(i).getID()));
				System.out.println("ecco bitset del nodo vicino: " + nt);
				listidRef = search.getCollect();
				BitSet s3 = createBitset(listidRef.get(0));
				
				/** calcolo quale dei vicini del nodo ha distanza di hamming minore rispetto al target */
				BitSet target = xor(s3,nt);
				
				if(target.cardinality() <= escape.cardinality()) {
					escape = target;
					ix=i;
				}
				
				if(target.cardinality() <= goal.cardinality()) {
					goal = target;
					n = (Node) Neighbors.get(i);
					other = false;
				}
				
				if(other==true) {
					n = (Node) Neighbors.get(ix);
				}
				System.out.println("ecco il bitset dopo lo xor: " + target.cardinality() + "   " + target);
				System.out.println("####################");
			}
			System.out.println("ecco il bitset finale: " + goal.cardinality() + "   " + goal);

			EProtocol ep1 = ((EProtocol) n.getProtocol(pid));
			ep1.processEvent(n, pid, search);	
		 
	 }
	 
	    
	
	/** trasformo il numero del nodo nel corrispondente in binario */
		 public String createBinaryID(int n) {
			 String IDBinario = Integer.toBinaryString(n);
	/** se il codice binario del nodo ottenuto non è della lunghezza r (dimensione dell'ipercubo) allora aggiungo gli zeri necessari */
		    while (IDBinario.length() < getHyper()){
		    	  IDBinario = "0" + IDBinario;
		        }
			 return IDBinario;
		 }
	
		
		 /** distanza di hamming calcolata dopo un'operazione di xor */	 
		 private BitSet xor(BitSet bsTarget, BitSet bsNeigh){
		        BitSet bs1 = new BitSet();
		        BitSet bsXor = new BitSet();
		        bs1.or(bsTarget);
		        bsXor.or(bsNeigh);
		        bsXor.xor(bs1);
		        return bsXor;
		    }
		 
		 
		/** dato in input l'id del nodo si ottiene un bitset dove si tiene traccia dei bit ad 1 */
		    private BitSet createBitset(String id){
		        return BitSet.valueOf(new long[] { Long.parseLong(id, 2) });
		    }
		    
		    
		    private boolean checkFind(String idRef, String id) {
		    	if(idRef.equals(id)) return true;
		    	return false;
		    }
		    
		    
		  /** metodo per controllare se il primo bitset è contenuto nel secondo */
		    public boolean isIncluded(BitSet bitSet1, BitSet bitSet2){
		        BitSet includedBitSet = new BitSet();
		        BitSet bs1Temp = new BitSet();
		        BitSet bs2Temp = new BitSet();
		        includedBitSet.or(bitSet1); 
		        bs1Temp.or(bitSet1);     
		        bs2Temp.or(bitSet2);	      
		        bs1Temp.and(bs2Temp);	    
		        if ( bs1Temp.equals(includedBitSet)) {
		            return true;
		        }
		        return false;
		    }
		    
		  
		  /** restituiscono i vicini del nodo i cui bitset includono il nodo stesso
		    tra tutti i vicini prendo solo quelli che soddisfano la condizione isIncluded */
		    public ArrayList<Node> getNeighborsIncluded(){
		        ArrayList<Node> neighborsIncluded = new ArrayList<Node>();
		        for (Node neighbor : this.getNeighbors()) {
		        	BitSet bit = createBitset(createBinaryID((int) neighbor.getID()));
		            if (isIncluded(this.getOne(), bit)) {
		                neighborsIncluded.add(neighbor);
		            }
		        }
		        return neighborsIncluded;
		    }
		    
		  
		    public BitSet getMax() {
		    	BitSet bit = new BitSet();
		    	for (int i = 0; i < getHyper(); i++) {
		    	    bit.set(i);
		    	}
		    	return bit;
		    }
		 
	
		@Override
		public Object clone(){
			EProtocol Ep = new EProtocol(prefix);
			
			return Ep;
		}

}
