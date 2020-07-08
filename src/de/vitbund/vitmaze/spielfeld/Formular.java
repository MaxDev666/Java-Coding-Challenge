package de.vitbund.vitmaze.spielfeld;

public class Formular {
	
	private int nummer;
	private Feld feld= new Feld();
	
	
	public int getNummer() {
		return nummer;
	}
	public void setNummer(int nummer) {
		this.nummer = nummer;
	}
	public Feld getFeld() {
		return feld;
	}
	public void setFeld(Feld feld) {
		this.feld = feld;
	}
	
}
