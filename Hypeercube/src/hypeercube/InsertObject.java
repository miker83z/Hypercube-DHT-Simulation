package hypeercube;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class InsertObject {
	
	public String object;
	public String Hashtext;
	
	public InsertObject (String object) {
		this.object = object;
	}
	
	public String getObject() {
		return this.object;
	}
	
	public String getHash() {
		return this.Hashtext;
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
