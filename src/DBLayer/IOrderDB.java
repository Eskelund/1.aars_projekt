package DBLayer;

import java.util.List;

/**
 * 
 * @author Kim Dam Gr�nh�j
 *
 */
public interface IOrderDB {
	List<Order> findOrder(int orderId);
}
