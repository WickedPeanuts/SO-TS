package br.poli.sots.swarmintelligence.ffa.utils;

import java.util.ArrayList;
import java.util.List;

import br.poli.sots.Parameters;
import br.poli.sots.swarmintelligence.utils.EFunction;

public class Swarm
{
    public List<FireflyParticle> ParticleList;
    public List<Double> GlobalBestLog;

    public Swarm(EFunction function)
    {
        FireflyParticle.ClearStaticFields();
        ParticleList = FireflyParticle.CreateSwarm(function);
    }

    public void InitializeSwarm()
    {
        for(FireflyParticle particle : ParticleList)
        {
            particle.Initialize();
        }
    }

    public void UpdatePopulation(boolean saveFitnessLog)
    {
        if (saveFitnessLog)
            GlobalBestLog = new ArrayList<Double>();

        for (int i = 0; i< Parameters.ITERATION_AMOUNT; i++)
        {
            for(FireflyParticle pA : ParticleList)
            {
                //Console.WriteLine("PB: {0}", FireflyParticle.GlobalBest);
                //if (pA == ParticleList[1]) Console.WriteLine("X: {0} / Y:{1}", FireflyParticle.PositionGBest[0], FireflyParticle.PositionGBest[1]);
                
                for (FireflyParticle pB : ParticleList)
                {
                    if (pA == pB) continue;

                    pA.UpdateAttractiveness(pB);
                    pA.UpdatePosition(pB);
                    pA.UpdateFitness();
                }

                pA.ForceBoundaries();
            }

            FireflyParticle.UpdateAttractivenessFactor();

            if (saveFitnessLog)
            {
                GlobalBestLog.add(FireflyParticle.GlobalBestFitness);
            }
        }
    }
}
