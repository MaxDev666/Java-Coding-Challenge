package de.vitbund.vitmaze.players;

import java.util.List;

import de.vitbund.vitmaze.spielfeld.Feld;
import de.vitbund.vitmaze.spielfeld.Spielfeld;

public class Standardbot {

	Spielfeld spielfeld;
	private int playerId;
	private int botx;
	private int boty;
	private Feld aktuellesFeld;
	private List<Feld> aktuelleRoute;


	public Standardbot(Spielfeld spielfeld) {
		this.spielfeld = spielfeld;
	}
	
	public boolean hatRoute() {
		if (this.getAktuelleRoute() != null) {
			if (this.getAktuelleRoute().isEmpty()==false) {
				return true;
			}
		}
		return false;
	}
	
	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getBotX() {
		return botx;
	}

	public void setBotX(int botx) {
		this.botx = botx;
	}

	public int getBotY() {
		return boty;
	}

	public void setBotY(int boty) {
		this.boty = boty;
	}
	
	public void setAktuellesFeld(Feld aktuellesFeld) {
		this.aktuellesFeld = aktuellesFeld;
	}

	public Feld getAktuellesFeld() {
		return aktuellesFeld;
	}
	
	public void setAktuelleRoute(List<Feld> aktuelleRoute) {
		this.aktuelleRoute = aktuelleRoute;
	}
	
	public List<Feld> getAktuelleRoute() {
		return aktuelleRoute;
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
			spielfeld.getBekannteFelder().add(this.getAktuellesFeld());
		}
		if (this.getAktuellesFeld() == zuFeld) {
			this.getAktuelleRoute().remove(this.getAktuellesFeld());
		}
		
		
	}
	
	public void goWest() {
		this.setAktuellesFeld(this.getAktuellesFeld().getWest());
		System.out.println("go west");	
	}
	

	public void goNorth() {
		this.aktuellesFeld = this.aktuellesFeld.getNorth();
		if (this.getBotY()-1 < 0) {
			
		} else {
		}
		
		
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
	
	public void getUpdate() {
		System.err.println("###################################################################");
		System.err.println("Ich stehe am Anfang des Zuges auf dem Feld: " + this.aktuellesFeld.getxKoordinate() + "|"+ this.aktuellesFeld.getyKoordinate());
		System.err.println("x: " + this.getBotX() + " y: " + this.getBotY());
		System.err.println("Im Norden ist: " + this.aktuellesFeld.getNorth());
		System.err.println("Im Osten ist: " + this.aktuellesFeld.getEast());
		System.err.println("Im Süden ist : " + this.aktuellesFeld.getSouth());
		System.err.println("Im Westen ist: " + this.aktuellesFeld.getWest());
		if (this.hatRoute()) {
		System.err.println("Ich will zum Feld: " + aktuelleRoute.get(0));
		} else { System.err.println("Ich habe noch keine Route");}
		System.err.println("###################################################################");
		
	}
	
}
