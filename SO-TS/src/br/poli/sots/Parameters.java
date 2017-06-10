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
}

