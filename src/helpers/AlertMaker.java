/**
 * 
 */
package helpers;

import java.util.List;
import java.util.Optional;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

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

	public static void dialogShowMessage(StackPane stackPane, Node node2BBlure, JFXTextField tf,
			List<JFXButton> controls, String title, String msg) {

		BoxBlur boxBlur = new BoxBlur(3, 3, 3);

		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		dialogLayout.getStyleClass().add("dialog");
		JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);

		controls.forEach((btn) -> {
			btn.getStyleClass().add("main-button");
			btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
				dialog.close();
			});
		});
		dialogLayout.setHeading(new Label(title));
		dialogLayout.setBody(new Label(msg));
		dialogLayout.setActions(controls);
		dialog.show();
		dialog.setOnDialogClosed((JFXDialogEvent ev) -> {
			node2BBlure.setEffect(null);
			if (tf != null) {
				tf.setDisable(false);
			}
		});
		node2BBlure.setEffect(boxBlur);
		if (tf != null) {
			tf.setDisable(true);
			tf.setText(null);
		}
	}
}
