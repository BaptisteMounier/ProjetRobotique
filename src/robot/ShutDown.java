//Fichier : ShutDown.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

package robot;

import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.BaseSensor;
import lejos.remote.nxt.NXTConnection;
import lejos.robotics.subsumption.Behavior;

/** Comportement d'arr�t du Robot */
public class ShutDown implements Behavior {
	
	/** Tableau des capteurs dont les ports sont ouverts */
	private BaseSensor[] sensors;
	/** Connexion BlueTooth */
	private NXTConnection btc;

	/** Constructeur principal du comportement
	 * @param s Tableau des capteurs dont les ports sont � fermer en cas d'arr�t */
	public ShutDown(BaseSensor[] s){
		this.sensors = s;
	}

	/** Constructeur principal du comportement
	 * @param s Tableau des capteurs dont les ports sont � fermer en cas d'arr�t */
	public ShutDown(BaseSensor[] s, NXTConnection btc){
		this.sensors = s;
		this.btc = btc;
	}

	@Override
	public boolean takeControl() {
		//Prend le contr�le si le bouton droit est appuy�
		return (Button.ENTER.isDown());
	}

	@Override
	public void action() {
		
		//Arr�t du robot
		Motor.B.stop(true);
		Motor.C.stop(true);

		//Fermeture des ports
		for(int i=0; i<sensors.length; i++){
			sensors[i].close();
		}
		try {
			if(btc != null)
				btc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Arr�t du programme
		System.exit(0);
		
	}

	@Override
	public void suppress() {
		//Non impl�ment�e car la m�thode action() arr�te le programme
	}

}
