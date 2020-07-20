package de.vitbund.vitmaze.players;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.vitbund.vitmaze.spielfeld.Feld;
import de.vitbund.vitmaze.spielfeld.Spielfeld;
/**
 * Klasse welche den Bot über Methoden agieren lässt
 * @author Arbeitstitel
 * @verion 1.4
 */
public class Standardbot {
	//Attribute
	Spielfeld spielfeld;
	private int playerId;
	private int botx;
	private int boty;
	private Feld aktuellesFeld;
	private Feld letztesFeld;
	private List<Feld> aktuelleRoute;
	private int sheetCount;
	private boolean redetDieRunde;
	
	//Getter und Setter
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public int getBotX() {
		return botx;
	}
	public void setBotX(int botx) {
		this.botx = botx;
	}
	public int getBotY() {
		return boty;
	}
	public void setBotY(int boty) {
		this.boty = boty;
	}
	public Feld getAktuellesFeld() {
		return aktuellesFeld;
	}	
	public void setAktuellesFeld(Feld aktuellesFeld) {
		this.aktuellesFeld = aktuellesFeld;
	}
	public List<Feld> getAktuelleRoute() {
		return aktuelleRoute;
	}
	public void setAktuelleRoute(List<Feld> aktuelleRoute) {
		this.aktuelleRoute = aktuelleRoute;
	}
	public int getSheetCount() {
		return sheetCount;
	}
	public void setSheetCount(int sheetCount) {
		this.sheetCount = sheetCount;
	}
	public boolean isRedetDieRunde() {
		return redetDieRunde;
	}
	public void setRedetDieRunde(boolean redetDieRunde) {
		this.redetDieRunde = redetDieRunde;
	}
	
	//Konstruktor
	public Standardbot(Spielfeld spielfeld) {
		this.spielfeld = spielfeld;
		this.setRedetDieRunde(false);
	}
	
