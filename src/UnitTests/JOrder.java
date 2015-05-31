package UnitTests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import CtrLayer.EmployeeCtr;
import CtrLayer.OrderCtr;
import CtrLayer.StepCtr;
import DBLayer.OrderDB;
import ModelLayer.Order;
import ModelLayer.PartOrder;
import ModelLayer.PartStep;
import ModelLayer.Step;

/**
 * This class represent all Unit Tests for Orders, PartSteps and partorders
 * @author Kim Dam Gr�nh�j, Tobias, Frank
 *
 */
public class JOrder {

	/**
	 * This is a positive test to find an order with partsteps, employees, partorders
	 * @throws SQLException SQL-server can fail
	 */
	@Test
	public void positiveTest_CanFindOrder() throws SQLException {
		OrderCtr ctr = new OrderCtr();
		Order o = ctr.findOrder(2);
		assertEquals(2, o.getId());
		assertEquals(2, o.getPartStepList().get(1).getEmployees().size());
		assertEquals(5, o.getPartStepList().get(1).getId());
		assertEquals("Pizza", o.getPartOrderList().get(0).getProduct().getName());
		assertNotNull(o);
	}
	
	/**
	 * This is a negative test to check orderCtr returns nothing with ID 2020
	 * @throws SQLException
	 */
	@Test
	public void negativeTest_NoOrder() throws SQLException
	{
		OrderCtr ctr = new OrderCtr();
		Order o = ctr.findOrder(2020);
		assertNull(o);
	}

	/**
	 * This is a positive test, testing adding a part step to database
	 * @throws SQLException SQL-server can fail
	 */
	@Test
	public void CanAddPartStep() throws SQLException
	{
		// Set this to true to execute inserts
		Boolean doExecuteQuery = false;
		
		OrderCtr oCtr = new OrderCtr();
		StepCtr sCtr = new StepCtr();
		EmployeeCtr eCtr = new EmployeeCtr();
		Step s = sCtr.findNextSteps(2).get(0);
		Order o = oCtr.findOrder(1);
		PartStep ps = new PartStep(s, o);
		ps.setEmployees(eCtr.getAllEmployees(1));
		
		if (doExecuteQuery) {
			oCtr.finishStep(ps);
		}
	}

	/**
	 * This is a negative test, testing an order will not be returned with ID under 0
	 * @throws SQLException SQL-server can fail
	 */
	@Test(expected=IllegalArgumentException.class)
	public void negativeTest_NoOrder_WithNegativeNumber() throws SQLException
	{
		OrderCtr ctr = new OrderCtr();
		Order o = ctr.findOrder(-1);
	}
	
	/**
	 * This is negative test, testing an order will not be returned with ID 0
	 * @throws SQLException SQL-server can fail
	 */
	@Test(expected=IllegalArgumentException.class)
	public void negativeTest_NoOrder_WithZero() throws SQLException
	{
		OrderCtr ctr = new OrderCtr();
		Order o = ctr.findOrder(0);
	}
	
	/**
	 * This is a positive test, testing all active orders will be returned by restaurant ID and all data is filled in the domain model
	 * @throws SQLException SQL-server can fail
	 */
	@Test
	public void positiveTest_CanFindAllActiveOrders() throws SQLException
	{
		OrderCtr ctr = new OrderCtr();
		ArrayList<Order> orders = (ArrayList<Order>) ctr.findAllActiveOrders(1);
		assertNotNull(orders);
		assertEquals(2, orders.size());
		assertEquals(2, orders.get(0).getPartOrderList().size());
	}
	
	/**
	 * This is a positive test, testing delete part steps
	 * @throws SQLException
	 */
	@Test
	public void positiveTest_deletePartSteps() throws SQLException
	{
		int partStepId = 1;
		int orderId = 1;
		
		OrderCtr orderCtr = new OrderCtr();
			
		Order order = orderCtr.findOrder(orderId);
		List<PartStep> steps = order.getPartStepList();
		
		Boolean found = false;
		int i = 0;
		while (!found || steps.size() > i)
		{
			PartStep po = steps.get(i);
			if (po.getId() == partStepId)
			{
				orderCtr.deletePartSteps(partStepId);
				
				found = true;
			}
			i++;
		}
		
		// Check the partsteps is deleted by getting the data again and check it exist
		order = orderCtr.findOrder(orderId);
		steps = order.getPartStepList();
		
		found = false;
		i = 0;
		while (!found || steps.size() > i)
		{
			PartStep po = steps.get(i);
			if (po.getId() == partStepId)
			{
				found = true;
			}
			l++;
		}
		
		assertTrue(!found);
	}
	
	/**
	 * This is a positive test, testing to get partorders by orderId
	 * @throws SQLException SQL-server can fail
	 */
	@Test
	public void positiveTest_FindAllPartOrders() throws SQLException {
		OrderCtr oc = new OrderCtr();
		List<PartOrder> partOrders = oc.findAllPartOrders(1);
		
		for (PartOrder po : partOrders)
		{
			System.out.println("Amount: " + po.getAmount());
	
			System.out.println("Product name: " + po.getProduct().getName());
		}
		
		assertTrue("partorders found", partOrders.size() > 0);
	}
}
