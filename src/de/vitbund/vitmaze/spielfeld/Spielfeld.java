package de.vitbund.vitmaze.spielfeld;

import java.util.ArrayList;
import java.util.List;

public class Spielfeld {

	private int sizeX;
	private int sizeY;
	private int level;
	private List<Feld> felder = new ArrayList<Feld>();
	
	public int getSizeX() {
		return sizeX;
	}
	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}
	public int getSizeY() {
		return sizeY;
	}
	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	public void addFeld(Feld newFeld) {
		felder.add(newFeld);
	}
	
	
	
	//public Route findeWegZu(Feld zielFeld)
	
}
