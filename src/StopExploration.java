
import lejos.robotics.subsumption.Behavior;

public class StopExploration implements Behavior{
	/** Robot */
	private Robot robot;
	/** Comportement lié à l'arrêt complet du robot */
	private ShutDown sd;

	/** Constructeur principal du comportement
	 * @param cs Capteur de couleur */
	public StopExploration(Robot robot, ShutDown sd){
		this.robot = robot;
		this.sd = sd;
	}

	@Override
	public boolean takeControl() {
		return robot.isOutOfMap();
	}

	@Override
	public void action() {
		sd.action();
	}

	@Override
	public void suppress() {
		
	}

}
