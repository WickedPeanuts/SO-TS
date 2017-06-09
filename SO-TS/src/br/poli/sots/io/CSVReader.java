package br.poli.sots.io;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CSVReader {
	
	public static List<String> ReadFile(String fileName){
		try {
			return Files.readAllLines(Paths.get(new File("").getCanonicalPath() + "/Bases/" + fileName + ".csv"));
		} catch (Exception e){
			System.out.println("Arquivo não encontrado!");
			return null;
		}
	}
}
