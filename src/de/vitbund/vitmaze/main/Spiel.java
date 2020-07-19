package de.vitbund.vitmaze.main;

import java.util.List;
import java.util.Random;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

import de.vitbund.vitmaze.eingabe.Eingabe;
import de.vitbund.vitmaze.players.Standardbot;
import de.vitbund.vitmaze.spielfeld.Feld;
import de.vitbund.vitmaze.spielfeld.Formular;
import de.vitbund.vitmaze.spielfeld.Spielfeld;

public class Spiel {

	Spielfeld spielfeld;
	Standardbot bot;
	String lastActionsResult;
	String currentCellStatus;
	String northCellStatus;
	String eastCellStatus;
	String southCellStatus;
	String westCellStatus;
	boolean hatZiel;
	List<Feld> ziele;
	String ausgabe;
	Formular[] forms;
	int anzahlFormulare;
	String anzahlFormulareString;
	int formID;
	String formIDString;
	int formcounter;
	boolean formFound;
	boolean allesGesammelt;
	boolean zugvorbei;
	Random wuerfel;
	boolean rundeZuEnde;
	
	// Klasse Formular noch anlegen
	// int id
	// Feld feld
	// String aufheben
	
	
	/***
	 * Wie viele Formulare hat der Bot bereits entdeckt?
	 * @return Anzahl entdeckter Formulare
	 */
	public int howManyForms() {
		int i=0;
		for (Formular f:forms) {
			if (f != null) {
				i++;
			}
		}
		return i;
	}
	
	
	public void init() {
		// Spielfeld anlegen und Startdaten setzen
		wuerfel = new Random();
		spielfeld = new Spielfeld();
		spielfeld.setSizeX(Eingabe.leseZahl()); // X-Groesse des Spielfeldes (Breite)
		spielfeld.setSizeY(Eingabe.leseZahl()); // Y-Groesse des Spielfeldes (Hoehe)
		spielfeld.setLevel(Eingabe.leseZahl()); // Level des Matches
		Eingabe.leseZeile();

		// Bote erstellen
		bot = new Standardbot(spielfeld);
		bot.setPlayerId(Eingabe.leseZahl());// id dieses Players / Bots
		bot.setBotX(Eingabe.leseZahl()); // X-Koordinate der Startposition dieses Player
		bot.setBotY(Eingabe.leseZahl()); // Y-Koordinate der Startposition dieses Players
		if (spielfeld.getLevel()==5) {
			bot.setSheetCount(Eingabe.leseZahl());
			System.err.println(bot.getSheetCount() + " Sheets habe ich");
		}
		Eingabe.leseZeile();

		
		Feld temp =new Feld(bot.getBotX(),bot.getBotY());
		// Startfeld anlegen und zum Spielfeld hinzufügen
		bot.setAktuellesFeld(temp);
		spielfeld.getBekannteFelder().add(temp);
		spielfeld.addFeld(bot.getAktuellesFeld());
		
		// Bot anlegen und Startdaten setzen
	

		hatZiel=false;
		anzahlFormulare=999;
		anzahlFormulareString="";
		forms = new Formular[26];
		for (Formular form : forms) {
			form = null;
		}
		formID=0;
		formIDString="";
		formcounter = 1;
		formFound = false;
		allesGesammelt = false;
		ausgabe = new String("position");
		
	}
	
	public void getStati() {
		

		this.lastActionsResult = Eingabe.leseZeile();
		this.currentCellStatus = Eingabe.leseZeile();
		this.northCellStatus = Eingabe.leseZeile();
		this.eastCellStatus = Eingabe.leseZeile();
		this.southCellStatus = Eingabe.leseZeile();
		this.westCellStatus = Eingabe.leseZeile();
		
		// prüfen was mit letzter Aktion war
		System.err.println(this.lastActionsResult + "\n" + this.currentCellStatus + "\n North is:" + this.northCellStatus + "\n East is:" + this.eastCellStatus + "\n South is:" + this.southCellStatus + "\n West is:" + this.westCellStatus);

		
		switch (spielfeld.getLevel()) {
		case 1:
			this.erkunden();
			break;
		case 2:
			this.erkunden2();
			break;
		case 3:
			this.erkunden2();
			break;
		case 4:
			this.erkunden3();
			break;
		case 5:
			this.erkunden4();
			break;
		}


		bot.getUpdate();
		
		
		// hier Aktion ausführen
		System.err.println("Ich sage: " + ausgabe);
		System.out.println(ausgabe);

		
		
	}
	
