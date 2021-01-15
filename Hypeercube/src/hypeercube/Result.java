package hypeercube;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Result {

	public String filePath = "result.csv";
	
	public Result() {}


	
	public void printResult(ArrayList<String> exchange) {

		try {
			
		FileWriter fw = new FileWriter(filePath, true);
		BufferedWriter bf = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bf);
		
		
		for(int i=0; i<exchange.size();i++) {
			
			if(i!=exchange.size()-1)
			pw.print(exchange.get(i) + ",");
			
			if(i==exchange.size()-1)
				pw.print(exchange.get(i) + "\n");
			
		}
		
		pw.flush();
		pw.close();
		
		System.out.println("Dati inseriti!");
		}catch(Exception e) {
			System.out.println("Dati non inseriti! " + "Errore: " + e);
		}
		
	}
	
	
	
}
