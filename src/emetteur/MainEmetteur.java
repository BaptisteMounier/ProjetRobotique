//Fichier : MainEmetteur.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

package emetteur;

import lejos.hardware.Button;

import lejos.hardware.sensor.BaseSensor;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

import robot.*;

/** Projet d'exploration robotique (côté robot émetteur de message) */
public class MainEmetteur {

	/** Point d'entrée du programme */
	public static void main(String[] args) {

		Robot robot = new Robot(7, 5, 3, 1, Robot.VERS_LE_BAS);

		System.out.println("Prêt");
		
		//Attente d'un appui sur le bouton droit pour démarrer
		Button.RIGHT.waitForPressAndRelease();

		// Etablir la connexion
		BTConnector bt= new BTConnector();
		BTConnection btc= bt.connect("00:16:53:43:AD:EE", NXTConnection.PACKET); // Adresse du robot Ed4 : 00:16:53:43:AD:EE

		// Regarde l'existence de la connexion
		if(btc == null){
			System.out.println("Pas de connexion !");
		}

		//Ouverture des ports pour l'utilisation des capteurs
		BaseSensor[] sensors = {};

		//Création des comportements
		Behavior bSaveBattery = new SaveBattery(sensors); //S'arrête quand la batterie est faible
		Behavior bShutDown = new ShutDown(sensors, btc); //S'arrête lors de l'appuie sur un bouton
		Behavior bRequestHelp = new RequestHelp(robot, btc); //Demander de l'aide

		Behavior[] bArray = {bRequestHelp, bSaveBattery, bShutDown}; //Du moins prioritaire au plus prioritaire

		//Arbitrator pour coordonner les comportements
		Arbitrator arby = new Arbitrator(bArray);
		
		try{
			arby.start();
		}catch(Exception e){
			e.printStackTrace();
			Button.UP.waitForPressAndRelease();
			bShutDown.action();
		}

	}

}
