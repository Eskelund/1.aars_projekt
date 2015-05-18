package DBLayer;

import java.sql.SQLException;
import java.util.List;

import ModelLayer.Order;

/**
 * 
 * @author Kim Dam Gr�nh�j, Frank Eskelund
 *
 */
public interface IOrderDB {
	Order findOrder(int orderId) throws SQLException;
}
