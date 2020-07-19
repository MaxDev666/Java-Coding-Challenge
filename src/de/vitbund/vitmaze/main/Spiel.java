package de.vitbund.vitmaze.main;

import java.util.List;
import java.util.Random;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

import de.vitbund.vitmaze.eingabe.Eingabe;
import de.vitbund.vitmaze.players.Standardbot;
import de.vitbund.vitmaze.spielfeld.Feld;
import de.vitbund.vitmaze.spielfeld.Formular;
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
	Random wuerfel;
	boolean rundeZuEnde;
	
	// Klasse Formular noch anlegen
	// int id
	// Feld feld
	// String aufheben
	
	
	/***
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
	
	
	public void init() {
		// Spielfeld anlegen und Startdaten setzen
		wuerfel = new Random();
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
		if (spielfeld.getLevel()==5) {
			bot.setSheetCount(Eingabe.leseZahl());
			System.err.println(bot.getSheetCount() + " Sheets habe ich");
		}
		Eingabe.leseZeile();

		
		Feld temp =new Feld(bot.getBotX(),bot.getBotY());
		// Startfeld anlegen und zum Spielfeld hinzuf�gen
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
	
	
	public void schaueRichtung(char richtung){
		String[] cellStatusArray = getCellStatus(richtung).split(" ");
		System.err.println(cellStatusArray[0]);
		if (bot.getAktuellesFeld().getFeld(richtung) == null) {
			if (!cellStatusArray[0].equals("WALL")) {
				this.erstellFeld(richtung);
			}
		}	
		
		if (cellStatusArray[0].equals("FORM") && cellStatusArray[1].equals(bot.getPlayerId()+"")) {
			formID = Integer.parseInt(cellStatusArray[2]);
			
			if (forms[formID] == null) {
				Formular formular = new Formular(formID, bot.getAktuellesFeld().getFeld(richtung));
				forms[formID] = formular;
			}

			if (formID == formcounter ) {
				bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), forms[formID].getFeld()));
				this.ausgabe = bot.move();
				rundeZuEnde = true;
			}
		}
		
		if (cellStatusArray[0].equals("SHEET") ) {
			spielfeld.getSheetList().add(bot.getAktuellesFeld().getFeld(richtung));
		}
			
		if (cellStatusArray[0].equals("FINISH") && cellStatusArray[1].equals(bot.getPlayerId()+"")) {
			anzahlFormulare = Integer.parseInt(cellStatusArray[2]);
			if (anzahlFormulare == howManyForms()) {
				allesGesammelt = true;
				System.err.println("HABE ALLES GESAMMELT UND GEHE ZUM ZIEL ZU FELD " + spielfeld.getZielfeld());
				bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(),  bot.getAktuellesFeld().getFeld(richtung)));
				bot.getUpdate();
				this.ausgabe = bot.move();
				rundeZuEnde = true;
			}else {
				spielfeld.setZielfeld(bot.getAktuellesFeld().getFeld(richtung));
			}
		}		
}

	public void getStati() {
	rundeZuEnde = false;

	this.lastActionsResult = Eingabe.leseZeile();
	this.currentCellStatus = Eingabe.leseZeile();
	this.northCellStatus = Eingabe.leseZeile();
	this.eastCellStatus = Eingabe.leseZeile();
	this.southCellStatus = Eingabe.leseZeile();
	this.westCellStatus = Eingabe.leseZeile();
	
	//get Last Action auswerten
		
	erkunden();
	
	if (rundeZuEnde == false) {
		if (bot.hatRoute()) {
			this.ausgabe = bot.move();
			rundeZuEnde = true;			
		} else {
			// �berlegen ob das passt
			if (spielfeld.getZielfeld()!=null && anzahlFormulare == formcounter) {
				bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getZielfeld()));
				this.ausgabe = bot.move();
				rundeZuEnde = true;		
			} else if (forms[formcounter]!=null){
				if (forms[formcounter].getFeld() == bot.getAktuellesFeld()) {
					this.ausgabe = bot.take();
					System.err.println("Will nehmen");
					formcounter +=1;
					rundeZuEnde = true;
				} else {
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), forms[formcounter].getFeld()));
					this.ausgabe = bot.move();
					rundeZuEnde = true;	
				}
			} else if (!spielfeld.getUnbekannteFelder().isEmpty()) {
				bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(0)));
				this.ausgabe = bot.move();
				rundeZuEnde = true;					
			} else {
				// hier muss noch die Pr�fung rein wenn das Formular nicht an der Stelle zu finden ist oder 
				// eine Routine um einfach nochmal durch alle Felder gehen
				
			}
		}
	}
	
	System.out.println(this.ausgabe);
	
	}
	
	public void erkunden() {
		
		schaueRichtung( 'n');
		schaueRichtung('e'); 
		schaueRichtung ('s');
		schaueRichtung ('w');
		
		String[] aktFeld = this.currentCellStatus.split(" ");
		
		if (aktFeld[aktFeld.length-1].equals("!")) {
			if (bot.isRedetDieRunde() == false) {
				bot.setRedetDieRunde(true);
				rundeZuEnde = true;					
			} else {
				bot.setRedetDieRunde(true);
			}
		}

		if (rundeZuEnde == false) {
			if (aktFeld[0].equals("FORM")){
				if (aktFeld[1].equals(bot.getPlayerId()+"") && aktFeld[2].equals(formcounter)) {
					this.ausgabe = bot.take();
					formcounter +=1;
					rundeZuEnde = true;
				} else if (!aktFeld[1].equals(bot.getPlayerId()+"")) {
					if (bot.getSheetCount()>0) {
						bot.setSheetCount(bot.getSheetCount()-1);
						this.ausgabe = bot.put();
						rundeZuEnde = true;
					} else {
						// Problem zu schauen ob kicken in Ordunung war
						String tmp = bot.kick(this.northCellStatus, this.eastCellStatus, this.southCellStatus, this.westCellStatus);
						if (!tmp.equals("fail")) {
						this.ausgabe = tmp;
						rundeZuEnde = true;
						}
					}
				}
			}  else if (aktFeld[0].equals("SHEET")) {
				String tmp = bot.kick(this.northCellStatus, this.eastCellStatus, this.southCellStatus, this.westCellStatus);
				if (!tmp.equals("fail")) {
					this.ausgabe = tmp;
					rundeZuEnde = true;
			} else if (aktFeld[0].equals("FINISH")) {
					if (aktFeld[1].equals(bot.getPlayerId()+"")) {
						// Abfrage ob alle Formulare vorhanden etc.
						this.ausgabe = bot.finish();	
					}
				} 
			}
		}
		}

	
	
	
	
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
				System.err.println("S�dkoordinaten: "+bot.getBotY()+" "+spielfeld.getSizeY());
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
			
			

	
	

