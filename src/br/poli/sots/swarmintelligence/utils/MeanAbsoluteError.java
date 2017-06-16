package br.poli.sots.swarmintelligence.utils;

import java.util.List;

import br.poli.sots.arma.Arma;
import br.poli.sots.arma.Arma;
import br.poli.sots.utils.serie.Series;

public class MeanAbsoluteError extends AbstractFunction {
	
	public static final AbstractFunction instance = new MeanAbsoluteError();
	
	private MeanAbsoluteError(){
	    BOUNDARY_MAX =  1;
	    BOUNDARY_MIN = -1;
	}
    
	//TODO refatorar pra calcular o mae certo
    public double calculateFitness(double[] position)
    {
    	//RMSE logic here
    	double sum = 0;
    	
    	Arma a = Series.armaSerie;
    	
    	a.setParameters(position);
    	a.forecastAll();
    	
    	List<Double> forecastSet, comparingSet;
    	forecastSet = a.serie.forecastSet;
    	comparingSet = a.serie.comparingSet;
    	
        for (int i = 0; i < forecastSet.size(); i++)
        {
        	sum += (forecastSet.get(i) - comparingSet.get(i));
        }
        
        sum = sum/forecastSet.size();

        return sum;
    }
}
