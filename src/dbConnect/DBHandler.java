package dbConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import com.mysql.jdbc.DatabaseMetaData;

public final class DBHandler extends DBConfigs {
	
	private static DBHandler dbHandler = null;

	private static String driver_connector = "com.mysql.jdbc.Driver";
	private static Connection conn = null;
	private static Statement stmt = null;
	private static ResultSet rs = null;
	
	private DBHandler() {
		getConnection();
		setupBooksTableIntoDB();
		setupUsersTableIntoDB();
		setupIssueTableIntoDB();
	}

	public static DBHandler getInstance() {
		if (dbHandler == null) {
			dbHandler = new DBHandler();
		}
		return dbHandler;
	}

	public void getConnection() {

		String connectionStr = "jdbc:mysql://" + DBConfigs.DB_HOST + ":" + DBConfigs.DB_PORT + "/" + DBConfigs.DB_NAME;

		try {
			Class.forName(driver_connector);
		} catch (ClassNotFoundException e) {
			System.err.println("#DRIVER_CONNECTOR_ERROR : " + e.getMessage());
		}
		
		try {
			conn = DriverManager.getConnection(connectionStr, DBConfigs.DB_USER, DBConfigs.DB_PASS);
		} catch (SQLException e) {
			System.err.println("#DB_CONNECTION_ERROR : " + e.getMessage());
		}
	}
	
	private void setupBooksTableIntoDB() {
		String TABLE_NAME = "books";
		
		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbm = (DatabaseMetaData) conn.getMetaData();
			rs = dbm.getTables(null,  null, TABLE_NAME, null);
			if (rs.next()) {
				System.out.println("Table "+TABLE_NAME+" already exists. Ready for go!");
			} else {
				stmt.execute("CREATE TABLE "+TABLE_NAME+"("
						+ "id varchar(200) primary key,"
						+ "title varchar(200),"
						+ "author varchar(200),"
						+ "publisher varchar(200),"
						+ "isAvail boolean default true)");
			}
		} catch (SQLException e) {
			System.err.println("#Error_Message_setupBooksTableIntoDB : "+e.getMessage());
		}finally {
			
		}
	}
	

	
	private void setupUsersTableIntoDB() {
		// TODO Auto-generated method stub
		String TABLE_NAME = "users";
		
		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbm = (DatabaseMetaData) conn.getMetaData();
			rs = dbm.getTables(null,  null, TABLE_NAME, null);
			if (rs.next()) {
				System.out.println("Table "+TABLE_NAME+" already exists. Ready for go!");
			} else {
				stmt.execute("CREATE TABLE "+TABLE_NAME+"("
						+ "id varchar(200) primary key,"
						+ "username varchar(200),"
						+ "mobile varchar(15),"
						+ "email varchar(100))");
			}
		} catch (SQLException e) {
			System.err.println("#Error_Message_setupUsersTableIntoDB : "+e.getMessage());
		}finally {
			
		}
	}


	private void setupIssueTableIntoDB() {
		// TODO Auto-generated method stub
				String TABLE_NAME = "issue";
				
				try {
					stmt = conn.createStatement();
					DatabaseMetaData dbm = (DatabaseMetaData) conn.getMetaData();
					rs = dbm.getTables(null,  null, TABLE_NAME, null);
					if (rs.next()) {
						System.out.println("Table "+TABLE_NAME+" already exists. Ready for go!");
					} else {
						stmt.execute("CREATE TABLE "+TABLE_NAME+"("
								+ "bookID varchar(200) primary key,"
								+ "userID varchar(200),"
								+ "issueTime timestamp default CURRENT_TIMESTAMP,"
								+ "renew_count integer default 0,"
								+ "FOREIGN KEY (bookID) REFERENCES books(id),"
								+ "FOREIGN KEY (userID) REFERENCES users(id))");
					}
				} catch (SQLException e) {
					System.err.println("#Error_Message_setupIssueTableIntoDB : "+e.getMessage());
				}finally {
					
				}
	}
	
	public ResultSet execQuery(String query) {
		ResultSet result;
		try {
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
			
		} catch (SQLException e) {
			System.err.println("#Error_Message_executeQuery : "+e.getLocalizedMessage());
			return null;
		}finally {
			
		}
		return result;
	}
	
	public boolean executeAction(String query) {
		try {
			stmt = conn.createStatement();
			stmt.execute(query);
			return true;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error : "+e.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
			System.err.println("#Error_Message_executeAction : "+e.getLocalizedMessage());
			return false;
		}finally {
			
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
