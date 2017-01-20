//Fichier : Main.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

package robot;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.BaseSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/** Projet d'exploration robotique (côté robot récepteur de message) */
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
		Behavior bShutDown = new ShutDown(sensors/*, btc, dis*/); //S'arrête lors de l'appuie sur un bouton
		Behavior bDriveForward = new DriveForward(); //Avancer
		Behavior bDetectColor = new DetectColor(cs, robot);
		Behavior bStopExploration = new StopExploration(robot, (DetectColor) bDetectColor);
		Behavior bTurnRight = new TurnRight(robot, bDriveForward);
		Behavior bTurnLeft = new TurnLeft(robot, bDriveForward);
		Behavior bAboutTurn = new AboutTurn(robot, bDriveForward);
		Behavior bReceiveRequest = new ReceiveRequest(robot);

		robot.addDriveForwardBehavior(bDriveForward);

		Behavior[] bArray = {
				/*bDriveForward,*/ bDetectColor,
				bTurnLeft, bTurnRight, bAboutTurn, //Ordre des priorités négligeable entre ces 3 comportements
				bStopExploration, bReceiveRequest, bSaveBattery, bShutDown
		}; //Du moins prioritaire au plus prioritaire

		//Arbitrator pour coordonner les comportements
		Arbitrator arby = new Arbitrator(bArray);
		try{
			
			// Création et lancement du Thread d'exploration
			final Exploration exploration = new Exploration(robot);
			exploration.start();
			
			// Création et lancement du Thread de la connexion BlueTooth
			new Thread(){
				public void run() {
					//Attend que le capteur soit bien actif
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					//Création de la connexion
					BTConnector bt= new BTConnector();
					NXTConnection btc = bt.waitForConnection(100000,NXTConnection.PACKET);
					DataInputStream dis = null;
					if(btc == null){ // Regarde l'existence de la connexion
						System.out.println("Pas de connexion !");
					}else{
						InputStream is= btc.openInputStream();
						dis = new DataInputStream(is);
					}
					try {
						// Reçoit les coordonnées et stop l'exploration
						int x = dis.read();
						int y = dis.read();
						exploration.interrupt();
						robot.updateTargetPosition(x,y);
						robot.setDemandeAideRecue(true);
					} catch (IOException e) {
						e.printStackTrace();
					} finally{
						try {
							dis.close();
							btc.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			
			// Lancement de l'Arbirtator
			arby.start();
			
		}catch(Exception e){ // Attrappe d'exceptions de secours qui permet d'éteindre proprement le programme (fermeture de ports, etc)
			e.printStackTrace();
			Button.UP.waitForPressAndRelease();
			bShutDown.action();
		}
	}

}
