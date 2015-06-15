/**
 * 
 */
package Utilities;

import java.awt.Color;

import javax.swing.JLabel;

import DBLayer.DBConnectionChecker;


/**
 * @author Kim Dam Gr�nh�j
 *
 */
public class DBConnectionCheckerThread extends Thread {
	private IConnectionStatusCallback callback;
	
	public DBConnectionCheckerThread(IConnectionStatusCallback callback)
	{
		this.callback = callback;
	}
	
	public void run() {
		DBConnectionChecker dbConnection = new DBConnectionChecker();
		
		int times = 40; // number of allowed failed times
		int i = 0; // number of failed times
		while (i < times)
		{
			if (!dbConnection.isConnectionActive()) {
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
