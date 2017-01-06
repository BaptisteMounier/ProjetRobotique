//Fichier : Robot.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

import java.util.HashMap;

import lejos.hardware.lcd.LCD;
import lejos.robotics.Color;

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
	public final static int VERS_LE_HAUT = 1;
	/** D�placement croissant en X */
	public final static int VERS_LA_DROITE = 2;
	/** D�placement croissant en Y */
	public final static int VERS_LE_BAS = 3;
	/** D�placement d�croissant en X */
	public final static int VERS_LA_GAUCHE = 4;
	
	/** Correspondance des couleurs avec l'environnement */
	private static HashMap<Integer,String> environnement;
	
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
		environnement.put(Color.NONE, "X");
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
	
	/** Test si la case sur laquelle se trouve le robot a d�j� �t� explor�e
	 * @return Vrai si la case poss�de une couleur */
	public boolean isAlreadyExplored(){
		return (map[posY][posX] != Color.NONE);
	}
	
	/** Test si le robot est en dehors de la carte (physique sur la derni�re case)
	 * @return Vrai si le robot est sorti */
	public boolean isOutOfMap(){
		return (posY < 0 || posX < 0 || posY > map.length || posX > map[0].length);
	}
	
	/** Affiche la carte sur l'afficheur LCD */
	public void displayMap(){
		LCD.clear();
		String toDisplay;
		
		for(int l=0; l<map.length; l++){
			toDisplay = "";
			for(int c=0; c<map[0].length; c++){
				toDisplay += (environnement.get(map[l][c])).charAt(0) + " | ";
			}
			toDisplay = toDisplay.substring(0, toDisplay.length()-2);
			LCD.drawString(toDisplay, 0, l);
		}
		
		LCD.refresh();
	}
}
