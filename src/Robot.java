//Fichier : Robot.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

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
	public final static int VERS_LE_HAUT = 1;
	/** Déplacement croissant en X */
	public final static int VERS_LA_DROITE = 2;
	/** Déplacement croissant en Y */
	public final static int VERS_LE_BAS = 3;
	/** Déplacement décroissant en X */
	public final static int VERS_LA_GAUCHE = 4;
	
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
	}
}
