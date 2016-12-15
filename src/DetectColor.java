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
		detectedColor = interpreterCouleur();
		return (detectedColor != oldColor);
	}

	@Override
	public void action() {
		//System.out.println(sample[0] + " " + sample[1] + " " + sample[2]);
		switch(detectedColor){
		case Color.BLACK:
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

	public int interpreterCouleur(){
		//sample[red, green, blue]
		
		//Recherche de la composante maximale
		float max = 0.f;
		int indexMax = -1;
		float[] autres = {0.f, 0.f};
		int index = 0;
		for(int i=0; i<sample.length; i++){
			if (sample[i] > max) {
				if(i != 0){
					autres[index++] = max;
				}
				max = sample[i];
				indexMax = i;
			} else {
				autres[index++] = sample[i];
			}
		}
		
		//Couleur blanche
		if(sample[0] > 0.8f && sample[1] > 0.8f && sample[2] > 0.8f){
			return Color.WHITE;
		}
		//Couleur noire
		else if(sample[0] < 0.2f && sample[1] < 0.2f && sample[2] < 0.2f){
			return Color.BLACK;
		}
		//Cherche si la couleur est une primaire
		else{//if(max > autres[0]*2 || max > autres[1]*2){
			if(max > autres[0]*2 && max > autres[1]*2){
				if(indexMax == 0){
					return Color.RED;
				}
				else if(indexMax == 1){
					return Color.GREEN;
				}
				else{
					return Color.BLUE;
				}
			} else {
				return Color.BROWN;
			}
		}
		
	}
}
