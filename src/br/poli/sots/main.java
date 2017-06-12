package br.poli.sots;

import br.poli.sots.arma.Arma;
import br.poli.sots.swarmintelligence.fss.FishSchoolSearch;
import br.poli.sots.swarmintelligence.pso.utils.AbstractPSOParticle;
import br.poli.sots.swarmintelligence.pso.utils.EConstrictionFactor;
import br.poli.sots.swarmintelligence.pso.utils.ETopology;
import br.poli.sots.swarmintelligence.pso.utils.Swarm;
import br.poli.sots.swarmintelligence.utils.EFunction;
import br.poli.sots.swarmintelligence.utils.RootMeanSquareError;
import br.poli.sots.utils.serie.Serie;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		Series.armaSerie = new Arma(Series.emborcacao, 98, 1, 1, 1, 1);
		
		//Swarm s = new Swarm(ETopology.Global, EFunction.RootMeanSquareError, EConstrictionFactor.ClercConstriction);
		
		RootMeanSquareError RMSE = (RootMeanSquareError) RootMeanSquareError.instanceFunction(EFunction.RootMeanSquareError);
		FishSchoolSearch fss = new FishSchoolSearch(RMSE);
		
		fss.Busca(10000, 1);
		
		
		
		//s.InitializeSwarm();
		//s.updatePopulation(true);
		//double[] particlePos = s.particleList.get(0).positionPBest;
		double[] particlePos = fss.getBestPosition();
		
		Series.armaSerie.setParameters(particlePos[0], particlePos[1], particlePos[2], particlePos[3]);
		Series.armaSerie.forecastAll();
		
		System.out.println(Series.armaSerie.serie.comparingSet + "\n" + Series.armaSerie.serie.forecastSet);
		System.out.println(Series.armaSerie.serie.comparingSet.size() + "\n" + Series.armaSerie.serie.forecastSet.size());
		System.out.println();
	}

}
