package br.poli.sots.swarmintelligence.pso.utils;

public class FixedInertia extends AbstractConstrictionFactor
{
    public static final double INERTIA = 0.8d;

    @Override
    public double calculateVelocity(double velocity, double random1, double random2, double position, double positionGlobalBest, double positionPersonalBest)
    {
        return super.calculateVelocity(INERTIA, velocity, random1, random2, position, positionGlobalBest, positionPersonalBest);
    }
}
