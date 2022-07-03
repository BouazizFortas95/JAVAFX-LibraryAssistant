/**
 * 
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dbConnect.DBHandler;
import helpers.AlertMaker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.User;

/**
 * @author Bouaziz Fortas
 *
 */
public class AddUserController implements Initializable {

	@FXML
	private AnchorPane rootPane;

	@FXML
	private JFXTextField tf_user_id;

	@FXML
	private JFXTextField tf_username;

	@FXML
	private JFXTextField tf_mobile;

	@FXML
	private JFXTextField tf_email;

	@FXML
	private JFXButton btn_save;

	@FXML
	private JFXButton btn_cancel;

	private DBHandler dbHandler;
	
	Boolean isEditMode = Boolean.FALSE;

	@FXML
	void addUser(ActionEvent event) {
		String userID = tf_user_id.getText();
		String username = tf_username.getText();
		String mobile = tf_mobile.getText();
		String email = tf_email.getText();

		Boolean flag = userID.isEmpty() || username.isEmpty() || mobile.isEmpty() || email.isEmpty();
		if (flag) {
			AlertMaker.getAlertMessage(Alert.AlertType.ERROR, "Error", "Please Enter in all fields!");
			return;
		}
		
		if (isEditMode) {
			updateInfoUser();
		} else {
			insertInfoUser(userID, username, mobile, email);
		}

		
	}

	/**
	 * @param userID
	 * @param username
	 * @param mobile
	 * @param email
	 */
	public void insertInfoUser(String userID, String username, String mobile, String email) {
		String query = "INSERT INTO `users` VALUES('" + userID + "', '" + username + "', '" + mobile + "', '" + email + "')";
		if (dbHandler.executeAction(query)) {
			AlertMaker.getAlertMessage(Alert.AlertType.INFORMATION, "Success", "Successful indserted data of new user!");
		} else {
			AlertMaker.getAlertMessage(Alert.AlertType.ERROR, "Errror", "This user information can't be added!!");
		}
	}

	private void updateInfoUser() {
		User user = new User(tf_user_id.getText(), tf_username.getText(), tf_mobile.getText(), tf_email.getText());
		if (dbHandler.updateUserFromDB(user)) {
			AlertMaker.getAlertMessage(Alert.AlertType.INFORMATION, "Success", "Successful Updated data of user selected!");
		}else {
			AlertMaker.getAlertMessage(Alert.AlertType.ERROR, "Error", "Can't Update data of this user!!");
		}
	}

	@FXML
	void cancel(ActionEvent event) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		dbHandler = DBHandler.getInstance();
	}

	public void inflateUI(User user) {
		tf_user_id.setText(user.getId());
		tf_user_id.setEditable(false);
		tf_username.setText(user.getUsername());
		tf_mobile.setText(user.getMobile());
		tf_email.setText(user.getEmail());
		isEditMode = Boolean.TRUE;
	}

}
