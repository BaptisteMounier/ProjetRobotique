//Fichier : DetectColor.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
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
		
		System.out.println(colorSensor.getCurrentMode());
		Button.UP.waitForPressAndRelease();
		
		switch(detectedColor){
		case Color.BLACK:
			System.out.println();
			System.out.println("Noir");
			break;
		case Color.BLUE:
			System.out.println("Bleu");
			break;
		case Color.BROWN:
			System.out.println("Marron");
			break;
		case Color.CYAN:
			System.out.println("Cyan");
			break;
		case Color.DARK_GRAY:
			System.out.println("Gris foncé");
			break;
		case Color.GRAY:
			System.out.println("Gris");
			break;
		case Color.GREEN:
			System.out.println("Vert");
			break;
		case Color.LIGHT_GRAY:
			System.out.println("Gris clair");
			break;
		case Color.MAGENTA:
			System.out.println("Magenta");
			break;
		case Color.NONE:
			System.out.println("Aucune couleur");
			break;
		case Color.ORANGE:
			System.out.println("Orange");
			break;
		case Color.PINK:
			System.out.println("Rose");
			break;
		case Color.RED:
			System.out.println("Rouge");
			break;
		case Color.WHITE:
			System.out.println("Blanc");
			break;
		case Color.YELLOW:
			System.out.println("Jaune");
			break;
	}

//		//Si détection de noir, changement de case sur le plateau
//		if(detectedColor == Color.BLACK){
//			System.out.println("Noir");
//			changing = true;
//		}
//		//Si changement de case et couleur détectée différente de noir
//		else if(changing){
//			System.out.println(detectedColor);
//			changing = false;
//			if(!robot.isAlreadyExplored()){
//				robot.updateMap(detectedColor);
//			}
//			robot.updatePos();
//		}
//		oldColor = detectedColor;
	}

	@Override
	public void suppress() {
		//Rien à faire en cas d'arrêt du comportement
	}

}
