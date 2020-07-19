package de.vitbund.vitmaze.eingabe;

import java.util.Scanner;
/**
 * Verarbeitet Eingabedaten der Konsole
 * @author Arbeitstitel
 * @version 1.0
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
		//damit sich der Scanner schlieﬂt
		public static void closeScanner() {
			scanner.close();
		}
		
}
