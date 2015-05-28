/**
 * 
 */
package ModelLayer;

import java.util.List;

/**
 * This class is a ViewModel with all data for the Use Case "set step"
 * @author Bo Handskemager S�rensen, Kim Dam Gr�nh�j
 *
 */
public class OrderInfoViewModel {
	private List<Employee> employees;
	private List<Step> steps;
	private Order order;
	
	/**
	 * OrderInfoViewModel constructor initialize this model
	 * @param employees
	 * @param steps
	 * @param order
	 */
	public OrderInfoViewModel(List<Employee> employees, List<Step> steps, Order order){
		this.employees = employees;
		this.steps = steps;
		this.order = order;
	}

	/**
	 * @return the employees
	 */
	public List<Employee> getEmployees() {
		return employees;
	}

	/**
	 * @return the steps
	 */
	public List<Step> getSteps() {
		return steps;
	}

	/**
	 * @return the order
	 */
	public Order getOrder() {
		return order;
	}
}
