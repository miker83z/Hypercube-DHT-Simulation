package hypeercube;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

import peersim.config.Configuration;
import peersim.core.Network;
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
	
	/** porzione di hash assegnato al nodo. Il nodo gestirà oggetti con porzione di hash corrispondente a quella assegnata al nodo */
	public String pHash;
	
	/** dimensione dell'ipercubo */
	public int hyper;
	
	/** tiene traccia del fatto che il nodo possa essere reale o creato per tenere la dimensione corretta dell'ipercubo */
	public boolean type;
	
	/** riferimento alla divisione delle lettere in base alla dimensione r dell'ipercubo */
	public String[] ref;
	
	/** tengo traccia dei vicini del nodo*/
	public ArrayList<Node> Neighbors;
	
	/** gli oggetti mantenuti dal nodo sono salvati in questo hashmap */
	private HashMap<String, String> objects = new HashMap<String, String>();
	
	/** le keyword associate al nodo sono salvate in questo hashmap */
	private HashMap<String, String> RefKey = new HashMap<String, String>();
	
	/** tiene traccia degli oggetti richiesti dall'utente e dopo ogni richiesta viene svuotato. Una specie di memoria temporanea */
	public ArrayList<String> listOb = new ArrayList<String>();
	
	public ArrayList<String> listObHash = new ArrayList<String>();
	
	public ArrayList<String> listidRef;
	
	public String keyword;
	
	public String Object;
	
	public boolean check;
	
	private BitSet goal = new BitSet();
	
	private Node n;
	
	public EProtocol(String prefix) { 
		this.prefix = prefix;
		p = new Parameters();
		p.tid = Configuration.getPid(prefix + "." + PAR_TRANSPORT);
	}
	
	public void porzHash(String s) {
		pHash = s;
	}
	
	public void Hyper(int h) {
		 hyper = h;
	}
	
	public String getHash() {
		return this.pHash;
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
	
	public void addRef(String[] ref) {
		this.ref=ref;
	}
	
	public String[] getRef() {
		return this.ref;
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
	
	public HashMap<String,String> getListKey(){
		return this.RefKey;
	}
	
	public ArrayList<String> getListOb(){
		return this.listOb;
	}
	
	public void removeListOb() {
		this.listOb.clear();
	}

	@Override
	public void processEvent(Node node, int pid, Object event) {
		p.pid = pid;
		/** Inserimento oggetto */
		if (event.getClass() == InsertObject.class) {
			InsertObject obj = (InsertObject) event;
			System.out.println("indice del nodo: " + node.getIndex());
			obj.Sha256(obj.getObject());
			String hash = obj.getHash().substring(0, 1);
			System.out.println("ecco l'Hash completo: " + obj.getHash());
			System.out.println("ecco iniziale hash: " + hash);
			EProtocol ep = ((EProtocol) node.getProtocol(p.pid));
			System.out.println("hash del nodo: " + ep.getHash());
			System.out.println("#############");
			if(ep.getHash().equals(hash)) {
				objects.put(obj.getHash(), obj.getObject());
				System.out.println("Oggetto inserito!");
			}else {
				int index = node.getIndex() + 1;
				if(index >= Network.size()) index = 0;
				Node node1 = (Node) Network.get(index);
				EProtocol ep1 = ((EProtocol) node1.getProtocol(p.pid));
				ep1.processEvent(node1, p.pid, obj);
			}
		}
		/** Inserimento keyword */
		if(event.getClass() == InsertKeyword.class) {
			InsertKeyword kw = (InsertKeyword) event;
			EProtocol ep = ((EProtocol) node.getProtocol(p.pid));
			keyword = kw.getKeyword();
			String idRef = kw.getBitRefer(ref);
			boolean check = checkFind(idRef, ep.getBinary());
			System.out.println("ecco bitId della parola chiave: " + idRef);
			BitSet s1 = createBitset(idRef);
			System.out.println("ecco bitset della parola chiave: " + s1);
			System.out.println("ecco bitId del nodo: " + ep.getBinary());
			bitset = createBitset(ep.getBinary());
			System.out.println("ecco bitset del nodo: " + bitset);
			System.out.println("sono uguali? : " + check);

			if(check==true){
				kw.Sha256(kw.getObject());
				Object = kw.getHash();
				if(!(RefKey.containsKey(keyword))) 
					RefKey.put(keyword, Object);
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
				ep1.processEvent(n, p.pid, kw);
			}
			
		}
		
		/** Ricerca all'interno dell'ipercubo */
		if(event.getClass() == Search.class) {
			Search search = (Search) event;
			EProtocol ep = ((EProtocol) node.getProtocol(p.pid));
						
			if(search.getNumber()!=0) {
				boolean control = search.getCheck();
				if(control==false) {
					 listidRef = search.getListBitRefer(ref);
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
					
					SuperSet(ep, node, p.pid, search, bt);
				}
			}else {
				String idRef = search.getBitRefer(ref);
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
		/** Una volta ottenuta la lista degli hash corrispondenti a degli oggetti*/
		if(event.getClass() == Obj.class) {
			Obj obj = (Obj) event;
			EProtocol ep = ((EProtocol) node.getProtocol(p.pid));
			listObHash = obj.getObjHash();
			String element="";
			if(!(listObHash.isEmpty())){
				 element = (listObHash.get(0)).substring(0, 1);
				boolean check = checkFind(element, ep.getHash());
				
				if(check==true) {
					listOb.add(objects.get(listObHash.get(0)));
					obj.remove();
					
					if(!(listObHash.isEmpty())) {
						int index = node.getIndex() + 1;
						if(index >= Network.size()) index = 0;
						 n = (Node) Network.get(index);
						EProtocol ep1 = ((EProtocol) n.getProtocol(p.pid));
						ep1.processEvent(n, p.pid, obj);
					}
					if(listObHash.isEmpty() && obj.getNumber()!=0) {
						int index = node.getIndex() + 1;
						if(index >= Network.size()) index = 0;
						 n = (Node) Network.get(index);
						EProtocol ep1 = ((EProtocol) n.getProtocol(p.pid));
						Search s = obj.getSearch();
						ep1.processEvent(n, p.pid, s);
					}
					
				}else {
					int index = node.getIndex() + 1;
					if(index >= Network.size()) index = 0;
					 n = (Node) Network.get(index);
					EProtocol ep1 = ((EProtocol) n.getProtocol(p.pid));
					ep1.processEvent(n, p.pid, obj);
				}
			}
		}
		
	}
	
	/** Ricerca PIN search */
	 public void PinSearch(EProtocol ep, Node node, int pid, Search search, BitSet bt) {
	    	if(check==true){		
				if(!(RefKey.isEmpty())) {
					System.out.println("possiamo ritirare!");
				for (HashMap.Entry<String,String> entry : RefKey.entrySet()) {
				     String value = entry.getValue();
				     listObHash.add(value);
				     System.out.println(value);
				}
				
				Obj ob = new Obj(listObHash);
				ep.processEvent(node, pid, ob);
				
				}
			}else {
				BitSet escape = getMax();
				int ix=0;
				boolean other = true;
		
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
	    }
	 
	 
	 public void SuperSet(EProtocol ep, Node node, int pid, Search search, BitSet bt) {
	    	if(check==true){		
				if(!(RefKey.isEmpty())) {	
					System.out.println("possiamo ritirare!");
				for (HashMap.Entry<String,String> entry : RefKey.entrySet()) {
				     String value = entry.getValue();
				     
				     /** aggiungiamo la lista degli hash associati agli oggetti */
				     if(search.getNumber() > search.getCont()) {
				    	 listObHash.add(value);
				    	 search.addCont();
				     }
				     System.out.println(value);
				}
				
				Obj ob = new Obj(listObHash, search.getNumber(), search);
				search.removeKey();
				ep.processEvent(node, pid, ob);
				
				}else {
					search.removeKey();
					if(search.getCollect().size()!=0) {
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
				}
				
			}else {
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
