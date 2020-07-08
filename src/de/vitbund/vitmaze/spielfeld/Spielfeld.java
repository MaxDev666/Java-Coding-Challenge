package de.vitbund.vitmaze.spielfeld;

import java.util.ArrayList;
import java.util.List;

public class Spielfeld {

	private int sizeX;
	private int sizeY;
	private int level;
	private List<Feld> felder = new ArrayList<Feld>();
	
	// interessante Felder merken
	private List<Feld> interessanteFelder = new ArrayList<Feld>();
	
	// Felder mit Formularen merken
	private List<Feld> formularFelder = new ArrayList<Feld>();
	
	// Felder mit Ziel merken
	private List<Feld> Zielfeld = new ArrayList<Feld>();
	
	// noch zu erkundende Felder merken
	private List<Feld> unbekannteFelder = new ArrayList<Feld>();

	// bekannte Felder merken
	private List<Feld> bekannteFelder = new ArrayList<Feld>();
	
	public List<Feld> getBekannteFelder() {
		return bekannteFelder;
	}
	public void setBekannteFelder(List<Feld> bekannteFelder) {
		this.bekannteFelder = bekannteFelder;
	}
	public List<Feld> getUnbekannteFelder() {
		return unbekannteFelder;
	}
	public void setUnbekannteFelder(List<Feld> unbekannteFelder) {
		this.unbekannteFelder = unbekannteFelder;
	}
	public List<Feld> getFormularFelder() {
		return formularFelder;
	}
	public void setFormularFelder(List<Feld> formularFelder) {
		this.formularFelder = formularFelder;
	}
	public List<Feld> getZielfeld() {
		return Zielfeld;
	}
	public void setZielfeld(List<Feld> zielfeld) {
		Zielfeld = zielfeld;
	}
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
	
	public List<Feld> getFelder() {
		return felder;
	}
	public void addUnbekanntesFeld(Feld newFeld) {
		unbekannteFelder.add(0,newFeld);
	}
	

	public Feld gibFeld(int x, int y) {
		for (Feld feld : this.getBekannteFelder()) {
			if (feld.getxKoordinate()== x && feld.getyKoordinate() == y) {
					return feld;
			}
		}
		return null;
	}
	
	

	/**
	 *  Diese Funktion berechnet den Weg eines beliebigen Feldes zum Zielfeld.
	 *  Das Zielarray gibt den Weg von Feld zu Feld an
	 *  A1 --> A2 --> B2 --> Ziel
	 *  Beim Laufen muss hinterher drauf geachtet werden zu schauen wo liegt das Feld A2 von A1 gesehen
	 *  
	 * @param aktuellesFeld ist das Feld auf dem die Spielfigur steht
	 * @param zielFeld ist das Feld wo die Spielfigur hin will
	 * @return Array mit Feldern in der Reihenfolge vom Spieler zum Ziel
	 */
	public List<Feld> route(Feld aktuellesFeld, Feld zielFeld) {
		// alle Vorgänger und den Bearbeitet Status für jedes Feld auf null setzen
		for (Feld feld : this.felder) {
			feld.setVorgaenger(null);
			feld.setInBearbeitung(false);
		}
		
		// Array mit noch zu bearbeitenden Feldern erstellen
		List<Feld> nochZuBearbeiten = new ArrayList<Feld>();
		
		// HilfsFelder setzen     ## eventuell unnötig oO
		
		Feld temp = new Feld();
		
		// temp Vorgänger auf sich selbst setzen, als AbschlussKriterium in Endschleife
		
		aktuellesFeld.setVorgaenger(aktuellesFeld);
		
		// aktuellesFeld in NochBearbeiten Array einfuegen
		
		nochZuBearbeiten.add(aktuellesFeld);
		
		// NochBearbeiten Array abarbeiten
		
		while (!nochZuBearbeiten.isEmpty()) {
			
			// temp auf das erste Element der Liste setzen
			temp = nochZuBearbeiten.get(0);
						
			// temp wird auf bearbeitet gesetzt
			
			temp.setInBearbeitung(true);
			
			// Nachfolger von temp kommen in NochZuBearbeiten und temp wird als deren Vorgänger gesetzt
			
			for (Feld nachfolger : temp.getNachbarnOhne(temp.getVorgaenger(),false)) {
				nochZuBearbeiten.add(nachfolger);
				nachfolger.setVorgaenger(temp);
			}
			
			// temp wird aus zu bearbeiten entfernt
			
			nochZuBearbeiten.remove(temp);
			
			// wenn temp = dem Zielfeld dann kann hier abgebrochen werden
			if (temp == zielFeld) {
				break;
			}
			
		}
		
		// Ergebnis Liste erstellen
		
		List<Feld> route = new ArrayList<Feld>();
		
		while (temp.getVorgaenger()!=null && temp.getVorgaenger()!=temp) {
			route.add(0,temp);
			temp = temp.getVorgaenger();
		}
		
		return route;
		
	}
}
