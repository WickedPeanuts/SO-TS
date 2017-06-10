package br.poli.sots.utils.serie;

import java.util.LinkedList;
import java.util.List;

public class SerieYear {
	public List<Integer> riverFlow;
	
	public int year;
	public int average;
	
	public SerieYear()
	{
		riverFlow = new LinkedList<Integer>();
	}
	
	public SerieYear(int year, List<Integer> riverFlow){
		this.year = year;
		this.riverFlow = riverFlow;
	}
}
