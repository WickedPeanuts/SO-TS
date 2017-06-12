package br.poli.sots.swarmintelligence.ffa.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.poli.sots.Parameters;
import br.poli.sots.swarmintelligence.utils.AbstractFunction;
import br.poli.sots.swarmintelligence.utils.EFunction;

public class FireflyParticle
{
    //Beta - initial glow (0.4 to 0.1) 
    public static double attractivenessFactor = 0.4d;
    public static double attractivenessDecreasingFactor = 0.3d / Parameters.ITERATION_AMOUNT;

    //Gamma - luciferin production / light absorption (0 to 1)
    public static final double luciferinProductionCoefficient = 0.1d;
    
    public double alpha;
    public double attractiveness;

    public double[] position;
    public double[] personalBestPosition;
    public double personalBestFitness;

    public static double globalBestFitness;
    public static double[] globalBestPosition;

    protected static EFunction functionType;
    protected static AbstractFunction function;

    static Random random = new Random();

    public static void clearStaticFields()
    {
        globalBestFitness = 0;
        globalBestPosition = null;
        attractivenessFactor = 0.4d;
    }

    public FireflyParticle(EFunction functionType)
    {
        alpha = 1.0d;

        position = new double[Parameters.DIMENSION_AMOUNT];
        
        if (function == null)
        {
            FireflyParticle.functionType = functionType;
            function = AbstractFunction.instanceFunction(functionType);
        }
    }

    public void initialize()
    {
        double high = function.BOUNDARY_MAX * 0.1;
        double low = function.BOUNDARY_MIN * 0.1;

        //Random distribution around the search space
        for (int i = 0; i < position.length; i++)
        {
            position[i] = (function.BOUNDARY_MAX - function.BOUNDARY_MIN) * random.nextDouble() + function.BOUNDARY_MIN;
        }

        personalBestPosition = (double[])position.clone();

        personalBestFitness = function.calculateFitness(position);

        if (globalBestFitness == 0 && globalBestPosition == null)
        {
            globalBestPosition = (double[])personalBestPosition.clone();
            globalBestFitness = personalBestFitness;
        }

        updateFitness();
    }
    public void updatePosition(FireflyParticle a)
    {
        for (int i = 0; i < Parameters.DIMENSION_AMOUNT; i++)
        {
            //xi,d ← xi,d + β(xj,d − xi,d ) + α(rand() − 0.5)
            if (a.personalBestFitness < this.personalBestFitness)
                this.position[i] = this.personalBestPosition[i]
                    + attractiveness * (a.personalBestPosition[i] - this.position[i])
                    + alpha * (random.nextDouble() - 0.5);
            else
                this.position[i] = this.position[i]
                    + alpha * (random.nextDouble() - 0.5);
        }
    }

    public void updateAttractiveness(FireflyParticle a)
    {
        if (a.personalBestFitness < this.personalBestFitness)
        {
            double distance = AbstractFunction.euclidianDistance(a.personalBestPosition, this.position);
            double exponential = Math.pow(Math.E, -luciferinProductionCoefficient * distance);
            attractiveness = attractivenessFactor * exponential;
        }
    }

    public void updateFitness()
    {
        double newPBest = function.calculateFitness(position);
        
        if (personalBestFitness > newPBest)
        {
            //Update PBest and current Position
            personalBestPosition = (double[])position.clone();
            personalBestFitness = newPBest;

            if (globalBestFitness > newPBest)
            {
                //Console.WriteLine("F: {0} To: {1}", GlobalBest, newPBest);
                globalBestPosition = (double[])position.clone();
                globalBestFitness = newPBest;
            }
        }
    }

    public void forceBoundaries()
    {
        for (int i = 0; i < Parameters.DIMENSION_AMOUNT; i++)
        {
            if (position[i] > function.BOUNDARY_MAX)
                position[i] = function.BOUNDARY_MAX;
            else if (position[i] < function.BOUNDARY_MIN)
                position[i] = function.BOUNDARY_MIN;
        }
    }

    public static List<FireflyParticle> createSwarm(EFunction functionType)
    {
        List<FireflyParticle> swarm = new ArrayList<FireflyParticle>();

        for (int i = 0; i < Parameters.PARTICLE_AMOUNT; i++)
        {
            swarm.add(new FireflyParticle(functionType));
        }

        return swarm;
    }

    public static void updateAttractivenessFactor()
    {
        attractivenessFactor -= attractivenessDecreasingFactor;
    }
}