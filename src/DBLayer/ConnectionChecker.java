/**
 * 
 */
package DBLayer;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

/**
 * @author Kim Dam Grønhøj
 *
 */
public class ConnectionChecker {
	private Connection con;
	
	public ConnectionChecker() {
		try {
			this.con = DBConnection.getInstance().getDBcon();
		} catch (Exception e) {
			// Do nothing, connection is closed
		}
	}
	
	public Boolean isConnectionActive() {
		Boolean isOpen = false;
		
		try {
			Enumeration<NetworkInterface> interfaces = null;
			try {
				interfaces = NetworkInterface.getNetworkInterfaces();
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while (interfaces.hasMoreElements()) {
			    NetworkInterface nic = interfaces.nextElement();
			    System.out.print("Interface Name : [" + nic.getDisplayName() + "]");
			    try {
					System.out.println(", Is connected : [" + nic.isUp() + "]");
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (this.con != null) {
				Statement smt = this.con.createStatement();
				smt.setQueryTimeout(1);
				smt.executeQuery("SELECT 1");
				isOpen = true;
			}
		} catch (SQLException e) {
			if (this.con != null) {
				try {
					this.con.close();
				} catch (SQLException e1) {
					isOpen = false;
				}
			}
			
			isOpen = false;
		}
		
		return isOpen;
	}
}
