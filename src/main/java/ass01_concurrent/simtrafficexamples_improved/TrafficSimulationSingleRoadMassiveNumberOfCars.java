package ass01_concurrent.simtrafficexamples_improved;

import java.util.Random;
import java.util.Optional;
import ass01_concurrent.simengineseq_improved.*;
import ass01_concurrent.simtrafficbase_improved.*;

public class TrafficSimulationSingleRoadMassiveNumberOfCars extends AbstractSimulation {

	private int numCars;
	
	public TrafficSimulationSingleRoadMassiveNumberOfCars(int numCars) {
		super();
		this.numCars = numCars;
	}

	public TrafficSimulationSingleRoadMassiveNumberOfCars(int numCars, Random random) {
		super(Optional.ofNullable(random));
		this.numCars = numCars;
	}
	
	public void setup() {
		this.setupTimings(0, 1);

		RoadsEnv env = new RoadsEnv();
		this.setupEnvironment(env);
		
		Road road = env.createRoad(new P2d(0,300), new P2d(15000,300));

		for (int i = 0; i < numCars; i++) {
			
			String carId = "car-" + i;
			double initialPos = i*10;			
			double carAcceleration = 1; //  + gen.nextDouble()/2;
			double carDeceleration = 0.3; //  + gen.nextDouble()/2;
			double carMaxSpeed = 7; // 4 + gen.nextDouble();
						
			CarAgent car = new CarAgentBasic(carId, env, 
									road,
									initialPos, 
									carAcceleration, 
									carDeceleration,
									carMaxSpeed,
									newRandomGenerator());
			this.addAgent(car);
			
			/* no sync with wall-time */
		}
		
	}	
}
	