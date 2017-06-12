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
    public static double AttractivenessFactor = 0.4d;
    public static double AttractivenessDecreasingFactor = 0.3d / Parameters.ITERATION_AMOUNT;

    //Gamma - luciferin production / light absorption (0 to 1)
    public static final double LuciferinProductionCoefficient = 0.1d;
    
    public double Alpha;
    public double Attractiveness;

    public double[] Position;
    public double[] PersonalBestPosition;
    public double PersonalBestFitness;

    public static double GlobalBestFitness;
    public static double[] GlobalBestPosition;

    protected static EFunction functionType;
    protected static AbstractFunction function;

    static Random random = new Random();

    public static void ClearStaticFields()
    {
        GlobalBestFitness = 0;
        GlobalBestPosition = null;
        AttractivenessFactor = 0.4d;
    }

    public FireflyParticle(EFunction functionType)
    {
        Alpha = 1.0d;

        Position = new double[Parameters.DIMENSION_AMOUNT];
        
        if (function == null)
        {
            FireflyParticle.functionType = functionType;
            function = AbstractFunction.instanceFunction(functionType);
        }
    }

    public void Initialize()
    {
        double high = function.BOUNDARY_MAX * 0.1;
        double low = function.BOUNDARY_MIN * 0.1;

        //Random distribution around the search space
        for (int i = 0; i < Position.length; i++)
        {
            Position[i] = (function.BOUNDARY_MAX - function.BOUNDARY_MIN) * random.nextDouble() + function.BOUNDARY_MIN;
        }

        PersonalBestPosition = (double[])Position.clone();

        PersonalBestFitness = function.calculateFitness(Position);

        if (GlobalBestFitness == 0 && GlobalBestPosition == null)
        {
            GlobalBestPosition = (double[])PersonalBestPosition.clone();
            GlobalBestFitness = PersonalBestFitness;
        }

        UpdateFitness();
    }
    public void UpdatePosition(FireflyParticle a)
    {
        for (int i = 0; i < Parameters.DIMENSION_AMOUNT; i++)
        {
            //xi,d ← xi,d + β(xj,d − xi,d ) + α(rand() − 0.5)
            if (a.PersonalBestFitness < this.PersonalBestFitness)
                this.Position[i] = this.PersonalBestPosition[i]
                    + Attractiveness * (a.PersonalBestPosition[i] - this.Position[i])
                    + Alpha * (random.nextDouble() - 0.5);
            else
                this.Position[i] = this.Position[i]
                    + Alpha * (random.nextDouble() - 0.5);
        }
    }

    public void UpdateAttractiveness(FireflyParticle a)
    {
        if (a.PersonalBestFitness < this.PersonalBestFitness)
        {
            double distance = AbstractFunction.euclidianDistance(a.PersonalBestPosition, this.Position);
            double exponential = Math.pow(Math.E, -LuciferinProductionCoefficient * distance);
            Attractiveness = AttractivenessFactor * exponential;
        }
    }

    public void UpdateFitness()
    {
        double newPBest = function.calculateFitness(Position);
        if (PersonalBestFitness > newPBest)
        {
            //Update PBest and current Position
            PersonalBestPosition = (double[])Position.clone();
            PersonalBestFitness = newPBest;

            if (GlobalBestFitness > newPBest)
            {
                //Console.WriteLine("F: {0} To: {1}", GlobalBest, newPBest);
                GlobalBestPosition = (double[])Position.clone();
                GlobalBestFitness = newPBest;
            }
        }
    }

    public void ForceBoundaries()
    {
        for (int i = 0; i < Parameters.DIMENSION_AMOUNT; i++)
        {
            if (Position[i] > function.BOUNDARY_MAX)
                Position[i] = function.BOUNDARY_MAX;
            else if (Position[i] < function.BOUNDARY_MIN)
                Position[i] = function.BOUNDARY_MIN;
        }
    }

    public static List<FireflyParticle> CreateSwarm(EFunction functionType)
    {
        List<FireflyParticle> swarm = new ArrayList<FireflyParticle>();

        for (int i = 0; i < Parameters.PARTICLE_AMOUNT; i++)
        {
            swarm.add(new FireflyParticle(functionType));
        }

        return swarm;
    }

    public static void UpdateAttractivenessFactor()
    {
        AttractivenessFactor -= AttractivenessDecreasingFactor;
    }
}