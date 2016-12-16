//Fichier : StopExploration.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

import lejos.hardware.motor.Motor;
import lejos.robotics.subsumption.Behavior;

/** Comportement d'arr�t de l'exploration */
public class StopExploration implements Behavior{
	/** Robot */
	private Robot robot;

	/** Constructeur principal du comportement
	 * @param robot Robot */
	public StopExploration(Robot robot){
		this.robot = robot;
	}

	@Override
	public boolean takeControl() {
		//Prend le contr�le quand le robot se consid�re hors de la carte
		return robot.isOutOfMap();
	}

	@Override
	public void action() {
		//Arr�t du robot
		Motor.B.stop(true);
		Motor.C.stop(true);
	}

	@Override
	public void suppress() {
		
	}

}
