package br.poli.sots.swarmintelligence.ffa.utils;

import java.util.ArrayList;
import java.util.List;

import br.poli.sots.Parameters;
import br.poli.sots.swarmintelligence.utils.EFunction;

public class Swarm
{
    public List<FireflyParticle> particleList;
    public List<Double> globalBestLog;

    public Swarm(EFunction function)
    {
        FireflyParticle.clearStaticFields();
        particleList = FireflyParticle.createSwarm(function);
    }

    public void initializeSwarm()
    {
        for(FireflyParticle particle : particleList)
        {
            particle.initialize();
        }
    }

    public void updatePopulation(boolean saveFitnessLog)
    {
        if (saveFitnessLog)
            globalBestLog = new ArrayList<Double>();

        for (int i = 0; i< Parameters.ITERATION_AMOUNT; i++)
        {
            for(FireflyParticle pA : particleList)
            {
                //Console.WriteLine("PB: {0}", FireflyParticle.GlobalBest);
                //if (pA == ParticleList[1]) Console.WriteLine("X: {0} / Y:{1}", FireflyParticle.PositionGBest[0], FireflyParticle.PositionGBest[1]);
                
                for (FireflyParticle pB : particleList)
                {
                    if (pA == pB) continue;

                    pA.updateAttractiveness(pB);
                    pA.updatePosition(pB);
                    pA.updateFitness();
                }

                pA.forceBoundaries();
            }

            FireflyParticle.updateAttractivenessFactor();

            if (saveFitnessLog)
            {
                globalBestLog.add(FireflyParticle.globalBestFitness);
            }
        }
    }
}
