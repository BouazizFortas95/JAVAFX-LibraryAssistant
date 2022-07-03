package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import dbConnect.DBHandler;
import helpers.AlertMaker;
import helpers.LibraryAssistantUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
			usersList.clear();
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


	@FXML public void editUserInfoBTNPushed(ActionEvent event) {
		User selected_user = tv_users.getSelectionModel().getSelectedItem();

		if (selected_user == null) {
			AlertMaker.getAlertMessage(Alert.AlertType.ERROR, "Error", "Please select a user for Edit.");
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FXMLAddUser.fxml"));
			Parent parent = loader.load();
			AddUserController controller = (AddUserController) loader.getController();
			controller.inflateUI(selected_user);
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Edit User");
			stage.setScene(new Scene(parent));
			stage.show();
			LibraryAssistantUtil.setStageIcon(stage);
			stage.setOnCloseRequest((e)->{
				refreshUsersTableBTNPushed(new ActionEvent());
			});
		} catch (IOException e) {
			System.err.println("#Error_Message_editUserInfoBTNPushed : " + e.getLocalizedMessage());
		}
	}

	@FXML public void refreshUsersTableBTNPushed(ActionEvent event) {
		loadDataUsersTableFromDB();
	}

	@FXML public void deleteUserInfoBTNPushed(ActionEvent event) {
		User selected_user = tv_users.getSelectionModel().getSelectedItem();

		if (selected_user == null) {
			AlertMaker.getAlertMessage(Alert.AlertType.ERROR, "Error", "Please select a book for deletion.");
			return;
		}

		if (DBHandler.getInstance().isUserAlreadyIssued(selected_user)) {
			AlertMaker.getAlertMessage(Alert.AlertType.ERROR, "Error",
					"This book is already issued and can't be deleted.");
			return;
		}
		Optional<ButtonType> response = AlertMaker.getAlertMessageConfirmation("Deleting Book",
				"Are you sure want to delete the book?");
		if (response.get() == ButtonType.OK) {
			Boolean isDeleted = DBHandler.getInstance().deleteUserFromDB(selected_user);
			if (isDeleted) {
				AlertMaker.getAlertMessage(Alert.AlertType.INFORMATION, "Success",
						"The selected user has been deleted successfull.");
				tv_users.getItems().remove(selected_user);
			} else {
				AlertMaker.getAlertMessage(Alert.AlertType.ERROR, "Error",
						selected_user.getUsername() + " could not be deleted!");
			}

		} else {
			AlertMaker.getAlertMessage(Alert.AlertType.ERROR, "Cancelled", "Deletion user process is cancelled");
		}

	}

}
