//Fichier : ReceiveRequest.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

package robot;

import lejos.robotics.subsumption.Behavior;

/** Comportement de réception de demande d'aide */
public class ReceiveRequest implements Behavior{

	/** Le robot */
	private Robot robot;
	/** Position en abscisses du robot à secourir */
	private int xTarget;
	/** Position en ordonnées du robot à secourir */
	private int yTarget;

	/** Constructeur principal
	 * @param robot Le robot*/
	public ReceiveRequest(Robot robot){
		this.robot = robot;
		xTarget = -1;
		yTarget = -1;
	}

	/** Met à jours la position du robot à secourir en allant chercher les valeurs dans le robot */
	public void updateTargetPosition(){
		xTarget = robot.getXTarget();
		yTarget = robot.getYTarget();;
	}

	@Override
	public boolean takeControl() {
		// Prend le contrôle lorsqu'un message est reçu
		return robot.isDemandeAideRecue();
	}

	@Override
	public void action() {
		//  Réinitialise l'attente de message
		robot.setDemandeAideRecue(false);
		// Met à jours la position du robot à secourir
		updateTargetPosition();
		// Crée un Thread pour le rejoindre (pour éviter de mettre en attente tous les autres comportements)
		new Thread(){
			public void run() {
				robot.voyager(xTarget, yTarget);
			}
		}.start();
	}

	@Override
	public void suppress() {}

}
