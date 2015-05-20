/**
 * 
 */
package CtrLayer;

import java.sql.SQLException;

import DBLayer.IOrderDB;
import DBLayer.OrderDB;
import ModelLayer.Order;

/**
 * @author Frank Eskelund
 * @version 
 */
public class OrderCtr {

	private IOrderDB orderDB;
	
	public OrderCtr()
	{
		this.orderDB = new OrderDB();
	}
	
	/*This function finds order by orderId */
	public Order findOrder(int orderId) throws SQLException
	{
		return orderDB.findOrder(orderId);
	}
}
