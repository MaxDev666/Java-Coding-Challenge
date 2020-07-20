package de.vitbund.vitmaze.spielfeld;

import java.util.LinkedList;
import java.util.List;
/**
 * Ermöglicht es Felder mit Koordinaten sowie deren Umgebung und Nachbarfelder anzulegen un abzufragen
 * @author Arbeitstitel
 * @version 1.0
 *
 */
public class Feld {
	//Attribute
	private Feld north = null;
	private Feld east = null;
	private Feld south = null;
	private Feld west = null;
	private int xKoordinate;
	private int yKoordinate;
	
	private Feld vorgaenger; // für die wegsuche
	private boolean istInBearbeitung;

	
	//Getter und Setter
	public Feld getNorth() {
		return north;
	}
	public void setNorth(Feld north) {
		this.north = north;
	}
	public Feld getEast() {
		return east;
	}
	public void setEast(Feld east) {
		this.east = east;
	}
	public Feld getSouth() {
		return south;
	}
	public void setSouth(Feld south) {
		this.south = south;
	}
	public Feld getWest() {
		return west;
	}
	public void setWest(Feld west) {
		this.west = west;
	}
	public Feld getVorgaenger() {
		return vorgaenger;
	}
	public void setVorgaenger(Feld vorgaenger) {
		this.vorgaenger = vorgaenger;
	}
	public int getxKoordinate() {
		return xKoordinate;
	}
	public void setxKoordinate(int xKoordinate) {
		this.xKoordinate = xKoordinate;
	}
	public int getyKoordinate() {
		return yKoordinate;
	}
	public void setyKoordinate(int yKoordinate) {
		this.yKoordinate = yKoordinate;
	}
	public boolean istInBearbeitung() {
		return istInBearbeitung;
	}
	public void setInBearbeitung(boolean istInBearbeitung) {
		this.istInBearbeitung = istInBearbeitung;
	}
	public List<Feld> getNachbarn() {
		List<Feld> nachbarn = new LinkedList<Feld>();
		if (this.getEast()!=null) {nachbarn.add(this.getEast());}
		if (this.getSouth()!=null) {nachbarn.add(this.getSouth());}
		if (this.getWest()!=null) {nachbarn.add(this.getWest());}
		if (this.getNorth()!=null) {nachbarn.add(this.getNorth());}
		return nachbarn;
	}
	public List<Feld> getNachbarnOhne(Feld feld, boolean mitbearbeitet) {
		List<Feld> nachbarn = new LinkedList<Feld>();
		if (this.getEast() != null && this.getEast() != feld && !this.getEast().istInBearbeitung()) {
			nachbarn.add(this.getEast());
		}
		if (this.getSouth() != null && this.getSouth() != feld && !this.getSouth().istInBearbeitung()) {
			nachbarn.add(this.getSouth());
		}
		if (this.getWest() != null && this.getWest() != feld && !this.getWest().istInBearbeitung()) {
			nachbarn.add(this.getWest());
		}
		if (this.getNorth() != null && this.getNorth() != feld && !this.getNorth().istInBearbeitung()) {
			nachbarn.add(this.getNorth());
		}
		return nachbarn;
	}
	//Konstruktoren
	public Feld() {	
	}
	
	public Feld(int x, int y) {
		this.xKoordinate = x;
		this.yKoordinate = y;
	}
	
	public Feld(Feld north, Feld south, Feld west, Feld east) {
		this.north = north;
		this.south=south;
		this.west = west;
		this.east = east;
	}
	//Methoden
	/**
	 * Methode, welche die die Richtung des nachbarFelds zum aktuellen Feld zurückgibt
	 * @param nachbarFeld Feld in der Umgebung des aktuellen Feldes
	 * @return string entsprechende Richtung "north","east","south","west","Feld kein Nachbar"
	 */
	public String getRichtung(Feld nachbarFeld) {
		if (this.getNorth()==nachbarFeld) {
			return "north";
		}else if (this.getEast()==nachbarFeld) {
			return "east";
		}else if (this.getSouth()==nachbarFeld) {
			return "south";
		}else if (this.getWest()==nachbarFeld) {
			return "west";
		}else {
			return "Feld kein Nachbar";
		}
	}
	
	/**
	 * Methode, welche sich das Nachbarfeld zum Feld in entsprechender Richtung ausgeben lässt
	 * @param richtung n/e/s/w
	 * @return Feld der entsprechenden Richtung
	 */
	public Feld getFeld(char richtung) {
		switch (richtung) {
		case 'n':
			return this.getNorth();
		case 'e':
			return this.getEast();
		case 's':
			return this.getSouth();
		case 'w':
			return this.getWest();
		default:
			return this;
		}
	}
}
