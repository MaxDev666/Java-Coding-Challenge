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
	
	public String move() {
		//System.err.println("unbekannte Felder: " + spielfeld.getUnbekannteFelder());
		Feld zuFeld = new Feld( );
		String ergebnis = new String();
		zuFeld = this.getAktuelleRoute().get(0);
		System.err.println("Zielfeld: " + this.getAktuelleRoute().get(this.aktuelleRoute.size()-1));
		//System.err.println("VonFeld: " + this.getAktuellesFeld());
		switch (this.getAktuellesFeld().getRichtung(zuFeld)){
			case "north":
				this.getAktuelleRoute().remove(this.getAktuellesFeld());
				ergebnis = this.goNorth();
				break;
			case "south":
				this.getAktuelleRoute().remove(this.getAktuellesFeld());
				ergebnis = this.goSouth();
				break;
			case "east":
				this.getAktuelleRoute().remove(this.getAktuellesFeld());
				ergebnis = this.goEast();
				break;
			case "west":
				this.getAktuelleRoute().remove(this.getAktuellesFeld());
				ergebnis = this.goWest();
				break;
		}
		//System.err.println(this.getAktuellesFeld());
		if (spielfeld.getUnbekannteFelder().contains(this.getAktuellesFeld())) {
			spielfeld.getUnbekannteFelder().remove(this.getAktuellesFeld());
			spielfeld.getBekannteFelder().add(this.getAktuellesFeld());
		}
		if (this.getAktuellesFeld() == zuFeld) {
			this.getAktuelleRoute().remove(this.getAktuellesFeld());
		}
		return ergebnis;
	}
	
	
	//aufnehmen Funktion, muss noch überarbeitet werden
	public String take() {
		return "take";
	}
	
	public String goWest() {
		this.setAktuellesFeld(this.getAktuellesFeld().getWest());
		if (this.getBotX()-1 < 0) {
			this.setBotX(this.getBotX()-1 +spielfeld.getSizeX());
		} else {
			this.setBotX(this.getBotX()-1);
		}
		return "go west";	
	}
	

	public String goNorth() {
		this.aktuellesFeld = this.aktuellesFeld.getNorth();
		if (this.getBotY()-1 < 0) {
			this.setBotY(this.getBotY()-1 +spielfeld.getSizeY());
		} else {
			this.setBotY(this.getBotY()-1);
		}
		return "go north";	
	}
	
	public String goEast() {
		this.aktuellesFeld = this.aktuellesFeld.getEast();
		if (this.getBotX()+1 > spielfeld.getSizeX()) {
			this.setBotX(this.getBotX()+1 -spielfeld.getSizeX());
		} else {
			this.setBotX(this.getBotX()+1);
		}
		return "go east";
	}
	
	public String goSouth() {
		this.aktuellesFeld = this.aktuellesFeld.getSouth();
		if (this.getBotY()+1 > spielfeld.getSizeY()) {
			this.setBotY(this.getBotY()+1 -spielfeld.getSizeY());
		} else {
			this.setBotY(this.getBotY()+1);
		}
		return "go south";	
	}
	
	public void getUpdate() {
		
		StringBuilder bla = new StringBuilder();
		bla.append("###################################################################" + "\n");
		bla.append("Ich stehe am Anfang des Zuges auf dem Feld: " + this.aktuellesFeld.getxKoordinate() + "|"+ this.aktuellesFeld.getyKoordinate() + "\n");
		bla.append("x: " + this.getBotX() + " y: " + this.getBotY() + "\n");
		bla.append("Im Norden ist: " + this.aktuellesFeld.getNorth() + "\n");
		bla.append("Im Osten ist: " + this.aktuellesFeld.getEast() + "\n");
		bla.append("Im Süden ist : " + this.aktuellesFeld.getSouth() + "\n");
		bla.append("Im Westen ist: " + this.aktuellesFeld.getWest() + "\n");
		if (this.hatRoute()) {
			bla.append("Ich HABE ROUTE und will zum Feld: " + aktuelleRoute.get(0) + "\n");
		} else { bla.append("Ich habe noch KEINE ROUTE" + "\n");}
		bla.append(spielfeld.getZielfeld() + " ist das ZielFeld \n");
		System.err.println(bla.toString());
		
	}
	
}
