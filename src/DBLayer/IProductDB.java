/**
 * 
 */
package DBLayer;

import java.util.List;

import ModelLayer.Product;

/**
 * @author Kim Dam Gr�nh�j
 *
 */
public interface IProductDB {
	List<Product> findAllProducts(int orderId);
}
