//Fichier : StopExploration.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

package robot;

import lejos.hardware.motor.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

/** Comportement d'arr�t de l'exploration : recule si sort de l'environnement */
public class StopExploration implements Behavior{
	/** Robot */
	private Robot robot;
	/** Comportement de d�tection des couleurs */
	private DetectColor bDetectColor;

	/** Constructeur principal du comportement
	 * @param robot Robot */
	public StopExploration(Robot robot, DetectColor bDetectColor){
		this.robot = robot;
		this.bDetectColor = bDetectColor;
	}

	@Override
	public boolean takeControl() {
		//Prend le contr�le quand le robot se consid�re hors de la carte
		return robot.isOutOfMap();
	}

	@Override
	public void action() {
		//Recule d'une case
		
		DifferentialPilot pilot = new DifferentialPilot(5.6, 12.0, Motor.B, Motor.C);
			//Diam�tre des roues, espace entre les roues, moteur gauche, moteur droit
		
		pilot.setTravelSpeed(5.); //En cm
		pilot.setRotateSpeed(30.); //Degr�s/sec
		pilot.travel(-4.);
		
		robot.updatePosInverse();
		bDetectColor.resetBehavior();
	}

	@Override
	public void suppress() {
		
	}

}
