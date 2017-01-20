//Fichier : RequestHelp.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

package emetteur;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import lejos.hardware.Button;
import lejos.remote.nxt.BTConnection;
import lejos.robotics.subsumption.Behavior;

import robot.*;

/** Comportement de demande d'aide */
public class RequestHelp implements Behavior{

	/** Connexion BlueTooth */
	private BTConnection btc;
	/** Robot */
	private Robot robot;

	/** Constructeur principal
	 * @param robot Le robot
	 * @param btc La connexion BlueTooth */
	public RequestHelp(Robot robot, BTConnection btc){
		this.btc = btc;
		this.robot = robot;
	}

	@Override
	public boolean takeControl() {
		// Demande l'aide si on appuie sur le boutton gauche
		return Button.LEFT.isDown();
	}

	@Override
	public void action() {

		// Creer un flux de données
		OutputStream os = btc.openOutputStream();
		DataOutputStream dos = new DataOutputStream(os);

		try {

			// Envoi du message
			dos.write(robot.getPosX()); // écrit une valeur int dans le flux
			dos.write(robot.getPosY()); // écrit une valeur int dans le flux
			dos.flush(); // force l’envoi
			System.out.println("envoyé");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				dos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void suppress() {}

}
