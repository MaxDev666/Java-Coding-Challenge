package de.vitbund.vitmaze.spielfeld;

public class Formular {
	
	private int nummer;
	private Feld feld= new Feld();
	
	Formular(int nummer, Feld feld) {
		this.nummer = nummer;
		this.feld = feld;
	}
	
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
