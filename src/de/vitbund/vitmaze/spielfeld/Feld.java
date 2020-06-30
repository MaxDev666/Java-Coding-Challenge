package de.vitbund.vitmaze.spielfeld;

import java.util.LinkedList;
import java.util.List;

public class Feld {
	private Feld north = null;
	private Feld east = null;
	private Feld south = null;
	private Feld west = null;
	private boolean ziel = false;
	
	private Feld vorgaenger; // für die wegsuche
	private Feld links;
	private Feld rechts;
	private Feld mitte;
	private boolean istInBearbeitung;
	
	
	public Feld getLinks() {
		return links;
	}
	public void setLinks(Feld links) {
		this.links = links;
	}
	public Feld getRechts() {
		return rechts;
	}
	public void setRechts(Feld rechts) {
		this.rechts = rechts;
	}
	public Feld getMitte() {
		return mitte;
	}
	public void setMitte(Feld mitte) {
		this.mitte = mitte;
	}
	
	public Feld() {
		
	}
	public Feld(Feld north, Feld south, Feld west, Feld east) {
		this.north = north;
		this.south=south;
		this.west = west;
		this.east = east;
	}
	
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
	public boolean isZiel() {
		return ziel;
	}
	public void setZiel(boolean ziel) {
		this.ziel = ziel;
	}
	public Feld getVorgaenger() {
		return vorgaenger;
	}
	public void setVorgaenger(Feld vorgaenger) {
		this.vorgaenger = vorgaenger;
	}
	
	public List<Feld> getNachbarn() {
		List<Feld> nachbarn = new LinkedList<Feld>();
		if (this.getEast()!=null) {nachbarn.add(this.getEast());}
		if (this.getSouth()!=null) {nachbarn.add(this.getSouth());}
		if (this.getWest()!=null) {nachbarn.add(this.getWest());}
		if (this.getNorth()!=null) {nachbarn.add(this.getNorth());}
		return nachbarn;
	}
	
	public List<Feld> getNachbarnOhne(Feld feld) {
		List<Feld> nachbarn = new LinkedList<Feld>();
		if (this.getEast()!=null && this.getEast()!=feld && !this.getEast().isIstInBearbeitung()) {nachbarn.add(this.getEast());}
		if (this.getSouth()!=null && this.getSouth()!= feld && !this.getSouth().isIstInBearbeitung()) {nachbarn.add(this.getSouth());}
		if (this.getWest()!=null && this.getWest()!= feld && !this.getWest().isIstInBearbeitung()) {nachbarn.add(this.getWest());}
		if (this.getNorth()!=null && this.getNorth()!=feld && !this.getNorth().isIstInBearbeitung()) {nachbarn.add(this.getNorth());}
		return nachbarn;
	}
	public boolean isIstInBearbeitung() {
		return istInBearbeitung;
	}
	public void setIstInBearbeitung(boolean istInBearbeitung) {
		this.istInBearbeitung = istInBearbeitung;
	}
	
	public int tiefe() {
		Feld feld = this;
		int i = 0;
		while (feld.getVorgaenger()!=feld) {
			i+=1;
			feld = feld.getVorgaenger();
		}
		return i;
	}
	
}
