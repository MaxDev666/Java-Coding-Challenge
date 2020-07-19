package de.vitbund.vitmaze.main;

import java.util.List;

import de.vitbund.vitmaze.eingabe.Eingabe;
import de.vitbund.vitmaze.players.Standardbot;
import de.vitbund.vitmaze.spielfeld.Feld;
import de.vitbund.vitmaze.spielfeld.Formular;
import de.vitbund.vitmaze.spielfeld.Spielfeld;

/**
 * 
 * 
 */

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
	List<Feld> ziele;
	String ausgabe;
	Formular[] forms;
	int anzahlFormulare;
	String anzahlFormulareString;
	int formID;
	String formIDString;
	int formcounter;
	boolean formFound;
	boolean allesGesammelt;
	boolean zugvorbei;
	
	/**
	 * Wie viele Formulare hat der Bot bereits entdeckt?
	 * @return Anzahl entdeckter Formulare
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
	 * Initialisierungs-Methode um Spiel zu starten.
	 */
	public void init() {
		// Spielfeld anlegen und Startdaten setzen
		spielfeld = new Spielfeld();
		spielfeld.setSizeX(Eingabe.leseZahl()); // X-Groesse des Spielfeldes (Breite)
		spielfeld.setSizeY(Eingabe.leseZahl()); // Y-Groesse des Spielfeldes (Hoehe)
		spielfeld.setLevel(Eingabe.leseZahl()); // Level des Matches
		Eingabe.leseZeile();

		// Bote erstellen
		bot = new Standardbot(spielfeld);
		bot.setPlayerId(Eingabe.leseZahl());// id dieses Players / Bots
		bot.setBotX(Eingabe.leseZahl()); // X-Koordinate der Startposition dieses Player
		bot.setBotY(Eingabe.leseZahl()); // Y-Koordinate der Startposition dieses Players
		Eingabe.leseZeile();

		
		Feld temp =new Feld(bot.getBotX(),bot.getBotY());
		// Startfeld anlegen und zum Spielfeld hinzufügen
		bot.setAktuellesFeld(temp);
		spielfeld.getBekannteFelder().add(temp);
		spielfeld.addFeld(bot.getAktuellesFeld());
		
		// Bot anlegen und Startdaten setzen
		hatZiel=false;
		anzahlFormulare=999;
		anzahlFormulareString="";
		forms = new Formular[26];
		for (Formular form : forms) {
			form = null;
		}
		formID=0;
		formIDString="";
		formcounter = 1;
		formFound = false;
		allesGesammelt = false;
		ausgabe = new String("position");
		
	}
	//Getter für Stati der umliegenden Felder.
	public void getStati() {
		
		this.lastActionsResult = Eingabe.leseZeile();
		this.currentCellStatus = Eingabe.leseZeile();
		this.northCellStatus = Eingabe.leseZeile();
		this.eastCellStatus = Eingabe.leseZeile();
		this.southCellStatus = Eingabe.leseZeile();
		this.westCellStatus = Eingabe.leseZeile();
		
		// Ausgabe der letzen Aktion.
		System.err.println(this.lastActionsResult);
		
		// 
		switch (spielfeld.getLevel()) {
		case 1:
			this.erkunden();
			break;
		case 2:
			this.erkunden2();
			break;
		}


		bot.getUpdate();
		
		
		// hier Aktion ausführen
		System.err.println("Ich sage: " + ausgabe);
		System.out.println(ausgabe);

		
		
	}
	/**
	 * 
	 * @param richtung beschreibt ob das Feld im Norden/Osten/Süden odeer Westen liegt
	 * @return Gibt Status des jeweiligen Feldes zurück
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
	 * Erstellung eines Feldes in entsprechender Blickrichtung.
	 * @param richtung Himmelsrichtung in welcher das Feld liegt
	 */
	//den Status der Felder erhalten und dementsprechend Felder erstellen
	public void schauerichtung(char richtung) {
		if (bot.getAktuellesFeld().getFeld(richtung) == null) {
			if (getCellStatus(richtung).startsWith("FLOOR") || getCellStatus(richtung).startsWith("FINISH ")) {
				System.err.println(richtung  + " ist ein Feld");
				this.erstellFeld(richtung);
			}
			//bei Zielfeld, Route auf Ziel setzen
			if (getCellStatus(richtung).startsWith("FINISH " + bot.getPlayerId())) {
				spielfeld.setZielfeld(bot.getAktuellesFeld().getFeld(richtung));
				bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getZielfeld()));
			}
		}
	}
	/**
	 * Erstellen eiens Feldes in entsprechender Blickrichtung (mit Formularen).
	 * @param richtung Himmelsrichtung in welcher das Feld liegt
	 */
	//den Status der Felder erhalten und dementsprechend Felder erstellen
	public void schauerichtung2(char richtung) {
			if (bot.getAktuellesFeld().getFeld(richtung) == null) {
				if (getCellStatus(richtung).startsWith("FLOOR") || getCellStatus(richtung).startsWith("FORM ") || getCellStatus(richtung).startsWith("FINISH ")) {
					this.erstellFeld(richtung);
				}
			}
			//prüfen ob Ziel in Blickrichtung liegt
			if (getCellStatus(richtung).startsWith("FINISH " + bot.getPlayerId())) {
				anzahlFormulareString = getCellStatus(richtung).substring(getCellStatus(richtung).length()-2);
				
				/*if (anzahlFormulareString.charAt(0)==' ') {
					anzahlFormulare=Integer.parseInt(anzahlFormulareString.charAt(1)+"");
					System.err.println("Anzahl der Formulare ist " +anzahlFormulare);
				}else {*/
				
				//merken wieviele Formulare benötigt werden
					anzahlFormulare=Integer.parseInt(anzahlFormulareString.trim());
					System.err.println(getCellStatus(richtung));
					System.err.println("Anzahl der Formulare ist " +anzahlFormulare + " " +  anzahlFormulareString);
				//}
				
				//merken wo sich das Zielfeld befindet
				spielfeld.setZielfeld(bot.getAktuellesFeld().getFeld(richtung));
			}
			//prüfen ob Formular in Blickrichtung liegt
			if (getCellStatus(richtung).startsWith("FORM " + bot.getPlayerId())) {
				formIDString = getCellStatus(richtung).substring(getCellStatus(richtung).length()-2);
				//
				if (formIDString.charAt(0)==' ') {
					formID=(Character.getNumericValue(formIDString.charAt(1)));
				}else {
					formID=Integer.parseInt(formIDString);
				}
				System.err.println(getCellStatus(richtung).substring(getCellStatus(richtung).length()-2) + " " + formID);
				//gefundenes Formular instanzieren und in Liste formular schreiben
				if (forms[formID] == null) {
					Formular formular = new Formular(formID, bot.getAktuellesFeld().getFeld(richtung));
					forms[formID] = formular;
				}
				//Route auf aktuell aufzuhebendes Formular setzen 
				if (formID == formcounter) {
					formFound = true;
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), forms[formID].getFeld()));

				}
			}
		}
	
	/**
	 * Methode welche den Bot unbekannte Felder erkundne lässt oder wenn möglich das Spiel beendet
	 */
	//ruft Methode(schauerichtung) von oben für entsprechende Richtungen auf
	public void erkunden() {
				schauerichtung('n');
				schauerichtung('e');
				schauerichtung('s');
				schauerichtung('w');
				
			//sofern keine Route vorhanden, neue Route auf ein unbekanntes Feld setzen
			if (bot.hatRoute()==false) {
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(0)));
			} 
			//Wenn alles Zielbedingungen erfüllt sind das Spiel beenden
			if (spielfeld.getZielfeld() == bot.getAktuellesFeld()) {
				this.ausgabe = bot.finish();
			} 
			//fortführen der Arbeit
			else {
				this.ausgabe = bot.move();
			}


	}
	/**
	 * Methode welche nach Formularen in Blickrichtung prüft und diese wenn möglich aufnimmt
	 */
	public void erkunden2() {
		
		zugvorbei = false;
		if (!allesGesammelt) {
			schauerichtung2('n');
			schauerichtung2('e');
			schauerichtung2('s');
			schauerichtung2('w');
			bot.getUpdate();
			//Aufnahme des passenden Formulars
			if (this.currentCellStatus.equals("FORM " + bot.getPlayerId() + " " + formcounter )) {
				this.ausgabe = bot.take();
				zugvorbei = true;
				// prüfen ob alle Formulare gesammelt wurden und entsprechend Route auf Ziel setzen
				if (formcounter==howManyForms() && spielfeld.getZielfeld()!=null) {
					allesGesammelt = true;
					System.err.println("HABE ALLES GESAMMELT UND GEHE ZUM ZIEL ZU FELD " + spielfeld.getZielfeld());
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(),  spielfeld.getZielfeld()));
					bot.getUpdate();
				} 
				//Formularcounter um 1 erhöhen
				else {
					formcounter++;
				}
			}
			//sofern keine Route vorhanden, neue Route auf ein unbekanntes Feld setzen
			if (bot.hatRoute()==false) {
				if (zugvorbei==false) {
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(0)));
					this.ausgabe = bot.move();
				}
			} else {
				if (zugvorbei==false) {
					// hier abfragen ob ich am Ende der Route bin und ob 
					// forms[formcounter].getFeld() == bot.getAktuellesFeld() && !this.currentCellStatus.equals("FORM " + bot.getPlayerId() + " " + formcounter )
					// dann weiss ich ich bin richtig aber das Formular ist nicht da
					// hier muss ich jetzt alle Nachbarfelder meines Feldes in die aktuelle Route kriegen  und am besten deren Nachbarn
					
					
					this.ausgabe = bot.move();
				}
			}
			//Route auf nächstes zu sammelndes Formular setzen oder Ziel aufsuchen
			if (howManyForms()==anzahlFormulare) {
				if (!allesGesammelt) {
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), forms[formcounter].getFeld()));
				} else {
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(),  spielfeld.getZielfeld()));
				}
			}	
			
		} else {
			if (spielfeld.getZielfeld() == bot.getAktuellesFeld()) {
				this.ausgabe = bot.finish();
			} else {
				this.ausgabe = bot.move();
			}
		}


	}

	public void erstellFeld(char richtungFeldErstellen) {
		
			/*	feststellen wohin das Feld soll
			 * 	-> n e s w
			 * 	-> Koordianten ermitteln
			 * 
			 * 	prüfen ob dort Feld exisitiert
			 * 
			 * 	feld verknüpfen oder feld erstellen
			 * 
			 */
			int wohinx = 0;
			int wohiny = 0;
			switch (richtungFeldErstellen) {
			case 'n':
				System.err.println("case n");
				if (bot.getBotY()-1 <0) {
					wohinx = bot.getBotX();
					wohiny= spielfeld.getSizeY()-1;
					break;					
				} else {
					wohinx=bot.getBotX();
					wohiny = bot.getBotY() - 1;
					break;
				}
			case 'e':
				System.err.println("case e");
				if (bot.getBotX()+1 == spielfeld.getSizeX()) {
					System.err.println("Ostkoordinaten: "+bot.getBotX()+" "+spielfeld.getSizeX());
					wohinx = 0;
					wohiny= bot.getBotY() ;
					break;					
				} else {
					wohinx=bot.getBotX() +1;
					wohiny = bot.getBotY();
					break;
				}
			case 's':
				System.err.println("case s");
				System.err.println("Südkoordinaten: "+bot.getBotY()+" "+spielfeld.getSizeY());
				if (bot.getBotY()+1 == spielfeld.getSizeY()) {
					wohinx = bot.getBotX();
					wohiny= 0;
					break;					
				} else {
					wohinx=bot.getBotX();
					wohiny = bot.getBotY() + 1;
					break;
				}
			case 'w':
				System.err.println("case w");
				if (bot.getBotX()-1 <0) {
					System.err.println("Westkoordinaten: "+bot.getBotX()+" "+spielfeld.getSizeX());
					wohinx = spielfeld.getSizeX()-1;
					wohiny= bot.getBotY();
					break;					
				} else {
					wohinx=bot.getBotX()-1;
					wohiny = bot.getBotY();
					break;
				}
			}
			
			System.err.println("Will Feld bei x:" + wohinx + "|" + wohiny + "anlegen");
			
				
				// prüfen ob das Feld exisitert
			boolean feldExistiert =false;
			Feld temp = null;
			for (Feld feld : spielfeld.getFelder()) {
				if (feld.getxKoordinate()== wohinx && feld.getyKoordinate() == wohiny) {
					feldExistiert = true;
					temp = feld;
					break;
				}
			}
			System.err.println(feldExistiert);
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
				System.err.println("ich bin richtig");
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
			
			

	
	

