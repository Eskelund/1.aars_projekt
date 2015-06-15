/**
 * 
 */
package Utilities;

import java.awt.Color;

import javax.swing.JLabel;

import DBLayer.DBConnection;
import GuiLayer.PartStepUI;

/**
 * @author Kim Dam Gr�nh�j
 *
 */
public class DBConnectionCheckerUtility implements IConnectionStatusCallback  {
	private Thread threadConnectionChecker;
	private PartStepUI frame;
	private Boolean useOpenConnectionMessages;
	private Boolean useClosedConnectionMessages;
	
	public DBConnectionCheckerUtility(PartStepUI frame)
	{
		this.frame = frame;
		this.useOpenConnectionMessages = false;
		this.useClosedConnectionMessages = true;
		
		// create thread and set the controller itself as callback
		threadConnectionChecker = new DBConnectionCheckerThread(this);
		
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

		} else {
			this.useOpenConnectionMessages = true;
			// reset database instance
			DBConnection.emptyInstance();
			
			// send gui messages
			if (this.useClosedConnectionMessages) {
				this.frame.disableWindow();
				this.useClosedConnectionMessages = false;
			}

		}
	}
}
