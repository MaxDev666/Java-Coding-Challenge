package de.vitbund.vitmaze.spielfeld;

/**
 * Klasse welche die Eigenschaften und Methoden für die im Labyrinth aufzuhebenden Formulare beschreibt.
 * @author Arbeitstitel
 * @version 1.0
 */

public class Formular {
	private int nummer;
	private Feld feld;
	/**
	 * Konstruktor zur Instanzierung eines Formulars.
	 * @param nummer beschreibt die Nummer des Formulars
	 * @param feld beschreibt das Feld auf welchem das Formular liegt
	 */
	public Formular(int nummer, Feld feld) {
		this.nummer = nummer;
		this.feld = feld;
	}
	// Getter und Setter
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
