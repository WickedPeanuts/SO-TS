package br.poli.sots.swarmintelligence.utils;

import java.util.List;

import br.poli.sots.arma.Arma;
import br.poli.sots.arma.ParametrizedArma;
import br.poli.sots.utils.serie.Series;

public class NewMSE extends AbstractFunction {
	
	public static final AbstractFunction instance = new NewMSE();
	
	private NewMSE(){
	    BOUNDARY_MAX =  1;
	    BOUNDARY_MIN = -1;
	}
    
    public double calculateFitness(double[] position)
    {
    	//RMSE logic here
    	double sum = 0;
    	
    	ParametrizedArma a = Series.crarmaSerie;
    	
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
