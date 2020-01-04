package utils;

import feature.MainTableElement;

import java.util.ArrayList;

public class PrintUtils {
	public static void printTable(MainTableElement[][] table) {
		System.out.println("Main global table:");
		System.out.println("--------------------------");
		for (int i = 0; i < table[0].length; i++) {
			for (MainTableElement[] mainTableElements : table) {
				System.out.print(mainTableElements[i].getValue() + "\t");
			}
			System.out.println("");
		}
		System.out.println("--------------------------");
	}

	public static void printResults(ArrayList<String> resultsList) {
		System.out.println("Global results: " + resultsList.size() / 2);
		System.out.println("--------------------------");
		int i = 0;
		for (String item : resultsList) {
			System.out.println(item);
			if (i++ % 2 == 1) System.out.println("--------");
		}
		System.out.println("--------------------------");
	}
}