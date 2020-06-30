package de.vitbund.vitmaze.players;

import java.util.Scanner;

import de.vitbund.vitmaze.spielfeld.Feld;
import de.vitbund.vitmaze.spielfeld.Spielfeld;

/**
 * Klasse eines minimalen Bots für das VITMaze
 * @author Patrick.Stalljohann
 * @version 1.0
 *
 */
public class MinimalBot {
    
	Spielfeld spielfeld;
	Feld aktuellesFeld;
	Scanner input;
	byte blickrichtung; // 1 Norden, 2 Osten, 3 Süden, 4 Westen
	int playerId;
	int startX;
	int startY;
	boolean hatZiel;
	String lastActionsResult;
	String currentCellStatus;
	String northCellStatus;
	String eastCellStatus;
	String southCellStatus;
	String westCellStatus;
	
	public MinimalBot() {
		this.spielfeld = new Spielfeld();
		this.input = new Scanner(System.in);
		this.spielfeld.setSizeX(this.input.nextInt()); // X-Groesse des Spielfeldes (Breite)
		this.spielfeld.setSizeY(this.input.nextInt()); // Y-Groesse des Spielfeldes (Hoehe)
		this.spielfeld.setLevel(this.input.nextInt()); // Level des Matches
		
		this.input.nextLine();
		this.playerId = this.input.nextInt(); // id dieses Players / Bots
		this.startX = this.input.nextInt(); // X-Koordinate der Startposition dieses Player
		this.startY = this.input.nextInt(); // Y-Koordinate der Startposition dieses Players
		this.input.nextLine();
		this.aktuellesFeld = new Feld();
		this.spielfeld.addFeld(this.aktuellesFeld);
		this.blickrichtung = 4;
		this.hatZiel = false;
	}
	
	public void tueWas() {
		while(this.input.hasNext()) {
			this.lastActionsResult = this.input.nextLine();
			this.currentCellStatus = this.input.nextLine();
			this.northCellStatus = this.input.nextLine();
			this.eastCellStatus = this.input.nextLine();
			this.southCellStatus = this.input.nextLine();
			this.westCellStatus = this.input.nextLine();
			
			if (!this.hatZiel) {
				this.erkunden();
			}
			
		}
		this.input.close();
	}

	
	public void erkunden() {
		
		if (this.currentCellStatus.equals("FINISH " +this.playerId + " 0")) {
			System.out.println("finish");
		} else {
				
			// Umgebung anlegen wenn vorhanden
			if (this.northCellStatus.equals("FLOOR")) {
				Feld neuesFeld = new Feld();
				this.aktuellesFeld.setNorth(neuesFeld);
				neuesFeld.setSouth(this.aktuellesFeld);
				this.spielfeld.addFeld(neuesFeld);
				spielfeld.addUnbekanntesFeld(neuesFeld);
			}
			if (this.eastCellStatus.equals("FLOOR")) {
				Feld neuesFeld = new Feld();
				this.aktuellesFeld.setEast(neuesFeld);
				neuesFeld.setWest(this.aktuellesFeld);
				this.spielfeld.addFeld(neuesFeld);
				spielfeld.addUnbekanntesFeld(neuesFeld);
			}
			if (this.southCellStatus.equals("FLOOR")) {
				Feld neuesFeld = new Feld();
				this.aktuellesFeld.setSouth(neuesFeld);
				neuesFeld.setNorth(this.aktuellesFeld);
				this.spielfeld.addFeld(neuesFeld);
				spielfeld.addUnbekanntesFeld(neuesFeld);
			}
			if (this.westCellStatus.equals("FLOOR")) {
				Feld neuesFeld = new Feld();
				this.aktuellesFeld.setWest(neuesFeld);
				neuesFeld.setEast(this.aktuellesFeld);
				this.spielfeld.addFeld(neuesFeld);
				spielfeld.addUnbekanntesFeld(neuesFeld);
			}
			
			// hier intelligente Möglichkeiten ausdenken zur Wegfindung
			boolean hatSichbewegt=false;
			while (hatSichbewegt==false) {
				if (this.blickrichtung == 4 &&   !this.westCellStatus.equals("WALL")) {
					this.aktuellesFeld = this.aktuellesFeld.getWest();
					System.out.println("go west");	
					hatSichbewegt=true;
				} else 	if (this.blickrichtung == 3 &&  !this.southCellStatus.equals("WALL")) {
					System.out.println("go south");	
					this.aktuellesFeld = this.aktuellesFeld.getSouth();
					hatSichbewegt=true;
				} else 	if (this.blickrichtung == 2 &&  !this.eastCellStatus.equals("WALL")) {
					System.out.println("go east");	
					this.aktuellesFeld = this.aktuellesFeld.getEast();
					hatSichbewegt=true;
				} else 	if (this.blickrichtung == 1 &&  !this.northCellStatus.equals("WALL")) {
					System.out.println("go north");	
					this.aktuellesFeld = this.aktuellesFeld.getNorth();
					hatSichbewegt=true;
				} else {
				aendereBlickrichtung();}
			}
			
			
		}
		
	}
	
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
	
	/**
	 * Hauptmethode zum Ausführen des Bots
	 * @param args
	 */
	public static void main(String[] args) {
		// Scanner zum Auslesen der Standardeingabe, welche Initialisierungs- und Rundendaten liefert
		MinimalBot unserBot = new MinimalBot();
		
		unserBot.tueWas();
				
	}

}
