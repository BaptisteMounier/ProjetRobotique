//Fichier : DetectColor.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

import lejos.hardware.Button;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.subsumption.Behavior;

/** Comportement de détection d'une couleur */
public class DetectColor implements Behavior {

	/** Capteur de couleur */
	private EV3ColorSensor colorSensor;
	/** Dernière couleur détectée par le capteur de couleur */
	private static int oldColor = Color.NONE;
	/** Nouvelle couleur détectée par le capteur de couleur */
	private static int detectedColor = Color.NONE;
	/** Booléen vrai si en train de changer de case */
	private static boolean changing = true;
	/** Variantes R, G et B indépendantes */
	private float[] sample;
	/** Robot */
	private Robot robot;

	/** Constructeur principal du comportement
	 * @param cs Capteur de couleur */
	public DetectColor(EV3ColorSensor cs, Robot robot){
		this.colorSensor = cs;
		this.robot = robot;
		this.sample = new float[3];
	}

	@Override
	public boolean takeControl() {
		//Prend le contrôle si la couleur détectée est une nouvelle couleur
		detectedColor = colorSensor.getColorID();
		colorSensor.getRGBMode().fetchSample(sample, 0);
		return (detectedColor != oldColor);
	}

	@Override
	public void action() {
		//System.out.println(sample[0] + " " + sample[1] + " " + sample[2]);

		//Si détection de noir, changement de case sur le plateau
		if(detectedColor == Color.BLACK){
			System.out.println("Noir");
			changing = true;
		}
		//Si changement de case et couleur détectée différente de noir
		else if(changing){
			System.out.println(detectedColor);
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
		//Rien à faire en cas d'arrêt du comportement
	}

}
