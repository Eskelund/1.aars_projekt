package DBLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ModelLayer.Customer;
import ModelLayer.Employee;
import ModelLayer.Order;
import ModelLayer.PartOrder;
import ModelLayer.PartStep;
import ModelLayer.Product;
import ModelLayer.Restaurant;
import ModelLayer.Step;
import ModelLayer.Town;

/**
 * 
 * @author Frank Eskelund, KIm Dam Gr�nh�j, Tobias
 * 
 *
 */
public class OrderDB implements IOrderDB {
	
	private Connection con;
	
	public OrderDB() 
	{
		this.con = DBConnection.getInstance().getDBcon();
	}
	
	public void deletePartSteps(int partStepId) throws SQLException
	{
		if(partStepId <= 0)
		{
			throw new IllegalArgumentException("An identifier must be a positive value.");
		}
		
		String query = "DELETE FROM [EmployeesOnPartStep] WHERE partstep_id = ?;DELETE FROM [PartStep] WHERE id = ?;";
				
		//
		// Delete employess on partstep and PartStep
		//
		
		// prepare SQL-statement
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setQueryTimeout(5);
		
		// input parameters
		stmt.setInt(1, partStepId);
		stmt.setInt(2, partStepId);
		
		// send SQL-query and open connection and return output
		stmt.execute();
		
		stmt.close();
	}
	
	/**
	 * Find all active orders by ResaurantID
	 * @param restaurantID current resaurantID
	 * @return list of Order objects
	 */
	public ArrayList<Order> findAllActiveOrders(int restaurantID) throws SQLException
	{
		if(restaurantID <= 0)
		{
			throw new IllegalArgumentException("An identifier must be a positive value.");
		}
		
		return multipleWhere(restaurantID);
	}
	
	private ArrayList<Order> multipleWhere(int restaurantID) throws SQLException {
		ResultSet results;
		ArrayList<Order> orders = new ArrayList<Order>();
		String query = "SELECT O.id as order_id, O.date as order_date, C.email as customer_email, CP.id as customer_id, CP.name as customer_name, CP.street as customer_street, CP.phone as customer_phone, T.zip as customer_zip, T.name as customer_town_name, PS.id as partstep_id, PS.startDate as partstep_startdate, S.id as step_id, S.name as step_name, S.description as step_description, S.is_last_step as step_is_last "
				+ "FROM [Order] AS O "
				+ "INNER JOIN CUSTOMER AS C ON C.person_id = O.customer_id "
				+ "INNER JOIN PERSON AS CP ON CP.id = C.person_id "
				+ "INNER JOIN TOWN AS T ON T.zip = CP.zip " 
				+ "INNER JOIN PARTSTEP AS PS ON PS.order_id = O.id "
				+ "INNER JOIN STEP AS S ON S.id = PS.step_id "
				+ "WHERE O.id NOT IN "
				+ "(SELECT O.id FROM [Order] AS O "
				+ "INNER JOIN PartStep AS PS ON PS.order_id = O.id "
				+ "INNER JOIN Step AS S ON S.id = PS.step_id "
				+ "WHERE O.rest_id = ? AND is_last_step = 1 ) "
				+ "AND O.rest_id = ? " 
				+ "ORDER BY O.id, partstep_startdate DESC";
		
		PreparedStatement statement = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		statement.setQueryTimeout(2);
		statement.setInt(1, restaurantID);
		statement.setInt(2, restaurantID);
		String sql = statement.toString();
		results = statement.executeQuery();
		while(results.next())
		{
			int orderId = results.getInt("order_id");
			Date orderDate = results.getDate("order_date");
			Customer customer = buildCustomer(results);
			Order order = new Order(orderId, orderDate, null, customer);
			ArrayList<PartStep> steps = buildPartSteps(results, null, order, false);
			order.setPartStepList(steps);
			orders.add(order);
		}
		
		statement.close();
		
		return orders;
	}

