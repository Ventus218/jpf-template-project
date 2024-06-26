package ass01_concurrent.simengineseq_improved;

import java.util.Optional;
import java.util.Random;

/**
 * 
 * Base  class for defining types of agents taking part to the simulation
 * 
 */
public abstract class AbstractAgent {
	
	private String myId;
	private AbstractEnvironment env;
	protected Optional<Random> random = Optional.empty();
	
	/**
	 * Each agent has an identifier
	 * 
	 * @param id
	 */
	protected AbstractAgent(String id) {
		this.myId = id;
	}

	protected AbstractAgent(String id, Optional<Random> random) {
		this(id);
		this.random = random;
	}
	
	/**
	 * This method is called at the beginning of the simulation
	 * 
	 * @param env
	 */
	public void init(AbstractEnvironment env) {
		this.env = env;
	}
	
	/**
	 * This method is called at each step of the simulation
	 * 
	 * @param dt - logical time step
	 */
	abstract public void step(int dt);
	

	public String getId() {
		return myId;
	}
	
	protected AbstractEnvironment getEnv() {
		return this.env;
	}
}
