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
	private String woherKommeIch;
	private int sheetCount;
	private boolean sheetPlatziert;
	public boolean isSheetPlatziert() {
		return sheetPlatziert;
	}

	public void setSheetPlatziert(boolean sheetPlatziert) {
		this.sheetPlatziert = sheetPlatziert;
	}

	public int getSheetCount() {
		return sheetCount;
	}

	public void setSheetCount(int sheetCount) {
		this.sheetCount = sheetCount;
	}

	public Standardbot(Spielfeld spielfeld) {
		this.spielfeld = spielfeld;
		sheetPlatziert = false;
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
	
	public String kick() {
		return "kick " + woherKommeIch;
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
		woherKommeIch = "east";
		return "go west";	
	}
	

	public String goNorth() {
		this.aktuellesFeld = this.aktuellesFeld.getNorth();
		if (this.getBotY()-1 < 0) {
			this.setBotY( +spielfeld.getSizeY()-1);
		} else {
			this.setBotY(this.getBotY()-1);
		}
		woherKommeIch = "south";
		return "go north";	
	}
	
	public String goEast() {
		this.aktuellesFeld = this.aktuellesFeld.getEast();
		if (this.getBotX()+1 == spielfeld.getSizeX()) {
			this.setBotX(0);
		} else {
			this.setBotX(this.getBotX()+1);
		}
		woherKommeIch = "west";
		return "go east";
	}
	
	public String goSouth() {
		this.aktuellesFeld = this.aktuellesFeld.getSouth();
		if (this.getBotY()+1 == spielfeld.getSizeY()) {
			this.setBotY(0);
		} else {
			this.setBotY(this.getBotY()+1);
		}
		woherKommeIch = "north";
		return "go south";	
	}
	
	public void getUpdate() {
		StringBuilder bla = new StringBuilder();
		bla.append("###################################################################" + "\n");
		bla.append("Ich stehe am Anfang des Zuges auf dem Feld: " + this.aktuellesFeld.getxKoordinate() + "|"+ this.aktuellesFeld.getyKoordinate() + "\n");
		bla.append("x: " + this.getBotX() + " y: " + this.getBotY() + "\n");
		bla.append("Im Norden ist: " + this.aktuellesFeld.getNorth()+ "\n");
		if (this.aktuellesFeld.getNorth()!=null) {
			bla.append("Im Norden ist: " + this.aktuellesFeld.getNorth().getxKoordinate() + this.aktuellesFeld.getNorth().getyKoordinate() + "\n");
		}
		bla.append("Im Osten ist: " + this.aktuellesFeld.getEast()+ "\n");
		if (this.aktuellesFeld.getEast()!=null) {
			bla.append("Im Osten ist: " + this.aktuellesFeld.getEast().getxKoordinate() + this.aktuellesFeld.getEast().getyKoordinate() + "\n");
		}
		bla.append("Im Süden ist : " + this.aktuellesFeld.getSouth()+ "\n");
		if (this.aktuellesFeld.getSouth()!=null) {
		bla.append("Im Süden ist : " + this.aktuellesFeld.getSouth().getxKoordinate() + this.aktuellesFeld.getSouth().getyKoordinate() + "\n");
		}
		bla.append("Im Westen ist: " + this.aktuellesFeld.getWest()+ "\n");
		if (this.getAktuellesFeld().getWest()!=null) {
			bla.append("Im Westen ist: " + this.aktuellesFeld.getWest().getxKoordinate() + this.aktuellesFeld.getWest().getyKoordinate() + "\n");
		}
		if (this.hatRoute()) {
			for (Feld f : aktuelleRoute) {
				bla.append("x: " +f.getxKoordinate() + " y:" + f.getyKoordinate() + " | ");
			}
			bla.append("Ich HABE ROUTE und will zum Feld: x: " + aktuelleRoute.get(0).getxKoordinate() + " y: " + aktuelleRoute.get(0).getyKoordinate()+ "\n");
		} else { bla.append("Ich habe noch KEINE ROUTE" + "\n");}
		if (spielfeld.getZielfeld()!=null) {
			bla.append("x: " + spielfeld.getZielfeld().getxKoordinate() + " y: " + spielfeld.getZielfeld().getyKoordinate() + " ist das ZielFeld \n");
		}
		System.err.println(bla.toString());
	}

	public String put() {
		// TODO Automatisch generierter Methodenstub
		return "put";
	}
	
}