	/* (non-Javadoc)
	 * @see DBLayer.OrderDB#findOrder(int)
	 */
	@Override
	public Order findOrder(int orderId) throws SQLException 
	{
		if(orderId <= 0)
		{
			throw new IllegalArgumentException("An identifier must be a positive value.");
		}
		String wClause = " @orderid = " + orderId;
		return singleWhere(wClause);
	}
	
	//returns a single order based on a wClause
	private Order singleWhere(String wClause) throws SQLException
	{
		ResultSet results;
		Order order = null;
		String query = buildQuery(wClause);
		
		//Parameterne til createStatement gør det muligt at gå frem og tilbage i ResultSettet.
		PreparedStatement statement = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		statement.setQueryTimeout(2);
		results = statement.executeQuery();
		
		if(results.next())
		{
			int id = results.getInt("Order_Id");
			Date date = results.getDate("Order_Date");
			Customer customer = buildCustomer(results);
			Restaurant restaurant = buildRestaurant(results);
			order = new Order(id, date, restaurant, customer);
			order.setPartStepList(buildPartSteps(results, restaurant, order, true));
		}
		
		statement.close();
		
		return order;		
	}
	
	private ArrayList<PartStep> buildPartSteps(ResultSet results, Restaurant restaurant, Order order, boolean retreiveAssociation) throws SQLException {
		ArrayList<PartStep> steps = new ArrayList<PartStep>();
		boolean finished = false;
		while(!finished){
			int stepId = results.getInt("Step_ID");
			String stepName = results.getString("Step_Name");
			String stepDesc = results.getString("Step_Description");
			boolean isLast = results.getBoolean("Step_Is_Last");
			
			int partStepId = results.getInt("Partstep_ID");
			Date partstepStartDate = results.getDate("Partstep_startdate");
			ArrayList<Employee> employeesOnPartStep;
			if(retreiveAssociation){
				employeesOnPartStep = buildEmployees(results, restaurant);
			}else{
				employeesOnPartStep = null;
			}
			
			Step step = new Step(stepId, stepName, stepDesc, restaurant, isLast);
			PartStep partStep = new PartStep(step, partStepId, partstepStartDate, order, employeesOnPartStep);
			steps.add(partStep);
			if(results.next())
			{
				int currentPartStepID = results.getInt("order_id");
				finished = currentPartStepID != order.getId();
			}else{
				finished = true;
			}
			if(finished)
			{
				//Færdig med at håndtere partsteps, flyt cursoren én tupel tilbage.
				results.previous();
			}
		}
		return steps;
	}

	private ArrayList<Employee> buildEmployees(ResultSet results, Restaurant restaurant) throws SQLException {
		ArrayList<Employee> list = new ArrayList<Employee>();
		int employeeID = results.getInt("Employee_Person_ID");
		int partStepID = results.getInt("PartStep_ID");
		boolean finished = employeeID == 0;
		while(!finished){
			String name = results.getString("Employee_Name");
			int zip = results.getInt("Employee_Zip");
			String townName = results.getString("Employee_Town_Name");
			String street = results.getString("Employee_Street");
			String phone = results.getString("Employee_Phone");
			int employeeNo = results.getInt("Employee_no");
			String position = results.getString("Employee_Position");
			
			Town town = new Town(zip, townName);
			Employee e = new Employee(employeeID, name, town, street, phone, employeeNo, position, null, restaurant);
			list.add(e);
			
			//Gå frem til næste tupel, og check om den indeholder en yderligere a.
			if(results.next())
			{
				int currentPartStepID = results.getInt("PartStep_ID");
				finished = currentPartStepID != partStepID;
			}else{
				finished = true;
			}
			if(finished)
			{
				//Færdig med at håndtere ansatte, flyt cursoren én tupel tilbage.
				results.previous();
			}
		}
		
		return list;
	}

	private Restaurant buildRestaurant(ResultSet results) throws SQLException {
		Restaurant restaurant;
		int id = results.getInt("Restaurant_ID");
		String name = results.getString("Restaurant_Name");
		String email = results.getString("Restaurant_Email");
		String phone = results.getString("Restaurant_Phone");
		String website = results.getString("Restaurant_Website");
		String street = results.getString("Restaurant_Street");
		int zip = results.getInt("Restaurant_zip");
		String townName = results.getString("Restaurant_Town_Name");
		Town town = new Town(zip, townName);
		restaurant = new Restaurant(id, name, street, town, phone, email, website);
		return restaurant;
	}

