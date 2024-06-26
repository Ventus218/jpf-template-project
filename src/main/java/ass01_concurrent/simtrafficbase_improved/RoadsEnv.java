package ass01_concurrent.simtrafficbase_improved;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import ass01_concurrent.simengineseq_improved.*;

public class RoadsEnv extends AbstractEnvironment {
		
	private static final int MIN_DIST_ALLOWED = 5;
	private static final int CAR_DETECTION_RANGE = 30;
	private static final int SEM_DETECTION_RANGE = 30;
	
	/* list of roads */
	private List<Road> roads;

	/* traffic lights */
	private List<TrafficLight> trafficLights;
	
	/* cars situated in the environment */	
	private HashMap<String, CarAgentInfo> registeredCars;


	public RoadsEnv() {
		super("traffic-env");
		registeredCars = new HashMap<>();	
		trafficLights = new ArrayList<>();
		roads = new ArrayList<>();
	}
	
	@Override
	public void init() {
		for (TrafficLight tl: trafficLights) {
			tl.init();
		}
	}
	
	@Override
	public void step(int dt) {
		for (TrafficLight tl: trafficLights) {
			tl.step(dt);
		}
	}
	
	public void registerNewCar(CarAgent car, Road road, double pos) {
		registeredCars.put(car.getId(), new CarAgentInfo(car, road, pos));
	}

	public Road createRoad(P2d p0, P2d p1) {
		Road r = new Road(p0, p1);
		this.roads.add(r);
		return r;
	}

	public TrafficLight createTrafficLight(P2d pos, TrafficLight.TrafficLightState initialState, int greenDuration, int yellowDuration, int redDuration) {
		TrafficLight tl = new TrafficLight(pos, initialState, greenDuration, yellowDuration, redDuration);
		this.trafficLights.add(tl);
		return tl;
	}

	@Override
	public Percept getCurrentPercepts(String agentId) {
		
		CarAgentInfo carInfo = registeredCars.get(agentId);
		double pos = carInfo.getPos();
		Road road = carInfo.getRoad();
		Optional<CarAgentInfo> nearestCar = getNearestCarInFront(road,pos, CAR_DETECTION_RANGE);
		Optional<TrafficLightInfo> nearestSem = getNearestSemaphoreInFront(road,pos, SEM_DETECTION_RANGE);
		
		return new CarPercept(pos, nearestCar, nearestSem);
	}

	private Optional<CarAgentInfo> getNearestCarInFront(Road road, double carPos, double range){
		List<CarAgentInfo> carInfos = new ArrayList<>();
		for (Entry<String, CarAgentInfo> entry : registeredCars.entrySet()) {
			carInfos.add(entry.getValue());
		}

		List<CarAgentInfo> usefulCarInfos = new ArrayList<>();
		for (CarAgentInfo carInfo : carInfos) {
			double dist = carInfo.getPos() - carPos;
			if ((dist > 0 && dist <= range) && (carInfo.getRoad() == road)) {
				usefulCarInfos.add(carInfo);
			}
		}

		return usefulCarInfos.stream().min((c1, c2) -> (int) Math.round(c1.getPos() - c2.getPos()));
	}

	private Optional<TrafficLightInfo> getNearestSemaphoreInFront(Road road, double carPos, double range){
		List<TrafficLightInfo> trafficLightInfos = new ArrayList<>(road.getTrafficLights());

		List<TrafficLightInfo> usefulTrafficLightInfos = new ArrayList<>();
		for (TrafficLightInfo trafficLightInfo : trafficLightInfos) {
			if (trafficLightInfo.roadPos() > carPos) {
				usefulTrafficLightInfos.add(trafficLightInfo);
			}
		}
		return trafficLightInfos.stream().min((c1, c2) -> (int) Math.round(c1.roadPos() - c2.roadPos()));
	}
	
	
	@Override
	public void processActions() {
		for (Action act: submittedActions()) {
			if (act instanceof MoveForward) { 
				MoveForward mv = (MoveForward) act;
				CarAgentInfo info = registeredCars.get(mv.agentId());
				Road road = info.getRoad();
				Optional<CarAgentInfo> nearestCar = getNearestCarInFront(road, info.getPos(), CAR_DETECTION_RANGE);
				
				if (nearestCar.isPresent()) {
					double dist = nearestCar.get().getPos() - info.getPos();
					if (dist > mv.distance() + MIN_DIST_ALLOWED) {
						info.updatePos(info.getPos() + mv.distance());
					}
				} else {
					info.updatePos(info.getPos() + mv.distance());
				}
	
				if (info.getPos() > road.getLen()) {
					info.updatePos(0);
				}
			}
		}
	}
	
	
	public List<CarAgentInfo> getAgentInfo(){
		return this.registeredCars.entrySet().stream().map(el -> el.getValue()).collect(Collectors.toList());
	}

	public List<Road> getRoads(){
		return roads;
	}
	
	public List<TrafficLight> getTrafficLights(){
		return trafficLights;
	}
}
