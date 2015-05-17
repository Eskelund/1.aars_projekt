/**
 * 
 */
package ModelLayer;

/**
 * @author Bo Handskemager S�rensen, Kim Dam Gr�nh�j
 *
 */
public class Person {
	private int id;
	private String name;
	private Town town;
	private String street;
	private String phone;
	
	Person (int id, String name, Town town, String street, String phone){
		this.id = id;
		this.name = name;
		this.town = town;
		this.street = street;
		this.phone = phone;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the town
	 */
	public Town getTown() {
		return town;
	}
	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	

}
