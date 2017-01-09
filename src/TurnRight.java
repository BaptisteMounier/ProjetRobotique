//Fichier : TurnRight.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

/** Comportement de quart-de-tour vers la droite */
public class TurnRight implements Behavior {
	/** Robot */
	private Robot robot;

	/** Constructeur principal du comportement
	 * @param robot Robot */
	public TurnRight(Robot robot){
		this.robot = robot;
	}

	@Override
	public boolean takeControl() {
		return (Button.RIGHT.isDown());
	}

	@Override
	public void action() {
		Motor.B.stop(true);
		Motor.C.stop(true);
		
		DifferentialPilot pilot = new DifferentialPilot(5.6, 12.0, Motor.B, Motor.C);
			//Diamètre des roues, espace entre les roues, moteur gauche, moteur droit
		
		pilot.setTravelSpeed(5.); //En cm
		pilot.setRotateSpeed(30.); //Degrés/sec
		pilot.travel(13.); //Avance de 13cm = 1,5 case + 1 limite entre deux cases
		pilot.rotate(-80.); //En degré dans le sens inverse des aiguilles d'une montre
		//Demander rotation de 80° effectue une rotation de 90°
		pilot.travel(-2.); //Recule de 2cm (pour bien rester callé au centre de la case après rotation)
		
		//Modification de l'orientation
		robot.updateDirection(Robot.ROTATION_DROITE);
	}

	@Override
	public void suppress() {
		//Rien à faire en cas d'arrêt du comportement
	}

}
