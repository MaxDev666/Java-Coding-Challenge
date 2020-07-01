package de.vitbund.vitmaze.eingabe;

import java.util.Scanner;

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
		
		public static void closeScanner() {
			scanner.close();
		}
		
}
