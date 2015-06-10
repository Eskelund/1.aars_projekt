/**
 * 
 */
package CtrLayer;

/**
 * @author Kim Dam Grønhøj
 *
 */
public class DBConnectionCheckerCtr {
	private Thread checker;
	
	public DBConnectionCheckerCtr()
	{
		try {
			checker = (new Thread(new DBConnectionCheckerThread()));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		checker.start();
	}
	
	public void isAlive() {
		if (!checker.isAlive())
		{
			checker.start();
		}
	}
}
