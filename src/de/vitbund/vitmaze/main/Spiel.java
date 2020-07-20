package de.vitbund.vitmaze.main;

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
	String ausgabe;
	Formular[] forms;
	int anzahlFormulare;
	int formID;
	int formcounter;
	boolean allesGesammelt;
	boolean rundeZuEnde;
	boolean sheetgelegt;

	
	public void init() {
		// Spielfeld anlegen und Startdaten setzen
		spielfeld = new Spielfeld();
		spielfeld.setSizeX(Eingabe.leseZahl()); // X-Groesse des Spielfeldes (Breite)
		spielfeld.setSizeY(Eingabe.leseZahl()); // Y-Groesse des Spielfeldes (Hoehe)
		spielfeld.setLevel(Eingabe.leseZahl()); // Level des Matches
		Eingabe.leseZeile();

		// Bot erstellen
		bot = new Standardbot(spielfeld);
		bot.setPlayerId(Eingabe.leseZahl());// id dieses Players / Bots
		bot.setBotX(Eingabe.leseZahl()); // X-Koordinate der Startposition dieses Player
		bot.setBotY(Eingabe.leseZahl()); // Y-Koordinate der Startposition dieses Players
		if (spielfeld.getLevel()==5) {
			bot.setSheetCount(Eingabe.leseZahl());
		}
		Eingabe.leseZeile();

		// Startfeld anlegen und zum Spielfeld hinzufügen
		Feld aktuellesFeld =new Feld(bot.getBotX(),bot.getBotY());
		bot.setAktuellesFeld(aktuellesFeld);
		spielfeld.getBekannteFelder().add(aktuellesFeld);
		spielfeld.addFeld(bot.getAktuellesFeld());
		
		// Formulardaten setzen
		anzahlFormulare=999;
		forms = new Formular[26];
		for (Formular form : forms) {
			form = null;
		}
		formID=0;
		formcounter = 1;
		
		
		allesGesammelt = false;
		sheetgelegt = false;
		ausgabe = new String("position");
	}
	
	
	public void getStati() {
		rundeZuEnde = false;

		this.lastActionsResult = Eingabe.leseZeile();
		this.currentCellStatus = Eingabe.leseZeile();
		this.northCellStatus = Eingabe.leseZeile();
		this.eastCellStatus = Eingabe.leseZeile();
		this.southCellStatus = Eingabe.leseZeile();
		this.westCellStatus = Eingabe.leseZeile();

		// get Last Action auswerten

		//getUpdate();
		erkunden();
		//getUpdate();

		if (rundeZuEnde == false) {
			if (sheetgelegt== true) {
				sheetgelegt = false;
			}
			if (bot.hatRoute()) {
				this.ausgabe = bot.move();
				rundeZuEnde = true;
			} else {
				if (spielfeld.getZielfeld() != null && (formcounter-1)==anzahlFormulare) {
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getZielfeld()));
					this.ausgabe = bot.move();
					rundeZuEnde = true;
				} else if (forms[formcounter] != null) {
					if (forms[formcounter].getFeld() == bot.getAktuellesFeld()) {
						if (this.currentCellStatus.startsWith("FORM " + bot.getPlayerId() + " " + formcounter)) {
							this.ausgabe = bot.take();
							formcounter += 1;
							rundeZuEnde = true;
						} else {
							/*bot.sucheUmfeldAb();
							this.ausgabe = bot.move();
							rundeZuEnde = true;*/
							System.err.println("Form suchen");
						}
					} else {
						bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), forms[formcounter].getFeld()));
						this.ausgabe = bot.move();
						rundeZuEnde = true;
					}
				} else if (!spielfeld.getUnbekannteFelder().isEmpty()){
					System.err.println("Unbekanntes Feld" + spielfeld.getUnbekannteFelder().get(0).getxKoordinate() + " " + spielfeld.getUnbekannteFelder().get(0).getyKoordinate());
					bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), spielfeld.getUnbekannteFelder().get(0)));
					this.ausgabe = bot.move();
					rundeZuEnde = true;
				} 
			
			}
		}
		getUpdate();
		System.out.println(this.ausgabe);
	}
	
	public void erkunden() {

		schaueRichtung('n');
		schaueRichtung('e');
		schaueRichtung('s');
		schaueRichtung('w');

		String[] aktFeld = this.currentCellStatus.split(" ");

		if (aktFeld[aktFeld.length - 1].equals("!")) {
			if (bot.isRedetDieRunde() == false) {
				bot.setRedetDieRunde(true);
				rundeZuEnde = true;
			} else {
				bot.setRedetDieRunde(false);
			}
		}

		if (rundeZuEnde == false) {
			if (aktFeld[0].equals("FORM")) {
				if (aktFeld[1].equals(bot.getPlayerId() + "") && aktFeld[2].equals(formcounter + "")) {
					this.ausgabe = bot.take();
					formcounter += 1;
					rundeZuEnde = true;
				} else if (!aktFeld[1].equals(bot.getPlayerId() + "")) {
					if (bot.getSheetCount() > 0) {
						bot.setSheetCount(bot.getSheetCount() - 1);
						this.ausgabe = bot.put();
						sheetgelegt = true;
						rundeZuEnde = true;
					} else {
						String kickMöglich = bot.kick(this.northCellStatus, this.eastCellStatus, this.southCellStatus, this.westCellStatus, false);
						if (!kickMöglich.equals("fail")) {
							this.ausgabe = kickMöglich;
							rundeZuEnde = true;
						}
					}
				}
			} else if (aktFeld[0].equals("SHEET") && sheetgelegt == false) {
				String kickmöglich = bot.kick(this.northCellStatus, this.eastCellStatus, this.southCellStatus,
						this.westCellStatus, true);
				if (!kickmöglich.equals("fail")) {
					this.ausgabe = kickmöglich;
					rundeZuEnde = true;
				}
			} else if (aktFeld[0].equals("FINISH") && aktFeld[1].equals(bot.getPlayerId() + "")) {
				if (allesGesammelt) {
					this.ausgabe = bot.finish();
					rundeZuEnde = true;
				}
			}
		}
	}

	public void schaueRichtung(char richtung) {
		String[] cellStatusArray = getCellStatus(richtung).split(" ");
		if (bot.getAktuellesFeld().getFeld(richtung) == null) {
			if (!cellStatusArray[0].equals("WALL")) {
				this.erstellFeld(richtung);
			}
		}

		if (cellStatusArray[0].equals("FINISH") && cellStatusArray[1].equals(bot.getPlayerId() + "")) {
			anzahlFormulare = Integer.parseInt(cellStatusArray[2]);
			if ( (formcounter-1) == anzahlFormulare) {
				allesGesammelt = true;
				System.err.println("HABE ALLES GESAMMELT UND GEHE ZUM ZIEL ZU FELD " + spielfeld.getZielfeld());
				bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), bot.getAktuellesFeld().getFeld(richtung)));
				this.ausgabe = bot.move();
				rundeZuEnde = true;
			} else {
				spielfeld.setZielfeld(bot.getAktuellesFeld().getFeld(richtung));
			}
		}
		
		if (cellStatusArray[0].equals("FORM") && cellStatusArray[1].equals(bot.getPlayerId()+ "")) {
			formID = Integer.parseInt(cellStatusArray[2]);
			if (forms[formID] == null) {
				Formular formular = new Formular(formID, bot.getAktuellesFeld().getFeld(richtung));
				forms[formID] = formular;
			}
			if (formID == formcounter) {
				bot.setAktuelleRoute(spielfeld.route(bot.getAktuellesFeld(), bot.getAktuellesFeld().getFeld(richtung)));
				this.ausgabe = bot.move();
				rundeZuEnde = true;
			}
		}

		if (cellStatusArray[0].equals("SHEET")) {
			spielfeld.getSheetList().add(bot.getAktuellesFeld().getFeld(richtung));
		}
	}
	
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
	
	public void erstellFeld(char richtungFeldErstellen) {
		int wohinx = 0;
		int wohiny = 0;
		switch (richtungFeldErstellen) {
		case 'n':
			//System.err.println("Nordkoordinaten: " + bot.getBotY() + " " + spielfeld.getSizeY());
			if (bot.getBotY() - 1 < 0) {
				wohinx = bot.getBotX();
				wohiny = spielfeld.getSizeY() - 1;
				break;
			} else {
				wohinx = bot.getBotX();
				wohiny = bot.getBotY() - 1;
				break;
			}
		case 'e':
			//System.err.println("Ostkoordinaten: " + bot.getBotX() + " " + spielfeld.getSizeX());
			if (bot.getBotX() + 1 == spielfeld.getSizeX()) {
				wohinx = 0;
				wohiny = bot.getBotY();
				break;
			} else {
				wohinx = bot.getBotX() + 1;
				wohiny = bot.getBotY();
				break;
			}
		case 's':
			//System.err.println("Suedkoordinaten: " + bot.getBotY() + " " + spielfeld.getSizeY());
			if (bot.getBotY() + 1 == spielfeld.getSizeY()) {
				wohinx = bot.getBotX();
				wohiny = 0;
				break;
			} else {
				wohinx = bot.getBotX();
				wohiny = bot.getBotY() + 1;
				break;
			}
		case 'w':
			//System.err.println("Westkoordinaten: " + bot.getBotX() + " " + spielfeld.getSizeX());
			if (bot.getBotX() - 1 < 0) {
				wohinx = spielfeld.getSizeX() - 1;
				wohiny = bot.getBotY();
				break;
			} else {
				wohinx = bot.getBotX() - 1;
				wohiny = bot.getBotY();
				break;
			}
		}

		System.err.println("Will Feld bei " + wohinx + "|" + wohiny + " anlegen");

		// prüfen ob das FEld exisitert
		boolean feldExistiert = false;
		Feld temp = null;
		for (Feld feld : spielfeld.getFelder()) {
			if (feld.getxKoordinate() == wohinx && feld.getyKoordinate() == wohiny) {
				feldExistiert = true;
				temp = feld;
				break;
			}
		}
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
	
public void getUpdate() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("###################################################################" + "\n");
		sb.append("Ich stehe auf dem Feld: " + bot.getAktuellesFeld().getxKoordinate() + "|"+ bot.getAktuellesFeld().getyKoordinate() + "\n");
		sb.append("Meine Koordinaten lauten : " + bot.getBotX() + "|" + bot.getBotY() + "\n");
		sb.append("Mein Cellstatus: "+currentCellStatus+"\n");
		sb.append("Ich habe noch " + bot.getSheetCount() + " Sheets\n");
		sb.append("Redet der Bot "+bot.isRedetDieRunde()+"\n");
		sb.append("Im Norden ist: " + bot.getAktuellesFeld().getNorth()+ "\n");
		if (bot.getAktuellesFeld().getNorth()!=null) {
			sb.append("Mit den Koordinaten: " + bot.getAktuellesFeld().getNorth().getxKoordinate() +"|"+ bot.getAktuellesFeld().getNorth().getyKoordinate() + "\n");
		}
		sb.append("Im Osten ist: " + bot.getAktuellesFeld().getEast()+ "\n");
		if (bot.getAktuellesFeld().getEast()!=null) {
			sb.append("Mit den Koordinaten: " + bot.getAktuellesFeld().getEast().getxKoordinate() +"|"+ bot.getAktuellesFeld().getEast().getyKoordinate() + "\n");
		}
		sb.append("Im Sueden ist : " + bot.getAktuellesFeld().getSouth()+ "\n");
		if (bot.getAktuellesFeld().getSouth()!=null) {
		sb.append("Mit den Koordinaten: " + bot.getAktuellesFeld().getSouth().getxKoordinate() +"|"+ bot.getAktuellesFeld().getSouth().getyKoordinate() + "\n");
		}
		sb.append("Im Westen ist: " + bot.getAktuellesFeld().getWest()+ "\n");
		if (bot.getAktuellesFeld().getWest()!=null) {
			sb.append("Mit den Koordinaten:: " + bot.getAktuellesFeld().getWest().getxKoordinate() +"|"+ bot.getAktuellesFeld().getWest().getyKoordinate() + "\n");
		}
		sb.append("Die Route sieht folgendermassen aus: \n");
		if (bot.hatRoute()) {
			for (Feld f : bot.getAktuelleRoute()) {
				sb.append("Feld bei Koordinaten: " +f.getxKoordinate() + "|" + f.getyKoordinate() + " ->");
			}
			sb.append("\nIch habe eine Route und will zum Feld: " + bot.getAktuelleRoute().get(0).getxKoordinate() + "|" + bot.getAktuelleRoute().get(0).getyKoordinate()+ "\n");
		} else { sb.append("Ich habe noch KEINE ROUTE" + "\n");}
		if (spielfeld.getZielfeld()!=null) {
			sb.append("Feld: " + spielfeld.getZielfeld().getxKoordinate() + "|" + spielfeld.getZielfeld().getyKoordinate() + " ist das Zielfeld \n");
		}
		if (anzahlFormulare != 999) {
			sb.append("Ich weiss wie viele Formulare brauche, dies sind: "+anzahlFormulare+"\n");
		}
		sb.append("Ich habe bereits "+howManyForms()+" Formulate\n");
		sb.append("Mein Formcounter ist "+formcounter+"\n");
		
		System.err.println(sb.toString());
	}
	
}
