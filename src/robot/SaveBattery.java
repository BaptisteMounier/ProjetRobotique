//Fichier : SaveBattery.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

package robot;

import lejos.hardware.Battery;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.BaseSensor;
import lejos.robotics.subsumption.Behavior;

/** Comportement d'arrêt du Robot en cas de batterie faible */
public class SaveBattery implements Behavior {
	/** Tableau des capteurs dont les ports sont ouverts */
	private BaseSensor[] sensors;
	
	/** Constructeur principal du comportement
	 * @param s Tableau des capteurs dont les ports sont à fermer en cas d'arrêt */
	public SaveBattery(BaseSensor[] s){
		this.sensors = s;
	}

	@Override
	public boolean takeControl() {
		//Prend le contrôle si la batterie est faible
		return (Battery.getVoltage() <= 1.5f);
	}

	@Override
	public void action() {
		//Arrêt du robot
		Motor.B.stop(true);
		Motor.C.stop(true);
		
		//Fermeture des ports
		for(int i=0; i<sensors.length; i++){
			sensors[i].close();
		}
		
		//Arrêt du programme
		System.exit(0);
	}

	@Override
	public void suppress() {
		//Non implémentée car la méthode action() arrête le programme
	}

}
