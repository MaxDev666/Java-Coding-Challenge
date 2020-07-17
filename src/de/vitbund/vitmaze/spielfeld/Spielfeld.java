package de.vitbund.vitmaze.spielfeld;

import java.util.ArrayList;
import java.util.List;
/**
 * Hier werden alle Arten von Feldern in Listen gepackt, sowie information zu Feldern/dem Spielfeld dokumentiert
 * @author Arbeitstitel
 * @version 1.0
 */
public class Spielfeld {

	private int sizeX;
	private int sizeY;
	private int level;
	// Felder merken
	private List<Feld> felder = new ArrayList<Feld>();
	
	// Interessante Felder merken.
	private List<Feld> interessanteFelder = new ArrayList<Feld>();
	
	// Felder mit Formularen merken.
	private List<Feld> formularFelder = new ArrayList<Feld>();
	
	// Feld Ziel merken.
	private Feld Zielfeld;
	
	// Noch zu erkundende Felder merken.
	private List<Feld> unbekannteFelder = new ArrayList<Feld>();

	// Bekannte Felder merken.
	private List<Feld> bekannteFelder = new ArrayList<Feld>();
	
	// Getter und Setter 
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
	public List<Feld> getFelder() {
		return felder;
	}
	// Zielfeld erzeugen.
	public Spielfeld() {
		this.Zielfeld = new Feld();
		this.Zielfeld = null;
	}
	
	public Feld getZielfeld() {
		return Zielfeld;
	}
	public void setZielfeld(Feld zielfeld) {
		if (this.Zielfeld==null) {
			Zielfeld = zielfeld;
		}
	}
	// Laenge der X-/Y-Achse des Spielfeldes merken.
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
	// Schwierigkeitsgrad des Levels merken.
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	// Erzeugen neuer bekannter/unbekannter Felder.
	public void addFeld(Feld newFeld) {
		felder.add(newFeld);
	}
	
	public void addInteressantesFeld(Feld newFeld) {
		interessanteFelder.add(newFeld);
	}
	
	public void addUnbekanntesFeld(Feld newFeld) {
		unbekannteFelder.add(0,newFeld);
	}
	
	/**
	 * Diese Funktion 
	 * @param x ist die x-Koordinate des bekannten Feldes
	 * @param y ist die y-Koordinate des bekannten Feldes
	 * @return gibt Feld zurück ansonsten null
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
