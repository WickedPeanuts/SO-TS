package br.poli.sots.swarmintelligence.pso.utils;

public class ClercConstriction extends AbstractConstrictionFactor
{
    /* K = 2/(2 - Phi - Sqrt(Phi^2 - 4Phi))
     */
    private static final double Phi = C1 + C2;
    private static final double Constriction = 2 / Math.abs((2 - Phi - Math.sqrt(Phi * Phi - 4 * Phi)));
    
    @Override
    public double calculateVelocity(double velocity, double random1, double random2, double position, double positionGlobalBest, double positionPersonalBest)
    {
        return (Constriction * super.calculateVelocity(1, velocity, random1, random2, position, positionGlobalBest, positionPersonalBest));
    }
}
