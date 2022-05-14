package hypeercube;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class InsertKeyword {
	
	public String[] references;
	public String keyword;
	public String object;
	public String Hashtext;
	
	public InsertKeyword (String[] references, String keyword, String object) {
		this.references=references;
		this.keyword=keyword;
		this.object=object;
	}
	
	public String[] getReferences() {
		return this.references;
	}
	
	public String getKeyword() {
		return this.keyword;
	}
	
	public String getObject() {
		return this.object;
	}
	
	public String getHash() {
		return this.Hashtext;
	}
	
	/** Metodo per stabilire l'id binario delle keyword inserite dall'utente in base alla divisione
	 *  delle lettere con la dimensione dell'ipercubo */
	public String getBitRefer(String[] ref) {
		String idBit = "";
		boolean check=false;
		for(int i=0;i<ref.length;i++) {
			check=false;
			for(int j=0;j<references.length;j++) {
				if(ref[i].contains(references[j])) {
					idBit = idBit+ "1";
					check=true;
					break;
				}
			}
			if(check==false) idBit = idBit + "0";
		}
		return idBit;
	}
	
	/** Trasforma una stringa nel corrispettivo in hash Sha256 */
	public void Sha256(String object){
		try {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		
		byte[] hash = digest.digest(object.getBytes(StandardCharsets.UTF_8));

        BigInteger convert = new BigInteger(1, hash); 

        Hashtext = convert.toString(16); 
        while (Hashtext.length() < 32) { 
            Hashtext = "0" + Hashtext; 
        } 
		
		}catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        } 
	}

}
