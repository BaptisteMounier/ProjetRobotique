//Fichier : Main.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.BaseSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/** Projet d'exploration robotique */
public class Main {

	/** Point d'entrée du programme */
	public static void main(String[] args) {
		//Affichage d'un message à l'écran
//		LCD.drawString("Exploration !", 0, 1);
		LCD.refresh();

		//Initialisation des informations du robot
		Robot robot = new Robot(7, 5, 0, 6, Robot.VERS_LE_HAUT);
		Motor.B.setSpeed(100);
		Motor.C.setSpeed(100);

		//Attente d'un appui sur le bouton droit pour démarrer
		Button.RIGHT.waitForPressAndRelease();


		//Ouverture des ports pour l'utilisation des capteurs
		EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S3);
		BaseSensor[] sensors = {cs};

		//Création des comportements
		Behavior bSaveBattery = new SaveBattery(sensors); //S'arrête quand la batterie est faible
		Behavior bShutDown = new ShutDown(sensors); //S'arrête lors de l'appuie sur un bouton
		Behavior bDriveForward = new DriveForward(); //Avancer
		Behavior bDetectColor = new DetectColor(cs, robot);
		Behavior[] bArray = {
				bDriveForward, bDetectColor, bSaveBattery, bShutDown}; //Du moins prioritaire au plus prioritaire

		//Arbitrator pour coordonner les comportements
		Arbitrator arby = new Arbitrator(bArray);
		try{
			arby.start();
		}catch(Exception e){
			e.printStackTrace();
			Button.RIGHT.waitForPressAndRelease();
			bShutDown.action();
		}
	}

}
