//Fichier : ReceiveRequest.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

package robot;

import lejos.robotics.subsumption.Behavior;

/** Comportement de r�ception de demande d'aide */
public class ReceiveRequest implements Behavior{

	/** Le robot */
	private Robot robot;
	/** Position en abscisses du robot � secourir */
	private int xTarget;
	/** Position en ordonn�es du robot � secourir */
	private int yTarget;

	/** Constructeur principal
	 * @param robot Le robot*/
	public ReceiveRequest(Robot robot){
		this.robot = robot;
		xTarget = -1;
		yTarget = -1;
	}

	/** Met � jours la position du robot � secourir en allant chercher les valeurs dans le robot */
	public void updateTargetPosition(){
		xTarget = robot.getXTarget();
		yTarget = robot.getYTarget();;
	}

	@Override
	public boolean takeControl() {
		// Prend le contr�le lorsqu'un message est re�u
		return robot.isDemandeAideRecue();
	}

	@Override
	public void action() {
		//  R�initialise l'attente de message
		robot.setDemandeAideRecue(false);
		// Met � jours la position du robot � secourir
		updateTargetPosition();
		// Cr�e un Thread pour le rejoindre (pour �viter de mettre en attente tous les autres comportements)
		new Thread(){
			public void run() {
				robot.voyager(xTarget, yTarget);
			}
		}.start();
	}

	@Override
	public void suppress() {}

}
