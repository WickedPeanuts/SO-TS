package br.poli.sots.swarmintelligence.pso.utils;

public abstract class AbstractConstrictionFactor
{
    protected static final double C1 = 2.05;
    protected static final double C2 = 2.05;

    public abstract double calculateVelocity(double velocity, double random1, double random2, double position, double positionGlobalBest, double positionPersonalBest);

    protected double calculateVelocity(double inertia, double velocity, double random1, double random2, double position, double positionGlobalBest, double positionPersonalBest)
    {
        /*
         * Velocidade [Eixo] = Position [Eixo] * Velocidade Atual [Eixo] +
         *                      Const1 * Random(0,1) * (Posição Atual - Posição G Best) +
         *                      Const2 * Random(0,1) * (Posição Atual - Posição P Best);
         */
        return inertia * velocity +
            C1 * random1 * (positionPersonalBest - position) +
            C2 * random2 * (positionGlobalBest - position);
    }

    public void updateParameter() { }
    
    public static AbstractConstrictionFactor instanceFunction(EConstrictionFactor constrictionFactor)
    {
        if (constrictionFactor == EConstrictionFactor.FixedInertia)
            return new FixedInertia();
        else if (constrictionFactor == EConstrictionFactor.FloatingInertia)
            return new FloatingInertia();
        else
            return new ClercConstriction();
    }
}