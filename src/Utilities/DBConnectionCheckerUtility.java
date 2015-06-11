/**
 * 
 */
package Utilities;

import java.awt.Color;

import javax.swing.JLabel;

import DBLayer.DBConnection;
import GuiLayer.PartStepUI;

/**
 * @author Kim Dam Grønhøj
 *
 */
public class DBConnectionCheckerUtility implements IConnectionStatusCallback  {
	private Thread threadConnectionChecker;
	private JLabel label;
	private PartStepUI frame;
	private Boolean useOpenConnectionMessages;
	private Boolean useClosedConnectionMessages;
	
	public DBConnectionCheckerUtility(PartStepUI frame, JLabel label)
	{
		this.frame = frame;
		this.label = label;
		this.useOpenConnectionMessages = false;
		this.useClosedConnectionMessages = true;
		
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
			if (this.useOpenConnectionMessages) {
				this.useOpenConnectionMessages = false;
				this.useClosedConnectionMessages = true;
				
				// send gui messages
				this.frame.enableWindowAndConnectionIsBack();
			}
			
			// update gui
			this.label.setText("Database online");
			this.label.setForeground(Color.GREEN);
		} else {
			this.useOpenConnectionMessages = true;
			// reset database instance
			DBConnection.emptyInstance();
			
			// send gui messages
			if (this.useClosedConnectionMessages) {
				this.frame.disableWindow();
				this.useClosedConnectionMessages = false;
			}
			// update gui
			this.label.setText("Database offline");
			this.label.setForeground(Color.red);
		}
	}
}
