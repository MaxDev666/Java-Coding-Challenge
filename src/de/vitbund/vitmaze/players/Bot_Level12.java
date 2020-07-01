package de.vitbund.vitmaze.players;

import de.vitbund.vitmaze.spielfeld.Feld;

public class Bot_Level12 {

	private int playerId;
	private int startX;
	private int startY;
	
	public Bot_Level12() {
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

	
	
	public Bot_Level12(int playerID, int startX, int startY) {
		this.playerId=playerId; 
		this.startX=startX;
		this.startY=startY;
	}
	
	public void goEast(Feld feld) {
		this.feld = this.feld.getEast();
		System.out.println("go east");
	}
}
