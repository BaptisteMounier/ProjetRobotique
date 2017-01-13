//Fichier : Robot.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

import java.util.HashMap;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.Color;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

/** Repr�sentation du Robot */
public class Robot {

	/** Tableau bidimensionnel repr�sentant la carte (grille) */
	private int[][] map;
	/** Position de d�part en abscisses */
	private int posX;
	/** Position de d�part en ordonn�es */
	private int posY;
	/** Direction d'avancement */
	private int direction;

	/** D�placement d�croissant en Y */
	public final static int VERS_LE_HAUT = 0;
	/** D�placement croissant en X */
	public final static int VERS_LA_DROITE = 1;
	/** D�placement croissant en Y */
	public final static int VERS_LE_BAS = 2;
	/** D�placement d�croissant en X */
	public final static int VERS_LA_GAUCHE = 3;
	
	/** Changement d'orientation par rotation droite */
	public final static int ROTATION_DROITE = 1;
	/** Changement d'orientation par rotation gauche */
	public final static int ROTATION_GAUCHE = -1;
	/** Changement d'orientation par demi-tour */
	public final static int DEMI_TOUR = 2;
	
	/** Correspondance des couleurs avec l'environnement */
	private static HashMap<Integer,String> environnement;
	
	/** Vrai si le robot doit activer le comportement AboutTurn */
	private boolean demandeDemiTour = false;
	/** Vrai si le robot doit activer le comportement TurnRight */
	private boolean demandeQuartDeTourDroit = false;
	/** Vrai si le robot doit activer le comportement TurnLeft */
	private boolean demandeQuartDeTourGauche = false;
	
	/** Comportement d'avancement */
	private Behavior bDriveForward;
	/** Pilot permettant d'effectuer des trajectoires */
	private DifferentialPilot pilot;

	/** Constructeur principal du robot
	 * @param nbLigne Nombre de lignes
	 * @param nbColonne Nombre de colonnes
	 * @param x Position de d�part en abscisses
	 * @param y Position de d�part en ordonn�es */
	public Robot(int nbLigne, int nbColonne, int x, int y, int dir){
		posX = x;
		posY = y;
		direction = dir;
		map = new int[nbLigne][nbColonne];

		for(int l=0; l<nbLigne; l++){
			for(int c=0; c<nbColonne; c++){
				map[l][c] = Color.NONE;
			}
		}

		environnement = new HashMap<Integer,String>();
		environnement.put(Color.GREEN, "Prairie");
		environnement.put(Color.BLUE, "Oc�an");
		environnement.put(Color.BROWN, "Montagne");
		environnement.put(Color.RED, "Ville");
		environnement.put(Color.WHITE, "D�part");
		environnement.put(Color.NONE, "-");
		
		pilot = new DifferentialPilot(5.6, 12.0, Motor.B, Motor.C);
			//Diam�tre des roues, espace entre les roues, moteur gauche, moteur droit
		pilot.setTravelSpeed(5.);
	}

	/** Met � jour la carte sur la position du robot
	 * @param color Couleur detect�e et � mettre dans la carte */
	public void updateMap(int color){
		map[posY][posX] = color;
	}

	/** Met � jour la position du robot en fonction de sa direction */
	public void updatePos(){
		switch(direction){
			case VERS_LA_GAUCHE:
				posX -= 1;
				break;
	
			case VERS_LA_DROITE:
				posX += 1;
				break;
	
			case VERS_LE_HAUT:
				posY -= 1;
				break;
	
			case VERS_LE_BAS:
				posY += 1;
				break;
		}
	}
	
	/** Mettre � jour la direction du robot en fonction d'une rotation
	 * @param rotation Rotation effectu�e entra�nant la modification de la direction : ROTATION_DROITE | ROTATION_GAUCHE */
	public void updateDirection(int rotation){
		direction = (direction + rotation) % 4;
		if(direction < 0) direction = 3;		//Car modulo conserve les valeurs n�gatives
	}
	/** Test si la case sur laquelle se trouve le robot a d�j� �t� explor�e
	 * @return Vrai si la case poss�de une couleur */
	public boolean isAlreadyExplored(){
		return (map[posY][posX] != Color.NONE);
	}

	/** Test si le robot est en dehors de la carte (physique sur la derni�re case)
	 * @return Vrai si le robot est sorti */
	public boolean isOutOfMap(){
		return (posY < 0 || posX < 0 || posY > map.length-1 || posX > map[0].length-1);
	}

	/** Affiche la carte sur l'afficheur LCD */
	public void displayMap(){
		LCD.clear();
		String toDisplay;

		for(int l=0; l<map.length; l++){
			toDisplay = "";
			for(int c=0; c<map[l].length; c++){
				toDisplay += (environnement.get(map[l][c])).charAt(0) + " | ";
			}
			toDisplay = toDisplay.substring(0, toDisplay.length()-2);
			LCD.drawString(toDisplay, 0, l);
		}
		LCD.refresh();
	}

