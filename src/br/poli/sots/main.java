package br.poli.sots;

import java.util.LinkedList;
import java.util.List;

import br.poli.sots.arma.Arma;
import br.poli.sots.arma.Common;
import br.poli.sots.arma.ParametrizedArma;
import br.poli.sots.swarmintelligence.ffa.utils.FireflyParticle;
import br.poli.sots.swarmintelligence.fss.FishSchoolSearch;
import br.poli.sots.swarmintelligence.pso.utils.AbstractPSOParticle;
import br.poli.sots.swarmintelligence.pso.utils.EConstrictionFactor;
import br.poli.sots.swarmintelligence.pso.utils.ETopology;
import br.poli.sots.swarmintelligence.pso.utils.Swarm;
import br.poli.sots.swarmintelligence.utils.AbstractFunction;
import br.poli.sots.swarmintelligence.utils.EFunction;
import br.poli.sots.swarmintelligence.utils.MeanSquareError;
import br.poli.sots.swarmintelligence.utils.NewMSE;
import br.poli.sots.utils.logging.StaticLogger;
import br.poli.sots.utils.serie.EOptimizer;
import br.poli.sots.utils.serie.Series;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		Series.armaSerie = new Arma(Series.emborcacao, 88.3, 1, 1, 1, 1);
		//Series.crarmaSerie = new ParametrizedArma(Series.emborcacao, 123);
		//Series.crarmaSerie = new CustomRegressionArma(Series.emborcacao, 98.9);
		
		PSO();
	}
	
	public static void FSS(){
		MeanSquareError RMSE = (MeanSquareError) AbstractFunction.instanceFunction(EFunction.MeanSquareError);
		FishSchoolSearch fss = new FishSchoolSearch(RMSE);
		fss.Busca(10000, 1);
		double[] particlePos = fss.getBestPosition();

		Series.armaSerie.setParameters(particlePos[0], particlePos[1], particlePos[2], particlePos[3]);
		Series.armaSerie.forecastAll();
		
		System.out.println(Series.armaSerie.serie.comparingSet + "\n" + Series.armaSerie.serie.forecastSet);
		System.out.println(Series.armaSerie.serie.comparingSet.size() + "\n" + Series.armaSerie.serie.forecastSet.size());
		System.out.println(particlePos[0] + " " + particlePos[1] + " " + particlePos[2] + " " + particlePos[3]);
	}
	
	public static void FFA(){
		br.poli.sots.swarmintelligence.ffa.utils.Swarm s = new br.poli.sots.swarmintelligence.ffa.utils.Swarm(EFunction.MeanSquareError);
		
		s.initializeSwarm();
		s.updatePopulation(true);
		double[] particlePos = FireflyParticle.globalBestPosition;
		
		
		Series.armaSerie.setParameters(particlePos[0], particlePos[1], particlePos[2], particlePos[3]);
		Series.armaSerie.forecastAll();
		
		System.out.println(Series.armaSerie.serie.comparingSet + "\n" + Series.armaSerie.serie.forecastSet);
		System.out.println(Series.armaSerie.serie.comparingSet.size() + "\n" + Series.armaSerie.serie.forecastSet.size());
		System.out.println(particlePos[0] + " " + particlePos[1] + " " + particlePos[2] + " " + particlePos[3]);
	}
	
	public static void PSO(){
		for (int bkwrd = 0; bkwrd <= 3; bkwrd++){
			for (int ffrd = 1; ffrd <= 3; ffrd++){
				
				//Adicionar label do logger
				StaticLogger.add("Feedfoward: " + ffrd + ", Backward: " + bkwrd, EOptimizer.PSO);
				
				//Adição da média da lista de convergências
				Series.crarmaSerie = new ParametrizedArma(Series.emborcacao, 120, ffrd, bkwrd);
				
				Swarm s = new Swarm(ETopology.Global, EFunction.NewMSE, EConstrictionFactor.ClercConstriction);
				
				List<List<Double>> convergencePerIteration = new LinkedList<List<Double>>();
				List<Double> bestErrorList = new LinkedList<Double>();
				
				for (int k = 0; k < Parameters.SAMPLE_COUNT; k++){
					s.InitializeSwarm();
					s.updatePopulation(true);
					convergencePerIteration.add(s.globalBestLog);
					bestErrorList.add(s.globalBestLog.get(s.globalBestLog.size() - 1));
				}
			    StaticLogger.add(Common.CalculateAverageConvergence(convergencePerIteration), EOptimizer.PSO, "Convergence " + AbstractPSOParticle.functionType + ": {", "}");
			    
			    //Erros totais
			    StaticLogger.add(bestErrorList, EOptimizer.PSO, AbstractPSOParticle.functionType + " per iteration: {", "}");
			    
			    //MSE da best			
			    Series.crarmaSerie = new ParametrizedArma(Series.emborcacao, 120, ffrd, bkwrd);
				Series.crarmaSerie.setParameters(AbstractPSOParticle.positionGBest);
				Series.crarmaSerie.forecastAll();
				StaticLogger.add(AbstractPSOParticle.positionGBest, EOptimizer.PSO, "Parameters (" + ffrd + "Feedfoward, " + ffrd + " Backward) : {", "}");
				
				//As séries desazonalizada
				StaticLogger.add(Series.armaSerie.serie.comparingSet, EOptimizer.PSO, "Comparing (Deseasonalized) set: {", "}");
				StaticLogger.add(Series.armaSerie.serie.forecastSet, EOptimizer.PSO, "Forecastset (Deseasonalized) set: {", "}");
				
				//As séries desazonalizadas
				Series.crarmaSerie.seasonalizeSerie();
				StaticLogger.add(Series.armaSerie.serie.comparingSet, EOptimizer.PSO, "Comparing (Seasonalized) set: {", "}");
				StaticLogger.add(Series.armaSerie.serie.forecastSet, EOptimizer.PSO, "Forecastset (Seasonalized) set: {", "}");
				
				//Erro quadrático médio (best)
				Double mseError = NewMSE.instance.calculateFitness(AbstractPSOParticle.positionGBest);
				StaticLogger.add(mseError, EOptimizer.PSO, "Best Mean (Seasonalized) square error: ", "");
				
				//Erro absoluto médio (best)
				Double maeError = NewMSE.instance.calculateFitness(AbstractPSOParticle.positionGBest);
				StaticLogger.add(maeError, EOptimizer.PSO, "Best Mean (Seasonalized) absolute error: ", "");
				
				System.out.println("\n\n");
				for (String ss : StaticLogger.psoLog){
					System.out.println(ss);
				}
				StaticLogger.psoLog.clear();
				
				//System.out.println(Series.armaSerie.serie.comparingSet + "\n" + Series.armaSerie.serie.forecastSet);
				//System.out.println(Series.armaSerie.serie.comparingSet.size() + "\n" + Series.armaSerie.serie.forecastSet.size());
				//System.out.println(particlePos);
				
				
			}
		}
	}
}
