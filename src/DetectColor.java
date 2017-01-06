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
	/** Derni�re couleur d�tect�e par le capteur de couleur */
	private static int oldColor = Color.NONE;
	/** Nouvelle couleur d�tect�e par le capteur de couleur */
	private static int detectedColor = Color.NONE;
	/** Bool�en vrai si en train de changer de case */
	private static boolean changing = true;
	/** Variantes R, G et B ind�pendantes */
	private float[] sample;
	/** Robot */
	private Robot robot;

	/** Constructeur principal du comportement
	 * @param cs Capteur de couleur
	 * @param robot Robot */
	public DetectColor(EV3ColorSensor cs, Robot robot){
		this.colorSensor = cs;
		this.robot = robot;
		this.sample = new float[3];
	}

	@Override
	public boolean takeControl() {
		//Prend le contr�le si la couleur d�tect�e est une nouvelle couleur
		detectedColor = colorSensor.getColorID();
		colorSensor.getRGBMode().fetchSample(sample, 0);
		detectedColor = interpreterCouleur();
		return (detectedColor != oldColor);
	}

	@Override
	public void action() {
		//Si d�tection de noir, changement de case sur le plateau
		if(detectedColor == Color.BLACK && oldColor != Color.BLACK){
//			System.out.println(colorToString(detectedColor));
			
			changing = true;
			oldColor = detectedColor;
		}
		//Si changement de case et couleur d�tect�e diff�rente de noir
		else if(detectedColor != Color.BLACK && changing){
//			System.out.println(colorToString(detectedColor));
			robot.displayMap();
			
			changing = false;
			if(!robot.isAlreadyExplored()){
				robot.updateMap(detectedColor);
			}
			robot.updatePos();
			oldColor = detectedColor;
		}
	}

	@Override
	public void suppress() {
		//Rien � faire en cas d'arr�t du comportement
	}
	
	/** Convertit une couleur en cha�ne de caract�res
	 * @param detectedColor Couleur
	 * @return Nom de la couleur */
	public String colorToString(int detectedColor){
		switch(detectedColor){
			case Color.BLACK:
				return("Noir");
			case Color.BLUE:
				return("Bleu");
			case Color.BROWN:
				return("Marron");
			case Color.RED:
				return("Rouge");
			case Color.WHITE:
				return("Blanc");
			case Color.GREEN:
				return("Vert");
			case Color.CYAN:
				return("Cyan");
			case Color.DARK_GRAY:
				return("Gris fonc�");
			case Color.GRAY:
				return("Gris");
			case Color.LIGHT_GRAY:
				return("Gris clair");
			case Color.MAGENTA:
				return("Magenta");
			case Color.ORANGE:
				return("Orange");
			case Color.PINK:
				return("Rose");
			case Color.YELLOW:
				return("Jaune");
			case Color.NONE:
				 default:
				return("Aucune couleur");
		}
	}

	public int interpreterCouleur(){
		//sample[red, green, blue]
		float red = sample[0];
		float green = sample[1];
		float blue = sample[2];
		
		//Calcule de la couleur r�elle
		if(red + green + blue > 0.5f){
			return Color.WHITE;
		} else if(red + green + blue < 0.15f){
			return Color.BLACK;
		} else if(green > (red + blue)){
			return Color.GREEN;
		} else if(red > (green + blue) * 4){
			return Color.RED;
		} else if((green + blue) > red * 2){
			return Color.BLUE;
		} else if(red > (green + blue) * 2){
			return Color.BROWN;
		} else {
			//Ne trouve pas blanc � cause de la limite de la case (noir + blanc m�lang�s)
			return Color.WHITE;
		}
	}
}
