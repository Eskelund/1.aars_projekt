package DBLayer;

import java.util.List;

import ModelLayer.Employee;

/**
 * 
 * @author Kim Dam Gr�nh�j
 * @version 15-05-2015
 *
 */
public interface IEmployeeDB {
	List<Employee> findEmployees(int restaurentId);
}
