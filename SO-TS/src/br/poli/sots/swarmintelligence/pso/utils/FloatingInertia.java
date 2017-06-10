package br.poli.sots.swarmintelligence.pso.utils;

import br.poli.sots.Parameters;

public class FloatingInertia extends AbstractConstrictionFactor
{
    private static final double INERTIA_FACTOR = 0.5 / Parameters.ITERATION_AMOUNT;
    private double INERTIA = 0.9d;

    public double calculateVelocity(double velocity, double random1, double random2, double position, double positionGlobalBest, double positionPersonalBest)
    {
        return super.calculateVelocity(INERTIA, velocity, random1, random2, position, positionGlobalBest, positionPersonalBest);
    }

    @Override
    public void updateParameter()
    {
        INERTIA -= INERTIA_FACTOR;
    }
}
