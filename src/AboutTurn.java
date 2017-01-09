//Fichier : AboutTurn.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

/** Comportement de demi-tour */
public class AboutTurn implements Behavior {
	/** Robot */
	private Robot robot;

	/** Constructeur principal du comportement
	 * @param robot Robot */
	public AboutTurn(Robot robot){
		this.robot = robot;
	}

	@Override
	public boolean takeControl() {
		return (Button.DOWN.isDown());
	}

	@Override
	public void action() {
		Motor.B.stop(true);
		Motor.C.stop(true);
		
		DifferentialPilot pilot = new DifferentialPilot(5.6, 12.0, Motor.B, Motor.C);
			//Diamètre des roues, espace entre les roues, moteur gauche, moteur droit
		
		pilot.setTravelSpeed(5.); //En cm
		pilot.setRotateSpeed(30.); //Degrés/sec
		
		/* Execution d'un quart-de-tour droit 2 fois de suite */
		pilot.travel(13.); //Avance de 13cm = 1,5 case + 1 limite entre deux cases
		pilot.rotate(-80.); //En degré dans le sens inverse des aiguilles d'une montre
		//Demander rotation de 80° effectue une rotation de 90°
		
		pilot.travel(11.); //Simplification : recule de 2cm et avance de 13cm
		pilot.rotate(-80.); //En degré dans le sens inverse des aiguilles d'une montre
		//Demander rotation de 80° effectue une rotation de 90°
		pilot.travel(-2.); //Recule de 2cm (pour bien rester callé au centre de la case après rotation)
		
		//Modification de l'orientation
		robot.updateDirection(Robot.DEMI_TOUR);
	}

	@Override
	public void suppress() {
		//Rien à faire en cas d'arrêt du comportement
	}

}