	private Customer buildCustomer(ResultSet results) throws SQLException {
		Customer customer;
		int id = results.getInt("Customer_id");
		String name = results.getString("customer_name");
		int zip = results.getInt("customer_zip");
		String town = results.getString("Customer_Town_Name");
		String street = results.getString("Customer_Street");
		String phone = results.getString("Customer_Phone");
		String email = results.getString("Customer_email");
		
		Town customerTown = new Town(zip, town);
		customer = new Customer(id, name, customerTown, street, phone, email);
		return customer;
	}

	//Builds query with wClause
	private String buildQuery(String wClause)
	{
		String query = "EXEC FindOrder ";
		
		if(wClause.length() > 0) {
			query += wClause;
		}
		
		return query;		
	}
	
	/**
	 * Save a partstep by inserting PartStep object as parameter
	 * @param partStep current filled partstep
	 */
	@Override
	public void savePartStep(PartStep partStep) throws SQLException
	{
		// Declare variables
		List<Employee> emps = partStep.getEmployees();
		String combinedQuery = "";
		
		// SQL-queries
		String insertPartStepQuery = "DECLARE @partStepID INT;"
				+ "INSERT INTO [PartStep] ([step_id] ,[order_id]) VALUES(?,?);"
				+ "SET @partStepID = SCOPE_IDENTITY();";
		String insertAssociatedEmployeesQuery = "INSERT INTO [EmployeesOnPartStep] ([partstep_id] ,[employee_no]) VALUES (@partStepID, ?);";
		
		// add insert partstep query into combined query
		combinedQuery += insertPartStepQuery;
		
		// add the same insert associated query based on how many employees are into combined query
		int l = 0;
		while (emps.size() > l) {
			combinedQuery += insertAssociatedEmployeesQuery;
			l++;
		}
		
		// prepare combined statement
		PreparedStatement partStepStatement = con.prepareStatement(combinedQuery, Statement.RETURN_GENERATED_KEYS);
		partStepStatement.setQueryTimeout(2);
		
		// Parameter index
		int i = 1;
		
		// Add partstep parameters to query by index. First ? in query is fx. index 1. and next ? is index 2...
		partStepStatement.setInt(i++, partStep.getStep().getId());
		partStepStatement.setInt(i++, partStep.getOrder().getId());

		// Add EmployeesOnPartStep parameters 
		for(Employee e : emps)
		{
			partStepStatement.setInt(i++, e.getEmployeeNo());
		}	
		
		// Add all parameters into query
		partStepStatement.addBatch();
		
		// Execute combined query
		partStepStatement.executeBatch();
		
		partStepStatement.close();
	}
	
	/* (non-Javadoc)
	 * @see DBLayer.IProductDB#findAllProducts(int)
	 */
	@Override
	public List<PartOrder> findAllPartOrders(int orderId) throws SQLException {
		List<PartOrder> list = new ArrayList<PartOrder>();
		
		if(orderId <= 0)
		{
			throw new IllegalArgumentException("An identifier must be a positive value.");
		}
		
		String query = "SELECT * FROM [PartOrder] AS po INNER JOIN [Product] AS p ON po.product_id = p.id WHERE po.order_id = ?";
		
		// prepare SQL-statement
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setQueryTimeout(5);
		
		// input parameters
		stmt.setInt(1, orderId);
		
		// send SQL-query and open connection and return output
		ResultSet results = stmt.executeQuery();
		
		while(results.next())
		{
			list.add(buildPartOrder(results));
		}
		
		stmt.close();
		
		return list;
	}
	
	private PartOrder buildPartOrder(ResultSet results) throws SQLException
	{
		Product p = new Product(results.getString("name"), results.getDouble("price"));	
		PartOrder model = new PartOrder(results.getInt("amount"), p);	
		
		return model;
	}
}
