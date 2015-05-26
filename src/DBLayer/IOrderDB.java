package DBLayer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ModelLayer.Order;
import ModelLayer.PartStep;

/**
 * 
 * @author Kim Dam Gr�nh�j, Frank Eskelund
 *
 */
public interface IOrderDB {
	Order findOrder(int orderId) throws SQLException;
	void savePartStep(PartStep partStep) throws SQLException;
	ArrayList<Order> findAllActiveOrders(int restaurantID) throws SQLException;
}
