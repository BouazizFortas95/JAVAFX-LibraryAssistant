/**
 * 
 */
package controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Setting;

/**
 * @author Bouaziz Fortas
 *
 */
public class SettingsController implements Initializable {

	@FXML
	private AnchorPane rootPane;

	@FXML
	private JFXTextField tf_Ndays;

	@FXML
	private JFXTextField tf_fine_per_day;

	@FXML
	private JFXTextField tf_username;

	@FXML
	private JFXPasswordField pf_password;

	@FXML
	private JFXButton btn_save;

	@FXML
	private JFXButton btn_cancel;

	@FXML
	void saveSettings(ActionEvent event) {
		int ndays = Integer.parseInt(tf_Ndays.getText());
		float finePD = Float.parseFloat(tf_fine_per_day.getText());
		String username = tf_username.getText();
		String password = pf_password.getText();

		Setting setting = Setting.getSettings();
		setting.setnDaysCanKeepBook(ndays);
		setting.setFinePerDay(finePD);
		setting.setUsername(username);
		setting.setPassword(password);

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Confirm Update Settings");
		alert.setContentText("Are you sure want to update settings ?");
		Optional<ButtonType> response = alert.showAndWait();
		if (response.get() == ButtonType.OK) {
			Setting.updateSettings(setting);
			Alert alert_info = new Alert(Alert.AlertType.INFORMATION);
			alert_info.setHeaderText(null);
			alert_info.setTitle("Successful");
			alert_info.setContentText("Settings has been Updated.");
			alert_info.showAndWait();
		}else {
			Alert alert_info = new Alert(Alert.AlertType.INFORMATION);
			alert_info.setHeaderText(null);
			alert_info.setTitle("Cancelled");
			alert_info.setContentText("Update settings is Canceled.");
			alert_info.showAndWait();
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

		initDefaultValues();

	}

	private void initDefaultValues() {
		// TODO Auto-generated method stub
		Setting setting = Setting.getSettings();
		tf_Ndays.setText(String.valueOf(setting.getnDaysCanKeepBook()));
		tf_fine_per_day.setText(String.valueOf(setting.getFinePerDay()));
		tf_username.setText(String.valueOf(setting.getUsername()));
		pf_password.setText(String.valueOf(setting.getPassword()));
	}

}
