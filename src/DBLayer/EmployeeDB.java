package DBLayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ModelLayer.Employee;
import ModelLayer.PartStep;
import ModelLayer.Restaurant;
import ModelLayer.Town;

/**
 * @author Tobias, Kim Dam Gr�nh�j, Frank
 *
 */
public class EmployeeDB implements IEmployeeDB {
	private Connection con;
	
	/**
	 * initializes database connection
	 */
	public EmployeeDB() {
		this.con = DBConnection.getInstance().getDBcon();
	}

	/*
	 * Find all Employee's from restaurent
	 * @restaurentId current restaurent ID we need all employees
	 * @return list of all Employees for the restaurent
	 */
	@Override
	public List<Employee> getAllEmployees(int restaurentId) throws SQLException {
		if(restaurentId <= 0)
		{
			throw new IllegalArgumentException("An identifier must be a positive value.");
		}
		String wClause = " rest_id = ?";
		return multipleWhere(wClause, restaurentId);
	}
	
	/**
	 *  find one employee by person id
	 *  @personId current person id
	 *  @return filled Employee object with data
	 */
	@Override
	public Employee findEmployee(int personId) throws SQLException
	{
		if(personId <= 0)
		{
			throw new IllegalArgumentException("An identifier must be a positive value.");
		}
		String wClause = " E.person_id = ?";
		return singleWhere(wClause, personId);
	}
	
	private Employee singleWhere(String wClause, int personId) throws SQLException {
		Employee emp = null;
		
		// prepare SQL-statement
		PreparedStatement stmt = con.prepareStatement(buildSingleQuery(wClause));
		stmt.setQueryTimeout(5);
		
		// input parameters
		stmt.setInt(1, personId);
		
		// send SQL-query and open connection and return output
		ResultSet results = stmt.executeQuery();
		
		if (results.next()) {
			emp = buildEmployee(results);
		}
		
		// fill result data into object-model
		return emp;
	}

	private List<Employee> multipleWhere(String wClause, int restaurentId) throws SQLException {
		ResultSet results;
		List<Employee> list = new ArrayList<Employee>();
		String query = buildQuery(wClause);
		
		PreparedStatement statement = con.prepareStatement(query);
		statement.setInt(1, restaurentId);
		statement.setQueryTimeout(2);
		results = statement.executeQuery();
	
		while(results.next()){
			list.add(buildEmployee(results));
		}
		
		statement.close();
		
		return list;
	}
	
	private String buildSingleQuery(String wClause)
	{
		String query = "SELECT E.person_id, E.position, E.rest_id, E.employee_no, p.id, p.name, p.zip, p.street, p.phone, t.name AS town_name "
				+ "FROM Employee AS E "
				+ "INNER JOIN Person AS P ON E.person_id = P.id "
				+ "INNER JOIN Town AS T ON P.zip = T.zip";
		
		if(wClause.length() > 0){
			query += " WHERE " + wClause;
		}
		
		return query;
	}
	
	private Employee buildEmployee(ResultSet result) throws SQLException
	{
		int id = result.getInt("id");
		String name = result.getString("name");
		int zip = result.getInt("zip");
		String townName = result.getString("town_name");
		String street = result.getString("street");
		String phone = result.getString("phone");
		int employeeNo = result.getInt("employee_no");
		String position = result.getString("position");
		List<PartStep> partSteps = null;
		Restaurant restaurant = null;
		Town town = new Town(zip, townName);
		Employee emp = new Employee(id, name, town, street, phone, employeeNo, position, partSteps, restaurant);
		
		return emp;
	}

	private String buildQuery(String wClause) {
		String query = "SELECT E.person_id, E.position, E.rest_id, E.employee_no, p.id, p.name, p.zip, p.street, p.phone, t.name AS town_name "
				+ "FROM Employee AS E INNER JOIN Person AS P ON E.person_id = P.id INNER JOIN Town AS T ON P.zip = T.zip";
		
		if(wClause.length() > 0){
			query += " WHERE " + wClause;
		}
		
		return query;
	}
}
