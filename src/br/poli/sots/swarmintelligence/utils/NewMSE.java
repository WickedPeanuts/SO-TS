package br.poli.sots.swarmintelligence.utils;

import java.util.List;

import br.poli.sots.Series;
import br.poli.sots.arma.Arma;
import br.poli.sots.arma.CustomRegressionArma;

public class NewMSE extends AbstractFunction {
	
	public static final AbstractFunction instance = new NewMSE();
	
	private NewMSE(){
	    BOUNDARY_MAX =  100;
	    BOUNDARY_MIN = -100;
	}
    
    public double calculateFitness(double[] position)
    {
    	//RMSE logic here
    	double sum = 0;
    	
    	CustomRegressionArma a = Series.crarmaSerie;
    	
    	a.setParameters(position);
    	a.forecastAll();
    	
    	List<Double> forecastSet, comparingSet;
    	forecastSet = a.serie.forecastSet;
    	comparingSet = a.serie.comparingSet;
    	
        for (int i = 0; i < forecastSet.size(); i++)
        {
        	sum += Math.pow((forecastSet.get(i) - comparingSet.get(i)), 2);
        }
        
        sum = sum/forecastSet.size();

        return sum;
    }
}
