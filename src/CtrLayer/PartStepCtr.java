package CtrLayer;

import java.sql.SQLException;
import java.util.ArrayList;

import ModelLayer.Employee;
import ModelLayer.Order;
import ModelLayer.OrderInfoViewModel;
import ModelLayer.PartStep;
import ModelLayer.Step;


/**
 * @author Frank Eskelund, Kim Dam Gr�nh�j, Bo Handskemager Sørensen
 * @version 
 */
public class PartStepCtr {
	
	/**
	 * Field variables
	 */
	private PartStep currentPartStep;
	private EmployeeCtr employeeCtr;
	private StepCtr stepCtr;
	private OrderCtr orderCtr;
	
	/**
	 * Constructor
	 * Calls the Employee-, Step- and OrderCtr in order to find the right order and employee
	 * so it can set the correct PartStep
	 */
	public PartStepCtr()
	{
		employeeCtr = new EmployeeCtr();
		stepCtr = new StepCtr();
		orderCtr = new OrderCtr();
	}
	
	/**
	 * TODO Tobias 
	 * 
	 * @param orderID
	 * @return
	 * @throws SQLException
	 */
	public OrderInfoViewModel findOrderInfo(int orderId) throws SQLException
	{
		Order o = orderCtr.findOrder(orderId);
		o.setPartOrderList(orderCtr.findAllPartOrders(orderId));
		ArrayList<PartStep> ps = (ArrayList<PartStep>) o.getPartStepList();
		PartStep lastPartStep = ps.get(0);
		ArrayList<Step> sl = (ArrayList<Step>) stepCtr.findNextSteps(lastPartStep.getStep().getId());
		
		ArrayList<Employee> el = (ArrayList<Employee>) employeeCtr.getAllEmployees(o.getRest().getId());
		OrderInfoViewModel vm = new OrderInfoViewModel(el, sl, o);
		
		return vm;
	}

	/**
	 * This method sets updates the current PartStep with the next one selected
	 * 
	 * @param order, step
	 */
	public void setPartStep(Step step, Order order) 
	{
		 currentPartStep = new PartStep(step, order);
	}
	
	/**
	 * Assigns an employee with a unique employee id number
	 * 
	 * @param employeeNo
	 */
	public void associateEmployees(ArrayList<Employee> employees)
	{
		currentPartStep.setEmployees(employees);
	}

	/**
	 * Firing this method, sends all data to database
	 * @throws SQLException 
	 */
	public void savePartStep() throws SQLException
	{
		orderCtr.savePartStep(currentPartStep);
	}
}
