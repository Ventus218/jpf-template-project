package ass01_concurrent.simtrafficbase_improved;

import java.util.Optional;

import ass01_concurrent.simengineseq_improved.Percept;

/**
 * 
 * Percept for Car Agents
 * 
 * - position on the road
 * - nearest car, if present (distance)
 * - nearest semaphore, if present (distance)
 * 
 */
public class CarPercept implements Percept {
    double roadPos;
    Optional<CarAgentInfo> nearestCarInFront;
    Optional<TrafficLightInfo> nearestSem;

    public CarPercept(double roadPos, Optional<CarAgentInfo> nearestCarInFront, Optional<TrafficLightInfo> nearestSem) {
        this.roadPos = roadPos;
        this.nearestCarInFront = nearestCarInFront;
        this.nearestSem = nearestSem;
    }

    public double roadPos() {
        return roadPos;
    }

    public Optional<CarAgentInfo> nearestCarInFront() {
        return nearestCarInFront;
    }

    public Optional<TrafficLightInfo> nearestSem() {
        return nearestSem;
    }
}