package de.vitbund.vitmaze.spielfeld;

public class Feld {
	private Feld north = null;
	private Feld east = null;
	private Feld south = null;
	private Feld west = null;
	private boolean ziel = false;
	
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
	
	
}
