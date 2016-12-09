//Fichier : Main.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.BaseSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/** Projet d'exploration robotique */
public class Main {

	/** Point d'entr�e du programme */
	public static void main(String[] args) {
		//Affichage d'un message � l'�cran
		LCD.drawString("Exploration !", 0, 1);
		LCD.refresh();
		
		//Initialisation de la carte
		Carte map = new Carte(7, 5);
		
		//Attente d'un appui sur le bouton droit pour d�marrer
		Button.RIGHT.waitForPressAndRelease();
		
		
		//Ouverture des ports pour l'utilisation des capteurs
		BaseSensor[] sensors = {};
		
		//Cr�ation des comportements
		Behavior b1 = new SaveBattery(sensors); //S'arr�te quand la batterie est faible
		Behavior b2 = new ShutDown(sensors); //S'arr�te lors de l'appuie sur un bouton
		Behavior[] bArray = {b1, b2}; //Du moins prioritaire au plus prioritaire
		
		//Arbitrator pour coordonner les comportements
		Arbitrator arby = new Arbitrator(bArray);
		arby.start();
	}

}
