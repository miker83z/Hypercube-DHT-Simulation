package hypeercube;

import java.util.ArrayList;

public class Obj {
	
	public ArrayList<String> objHash;
	public ArrayList<String> obj;
	public int number;
	public Search search;
	
	/** costruttore PIN search */
		public Obj(ArrayList<String> objH) {
			this.objHash = objH;
			this.obj = new ArrayList<String>();
		}

		/** costruttore SUPERSET search */
		public Obj(ArrayList<String> objH, int number, Search search) {
			this.objHash = objH;
			this.obj = new ArrayList<String>();
			this.number=number;
			this.search=search;
		}
		
		
		public ArrayList<String> getObjHash(){
			return this.objHash;
		}
		
		public void remove() {
			this.objHash.remove(0);
		}
		
		public ArrayList<String> getObj(){
			return this.obj;
		}
		
		public int getNumber() {
			return this.number;
		}
		
		public Search getSearch() {
			return this.search;
		}

}