	public void schaueRichtung(char richtung){
		String[] cellStatusArray = getCellStatus(richtung).split(" ");
		
		if (bot.getAktuellesFeld().getFeld(richtung) == null) {
			if (!cellStatusArray[0].equals("WALL")) {
				this.erstellFeld(richtung);
			}
		}	
		
		if (cellStatusArray[0].equals("FORM")) {
			formID = Integer.parseInt(cellStatusArray[3]);
			
			if (forms[formID] == null) {
				Formular formular = new Formular(formID, bot.getAktuellesFeld().getFeld(richtung));
				forms[formID] = formular;
			}

			if (formID == formcounter) {
				bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), forms[formID].getFeld()));
				this.ausgabe = bot.move();
				rundeZuEnde = true;
			}
		}
		
		if (getCellStatus(richtung).startsWith("SHEET")) {
			spielfeld.getSheetList().add(bot.getAktuellesFeld().getFeld(richtung));
		}
			
		if (cellStatusArray[0].equals("FINISH")) {
			anzahlFormulare = Integer.parseInt(cellStatusArray[3]);
			if (anzahlFormulare == howManyForms()) {
				allesGesammelt = true;
				System.err.println("HABE ALLES GESAMMELT UND GEHE ZUM ZIEL ZU FELD " + spielfeld.getZielfeld());
				bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(),  bot.getAktuellesFeld().getFeld(richtung)));
				bot.getUpdate();
				this.ausgabe = bot.move();
				rundeZuEnde = true;
			}else {
				spielfeld.setZielfeld(bot.getAktuellesFeld().getFeld(richtung));
			}
		}		
}

	public getStati
	istRundeZuende = false
	runde zuende
	Werte holen
	
	get Last Action auswerten
	
	erkunden
	
	ist die Runde zuende?
		-Nein 
			habe ich Route?
				- laufen
				- Runde ende
			nein?
				habe ich alle Formulare und kenne Ziel?
					- Route zum ziel
					- laufen
					- runde ende
				habe ich Formulare die ich sammeln kann?
					- Route zu Formular
						- laufen
						- Runde ende
				gibt es unbekannte Felder?
					- laufe dorthin
					- Runde ende
				keine unbekannten Felder, keine Route, kein Ziel?
					- bekannte Felder nochmal erkunden

	
	
		public void erkunden() {
		
		schaueRichtung( 'n');
		schaueRichtung('e'); 
		schaueRichtung ('s');
		schaueRichtung ('w');
		
		String[] aktFeld = this.currentCellStatus.split(" ");
		
		if (aktFeld[aktFeld.length-1].equals("!")) {
			if (bot.isRedetDieRunde() == false) {
				bot.setRedetDieRunde(true);
				rundeZuEnde = true;					
			} else {
				bot.setRedetDieRunde(true);
			}
		}
		
		if (rundeZuEnde == false) {
			if (aktFeld[0].equals("FORM")){
				if (aktFeld[1].equals(bot.getPlayerId()) && aktFeld[2].equals(formcounter)) {
					this.ausgabe = bot.take();
					rundeZuEnde = true;
				} else if (!aktFeld[1].equals(bot.getPlayerId())) {
					if (bot.getSheetCount()>0) {
						this.ausgabe = bot.put();
						rundeZuEnde = true;
					} else {
						// Problem zu schauen ob kicken in Ordunung war
						this.ausgabe = bot.kick();
						rundeZuEnde = true;
					}
				}
			}  else if (aktFeld[0].equals("FINISH")) {
				if (aktFeld[1].equals(bot.getPlayerId())) {
					this.ausgabe = bot.finish();	
				}
			} 
		}
		}

		
		was ist aktuelles Feld?
			- Form
				-> eigenes und in Reihenfolge?
					-> aufheben
					Runde ende
			
				-> fremdes 
					-> hab ich Sheets? (& Level =5)
						-> verdecken
					-> sonst kicken
					Runde ende
			- FLOOR
				-> sollte aber Formular sein
					-> erkunden
			- Ziel
				schauen wie viele Formulare nötig
				hab ich alle Formulare?
					- FINISH
					Runde ende
	}
	
	
	
	
	public String getCellStatus(char richtung){
		switch(richtung) {
		case 'n':
			return this.northCellStatus;
			
		case 'e':
			return this.eastCellStatus;
			
		case 's':
			return this.southCellStatus;
			
		case 'w':
			return this.westCellStatus;
			
		default: 
			return "kein Status";			
		}
		
	}
	//den Status der Felder erhalten und dementsprechend Felder erstellen
	public void schauerichtung(char richtung) {
		if (bot.getAktuellesFeld().getFeld(richtung) == null) {
			if (getCellStatus(richtung).startsWith("FLOOR") || getCellStatus(richtung).startsWith("FINISH ")) {
				System.err.println(richtung  + " ist ein Feld");
				this.erstellFeld(richtung);
			}
			if (getCellStatus(richtung).startsWith("FINISH " + bot.getPlayerId())) {
				spielfeld.setZielfeld(bot.getAktuellesFeld().getFeld(richtung));
				bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getZielfeld()));
			}
		}
	}
	//den Status der Felder erhalten und dementsprechend Felder erstellen bei erkunden 2 (mit Formularen)
	public void schauerichtung2(char richtung) {
			if (bot.getAktuellesFeld().getFeld(richtung) == null) {
				if (getCellStatus(richtung).startsWith("FLOOR") || getCellStatus(richtung).startsWith("FORM ") || getCellStatus(richtung).startsWith("FINISH ")) {
					this.erstellFeld(richtung);
				}
			}
			if (getCellStatus(richtung).startsWith("FINISH " + bot.getPlayerId())) {
				if (getCellStatus(richtung).contains("!")) {
					anzahlFormulareString = getCellStatus(richtung).substring(getCellStatus(richtung).indexOf("!")-3, getCellStatus(richtung).indexOf("!")-1);
				}else {
					anzahlFormulareString = getCellStatus(richtung).substring(getCellStatus(richtung).length()-2);
				}
				System.err.println("Anzahl Formulare" + anzahlFormulareString);
				if (anzahlFormulareString.charAt(0)==' ') {
					anzahlFormulare=Character.getNumericValue(anzahlFormulareString.charAt(1));
				}else {
					anzahlFormulare=Integer.parseInt(anzahlFormulareString);
					System.err.println(getCellStatus(richtung));
					System.err.println("Anzahl der Formulare ist " +anzahlFormulare + " " +  anzahlFormulareString);
				}
				spielfeld.setZielfeld(bot.getAktuellesFeld().getFeld(richtung));
			}
		
			
			if (getCellStatus(richtung).startsWith("FORM " + bot.getPlayerId())) {
				if (getCellStatus(richtung).contains("!")) {
					formIDString = getCellStatus(richtung).substring(getCellStatus(richtung).indexOf("!")-3, getCellStatus(richtung).indexOf("!")-1);
				}else {
					formIDString = getCellStatus(richtung).substring(getCellStatus(richtung).length()-2);
				}
				System.err.println("Formular ID" + formIDString);
				if (formIDString.charAt(0)==' ') {
					formID=(Character.getNumericValue(formIDString.charAt(1)));
				}else {
					formID=Integer.parseInt(formIDString);
				}
				System.err.println(getCellStatus(richtung).substring(getCellStatus(richtung).length()-2) + " " + formID);
				// Aufnehmen muss noch überarbeitet werden
				if (forms[formID] == null) {
					Formular formular = new Formular(formID, bot.getAktuellesFeld().getFeld(richtung));
					forms[formID] = formular;
				}

				if (formID == formcounter) {
					formFound = true;
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), forms[formID].getFeld()));

				}
			}
		}
	
	public void schauerichtung3(char richtung) {
		if (bot.getAktuellesFeld().getFeld(richtung) == null) {
			if (getCellStatus(richtung).startsWith("FLOOR") || getCellStatus(richtung).startsWith("FORM ") || getCellStatus(richtung).startsWith("FINISH ") || getCellStatus(richtung).startsWith("SHEET ")) {
				this.erstellFeld(richtung);
			}
		}
		if (getCellStatus(richtung).startsWith("FINISH " + bot.getPlayerId())) {
			if (getCellStatus(richtung).contains("!")) {
				anzahlFormulareString = getCellStatus(richtung).substring(getCellStatus(richtung).indexOf("!")-3, getCellStatus(richtung).indexOf("!")-1);
			}else {
				anzahlFormulareString = getCellStatus(richtung).substring(getCellStatus(richtung).length()-2);
			}
			System.err.println("Anzahl Formulare" + anzahlFormulareString);
			if (anzahlFormulareString.charAt(0)==' ') {
				anzahlFormulare=Character.getNumericValue(anzahlFormulareString.charAt(1));
			}else {
				anzahlFormulare=Integer.parseInt(anzahlFormulareString);
				System.err.println(getCellStatus(richtung));
				System.err.println("Anzahl der Formulare ist " +anzahlFormulare + " " +  anzahlFormulareString);
			}
			spielfeld.setZielfeld(bot.getAktuellesFeld().getFeld(richtung));
		}
		
		if (getCellStatus(richtung).startsWith("SHEET")) {
			spielfeld.getSheetList().add(bot.getAktuellesFeld().getFeld(richtung));
		}
		
		if (getCellStatus(richtung).startsWith("FORM " + bot.getPlayerId())) {
			if (getCellStatus(richtung).contains("!")) {
				formIDString = getCellStatus(richtung).substring(getCellStatus(richtung).indexOf("!")-3, getCellStatus(richtung).indexOf("!")-1);
			}else {
				formIDString = getCellStatus(richtung).substring(getCellStatus(richtung).length()-2);
			}
			System.err.println("Formular ID" + formIDString);
			if (formIDString.charAt(0)==' ') {
				formID=(Character.getNumericValue(formIDString.charAt(1)));
			}else {
				formID=Integer.parseInt(formIDString);
			}
			System.err.println(getCellStatus(richtung).substring(getCellStatus(richtung).length()-2) + " " + formID);
			// Aufnehmen muss noch überarbeitet werden
			if (forms[formID] == null) {
				Formular formular = new Formular(formID, bot.getAktuellesFeld().getFeld(richtung));
				forms[formID] = formular;
			}

			if (formID == formcounter) {
				formFound = true;
				bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), forms[formID].getFeld()));

			}
		}
	}
	
