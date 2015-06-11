/**
 * 
 */
package CtrLayer;

import java.awt.Color;

import javax.swing.JLabel;

import DBLayer.ConnectionChecker;


/**
 * @author Kim Dam Grønhøj
 *
 */
public class DBConnectionCheckerThread extends Thread {
	private IConnectionStatusCallback callback;
	
	public DBConnectionCheckerThread(IConnectionStatusCallback callback)
	{
		this.callback = callback;
	}
	
	public void run() {
		ConnectionChecker cc = new ConnectionChecker();
		
		int times = 12; // number of allowed failed times
		int i = 0; // number of failed times
		while (i < times)
		{
			if (!cc.isConnectionActive()) {
				// callback to program connection was closed
				this.callback.connectionStatusCallback(false);
				
				i++;
			} else {
				// reset connection trying times
				i = 0;
				
				// callback to program connection was open
				this.callback.connectionStatusCallback(true);
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
