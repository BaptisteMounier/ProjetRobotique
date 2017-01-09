//Fichier : Robot.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

import java.util.HashMap;

import lejos.hardware.lcd.LCD;
import lejos.robotics.Color;

/** Représentation du Robot */
public class Robot {

	/** Tableau bidimensionnel représentant la carte (grille) */
	private int[][] map;
	/** Position de départ en abscisses */
	private int posX;
	/** Position de départ en ordonnées */
	private int posY;
	/** Direction d'avancement */
	private int direction;

	/** Déplacement décroissant en Y */
	public final static int VERS_LE_HAUT = 0;
	/** Déplacement croissant en X */
	public final static int VERS_LA_DROITE = 1;
	/** Déplacement croissant en Y */
	public final static int VERS_LE_BAS = 2;
	/** Déplacement décroissant en X */
	public final static int VERS_LA_GAUCHE = 3;
	
	/** Changement d'orientation par rotation droite */
	public final static int ROTATION_DROITE = 1;
	/** Changement d'orientation par rotation gauche */
	public final static int ROTATION_GAUCHE = -1;
	/** Changement d'orientation par demi-tour */
	public final static int DEMI_TOUR = 2;
	
	/** Correspondance des couleurs avec l'environnement */
	private static HashMap<Integer,String> environnement;

	/** Constructeur principal du robot
	 * @param nbLigne Nombre de lignes
	 * @param nbColonne Nombre de colonnes
	 * @param x Position de départ en abscisses
	 * @param y Position de départ en ordonnées */
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
		environnement.put(Color.BLUE, "Océan");
		environnement.put(Color.BROWN, "Montagne");
		environnement.put(Color.RED, "Ville");
		environnement.put(Color.WHITE, "Départ");
		environnement.put(Color.NONE, "X");
	}

	/** Met à jour la carte sur la position du robot
	 * @param color Couleur detectée et à mettre dans la carte */
	public void updateMap(int color){
		map[posY][posX] = color;
	}

	/** Met à jour la position du robot en fonction de sa direction */
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
	
	/** Mettre à jour la direction du robot en fonction d'une rotation
	 * @param rotation Rotation effectuée entraînant la modification de la direction : ROTATION_DROITE | ROTATION_GAUCHE */
	public void updateDirection(int rotation){
		direction = (direction + rotation) % 4;
		if(direction < 0) direction = 3;		//Car modulo conserve les valeurs négatives
	}
	/** Test si la case sur laquelle se trouve le robot a déjà été explorée
	 * @return Vrai si la case possède une couleur */
	public boolean isAlreadyExplored(){
		return (map[posY][posX] != Color.NONE);
	}

	/** Test si le robot est en dehors de la carte (physique sur la dernière case)
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
}
