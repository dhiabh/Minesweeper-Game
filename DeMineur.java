/*
 * Règles du jeu:
 * Pour gagner: 1) Vous devez cliquez sur tous les cases qui ne contiennent pas des bombes
 *              2) Vous devez deviner les positions des bombes en cliquant avec le click droit de la souris
 *                 sur la bombe et cette bombe va apparaitre en bleu
 * Si vous cliquez avec le click gauche sur une bombe, tous les bombes vont apparaitre en noir et vous perdez
 * le numero de chaque case ne contenat pas de bombe indique le nombre de bombes situées à sa coté
 */

package DeMineurGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Random;



import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class DeMineur extends JFrame{

	int nbCase = 12;                   // le nombre de lignes et de colonnes
	int largeur = 50;                  // la largeur d'une case en pixel
	int bombsNumber = 10;			   // le nombre des bombes
	int situation = 0;				   // situation du jeu
	int x=0,y=0;					   // les coordonnées du click de la souris
	int occupiedBox = bombsNumber;	   // le nombre des cases remplies (initialisé au nombre des bombes)
	ArrayList<Cellule> bombs;		   // liste de la classe cellule qui contient les bombes
	JPanel panneau; 				   // le paneau de la fenetre
	boolean[][] exist = new boolean [nbCase][nbCase] ;      // tableau 2D contenant la situation de chaque case: true si la case est remplie / false si elle n'est pas remplie
	boolean letPress = true;		   // true: on peut encore utiliser la souris / false: jeu terminé, on ne peut plus utiliser la souris
	ArrayList<ArrayList<Integer>> rightClickedBomb ;  // liste 2D contenant les coordonnées des bombes devinées par click droit

	// Génarateur des bombes dans des postions aléatoires

	void bombsGenerator()
	{
		bombs = new ArrayList<Cellule>();

		ArrayList<Integer> randomX = new ArrayList<Integer>();
		ArrayList<Integer> randomY = new ArrayList<Integer>();
		Random rand = new Random();

		Integer x = rand.nextInt(nbCase);
		Integer y = rand.nextInt(nbCase);
		randomX.add(x);
		randomY.add(y);
		bombs.add(new Cellule(x, y, Color.black));
		exist[x][y] = true;


		int i =1;
		boolean flag;
		do {
			flag = true;
			x=rand.nextInt(nbCase);
			y=rand.nextInt(nbCase);
			for(int j = 0; j < randomX.size(); j++)
			{
				if(x == randomX.get(j))
				{
					if(y == randomY.get(j))
					{
						flag = false;
						break;
					}
				}
			}

			if(flag == true)
			{
				randomX.add(x);; 
				randomY.add(y);;
				bombs.add(new Cellule(x, y, Color.black));
				exist[x][y] = true;


				i++;

			}
		} while( (flag == false) || (i < bombsNumber));

		for(Cellule b : bombs)
			System.out.println(b.x + "    "+b.y);

	}

	// constructeur

	public DeMineur() {


		this.setTitle("DéMineur Game");
		this.setSize(nbCase*largeur +15 ,nbCase*largeur +38);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);

		for(int i = 0; i< nbCase; i++)
		{
			for(int j = 0;j < nbCase ; j++)
				exist[i][j] = false;
		}

		rightClickedBomb = new ArrayList<ArrayList<Integer>>();


		bombsGenerator();

		panneau = new JPanel() {

			@Override
			public void paint(Graphics g) {
				super.paint(g);	

				switch (situation) {

				case 0: {                       // cas initial: tout est caché

					for(Cellule c : bombs)
					{
						dessinerBomb(c,g);
					}

					dessinerGrille(g);
					break;
				}

				case 1: {                       // si l'utilisateur clique avec click gauche sur une bombe
					dessinerGrille(g);
					for(int i = 0; i < nbCase; i++)
					{
						for(int j = 0; j< nbCase; j++ )
						{
							for(Cellule b : bombs)
							{
								if(exist[i][j] && i != b.x && j != b.y)
								{

									printNumbers(i, j, g);
								}
							}

						}
					}

					for(Cellule c : bombs)
						dessinerBomb(c,g);

					break;
				}

				case 2: {					// si l'utilisateur clique sur une case vide (ne conient pas de bombe)
					dessinerGrille(g);

					if(!exist[x][y])
					{
						if(numberCalculator(x, y) == 0)    // si cette case vaut 0, on affiche les numéros des cases adjacentes
						{
							printAdjacentsNumbers(x, y, g);

						}else{
							printNumbers(x,y,g);
						}
					}

					for(int i = 0; i < nbCase; i++)
					{
						for(int j = 0; j< nbCase; j++ )
						{
							for(Cellule b : bombs)
							{
								if(exist[i][j] && i != b.x && j != b.y)
								{
									printNumbers(i, j, g);
								}
							}

						}
					}

					for(ArrayList<Integer> rb : rightClickedBomb)
					{
						Cellule c = new Cellule(rb.get(0), rb.get(1), Color.blue);
						dessinerBomb(c,g);
					}

					break;
				}

				case 3: {                 // si l'utilisateur clique avec click droit sur une bombe

					dessinerGrille(g);
					for(int i = 0; i < nbCase; i++)
					{
						for(int j = 0; j< nbCase; j++ )
						{
							for(Cellule b : bombs)
							{
								if(exist[i][j] && i != b.x && j != b.y)
								{

									printNumbers(i, j, g);
								}
							}

						}
					}

					for(ArrayList<Integer> rb : rightClickedBomb)
					{
						Cellule c = new Cellule(rb.get(0), rb.get(1), Color.blue);
						dessinerBomb(c,g);
					}


				}

				}

			}
		};



		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mousePressed(e);
				boolean flag = false;

				if(letPress)
				{

					x=(e.getX()-6)/largeur;       // -6 et -30 pour adapter le click de la souris
					y=(e.getY()-30)/largeur;

					if(SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1)   // click droit
					{
						for(Cellule b : bombs)
						{
							if( ( ((e.getX()-6) > b.x*largeur) && ((e.getX()-6) < (b.x*largeur + largeur) ) ) &&
									( ((e.getY()-30) > b.y*largeur) && ((e.getY()-30)  < (b.y*largeur + largeur)) ))
							{

								ArrayList<Integer> bombClicked = new ArrayList<Integer>();   // liste contenant les coordonnées d'une bombe
								bombClicked.add(x);
								bombClicked.add(y);
								rightClickedBomb.add(bombClicked);
								situation = 3;
								repaint();
								if(rightClickedBomb.size() == bombsNumber)
								{
									letPress = false;
									JOptionPane.showMessageDialog(null, "Congratulations! You Won!");
									break;
								}



							}
						}
					}else {         // click gauche

						for(Cellule b : bombs)
						{
							if( ( ((e.getX()-6) > b.x*largeur) && ((e.getX()-6) < (b.x*largeur + largeur) ) ) &&
									( ((e.getY()-30) > b.y*largeur) && ((e.getY()-30)  < (b.y*largeur + largeur)) ))
							{
								flag = true;     // si l'utilisateur a cliqué sur une bombe flag sera true
							}
						}
						
						if(flag)
						{
							situation = 1;
							repaint();
							letPress = false;
							JOptionPane.showMessageDialog(null, "You Lost!");


						}else {

							situation = 2;

							if(!exist[x][y] && numberCalculator(x, y) != 0) 
							{
								occupiedBox++;
								exist[x][y] = true;
							}

							repaint();
							if(occupiedBox == nbCase*nbCase)
							{
								letPress = false;
								JOptionPane.showMessageDialog(null, "Congratulations! You Won!");

							}

						}

					}


				}

			}
		});


		this.setContentPane(panneau);
		panneau.setBackground(new Color(0xDAFAB2));
		this.setVisible(true);

	}



	void dessinerGrille(Graphics g)      // dessin de la grille initiale
	{

		for(int i = 0; i < nbCase ; i++)
		{
			for(int j =0; j< nbCase ; j++)
			{
				if((i+j) % 2 == 0)
				{
					g.setColor(new Color(0xC9C0B1));
					g.fillRect(i*largeur, j*largeur, largeur, largeur);

				}else {
					g.setColor(panneau.getBackground());
					g.fillRect(i*largeur, j*largeur, largeur, largeur);

				}
			}
		}

	}

	void dessinerBomb(Cellule c,Graphics g)    // dessin de la bombe
	{
		exist[c.x][c.y] = true;
		g.setColor(c.couleur);
		g.fillOval(c.x*largeur +5, c.y*largeur + 5, largeur -10, largeur - 10);

	}

	
	void printNumbers(int i,int j,Graphics g)  // affichage des nombres
	{
		int caseNumber;
		for(Cellule c : bombs)
		{
			if(i == c.x && j == c.y)
				return;
		}

		caseNumber = numberCalculator(i,j);
		g.setColor(Color.black);
		g.setFont(new Font("Roboto Mono", 1, 30));
		g.drawString(String.valueOf(caseNumber), i*largeur + 15, j*largeur + 35);

	}


	int numberCalculator(int x, int y)      // calcul du numéro à mettre dans la case
	{
		int caseNumber = 0;

		for(int i = x-1; i < x+2; i++)
		{
			if((i < 0) || (i > nbCase - 1))
				continue;
			for(int j = y - 1; j < y + 2; j++)
			{
				if( (j < 0) || (j > nbCase - 1) || ( (i == x) && (y == j)))
					continue;

				for(int k = 0; k < bombs.size(); k++)
				{
					if( (i == bombs.get(k).x) && (j == bombs.get(k).y) )
						caseNumber ++;	
				}
			}
		}

		return caseNumber;

	}


	void printAdjacentsNumbers(int x, int y,Graphics g)  // affichage des numéros des cases adjacents si la case cliquée contient le numéro 0
	{
		printNumbers(x, y, g);

		if(x < 0 || x > nbCase - 1 || y < 0 || y > nbCase - 1) return;

		if(!exist[x][y])
			occupiedBox++;

		if( numberCalculator(x, y) == 0 && !exist[x][y])
		{
			exist[x][y] = true;

			printAdjacentsNumbers(x, y-1, g);
			printAdjacentsNumbers(x-1, y, g);
			printAdjacentsNumbers(x+1, y, g);
			printAdjacentsNumbers(x, y+1, g);

		}else {
			exist[x][y] = true;
			return;
		}


	}



	public static void main(String[] args) {
		new DeMineur();

	}



	class Cellule
	{
		int x,y;
		Color couleur;

		public Cellule(int x, int y, Color couleur) {
			super();
			this.x = x;
			this.y = y;
			this.couleur = couleur;
		}
	}

}
