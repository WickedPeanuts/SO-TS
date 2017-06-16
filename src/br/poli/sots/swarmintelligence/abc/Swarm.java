package br.poli.sots.swarmintelligence.abc;

import java.util.ArrayList;
import java.util.List;

import br.poli.sots.Parameters;
import br.poli.sots.swarmintelligence.utils.EFunction;

public class Swarm
{
	public List<AbstractBeeParticle> employedBeesList;
    public List<Double> globalBestLog;

    public Swarm(EFunction function){
        AbstractBeeParticle.ClearStaticFields();
        employedBeesList = AbstractBeeParticle.createSwarm(function, Parameters.PARTICLE_AMOUNT);
    }

    public void InitializeSwarm(){
        for (AbstractBeeParticle particle : employedBeesList){
            particle.initialize();
        }
    }

    public void updatePopulation(boolean saveFitnessLog){
        if (saveFitnessLog)
            globalBestLog = new ArrayList<Double>();

        for (int i = 0; i < Parameters.ITERATION_AMOUNT; i++){
            for(AbstractBeeParticle particle : employedBeesList){
            	employedBeesPhase();
            	onlookerBeesPhase();
            	scoutBeesPhase();
            }

            if (saveFitnessLog){
                globalBestLog.add(AbstractBeeParticle.globalBest);
            }
            
        }
    }
    
    public void employedBeesPhase(){
    	
    }
    
    public void onlookerBeesPhase(){
    	
    }
    
    public void scoutBeesPhase(){
    	
    }
}