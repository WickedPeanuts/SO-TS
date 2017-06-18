package br.poli.sots.swarmintelligence.abc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import br.poli.sots.Parameters;
import br.poli.sots.swarmintelligence.utils.EFunction;

public class Swarm
{
	public List<AbstractBeeParticle> employedBeesList;
	public List<AbstractBeeParticle> onlookerBeesList;
    public List<Double> globalBestLog;
    public EFunction function;

    public Swarm(EFunction function){
    	this.function = function;
        AbstractBeeParticle.ClearStaticFields();
        employedBeesList = AbstractBeeParticle.createSwarm(function, Parameters.EMPLOYED_BEES);
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
            	employedBeesPhase(particle);
            	onlookerBeesPhase();
            	scoutBeesPhase();
            }

            if (saveFitnessLog){
                globalBestLog.add(AbstractBeeParticle.globalBest);
            }
            
        }
    }
    
    public void employedBeesPhase(AbstractBeeParticle particle){
    	workOn(particle);
    }
    
    public void workOn(AbstractBeeParticle particle){
    	AbstractBeeParticle mutate = particle.mutate(employedBeesList, employedBeesList.indexOf(particle));
    	if (mutate.fitness < particle.fitness){
    		particle = mutate;
    		globalBestLog.add(mutate.fitness);
    		//UPDATES GBEST
    	}else{
    		particle.setAtempts(particle.getAtempts()-1);
    	}
    }
    
    public void onlookerBeesPhase(){
    	//Work on random solutions, weighted to favor the promising ones.
    	double sumfitness = 0;
    	Random r = new Random();
    	for (AbstractBeeParticle particle: employedBeesList){
    		sumfitness += particle.fitness;	
    	}
    	
    	for (int onlooker = 0; onlooker<Parameters.ONLOOKER_BEES; onlooker++){
    		int i = 0;
    		double random = ThreadLocalRandom.current().nextDouble(0, sumfitness);
    		
    		while(employedBeesList.get(i).fitness < random){
    			random -= employedBeesList.get(i).fitness;
    			i++;
    		}
    		workOn(employedBeesList.get(i));
    	}
    	
    }
    
    public void scoutBeesPhase(){
    	//Reinitialize any solution that hasn't been fruitful recently.
    	for (AbstractBeeParticle particle: employedBeesList){
    		if (particle.getAtempts() <= 0){
    			particle = new Bee(function);
    		}
    	}
    }
}