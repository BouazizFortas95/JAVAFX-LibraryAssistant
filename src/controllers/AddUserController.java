/**
 * 
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dbConnect.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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

	@FXML
	void addUser(ActionEvent event) {
		String userID = tf_user_id.getText();
		String username = tf_username.getText();
		String mobile = tf_mobile.getText();
		String email = tf_email.getText();

		Boolean flag = userID.isEmpty() || username.isEmpty() || mobile.isEmpty() || email.isEmpty();
		if (flag) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Please Enter in all fields!");
			alert.showAndWait();
			return;
		}

		String query = "INSERT INTO `users` VALUES('" + userID + "', '" + username + "', '" + mobile + "', '" + email + "')";
		System.out.println(query);
		if (dbHandler.executeAction(query)) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Successful indserted data of new user!");
			alert.showAndWait();
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Please Enter in all fields!");
			alert.showAndWait();
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

}
