package br.poli.sots.arma;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import br.poli.sots.Parameters;
import br.poli.sots.utils.serie.Serie;

public class ParametrizedArma {
	public Serie serie;
	double[] theta, phi;
	int training;
	int reintegration;
	
	@SuppressWarnings("unchecked")
	public ParametrizedArma (Serie serie, int training){
		this.serie = serie;
		this.training = training;
		
		initialize(Parameters.MAX_ARMA_FEEDFOWARD, Parameters.MAX_ARMA_BACKWARD);
	}
	
	public ParametrizedArma (Serie serie, int training, int feedfoward, int backward){
		this.serie = serie;
		this.training = training + Math.max(feedfoward, backward);
		
		initialize(feedfoward, backward);
	}
	
	private void initialize(int feedfoward, int backward){
		reintegration = Math.max(feedfoward, backward);
		
		serie.deseasonalize();
		
		theta = new double[feedfoward];
		phi = new double[backward];
		
		serie.trainingSet = new LinkedList<Double>(serie.fullSerie.subList(0, serie.fullSerie.size() - training));
		serie.comparingSet = new LinkedList<Double>(serie.fullSerie.subList(serie.trainingSet.size(), serie.fullSerie.size()));
	}
	
	public void setParameters(double[] param){
		
		for (int i = 0; i < theta.length; i++){
			this.theta[i] = param[i];
		}
		
		for (int i = theta.length; i < theta.length + phi.length; i++){
			this.phi[i - theta.length] = param[i];
		}
		
		serie.forecastSet.clear();
	}
	
	//Prevê o próximo valor do da série, caso a quantidade de valores
	//exceda o número máximo da série, retorna false!
	public boolean forecastNext(){
		if (serie.forecastSet.size() >= serie.comparingSet.size()) return false;
		
		if (serie.forecastSet.size() == 0){
			serie.forecastSet.add(serie.comparingSet.get(0));
			return true;
		}
		/*else if (serie.forecastSet.size() == 1){
			serie.forecastSet.add(theta[0] * serie.comparingSet.get(1));
		}*/
		
		int maxThetaIndex = 0;
		int maxPhiIndex = 0;
		
		if (serie.forecastSet.size() < theta.length){
			maxThetaIndex = serie.forecastSet.size();
		} else {
			maxThetaIndex = theta.length;
		}
		
		if (serie.forecastSet.size() < phi.length){
			maxPhiIndex = serie.forecastSet.size();
		} else {
			maxPhiIndex = phi.length;
		}		
		
		double sum = 0;
		
		for (int j = 0, i = serie.forecastSet.size() - maxThetaIndex; i < serie.forecastSet.size(); i++){
			if (j < maxThetaIndex){
				j++;
			}
			sum += theta[j-1] * serie.comparingSet.get(i);
		}
		
		if (serie.forecastSet.size() > 1){
			for (int j = 0, i = serie.forecastSet.size() - maxPhiIndex; i < serie.forecastSet.size(); i++){
				if (j < maxThetaIndex){
					j++;
				}
				
				double error = serie.forecastSet.get(i) - serie.comparingSet.get(i);
				//System.out.println("j: " + j + ", i:" + i);
				sum -= theta[j-1] * error;
			}
		}
		
		serie.forecastSet.add(sum);
		
		return true;
	}
	
	//forecast the entire serie
	public void forecastAll(){
		while(forecastNext());
		reintegrateSerie();
	}
	
	public void seasonalizeSerie(){
		serie.seasonalize();
		serie.trainingSet = new LinkedList<Double>(serie.fullSerie.subList(0, serie.fullSerie.size() - (training - reintegration)));
		serie.comparingSet = new LinkedList<Double>(serie.fullSerie.subList(serie.trainingSet.size(), serie.fullSerie.size()));
		serie.forecastSet = new LinkedList<Double>(serie.fullSerie.subList(reintegration, training));
	}
	
	//reintegrate
	private void reintegrateSerie(){
		for (int i = 0; i < reintegration; i++){
			serie.forecastSet.set(i, serie.comparingSet.get(i));
		}
	}
}