	/** Acc�de � la largeur de l'environnement
	 * @return Largeur de la carte */
	public int getMapLength() {
		return map[0].length;
	}

	/** Acc�de � la hauteur de l'environnement
	 * @return Hauteur de la carte */
	public int getMapHeight() {
		return map.length;
	}
	
	public void explorer(){
		/* On suppose que le robot d�marre � sa position de d�part (0, 6) */
		
		int mapHeight = this.getMapHeight();
		
		for(int c=0; c<this.getMapLength(); c++){
			if(c%2 == 0){ //Vers le haut
				//Parcours de la colonne puis d�calage sur la suivante
				this.voyager(c, 0);
				this.voyager(c+1, 0);
			} else { //Vers le bas
				//Parcours de la colonne puis d�calage sur la suivante
				this.voyager(c, mapHeight-1);
				this.voyager(c+1, mapHeight-1);
			}
		}
		/*this.voyager(0, 0);
		this.voyager(4, 0);*/
	}
	
	/** Fait se d�placer le robot de sa position actuelle � la position demand�e
	 * @param xDest Abscisse de la position de destination
	 * @param yDest Ordonn�e de la position de destination */
	public void voyager(int xDest, int yDest){
		//System.out.println(xDest + " " + yDest + " " + posX + " " + posY + "\n" + direction);
		//Recherche de la position de la destination par rapport � la position actuelle
		int xDir = -1;
		int yDir = -1;
		
		if(xDest - posX < 0){
			xDir = VERS_LA_GAUCHE;
		} else if(xDest - posX > 0){
			xDir = VERS_LA_DROITE;
		}
		
		if(yDest - posY < 0){
			yDir = VERS_LE_HAUT;
		} else if(yDest - posY > 0){
			yDir = VERS_LE_BAS;
		}
		
		switch(direction){
			case VERS_LE_HAUT:
			case VERS_LE_BAS:
				if(posY != yDest){
					if(yDir != direction){
						demiTour();
					}
					
					//Avancer jusqu'� posY = yDest
					bDriveForward.action();
					while(posY != yDest){
						//TODO trouver un autre moyen de savoir quand arr�ter le robot
					}
					bDriveForward.suppress();
					
					avancer(4.);
					quartDeTourDroit();
				}
				break;
			
			case VERS_LA_DROITE:
			case VERS_LA_GAUCHE:
				if(posX != xDest){
					if(xDir != direction){
						demiTour();
					}
					
					//Avancer jusqu'� posX = xDest
					bDriveForward.action();
					while(posX != xDest){
						//TODO trouver un autre moyen de savoir quand arr�ter le robot
					}
					bDriveForward.suppress();
					
					avancer(4.);
					quartDeTourDroit();
				}
				break;
				
				/* Quart de tour vers la droite si monter-descente
				Quart de tour vers la gauche si descendre-monter */
				//Mettre un bool�en en param�tre de la fonction ?
		}
	}
	
	/** Fait avancer le robot du nombre de cm donn� param�tre
	 * @param cm Longueur sur laquelle faire avancer le robot */
	public void avancer(double cm){
		pilot.travel(cm);
	}
	
	/** Faire faire au robot un demi-tour */
	public void demiTour(){
		//Faire demi-tour
		demandeDemiTour = true;
		//Attend fin du demi-tour
		try {
			Thread.sleep(14000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** Accesseur de demandeDemiTour
	 * @return Vrai si la robot doit faire demi-tour */
	public boolean isRequestingForAboutTurn() {
		return demandeDemiTour;
	}
	
	/** Arr�te la demande de demi-tour */
	public void stopDemandeDemiTour(){
		demandeDemiTour = false;
	}
	
	/** Faire faire au robot un quart-de-tour � droite */
	public void quartDeTourDroit(){
		//Faire quart-tour
		demandeQuartDeTourDroit = true;
		//Attend fin du demi-tour
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** Accesseur de demandeQuartDeTourDroit
	 * @return Vrai si la robot doit faire un quart-de-tour � droite */
	public boolean isRequestingForTurnRight() {
		return demandeQuartDeTourDroit;
	}
	
	/** Arr�te la demande de quart-de-tour � droite */
	public void stopDemandeQuartDeTourDroit(){
		demandeQuartDeTourDroit = false;
	}
	
	/** Faire faire au robot un quart-de-tour � gauche */
	public void quartDeTourGauche(){
		//Faire quart-tour
		demandeQuartDeTourGauche = true;
		//Attend fin du demi-tour
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** Accesseur de demandeQuartDeTourDroit
	 * @return Vrai si la robot doit faire un quart-de-tour � droite */
	public boolean isRequestingForTurnLeft() {
		return demandeQuartDeTourGauche;
	}
	
	/** Arr�te la demande de quart-de-tour � droite */
	public void stopDemandeQuartDeTourGauche(){
		demandeQuartDeTourGauche = false;
	}

	/** Ajoute un comportement d'avancement au robot */
	public void addDriveForwardBehavior(Behavior bDriveForward) {
		this.bDriveForward = bDriveForward;
	}
}
