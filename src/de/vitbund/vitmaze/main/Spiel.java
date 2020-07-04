package de.vitbund.vitmaze.main;

import java.util.List;

import de.vitbund.vitmaze.eingabe.Eingabe;
import de.vitbund.vitmaze.players.Standardbot;
import de.vitbund.vitmaze.spielfeld.Feld;
import de.vitbund.vitmaze.spielfeld.Spielfeld;

public class Spiel {

	Spielfeld spielfeld;
	Feld aktuellesFeld;
	Standardbot bot;
	
	// dürfte obsolet werden
	byte blickrichtung; // 1 Norden, 2 Osten, 3 Süden, 4 Westen
	
	
	String lastActionsResult;
	String currentCellStatus;
	String northCellStatus;
	String eastCellStatus;
	String southCellStatus;
	String westCellStatus;
	boolean hatZiel;
	char richtungFeldErstellen;
	List<Feld> ziele;
	
	
	
	public void init() {
		// Spielfeld anlegen und Startdaten setzen
		spielfeld = new Spielfeld();
		spielfeld.setSizeX(Eingabe.leseZahl()); // X-Groesse des Spielfeldes (Breite)
		spielfeld.setSizeY(Eingabe.leseZahl()); // Y-Groesse des Spielfeldes (Hoehe)
		spielfeld.setLevel(Eingabe.leseZahl()); // Level des Matches
		Eingabe.leseZeile();

		// Startfeld anlegen und zum Spielfeld hinzufügen
		aktuellesFeld = new Feld();
		spielfeld.addFeld(aktuellesFeld);
		
		// Bot anlegen und Startdaten setzen
		bot = new Standardbot(aktuellesFeld);
		bot.setPlayerId(Eingabe.leseZahl());// id dieses Players / Bots
		bot.setStartX(Eingabe.leseZahl());// X-Koordinate der Startposition dieses Player
		bot.setStartY(Eingabe.leseZahl()); // Y-Koordinate der Startposition dieses Players
		Eingabe.leseZeile();
		
		blickrichtung = 1;
		hatZiel=false;
	}
	
