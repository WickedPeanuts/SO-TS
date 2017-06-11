package br.poli.sots.swarmintelligence.fss;

import java.util.Random;

import br.poli.sots.*;
import br.poli.sots.swarmintelligence.pso.utils.*;

public class FishSchoolSearch {
	private Fish[] school;
	private Random rand;
	private RootMeanSquareError problema;
	private double[] bestPosition;
	
	private double stepIndPercentage;
	private double stepVolPercentage;
	
	private int iterationsNumber;
	private boolean isMinimumOptimization;
	
	public FishSchoolSearch(RootMeanSquareError problem){
		rand = new Random();
		this.problema = problem;
	}
	
	public void initializeSwarm(){
		this.school = new Fish[Parameters.SCHOOL_SIZE];
		double position[];
		for(int i = 0; i < school.length; i++){
			position = createRandomPosition(Parameters.DIMENSIONS);
			this.school[i] = new Fish((double)(Parameters.MIN_WEIGHT + Parameters.MAX_WEIGHT)/2.0, position);
		}
	}
	
	public void initializeBestPosition(){
		this.bestPosition = (double[]) school[0].getCurrentPosition().clone();
	}
	
	public double[] createRandomPosition(int dimensions){

		double position[] = new double[dimensions];
		for (int i = 0; i < position.length; i++) {
			position[i] = problema.BOUNDARY_MAX/2 + (problema.BOUNDARY_MAX - 
					problema.BOUNDARY_MAX/2)*rand.nextDouble();
		}
		return position;
	}
	
	//função que valida as posições dos peixes, para que eles não 
	//passem do limite
	
	public double[] validatePosition(double position[]){
		for(int j = 0; j < position.length; j++){
			if(position[j] > problema.BOUNDARY_MAX){
				position[j] = problema.BOUNDARY_MAX;
			}else if(position[j] < problema.BOUNDARY_MIN){
				position[j] = this.problema.BOUNDARY_MIN;
			}
		}
		
		return position;
	}
	
	public double[] calculateIndividualDisplacement(double currentPosition[], double previousPosition[]){
		double displacement[] = new double[Parameters.DIMENSIONS];
		
		for (int i = 0; i < Parameters.DIMENSIONS; i++) {
			displacement[i] = currentPosition[i] - previousPosition[i];
		}
		return displacement;
	}
	
	private static double euclidianDistance(double[] a, double[] b){
		double sum = 0;
		for(int i = 0; i < a.length; i++){
			sum += Math.pow(a[i] - b[i], 2.0);
		}
		return Math.sqrt(sum);
	}
	
public double[] calculateBarycenter(){
		
		double barycenter[] = new double[Parameters.DIMENSIONS];
		double SomaPeso = calculateWeightSum();
		double weightSumTimesPosition = 0;
		//double positionSum = 0;
		
		for(int i = 0; i < Parameters.DIMENSIONS; i++){
			weightSumTimesPosition = 0;
			for(int j = 0; j < school.length; j++){
				weightSumTimesPosition += school[j].getWeight()*
						school[j].getCurrentPosition()[i];
			}
			//O C�LCULO DO BARICENTRO DEVE TER COMO QUOCIENTE A SOMA DOS PESOS OU SOMA DAS POSI��ES ???
			barycenter[i] = weightSumTimesPosition/SomaPeso;
		}
		return barycenter;
	}

public double[] calculateSumCollectiveMoviment(){
	
	double collectiveMoviment[] = new double[Parameters.DIMENSIONS];
	double schoolFitnessGain[] = schoolFitnessGain();//GANHO DE FITNESS DO CADURME
	double fitnessGainSum = calculateFitnessGainSum();//SOMAT�RIO DO GANHO TOTAL DO CADURME
	double fitnessTimesIndividualDisplacement = 0;
	
	//PARA QUE SERVE A �LTIMA VARIAVEL???
	
	for(int i = 0; i< Parameters.DIMENSIONS; i++){
		fitnessTimesIndividualDisplacement = 0;
		for(int j = 0; j < school.length; j++){
			if(school[j].isImprovedFitness()){
				fitnessTimesIndividualDisplacement += schoolFitnessGain[j] *
						school[j].getIndividualDisplacement()[i];
			}
		}
		if(fitnessGainSum != 0){
			collectiveMoviment[i] = fitnessTimesIndividualDisplacement / fitnessGainSum;
		}else{
			collectiveMoviment[i] = 0;
		}
		if(fitnessGainSum == 0 && Parameters.DEBUG){
			System.out.println("FitnessGainSum = "+fitnessGainSum);
		}
	}
	return collectiveMoviment;
}

private double[] schoolFitnessGain(){
	
	double schoolFitnessGain[] = new double[school.length];
	
	for(int i = 0; i < school.length; i++){
		if(school[i].isImprovedFitness()){
			schoolFitnessGain[i] = calculateFitnessGain(school[i]);
		}
	}
	return schoolFitnessGain;
}

private double calculateFitnessGainSum(){
	
	double fitnessGainSum = 0;
	double schoolFitnessGain[] = schoolFitnessGain();
	
	for(int i = 0; i < schoolFitnessGain.length; i++){
		if(school[i].isImprovedFitness()){
			fitnessGainSum += schoolFitnessGain[i];
		}
	}
	return fitnessGainSum;
}

public double calculateFitnessGain(Fish peixe){
	
	double fitnessGain = problema.calculateFitness(peixe.getCurrentPosition()) - 
			problema.calculateFitness(peixe.getPreviousPosition());
	if(isMinimumOptimization){
		//ANALISA SE O GANHO NA POSI��O ATUAL FOI MELHOR QUE
		//O GANHO NA POSI��O ANTERIOR.
		fitnessGain *= -1;	//PQ MULTIPLICA POR -1?
	}
	
	return fitnessGain;
}

public void volitiveMovement(){
	
	double position[] = new double[Parameters.DIMENSIONS];
	double barycenter[] = calculateBarycenter();
	double signal = calculateSignalFromSchoolWeightVariation();
	double distanceToBarycenter;
	
	for(int i = 0; i < school.length; i++){
		position = new double[Parameters.DIMENSIONS];
		distanceToBarycenter = euclidianDistance(school[i].getCurrentPosition(), barycenter);
		for(int j = 0; j < Parameters.DIMENSIONS; j++){
			position[j] = school[i].getCurrentPosition()[j] + 
					signal * stepVolPercentage * (problema.BOUNDARY_MAX - 
							problema.BOUNDARY_MIN) * (school[i].getCurrentPosition()[j] - 
									barycenter[j])/distanceToBarycenter;
			
		}
		school[i].setCurrentPosition(validatePosition(position));
	}
}

public double calculateSignalFromSchoolWeightVariation(){
	//CALCULA O SINAL - OU + DEPENDENDO SE A 
	//VARIA��O � POSITIVA OU NEGATIVA
	double weightVariation = 0;
	
	for(int i = 0; i < school.length; i++){
		weightVariation += school[i].getWeightVariation();
	}
	return (weightVariation >= 0 ? -1.0 : 1.0);
	
}
	
