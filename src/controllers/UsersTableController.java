package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import dbConnect.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import models.User;

public class UsersTableController implements Initializable {
	@FXML
	private AnchorPane rootPane;
	@FXML
	private TableView<User> tv_users;
	@FXML
	private TableColumn<User, String> col_id;
	@FXML
	private TableColumn<User, String> col_username;
	@FXML
	private TableColumn<User, String> col_mobile;
	@FXML
	private TableColumn<User, String> col_email;

	ObservableList<User> usersList = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		initUsersTableView();
		loadDataUsersTableFromDB();
	}

	private void loadDataUsersTableFromDB() {
		// TODO Auto-generated method stub
		DBHandler dbHandler = DBHandler.getInstance();
		String query = "SELECT * FROM users";
		ResultSet rs = dbHandler.execQuery(query);
		try {
			while (rs.next()) {
				String id = rs.getString("id");
				String username = rs.getString("username");
				String mobile = rs.getString("mobile");
				String email = rs.getString("email");
				
				usersList.add(new User(id, username, mobile, email));
			}
		} catch (SQLException e) {
			System.err.println("#Error_Message_loadDataUsersTableFromDB : " + e.getLocalizedMessage());
		}
		tv_users.getItems().setAll(usersList);
	}

	private void initUsersTableView() {
		// TODO Auto-generated method stub
		col_id.setCellValueFactory(new PropertyValueFactory<User, String>("id"));
		col_username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
		col_mobile.setCellValueFactory(new PropertyValueFactory<User, String>("mobile"));
		col_email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
	}

}
