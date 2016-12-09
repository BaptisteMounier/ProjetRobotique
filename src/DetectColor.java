//Fichier : DetectColor.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.subsumption.Behavior;

/** Comportement de d�tection d'une couleur */
public class DetectColor implements Behavior {

	/** Capteur de couleur */
	private EV3ColorSensor colorSensor;
	/** Couleur actuellement d�tect�e par le capteur de couleur */
	private static int currentColor = Color.NONE;
	/** Bool�en vrai si en train de changer de case */
	private static boolean changing = true;
	
	/** Constructeur principal du comportement
	 * @param cs Capteur de couleur */
	public DetectColor(EV3ColorSensor cs){
		this.colorSensor = cs;
	}

	@Override
	public boolean takeControl() {
		//Prend le contr�le si la couleur d�tect�e est une nouvelle couleur
		return (colorSensor.getColorID() != currentColor);
	}

	@Override
	public void action() {
		int detectedColor = colorSensor.getColorID();
		
		//Si d�tection de noir, changement de case sur le plateau
		if(detectedColor == Color.BLACK){
			changing = true;
		}
		//Si changement de case et couleur d�tect�e diff�rente de noir
		else if(changing){
			//TODO Mettre � jour carte avec la nouvelle couleur en fonction de la direction d'avancement
		}
		currentColor = detectedColor;
	}

	@Override
	public void suppress() {
		//Rien � faire en cas d'arr�t du comportement
	}

}
