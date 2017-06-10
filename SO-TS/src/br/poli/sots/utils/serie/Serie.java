package br.poli.sots.io;

import java.util.LinkedList;
import java.util.List;

public class Serie {

	//S�rie (em ordem) classificada por crit�rio de ano
	public List<SerieYear> timedSerie;
	
	//S�rie (em ordem) sem classifica��o 
	public List<Integer> fullSerie;
	
	public List<Integer> minimumPerMonth;
	public List<Integer> maximumPerMonth;
	public List<Integer> averagePerMonth;
	
	int minimumPerMonthAverage, averagePerMonthAverage, maximumPerMonthAverage;
		
	public Serie(String fileName){
		List<String> textLines = CSVReader.ReadFile(fileName);

		timedSerie = new LinkedList<SerieYear>();
		fullSerie = new LinkedList<Integer>();
		minimumPerMonth = new LinkedList<Integer>();
		maximumPerMonth = new LinkedList<Integer>();
		averagePerMonth = new LinkedList<Integer>();
				
		for(String line : textLines){
			String[] arrayLine = line.split(",");
			
			//Caso seja letra, ignore a leitura
			if (arrayLine[0].equals("ANO")) continue;
			
			//Inicia a lista de m�nimos
			if (arrayLine[0].equals("MIN")){
				for (int i = 1; i < arrayLine.length - 1; i++){
					minimumPerMonth.add(Integer.parseInt(arrayLine[i]));
				}
				
				minimumPerMonthAverage = Integer.parseInt(arrayLine[arrayLine.length - 1]);
				continue;
			}
			
			//Inicia a lista de m�dias
			if (arrayLine[0].equals("MED")) {
				for (int i = 1; i < arrayLine.length - 1; i++){
					averagePerMonth.add(Integer.parseInt(arrayLine[i]));
				}
				
				averagePerMonthAverage = Integer.parseInt(arrayLine[arrayLine.length - 1]);
				continue;
			}
			
			//Inicia a lista de m�ximos
			if (arrayLine[0].equals("MAX")) {
				for (int i = 1; i < arrayLine.length - 1; i++){
					maximumPerMonth.add(Integer.parseInt(arrayLine[i]));
				}
				
				maximumPerMonthAverage = Integer.parseInt(arrayLine[arrayLine.length - 1]);
				continue;
			}
			
			SerieYear serieYear = new SerieYear();
			
			serieYear.year = Integer.parseInt(arrayLine[0]);
			for (int i = 1; i < arrayLine.length - 1; i++){
				serieYear.riverFlow.add(Integer.parseInt(arrayLine[i]));
			}
			serieYear.average = Integer.parseInt(arrayLine[arrayLine.length - 1]);
			
			fullSerie.addAll(serieYear.riverFlow);
			timedSerie.add(serieYear);
		}
		
		
	}
}
