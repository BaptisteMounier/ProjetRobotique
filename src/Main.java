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

	/** Point d'entr�e du programme */
	public static void main(String[] args) {
		//Affichage d'un message � l'�cran
//		LCD.drawString("Exploration !", 0, 1);
		LCD.refresh();

		//Initialisation des informations du robot
		Robot robot = new Robot(7, 5, 0, 6, Robot.VERS_LE_HAUT);
		Motor.B.setSpeed(100);
		Motor.C.setSpeed(100);

		//Attente d'un appui sur le bouton droit pour d�marrer
		Button.RIGHT.waitForPressAndRelease();


		//Ouverture des ports pour l'utilisation des capteurs
		EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S3);
		BaseSensor[] sensors = {cs};

		//Cr�ation des comportements
		Behavior bSaveBattery = new SaveBattery(sensors); //S'arr�te quand la batterie est faible
		Behavior bShutDown = new ShutDown(sensors); //S'arr�te lors de l'appuie sur un bouton
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
