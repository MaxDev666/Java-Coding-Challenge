package de.vitbund.vitmaze.main;

import java.util.List;

import de.vitbund.vitmaze.eingabe.Eingabe;
import de.vitbund.vitmaze.players.Standardbot;
import de.vitbund.vitmaze.spielfeld.Feld;
import de.vitbund.vitmaze.spielfeld.Spielfeld;

public class Spiel {

	Spielfeld spielfeld;
	Standardbot bot;
	
	
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

		// Bote erstellen
		bot = new Standardbot(spielfeld);
		
		// Startfeld anlegen und zum Spielfeld hinzuf�gen
		bot.setAktuellesFeld(new Feld());
		spielfeld.addFeld(bot.getAktuellesFeld());
		
		// Bot anlegen und Startdaten setzen
		
		bot.setPlayerId(Eingabe.leseZahl());// id dieses Players / Bots
		Eingabe.leseZeile();
		bot.setBotX(500); // X-Koordinate der Startposition dieses Player
		bot.setBotY(500); // Y-Koordinate der Startposition dieses Players
		
		hatZiel=false;

	}
	
	public void getStati() {
		

		this.lastActionsResult = Eingabe.leseZeile();
		this.currentCellStatus = Eingabe.leseZeile();
		this.northCellStatus = Eingabe.leseZeile();
		this.eastCellStatus = Eingabe.leseZeile();
		this.southCellStatus = Eingabe.leseZeile();
		this.westCellStatus = Eingabe.leseZeile();
		bot.getUpdate();
		
		this.erkunden();

		System.err.println("Jetze soll ich mich bewegen von: " + bot.getAktuellesFeld() + " nach " + bot.getAktuelleRoute().get(0));
		bot.move();

		// hier noch abfragen ob wir am Ziel sind und irgendwas aufnehmen m�ssen oder ob wir was auf dem Weg gefunden haben
		

		bot.getUpdate();
		
		
		
	}
	
	
	public void erkunden() {

		if (this.currentCellStatus.equals("FINISH " +bot.getPlayerId()+ " 0")) {
			hatZiel=true;
			System.out.println("finish");
		} else {
				
			// Umgebung anlegen wenn noch nicht vorhanden
			System.err.println("Ich schaue mir meine Umgebung an");
			if ( bot.getAktuellesFeld().getNorth()==null) {
				if (this.northCellStatus.equals("FLOOR") || this.northCellStatus.equals("FINISH " +bot.getPlayerId()+ " 0")) {
					this.richtungFeldErstellen='n';
					this.erstellFeld();
					System.err.println("Neues Feld im Norden enddeckt" + bot.getAktuellesFeld().getNorth());
				}
			}
			if (bot.getAktuellesFeld().getEast()==null) {
				if (this.eastCellStatus.equals("FLOOR") || this.eastCellStatus.equals("FINISH " +bot.getPlayerId()+ " 0")) {
					this.richtungFeldErstellen='e';
					this.erstellFeld();
					System.err.println("Neues Feld im Osten enddeckt " + bot.getAktuellesFeld().getEast());
				}
			}
			if (bot.getAktuellesFeld().getSouth()==null) {
				if (this.southCellStatus.equals("FLOOR") ||  this.southCellStatus.equals("FINISH " +bot.getPlayerId()+ " 0")) {
					this.richtungFeldErstellen='s';
					this.erstellFeld();
					System.err.println("Neues Feld im S�den enddeckt"  + bot.getAktuellesFeld().getSouth());
				}
			}
			if (bot.getAktuellesFeld().getWest()==null) {
				if (this.westCellStatus.equals("FLOOR")||  this.westCellStatus.equals("FINISH " +bot.getPlayerId()+ " 0")) {
					this.richtungFeldErstellen='w';
					this.erstellFeld();
					System.err.println("Neues Feld im Westen enddeckt" + bot.getAktuellesFeld().getWest());
				}
			}
			
			bot.getUpdate();
			
			System.err.println("Bot hat Route: " + bot.hatRoute());
			if (bot.hatRoute()==false) {
				System.err.println(" MUSS NEUE ROUTE W�HLEN ");
				bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(0)));
				System.err.println("Bot hat Route: " + bot.hatRoute());
			}
			for (Feld temp : bot.getAktuelleRoute()) {
				System.err.printf(temp + " -> ");
			}
			System.err.println();


			
			// hier intelligente M�glichkeiten ausdenken zur Wegfindung
			
			//Idee von Maxi zur Umsetzung der Aktion, die man aus dem Feld bekommt
			
			//## Anmerkung ich w�rde hier aktuelleRoute verwenden und die nur einmal festlegen, dann passt das wie in der Methode oben genau rein, und der Bit l�uft immer bis zum n�chsten unbekannten Feld, AUSSER der Bot bemerkt auf dem Weg z.B. ein verschobenes Formular
			// dann sparen wir uns hier die Wegfindung und in erkunden w�rden wir nur das n�chste Feld aus unbekannteFelder in die aktuelleRoute packen und hatZiel auf true setzen
			
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
			
			
			
		}
	}

	public void erstellFeld() {
			Feld neuesFeld = new Feld();
			switch (richtungFeldErstellen) {
			case 'n':
				bot.getAktuellesFeld().setNorth(neuesFeld);
				neuesFeld.setSouth(bot.getAktuellesFeld());
				break;
			case 'e':
				bot.getAktuellesFeld().setEast(neuesFeld);
				neuesFeld.setWest(bot.getAktuellesFeld());
				break;
			case 's':
				bot.getAktuellesFeld().setSouth(neuesFeld);
				neuesFeld.setNorth(bot.getAktuellesFeld());
				break;
			case 'w':
				bot.getAktuellesFeld().setWest(neuesFeld);
				neuesFeld.setEast(bot.getAktuellesFeld());
				break;
			}
			
			this.spielfeld.addFeld(neuesFeld);
			if (!spielfeld.getBekannteFelder().contains(neuesFeld)) {
				spielfeld.addUnbekanntesFeld(neuesFeld);		
			}
	}
	
}
