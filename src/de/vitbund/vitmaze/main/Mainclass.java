package de.vitbund.vitmaze.main;


import java.util.Scanner;

import de.vitbund.vitmaze.eingabe.Eingabe;
import de.vitbund.vitmaze.players.Bot_Level12;
import de.vitbund.vitmaze.spielfeld.Feld;
import de.vitbund.vitmaze.spielfeld.Spielfeld;

public class Mainclass {
	
	Feld aktuellesFeld;
	byte blickrichtung; // 1 Norden, 2 Osten, 3 Süden, 4 Westen
	String lastActionsResult;
	String currentCellStatus;
	String northCellStatus;
	String eastCellStatus;
	String southCellStatus;
	String westCellStatus;
	boolean hatZiel;
	
	public static void main(String[] args) {
		
		
	}
	
	public void init() {
		// Spielfeld anlegen und Startdaten setzen
		Spielfeld spielfeld = new Spielfeld();
		spielfeld.setSizeX(Eingabe.leseZahl()); // X-Groesse des Spielfeldes (Breite)
		spielfeld.setSizeY(Eingabe.leseZahl()); // Y-Groesse des Spielfeldes (Hoehe)
		spielfeld.setLevel(Eingabe.leseZahl()); // Level des Matches
		Eingabe.leseZeile();

		// Bot anlegen und Startdaten setzen
		Bot_Level12 newBot = new Bot_Level12();
		newBot.setPlayerId(Eingabe.leseZahl());// id dieses Players / Bots
		newBot.setStartX(Eingabe.leseZahl());// X-Koordinate der Startposition dieses Player
		newBot.setStartY(Eingabe.leseZahl()); // Y-Koordinate der Startposition dieses Players
		Eingabe.leseZeile();

		//Startfeld anlegen und zum Spielfeld hinzufügen
		aktuellesFeld = new Feld();
		spielfeld.addFeld(aktuellesFeld);
		
		blickrichtung = 4;
		hatZiel = false;	
	}
	
	public void getStati() {
		while(Eingabe.isnaechsterEintragvorhanden()) {
			this.lastActionsResult = Eingabe.leseZeile();
			this.currentCellStatus = Eingabe.leseZeile();
			this.northCellStatus = Eingabe.leseZeile();
			this.eastCellStatus = Eingabe.leseZeile();
			this.southCellStatus = Eingabe.leseZeile();
			this.westCellStatus = Eingabe.leseZeile();
			
			if (!hatZiel) {
				erkunden();
			}
			
		}
	}
	
	public void erkunden() {
		
	}
	
	
}
