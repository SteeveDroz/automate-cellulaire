public class Chrono implements Runnable
{
	public Chrono(long millis)
	{
		this.millis = millis;
	}
	
	public void run()
	{
		long tempsAvant = System.currentTimeMillis();
		while(System.currentTimeMillis() < tempsAvant + millis)
		{
		}
	}
	
	private long millis;
}