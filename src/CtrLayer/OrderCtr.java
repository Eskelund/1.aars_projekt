/**
 * 
 */
package CtrLayer;

import DBLayer.IOrderDB;
import ModelLayer.Order;

/**
 * @author Frank Eskelund, Kim Dam Gr�nh�j
 * @version 
 */
public class OrderCtr {

	private IOrderDB orderDB;
	
	public OrderCtr()
	{
		/* orderDB = new OrderDB(); Kim: 16-05-2015: Kan ikke compile! */
	}
	
	/*This function finds order by orderId */
	public Order findOrder(int orderId)
	{
		return null;
	}
}
