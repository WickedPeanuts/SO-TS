package br.poli.sots.arma;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class Common {

	public static Object DeepCopy(Object original)
	    {
	        ObjectOutputStream oos = null;
	        ObjectInputStream ois = null;

	        Object clone = null;
	        
	        try
	        {
	            // deep copy
	            ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
	            oos = new ObjectOutputStream(bos); 
	            oos.writeObject(original);   
	            oos.flush();               
	            ByteArrayInputStream bin = 
				        new ByteArrayInputStream(bos.toByteArray()); 
	            ois = new ObjectInputStream(bin);                  
	            
	            // retorna novo objeto
	            clone = ois.readObject();
	        }
	        catch(Exception e)
	        {
	            System.out.println("Exception in main = " +  e);
	        }
	        finally
	        {        
	        	try{
	            oos.close();
	            ois.close();
	        	} catch(Exception e) {}
	        }
	        
	        return clone;
	    }
		
		/*
		 * (Dado - media)/(desvio padrao)
		 */
		
		public static void Normalize(List<Double> input){
			Double media = CalculateAverage(input);
			Double desvioPadrao = CalculateStandardDeviation(input);
			input.forEach(x -> x = ((x - media)/desvioPadrao));
		}
						
		public static double CalculateStandardDeviation(List<Double> serieTemporal){
			DescriptiveStatistics estatistica = new DescriptiveStatistics();
		
			for (Double d : serieTemporal){
				estatistica.addValue(d);
			}
			
			return estatistica.getStandardDeviation();
		}
		
		public static double CalculateAverage(List<Double> input){
			DescriptiveStatistics estatistica = new DescriptiveStatistics();
			
			for (Double d : input){
				estatistica.addValue(d);
			}
			
			return estatistica.getMean();
		}
		
		public static List<Double> CalculateAverageConvergence(List<List<Double>> convergenceList){
			List<Double> convergenceAverageList = new LinkedList<Double>();
			List<Double> averagePerIteration = new LinkedList<Double>();
			
			for (int j = 0; j < convergenceList.size(); j++){
				for (int i = 0; i < convergenceList.get(j).size(); i++){
					averagePerIteration.add(convergenceList.get(j).get(i));
				}
				convergenceAverageList.add(CalculateAverage(averagePerIteration));
			}
			
			return convergenceAverageList;
		}
}
