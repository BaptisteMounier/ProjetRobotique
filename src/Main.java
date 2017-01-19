//Fichier : Main.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

import lejos.hardware.Button;
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
		//Initialisation des informations du robot
		final Robot robot = new Robot(7, 5, 0, 6, Robot.VERS_LE_HAUT);
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
		Behavior bStopExploration = new StopExploration(robot, (DetectColor) bDetectColor);
		Behavior bTurnRight = new TurnRight(robot, bDriveForward);
		Behavior bTurnLeft = new TurnLeft(robot, bDriveForward);
		Behavior bAboutTurn = new AboutTurn(robot, bDriveForward);
		
		robot.addDriveForwardBehavior(bDriveForward);
		
		Behavior[] bArray = {
				/*bDriveForward,*/ bDetectColor,
				bTurnLeft, bTurnRight, bAboutTurn, //Ordre des priorités négligeable entre ces 3 comportements
				bStopExploration, bSaveBattery, bShutDown
			}; //Du moins prioritaire au plus prioritaire

		//Comportement DetectColor en arrière plan
		//bDetectColor.action();
		//new DetectColorThread(cs, robot).start();
		
		//Arbitrator pour coordonner les comportements
		Arbitrator arby = new Arbitrator(bArray);
		try{
			new Thread(){
			    public void run() {
			    	//Attend que le capteur soit bien actif
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					robot.displayMap();
					/* Exploration */
					robot.explorer();
					/* Voyager vers une ville direction si exploration déjà faite */
					//robot.letsGoToTheCity();
			    }
			}.start();
			arby.start();
		}catch(Exception e){
			e.printStackTrace();
			Button.UP.waitForPressAndRelease();
			bShutDown.action();
		}
	}

}
