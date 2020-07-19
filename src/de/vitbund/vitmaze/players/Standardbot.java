package de.vitbund.vitmaze.players;

import java.util.ArrayList;
import java.util.List;

import de.vitbund.vitmaze.spielfeld.Feld;
import de.vitbund.vitmaze.spielfeld.Spielfeld;

public class Standardbot {

	Spielfeld spielfeld;
	private int playerId;
	private int botx;
	private int boty;
	private Feld aktuellesFeld;
	private Feld letztesFeld;
	private List<Feld> aktuelleRoute;
	private int sheetCount;
	private boolean redetDieRunde;
	
	//Getter und Setter
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
	public Feld getAktuellesFeld() {
		return aktuellesFeld;
	}	
	public void setAktuellesFeld(Feld aktuellesFeld) {
		this.aktuellesFeld = aktuellesFeld;
	}
	public List<Feld> getAktuelleRoute() {
		return aktuelleRoute;
	}
	public void setAktuelleRoute(List<Feld> aktuelleRoute) {
		this.aktuelleRoute = aktuelleRoute;
	}
	public int getSheetCount() {
		return sheetCount;
	}
	public void setSheetCount(int sheetCount) {
		this.sheetCount = sheetCount;
	}
	public boolean isRedetDieRunde() {
		return redetDieRunde;
	}
	public void setRedetDieRunde(boolean redetDieRunde) {
		this.redetDieRunde = redetDieRunde;
	}
	
	//Konstruktor
	public Standardbot(Spielfeld spielfeld) {
		this.spielfeld = spielfeld;
		this.setRedetDieRunde(false);
	}
	
	public boolean hatRoute() {
		if (this.getAktuelleRoute() != null) {
			if (this.getAktuelleRoute().isEmpty()==false) {
				return true;
			}
		}
		return false;
	}

	public void sucheUmfeldAb() {
		System.err.println("So ein Mist ich muss suchen");
		// Ring 1
		Feld Startfeld = this.aktuellesFeld;
		List<Feld> tempListe = new ArrayList<Feld>();
		List<Feld> tempListe2 = new ArrayList<Feld>();
		List<Feld> tempListe3 = new ArrayList<Feld>();
		// Ring 1 in Array eingefügt
		for (Feld f : this.aktuellesFeld.getNachbarn()) {
			tempListe2.add(f);
			tempListe.addAll(spielfeld.route(Startfeld, f));
			Startfeld = f;
		}
		
		// Ring2
		
		for (Feld f2 : tempListe2) {
			tempListe3.add(f2);
			tempListe.addAll(spielfeld.route(Startfeld, f2));
			Startfeld = f2;
		}
		
		// Ring 3
		
		for (Feld f3 : tempListe3) {
			tempListe.addAll(spielfeld.route(Startfeld, f3));
			Startfeld = f3;
		}
		
		setAktuelleRoute(tempListe);
	}
	
	public void rueckgaengig() {
		// Problem wenn Bot gerade take macht und anderer Bot zu uns kommt
		
		this.getAktuelleRoute().add(0, this.aktuellesFeld);
		this.aktuellesFeld = this.letztesFeld;
		this.botx = this.aktuellesFeld.getxKoordinate();
		this.boty = this.aktuellesFeld.getyKoordinate();
	}
	
	public String move() {
		//System.err.println("unbekannte Felder: " + spielfeld.getUnbekannteFelder());
		Feld zuFeld = new Feld( );
		String ergebnis = new String();
		this.letztesFeld = this.aktuellesFeld;
		zuFeld = this.getAktuelleRoute().get(0);
		System.err.println("Zielfeld der Route: " + this.getAktuelleRoute().get(this.aktuelleRoute.size()-1));
		System.err.println("Die Koordinaten dieses Feldes: "+this.getAktuelleRoute().get(this.aktuelleRoute.size()-1).getxKoordinate()+"|"+
							this.getAktuelleRoute().get(this.aktuelleRoute.size()-1).getyKoordinate());
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
	
	public String take() {
		return "take";
	}
	
	public String put() {
		return "put";
	}
	
	public String kick(String n, String e, String s, String w, boolean sheet) {
		
		if (sheet) {
			if (n.split(" ")[0].equals("FLOOR") | n.split(" ")[0].equals("FORM")) {
				return "kick north";
			} else 	if (e.split(" ")[0].equals("FLOOR") | n.split(" ")[0].equals("FORM")) {
				return "kick east";
			} else 	if (s.split(" ")[0].equals("FLOOR") | n.split(" ")[0].equals("FORM")) {
				return "kick south";
			} else 	if (w.split(" ")[0].equals("FLOOR") | n.split(" ")[0].equals("FORM")) {
				return "kick west";
			}
		} else {
			if (n.split(" ")[0].equals("FLOOR")) {
				return "kick north";
			} else 	if (e.split(" ")[0].equals("FLOOR")) {
				return "kick east";
			} else 	if (s.split(" ")[0].equals("FLOOR")) {
				return "kick south";
			} else 	if (w.split(" ")[0].equals("FLOOR")) {
				return "kick west";
			}
		}
		return "fail";
	}
	
	public String finish() {
		return "finish";
	}
	
	public String goWest() {
		this.setAktuellesFeld(this.getAktuellesFeld().getWest());
		if (this.getBotX()-1 < 0) {
			this.setBotX(spielfeld.getSizeX()-1);
		} else {
			this.setBotX(this.getBotX()-1);
		}
		return "go west";	
	}
	
	public String goNorth() {
		this.aktuellesFeld = this.aktuellesFeld.getNorth();
		if (this.getBotY()-1 < 0) {
			this.setBotY( +spielfeld.getSizeY()-1);
		} else {
			this.setBotY(this.getBotY()-1);
		}
		return "go north";	
	}
	
	public String goEast() {
		this.aktuellesFeld = this.aktuellesFeld.getEast();
		if (this.getBotX()+1 == spielfeld.getSizeX()) {
			this.setBotX(0);
		} else {
			this.setBotX(this.getBotX()+1);
		}
		return "go east";
	}
	
	public String goSouth() {
		this.aktuellesFeld = this.aktuellesFeld.getSouth();
		if (this.getBotY()+1 == spielfeld.getSizeY()) {
			this.setBotY(0);
		} else {
			this.setBotY(this.getBotY()+1);
		}
		return "go south";	
	}
	
	

	
}
