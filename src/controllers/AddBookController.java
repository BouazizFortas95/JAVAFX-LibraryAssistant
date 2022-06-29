package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class AddBookController implements Initializable {

	@FXML
	private AnchorPane rootPane;
	
	@FXML
	private JFXTextField tf_book_id;

	@FXML
	private JFXTextField tf_book_title;

	@FXML
	private JFXTextField tf_book_author;

	@FXML
	private JFXTextField tf_publisher;

	@FXML
	private JFXButton btn_save;

	@FXML
	private JFXButton btn_cancel;

	private DBHandler dbHandler;
	private ResultSet rs = null;

	@FXML
	void addBook(ActionEvent event) {
		String bookID = tf_book_id.getText();
		String bookAuthor = tf_book_author.getText();
		String bookTitle = tf_book_title.getText();
		String bookPublisher = tf_publisher.getText();

		if (bookID.isEmpty() || bookAuthor.isEmpty() || bookTitle.isEmpty() || bookPublisher.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Please Enter in all fields!");
			alert.showAndWait();
			return;
		}
		String query = "INSERT INTO `books` VALUES('" + bookID + "', '" + bookTitle + "', '" + bookAuthor + "', '"
				+ bookPublisher + "', " + true + ")";
		System.out.println(query);
		if (dbHandler.executeAction(query)) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Successful indserted data of new book!");
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

		checkData();
	}

	private void checkData() {
		String query = "SELECT title FROM `books`";
		rs = dbHandler.execQuery(query);
		try {
			while (rs.next()) {
				String titlex = rs.getString("title");
				System.out.println(titlex);
			}
		} catch (SQLException e) {
			System.err.println("#Error_Message_checkData : " + e.getLocalizedMessage());
		}

	}

}
