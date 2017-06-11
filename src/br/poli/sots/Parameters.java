package br.poli.sots;

public class Parameters {
	//PSO
	public static final int DIMENSION_AMOUNT = 30;
	public static final int PARTICLE_AMOUNT = 30;
	public static final int ITERATION_AMOUNT = 10000;
	public static final int SAMPLE_COUNT = 30;

	//ABC
	public static final int EMPLOYED_BEE_AMOUNT = 40;
	public static final int SCOUT_BEE_AMOUNT = 20;
	public static final int ONLOOKER_BEE_AMOUNT = 100 - (EMPLOYED_BEE_AMOUNT + SCOUT_BEE_AMOUNT);
	public static final int INITIAL_FOOD_SOURCES = 3;
	public static final int ATTEMPT_TO_CHANGE_SOURCE = 3;
	
	//FSS
	public static final int SCHOOL_SIZE = 30;
	public static final int DIMENSIONS = 30;
	public static final int MIN_WEIGHT = 1;
	public static final int MAX_WEIGHT = 5000;
	public static final double STEP_IND_INIT = 0.1;
	public static final double STEP_IND_FINAL = 0.00001;
	public static final double STEP_VOL_INIT = 0.1;
	public static final double STEP_VOL_FINAL = 0.001;
	public static boolean DEBUG = false;
}

