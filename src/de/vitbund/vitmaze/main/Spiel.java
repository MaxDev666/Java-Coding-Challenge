package de.vitbund.vitmaze.main;

import de.vitbund.vitmaze.eingabe.Eingabe;
import de.vitbund.vitmaze.players.Standardbot;
import de.vitbund.vitmaze.spielfeld.Feld;
import de.vitbund.vitmaze.spielfeld.Formular;
import de.vitbund.vitmaze.spielfeld.Spielfeld;

/**
* Diese Klasse kümmert sich um die Spielmechanik
* @author Benjamin Bogusch, Fritz Köhler, Florian Kreibe, Maximilian Hett
* @version 1.5
*/
public class Spiel {
	//Attribute
	Spielfeld spielfeld;
	Standardbot bot;
	String lastActionsResult;
	String currentCellStatus;
	String northCellStatus;
	String eastCellStatus;
	String southCellStatus;
	String westCellStatus;
	String ausgabe;
	Formular[] forms;
	int anzahlFormulare;
	int formID;
	int formcounter;
	boolean allesGesammelt;
	boolean rundeZuEnde;
	boolean sheetgelegt;

	/**
	 * Methode um das Spielfield, den Bot, das Startfeld und die Formulardaten zu initialisieren
	 */
	public void init() {
		// Spielfeld anlegen und Startdaten setzen
		spielfeld = new Spielfeld();
		spielfeld.setSizeX(Eingabe.leseZahl()); // X-Groesse des Spielfeldes (Breite)
		spielfeld.setSizeY(Eingabe.leseZahl()); // Y-Groesse des Spielfeldes (Hoehe)
		spielfeld.setLevel(Eingabe.leseZahl()); // Level des Matches
		Eingabe.leseZeile();

		// Bot erstellen
		bot = new Standardbot(spielfeld);
		bot.setPlayerId(Eingabe.leseZahl());// id dieses Players / Bots
		bot.setBotX(Eingabe.leseZahl()); // X-Koordinate der Startposition dieses Player
		bot.setBotY(Eingabe.leseZahl()); // Y-Koordinate der Startposition dieses Players
		if (spielfeld.getLevel()==5) {
			bot.setSheetCount(Eingabe.leseZahl());
		}
		Eingabe.leseZeile();

		// Startfeld anlegen und zum Spielfeld hinzufügen
		Feld aktuellesFeld =new Feld(bot.getBotX(),bot.getBotY());
		bot.setAktuellesFeld(aktuellesFeld);
		spielfeld.getBekannteFelder().add(aktuellesFeld);
		spielfeld.addFeld(bot.getAktuellesFeld());
		
		// Formulardaten setzen
		anzahlFormulare=999;
		forms = new Formular[26];
		for (Formular form : forms) {
			form = null;
		}
		formID=0;
		formcounter = 1;
		
		
		allesGesammelt = false;
		sheetgelegt = false;
		ausgabe = new String("position");
	}
	
