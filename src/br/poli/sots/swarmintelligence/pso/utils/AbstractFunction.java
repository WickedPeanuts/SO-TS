package br.poli.sots.swarmintelligence.pso.utils;

public abstract class AbstractFunction
{
    public float BOUNDARY_MAX;
    public float BOUNDARY_MIN;

    public abstract double calculateFitness(double[] position);

    public static double euclidianDistance(double[] a, double[] b)
    {
        double sum = 0;

        for(int i = 0; i< a.length; i++)
        {
            sum += Math.pow(b[i] - a[i], 2);
        }

        return Math.sqrt(sum);
    }

    public static AbstractFunction instanceFunction(EFunction functionType)
    {
        if (functionType == EFunction.RootMeanSquareError)
            return RootMeanSquareError.instance;
        
        return null;
    }
}