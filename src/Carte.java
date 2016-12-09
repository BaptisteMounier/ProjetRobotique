//Fichier : Carte.java
//Auteurs : Gutierrez Cyrian - Magnin Gauthier - Mounier Baptiste
//Contexte : TSCR - Projet robotique - M1 SCA

import lejos.robotics.Color;

/** Repr�sentation de la carte */
public class Carte {

	/** Tableau bidimensionnel repr�sentant la carte */
	private int[][] map;
	
	/** Constructeur principal d'une carte repr�sent�e par une grille
	 * @param nbLigne Nombre de lignes
	 * @param nbColonne Nombre de colonnes */
	public Carte(int nbLigne, int nbColonne){
		map = new int[nbLigne][nbColonne];
		
		for(int l=0; l<nbLigne; l++){
			for(int c=0; c<nbColonne; c++){
				map[l][c] = Color.NONE;
			}
		}
	}
}
