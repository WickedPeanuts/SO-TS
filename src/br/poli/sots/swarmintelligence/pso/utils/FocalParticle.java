package br.poli.sots.swarmintelligence.pso.utils;

import java.util.List;

import br.poli.sots.Parameters;
import br.poli.sots.swarmintelligence.utils.EFunction;

public class FocalParticle extends AbstractPSOParticle
{
    public boolean isFocalParticle;

    private List<AbstractPSOParticle> swarm;
    private AbstractPSOParticle focalParticle;
    
    public FocalParticle(EFunction functionType, EConstrictionFactor constrictionType, List<AbstractPSOParticle> swarm,  boolean isFocalParticle)
    {
    	super(functionType, constrictionType);
    	
        this.swarm = swarm;
        this.isFocalParticle = isFocalParticle;
        
        for(AbstractPSOParticle abs : swarm)
        {
            if (((FocalParticle)abs).isFocalParticle)
            {
                focalParticle = abs;
                break;
            }
        }
    }

    @Override
    public void updateSpeed()
    {
        //FocalParticle seleciona a particula mais "influente"
        AbstractPSOParticle bestParticle = null;

        if (isFocalParticle)
        {
            bestParticle = swarm.get(0);

            for (int j = 0; j < Parameters.PARTICLE_AMOUNT; j++)
            {
                if (swarm.get(j).personalBest < bestParticle.personalBest)
                {
                    bestParticle = swarm.get(j);
                }
            }
        }

        for (int i = 0; i<Parameters.DIMENSION_AMOUNT; i++)
        {
            if (isFocalParticle)
            {
                velocity[i] = constriction.calculateVelocity
                    (
                    velocity[i], random.nextDouble(), random.nextDouble(),
                    position[i], positionPBest[i], bestParticle.positionPBest[i]
                    );
            }
            else
            {
                velocity[i] = constriction.calculateVelocity
                    (
                    velocity[i], random.nextDouble(), random.nextDouble(),
                    position[i], focalParticle.position[i], positionPBest[i]
                    );

            }
        }
    }
}
