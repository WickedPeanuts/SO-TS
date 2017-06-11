package br.poli.sots.swarmintelligence.pso.utils;

public class RootMeanSquareError extends AbstractFunction {
	
	public static final AbstractFunction instance = new RootMeanSquareError();

    public float BOUNDARY_MAX =  2;
    public float BOUNDARY_MIN = -2;
    
    public double calculateFitness(double[] position)
    {
    	//RMSE logic here
    	double sum = 0;
    	/*
        for (int x = 0; x < position.length - 1; x++)
        {
            sum += (100 * Math.pow((Math.pow(position[x], 2) - position[x + 1]), 2) + Math.pow((position[x] - 1), 2));
        }*/

        return sum;
    }
}
