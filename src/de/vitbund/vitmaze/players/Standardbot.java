package de.vitbund.vitmaze.players;

import java.util.List;
import java.util.Scanner;

import de.vitbund.vitmaze.main.Spiel;
import de.vitbund.vitmaze.spielfeld.Feld;

public class Standardbot {

	private int playerId;
	private int startX;
	private int startY;
	Feld aktuellesFeld;
	private List<Feld> aktuelleRoute;
	
		
	public static void main (String[] args) {
		Spiel neuesSpiel = new Spiel();
		neuesSpiel.init();
		while (true) {
			neuesSpiel.getStati();
		}
	}
	
	public boolean hatRoute() {
		if (aktuelleRoute != null) {
			if (aktuelleRoute.isEmpty()!=true) {
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

	public Standardbot(Feld feld) {
		this.aktuellesFeld=feld;
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
		for (Feld zuFeld : aktuelleRoute) {
		switch (aktuellesFeld.getRichtung(zuFeld)){
			case "north":
				this.goNorth();
			case "south":
				this.goSouth();
			case "east":
				this.goEast();
			case "west":
				System.err.println("ich will ja gehen");
				this.goWest();
		}
		break;
		}
		aktuelleRoute.remove(this.aktuellesFeld);
	}
	
	public void goWest() {
		this.aktuellesFeld = this.aktuellesFeld.getWest();
		System.out.println("go west");	
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
