import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Cellule
{
	//Constructor
	
	public Cellule(int x,int y)
	{
		this.x = x;
		this.y = y;
		this.rayon = 0;
		
		Random random = new Random();
		this.couleur = new Color((int) (random.nextDouble() * 255),(int) (random.nextDouble() * 255),(int) (random.nextDouble() * 255));
		
		this.actif = true;
		
		this.parents = new Cellule[2];
		this.parents[0] = null;
		this.parents[1] = null;
	}
	
	public Cellule(int x,int y,int rouge,int vert, int bleu)
	{
		this(x,y);
		
		this.couleur = new Color(rouge,vert,bleu);
	}
	
	public Cellule(Cellule parent1,Cellule parent2)
	{
		this.x = (parent1.x + parent2.x) / 2;
		this.y = (parent1.y + parent2.y) / 2;
		this.rayon = 0;
		
		Random random = new Random();
		
		int rougeRandom = (int) (random.nextDouble() * 2 * MUTATION - MUTATION);
		int rouge = (parent1.couleur.getRed() + parent2.couleur.getRed()) / 2 + rougeRandom;
		if (rouge < 0)
		{
			rouge = 0;
		}
		else if (rouge > 255)
		{
			rouge = 255;
		}
		
		int vertRandom = (int) (random.nextDouble() * 2 * MUTATION - MUTATION);
		int vert = (parent1.couleur.getGreen() + parent2.couleur.getGreen()) / 2 + vertRandom;
		if (vert < 0)
		{
			vert = 0;
		}
		else if (vert > 255)
		{
			vert = 255;
		}
		
		int bleuRandom = (int) (random.nextDouble() * 2 * MUTATION - MUTATION);
		int bleu = (parent1.couleur.getBlue() + parent2.couleur.getBlue()) / 2 + bleuRandom;
		if (bleu < 0)
		{
			bleu = 0;
		}
		else if (bleu > 255)
		{
			bleu = 255;
		}
		
		this.couleur = new Color(rouge,vert,bleu);
		
		this.actif = true;
		
		this.parents = new Cellule[2];
		this.parents[0] = parent1;
		this.parents[1] = parent2;
	}
	
	//Public methods
	
	public int distance(int posX,int posY)
	{
		return (int) Math.sqrt((posX - x) * (posX - x) + (posY - y) * (posY - y)) - rayon;
	}
	
	public int distance(Cellule cellule)
	{
		return distance(cellule.x,cellule.y) - cellule.rayon;
	}
	
	public void dessiner(Graphics2D g2d)
	{
		g2d.setColor(couleur);
		g2d.fillOval(x - rayon,y - rayon,2 * rayon,2 * rayon);
		g2d.setColor(Color.BLACK);
		g2d.fillOval(x - rayon / 3,y - rayon / 3,(int) (2. / 3 * rayon),(int) (2. / 3 * rayon));
	}
	
	public void grandit()
	{
		if (actif)
		{
			Random random = new Random();
			rayon += (int) (random.nextDouble() * 5 - 2);
			if (rayon < 0)
			{
				rayon = 0;
			}
			
			else if (rayon > RAYON_MAX)
			{
				actif = false;
			}
		}
		else
		{
			claircir();
		}
	}
	
	public void claircir()
	{
		int rouge = couleur.getRed() + (couleur.getRed() + 255) / 20;
		int vert = couleur.getGreen() + (couleur.getGreen() + 255) / 20;
		int bleu = couleur.getBlue() + (couleur.getBlue() + 255) / 20;
		
		if (rouge > 255)
		{
			rouge = 255;
		}
		
		if (vert > 255)
		{
			vert = 255;
		}
		
		if (bleu > 255)
		{
			bleu = 255;
		}
		
		couleur = new Color(rouge,vert,bleu);
	}
	
	public void sEloigner(Cellule cellule)
	{
		int trajet = -distance(cellule);
		
		int trajetX;
		int trajetY;
		
		if (cellule.x == x)
		{
			trajetX = 0;
			trajetY = trajet;
		}
		else
		{
			double angle = Math.atan((cellule.y - y) / (cellule.x - x));
			trajetX = (int) (Math.cos(angle) * trajet);
			trajetY = (int) (Math.sin(angle) * trajet);
		}
		
		if (cellule.x > x)
		{
			x -= trajetX;
		}
		else
		{
			x += trajetX;
		}
		
		if (cellule.y > y)
		{
			y -= trajetY;
		}
		else
		{
			y += trajetY;
		}
	}
	
	public void desactiver()
	{
		actif = false;
	}
	
	//Get
	
	public int getRayon()
	{
		return rayon;
	}
	
	public boolean isActif()
	{
		return actif;
	}
	
	public boolean isBlanc()
	{
		return couleur.getRed() > 250 && couleur.getGreen() > 250 && couleur.getBlue() > 250;
	}
	
	public boolean isSoeur(Cellule cellule)
	{
		if (parents[0] == null || this == cellule)
		{
			return false;
		}
		else if ((parents[0] == cellule.parents[0] && parents[1] == cellule.parents[1]) || (parents[0] == cellule.parents[1] && parents[1] == cellule.parents[0]))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//Public static final attributes
	
	public static final int RAYON_MIN = 50;
	public static final int RAYON_MAX = 500;
	public static final int MUTATION = 30;
	
	//Private attributes
	
	private int x;
	private int y;
	private int rayon;
	private Color couleur;
	private boolean actif;
	private Cellule[] parents;
}
