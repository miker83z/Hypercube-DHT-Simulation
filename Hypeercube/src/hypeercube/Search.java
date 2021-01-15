package hypeercube;

import java.util.ArrayList;
import java.util.BitSet;

import peersim.core.Network;
import peersim.core.Node;

public class Search {
	
	public int nKey;
	public int number;
	public boolean check=false;
	public ArrayList<String> collectidBit = new ArrayList<String>();
	public int cont=0;
	public int cMex;

	/** costruttore PIN search */
	public Search(int kw, int cMex) {
		this.nKey=kw;
		this.cMex=cMex;
	}
	
	/** costruttore SUPERSET search */
	public Search(int kw, int number, int cMex) {
		this.nKey=kw;
		this.number=number;
		this.cMex=cMex;
	}
	
	public void addMex() {
		cMex++;
	}
	
	public int getNMex() {
		return this.cMex;
	}
	
	public int getKey() {
		return this.nKey;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public boolean getCheck() {
		return this.check;
	}
	
	public void setCheck() {
		this.check=true;
	}
	
	public void addCont() {
		cont++;
	}
	
	public int getCont() {
		return this.cont;
	}
	
	public void removeKey() {
		this.collectidBit.remove(0);
	}
	
	public ArrayList<String> getCollect(){
		return this.collectidBit;
	}
	
	public ArrayList<String> getListBitRefer(int kw, int hyper) {
		String idBit = createBinaryID(kw, hyper);
		BitSet bit1 = createBitset(idBit);
		for(int i=0;i<Network.size();i++) {
			Node node = (Node) Network.get(i);
			String idNet = createBinaryID((int) node.getID(), hyper);
			BitSet bit2 = createBitset(idNet);
			bit2.and(bit1);
			if(bit2.equals(bit1)) {
				collectidBit.add(idNet);
			}
		}
		
		return collectidBit;
	}
	
		/** trasformo il numero del nodo nel corrispondente in binario */
		 public String createBinaryID(int n, int hyper) {
			 String IDBinario = Integer.toBinaryString(n);
	/** se il codice binario del nodo ottenuto non è della lunghezza r (dimensione dell'ipercubo) allora aggiungo gli zeri necessari */
		    while (IDBinario.length() < hyper){
		    	  IDBinario = "0" + IDBinario;
		        }
			 return IDBinario;
		 }
	
		 /** dato in input l'id del nodo si ottiene un bitset dove si tiene traccia dei bit ad 1 */
		    private BitSet createBitset(String id){
		        return BitSet.valueOf(new long[] { Long.parseLong(id, 2) });
		    }
	
}
