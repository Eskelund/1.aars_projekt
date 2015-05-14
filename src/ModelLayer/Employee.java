/**
 * 
 */
package ModelLayer;

import java.util.List;

/**
 * @author Bo Handskemager S�rensen
 *
 */
public class Employee extends Person {
	public int employeeNo;
	public String position;
	public List<PartStep> partSteps;
	public Restaurant restaurant;
	
	/**
	 * @param id
	 * @param name
	 * @param zip
	 * @param street
	 * @param phone
	 */
	public Employee(int id, String name, int zip, String street, String phone, int employeeNo, String position, List<PartStep> partSteps, Restaurant restaurant) {
		super(id, name, zip, street, phone);
		// TODO Auto-generated constructor stub
		this.employeeNo = employeeNo;
		this.position = position;
		this.partSteps = partSteps;
		this.restaurant = restaurant;
	}
}
