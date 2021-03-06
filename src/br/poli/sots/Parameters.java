package br.poli.sots;

public class Parameters {
	
	//ARMA
	public static int MAX_ARMA_FEEDFOWARD = 3;
	public static int MAX_ARMA_BACKWARD = 3;
	
	//PSO & FFA
	public static int DIMENSION_AMOUNT = MAX_ARMA_FEEDFOWARD + MAX_ARMA_BACKWARD;
	public static final int PARTICLE_AMOUNT = 5;
	public static final int ITERATION_AMOUNT = 10;
	public static final int SAMPLE_COUNT = 5;
	
	//FSS
	public static final int SCHOOL_SIZE = 5;
	public static final int MIN_WEIGHT = 1;
	public static final int MAX_WEIGHT = 5000;
	public static final double STEP_IND_INIT = 0.1;
	public static final double STEP_IND_FINAL = 0.00001;
	public static final double STEP_VOL_INIT = 0.1;
	public static final double STEP_VOL_FINAL = 0.001;
	
	//ABC
	public static final int EMPLOYED_BEES = 30;
	public static final int ONLOOKER_BEES = 30;
	public static final int ALPHA = 1;
	public static final int ATTEMPTS = 10;
	
	public static boolean DEBUG = false;
	
	public static void UpdateParameters(int feed, int back){
		DIMENSION_AMOUNT = feed + back;
		//MAX_ARMA_FEEDFOWARD = feed;
		//MAX_ARMA_BACKWARD = back;
	}
}

