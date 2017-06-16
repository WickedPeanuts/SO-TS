package br.poli.sots.swarmintelligence.pso.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.poli.sots.Parameters;
import br.poli.sots.swarmintelligence.utils.AbstractFunction;
import br.poli.sots.swarmintelligence.utils.EFunction;

public abstract class AbstractPSOParticle
{
    public double[] position;
    public double[] positionPBest;
    public double[] velocity;
    public double personalBest;

    public static double globalBest;
    public static double[] positionGBest;
    
    public static EFunction functionType;
    protected static EConstrictionFactor constrictionType;

    protected static AbstractFunction function;
    protected static AbstractConstrictionFactor constriction;

    protected static Random random = new Random();

    public static void ClearStaticFields()
    {
        globalBest = 0;
        positionGBest = null;

        function = null;
        constriction = null;
    }
    
    public AbstractPSOParticle(EFunction functionType, EConstrictionFactor constrictionType)
    {
        position = new double[Parameters.DIMENSION_AMOUNT];
        velocity = new double[Parameters.DIMENSION_AMOUNT];

        if (function == null || constriction == null)
        {
            AbstractPSOParticle.functionType = functionType;
            AbstractPSOParticle.constrictionType = constrictionType;
            function = AbstractFunction.instanceFunction(functionType);
            constriction = AbstractConstrictionFactor.instanceFunction(constrictionType);
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
            velocity[i] = (high - low) * random.nextDouble() + low;
        }

        positionPBest = position.clone();

        personalBest = function.calculateFitness(position);

        if (globalBest == 0 && positionGBest == null)
        {
            positionGBest = positionPBest.clone();
            globalBest = personalBest;
        }

        updateFitness();
    }

    public void updatePosition()
    {
        for (int i = 0; i < Parameters.DIMENSION_AMOUNT; i++)
        {
            position[i] += velocity[i];
        }

        constriction.updateParameter();
    }

    public abstract void updateSpeed();

    public void updateFitness()
    {
        double newPBest = function.calculateFitness(position);
        if (personalBest > newPBest)
        {
            //Update PBest and current Position
            positionPBest = position.clone();
            personalBest = newPBest;

            if (globalBest > newPBest)
            {
                //Console.WriteLine("F: {0} To: {1}", GlobalBest, newPBest);
                positionGBest = position.clone();
                globalBest = newPBest;
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

    public static List<AbstractPSOParticle> createSwarm(ETopology topology, EFunction function, EConstrictionFactor constrictionFactor, int particleAmmount)
    {
        List<AbstractPSOParticle> swarm = new ArrayList<AbstractPSOParticle>();

        for (int i = 0; i < particleAmmount; i++)
        {
            if (topology == ETopology.Ring)
            {
                swarm.add(new LocalParticle(function, constrictionFactor));
            }
            else if (topology == ETopology.Global)
            {
                swarm.add(new GlobalParticle(function, constrictionFactor));
            }
            else if (topology == ETopology.Focal)
            {
                swarm.add(new FocalParticle(function, constrictionFactor, swarm, i == 0));
            }
        }

        if (topology == ETopology.Ring)
        {
            for (int i = 0; i < swarm.size(); i++)
            {
                ((LocalParticle)swarm.get(i)).LinkSwarm(swarm, i);
            }
        }

        return swarm;
    }
}