	/**
	 * Dies ist eine Methode, welche die Eingaben des Spiels entgegennimmt, den Bot zum erkunden schickt, entsprechende Formulare aufnimmt oder gegebenenfalls in der Umgebung suchen lässt
	 * sowie das Ziel bei allen vorhandenen Formularen aufsucht. Diese Methode wiederholt sich jede Runde und gibt die Ausgabe aus.
	 * * Sollte eine Aktion durchgeführt werden, wird das Attribut "rundezuEnde" auf true gesetzt, sodass keine weiteren Aktionen mehr durchgeführt werden können 
	 */
	public void getStati() {
		rundeZuEnde = false;

		//Eingaben entgegennehmen
		this.lastActionsResult = Eingabe.leseZeile();
		this.currentCellStatus = Eingabe.leseZeile();
		this.northCellStatus = Eingabe.leseZeile();
		this.eastCellStatus = Eingabe.leseZeile();
		this.southCellStatus = Eingabe.leseZeile();
		this.westCellStatus = Eingabe.leseZeile();
		
		//Aufruf der erkunden()-Methode 
		erkunden();
		
		//prüft ob Runde beendet ist
		if (rundeZuEnde == false) {
			if (sheetgelegt== true) {
				sheetgelegt = false;
			}
			//prüft ob er bereits eine Route hat
			if (bot.hatRoute()) {
				this.ausgabe = bot.move();
				rundeZuEnde = true;
			} else {
				//wenn Anzahl der Formulare stimmt, route auf Zielfeld setzen
				if (spielfeld.getZielfeld() != null && (formcounter-1)==anzahlFormulare) {
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getZielfeld()));
					this.ausgabe = bot.move();
					rundeZuEnde = true;
				} else if (forms[formcounter] != null) {
					//nimmt passendes Formular auf wenn es auf aktuellen Feld liegt
					if (forms[formcounter].getFeld() == bot.getAktuellesFeld()) {
						if (this.currentCellStatus.startsWith("FORM " + bot.getPlayerId() + " " + formcounter)) {
							this.ausgabe = bot.take();
							formcounter += 1;
							rundeZuEnde = true;
						} else {
							//sucht Umfeld ab wenn Formular nichtmehr da liegt
							bot.sucheUmfeldAb();
							this.ausgabe = bot.move();
							rundeZuEnde = true;
						}
					} else {
						//setzt Route auf das nächste benötigte Formular
						bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), forms[formcounter].getFeld()));
						this.ausgabe = bot.move();
						rundeZuEnde = true;
					}
				} else if (!spielfeld.getUnbekannteFelder().isEmpty()){
					//gibt Koordinaten von nächstem Unbekannten Feld aus und setzt seine Route dorthin
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(0)));
					this.ausgabe = bot.move();
					rundeZuEnde = true;
				} 
			}
		}
		System.out.println(this.ausgabe);
	}
	
	/**
	 * Diese Methode erkundet die benachbarten Felder, prüft ob mit anderen Bots geredet wird.
	 * Weiterhin sucht diese Methode in der Umgebung nach Formularen und nimmt diese wenn möglich auf. 
	 * Prüft ob "Gegner-Formulare" kick/verdeckbar sind, ausgabe "kickMöglich"
	 * Sollte eine Aktion durchgeführt werden, wird das Attribut "rundezuEnde" auf true gesetzt, sodass keine weiteren Aktionen mehr durchgeführt werden können 
	 */
	
	public void erkunden() {

		schaueRichtung('n');
		schaueRichtung('e');
		schaueRichtung('s');
		schaueRichtung('w');

		String[] aktFeld = this.currentCellStatus.split(" ");
		//prüft ob Runde durch reden mit anderen Bot beendet ist
		if (aktFeld[aktFeld.length - 1].equals("!")) {
			if (bot.isRedetDieRunde() == false) {
				bot.setRedetDieRunde(true);
				rundeZuEnde = true;
			} else {
				bot.setRedetDieRunde(false);
			}
		}
		//sofern Runde noch läuft
		if (rundeZuEnde == false) {
			//nimmt zutreffendes Formular auf
			if (aktFeld[0].equals("FORM")) {
				if (aktFeld[1].equals(bot.getPlayerId() + "") && aktFeld[2].equals(formcounter + "")) {
					this.ausgabe = bot.take();
					formcounter += 1;
					rundeZuEnde = true;

					// wenn Aufnahme nicht möglich ist, wird je nach Level ein Sheet gelegt auf fremde Formulare gelegt oder diese gekickt
				} else if (!aktFeld[1].equals(bot.getPlayerId() + "") && (spielfeld.getLevel()>3) ) {

					if (bot.getSheetCount() > 0 && (spielfeld.getLevel()==5)) {
						bot.setSheetCount(bot.getSheetCount() - 1);
						this.ausgabe = bot.put();
						sheetgelegt = true;
						rundeZuEnde = true;
					} else {

						if (spielfeld.getLevel()>3) {
							String kickMöglich = bot.kick(this.northCellStatus, this.eastCellStatus, this.southCellStatus, this.westCellStatus, false);
							if (!kickMöglich.equals("fail")) {
								this.ausgabe = kickMöglich;
								rundeZuEnde = true;
							}
						}
					}
				}
			} else if ((spielfeld.getLevel()==5) && aktFeld[0].equals("SHEET") && sheetgelegt == false) {

						//prüft ob ein Sheet kickbar ist
						String kickMöglich = bot.kick(this.northCellStatus, this.eastCellStatus, this.southCellStatus, this.westCellStatus, false);
						if (!kickMöglich.equals("fail")) {
							this.ausgabe = kickMöglich;
							rundeZuEnde = true;
						}
					
			} else if (aktFeld[0].equals("FINISH") && aktFeld[1].equals(bot.getPlayerId() + "")) {
				// es wird die Aktion "finish" ausgegeben
				if (allesGesammelt) {
					this.ausgabe = bot.finish();
					rundeZuEnde = true;
				}
			}
			}
		}
	
	
	/**
	 * Prüft , ob sich in der Richtung keine Wand befindet, dann erstelle das Feld
	 * Prüft ob in der Richtung das Finish ist, dann gehe zum Ziel
	 * Prüft, ob in der Richtung ein Formular ist, dann setzt er seine Route dorthin
	 * @param richtung n/e/s/w
	 */
	public void schaueRichtung(char richtung) {
		String[] cellStatusArray = getCellStatus(richtung).split(" ");
		if (bot.getAktuellesFeld().getFeld(richtung) == null) {
			if (!cellStatusArray[0].equals("WALL")) {
				this.erstellFeld(richtung);
			}
		}
		//Ziel liegt in Blickrichtung 
		if (cellStatusArray[0].equals("FINISH") && cellStatusArray[1].equals(bot.getPlayerId() + "")) {
			anzahlFormulare = Integer.parseInt(cellStatusArray[2]);
			if ( (formcounter-1) == anzahlFormulare) {
				//alle Formulare gefunden
				allesGesammelt = true;
				bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), bot.getAktuellesFeld().getFeld(richtung)));
				this.ausgabe = bot.move();
				rundeZuEnde = true;
			} else {
				//speichert Zielfeld 
				spielfeld.setZielfeld(bot.getAktuellesFeld().getFeld(richtung));
			}
		}
		//Formular liegt in Blickrichtung 
		if (cellStatusArray[0].equals("FORM") && cellStatusArray[1].equals(bot.getPlayerId()+ "")) {
			formID = Integer.parseInt(cellStatusArray[2]);
			if (forms[formID] == null) {
				//speichert sich Feld mit Formular
				Formular formular = new Formular(formID, bot.getAktuellesFeld().getFeld(richtung));
				forms[formID] = formular;
			}
			//setzt Route auf passendes Formular in Blickrichtung
			if (formID == formcounter) {
				bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), bot.getAktuellesFeld().getFeld(richtung)));
				this.ausgabe = bot.move();
				rundeZuEnde = true;
			}
		}
		//sheet liegt in Blickrichtung
		if (cellStatusArray[0].equals("SHEET")) {
			spielfeld.getSheetList().add(bot.getAktuellesFeld().getFeld(richtung));
		}
	}
	
	/***
	 * Diese Methode gibt an, wie viele Formulare der Bot bereits entdeckt hat?
	 * @return i Anzahl entdeckter Formulare
	 */
	public int howManyForms() {
		int i=0;
		for (Formular f:forms) {
			if (f != null) {
				i++;
			}
		}
		return i;
	}

	/**
	 * Gibt Cellstati der jeweiligen Richtungsfelder zurück
	 * @param richtung
	 * @return gibt den Status zurück oder bei keinem passenden Status "kein Status"
	 */
	public String getCellStatus(char richtung){
		switch(richtung) {
		case 'n':
			return this.northCellStatus;
		case 'e':
			return this.eastCellStatus;
		case 's':
			return this.southCellStatus;
		case 'w':
			return this.westCellStatus;
		default: 
			return "kein Status";			
		}
		
	}
	/**
	 * Dies ist eine Methode welche bei Bedarf ein Feld in entsprechender Richtung erstellt und die X/Y Koordinaten setzt
	 * @param richtungFeldErstellen n/e/s/w 
	 */
	public void erstellFeld(char richtungFeldErstellen) {
		int wohinx = 0;
		int wohiny = 0;
		switch (richtungFeldErstellen) {
		//setzt die Koordinaten und beachtet dabei Spielfeldgrenzen
		case 'n':
			if (bot.getBotY() - 1 < 0) {
				wohinx = bot.getBotX();
				wohiny = spielfeld.getSizeY() - 1;
				break;
			} else {
				wohinx = bot.getBotX();
				wohiny = bot.getBotY() - 1;
				break;
			}
		case 'e':
			if (bot.getBotX() + 1 == spielfeld.getSizeX()) {
				wohinx = 0;
				wohiny = bot.getBotY();
				break;
			} else {
				wohinx = bot.getBotX() + 1;
				wohiny = bot.getBotY();
				break;
			}
		case 's':
			if (bot.getBotY() + 1 == spielfeld.getSizeY()) {
				wohinx = bot.getBotX();
				wohiny = 0;
				break;
			} else {
				wohinx = bot.getBotX();
				wohiny = bot.getBotY() + 1;
				break;
			}
		case 'w':
			if (bot.getBotX() - 1 < 0) {
				wohinx = spielfeld.getSizeX() - 1;
				wohiny = bot.getBotY();
				break;
			} else {
				wohinx = bot.getBotX() - 1;
				wohiny = bot.getBotY();
				break;
			}
		}

		// prüfen ob das Feld exisitert
		boolean feldExistiert = false;
		Feld temp = null;
		for (Feld feld : spielfeld.getFelder()) {
			if (feld.getxKoordinate() == wohinx && feld.getyKoordinate() == wohiny) {
				feldExistiert = true;
				temp = feld;
				break;
			}
		}
		//wenn das Feld existiert, nur das aktuelle Feld mit diesem verlinken
		if (feldExistiert == true) {
			switch (richtungFeldErstellen) {
			case 'n':
				bot.getAktuellesFeld().setNorth(temp);
				temp.setSouth(bot.getAktuellesFeld());
				break;
			case 'e':
				bot.getAktuellesFeld().setEast(temp);
				temp.setWest(bot.getAktuellesFeld());
				break;
			case 's':
				bot.getAktuellesFeld().setSouth(temp);
				temp.setNorth(bot.getAktuellesFeld());
				break;
			case 'w':
				bot.getAktuellesFeld().setWest(temp);
				temp.setEast(bot.getAktuellesFeld());
				break;
			}
		} else {
			// wenn das Feld nicht existiert, wird es angelegt
			Feld neuesFeld = new Feld(wohinx, wohiny);
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
}