	private double calculateWeightSum(){
		double somaPeso = 0;
		
		for(int i = 0; i < school.length; i++){
			somaPeso += school[i].getWeight();
		}
		
		if(somaPeso == 0 && Parameters.DEBUG){
			System.out.println("Soma dos pesos = " + somaPeso);
		}
		return somaPeso;
	}
	
	/*public void initializeBestPosition(){
		this.bestPosition = school[0].getCurrentPosition().clone();
	}
	*/
	
	public void updateBestPosition(){
		double bestPosition[] = school[0].getCurrentPosition();
		
		for(int i = 1; i < school.length; i++){
			if(problema.calculateFitness(school[i].getCurrentPosition()) <  
					problema.calculateFitness(school[i].getPreviousPosition())){
				bestPosition = school[i].getCurrentPosition();
			}
		}
		if(problema.calculateFitness(bestPosition) <
				problema.calculateFitness(this.bestPosition)){
			this.bestPosition = (double[]) bestPosition.clone();
		}
	}
		
	private void updateStepVolPercentage(){
		stepVolPercentage -= (double)(Parameters.STEP_VOL_INIT - Parameters.STEP_VOL_FINAL)/
				(double)iterationsNumber;
	}
	
	private void updateStepIndPercetage(){
		stepIndPercentage -= (double)(Parameters.STEP_IND_INIT - Parameters.STEP_IND_FINAL)/
				(double)iterationsNumber;
	}
	
	public Fish[] getSchool(){
		return school;
	}
	//for testing
	public void setSchool(Fish school[]){
		this.school = school;
	}

	public double getBestFitness() {
		return problema.calculateFitness(bestPosition);
	}
	
	//CALCULA O MAIOR GANHO DE FITNESS APENAS ENTRE PEIXES QUE
		//TENHAM TIDO SUCESSO NO LOCAL DE PESQUISA
		public double calculateLocalSearchGreaterFitnessGain(){
			
			//RETORNA O MELHOR GANHO DE FITNESS DENTRO DO CADURME
			double greaterFitnessGain = Double.MIN_VALUE;
			double fitnessGain;
			
			for(int i = 1; i < school.length; i++){
				if(school[i].isImprovedFitness()){
					fitnessGain = calculateFitnessGain(school[i]);
					if(fitnessGain > greaterFitnessGain){
						greaterFitnessGain = fitnessGain;
					}
				}
			}
			return greaterFitnessGain;
		}
		
