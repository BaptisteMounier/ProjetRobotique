//Fichier : Exploration.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

package robot;

/** Thread d'exploration de la carte par le robot */
public class Exploration extends Thread{
	
	/** Le robot */
	private Robot robot;
	
	/** Constructeur principal
	 * @param robot Le robot */
	public Exploration(Robot robot){
		this.robot = robot;
	}
	
	@Override
	public void run(){
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

}
