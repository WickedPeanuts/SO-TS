package br.poli.sots;

import java.util.LinkedList;
import java.util.List;

import br.poli.sots.arma.Arma;
import br.poli.sots.arma.Common;
import br.poli.sots.swarmintelligence.abc.AbstractBeeParticle;
import br.poli.sots.swarmintelligence.ffa.utils.FireflyParticle;
import br.poli.sots.swarmintelligence.fss.FishSchoolSearch;
import br.poli.sots.swarmintelligence.pso.utils.AbstractPSOParticle;
import br.poli.sots.swarmintelligence.pso.utils.EConstrictionFactor;
import br.poli.sots.swarmintelligence.pso.utils.ETopology;
import br.poli.sots.swarmintelligence.pso.utils.Swarm;
import br.poli.sots.swarmintelligence.utils.AbstractFunction;
import br.poli.sots.swarmintelligence.utils.EFunction;
import br.poli.sots.swarmintelligence.utils.MeanAbsoluteError;
import br.poli.sots.swarmintelligence.utils.MeanSquareError;
import br.poli.sots.swarmintelligence.utils.RootMeanSquareError;
import br.poli.sots.utils.logging.StaticLogger;
import br.poli.sots.utils.serie.EOptimizer;
import br.poli.sots.utils.serie.Serie;
import br.poli.sots.utils.serie.Series;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//RunAllPSOSimulations(Series.emborcacao);
		//FFA(EFunction.MeanSquareError, Series.emborcacao);
		//FFA(EFunction.MeanSquareError, Series.furnas);
		//FFA(EFunction.MeanSquareError, Series.sobradinho);
		
		//StaticLogger.saveToFile();
		
		FSS();
		StaticLogger.saveToFile();
	}
	
	public static void FSS(){
		MeanSquareError RMSE = (MeanSquareError) AbstractFunction.instanceFunction(EFunction.MeanSquareError);
		
		FishSchoolSearch fss = new FishSchoolSearch(EFunction.RootMeanSquareError);
		
		StaticLogger.add("", EOptimizer.FSS);
		for (int bkwrd = 0; bkwrd <= 3; bkwrd++){
			for (int ffrd = 1; ffrd <= 3; ffrd++){
				if (firstTry && bkwrd == 0) continue;
				Parameters.UpdateParameters(ffrd, bkwrd);
				
				StaticLogger.add("Feedfoward: " + ffrd + ", Backward: " + bkwrd, EOptimizer.FSS);
				System.out.println("Feedfoward: " + ffrd + ", Backward: " + bkwrd);
				
				Series.armaSerie = new Arma(Series.emborcacao, 120, ffrd, bkwrd);
				fss.Busca(100, 1);
				
				List<List<Double>> convergencePerIteration = new LinkedList<List<Double>>();
				List<Double> bestErrorList = new LinkedList<Double>();
				for (int k = 0; k < Parameters.SAMPLE_COUNT; k++){
					convergencePerIteration.add(fss.getListBestFitness());
					bestErrorList.add(fss.getBestFitness());
				}
				
				 StaticLogger.add(Common.CalculateAverageConvergence(convergencePerIteration), EOptimizer.FSS, "Convergence " + " FSS " + ": {", "}");
				 
				//Erros totais
				    StaticLogger.add(bestErrorList, EOptimizer.FSS, " per iteration: {", "}");
				 //MSE da best			
				  Series.armaSerie = new Arma(Series.emborcacao, 120, ffrd, bkwrd);
				  Series.armaSerie.setParameters(AbstractPSOParticle.positionGBest);
				  Series.armaSerie.forecastAll();
				  StaticLogger.add(AbstractPSOParticle.positionGBest, EOptimizer.PSO, "Parameters (" + ffrd + "Feedfoward, " + ffrd + " Backward) : {", "}");
				 
				
				//As séries desazonalizada
					StaticLogger.add(Series.armaSerie.serie.comparingSet, EOptimizer.FSS, "Comparing (Deseasonalized) set: {", "}");
					StaticLogger.add(Series.armaSerie.serie.forecastSet, EOptimizer.FSS, "Forecastset (Deseasonalized) set: {", "}");
					
					//Erro quadrático médio (best) desazonalizado
					Double mseError = MeanSquareError.instance.calculateFitness(fss.getBestPosition());
					StaticLogger.add(mseError, EOptimizer.FSS, "Best Mean (Deseasonalized) mse: ", "");
					
					//Erro absoluto médio (best) desazonalizado
					Double maeError = MeanAbsoluteError.instance.calculateFitness(fss.getBestPosition());
					StaticLogger.add(maeError, EOptimizer.FSS, "Best Mean (Deseasonalized) mae: ", "");
					
					//Erro raiz quadrático (best) desazonalizado
					Double rmseError = RootMeanSquareError.instance.calculateFitness(fss.getBestPosition());
					StaticLogger.add(rmseError, EOptimizer.FSS, "Best Mean (Deseasonalized) rmse: ", "");
					
					//As séries desazonalizadas
					Series.armaSerie.seasonalizeSerie();
					StaticLogger.add(Series.armaSerie.serie.comparingSet, EOptimizer.FSS, "Comparing (Seasonalized) set: {", "}");
					StaticLogger.add(Series.armaSerie.serie.forecastSet, EOptimizer.FSS, "Forecastset (Seasonalized) set: {", "}");
					
					//Erro quadrático médio (best) sazonalizado
					mseError = MeanSquareError.instance.calculateFitness(fss.getBestPosition());
					StaticLogger.add(mseError, EOptimizer.FSS, "Best Mean (Seasonalized) mse: ", "");
					
					//Erro absoluto médio (best) sazonalizado
					maeError = MeanAbsoluteError.instance.calculateFitness(fss.getBestPosition());
					StaticLogger.add(maeError, EOptimizer.FSS, "Best Mean (Seasonalized) mae: ", "");
					
					//Erro raiz quadrático (best) desazonalizado
					rmseError = RootMeanSquareError.instance.calculateFitness(fss.getBestPosition());
					StaticLogger.add(rmseError, EOptimizer.FSS, "Best Mean (Seasonalized) rmse: ", "");
					
					StaticLogger.add("\n", EOptimizer.FSS);
					
					
				
			}
		}
		
		double[] particlePos = fss.getBestPosition();

		Series.armaSerie.setParameters(particlePos);
		Series.armaSerie.forecastAll();
		
		System.out.println(Series.armaSerie.serie.comparingSet + "\n" + Series.armaSerie.serie.forecastSet);
		System.out.println(Series.armaSerie.serie.comparingSet.size() + "\n" + Series.armaSerie.serie.forecastSet.size());
		System.out.println(particlePos[0] + " " + particlePos[1] + " " + particlePos[2] + " " + particlePos[3]);
	}
	
	public static void RunAllPSOSimulations(Serie serie){
		int i = 1;
		PSO(ETopology.Global, EFunction.MeanSquareError, EConstrictionFactor.FixedInertia, serie);
		System.out.println(i++ + "/9 PSO simulations complete!");
		PSO(ETopology.Focal, EFunction.MeanSquareError, EConstrictionFactor.FixedInertia, serie);
		System.out.println(i++ + "/9 PSO simulations complete!");
		PSO(ETopology.Ring, EFunction.MeanSquareError, EConstrictionFactor.FixedInertia, serie);
		System.out.println(i++ + "/9 PSO simulations complete!");
		PSO(ETopology.Global, EFunction.MeanSquareError, EConstrictionFactor.FloatingInertia, serie);
		System.out.println(i++ + "/9 PSO simulations complete!");
		PSO(ETopology.Focal, EFunction.MeanSquareError, EConstrictionFactor.FloatingInertia, serie);
		System.out.println(i++ + "/9 PSO simulations complete!");
		PSO(ETopology.Ring, EFunction.MeanSquareError, EConstrictionFactor.FloatingInertia, serie);
		System.out.println(i++ + "/9 PSO simulations complete!");
		PSO(ETopology.Global, EFunction.MeanSquareError, EConstrictionFactor.ClercConstriction, serie);
		System.out.println(i++ + "/9 PSO simulations complete!");
		PSO(ETopology.Focal, EFunction.MeanSquareError, EConstrictionFactor.ClercConstriction, serie);
		System.out.println(i++ + "/9 PSO simulations complete!");
		PSO(ETopology.Ring, EFunction.MeanSquareError, EConstrictionFactor.ClercConstriction, serie);
		System.out.println(i++ + "/9 PSO simulations complete!");
		
	}
	
	public static boolean firstTry = true;
	
	public static void PSO(ETopology topology, EFunction function, EConstrictionFactor constriction, Serie serie){
		StaticLogger.add(topology + ", " + function + ", " + constriction + "\n", EOptimizer.PSO);
		
		for (int bkwrd = 0; bkwrd <= 3; bkwrd++){
			for (int ffrd = 1; ffrd <= 3; ffrd++){
				if (firstTry && bkwrd == 0) continue;
				//Adicionar label do logger
				StaticLogger.add("Feedfoward: " + ffrd + ", Backward: " + bkwrd, EOptimizer.PSO);
				System.out.println("Feedfoward: " + ffrd + ", Backward: " + bkwrd);
				
				//Adi��o da m�dia da lista de converg�ncias
				Series.armaSerie = new Arma(serie, 120, ffrd, bkwrd);
				
				Swarm s = new Swarm(topology, function, constriction);
				
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
			    Series.armaSerie = new Arma(serie, 120, ffrd, bkwrd);
				Series.armaSerie.setParameters(AbstractPSOParticle.positionGBest);
				Series.armaSerie.forecastAll();
				StaticLogger.add(AbstractPSOParticle.positionGBest, EOptimizer.PSO, "Parameters (" + ffrd + "Feedfoward, " + ffrd + " Backward) : {", "}");
				
				//As séries desazonalizada
				StaticLogger.add(Series.armaSerie.serie.comparingSet, EOptimizer.PSO, "Comparing (Deseasonalized) set: {", "}");
				StaticLogger.add(Series.armaSerie.serie.forecastSet, EOptimizer.PSO, "Forecastset (Deseasonalized) set: {", "}");
				
				//Erro quadrático médio (best) desazonalizado
				Double mseError = MeanSquareError.instance.calculateFitness(AbstractPSOParticle.positionGBest);
				StaticLogger.add(mseError, EOptimizer.PSO, "Best Mean (Deseasonalized) mse: ", "");
				
				//Erro absoluto médio (best) desazonalizado
				Double maeError = MeanAbsoluteError.instance.calculateFitness(AbstractPSOParticle.positionGBest);
				StaticLogger.add(maeError, EOptimizer.PSO, "Best Mean (Deseasonalized) mae: ", "");
				
				//Erro raiz quadrático (best) desazonalizado
				Double rmseError = RootMeanSquareError.instance.calculateFitness(AbstractPSOParticle.positionGBest);
				StaticLogger.add(rmseError, EOptimizer.PSO, "Best Mean (Deseasonalized) rmse: ", "");
				
				//As séries desazonalizadas
				Series.armaSerie.seasonalizeSerie();
				StaticLogger.add(Series.armaSerie.serie.comparingSet, EOptimizer.PSO, "Comparing (Seasonalized) set: {", "}");
				StaticLogger.add(Series.armaSerie.serie.forecastSet, EOptimizer.PSO, "Forecastset (Seasonalized) set: {", "}");
				
				//Erro quadrático médio (best) sazonalizado
				mseError = MeanSquareError.instance.calculateFitness(AbstractPSOParticle.positionGBest);
				StaticLogger.add(mseError, EOptimizer.PSO, "Best Mean (Seasonalized) mse: ", "");
				
				//Erro absoluto médio (best) sazonalizado
				maeError = MeanAbsoluteError.instance.calculateFitness(AbstractPSOParticle.positionGBest);
				StaticLogger.add(maeError, EOptimizer.PSO, "Best Mean (Seasonalized) mae: ", "");
				
				//Erro raiz quadrático (best) desazonalizado
				rmseError = RootMeanSquareError.instance.calculateFitness(AbstractPSOParticle.positionGBest);
				StaticLogger.add(rmseError, EOptimizer.PSO, "Best Mean (Seasonalized) rmse: ", "");
				
				StaticLogger.add("\n", EOptimizer.PSO);
			}
		}
		
		firstTry = true;
		
		StaticLogger.add("\n\n\n", EOptimizer.PSO);
	}
	
	public static void FFA(EFunction function, Serie serie){
		
		for (int bkwrd = 0; bkwrd <= 3; bkwrd++){
			for (int ffrd = 1; ffrd <= 3; ffrd++){
				if (firstTry && bkwrd == 0) continue;
				//Adicionar label do logger
				StaticLogger.add("Feedfoward: " + ffrd + ", Backward: " + bkwrd, EOptimizer.PSO);
				System.out.println("Feedfoward: " + ffrd + ", Backward: " + bkwrd);
				
				//Adi��o da m�dia da lista de converg�ncias
				Series.armaSerie = new Arma(serie, 120, ffrd, bkwrd);
				
				br.poli.sots.swarmintelligence.ffa.utils.Swarm s = new br.poli.sots.swarmintelligence.ffa.utils.Swarm(function);
				
				List<List<Double>> convergencePerIteration = new LinkedList<List<Double>>();
				List<Double> bestErrorList = new LinkedList<Double>();
				
				for (int k = 0; k < Parameters.SAMPLE_COUNT; k++){
					s.initializeSwarm();
					s.updatePopulation(true);
					convergencePerIteration.add(s.globalBestLog);
					bestErrorList.add(s.globalBestLog.get(s.globalBestLog.size() - 1));
				}
			    StaticLogger.add(Common.CalculateAverageConvergence(convergencePerIteration), EOptimizer.FFA, "Convergence " + FireflyParticle.functionType + ": {", "}");
			    
			    //Erros totais
			    StaticLogger.add(bestErrorList, EOptimizer.FFA, FireflyParticle.functionType + " per iteration: {", "}");
			    
			    //MSE da best			
			    Series.armaSerie = new Arma(serie, 120, ffrd, bkwrd);
				Series.armaSerie.setParameters(FireflyParticle.globalBestPosition);
				Series.armaSerie.forecastAll();
				StaticLogger.add(FireflyParticle.globalBestPosition, EOptimizer.FFA, "Parameters (" + ffrd + "Feedfoward, " + ffrd + " Backward) : {", "}");
				
				//As séries desazonalizada
				StaticLogger.add(Series.armaSerie.serie.comparingSet, EOptimizer.FFA, "Comparing (Deseasonalized) set: {", "}");
				StaticLogger.add(Series.armaSerie.serie.forecastSet, EOptimizer.FFA, "Forecastset (Deseasonalized) set: {", "}");
				
				//Erro quadrático médio (best) desazonalizado
				Double mseError = MeanSquareError.instance.calculateFitness(FireflyParticle.globalBestPosition);
				StaticLogger.add(mseError, EOptimizer.FFA, "Best Mean (Deseasonalized) mse: ", "");
				
				//Erro absoluto médio (best) desazonalizado
				Double maeError = MeanAbsoluteError.instance.calculateFitness(FireflyParticle.globalBestPosition);
				StaticLogger.add(maeError, EOptimizer.FFA, "Best Mean (Deseasonalized) mae: ", "");
				
				//Erro raiz quadrático (best) desazonalizado
				Double rmseError = RootMeanSquareError.instance.calculateFitness(FireflyParticle.globalBestPosition);
				StaticLogger.add(rmseError, EOptimizer.FFA, "Best Mean (Deseasonalized) rmse: ", "");
				
				//As séries desazonalizadas
				Series.armaSerie.seasonalizeSerie();
				StaticLogger.add(Series.armaSerie.serie.comparingSet, EOptimizer.FFA, "Comparing (Seasonalized) set: {", "}");
				StaticLogger.add(Series.armaSerie.serie.forecastSet, EOptimizer.FFA, "Forecastset (Seasonalized) set: {", "}");
				
				//Erro quadrático médio (best) sazonalizado
				mseError = MeanSquareError.instance.calculateFitness(FireflyParticle.globalBestPosition);
				StaticLogger.add(mseError, EOptimizer.FFA, "Best Mean (Seasonalized) mse: ", "");
				
				//Erro absoluto médio (best) sazonalizado
				maeError = MeanAbsoluteError.instance.calculateFitness(FireflyParticle.globalBestPosition);
				StaticLogger.add(maeError, EOptimizer.FFA, "Best Mean (Seasonalized) mae: ", "");
				
				//Erro raiz quadrático (best) desazonalizado
				rmseError = RootMeanSquareError.instance.calculateFitness(FireflyParticle.globalBestPosition);
				StaticLogger.add(rmseError, EOptimizer.FFA, "Best Mean (Seasonalized) rmse: ", "");
				
				StaticLogger.add("\n", EOptimizer.FFA);
			}
		}
		
		firstTry = true;
		
		StaticLogger.add("\n\n\n", EOptimizer.PSO);
	}
	
	public static void ABC(EFunction function, Serie serie){
		StaticLogger.add(function + "\n", EOptimizer.ABC);
		
		for (int bkwrd = 0; bkwrd <= 3; bkwrd++){
			for (int ffrd = 1; ffrd <= 3; ffrd++){
				if (firstTry && bkwrd == 0) continue;
				//Adicionar label do logger
				StaticLogger.add("Feedfoward: " + ffrd + ", Backward: " + bkwrd, EOptimizer.ABC);
				System.out.println("Feedfoward: " + ffrd + ", Backward: " + bkwrd);
				
				//Adição da média da lista de convergências
				Series.armaSerie = new Arma(serie, 120, ffrd, bkwrd);
				
				
				
				List<List<Double>> convergencePerIteration = new LinkedList<List<Double>>();
				List<Double> bestErrorList = new LinkedList<Double>();
				
				for (int k = 0; k < Parameters.SAMPLE_COUNT; k++){
					br.poli.sots.swarmintelligence.abc.Swarm s = new br.poli.sots.swarmintelligence.abc.Swarm(function);
					s.InitializeSwarm();
					s.updatePopulation(true);
					convergencePerIteration.add(s.globalBestLog);
					bestErrorList.add(s.globalBestLog.get(s.globalBestLog.size() - 1));
				}
			    StaticLogger.add(Common.CalculateAverageConvergence(convergencePerIteration), EOptimizer.ABC, "Convergence " + AbstractBeeParticle.functionType + ": {", "}");
			    
			    //Erros totais
			    StaticLogger.add(bestErrorList, EOptimizer.ABC, AbstractBeeParticle.functionType + " per iteration: {", "}");
			    
			    //MSE da best			
			    Series.armaSerie = new Arma(serie, 120, ffrd, bkwrd);
				Series.armaSerie.setParameters(AbstractBeeParticle.positionGBest);
				Series.armaSerie.forecastAll();
				StaticLogger.add(AbstractBeeParticle.positionGBest, EOptimizer.ABC, "Parameters (" + ffrd + "Feedfoward, " + ffrd + " Backward) : {", "}");
				
				//As séries desazonalizada
				StaticLogger.add(Series.armaSerie.serie.comparingSet, EOptimizer.ABC, "Comparing (Deseasonalized) set: {", "}");
				StaticLogger.add(Series.armaSerie.serie.forecastSet, EOptimizer.ABC, "Forecastset (Deseasonalized) set: {", "}");
				
				//Erro quadrático médio (best) desazonalizado
				Double mseError = MeanSquareError.instance.calculateFitness(AbstractBeeParticle.positionGBest);
				StaticLogger.add(mseError, EOptimizer.ABC, "Best Mean (Deseasonalized) mse: ", "");
				
				//Erro absoluto médio (best) desazonalizado
				Double maeError = MeanAbsoluteError.instance.calculateFitness(AbstractBeeParticle.positionGBest);
				StaticLogger.add(maeError, EOptimizer.ABC, "Best Mean (Deseasonalized) mae: ", "");
				
				//Erro raiz quadrático (best) desazonalizado
				Double rmseError = RootMeanSquareError.instance.calculateFitness(AbstractBeeParticle.positionGBest);
				StaticLogger.add(rmseError, EOptimizer.ABC, "Best Mean (Deseasonalized) rmse: ", "");
				
				//As séries desazonalizadas
				Series.armaSerie.seasonalizeSerie();
				StaticLogger.add(Series.armaSerie.serie.comparingSet, EOptimizer.ABC, "Comparing (Seasonalized) set: {", "}");
				StaticLogger.add(Series.armaSerie.serie.forecastSet, EOptimizer.ABC, "Forecastset (Seasonalized) set: {", "}");
				
				//Erro quadrático médio (best) sazonalizado
				mseError = MeanSquareError.instance.calculateFitness(AbstractBeeParticle.positionGBest);
				StaticLogger.add(mseError, EOptimizer.ABC, "Best Mean (Seasonalized) mse: ", "");
				
				//Erro absoluto médio (best) sazonalizado
				maeError = MeanAbsoluteError.instance.calculateFitness(AbstractBeeParticle.positionGBest);
				StaticLogger.add(maeError, EOptimizer.ABC, "Best Mean (Seasonalized) mae: ", "");
				
				//Erro raiz quadrático (best) desazonalizado
				rmseError = RootMeanSquareError.instance.calculateFitness(AbstractBeeParticle.positionGBest);
				StaticLogger.add(rmseError, EOptimizer.ABC, "Best Mean (Seasonalized) rmse: ", "");
				
				StaticLogger.add("\n", EOptimizer.ABC);
			}
		}
		
		StaticLogger.add("\n\n\n", EOptimizer.ABC);
	}
}