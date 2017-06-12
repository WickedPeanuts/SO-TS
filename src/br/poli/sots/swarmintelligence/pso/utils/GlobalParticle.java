package br.poli.sots.swarmintelligence.pso.utils;

import br.poli.sots.Parameters;
import br.poli.sots.swarmintelligence.utils.EFunction;

class GlobalParticle extends AbstractPSOParticle
{
    public GlobalParticle(EFunction functionType, EConstrictionFactor constrictionType)
    {
    	super(functionType, constrictionType);
    }

    @Override
    public void updateSpeed()
    {
        for (int i = 0; i < Parameters.DIMENSION_AMOUNT; i++)
        {
            velocity[i] = constriction.calculateVelocity
                (
                velocity[i], random.nextDouble(), random.nextDouble(),
                position[i], positionGBest[i], positionPBest[i]
                );
        }
    }
}
