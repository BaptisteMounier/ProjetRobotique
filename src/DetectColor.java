//Fichier : DetectColor.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.subsumption.Behavior;

/** Comportement de détection d'une couleur */
public class DetectColor implements Behavior {

	/** Capteur de couleur */
	private EV3ColorSensor colorSensor;
	/** Couleur actuellement détectée par le capteur de couleur */
	private static int currentColor = Color.NONE;
	/** Booléen vrai si en train de changer de case */
	private static boolean changing = true;
	
	/** Constructeur principal du comportement
	 * @param cs Capteur de couleur */
	public DetectColor(EV3ColorSensor cs){
		this.colorSensor = cs;
	}

	@Override
	public boolean takeControl() {
		//Prend le contrôle si la couleur détectée est une nouvelle couleur
		return (colorSensor.getColorID() != currentColor);
	}

	@Override
	public void action() {
		int detectedColor = colorSensor.getColorID();
		
		//Si détection de noir, changement de case sur le plateau
		if(detectedColor == Color.BLACK){
			changing = true;
		}
		//Si changement de case et couleur détectée différente de noir
		else if(changing){
			//TODO Mettre à jour carte avec la nouvelle couleur en fonction de la direction d'avancement
		}
		currentColor = detectedColor;
	}

	@Override
	public void suppress() {
		//Rien à faire en cas d'arrêt du comportement
	}

}
