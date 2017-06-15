package br.poli.sots.arma;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import br.poli.sots.Parameters;
import br.poli.sots.utils.serie.Serie;

public class CustomRegressionArma {
	public Serie serie;
	double[] theta, phi;
	double training;
	
	@SuppressWarnings("unchecked")
	public CustomRegressionArma (Serie serie, double training){
		this.serie = serie;
		this.training = training;
		
		//serie.deseasonalize();
		
		theta = new double[Parameters.ARMA_FEEDFOWARD];
		phi = new double[Parameters.ARMA_BACKWARD];
		
		serie.trainingSet = new LinkedList<Double>(serie.fullSerie.subList(0, (int)(serie.fullSerie.size()*training/100)));
		serie.comparingSet = new LinkedList<Double>(serie.fullSerie.subList((int)(serie.fullSerie.size()*training/100), serie.fullSerie.size()));
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
		//System.out.println("STOP HAMMER TIME");
		
		/*for (int i = phiIndex; i < Math.min(serie.forecastSet.size(), theta.length) + serie.forecastSet.size(); i++){
			sum -= phiIndex * serie.forecastSet.get(i);
		}*/
		
		/*
		if (serie.forecastSet.size() < theta.length){
			
			double value = theta[serie.forecastSet.size()] * serie.comparingSet.get(serie.forecastSet.size());
			serie.forecastSet.add(value);
			
			return true;
		}
		
		double sum = 0;
		
		int index = theta.length - 1;
		int errorIndex = serie.forecastSet.size() - 1;

		for (int i = 0; i < index; i++){
			sum += theta[index - i] * serie.comparingSet.get(index - i);
			sum -= phi[index - i] * (serie.forecastSet.get(errorIndex - i) - serie.comparingSet.get(errorIndex - i));
		}
		
		serie.forecastSet.add(sum);*/
		
		return true;
	}
	
	//Prevê toda a série
	public void forecastAll(){
		while(forecastNext());
	}
	
	public void seasonalizeSerie(){
		serie.seasonalize();
		serie.trainingSet = new LinkedList<Double>(serie.fullSerie.subList(0, (int)(serie.fullSerie.size()*training/100)));
		serie.comparingSet = new LinkedList<Double>(serie.fullSerie.subList((int)(serie.fullSerie.size()*training/100), serie.fullSerie.size()));
	}
}
