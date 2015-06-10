/**
 * 
 */
package CtrLayer;

import DBLayer.ConnectionChecker;

/**
 * @author Kim Dam Grønhøj
 *
 */
public class DBConnectionCheckerThread extends Thread {
	private Boolean isConnectionOpen = false;
	
	public DBConnectionCheckerThread() throws InterruptedException
	{
		int times = 8;
		int i = 0;
		while (i < times)
		{
			ConnectionChecker cc = new ConnectionChecker();
			if (!cc.isConnectionActive()) {
				setIsConnectionOpen(false);
				i++;
			} else {
				setIsConnectionOpen(true);
			}
			
			Thread.sleep(4000);
		}
	}

	/**
	 * @return the isConnectionOpen
	 */
	public Boolean getIsConnectionOpen() {
		return isConnectionOpen;
	}

	/**
	 * @param isConnectionOpen the isConnectionOpen to set
	 */
	public void setIsConnectionOpen(Boolean isConnectionOpen) {
		this.isConnectionOpen = isConnectionOpen;
	}
}
