package br.poli.sots.utils.logging;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import br.poli.sots.utils.serie.EOptimizer;

public class StaticLogger {
	static List<String> psoLog = new LinkedList<String>();
	static List<String> fssLog = new LinkedList<String>();
	static List<String> abcLog = new LinkedList<String>();
	static List<String> ffaLog = new LinkedList<String>();

	public static void add(List<Double> doubleList, EOptimizer optimizer, String prepend, String append){
		Double[] newInst = new Double[doubleList.size()];
		for (int i = 0 ; i < doubleList.size(); i++){
			newInst[i] = doubleList.get(i);
		}
		StaticLogger.add(newInst, optimizer, prepend, append);
	}
	
	public static void add(Double[] doubleArray, EOptimizer optimizer, String prepend, String append) {
		StaticLogger.add(Stream.of(doubleArray).mapToDouble(Double::doubleValue).toArray(), optimizer, prepend, append);
	}
			
	public static void add(double[] doubleArray, EOptimizer optimizer, String prepend, String append){
		List<String> log = selectLog(optimizer);
		
		StringBuilder text = new StringBuilder(prepend);
		
		for(Double d : doubleArray){
			text.append(d).append(",");
		}
		
		//Dexando bonitinho (Text = Convergence: {1,2,3,4,5})
		log.add(text.append(".").toString().replace(",.", append));
	}
	
	public static void add(Double number, EOptimizer optimizer, String prepend, String append){
		List<String> log = selectLog(optimizer);
		
		StringBuilder text = new StringBuilder(prepend);
		text.append(number).append(append);
		
		log.add(text.toString());
	}
	
	public static void add(String text, EOptimizer optimizer){
		List<String> log = selectLog(optimizer);
		log.add(text);
	}
	
	private static List<String> selectLog(EOptimizer optimizer){
		switch (optimizer){
			case PSO:
				return psoLog;
			case ABC:
				return abcLog;
			case FFA:
				return ffaLog;
			default:
				return fssLog;
		}
	}
	
	public static void saveToFile(){
		try{
			PrintWriter out = new PrintWriter("results.txt");
			out.print("PSO\n\n");
			psoLog.forEach(x -> out.print(x + "\n"));
			out.print("\n\nABC\n\n");
			abcLog.forEach(x -> out.print(x + "\n"));
			out.print("\n\nFFA\n\n");
			ffaLog.forEach(x -> out.print(x + "\n"));
			out.print("\n\nFSS\n\n");
			fssLog.forEach(x -> out.print(x + "\n"));
			System.out.println("File saved sucessfully!");
			out.close();
		} catch (Exception e){
			System.out.println("Could not save on file, error: " + e.getMessage());
		}
	}
}