	//Methoden
	/**
	 * Methode welche prüft ob eine Route existiert
	 * @return Antwort mit ja oder nein
	 */
	public boolean hatRoute() {
		if (this.getAktuelleRoute() != null) {
			if (this.getAktuelleRoute().isEmpty()==false) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Methode welche bei verschwundenem Formular die Umgebung absucht
	 * Neue Route mit Ringförmigen Ablaufen der Umgebungsfelder
	 */
	public void sucheUmfeldAb() {
		System.err.println("So ein Mist ich muss suchen");
		// Ring 1
		Feld Startfeld = this.aktuellesFeld;
		List<Feld> tempListe = new ArrayList<Feld>();
		List<Feld> tempListe2 = new ArrayList<Feld>();
		// Ring 1 in Array eingefügt
		for (Feld f1 : this.aktuellesFeld.getNachbarnOhne(Startfeld)) {
			if (!tempListe.contains(f1)) {
				tempListe.add(f1);
				tempListe2.addAll(spielfeld.route(Startfeld, f1));
				for (Feld f2 : f1.getNachbarnOhne(Startfeld)) {
					if (!tempListe.contains(f2)) {
						tempListe.add(f2);
						tempListe2.addAll(spielfeld.route(f1, f2));
						tempListe2.add(f1);
					}
				}
				tempListe2.add(Startfeld);
			}
		}
		System.err.println("Templiste und 2. Templiste "+tempListe +" "+ tempListe2);
		setAktuelleRoute(tempListe2);
	}
	
	/**
	 * Methode welche von der Route abruft wohin sich der Bot bewegen soll und
	 * nach bewegung das Unbekannte Feld den Bekannten hinzufügt
	 * @return ergebnis string in welche Richtung der Bot gelaufen ist
	 */
	public String move() {
		Feld zuFeld = new Feld( );
		String ergebnis = new String();
		
		//holt sich nächstes Feld aus der Route
		this.letztesFeld = this.aktuellesFeld;
		zuFeld = this.getAktuelleRoute().get(0);
		System.err.println("Zielfeld der Route: " + this.getAktuelleRoute().get(this.aktuelleRoute.size()-1));
		System.err.println("Die Koordinaten dieses Feldes: "+this.getAktuelleRoute().get(this.aktuelleRoute.size()-1).getxKoordinate()+"|"+
							this.getAktuelleRoute().get(this.aktuelleRoute.size()-1).getyKoordinate());
		
		//in entsprechende Richtung laufen
		switch (this.getAktuellesFeld().getRichtung(zuFeld)){
			case "north":
				this.getAktuelleRoute().remove(this.getAktuellesFeld());
				ergebnis = this.goNorth();
				break;
			case "south":
				this.getAktuelleRoute().remove(this.getAktuellesFeld());
				ergebnis = this.goSouth();
				break;
			case "east":
				this.getAktuelleRoute().remove(this.getAktuellesFeld());
				ergebnis = this.goEast();
				break;
			case "west":
				this.getAktuelleRoute().remove(this.getAktuellesFeld());
				ergebnis = this.goWest();
				break;
		}
		//löscht das Feld aus unbekannten Feldern und fügt es den bekannten hinzu
		if (spielfeld.getUnbekannteFelder().contains(this.getAktuellesFeld())) {
			spielfeld.getUnbekannteFelder().remove(this.getAktuellesFeld());
			spielfeld.getBekannteFelder().add(this.getAktuellesFeld());
		}
		//Feld aus Route entfernen
		if (this.getAktuellesFeld() == zuFeld) {
			this.getAktuelleRoute().remove(this.getAktuellesFeld());
		}
		return ergebnis;
	}
	
	/**
	 * Methode welche string zurück gibt
	 * @return string "take"
	 */
	public String take() {
		return "take";
	}
	
	/**
	 * Methode welche string zurück gibt
	 * @return string "put"
	 */
	public String put() {
		return "put";
	}
	
	/**
	 * Methode welche Sheets sowie Formulare kickt
	 * @param n Status des Feld im Norden
	 * @param e Status des Feldes im Osten
	 * @param s Status des Feldes im Süden
	 * @param w Status des Feldes im Westen
	 * @param sheet ja = kickt sheet, nein = kickt Formular
	 * @return string "fail"
	 */
	public String kick(String n, String e, String s, String w, boolean sheet) {
		
		if (sheet) {
			if (n.split(" ")[0].equals("FLOOR") | n.split(" ")[0].equals("FORM")) {
				return "kick north";
			} else 	if (e.split(" ")[0].equals("FLOOR") | n.split(" ")[0].equals("FORM")) {
				return "kick east";
			} else 	if (s.split(" ")[0].equals("FLOOR") | n.split(" ")[0].equals("FORM")) {
				return "kick south";
			} else 	if (w.split(" ")[0].equals("FLOOR") | n.split(" ")[0].equals("FORM")) {
				return "kick west";
			}
		} else {
			if (n.split(" ")[0].equals("FLOOR")) {
				return "kick north";
			} else 	if (e.split(" ")[0].equals("FLOOR")) {
				return "kick east";
			} else 	if (s.split(" ")[0].equals("FLOOR")) {
				return "kick south";
			} else 	if (w.split(" ")[0].equals("FLOOR")) {
				return "kick west";
			}
		}
		return "fail";
	}
	
	/**
	 * Methode welche string zurück gibt
	 * @return string "finsish"
	 */
	public String finish() {
		return "finish";
	}
	
	/**
	 * Methode welche den Bot sich nach Westen bewegen lässt
	 * @return string "go west" 
	 */
	public String goWest() {
		this.setAktuellesFeld(this.getAktuellesFeld().getWest());
		//bei verlassen des Spielfeldrandes springen auf andere Spielfeldseite
		if (this.getBotX()-1 < 0) {
			this.setBotX(spielfeld.getSizeX()-1);
		} else {
			this.setBotX(this.getBotX()-1);
		}
		return "go west";	
	}
	
	/**
	 * Methode welche den Bot sich nach Norden bewegen lässt
	 * @return string "go north"
	 */
	public String goNorth() {
		this.aktuellesFeld = this.aktuellesFeld.getNorth();
		if (this.getBotY()-1 < 0) {
			this.setBotY( +spielfeld.getSizeY()-1);
		} else {
			this.setBotY(this.getBotY()-1);
		}
		return "go north";	
	}
	
	/**
	 * Methode welche den Bot sich nach Osten bewegen lässt
	 * @return string "go east" 
	 */
	public String goEast() {
		this.aktuellesFeld = this.aktuellesFeld.getEast();
		if (this.getBotX()+1 == spielfeld.getSizeX()) {
			this.setBotX(0);
		} else {
			this.setBotX(this.getBotX()+1);
		}
		return "go east";
	}
	
	/**
	 * Methode welche den Bot sich nach Süden bewegen lässt
	 * @return string "go south" 
	 */
	public String goSouth() {
		this.aktuellesFeld = this.aktuellesFeld.getSouth();
		if (this.getBotY()+1 == spielfeld.getSizeY()) {
			this.setBotY(0);
		} else {
			this.setBotY(this.getBotY()+1);
		}
		return "go south";	
	}
	
	

	
}
