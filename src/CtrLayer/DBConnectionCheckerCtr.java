/**
 * 
 */
package CtrLayer;

import javax.swing.JLabel;

/**
 * @author Kim Dam Grønhøj
 *
 */
public class DBConnectionCheckerCtr {
	private Thread threadConnectionChecker;
	
	public DBConnectionCheckerCtr(JLabel label)
	{
		threadConnectionChecker = (new Thread(new DBConnectionCheckerThread(label)));
		
		// start checeker
		threadConnectionChecker.start();
	}
	
	public void isAlive() {
		if (!threadConnectionChecker.isAlive())
		{
			threadConnectionChecker.start();
		}
	}
}
