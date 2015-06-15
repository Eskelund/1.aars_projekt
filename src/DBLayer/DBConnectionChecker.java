/**
 * 
 */
package DBLayer;

import java.net.SocketException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

/**
 * @author Kim Dam Gr�nh�j
 *
 */
public class DBConnectionChecker {
	private Connection con;
	
	public DBConnectionChecker() {
		try {
			this.con = DBConnection.getInstanceTestConnection().getDBcon();
		} catch (Exception e) {
			this.con = null;
		}
	}
	
	public Boolean isConnectionActive() {
		Boolean isOpen = false;
		Statement smt = null;
		
		// Is connection empty?
		if (this.con == null) {
			this.con = DBConnection.getInstanceTestConnection().getDBcon();
		}
		
		try {
			if (this.con != null) {
				// Test connection
				smt = this.con.createStatement();
				smt.setQueryTimeout(1);
				smt.executeQuery("SELECT 1");
				isOpen = true;
			}
		} catch (SQLException e) {
			// close statement
			if (smt != null) {
				try {
					smt.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			// Close failed connection
			if (this.con != null) {
				try {
					this.con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			// Clear old instance
			DBConnection.emptyInstance();
			this.con = null;
		}
		
		return isOpen;
	}
}
