import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AutomateCellulaire extends JPanel
{
	//Constructors
	
	public AutomateCellulaire(int largeur,int hauteur,JFrame parent)
	{
		this(NOMBRE_CELLULES_PAR_DEFAUT,largeur,hauteur,parent);
	}
	
	public AutomateCellulaire(int nbCellules,int largeur,int hauteur,JFrame parent)
	{
		this.largeur = largeur;
		this.hauteur = hauteur;
		
		this.nbCellules = nbCellules;
		
		cellules = new LinkedList<Cellule>();
		initCellules();
		
		this.parent = parent;
	}
	
	//Protected methods
	
	@Override protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		dessiner(g2d);
	}
	
	//Public methods
	
	public void ajouterCellule(int couleur)
	{
		Random random = new Random();
		int x = (int) (random.nextDouble() * (largeur - 2 * DISTANCE_MINIMALE) + DISTANCE_MINIMALE);
		int y = (int) (random.nextDouble() * (hauteur - 2 * DISTANCE_MINIMALE) + DISTANCE_MINIMALE);
		
		switch(couleur % 3)
		{
			case 0:
				cellules.add(new Cellule(x,y,255,0,0));
			break;
			
			case 1:
				cellules.add(new Cellule(x,y,0,255,0));
			break;
			
			case 2:
				cellules.add(new Cellule(x,y,0,0,255));
			break;
			
			default:
				cellules.add(new Cellule(x,y));
		}
	}
	
	public void dessiner(Graphics2D g2d)
	{
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0,0,largeur,hauteur);
		for (Cellule cellule : cellules)
		{
			cellule.dessiner(g2d);
		}
		
		if (total > TOTAL)
		{
			for (Cellule cellule : cellules)
			{
				cellule.desactiver();
			}
			initCellules();
		}
	}
	
	public void evoluer()
	{
		LinkedList<Cellule> cellulesAAjouter = new LinkedList<Cellule>();
		LinkedList<Cellule> cellulesAEnlever = new LinkedList<Cellule>();
		Random random = new Random();
		
		for (Cellule cellule : cellules)
		{
			cellule.grandit();
			if (cellule.isActif())
			{
				for (Cellule autreCellule : cellules)
				{
					if (cellule != autreCellule && autreCellule.isActif())
					{
						if(cellule.distance(autreCellule) < 0)
						{
							cellule.sEloigner(autreCellule);
							
							if (random.nextDouble() < 0.01)
							{
								Cellule nouvelleCellule = new Cellule(cellule,autreCellule);
								
								boolean ajoutable = true;
								for (Cellule celluleSoeur : cellules)
								{
									if (nouvelleCellule.isSoeur(celluleSoeur))
									{
										ajoutable = false;
									}
								}
								for (Cellule celluleSoeur : cellulesAAjouter)
								{
									if (nouvelleCellule.isSoeur(celluleSoeur))
									{
										ajoutable = false;
									}
								}
								if (ajoutable && cellule.getRayon() > Cellule.RAYON_MIN && autreCellule.getRayon() > Cellule.RAYON_MIN)
								{
									cellulesAAjouter.add(nouvelleCellule);
								}
							}
						}
					}
				}
			}
		}
		
		for (Cellule cellule : cellulesAAjouter)
		{
			cellules.add(cellule);
			total++;
		}
		
		int nombreActif = 0;
		for (Cellule cellule : cellules)
		{
			if (cellule.isActif())
			{
				nombreActif++;
			}
		}
		
		if (nombreActif == 0)
		{
			parent.dispose();
		}
		else if (nombreActif > NOMBRE_CELLULES_MAX )
		{
			for (Cellule cellule : cellules)
			{
				if (cellule.getRayon() > Cellule.RAYON_MIN)
				{
					cellule.desactiver();
				}
			}
		}
		for (Cellule cellule : cellules)
		{
			if (cellule.isBlanc())
			{
				cellulesAEnlever.add(cellule);
			}
		}
		for (Cellule cellule : cellulesAEnlever)
		{
			cellules.remove(cellule);
		}
		repaint();
	}
	
	//Private methods
	
	private void initCellules()
	{
		total = nbCellules;
		for (int i=0;i<nbCellules;i++)
		{
			ajouterCellule(i);
		}
	}
	
	//Private static final attributes
	
	private static final int NOMBRE_CELLULES_PAR_DEFAUT = 3;
	private static final int NOMBRE_CELLULES_MAX = 200;
	private static final int DISTANCE_MINIMALE = 50;
	private static final int TOTAL = 1000;
	
	//Private attributes
	
	private LinkedList<Cellule> cellules;
	private int nbCellules;
	private int largeur;
	private int hauteur;
	private JFrame parent;
	private int total;
}
