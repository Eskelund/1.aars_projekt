/**
 * 
 */
package CtrLayer;

import java.awt.Color;

import javax.swing.JLabel;

import DBLayer.DBConnection;

/**
 * @author Kim Dam Grønhøj
 *
 */
public class DBConnectionCheckerCtr implements IConnectionStatusCallback  {
	private Thread threadConnectionChecker;
	private JLabel label;
	
	public DBConnectionCheckerCtr(JLabel label)
	{
		this.label = label;
		// create thread and set the controller itself as callback
		threadConnectionChecker = (new Thread(new DBConnectionCheckerThread(this)));
		
		// start checker
		threadConnectionChecker.start();
	}
	
	/**
	 * This method is used in the program to start the thread again when using buttons
	 */
	public void isAlive() {
		if (!threadConnectionChecker.isAlive())
		{
			threadConnectionChecker.start();
		}
	}
	
	/**
	 * Test is a callback method for the thread to set the status in GUI
	 */
	public void connectionStatusCallback(Boolean isConnected) {
		if (isConnected) {
			this.label.setText("Database online");
			this.label.setForeground(Color.GREEN);
		} else {
			DBConnection.emptyInstance();
			
			this.label.setText("Database offline");
			this.label.setForeground(Color.red);
		}
	}
}
