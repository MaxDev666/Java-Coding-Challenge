package de.vitbund.vitmaze.main;

import java.util.List;

import de.vitbund.vitmaze.eingabe.Eingabe;
import de.vitbund.vitmaze.players.Standardbot;
import de.vitbund.vitmaze.spielfeld.Feld;
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
	
	
	// Klasse Formular noch anlegen
	// int id
	// Feld feld
	// String aufheben
	
//	Array formulare  {Formular  1 , 2 , 3  }
	
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
		// Startfeld anlegen und zum Spielfeld hinzuf�gen
		bot.setAktuellesFeld(temp);
		spielfeld.getBekannteFelder().add(temp);
		spielfeld.addFeld(bot.getAktuellesFeld());
		
		// Bot anlegen und Startdaten setzen
	

		hatZiel=false;
		anzahlFormulare=999;
		
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
			
			if ( bot.getAktuellesFeld().getNorth()==null) {
				if (this.northCellStatus.equals("FLOOR") || this.northCellStatus.startsWith("FINISH ") || this.northCellStatus.startsWith("FORM ")) {
					this.erstellFeld('n');
				}
				if (this.northCellStatus.startsWith("FINISH " +bot.getPlayerId())) {
					anzahlFormulare =(int)(this.northCellStatus.charAt(this.northCellStatus.length()));
					spielfeld.setZielfeld(bot.getAktuellesFeld().getNorth());
				}
					
				if(this.northCellStatus.startsWith("FORM " +bot.getPlayerId())) {
					spielfeld.getFormularFelder().add(bot.getAktuellesFeld().getNorth());
				}
			}
			if (bot.getAktuellesFeld().getEast()==null) {
				if (this.eastCellStatus.equals("FLOOR") || this.eastCellStatus.startsWith("FINISH ")|| this.eastCellStatus.startsWith("FORM ")) {
					this.erstellFeld('e');

				}
				if (this.eastCellStatus.startsWith("FINISH " +bot.getPlayerId())) {
					anzahlFormulare =(int)(this.eastCellStatus.charAt(this.eastCellStatus.length()));
					spielfeld.setZielfeld(bot.getAktuellesFeld().getEast());
				}
					
				if(this.eastCellStatus.startsWith("FORM " +bot.getPlayerId())) {
					spielfeld.getFormularFelder().add(bot.getAktuellesFeld().getEast());
				}
			}
			if (bot.getAktuellesFeld().getSouth()==null) {
				if (this.southCellStatus.equals("FLOOR") ||  this.southCellStatus.startsWith("FINISH ")|| this.southCellStatus.startsWith("FORM ")) {
					this.erstellFeld('s');

				}
				if (this.southCellStatus.startsWith("FINISH " +bot.getPlayerId())) {
					anzahlFormulare =(int)(this.southCellStatus.charAt(this.southCellStatus.length()));
					spielfeld.setZielfeld(bot.getAktuellesFeld().getSouth());
				}
					
				if(this.southCellStatus.startsWith("FORM " +bot.getPlayerId())) {
					spielfeld.getFormularFelder().add(bot.getAktuellesFeld().getSouth());
				}
			}
			if (bot.getAktuellesFeld().getWest()==null) {
				if (this.westCellStatus.equals("FLOOR")||  this.westCellStatus.startsWith("FINISH ")|| this.westCellStatus.startsWith("FORM ")) {
					this.erstellFeld('w');
				}
				if (this.westCellStatus.startsWith("FINISH " +bot.getPlayerId())) {
					anzahlFormulare =(int)(this.westCellStatus.charAt(this.westCellStatus.length()));
					spielfeld.setZielfeld(bot.getAktuellesFeld().getWest());
				}
					
				if(this.westCellStatus.startsWith("FORM " +bot.getPlayerId())) {
					spielfeld.getFormularFelder().add(bot.getAktuellesFeld().getWest());
				}
			}
			bot.getUpdate();

			if (bot.hatRoute()==false) {
				bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(0)));
				this.ausgabe = bot.move();
			} else {
				this.ausgabe = bot.move();
				
			}


			
			
		
	}
	
	/*		0	1	2	3
	 * 	0				
	 * 	1		X		
	 * 	2					
	 * 	3				
	 * 	4			
	 * 
	 */

	public void erstellFeld(char richtungFeldErstellen) {
		
			/*	feststellen wohin das Feld soll
			 * 	-> n e s w
			 * 	-> Koordianten ermitteln
			 * 
			 * 	pr�fen ob dort Feld exisitiert
			 * 
			 * 	feld verkn�pfen oder feld erstellen
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
			
				
				// pr�fen ob das FEld exisitert
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
			
			

	
	
