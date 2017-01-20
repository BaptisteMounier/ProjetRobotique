//Fichier : DriveForward.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

package robot;

import lejos.hardware.motor.Motor;
import lejos.robotics.subsumption.Behavior;

/** Comportement d'avancement du robot */
public class DriveForward implements Behavior {

	@Override
	public boolean takeControl(){
		return true;
	}
	
	@Override
	public void action(){
		Motor.B.setSpeed(100);
		Motor.C.setSpeed(100);
		
		Motor.B.forward();
		Motor.C.forward();
	}
	
	@Override
	public void suppress(){
		Motor.B.stop(true);
		Motor.C.stop(true);
	}
}
