package de.vitbund.vitmaze.players;

import java.util.ArrayList;
import java.util.List;

import de.vitbund.vitmaze.spielfeld.Feld;
import de.vitbund.vitmaze.spielfeld.Spielfeld;
/**
 * Dies ist eine Klasse, die die Aktionen unsereres Bots steuert. Dazu zählt unter anderem move, kick, take und put. 
 * Außerdem gibt es eine Methode zur Formularfindung, wenn dieses weggekickt wurde.
 * @author Benjamin Bogusch, Fritz Köhler, Florian Kreibe, Maximilian Hett
 * @version 1.5
 */
public class Standardbot {
	//Attribute
	Spielfeld spielfeld;
	private int playerId;
	private int botx;
	private int boty;
	private Feld aktuellesFeld;
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
	/**
	 * Der Konstruktor setzt das Spielfeld auf das im Parameter angegebene Spielfeld und setzt die Variable zur Redeerkennung auf false.
	 * @param unser vorher angegelegtes Spielfeld
	 */
	public Standardbot(Spielfeld spielfeld) {
		this.spielfeld = spielfeld;
		this.setRedetDieRunde(false);
	}
	
	//Methoden
	/**
	 * Dies ist eine Methode, die prüft, ob unser Bot momentan eine Route hat.
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
	 * Dies ist eine Methode, die bei weg gekickten Formularen die Umgebung absucht.
	 * Dabei wird nur bis zu einer Tiefe von 5 Feldern ausgehend vom Startpunkt gesucht, da wir nicht davon ausgehen, dass ein Formular weiter weg gekickt wird.
	 */
	public void sucheUmfeldAb() {
		// Initialisierung des Startfelds und der Listen
		Feld Startfeld = this.aktuellesFeld;
		List<Feld> tempListe = new ArrayList<Feld>();
		List<Feld> tempListe2 = new ArrayList<Feld>();
		// direkte Nachbarn und Route dahin in die Listen einfügen
		for (Feld f1 : this.aktuellesFeld.getNachbarnOhne(Startfeld)) {
			if (!tempListe.contains(f1)) {
				tempListe.add(f1);
				tempListe2.addAll(spielfeld.route(Startfeld, f1));
				// von dem direkten Nachbarn ausgehend jeweilige Nachbarn und Route in die Listen einfügen
				for (Feld f2 : f1.getNachbarnOhne(Startfeld)) {
					if (!tempListe.contains(f2)) {
						tempListe.add(f2);
						tempListe2.addAll(spielfeld.route(f1, f2));
						// vom neuen Nachbarn ausgehend jeweilige Nachbarn und Route in die Listen einfügen
						for (Feld f3 : f2.getNachbarnOhne(f1)) {
							if (!tempListe.contains(f3)) {
								tempListe.add(f3);
								tempListe2.addAll(spielfeld.route(f2, f3));
								// vom neuen Nachbarn ausgehend jeweilige Nachbarn und Route in die Listen einfügen
								for (Feld f4 : f3.getNachbarnOhne(f2)) {
									if (!tempListe.contains(f4)) {
										tempListe.add(f4);
										tempListe2.addAll(spielfeld.route(f3, f4));
										// vom neuen Nachbarn ausgehend jeweilige Nachbarn und Route in die Listen einfügen
										for (Feld f5 : f4.getNachbarnOhne(f3)) {
											if (!tempListe.contains(f5)) {
												tempListe.add(f5);
												tempListe2.addAll(spielfeld.route(f4, f5));
												tempListe2.add(f4);
											}
										}
										tempListe2.add(f3);
									}
								}
								tempListe2.add(f2);
							}
						}
						tempListe2.add(f1);
					}
				}
				tempListe2.add(Startfeld);
			}
		}
		setAktuelleRoute(tempListe);
	}
	
	/**
	 * Dies ist eine Methode, die von der Route abruft, wohin sich der Bot bewegen soll und
	 * nach der Bewegung das Unbekannte Feld den Bekannten hinzufügt
	 * @return String, in welche Richtung der Bot gelaufen ist. (Dies wird in der Spiel-Klasse als Aktion gesetzt)
	 */
	public String move() {
		Feld zuFeld = new Feld( );
		String ergebnis = new String();
		
		//holt sich nächstes Feld aus der Route
		zuFeld = this.getAktuelleRoute().get(0);
		
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
		//löscht das Feld aus unbekannten Feldern und fügt es den Bekannten hinzu
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
	 * Dies ist eine Methode, welche den String "finish" als Zielmarkierung zurück gibt
	 * @return string "finish"
	 */
	public String finish() {
		return "finish";
	}
	
	/**
	 * Dies ist eine Methode, welche den String "take" als Aufhebebefehl zurück gibt
	 * @return String "take"
	 */
	public String take() {
		return "take";
	}
	
	/**
	 * Dies ist eine Methode, welche den String "put" als Legebefehl eines Sheets zurück gibt
	 * @return string "put"
	 */
	public String put() {
		return "put";
	}
	
	/**
	 * Dies ist eine Methode, welche sowohl Sheets als auch Formulare in eine Richtung kicken kann
	 * Dieser Befehl mit der Richtung wird dann zurückgegeben
	 * @param n gibt den Status des Feldes im Norden an
	 * @param e gibt den Status des Feldes im Osten an
	 * @param s gibt den Status des Feldes im Süden an
	 * @param w gibt den Status des Feldes im Westen an
	 * @param sheet ist ein boolean, der anzeigt, ob ein Sheet oder ein Formular gekickt werden soll. (ja = kickt sheet, nein = kickt Formular)
	 * @return gibt den kick-Befehl mit der Richtung oder ein "fail" zurück
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
	 * Dies ist eine Methode, den Bot nach Westen bewegen lässt. Dabei prüft Sie, ob die Spielfeldränder übertreten werden.
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
	 * Dies ist eine Methode, den Bot nach Norden bewegen lässt. Dabei prüft Sie, ob die Spielfeldränder übertreten werden.
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
	 * Dies ist eine Methode, den Bot nach Osten bewegen lässt. Dabei prüft Sie, ob die Spielfeldränder übertreten werden.
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
	 * Dies ist eine Methode, den Bot nach Süden bewegen lässt. Dabei prüft Sie, ob die Spielfeldränder übertreten werden.
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
