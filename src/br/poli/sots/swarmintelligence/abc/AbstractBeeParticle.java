package br.poli.sots.swarmintelligence.abc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import br.poli.sots.Parameters;
import br.poli.sots.swarmintelligence.utils.AbstractFunction;
import br.poli.sots.swarmintelligence.utils.EFunction;

public abstract class AbstractBeeParticle
{
    public double[] position;
    public double[] positionPBest;
    public double personalBest;
    public double fitness;
    private int atempts;

    public static double globalBest;
    public static double[] positionGBest;
    
    public static EFunction functionType;

    protected static AbstractFunction function;

    protected static Random random = new Random();

    public static void ClearStaticFields()
    {
        globalBest = 0;
        positionGBest = null;

        function = null;
    }
    
    public AbstractBeeParticle(EFunction functionType)
    {
        position = new double[Parameters.DIMENSION_AMOUNT];

        {
            AbstractBeeParticle.functionType = functionType;
            function = AbstractFunction.instanceFunction(functionType);
        }
        this.setAtempts(Parameters.ATTEMPTS);
    }

    public void initialize()
    {
        double high = function.BOUNDARY_MAX * 0.1;
        double low = function.BOUNDARY_MIN * 0.1;

        //Random distribution around the search space
        for (int i = 0; i < position.length; i++)
        {
            position[i] = (high - low) * random.nextDouble() + low;
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
            //position[i] += velocity[i];
        }

    }

    public void updateFitness()
    {
        double newPBest = function.calculateFitness(position);
        this.fitness = newPBest;
        if (personalBest >= newPBest)
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

    public static List<AbstractBeeParticle> createSwarm(EFunction function, int particleAmmount)
    {
        List<AbstractBeeParticle> swarm = new ArrayList<AbstractBeeParticle>();

        for (int i = 0; i < particleAmmount; i++){
        	swarm.add(new Bee(function));
        }

        return swarm;
    }
    
    public AbstractBeeParticle mutate(List<AbstractBeeParticle> employedBeesList, int index){
		AbstractBeeParticle bee = new Bee(functionType);
		for (int i = 0; i < this.position.length; i ++){
			int rand = ThreadLocalRandom.current().nextInt(employedBeesList.size()-1);
			if (rand >= index){
				rand++;
			}
			double phi = ThreadLocalRandom.current().nextDouble(-Parameters.ALPHA, Parameters.ALPHA);
			double diff = position[i] - employedBeesList.get(rand).position[i];
			bee.position[i] = position[i] + phi * diff;
		}
		bee.updateFitness();
		return bee;
	}

	public int getAtempts() {
		return atempts;
	}

	public void setAtempts(int atempts) {
		this.atempts = atempts;
	}
}