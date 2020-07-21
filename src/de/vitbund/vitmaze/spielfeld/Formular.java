package de.vitbund.vitmaze.spielfeld;
/**
 * Eine Klasse, welche die Eigenschaften und Methoden für die im Labyrinth aufzuhebenden Formulare beschreibt.
 * @author Benjamin Bogusch, Fritz Köhler, Florian Kreibe, Maximilian Hett
 * @version 1.5
 */
public class Formular {
	//Attribute
	private int nummer;
	private Feld feld;
	
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
	
	/**
	 * Konstruktor zur Instanzierung eines Formulars.
	 * @param nummer beschreibt die Nummer des Formulars
	 * @param feld beschreibt das Feld auf welchem das Formular liegt
	 */
	public Formular(int nummer, Feld feld) {
		this.nummer = nummer;
		this.feld = feld;
	}
}
