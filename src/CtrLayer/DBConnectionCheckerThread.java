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
	private Boolean isConnectionOpen;
	private JLabel label;
	
	public DBConnectionCheckerThread(JLabel label)
	{
		this.label = label;
		isConnectionOpen = false;
	}
	
	public void run() {
		ConnectionChecker cc = new ConnectionChecker();
		
		int times = 12; // number of allowed failed times
		int i = 0; // number of failed times
		while (i < times)
		{
			if (!cc.isConnectionActive()) {
				// set connection is closed
				setIsConnectionOpen(false);
				
				// set label color
				this.label.setText("Database offline");
				this.label.setForeground(Color.RED);
				
				i++;
			} else {
				// reset connection trying times
				i = 0;
				
				// set connection is open
				setIsConnectionOpen(true);
				
				// set label color
				this.label.setText("Database online");
				this.label.setForeground(Color.GREEN);
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
