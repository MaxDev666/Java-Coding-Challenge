package de.vitbund.vitmaze.players;

import java.util.List;

import de.vitbund.vitmaze.main.Spiel;
import de.vitbund.vitmaze.spielfeld.Feld;
import de.vitbund.vitmaze.spielfeld.Spielfeld;

public class Standardbot {

	private int playerId;
	private int startX;
	Spielfeld spielfeld;
	private int startY;
	private Feld aktuellesFeld;
	public void setAktuellesFeld(Feld aktuellesFeld) {
		this.aktuellesFeld = aktuellesFeld;
	}

	public Feld getAktuellesFeld() {
		return aktuellesFeld;
	}


	private List<Feld> aktuelleRoute;
	
		
	public static void main (String[] args) {
		Spiel neuesSpiel = new Spiel();
		neuesSpiel.init();
		while (true) {
			neuesSpiel.getStati();
		}
	}
	
	public boolean hatRoute() {
		if (this.getAktuelleRoute() != null) {
			if (this.getAktuelleRoute().isEmpty()==false) {
				return true;
			}
		}
		return false;
		}
	
	public List<Feld> getAktuelleRoute() {
		return aktuelleRoute;
	}

	public void setAktuelleRoute(List<Feld> aktuelleRoute) {
		this.aktuelleRoute = aktuelleRoute;
	}

	public Standardbot(Spielfeld spielfeld) {
		this.spielfeld = spielfeld;
	}
	
	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}
	
	public void move() {
		System.err.println("bin in Move Routine");
		Feld zuFeld = new Feld( );
		zuFeld = this.getAktuelleRoute().get(0);
		System.err.println("ZuFeld: " + zuFeld);
		System.err.println("VonFeld: " + this.getAktuellesFeld());
		switch (this.getAktuellesFeld().getRichtung(zuFeld)){
			case "north":
				this.getAktuelleRoute().remove(this.getAktuellesFeld());
				this.goNorth();
				break;
			case "south":
				this.getAktuelleRoute().remove(this.getAktuellesFeld());
				this.goSouth();
				break;
			case "east":
				this.getAktuelleRoute().remove(this.getAktuellesFeld());
				this.goEast();
				break;
			case "west":
				this.getAktuelleRoute().remove(this.getAktuellesFeld());
				this.goWest();
				break;
		}
		if (spielfeld.getUnbekannteFelder().contains(this.getAktuellesFeld())) {
			spielfeld.getUnbekannteFelder().remove(this.getAktuellesFeld());
		}
		if (this.getAktuellesFeld() == zuFeld) {
			this.getAktuelleRoute().remove(this.getAktuellesFeld());
		}
		
	}
	
	public void goWest() {
		this.setAktuellesFeld(this.getAktuellesFeld().getWest());
		System.out.println("go west");	
	}
	
	public void getUpdate() {
		System.err.println("###################################################################");
		System.err.println("Ich stehe am Anfang des Zuges auf dem Feld: " + this.aktuellesFeld);
		System.err.println("Im Norden ist: " + this.aktuellesFeld.getNorth());
		System.err.println("Im Osten ist: " + this.aktuellesFeld.getEast());
		System.err.println("Im Süden ist : " + this.aktuellesFeld.getSouth());
		System.err.println("Im Westen ist: " + this.aktuellesFeld.getWest());
		if (this.hatRoute()) {
		System.err.println("Ich will zum Feld: " + aktuelleRoute.get(0));
		} else { System.err.println("Ich habe noch keine Route");}
		System.err.println("###################################################################");
		
	}
	
	public void goNorth() {
		this.aktuellesFeld = this.aktuellesFeld.getNorth();
		System.out.println("go north");	
	}
	
	public void goEast() {
		this.aktuellesFeld = this.aktuellesFeld.getEast();
		System.out.println("go east");
	}
	
	public void goSouth() {
		this.aktuellesFeld = this.aktuellesFeld.getSouth();
		System.out.println("go south");	
	}
}
