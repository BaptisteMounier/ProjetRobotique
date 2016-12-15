//Fichier : DetectColor.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

import lejos.hardware.Button;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.subsumption.Behavior;

/** Comportement de d�tection d'une couleur */
public class DetectColor implements Behavior {

	/** Capteur de couleur */
	private EV3ColorSensor colorSensor;
	/** Derni�re couleur d�tect�e par le capteur de couleur */
	private static int oldColor = Color.NONE;
	/** Nouvelle couleur d�tect�e par le capteur de couleur */
	private static int detectedColor = Color.NONE;
	/** Bool�en vrai si en train de changer de case */
	private static boolean changing = true;
	/** Robot */
	private  Robot robot;

	/** Constructeur principal du comportement
	 * @param cs Capteur de couleur */
	public DetectColor(EV3ColorSensor cs, Robot robot){
		this.colorSensor = cs;
		this.robot = robot;
	}

	@Override
	public boolean takeControl() {
		//Prend le contr�le si la couleur d�tect�e est une nouvelle couleur
		detectedColor = colorSensor.getColorID();
		return (detectedColor != oldColor);
	}

	@Override
	public void action() {
		System.out.println(detectedColor);
		Button.RIGHT.waitForPressAndRelease();
//		Color color = new java.awt.Color(detectedColor)
//		System.out.println(detectedColor);
//		Color.

		//Si d�tection de noir, changement de case sur le plateau
		if(detectedColor == Color.BLACK){
			System.out.println("Noir");
			changing = true;
		}
		//Si changement de case et couleur d�tect�e diff�rente de noir
		else if(changing){
			changing = false;
			if(!robot.isAlreadyExplored()){
				robot.updateMap(detectedColor);
			}
			robot.updatePos();
		}
		oldColor = detectedColor;
	}

	@Override
	public void suppress() {
		//Rien � faire en cas d'arr�t du comportement
	}

}
