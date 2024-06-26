package ass01_concurrent.simtrafficexamples_improved;

public class RunTrafficSimulationMassiveTest {

	public static void main(String[] args) {		

		int numCars = 3;
		int nSteps = 2;
		
		TrafficSimulationSingleRoadMassiveNumberOfCars simulation = new TrafficSimulationSingleRoadMassiveNumberOfCars(numCars);
		simulation.setup();
		
		log("Running the simulation: " + numCars + " cars, for " + nSteps + " steps ...");
		
		simulation.run(nSteps);

		long d = simulation.getSimulationDuration();
		log("Completed in " + d + " ms - average time per step: " + simulation.getAverageTimePerCycle() + " ms");
	}
	
	private static void log(String msg) {
		System.out.println("[ SIMULATION ] " + msg);
	}
}
