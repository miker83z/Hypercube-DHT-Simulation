package hypeercube;

import java.util.ArrayList;

public class Search {
	
	public String[] refKey;
	public int number;
	public ArrayList<String> collectidBit = new ArrayList<String>();
	public boolean check=false;
	public int cont=0;
	
	/** costruttore PIN search */
	public Search(String[] ref) {
		this.refKey=ref;
	}
	
	/** costruttore SUPERSET search */
	public Search(String[] ref, int number) {
		this.refKey=ref;
		this.number=number;
	}
	
	public String[] getKey() {
		return this.refKey;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public void removeKey() {
		this.collectidBit.remove(0);
	}
	
	public void addCont() {
		cont++;
	}
	
	public int getCont() {
		return this.cont;
	}
	
	public boolean getCheck() {
		return this.check;
	}
	
	public void setCheck() {
		this.check=true;
	}
	
	public ArrayList<String> getCollect(){
		return this.collectidBit;
	}
	
	/** Metodo per stabilire l'id binario delle keyword inserite dall'utente in base alla divisione
	 *  delle lettere con la dimensione dell'ipercubo */
			public String getBitRefer(String[] ref) {
				String idBit = "";
				boolean check=false;
				for(int i=0;i<ref.length;i++) {
					check=false;
					for(int j=0;j<refKey.length;j++) {
						if(ref[i].contains(refKey[j])) {
							idBit = idBit+ "1";
							check=true;
							break;
						}
					}
					if(check==false) idBit = idBit + "0";
				}
				return idBit;
			}
			
			
			public ArrayList<String> getListBitRefer(String[] ref) {
				
				String idBit = getBitRefer(ref);
				collectidBit.add(idBit);
				ArrayList<String> Bit = new ArrayList<String>();
				for(int i=0; i<refKey.length;i++) {
					Bit.add(refKey[i]);
				}
				
				while(!(Bit.isEmpty())) {
					String init = Bit.get(0);
					idBit = Refer(ref, init);
					if(!(collectidBit.contains(idBit))) {
						collectidBit.add(idBit);
					}
					Bit.remove(0);
				}
				return collectidBit;
			}
			
			public String Refer(String[] ref, String init) {
				String idBit = "";
				boolean check=false;
				for(int i=0;i<ref.length;i++) {
					check=false;	
						if(ref[i].contains(init)) {
							idBit = idBit+ "1";
							check=true;
						}
					if(check==false) idBit = idBit + "0";
				}
				return idBit;
			}

}
