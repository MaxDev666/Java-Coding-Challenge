package de.vitbund.vitmaze.main;


import de.vitbund.vitmaze.eingabe.Eingabe;
/**
 * instanziert das Spiel
 * @author Arbeitstitel
 * @version 1.1
 *
 */
public class Mainclass {
	//Mainmethode
	public static void main(String[] args) {
		
		Spiel neuesSpiel = new Spiel();
		neuesSpiel.init();
		
		while(Eingabe.isnaechsterEintragvorhanden()) {
			neuesSpiel.getStati();
			
		}
		
		Eingabe.closeScanner();
	}

}
