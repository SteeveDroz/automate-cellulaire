import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Main
{
	public static void main(String[] args)
	{
		int largeur = 1440;
		int hauteur = 900;
		JFrame frame = new JFrame();
		
		AutomateCellulaire automate = new AutomateCellulaire(12,largeur,hauteur,frame);
		frame.add(automate,BorderLayout.CENTER);
		frame.setLocation(0,0);
		frame.setSize(largeur,hauteur);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setUndecorated(true);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Cursor cursor = toolkit.createCustomCursor(new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB),new Point(15,15),"Vide");
		frame.setCursor(cursor);
		
		frame.addKeyListener(new KeyListener()
			{
				public void keyPressed(KeyEvent e)
				{
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					{
						JFrame frame = (JFrame) e.getSource();
						frame.dispose();
					}
				}
				
				public void keyReleased(KeyEvent e)
				{
				}
				
				public void keyTyped(KeyEvent e)
				{
				}
				
			}
		);
		frame.setVisible(true);
		
		while (frame.isVisible())
		{
			Thread chrono = new Thread(new Chrono(40));
			chrono.start();
			
			try
			{
				chrono.join();
				automate.evoluer();
			}
			catch(InterruptedException e)
			{
			}
		}
	}
}
