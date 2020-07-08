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
		
	}
	
	public void getStati() {
		

		this.lastActionsResult = Eingabe.leseZeile();
		this.currentCellStatus = Eingabe.leseZeile();
		this.northCellStatus = Eingabe.leseZeile();
		this.eastCellStatus = Eingabe.leseZeile();
		this.southCellStatus = Eingabe.leseZeile();
		this.westCellStatus = Eingabe.leseZeile();
		
		//System.err.println(		this.lastActionsResult + "|"+ 		this.currentCellStatus + "|"+		this.northCellStatus +"|"+		this.eastCellStatus+"|"+		this.southCellStatus+"|"+		this.westCellStatus );
		
		this.erkunden();

		//System.err.println("Jetze soll ich mich bewegen von: " + bot.getAktuellesFeld() + " nach " + bot.getAktuelleRoute().get(0));
		bot.move();
		bot.getUpdate();
		
		// hier noch abfragen ob wir am Ziel sind und irgendwas aufnehmen müssen oder ob wir was auf dem Weg gefunden haben
		

		//bot.getUpdate();
		//System.err.println("x:" + bot.getBotX() + " y:" + bot.getBotY());
		
		
	}
	
	
	public void erkunden() {

		if (this.currentCellStatus.equals("FINISH " +bot.getPlayerId()+ " 0")) {
			hatZiel=true;
			System.out.println("finish");
		} 
		else {
				
			if ( bot.getAktuellesFeld().getNorth()==null) {
				if (this.northCellStatus.equals("FLOOR") || this.northCellStatus.equals("FINISH " +bot.getPlayerId()+ " 0") || this.northCellStatus.equals("FORM " +bot.getPlayerId())) {
					this.richtungFeldErstellen='n';
					this.erstellFeld();
				}
				if (this.northCellStatus.startsWith("FINISH ")) {
					anzahlFormulare =(int)(this.northCellStatus.charAt(this.northCellStatus.length()));
				}
					
				if(this.northCellStatus.equals("FORM " +bot.getPlayerId())) {
					spielfeld.getFormularFelder().add(bot.getAktuellesFeld().getNorth());
				}
			}
			if (bot.getAktuellesFeld().getEast()==null) {
				if (this.eastCellStatus.equals("FLOOR") || this.eastCellStatus.equals("FINISH " +bot.getPlayerId()+ " 0")) {
					this.richtungFeldErstellen='e';
					this.erstellFeld();

				}
				if (this.eastCellStatus.equals("FINISH " +bot.getPlayerId()+ " 0")) {
					spielfeld.getZielfeld().add(bot.getAktuellesFeld().getEast());
				}
					
				if(this.eastCellStatus.equals("FORM " +bot.getPlayerId())) {
					spielfeld.getFormularFelder().add(bot.getAktuellesFeld().getEast());
				}
			}
			if (bot.getAktuellesFeld().getSouth()==null) {
				if (this.southCellStatus.equals("FLOOR") ||  this.southCellStatus.equals("FINISH " +bot.getPlayerId()+ " 0")) {
					this.richtungFeldErstellen='s';
					this.erstellFeld();

				}
				if (this.southCellStatus.equals("FINISH " +bot.getPlayerId()+ " 0")) {
					spielfeld.getZielfeld().add(bot.getAktuellesFeld().getSouth());
				}
					
				if(this.southCellStatus.equals("FORM " +bot.getPlayerId())) {
					spielfeld.getFormularFelder().add(bot.getAktuellesFeld().getSouth());
				}
			}
			if (bot.getAktuellesFeld().getWest()==null) {
				if (this.westCellStatus.equals("FLOOR")||  this.westCellStatus.equals("FINISH " +bot.getPlayerId()+ " 0")) {
					this.richtungFeldErstellen='w';
					this.erstellFeld();
				}
				if (this.westCellStatus.equals("FINISH " +bot.getPlayerId()+ " 0")) {
					spielfeld.getZielfeld().add(bot.getAktuellesFeld().getWest());
				}
					
				if(this.westCellStatus.equals("FORM " +bot.getPlayerId())) {
					spielfeld.getFormularFelder().add(bot.getAktuellesFeld().getWest());
				}
			}
			bot.getUpdate();

			if (bot.hatRoute()==false) {

				bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(0)));
			}


			
			
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

	public void erstellFeld() {
		
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
			
			

	
	

