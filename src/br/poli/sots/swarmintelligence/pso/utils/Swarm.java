package br.poli.sots.swarmintelligence.pso.utils;

import java.util.ArrayList;
import java.util.List;

import br.poli.sots.Parameters;

public class Swarm
{
    public List<AbstractPSOParticle> garticleList;
    public List<Double> globalBestLog;

    public Swarm(ETopology topology, EFunction function, EConstrictionFactor constrictionFactor)
    {
        AbstractPSOParticle.ClearStaticFields();
        garticleList = AbstractPSOParticle.createSwarm(topology, function, constrictionFactor, Parameters.PARTICLE_AMOUNT);
    }

    public void InitializeSwarm()
    {
        for (AbstractPSOParticle particle : garticleList)
        {
            particle.initialize();
        }
    }

    public void updatePopulation(boolean saveFitnessLog)
    {
        if (saveFitnessLog)
            globalBestLog = new ArrayList<Double>();

        for (int i = 0; i < Parameters.ITERATION_AMOUNT; i++)
        {
            for(AbstractPSOParticle particle : garticleList)
            {
                particle.updateSpeed();
                particle.updatePosition();
                particle.forceBoundaries();
                //if (particle == ParticleList[0])
                //{
                //    Console.WriteLine("PX: {0}/PY: {1}/ PZ: {2}/ FIT: {3} / FBest: {4}", particle.Position[0], particle.Position[1], particle.Position[2], particle.PersonalBest, AbstractParticle.GlobalBest);
                //    Console.WriteLine("SX: {0}/SY: {1}/ SZ: {2}", particle.Velocity[0], particle.Velocity[1], particle.Velocity[2]);
                //    Console.WriteLine("{0}", particle.PersonalBest);
                //}
                particle.updateFitness();
            }

            if (saveFitnessLog)
            {
                globalBestLog.add(AbstractPSOParticle.globalBest);
            }
            
        }
    }
}