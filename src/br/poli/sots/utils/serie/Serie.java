package br.poli.sots.utils.serie;

import java.util.LinkedList;
import java.util.List;

import br.poli.sots.arma.Common;
import br.poli.sots.io.CSVReader;

public class Serie {

	//Série (em ordem) classificada por critério de ano
	public List<SerieYear> timedSerie;

	//Série (em ordem) sem classificação 
	public List<Double> fullSerie;

	public List<Double> trainingSet;
	public List<Double> comparingSet;
	public List<Double> forecastSet;

	//public List<Double> minimumPerMonth;
	//public List<Double> maximumPerMonth;
	//public List<Double> averagePerMonth;
	
	public double[] standardDeviation;
	public double[] average;

	//int minimumPerMonthAverage, averagePerMonthAverage, maximumPerMonthAverage;

	public Serie(String fileName){
		List<String> textLines = CSVReader.ReadFile(fileName);

		timedSerie = new LinkedList<SerieYear>();
		fullSerie = new LinkedList<Double>();
		//minimumPerMonth = new LinkedList<Double>();
		//maximumPerMonth = new LinkedList<Double>();
		//averagePerMonth = new LinkedList<Double>();
		trainingSet = new LinkedList<Double>();
		forecastSet = new LinkedList<Double>();
		
		standardDeviation = new double[12];
		average = new double[12];

		for(String line : textLines){
			String[] arrayLine = line.split(",");

			//Caso seja letra, ignore a leitura
			if (arrayLine[0].equals("ANO")) continue;

			//Inicia a lista de mínimos
			if (arrayLine[0].equals("MIN")){
				/*
				for (int i = 1; i < arrayLine.length - 1; i++){
					minimumPerMonth.add(Double.parseDouble(arrayLine[i]));
				}

				minimumPerMonthAverage = Integer.parseInt(arrayLine[arrayLine.length - 1]);
				*/
				continue;
			}

			//Inicia a lista de médias
			if (arrayLine[0].equals("MED")) {
				/*
				for (int i = 1; i < arrayLine.length - 1; i++){
					averagePerMonth.add(Double.parseDouble(arrayLine[i]));
				}

				averagePerMonthAverage = Integer.parseInt(arrayLine[arrayLine.length - 1]);
				*/
				continue;
			}

			//Inicia a lista de máximos
			if (arrayLine[0].equals("MAX")) {
				/*
				for (int i = 1; i < arrayLine.length - 1; i++){
					maximumPerMonth.add(Double.parseDouble(arrayLine[i]));
				}

				maximumPerMonthAverage = Integer.parseInt(arrayLine[arrayLine.length - 1]);
				*/
				continue;
			}

			SerieYear serieYear = new SerieYear();

			serieYear.year = Integer.parseInt(arrayLine[0]);
			for (int i = 1; i < arrayLine.length - 1; i++){
				serieYear.riverFlow.add(Double.parseDouble(arrayLine[i]));
			}
			serieYear.average = Integer.parseInt(arrayLine[arrayLine.length - 1]);

			fullSerie.addAll(serieYear.riverFlow);
			timedSerie.add(serieYear);
		}


	}

	//TODO pegar a média/desvio padrão em double
	public void deseasonalize() {

		//Collect all standardDeviation and average from each month;
		for (int i = 0; i < standardDeviation.length; i++){
			List<Double> monthFlow = new LinkedList<Double>(); 

			for(SerieYear sy : timedSerie){
				monthFlow.add(sy.riverFlow.get(i));
			}

			average[i] = Common.CalculateAverage(monthFlow);
			standardDeviation[i] = Common.CalculateStandardDeviation(monthFlow);
		}

		//Replace with padronized serie
		for(SerieYear sy : timedSerie){

			for(int month = 0; month < 12; month++){
				sy.riverFlow.set(month,
						(sy.riverFlow.get(month) - average[month])/standardDeviation[month]
						);
			}
		}

		//Set values to fullSerie
		fullSerie.clear();
		timedSerie.forEach(x -> fullSerie.addAll(x.riverFlow));
	}

	//TODO ajeitar
	public void seasonalize(){
		
		for(SerieYear sy : timedSerie){
			for(int month = 0; month < 12; month++){
				sy.riverFlow.set(month,
						(sy.riverFlow.get(month)*standardDeviation[month] + average[month])
						);
			}
		}
		
		for (int i = fullSerie.size() - forecastSet.size(), j = 0; i + j < fullSerie.size(); j++){
			int month = ((i + j) % 11);
			forecastSet.set(j, forecastSet.get(j)*standardDeviation[month] + average[month]);
		}

		//Set values to fullSerie
		fullSerie.clear();
		timedSerie.forEach(x -> fullSerie.addAll(x.riverFlow));
	}
}