//ruft Methode(schauerichtung) von oben für entsprechende Richtungen auf
	public void erkunden() {
				schauerichtung('n');
				schauerichtung('e');
				schauerichtung('s');
				schauerichtung('w');
				
//wenn auf sonstigen Feldern, bzw abbruch von Erkunden bei Ziel
			if (bot.hatRoute()==false) {
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(0)));
			} 
			if (spielfeld.getZielfeld() == bot.getAktuellesFeld()) {
				this.ausgabe = bot.finish();
			} else {
				this.ausgabe = bot.move();
			}


	}
	
	public void erkunden2() {
		
		zugvorbei = false;
		if (!allesGesammelt) {
			schauerichtung2('n');
			schauerichtung2('e');
			schauerichtung2('s');
			schauerichtung2('w');
			bot.getUpdate();
		// wenn ich alle Formulare habe

			
			if (this.currentCellStatus.equals("FORM " + bot.getPlayerId() + " " + formcounter )) {
				this.ausgabe = bot.take();
				zugvorbei = true;
				if (formcounter==howManyForms() && spielfeld.getZielfeld()!=null) {
					allesGesammelt = true;
					System.err.println("HABE ALLES GESAMMELT UND GEHE ZUM ZIEL ZU FELD " + spielfeld.getZielfeld());
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(),  spielfeld.getZielfeld()));
					bot.getUpdate();
				} else {
					formcounter++;
				}
			}
			
			if (bot.hatRoute()==false) {
				if (zugvorbei==false) {
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(0)));
					this.ausgabe = bot.move();
				}
			} else {
				if (zugvorbei==false) {
					
					this.ausgabe = bot.move();
					
				}
			}
			if (howManyForms()==anzahlFormulare) {
				if (!allesGesammelt) {
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), forms[formcounter].getFeld()));
				} else {
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(),  spielfeld.getZielfeld()));
				}
			}	
			
		} else {
			if (spielfeld.getZielfeld() == bot.getAktuellesFeld()) {
				this.ausgabe = bot.finish();
			} else {
				this.ausgabe = bot.move();
			}
		}


	}

	public void erkunden3() {
		
		zugvorbei = false;
		if (!allesGesammelt) {
			schauerichtung2('n');
			schauerichtung2('e');
			schauerichtung2('s');
			schauerichtung2('w');
			bot.getUpdate();
		// wenn ich alle Formulare habe

			if (this.currentCellStatus.startsWith("FORM ") && !this.currentCellStatus.startsWith("FORM " + bot.getPlayerId())) {
				if (this.lastActionsResult.equals("NOK BLOCKED")){
					System.err.println("Dann halt nciht, neue Route");
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(0)));
					this.ausgabe = bot.move();
					zugvorbei = true;
				} else {
					this.ausgabe = bot.kick();
					zugvorbei = true;
				}
			}
			
			if (this.currentCellStatus.equals("FORM " + bot.getPlayerId() + " " + formcounter )) {
				this.ausgabe = bot.take();
				zugvorbei = true;
				if (formcounter==howManyForms() && spielfeld.getZielfeld()!=null) {
					allesGesammelt = true;
					System.err.println("HABE ALLES GESAMMELT UND GEHE ZUM ZIEL ZU FELD " + spielfeld.getZielfeld());
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(),  spielfeld.getZielfeld()));
					bot.getUpdate();
				} else {
					formcounter++;
				}
			}
			
			if (bot.hatRoute()==false) {
				if (zugvorbei==false) {
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(0)));
					this.ausgabe = bot.move();
				}
			} else {
				if (zugvorbei==false) {
					// hier abfragen ob ich am Ende der Route bin und ob 
					// forms[formcounter].getFeld() == bot.getAktuellesFeld() && !this.currentCellStatus.equals("FORM " + bot.getPlayerId() + " " + formcounter )
					// dann weiss ich ich bin richtig aber das Formular ist nicht da
					// hier muss ich jetzt alle Nachbarfelder meines Feldes in die aktuelle Route kriegen  und am besten deren Nachbarn
					
					 //TODO hier muss noch angepasst werden
					if (forms[formcounter]!= null && forms[formcounter].getFeld() == bot.getAktuellesFeld() && !this.currentCellStatus.startsWith("FORM " + bot.getPlayerId() + " " + formcounter )) {
						bot.sucheUmfeldAb();
					}
					
					this.ausgabe = bot.move();
					
				}
			}
			if (howManyForms()==anzahlFormulare) {
				if (!allesGesammelt) {
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), forms[formcounter].getFeld()));
				} else {
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(),  spielfeld.getZielfeld()));
				}
			}	
			
		} else {
			if (spielfeld.getZielfeld() == bot.getAktuellesFeld()) {
				this.ausgabe = bot.finish();
			} else {
				this.ausgabe = bot.move();
			}
		}
	}
	
