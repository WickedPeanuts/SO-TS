package br.poli.sots.swarmintelligence.utils;

import java.util.List;

import br.poli.sots.Series;
import br.poli.sots.arma.Arma;

public class RootMeanSquareError extends AbstractFunction {
	
	public static final AbstractFunction instance = new RootMeanSquareError();

    public float BOUNDARY_MAX =  100;
    public float BOUNDARY_MIN = -100;
    
    public double calculateFitness(double[] position)
    {
    	//RMSE logic here
    	double sum = 0;
    	
    	Arma a = Series.armaSerie;
    	
    	a.setParameters(position[0], position[1], position[2], position[3]);
    	a.forecastAll();
    	
    	List<Double> forecastSet, comparingSet;
    	forecastSet = a.serie.forecastSet;
    	comparingSet = a.serie.comparingSet;
    	
        for (int i = 0; i < forecastSet.size(); i++)
        {
        	sum += Math.pow((forecastSet.get(i) - comparingSet.get(i)), 2);
        }
        
        sum = Math.sqrt(sum/forecastSet.size());

        return sum;
    }
}
