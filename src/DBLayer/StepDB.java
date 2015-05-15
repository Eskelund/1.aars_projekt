package DBLayer;

import java.sql.ResultSet;
import java.sql.Statement;

import ModelLayer.Customer;
import ModelLayer.Enums.CustomerType;

/**
 * 
 * @author Kim Dam Gr�nh�j
 * 
 * """"""""""""KODE ER IKKE F�RDIG"""""""""""""""
 *
 */
public class StepDB implements IStepDB {
	private Connection con;
	
	public StepDB()
	{
		this.con = new DBConnection().getDBcon();
	}
	
	public List<Step> findNextSteps(int orderId, int stepId)
	{
		String wClause = " phonenumber = '" + phonenumber +"'";
		return findSingleWhere(wClause);
	}
	
	private Customer findSingleWhere(String wClause) {

		ResultSet results;
		Customer customer = null;
		String query = findBuildQuery(wClause);
		
		Statement stmt = con.createStatement();
		stmt.setQueryTimeout(5);
		results = stmt.executeQuery(query);
		
		if(results.next()){
			customer = findBuildStep(results);
			stmt.close();
		}
		
		return customer;
	}

	private Customer findBuildStep(ResultSet results) throws Exception {
		Customer customer;

		int id = results.getInt("id");
		String lname = results.getString("lname");
		String fname = results.getString("fname");
		String address = results.getString("address");
		//City city = results.getCity("city");
		String email = results.getString("email");
		String phonenumber = results.getString("phonenumber");
		int customertypeNo = results.getInt("customertype");
		CustomerType customerType = CustomerType.values()[customertypeNo -1];
		
		customer = new Customer(id, lname, fname, address, null, phonenumber, email, customerType);
		return customer;
	}

	private String findBuildQuery(String wClause) {
		String query = "SELECT * FROM Customer";
		
		if(wClause.length()>0)
		{
			query=query+" where " + wClause;
		}	
		return query;
	}
}
