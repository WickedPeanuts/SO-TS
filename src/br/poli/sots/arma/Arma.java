package br.poli.sots.arma;

import java.util.LinkedList;
import java.util.List;

import br.poli.sots.utils.serie.Serie;

public class Arma {
	public Serie serie;
	double theta1, theta2, phi1, phi2;
	
	@SuppressWarnings("unchecked")
	public Arma (Serie serie, double training, double theta1, double theta2, double phi1, double phi2){
		this.serie = serie;
		setParameters(theta1, theta2, phi1, phi2);
		
		serie.padronize();
		
		
		serie.trainingSet = new LinkedList<Double>(serie.fullSerie.subList(0, (int)(serie.fullSerie.size()*training/100)));
		serie.comparingSet = new LinkedList<Double>(serie.fullSerie.subList((int)(serie.fullSerie.size()*training/100), serie.fullSerie.size()));
		serie.forecastSet.add(serie.comparingSet.get(0));
		serie.forecastSet.add(serie.comparingSet.get(1));
	}
	
	public void setParameters(double theta1, double theta2, double phi1, double phi2){
		this.theta1 = theta1;
		this.theta2 = theta2;
		this.phi1 = phi1;
		this.phi2 = phi2;
		serie.forecastSet.clear();
	}
	
	//Prevê o próximo valor do da série, caso a quantidade de valores
	//exceda o número máximo da série, retorna false!
	public boolean forecastNext(){
		if (serie.forecastSet.size() >= serie.comparingSet.size()) return false;
		
		double x_1 = serie.forecastSet.get(serie.forecastSet.size() - 2);
		double x_2 = serie.forecastSet.get(serie.forecastSet.size() - 1);
		
		double newValue = ((theta1 * x_1 + theta2 * x_2) - (phi1 * x_1 + phi2 * x_2));
		serie.forecastSet.add(newValue);
		
		return true;
	}
	
	//Prevê toda a série
	public void forecastAll(){
		while(forecastNext());
	}
}
