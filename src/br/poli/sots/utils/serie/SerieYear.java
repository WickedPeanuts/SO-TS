package br.poli.sots.utils.serie;

import java.util.LinkedList;
import java.util.List;

public class SerieYear {
	public List<Double> riverFlow;
	
	public int year;
	public int average;
	
	public SerieYear()
	{
		riverFlow = new LinkedList<Double>();
	}
	
	public SerieYear(int year, List<Double> riverFlow){
		this.year = year;
		this.riverFlow = riverFlow;
	}
}
