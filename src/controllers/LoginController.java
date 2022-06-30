/**
 * 
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.codec.digest.DigestUtils;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import helpers.LibraryAssistantUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Setting;

/**
 * @author Bouaziz Fortas
 *
 */
public class LoginController implements Initializable {


	@FXML
	private JFXTextField tf_username;

	@FXML
	private JFXPasswordField pf_password;
	
	Setting settings;

	@FXML
	void cancelBTNPushed(ActionEvent event) {
		System.exit(0);
	}

	@SuppressWarnings("deprecation")
	@FXML
	void loginBTNPushed(ActionEvent event) {
		String username = tf_username.getText();
		String password = DigestUtils.shaHex(pf_password.getText());
		
		if (username.equals(settings.getUsername()) && password.equals(settings.getPassword())) {
			closeStage();
			loadWindow();
		}else{
			Alert alert_err = new Alert(Alert.AlertType.ERROR);
			alert_err.setHeaderText(null);
			alert_err.setTitle("Error");
			alert_err.setContentText("Login Operation is Faild!!, Please Try Again with correct inputs.");
			alert_err.showAndWait();
		}
	}

	private void closeStage() {
		// TODO Auto-generated method stub
		((Stage) tf_username.getScene().getWindow()).close();
	}
	
	private void loadWindow() {
		// TODO Auto-generated method stub
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/views/FXMLMainApp.fxml"));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Library Assistant");
			stage.setScene(new Scene(parent));
			stage.show();

			LibraryAssistantUtil.setStageIcon(stage);
		} catch (IOException e) {
			System.err.println("#Error_Message_LoginController_loadWindow : " + e.getLocalizedMessage());
		}
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		settings = Setting.getSettings();
	}

}
