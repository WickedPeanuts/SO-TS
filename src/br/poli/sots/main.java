package br.poli.sots;

import br.poli.sots.arma.Arma;
import br.poli.sots.utils.serie.Serie;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Serie s = new Serie("Passo Real");
		Serie n = new Serie("Furnas");
		Serie m = new Serie("Emborcacao");
		Serie o = new Serie("Sobradinho");
		
		Arma arma = new Arma(s, 99, 0.9, 1.1, 0.5, 0.34);
		arma.forecastAll();
		
		System.out.println(arma.serie.comparingSet + "\n" + arma.serie.forecastSet);
		System.out.println(arma.serie.comparingSet.size() + "\n" + arma.serie.forecastSet.size());
		System.out.println();
	}

}
