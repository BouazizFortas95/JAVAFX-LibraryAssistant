/**
 * 
 */
package models;

/**
 * @author Bouaziz Fortas
 *
 */
public class User {

	private String id;
	private String username;
	private String mobile;
	private String email;

	public User(String id, String username, String mobile, String email) {
		this.id = id;
		this.username = username;
		this.mobile = mobile;
		this.email = email;
	}


	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
}
