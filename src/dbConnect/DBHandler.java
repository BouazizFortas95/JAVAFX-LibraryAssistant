package dbConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import com.mysql.jdbc.DatabaseMetaData;

import models.Book;
import models.User;

public final class DBHandler extends DBConfigs {

	private static DBHandler dbHandler = null;

	private static String driver_connector = "com.mysql.jdbc.Driver";
	private static Connection conn = null;
	private static Statement stmt = null;
	private static ResultSet rs = null;

	private DBHandler() {
		if (getConnection()) {
			setupBooksTableIntoDB();
			setupUsersTableIntoDB();
			setupIssueTableIntoDB();
		} else {
			System.exit(0);
		}
	}

	public static DBHandler getInstance() {
		if (dbHandler == null) {
			dbHandler = new DBHandler();
		}
		return dbHandler;
	}

	public Boolean getConnection() {

		try {
			String connectionStr = "jdbc:mysql://" + DBConfigs.DB_HOST + ":" + DBConfigs.DB_PORT + "/"
					+ DBConfigs.DB_NAME;

			Class.forName(driver_connector);
			conn = DriverManager.getConnection(connectionStr, DBConfigs.DB_USER, DBConfigs.DB_PASS);
			return true;
		} catch (SQLException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Can't load database", "Database Error", JOptionPane.ERROR_MESSAGE);
			// System.err.println("#DB_CONNECTION_ERROR : " + e.getMessage());
		}
		return false;
	}

	private void setupBooksTableIntoDB() {
		String TABLE_NAME = "books";

		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbm = (DatabaseMetaData) conn.getMetaData();
			rs = dbm.getTables(null, null, TABLE_NAME, null);
			if (rs.next()) {
				System.out.println("Table " + TABLE_NAME + " already exists. Ready for go!");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" + "id varchar(200) primary key," + "title varchar(200),"
						+ "author varchar(200)," + "publisher varchar(200)," + "isAvail boolean default true)");
			}
		} catch (SQLException e) {
			System.err.println("#Error_Message_setupBooksTableIntoDB : " + e.getMessage());
		} finally {

		}
	}

	private void setupUsersTableIntoDB() {
		// TODO Auto-generated method stub
		String TABLE_NAME = "users";

		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbm = (DatabaseMetaData) conn.getMetaData();
			rs = dbm.getTables(null, null, TABLE_NAME, null);
			if (rs.next()) {
				System.out.println("Table " + TABLE_NAME + " already exists. Ready for go!");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" + "id varchar(200) primary key,"
						+ "username varchar(200)," + "mobile varchar(15)," + "email varchar(100))");
			}
		} catch (SQLException e) {
			System.err.println("#Error_Message_setupUsersTableIntoDB : " + e.getMessage());
		} finally {

		}
	}

	private void setupIssueTableIntoDB() {
		// TODO Auto-generated method stub
		String TABLE_NAME = "issue";

		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbm = (DatabaseMetaData) conn.getMetaData();
			rs = dbm.getTables(null, null, TABLE_NAME, null);
			if (rs.next()) {
				System.out.println("Table " + TABLE_NAME + " already exists. Ready for go!");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" + "bookID varchar(200) primary key,"
						+ "userID varchar(200)," + "issueTime timestamp default CURRENT_TIMESTAMP,"
						+ "renew_count integer default 0," + "FOREIGN KEY (bookID) REFERENCES books(id),"
						+ "FOREIGN KEY (userID) REFERENCES users(id))");
			}
		} catch (SQLException e) {
			System.err.println("#Error_Message_setupIssueTableIntoDB : " + e.getMessage());
		} finally {

		}
	}

	public ResultSet execQuery(String query) {
		ResultSet result;
		try {
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);

		} catch (SQLException e) {
			System.err.println("#Error_Message_executeQuery : " + e.getLocalizedMessage());
			return null;
		} finally {

		}
		return result;
	}

	public boolean executeAction(String query) {
		try {
			stmt = conn.createStatement();
			stmt.execute(query);
			return true;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error : " + e.getMessage(), "Error Occured",
					JOptionPane.ERROR_MESSAGE);
			System.err.println("#Error_Message_executeAction : " + e.getLocalizedMessage());
			return false;
		} finally {

		}
	}

	public Boolean deleteBookFromDB(Book book) {
		try {
			String query = "DELETE FROM books WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, book.getId());
			return (ps.executeUpdate() == 1);
		} catch (SQLException e) {
			System.err.println("#Error_Message_deleteBookFromDB : " + e.getLocalizedMessage());
		}
		return false;
	}

	public boolean updateBookFromDB(Book book) {
		try {
			String query = "UPDATE `books` SET `title`=?,`author`=?, `publisher`=? WHERE `id`=?";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, book.getTitle());
			ps.setString(2, book.getAuthor());
			ps.setString(3, book.getPublisher());
			ps.setString(4, book.getId());
			return (ps.executeUpdate() > 0);
		} catch (SQLException e) {
			System.err.println("#Error_Message_updateBookFromDB : " + e.getLocalizedMessage());
		}
		return false;
	}

	public Boolean isBookAlreadyIssued(Book book) {
		// TODO Auto-generated method stub
		try {
			String query = "SELECT COUNT(*) FROM issue WHERE bookID = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, book.getId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				return (count > 0);
			}
		} catch (SQLException e) {
			System.err.println("#Error_Message_isBookAlreadyIssued : " + e.getLocalizedMessage());
		}
		return false;
	}

	public boolean updateUserFromDB(User user) {
		try {
			String query = "UPDATE `users` SET `username`=?,`mobile`=?, `email`=? WHERE `id`=?";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getMobile());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getId());
			return (ps.executeUpdate() > 0);
		} catch (SQLException e) {
			System.err.println("#Error_Message_updateUserFromDB : " + e.getLocalizedMessage());
		}
		return false;
	}

	public Boolean deleteUserFromDB(User user) {
		try {
			String query = "DELETE FROM users WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, user.getId());
			return (ps.executeUpdate() == 1);
		} catch (SQLException e) {
			System.err.println("#Error_Message_deleteUserFromDB : " + e.getLocalizedMessage());
		}
		return false;
	}

	public boolean isUserAlreadyIssued(User user) {
		try {
			String query = "SELECT COUNT(*) FROM issue WHERE userID = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, user.getId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				return (count > 0);
			}
		} catch (SQLException e) {
			System.err.println("#Error_Message_isUserAlreadyIssued : " + e.getLocalizedMessage());
		}
		return false;
	}

}
