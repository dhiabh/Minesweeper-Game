package DeMineurGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Random;



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class DeMineur extends JFrame{
	
	int nbCase = 12;
	int largeur = 50;
	int bombsNumber = 15;
	int situation = 0;
	int x=0,y=0;
	int occupiedBox = bombsNumber;
	ArrayList<Cellule> bombs;
	JPanel panneau;
	boolean[][] exist = new boolean [nbCase][nbCase] ;
	boolean letPress = true;
	ArrayList<ArrayList<Integer>> rightClickedBomb ;
	
	
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
				
				case 0: {                       // initial case
					
					for(Cellule c : bombs)
					{
						dessinerBomb(c,g);
					}
					
					dessinerGrille(g);
					break;
				}
 
				case 1: {                       // if the user pressed a bomb
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
					
					
					g.setColor(Color.red);
					g.setFont(new Font("Roboto Mono", 1, 50));
					g.drawString("Game Over", 170, 300);
				
					break;
				}
				
				case 2: {								// if the user pressed an empty box 
					dessinerGrille(g);
					
						if(!exist[x][y])
						{
							if(numberCalculator(x, y) == 0)
							{
								printAdjacentsNumbers(x, y, g);
								
							}else{
								exist[x][y] = true;
								occupiedBox++;
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
						
						
						if(occupiedBox == nbCase*nbCase)
						{
							g.setColor(Color.red);
							g.setFont(new Font("Roboto Mono", 1, 50));
							g.drawString("You won!", 170, 300);
							letPress = false;
						}
						
						
					
					break;
				}
				
				case 3: {                 // if the user right click a bomb
					
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
					
					if(rightClickedBomb.size() == bombsNumber)
					{
						g.setColor(Color.red);
						g.setFont(new Font("Roboto Mono", 1, 50));
						g.drawString("You won!", 170, 300);
						letPress = false;
					}
					
				}

				}
				
//				System.out.println(occupiedBox);
//				System.out.println(situation);


			}
		};
		
		
		
		
		
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mousePressed(e);
				
				x=(e.getX()-6)/largeur;
				y=(e.getY()-30)/largeur;
				
				if(letPress)
				{
					if(SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1)
					{
						for(Cellule b : bombs)
						{
							if( ( ((e.getX()-6) > b.x*largeur) && ((e.getX()-6) < (b.x*largeur + largeur) ) ) &&
									( ((e.getY()-30) > b.y*largeur) && ((e.getY()-30)  < (b.y*largeur + largeur)) ))
							{

								ArrayList<Integer> bombClicked = new ArrayList<Integer>();
								bombClicked.add(x);
								bombClicked.add(y);
								rightClickedBomb.add(bombClicked);
								situation = 3;
								repaint();
								
								
							}
						}
					}else {

						for(Cellule b : bombs)
						{
							if( ( ((e.getX()-6) > b.x*largeur) && ((e.getX()-6) < (b.x*largeur + largeur) ) ) &&
									( ((e.getY()-30) > b.y*largeur) && ((e.getY()-30)  < (b.y*largeur + largeur)) ))
							{

								situation = 1;
								repaint();
								letPress = false;
								return;

							}else {

								situation = 2;
								repaint();
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
	
	
	
	void dessinerGrille(Graphics g)
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
	
	void dessinerBomb(Cellule c,Graphics g)
	{
			exist[c.x][c.y] = true;
			g.setColor(c.couleur);
			g.fillOval(c.x*largeur +5, c.y*largeur + 5, largeur -10, largeur - 10);
		
	}
	
	
	 //print all the numbers
	void printNumbers(int i,int j,Graphics g)
	{
		int caseNumber;
				boolean flag = true;
				for(Cellule c : bombs)
				{
					if(i == c.x && j == c.y)
						flag = false;
				}
				if(!flag)
					return;
				caseNumber = numberCalculator(i,j);
				g.setColor(Color.black);
				g.setFont(new Font("Roboto Mono", 1, 30));
				g.drawString(String.valueOf(caseNumber), i*largeur + 15, j*largeur + 35);

	}
	
	
	
	// calculate the number to put in the case
	int numberCalculator(int x, int y)
	{
		int caseNumber = 0;
		

		

		for(int c = x-1; c < x+2; c++)
		{
			if((c < 0) || (c > nbCase - 1))
				continue;
			for(int a = y - 1; a < y + 2; a++)
			{
				if( (a < 0) || (a > nbCase - 1) || ( (c == x) && (y == a)))
					continue;

				for(int k = 0; k < bombs.size(); k++)
				{
					if( (c == bombs.get(k).x) && (a == bombs.get(k).y) )
						caseNumber ++;	
				}
			}
		}

		return caseNumber;

	}

	
	void printAdjacentsNumbers(int x, int y,Graphics g)
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
		DeMineur d = new DeMineur();

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
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		public Color getCouleur() {
			return couleur;
		}
		public void setCouleur(Color couleur) {
			this.couleur = couleur;
		}
	}

}
