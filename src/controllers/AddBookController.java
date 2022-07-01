package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dbConnect.DBHandler;
import helpers.AlertMaker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Book;
import models.User;

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
	private Boolean isEditMode = Boolean.FALSE;

	@FXML
	void addBook(ActionEvent event) {
		String bookID = tf_book_id.getText();
		String bookAuthor = tf_book_author.getText();
		String bookTitle = tf_book_title.getText();
		String bookPublisher = tf_publisher.getText();

		if (bookID.isEmpty() || bookAuthor.isEmpty() || bookTitle.isEmpty() || bookPublisher.isEmpty()) {
			AlertMaker.getAlertMessage(Alert.AlertType.ERROR, "Error", "Please Enter in all fields!");
			return;
		}
		
		if (isEditMode) {
			updateInfoBook();
		}else {
			insertInfoBook(bookID, bookAuthor, bookTitle, bookPublisher);
		}
	}

	/**
	 * @param bookID
	 * @param bookAuthor
	 * @param bookTitle
	 * @param bookPublisher
	 */
	public void insertInfoBook(String bookID, String bookAuthor, String bookTitle, String bookPublisher) {
		String query = "INSERT INTO `books` VALUES('" + bookID + "', '" + bookTitle + "', '" + bookAuthor + "', '"
				+ bookPublisher + "', " + true + ")";
		if (dbHandler.executeAction(query)) {
			AlertMaker.getAlertMessage(Alert.AlertType.INFORMATION, "Success", "Successful inserted data of new book!");
		} else {
			AlertMaker.getAlertMessage(Alert.AlertType.ERROR, "Error", "Please Enter a correct inputs!");
		}
	}

	private void updateInfoBook() {
		Book book = new Book(tf_book_id.getText(), tf_book_title.getText(), tf_book_author.getText(), tf_publisher.getText(), true);
		if (dbHandler.updateBookFromDB(book)) {
			AlertMaker.getAlertMessage(Alert.AlertType.INFORMATION, "Success", "Successful Updated data of book selected!");
		}else {
			AlertMaker.getAlertMessage(Alert.AlertType.ERROR, "Error", "Can't Update data of book!!");
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

	public void inflateUI(Book book) {
		tf_book_id.setText(book.getId());
		tf_book_id.setEditable(false);
		tf_book_title.setText(book.getTitle());
		tf_book_author.setText(book.getAuthor());
		tf_publisher.setText(book.getPublisher());
		isEditMode = Boolean.TRUE;
	}
}
