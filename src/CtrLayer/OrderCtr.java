package CtrLayer;

import java.sql.SQLException;
import java.util.List;

import DBLayer.IOrderDB;
import DBLayer.OrderDB;
import ModelLayer.Order;
import ModelLayer.PartOrder;
import ModelLayer.PartStep;

/**
 * @author Frank Eskelund
 * @version 
 */
public class OrderCtr {

	/**
	 * Field variable
	 */
	private IOrderDB orderDB;
	
	/**
	 * Constructor
	 * Invokes the OrderDB constructor in order to get access to the DB
	 */
	public OrderCtr()
	{
		this.orderDB = new OrderDB();
	}
	
	/**
	 * This method finds order by orderId
	 * 
	 * @param orderId
	 * @return
	 * @throws SQLException
	 */
	public Order findOrder(int orderId) throws SQLException
	{
		Order order = orderDB.findOrder(orderId);
		if (order != null) {
			List<PartOrder> partOrders = findAllPartOrders(order.getId());
			order.setPartOrderList(partOrders);
		}
		
		return order;
	}
	
	/**
	 * This method finds all active orders and their respective part orders based on the restaurantId
	 * 
	 * @param restaurantId
	 * @return
	 * @throws SQLException
	 */
	public List<Order> findAllActiveOrders(int restaurantId) throws SQLException
	{
		List<Order> orders = orderDB.findAllActiveOrders(restaurantId);
		for(Order order : orders)
		{
			List<PartOrder> partOrders = findAllPartOrders(order.getId());
			order.setPartOrderList(partOrders);
		}
		return orders;
	}
	
	public void deletePartSteps(int partStepId) throws SQLException
	{
		orderDB.deletePartSteps(partStepId);
	}
	
	/**
	 * This method finishes the PartStep by saving it in the DB
	 * 
	 * @param ps
	 * @throws SQLException
	 */
	public void savePartStep(PartStep ps) throws SQLException
	{
		orderDB.savePartStep(ps);
	}
	
	/**
	 * This method finds all PartOrders by orderId
	 * 
	 * @param orderID
	 * @return
	 * @throws SQLException
	 */
	public List<PartOrder> findAllPartOrders(int orderID) throws SQLException
	{
		return this.orderDB.findAllPartOrders(orderID);
	}
}
