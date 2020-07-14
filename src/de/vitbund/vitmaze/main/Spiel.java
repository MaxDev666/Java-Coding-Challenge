package de.vitbund.vitmaze.main;

import java.util.List;

import javax.sound.midi.SysexMessage;

import de.vitbund.vitmaze.eingabe.Eingabe;
import de.vitbund.vitmaze.players.Standardbot;
import de.vitbund.vitmaze.spielfeld.Feld;
import de.vitbund.vitmaze.spielfeld.Formular;
import de.vitbund.vitmaze.spielfeld.Spielfeld;

public class Spiel {

	Spielfeld spielfeld;
	Standardbot bot;
	int anzahlFormulare;
	
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
	int formID;
	int formcounter;
	boolean formFound;
	boolean allesGesammelt;
	boolean zugvorbei;
	
	// Klasse Formular noch anlegen
	// int id
	// Feld feld
	// String aufheben
	
	public int howManyForms() {
		int i=0;
		for (Formular f:forms) {
			if (f != null) {
				i++;
			}
		}
		return i;
	}
	
	
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
		forms = new Formular[9];
		for (Formular bla : forms) {
			bla = null;
		}
		formID=0;
		formcounter = 1;
		formFound = false;
		allesGesammelt = false;
		ausgabe = new String("position");
		
	}
	
	public void getStati() {
		

		this.lastActionsResult = Eingabe.leseZeile();
		this.currentCellStatus = Eingabe.leseZeile();
		this.northCellStatus = Eingabe.leseZeile();
		this.eastCellStatus = Eingabe.leseZeile();
		this.southCellStatus = Eingabe.leseZeile();
		this.westCellStatus = Eingabe.leseZeile();
		
			
		this.erkunden();


		bot.getUpdate();
		
		System.err.println("Ich sage: " + ausgabe);
		System.out.println(ausgabe);

		
		
	}
	
	/*
	 * 	haben wir ein Ziel?
	 * 		haben wir alle formulare?
	 * 			-> gehe zu Ziel
	 * 		nein: wissen wir wo Formulare sind?
	 * 			-> sammeln gehen
	 * 			nein: erkunde und finde Formulare
	 */
	public void erkunden() {
		zugvorbei = false;
		if (!allesGesammelt) {
			if ( bot.getAktuellesFeld().getNorth()==null) {
				if (this.northCellStatus.equals("FLOOR") || this.northCellStatus.startsWith("FINISH ") || this.northCellStatus.startsWith("FORM ")) {
					this.erstellFeld('n');
				}
				if (this.northCellStatus.startsWith("FINISH " +bot.getPlayerId())) {
					anzahlFormulare =Integer.parseInt(String.valueOf(this.northCellStatus.charAt(this.northCellStatus.length()-1)));
					spielfeld.setZielfeld(bot.getAktuellesFeld().getNorth());
				}
	
				if(this.northCellStatus.startsWith("FORM " +bot.getPlayerId())) {
					formID = Integer.parseInt(String.valueOf(this.northCellStatus.charAt(this.northCellStatus.length()-1)));
					System.err.println(this.northCellStatus.charAt(this.northCellStatus.length()-1) + " " + formID);
					//Aufnehmen muss noch überarbeitet werden
					if (forms[formID]==null) {
						Formular formular = new Formular(formID, bot.getAktuellesFeld().getNorth());
						forms[formID] = formular; 
					}
					
					if (formID==formcounter) {
						formFound = true;
						bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), forms[formID].getFeld()));
						
					}	
				}
			}
			if (bot.getAktuellesFeld().getEast()==null) {
				if (this.eastCellStatus.equals("FLOOR") || this.eastCellStatus.startsWith("FINISH ")|| this.eastCellStatus.startsWith("FORM ")) {
					this.erstellFeld('e');
	
				}
				if (this.eastCellStatus.startsWith("FINISH " +bot.getPlayerId())) {
					anzahlFormulare =Integer.parseInt(String.valueOf(this.eastCellStatus.charAt(this.eastCellStatus.length()-1)));
					spielfeld.setZielfeld(bot.getAktuellesFeld().getEast());
				}
	
				if(this.eastCellStatus.startsWith("FORM " +bot.getPlayerId())) {
	
					formID = Integer.parseInt(String.valueOf(this.eastCellStatus.charAt(this.eastCellStatus.length()-1)));
					System.err.println("Im Osten ist ein Formular Nummer " + formID + " aktuell suche ich " + formcounter);
					
					//Aufnehmen muss noch überarbeitet werden
					if (forms[formID]==null) {
						Formular formular = new Formular(formID, bot.getAktuellesFeld().getEast());
						forms[formID] = formular; 
					}
					
					if (formID==formcounter) {
						System.err.println("ICH LAUFE ZUM FORMULAR");
						formFound = true;
						bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), forms[formID].getFeld()));
						
					}	
				}
			}
			if (bot.getAktuellesFeld().getSouth()==null) {
				if (this.southCellStatus.equals("FLOOR") ||  this.southCellStatus.startsWith("FINISH ")|| this.southCellStatus.startsWith("FORM ")) {
					this.erstellFeld('s');
	
				}
				if (this.southCellStatus.startsWith("FINISH " +bot.getPlayerId())) {
					anzahlFormulare =Integer.parseInt(String.valueOf(this.southCellStatus.charAt(this.southCellStatus.length()-1)));
					spielfeld.setZielfeld(bot.getAktuellesFeld().getSouth());
				}
	
				if(this.southCellStatus.startsWith("FORM " +bot.getPlayerId())) {
					formID = Integer.parseInt(String.valueOf(this.southCellStatus.charAt(this.southCellStatus.length()-1)));
					//Aufnehmen muss noch überarbeitet werden
					if (forms[formID]==null) {
						Formular formular = new Formular(formID, bot.getAktuellesFeld().getSouth());
						forms[formID] = formular; 
					}
					
					if (formID==formcounter) {
						formFound = true;
						bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), forms[formID].getFeld()));
						
					}	
				}
			}
			if (bot.getAktuellesFeld().getWest()==null) {
				if (this.westCellStatus.equals("FLOOR")||  this.westCellStatus.startsWith("FINISH ")|| this.westCellStatus.startsWith("FORM ")) {
					this.erstellFeld('w');
				}
				if (this.westCellStatus.startsWith("FINISH " +bot.getPlayerId())) {
					anzahlFormulare =Integer.parseInt(String.valueOf(this.westCellStatus.charAt(this.westCellStatus.length()-1)));
					spielfeld.setZielfeld(bot.getAktuellesFeld().getWest());
				}
	
				if(this.westCellStatus.startsWith("FORM " +bot.getPlayerId())) {
					formID = Integer.parseInt(String.valueOf(this.westCellStatus.charAt(this.westCellStatus.length()-1)));
					//Aufnehmen muss noch überarbeitet werden
					if (forms[formID]==null) {
						Formular formular = new Formular(formID, bot.getAktuellesFeld().getWest());
						forms[formID] = formular; 
					}
					if (formID==formcounter) {
						formFound = true;
						bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), forms[formID].getFeld()));
						
					}	
				}
			}
			bot.getUpdate();
	
			// wenn ich alle Formulare habe
	

			

			
			if (this.currentCellStatus.equals("FORM " + bot.getPlayerId() + " " + formcounter )) {
				this.ausgabe = bot.take();
				zugvorbei = true;
				if (formcounter==howManyForms()) {
					allesGesammelt = true;
					System.err.println("HABE ALLES GESAMMELT UND GEHE ZUM ZIEL ZU FELD " + spielfeld.getZielfeld());
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(),  spielfeld.getZielfeld()));
					bot.getUpdate();
				} else {
					formcounter++;
				}
			}
			
			if (bot.hatRoute()==false) {
				if (zugvorbei==false) {
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(0)));
					this.ausgabe = bot.move();
					zugvorbei = true;
				}
			} else {
				if (zugvorbei==false) {
					// hier abfragen ob ich am Ende der Route bin und ob 
					// forms[formcounter].getFeld() == bot.getAktuellesFeld() && !this.currentCellStatus.equals("FORM " + bot.getPlayerId() + " " + formcounter )
					// dann weiss ich ich bin richtig aber das Formular ist nicht da
					// hier muss ich jetzt alle Nachbarfelder meines Feldes in die aktuelle Route kriegen  und am besten deren Nachbarn
					
					
					this.ausgabe = bot.move();
					zugvorbei = true;
				}
			}
			
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
				if (bot.getBotY()-1 <0) {
					wohinx = bot.getBotX();
					wohiny= bot.getBotY() - 1 +spielfeld.getSizeY();
					break;					
				} else {
					wohinx=bot.getBotX();
					wohiny = bot.getBotY() - 1;
					break;
				}
			case 'e':
				if (bot.getBotX()+1 > spielfeld.getSizeX()) {
					wohinx = bot.getBotX() +1 - spielfeld.getSizeX();
					wohiny= bot.getBotY() ;
					break;					
				} else {
					wohinx=bot.getBotX() +1;
					wohiny = bot.getBotY();
					break;
				}
			case 's':
				if (bot.getBotY()+1 > spielfeld.getSizeY()) {
					wohinx = bot.getBotX();
					wohiny= bot.getBotY() + 1 -spielfeld.getSizeY();
					break;					
				} else {
					wohinx=bot.getBotX();
					wohiny = bot.getBotY() + 1;
					break;
				}
			case 'w':
				if (bot.getBotX()-1 <0) {
					wohinx = bot.getBotX() -1 + spielfeld.getSizeX();
					wohiny= bot.getBotY();
					break;					
				} else {
					wohinx=bot.getBotX()-1;
					wohiny = bot.getBotY();
					break;
				}
			}
			
			System.err.println("Will Feld bei x:" + wohinx + "|" + wohiny + "anlegen");
			
				
				// prüfen ob das FEld exisitert
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
			
			

	
	

