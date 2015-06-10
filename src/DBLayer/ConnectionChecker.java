/**
 * 
 */
package DBLayer;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Kim Dam Grønhøj
 *
 */
public class ConnectionChecker {
	private Connection con;
	
	public ConnectionChecker() {
		this.con = DBConnection.getInstance().getDBcon();
	}
	
	public Boolean isConnectionActive() {
		try {
			return !this.con.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}
}
