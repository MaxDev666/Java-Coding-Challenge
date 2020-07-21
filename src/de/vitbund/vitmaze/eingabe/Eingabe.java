package de.vitbund.vitmaze.eingabe;

import java.util.Scanner;
/**
 * Diese Klasse verarbeitet Eingabedaten der Konsole bzw in diesem Fall vom Spiel
 * @author Benjamin Bogusch, Fritz Köhler, Florian Kreibe, Maximilian Hett
 * @version 1.5
 *
 */
public class Eingabe {
		static Scanner scanner = new Scanner(System.in);
		
		public static String leseZeile() {
			return scanner.nextLine();
		}
		
		public static int leseZahl() {
			return scanner.nextInt();
		}
		
		public static boolean isnaechsterEintragvorhanden() {
			return scanner.hasNext();
		}
		//damit sich der Scanner schließt
		public static void closeScanner() {
			scanner.close();
		}
		
}