	public void getStati() {
		

		this.lastActionsResult = Eingabe.leseZeile();
		this.currentCellStatus = Eingabe.leseZeile();
		this.northCellStatus = Eingabe.leseZeile();
		this.eastCellStatus = Eingabe.leseZeile();
		this.southCellStatus = Eingabe.leseZeile();
		this.westCellStatus = Eingabe.leseZeile();
		
		System.err.println("A01");
		if (!hatZiel){
			
			this.erkunden();
		} else {
			System.err.println("B01");
			if (bot.hatRoute()) {
				System.err.println("C01");
				bot.move();
			} else {
				System.err.println("C02");
				// suche Route
				// hier in interessanteFelder suchen welchen Antrag man als nächstes braucht
				// dann auf aktuelles Ziel setzen
				// aktuelleRoute auf nächstes Ziel setzen und hatZiel auf ja setzen
			}
		}
		
		
		// hier noch abfragen ob wir am Ziel sind und irgendwas aufnehmen müssen oder ob wir was auf dem Weg gefunden haben
		
		
		// MOVE

		
		
		
	}
	
	
	public void erkunden() {

		if (this.currentCellStatus.equals("FINISH " +bot.getPlayerId()+ " 0")) {
			hatZiel=true;
			System.out.println("finish");
		} else {
				
			// Umgebung anlegen wenn noch nicht vorhanden

			if (this.northCellStatus.equals("FLOOR") && this.aktuellesFeld.getNorth()==null) {
				this.richtungFeldErstellen='n';
				Feld neuesFeld = new Feld();
				aktuellesFeld.setNorth(neuesFeld);
				spielfeld.addUnbekanntesFeld(neuesFeld);
			}
			if (this.eastCellStatus.equals("FLOOR")  && this.aktuellesFeld.getEast()==null) {
				this.richtungFeldErstellen='e';
				Feld neuesFeld = new Feld();
				aktuellesFeld.setEast(neuesFeld);
				spielfeld.addUnbekanntesFeld(neuesFeld);
			}
			if (this.southCellStatus.equals("FLOOR") && this.aktuellesFeld.getSouth()==null) {
				this.richtungFeldErstellen='s';
				Feld neuesFeld = new Feld();
				aktuellesFeld.setSouth(neuesFeld);
				spielfeld.addUnbekanntesFeld(neuesFeld);
			}
			if (this.westCellStatus.equals("FLOOR") && this.aktuellesFeld.getWest()==null) {
				this.richtungFeldErstellen='w';
				Feld neuesFeld = new Feld();
				aktuellesFeld.setWest(neuesFeld);
				spielfeld.addUnbekanntesFeld(neuesFeld);
			}
			
			System.err.println("Bot hat Route: " + bot.hatRoute());
			if (bot.hatRoute()==false) {
				System.err.println("hier passiert nichts!!!!!");
				bot.setAktuelleRoute(spielfeld.route(aktuellesFeld, spielfeld.getUnbekannteFelder().get(0)));
			}
			bot.move();
			System.err.println(aktuellesFeld + " zu " + spielfeld.getUnbekannteFelder().get(0));
			System.err.println(aktuellesFeld.getRichtung(spielfeld.getUnbekannteFelder().get(0)));
			if (aktuellesFeld == spielfeld.getUnbekannteFelder().get(0)) {
				bot.getAktuelleRoute().remove(spielfeld.getUnbekannteFelder().get(0));
			}
			
			// hier intelligente Möglichkeiten ausdenken zur Wegfindung
			
			//Idee von Maxi zur Umsetzung der Aktion, die man aus dem Feld bekommt
			
			//## Anmerkung ich würde hier aktuelleRoute verwenden und die nur einmal festlegen, dann passt das wie in der Methode oben genau rein, und der Bit läuft immer bis zum nächsten unbekannten Feld, AUSSER der Bot bemerkt auf dem Weg z.B. ein verschobenes Formular
			// dann sparen wir uns hier die Wegfindung und in erkunden würden wir nur das nächste Feld aus unbekannteFelder in die aktuelleRoute packen und hatZiel auf true setzen
			
			/*
			List<Feld> route;
			route = spielfeld.route(aktuellesFeld, zielFeld);
			for (int i = 0; i < route.size(); i++) {
				switch (route.get(i).getRichtung(route.get(i+1))) {
				case "north":
					bot.goNorth();
					break;
				case "east":
					bot.goEast();
					break;
				case "south":
					bot.goSouth();
					break;
				case "west":
					bot.goWest();
					break;
				default:
					System.err.println("Das Feld ist kein Nachbar");
				}
			}
			*/
			
			
			// Felder die in der nächsten If Abfrage gesichtet wurden müssen aus der unbekannteFelder Liste gelöscht werden
			// Entweder hier oder in Standartbot
			// der nächste Block müsste auch verschwinden, wenn die Wegfindungsroutine läuft
			/*boolean hatSichbewegt=false;
			while (hatSichbewegt==false) {
				if (this.blickrichtung == 1 &&   !this.northCellStatus.equals("WALL")) {
					bot.goNorth();
					hatSichbewegt=true;
				} else 	if (this.blickrichtung == 2 &&  !this.eastCellStatus.equals("WALL")) {
					bot.goEast();
					hatSichbewegt=true;
				} else 	if (this.blickrichtung == 3 &&  !this.southCellStatus.equals("WALL")) {
					bot.goSouth();
					hatSichbewegt=true;
				} else 	if (this.blickrichtung == 4 &&  !this.westCellStatus.equals("WALL")) {
					bot.goWest();
					hatSichbewegt=true;
				} else {
				aendereBlickrichtung();}
			*/
			
		}
	}
	
	// Wenn Wegfindung läuft uninteressant
	public void aendereBlickrichtung() {
		if (this.blickrichtung == 4) {
			this.blickrichtung = 2;
		}
		else if (this.blickrichtung == 3) {
			this.blickrichtung = 1;
		}
		else if (this.blickrichtung == 2) {
			this.blickrichtung = 3;
		}		
		else if (this.blickrichtung == 1) {
			this.blickrichtung = 4;
		}
		
	}
	
	public void erstellFeld() {
		Feld neuesFeld = new Feld();
		switch (richtungFeldErstellen) {
		case 'n':
			this.aktuellesFeld.setNorth(neuesFeld);
			neuesFeld.setSouth(this.aktuellesFeld);
			break;
		case 'e':
			this.aktuellesFeld.setEast(neuesFeld);
			neuesFeld.setWest(this.aktuellesFeld);
			break;
		case 's':
			this.aktuellesFeld.setSouth(neuesFeld);
			neuesFeld.setNorth(this.aktuellesFeld);
			break;
		case 'w':
			this.aktuellesFeld.setWest(neuesFeld);
			neuesFeld.setEast(this.aktuellesFeld);
			break;
		}
		this.spielfeld.addFeld(neuesFeld);
		spielfeld.addUnbekanntesFeld(neuesFeld);
	}
	
}
