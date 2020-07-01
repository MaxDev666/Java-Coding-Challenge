package de.vitbund.vitmaze.main;


import de.vitbund.vitmaze.eingabe.Eingabe;

public class Mainclass {
	
	public static void main(String[] args) {
		
		Spiel neuesSpiel = new Spiel();
		neuesSpiel.init();
		
		while(Eingabe.isnaechsterEintragvorhanden()) {
			neuesSpiel.getStati();
			
		}
		
		Eingabe.closeScanner();
	}

}
