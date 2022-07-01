/**
 * 
 */
package helpers;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * @author Bouaziz Fortas
 *
 */
public class AlertMaker {

	public static void getAlertMessage(AlertType alert_type, String title, String message) {
		// TODO Auto-generated method stub
		Alert alert = new Alert(alert_type);
		alert.setHeaderText(null);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public static Optional<ButtonType> getAlertMessageConfirmation(String title, String message) {
		// TODO Auto-generated method stub
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle(title);
		alert.setContentText(message);
		return alert.showAndWait();
	}
}
