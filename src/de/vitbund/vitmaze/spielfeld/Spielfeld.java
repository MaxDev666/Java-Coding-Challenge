package de.vitbund.vitmaze.spielfeld;

import java.util.ArrayList;
import java.util.List;
/**
 * Hier werden alle Arten von Feldern in Listen gepackt, sowie Informationen zu den Feldern und dem Spielfeld dokumentiert.
 * @author Benjamin Bogusch, Fritz Köhler, Florian Kreibe, Maximilian Hettitel
 * @version 1.5
 */
public class Spielfeld {
	//Attribute
	private int sizeX;
	private int sizeY;
	private int level;
	private Feld Zielfeld;
	
	// Listen für Felder aller Arten 
	private List<Feld> felder = new ArrayList<Feld>();
	private List<Feld> unbekannteFelder = new ArrayList<Feld>();
	private List<Feld> bekannteFelder = new ArrayList<Feld>();
	private List<Feld> sheetList = new ArrayList<Feld>();
	
	//Getter und Setter
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
	public List<Feld> getFelder() {
		return felder;
	}
	public Feld getZielfeld() {
		return Zielfeld;
	}
	public void setZielfeld(Feld zielfeld) {
		if (this.Zielfeld==null) {
			Zielfeld = zielfeld;
		}
	}
	public List<Feld> getUnbekannteFelder() {
		return unbekannteFelder;
	}
	public List<Feld> getBekannteFelder() {
		return bekannteFelder;
	}
	public List<Feld> getSheetList() {
		return sheetList;
	}
	
	//Methoden
	
	/**
	 * Dies ist eine Methode um ein neues Feld in Liste Feld hinzuzufügen
	 * @param newFeld Feld welches in Liste gespeichert wird
	 */
	public void addFeld(Feld newFeld) {
		felder.add(newFeld);
	}
	/**
	 * Dies ist eine Methode um ein neues Feld in Liste mit unbekannten Feldern hinzuzufügen
	 * @param newFeld Feld welches in Liste gespeichert wird
	 */
	public void addUnbekanntesFeld(Feld newFeld) {
		unbekannteFelder.add(0,newFeld);
	}
	/**
	 * Dies ist eine Methode welche ein Feld an X/Y Koordinate zurück gibt
	 * @param x X-Koordinate des Feldes
	 * @param y Y-Koordinate des Feldes
	 * @return Feld oder null
	 */
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
	 * @return route Array mit Feldern in der Reihenfolge vom Spieler zum Ziel
	 */
	public List<Feld> route(Feld aktuellesFeld, Feld zielFeld) {
		
		long startzeit = System.currentTimeMillis();
		// alle Vorgänger und den Bearbeitet Status für jedes Feld auf null setzen
		for (Feld feld : this.felder) {
			feld.setVorgaenger(null);
			feld.setInBearbeitung(false);
		}
		// Array mit noch zu bearbeitenden Feldern erstellen
		List<Feld> nochZuBearbeiten = new ArrayList<Feld>();
		
		// HilfsFelder setzen 
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
			for (Feld nachfolger : temp.getNachbarnOhne(temp.getVorgaenger())) {
				if (!nochZuBearbeiten.contains(nachfolger)) {
				nochZuBearbeiten.add(nachfolger);
				nachfolger.setVorgaenger(temp);
				}
			}
			// temp wird aus zu bearbeiten entfernt
			nochZuBearbeiten.remove(temp);
			
			// wenn temp = dem Zielfeld dann kann hier abgebrochen werden
			if (temp == zielFeld) {
				break;
			}
			System.err.println(System.currentTimeMillis()-startzeit);
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
