package de.vitbund.vitmaze.spielfeld;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class Spielfeld {

	private int sizeX;
	private int sizeY;
	private int level;
	private List<Feld> felder = new ArrayList<Feld>();
	
	// interessante Felder merken
	private List<Feld> interessanteFelder = new ArrayList<Feld>();
	
	// noch zu erkundende Felder merken
	private List<Feld> unbekannteFelder = new ArrayList<Feld>();
	
	public int getSizeX() {
		return sizeX;
	}
	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}
	public int getSizeY() {
		return sizeY;
	}
	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	public void addFeld(Feld newFeld) {
		felder.add(newFeld);
	}
	
	public void addInteressantesFeld(Feld newFeld) {
		interessanteFelder.add(newFeld);
	}
	
	public void addUnbekanntesFeld(Feld newFeld) {
		unbekannteFelder.add(newFeld);
	}
	
	/* Bitte noch ignorieren
	
	public List<String> findeWeg(Feld aktuellesFeld, Feld zielFeld) {
		for (Feld t : felder) {
			t.setVorgaenger(null);
			t.setLinks(null);
			t.setRechts(null);
			t.setMitte(null);
		}
		
		aktuellesFeld.setVorgaenger(aktuellesFeld);
		List<Feld> enden = new ArrayList<Feld>();
		
		Stack<Feld> nochpruefen = new Stack<Feld>();
		
		nochpruefen.add(aktuellesFeld);
		
		while (!nochpruefen.empty()) {
			
			aktuellesFeld = new Feld();
			aktuellesFeld = nochpruefen.pop();
			
			for (Feld bla : aktuellesFeld.getNachbarnOhne(aktuellesFeld.getVorgaenger())) {
				
				if (bla == zielFeld) {
					enden.add(aktuellesFeld);
					break;
				}
				
				bla.setVorgaenger(aktuellesFeld);
				bla.setIstInBearbeitung(true);
				

				nochpruefen.add(bla);
			}
			
		}
		Feld sieger = new Feld();
		int tiefe = 100000;
		for (Feld bla : enden) {
			if (bla.tiefe()<tiefe) {
			 sieger = bla;	
			}
		}
		
		return gibWeg(sieger);
	}
	
	public List<String> gibWeg(Feld feld){
		List<String> weg = new ArrayList<String>();
		
		while (feld.getVorgaenger()!=feld) {
			if (feld.getVorgaenger().getEast()==feld) {weg.add("go east");}
			if (feld.getVorgaenger().getWest()==feld) {weg.add("go west");}
			if (feld.getVorgaenger().getNorth()==feld) {weg.add("go north");}
			if (feld.getVorgaenger().getSouth()==feld) {weg.add("go south");}
			feld = feld.getVorgaenger();
		}
		return weg;
	}
	
	 */
	
	
}
