package de.vitbund.vitmaze.players;

import de.vitbund.vitmaze.spielfeld.Feld;

public class Standardbot {

	private int playerId;
	private int startX;
	private int startY;
	Feld aktuellesFeld;
	
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