public void erkunden4() {
		
		zugvorbei = false;
		if (!allesGesammelt) {
			schauerichtung3('n');
			schauerichtung3('e');
			schauerichtung3('s');
			schauerichtung3('w');
			bot.getUpdate();
		// wenn ich alle Formulare habe

			if (this.currentCellStatus.startsWith("SHEET")) {
				if (this.lastActionsResult.equals("NOK BLOCKED")){
					System.err.println("Dann halt nciht, neue Route");
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(0)));
					if (!this.currentCellStatus.equals("FLOOR !")) {
					this.ausgabe = bot.move();
					zugvorbei = true;
					}
				} else {
					this.ausgabe = bot.kick();
					zugvorbei = true;
				}
			}
			
			if (!zugvorbei) {
			if (this.currentCellStatus.startsWith("FORM ") && !this.currentCellStatus.startsWith("FORM " + bot.getPlayerId())) {
				if (this.lastActionsResult.equals("NOK BLOCKED")){
					System.err.println("Dann halt nciht, neue Route");
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(0)));
					if (!this.currentCellStatus.equals("FLOOR !")) {
					this.ausgabe = bot.move();
					zugvorbei = true;
					}
				} else {
					if (bot.getSheetCount()>0) {
						this.ausgabe= bot.put();
						bot.setSheetCount(bot.getSheetCount()-1);
						zugvorbei = true;
					} else {
						this.ausgabe = bot.kick();
						zugvorbei = true;
					}
				}
			}}
			
			if (this.currentCellStatus.equals("FORM " + bot.getPlayerId() + " " + formcounter )) {
				this.ausgabe = bot.take();
				zugvorbei = true;
				if (formcounter==howManyForms() && spielfeld.getZielfeld()!=null) {
					allesGesammelt = true;
					System.err.println("HABE ALLES GESAMMELT UND GEHE ZUM ZIEL ZU FELD " + spielfeld.getZielfeld());
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(),  spielfeld.getZielfeld()));
					bot.getUpdate();
				} else {
					formcounter++;
				}
			}
			
			if (bot.hatRoute()==false) {
				if (zugvorbei==false) {
					if (this.lastActionsResult.equals("NOK TALKING")){
						int zufall =wuerfel.nextInt(spielfeld.getUnbekannteFelder().size());
						System.err.println("Spielfeld unbekannt ist: " + zufall);
						bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(zufall)));
						if (!this.currentCellStatus.equals("FLOOR !")) {
							this.ausgabe = bot.move();
							zugvorbei = true;
							}
					} else {
						if (spielfeld.getUnbekannteFelder().size()==0) {
							bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getSheetList().get(0)));
							if (!this.currentCellStatus.equals("FLOOR !")) {
								this.ausgabe = bot.move();
								zugvorbei = true;
								}		
						} else {
							bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(0)));
							if (!this.currentCellStatus.equals("FLOOR !")) {
								this.ausgabe = bot.move();
								zugvorbei = true;
								}	
						}
					}
				}
			} else {
				if (this.lastActionsResult.equals("NOK TALKING")){
					int zufall = wuerfel.nextInt(spielfeld.getUnbekannteFelder().size());
					System.err.println("Spielfeld unbekannt ist: " + zufall);
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(zufall)));
					this.ausgabe = bot.move();
				}
				if (zugvorbei==false) {
					// hier abfragen ob ich am Ende der Route bin und ob 
					// forms[formcounter].getFeld() == bot.getAktuellesFeld() && !this.currentCellStatus.equals("FORM " + bot.getPlayerId() + " " + formcounter )
					// dann weiss ich ich bin richtig aber das Formular ist nicht da
					// hier muss ich jetzt alle Nachbarfelder meines Feldes in die aktuelle Route kriegen  und am besten deren Nachbarn
					
					 //TODO hier muss noch angepasst werden
					if (forms[formcounter]!= null && forms[formcounter].getFeld() == bot.getAktuellesFeld() && !this.currentCellStatus.startsWith("FORM " + bot.getPlayerId() + " " + formcounter )) {
						bot.sucheUmfeldAb();
					}
					
					if (!this.currentCellStatus.equals("FLOOR !")) {
					this.ausgabe = bot.move();
					zugvorbei = true;
					}
					
				}
			}
			if (howManyForms()==anzahlFormulare) {
				if (!allesGesammelt) {
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), forms[formcounter].getFeld()));
				} else {
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(),  spielfeld.getZielfeld()));
				}
			}	
			
		} else {
			if (spielfeld.getZielfeld() == bot.getAktuellesFeld()) {
				this.ausgabe = bot.finish();
			} else {
				if (!this.currentCellStatus.equals("FLOOR !")) {
				this.ausgabe = bot.move();
				zugvorbei = true;
				}
			}
		}
	}
	
	
	public void erstellFeld(char richtungFeldErstellen) {
		
			/*	feststellen wohin das Feld soll
			 * 	-> n e s w
			 * 	-> Koordianten ermitteln
			 * 
			 * 	prüfen ob dort Feld exisitiert
			 * 
			 * 	feld verknüpfen oder feld erstellen
			 * 
			 */
			int wohinx = 0;
			int wohiny = 0;
			switch (richtungFeldErstellen) {
			case 'n':
				System.err.println("case n");
				if (bot.getBotY()-1 <0) {
					wohinx = bot.getBotX();
					wohiny= spielfeld.getSizeY()-1;
					break;					
				} else {
					wohinx=bot.getBotX();
					wohiny = bot.getBotY() - 1;
					break;
				}
			case 'e':
				System.err.println("case e");
				if (bot.getBotX()+1 == spielfeld.getSizeX()) {
					System.err.println("Ostkoordinaten: "+bot.getBotX()+" "+spielfeld.getSizeX());
					wohinx = 0;
					wohiny= bot.getBotY() ;
					break;					
				} else {
					wohinx=bot.getBotX() +1;
					wohiny = bot.getBotY();
					break;
				}
			case 's':
				System.err.println("case s");
				System.err.println("Südkoordinaten: "+bot.getBotY()+" "+spielfeld.getSizeY());
				if (bot.getBotY()+1 == spielfeld.getSizeY()) {
					wohinx = bot.getBotX();
					wohiny= 0;
					break;					
				} else {
					wohinx=bot.getBotX();
					wohiny = bot.getBotY() + 1;
					break;
				}
			case 'w':
				System.err.println("case w");
				if (bot.getBotX()-1 <0) {
					System.err.println("Westkoordinaten: "+bot.getBotX()+" "+spielfeld.getSizeX());
					wohinx = spielfeld.getSizeX()-1;
					wohiny= bot.getBotY();
					break;					
				} else {
					wohinx=bot.getBotX()-1;
					wohiny = bot.getBotY();
					break;
				}
			}
			
			System.err.println("Will Feld bei x:" + wohinx + "|" + wohiny + "anlegen");
			
				
				// prüfen ob das FEld exisitert
			boolean feldExistiert =false;
			Feld temp = null;
			for (Feld feld : spielfeld.getFelder()) {
				if (feld.getxKoordinate()== wohinx && feld.getyKoordinate() == wohiny) {
					feldExistiert = true;
					temp = feld;
					break;
				}
			}
			System.err.println(feldExistiert);
			if (feldExistiert == true) {
				switch (richtungFeldErstellen) {
				case 'n':
					bot.getAktuellesFeld().setNorth(temp);
					temp.setSouth(bot.getAktuellesFeld());
					break;
				case 'e':
					bot.getAktuellesFeld().setEast(temp);
					temp.setWest(bot.getAktuellesFeld());	
					break;
				case 's':
					bot.getAktuellesFeld().setSouth(temp);
					temp.setNorth(bot.getAktuellesFeld());
					break;
				case 'w':
					bot.getAktuellesFeld().setWest(temp);
					temp.setEast(bot.getAktuellesFeld());
					break;
				}
			} else {
				System.err.println("ich bin richtig");
				Feld neuesFeld = new Feld(wohinx, wohiny);
				switch (richtungFeldErstellen) {
				case 'n':
					bot.getAktuellesFeld().setNorth(neuesFeld);
					neuesFeld.setSouth(bot.getAktuellesFeld());
					break;
				case 'e':
					bot.getAktuellesFeld().setEast(neuesFeld);
					neuesFeld.setWest(bot.getAktuellesFeld());	
					break;
				case 's':
					bot.getAktuellesFeld().setSouth(neuesFeld);
					neuesFeld.setNorth(bot.getAktuellesFeld());
					break;
				case 'w':
					bot.getAktuellesFeld().setWest(neuesFeld);
					neuesFeld.setEast(bot.getAktuellesFeld());
					break;
				}
				this.spielfeld.addFeld(neuesFeld);
				if (!spielfeld.getBekannteFelder().contains(neuesFeld)) {
					spielfeld.addUnbekanntesFeld(neuesFeld);		
				}
			}
	}
	
}
			
			

	
	

