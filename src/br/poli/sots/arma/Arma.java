package br.poli.sots.arma;

import java.util.LinkedList;
import java.util.List;

import br.poli.sots.utils.serie.Serie;

public class Arma {
	public Serie serie;
	double theta1, theta2, phi1, phi2;
	double training;
	
	@SuppressWarnings("unchecked")
	public Arma (Serie serie, double training, double theta1, double theta2, double phi1, double phi2){
		this.serie = serie;
		this.training = training;
		
		//serie.deseasonalize();
		
		serie.trainingSet = new LinkedList<Double>(serie.fullSerie.subList(0, (int)(serie.fullSerie.size()*training/100)));
		serie.comparingSet = new LinkedList<Double>(serie.fullSerie.subList((int)(serie.fullSerie.size()*training/100), serie.fullSerie.size()));
		
		setParameters(theta1, theta2, phi1, phi2);
	}
	
	public void setParameters(double theta1, double theta2, double phi1, double phi2){
		this.theta1 = theta1;
		this.theta2 = theta2;
		this.phi1 = phi1;
		this.phi2 = phi2;
		serie.forecastSet.clear();
		serie.forecastSet.add(serie.comparingSet.get(0));
	}
	
	//Prevê o próximo valor do da série, caso a quantidade de valores
	//exceda o número máximo da série, retorna false!
	public boolean forecastNext(){
		if (serie.forecastSet.size() >= serie.comparingSet.size()) return false;
		
		double x_1 = serie.comparingSet.get(serie.forecastSet.size() - 1);
		
		if (serie.forecastSet.size() == 1){
			x_1 *= theta1;
			serie.forecastSet.add(x_1);
			return true;
		}
		
		double x_2 = serie.comparingSet.get(serie.forecastSet.size() - 2);
		double e_1 = x_1 - serie.forecastSet.get(serie.forecastSet.size() - 1);
		double e_2 = x_2 - serie.forecastSet.get(serie.forecastSet.size() - 2);
		
		double newValue = ((theta1 * x_1 + theta2 * x_2) - (phi1 * e_1 + phi2 * e_2));
		serie.forecastSet.add(newValue);
		
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