		public double calculateGreaterFitnessGain(){
			double greaterFitnessGain = Double.NEGATIVE_INFINITY;
			double fitnessGain;

			for (int i = 1; i < school.length; i++) {
				fitnessGain = Math.abs(calculateFitnessGain(school[i]));
				if(fitnessGain > greaterFitnessGain){
					greaterFitnessGain = fitnessGain;
				}
			}					//POR QUE Parameters.DEBUG ?
			if(greaterFitnessGain == 0 && Parameters.DEBUG){
				System.out.println("greaterFitnessGain = "+greaterFitnessGain);
			}
			return greaterFitnessGain;
		}
	public void setSchoolLocalSearchNewWeight(){
		
		double greaterFitnessGain = calculateLocalSearchGreaterFitnessGain();
		double newWeight, fitnessGain;
		
		for(int i = 0; i < school.length; i++){
			if(school[i].isImprovedFitness()){
				fitnessGain = calculateFitnessGain(school[i]);
				newWeight = validateWeight(school[i].getWeight() + fitnessGain/greaterFitnessGain);
				if(greaterFitnessGain == 0 && Parameters.DEBUG){
					System.out.println("Greater Fitness Gain = "+ greaterFitnessGain);
				}
				school[i].setWeightVariation(newWeight - school[i].getWeight());
				school[i].setWeight(newWeight);
			}
		}
	}
	
	public double validateWeight(double weight){
		if(weight < Parameters.MIN_WEIGHT){
			weight = Parameters.MIN_WEIGHT;
		}else if(weight > Parameters.MAX_WEIGHT){
			weight = Parameters.MAX_WEIGHT;
		}
		
		return weight;
	}
	
	public void movimentoColetivo(){
		
		double posicao[] = new double[Parameters.DIMENSIONS];
		double sumCollectiveMoviment[] = calculateSumCollectiveMoviment();
		
		for(int i = 0; i < school.length; i++){
			school[i].setPreviousPosition(school[i].getCurrentPosition());
			posicao = new double[Parameters.DIMENSIONS];
			for(int j = 0; j < Parameters.DIMENSIONS; j++){
				posicao[j] = (school[i].getCurrentPosition()[j] + sumCollectiveMoviment[j]); 
			}
			school[i].setCurrentPosition(validatePosition(posicao));
		}
	}
	public void setSchoolNewWeight(){
		
		//MODIFICA O NOVO PESO DO CARDUME E ASSIM EM CADA PEIXE
		
		double greaterFitnessGain = calculateGreaterFitnessGain();
		double newWeight;
		double fitnessGain;
		
		for(int i = 0; i < school.length; i++){
			fitnessGain = calculateFitnessGain(school[i]);
			newWeight = validateWeight(school[i].getWeight() + fitnessGain/greaterFitnessGain);
			school[i].setWeightVariation(newWeight - school[i].getWeight());
			school[i].setWeight(newWeight);
		}
	}
	
	public double[] createNeighboorPosition(double[] position){
		double neighboorPosition[] = new double[position.length];
		
		for(int i = 0; i < position.length; i++){
			neighboorPosition[i] = position[i] + stepIndPercentage*(problema.BOUNDARY_MAX - 
					problema.BOUNDARY_MIN)*createRandomNumberInRange(-1.0, 1.0);
		}
		
		return neighboorPosition;
	}
	
	private double createRandomNumberInRange(double min, double max) {
		// TODO Auto-generated method stub
		return min + (max - min)* rand.nextDouble();
	}
	
	public void localSearch(){
		
		double[] neighboorPosition;
		
		for(int i = 0; i < school.length; i++){
			neighboorPosition = validatePosition(createNeighboorPosition(school[i].getCurrentPosition()));
			
			//PEIXE QUE MELHOROU A APTID�O NA ITERA��O ATUAL
			if(problema.calculateFitness(neighboorPosition) < problema.calculateFitness(school[i].getCurrentPosition())){
				school[i].setImprovedFitness(true);
				school[i].setPreviousPosition(school[i].getCurrentPosition());
				school[i].setCurrentPosition(neighboorPosition);
				school[i].setIndividualDisplacement(calculateIndividualDisplacement(
						school[i].getCurrentPosition(), school[i].getPreviousPosition()));
				
			}else{
				school[i].setImprovedFitness(false);
			}
		}
	}
	
	public void Busca(int iteracoes, int simulacoes){
		//Vai simular de acordo com o n�mero de sim
		isMinimumOptimization = 0 < 1;
		iterationsNumber = iteracoes;
		
		initializeSwarm();
		initializeBestPosition();
		stepIndPercentage = Parameters.STEP_IND_INIT;
		stepVolPercentage = Parameters.STEP_VOL_INIT;
		
		for(int i = 0; i < iteracoes; i++){
			localSearch();
			setSchoolLocalSearchNewWeight();
			movimentoColetivo();
			volitiveMovement();
			setSchoolNewWeight();
			updateBestPosition();
			updateStepIndPercetage();
			updateStepVolPercentage();
			System.out.println((getBestFitness()+ "").replace('.', ','));
		}
		System.out.println("Melhor Fitness = " + getBestFitness());
	}
	
}
