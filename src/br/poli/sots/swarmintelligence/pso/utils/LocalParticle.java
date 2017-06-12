package br.poli.sots.swarmintelligence.pso.utils;

import java.util.ArrayList;
import java.util.List;

import br.poli.sots.Parameters;
import br.poli.sots.swarmintelligence.utils.EFunction;

class LocalParticle extends AbstractPSOParticle
{
    private List<AbstractPSOParticle> neighbors;

    public LocalParticle(EFunction functionType, EConstrictionFactor parameter)
    {
    	super(functionType, parameter);
    }

    public void LinkSwarm(List<AbstractPSOParticle> swarm, int index)
    {
        neighbors = new ArrayList<AbstractPSOParticle>();
        neighbors.add(this);
        neighbors.add(swarm.get((index > 0 ? index - 1 : swarm.size() - 1)));
        neighbors.add(swarm.get((index + 1 < swarm.size() ? index + 1 : 0)));
    }

    @Override
    public void updateSpeed()
    {
        //Selecionar partícula mais "relevante"
        //TODO lambda para ordenar saporra'
    	neighbors.sort((x, y) -> Double.compare(x.personalBest, x.personalBest));
    	
    	AbstractPSOParticle bestParticle = neighbors.get(0);

        //Fazer as matemágicas
        for (int i = 0; i<Parameters.DIMENSION_AMOUNT; i++)
        {
            velocity[i] = constriction.calculateVelocity
                (
                velocity[i], random.nextDouble(), random.nextDouble(), 
                position[i], bestParticle.positionPBest[i], positionPBest[i]
                );
        }
    }
}