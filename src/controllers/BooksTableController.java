/**
 * 
 */
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Book;

/**
 * @author Bouaziz Fortas
 *
 */
public class BooksTableController implements Initializable {

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TableView<Book> tv_books;

	@FXML
	private TableColumn<Book, String> col_id;

	@FXML
	private TableColumn<Book, String> col_title;

	@FXML
	private TableColumn<Book, String> col_author;

	@FXML
	private TableColumn<Book, String> col_publisher;

	@FXML
	private TableColumn<Book, Boolean> col_availability;

	@FXML
	private MenuItem cmi_delete;

	ObservableList<Book> booksList = FXCollections.observableArrayList();
	Book book;

	// Event Listener on MenuItem.onAction
	@FXML
	public void refreshBooksTableBTNPushed(ActionEvent event) {
		loadDataBooksTableFromDB();
	}

	// Event Listener on MenuItem.onAction
	@FXML
	void editBookBTNPushed(ActionEvent event) {
		Book selected_book = tv_books.getSelectionModel().getSelectedItem();

		if (selected_book == null) {
			AlertMaker.getAlertMessage(Alert.AlertType.ERROR, "Error", "Please select a book for deletion.");
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FXMLAddBook.fxml"));
			Parent parent = loader.load();
			AddBookController controller = (AddBookController) loader.getController();
			controller.inflateUI(selected_book);
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Edit Book");
			stage.setScene(new Scene(parent));
			stage.show();
			LibraryAssistantUtil.setStageIcon(stage);
			stage.setOnCloseRequest((e)->{
				refreshBooksTableBTNPushed(new ActionEvent());
			});
		} catch (IOException e) {
			System.err.println("#Error_Message_editBookBTNPushed : " + e.getLocalizedMessage());
		}
	}

	// Event Listener on MenuItem.onAction
	@FXML
	void deleteBookBTNPushed(ActionEvent event) {
		Book selected_book = tv_books.getSelectionModel().getSelectedItem();

		if (selected_book == null) {
			AlertMaker.getAlertMessage(Alert.AlertType.ERROR, "Error", "Please select a book for deletion.");
			return;
		}

		if (DBHandler.getInstance().isBookAlreadyIssued(selected_book)) {
			AlertMaker.getAlertMessage(Alert.AlertType.ERROR, "Error",
					"This book is already issued and can't be deleted.");
			return;
		}
		Optional<ButtonType> response = AlertMaker.getAlertMessageConfirmation("Deleting Book",
				"Are you sure want to delete the book?");
		if (response.get() == ButtonType.OK) {
			Boolean isDeleted = DBHandler.getInstance().deleteBookFromDB(selected_book);
			if (isDeleted) {
				AlertMaker.getAlertMessage(Alert.AlertType.INFORMATION, "Success",
						"The selected book has been deleted successfull.");
				tv_books.getItems().remove(selected_book);
			} else {
				AlertMaker.getAlertMessage(Alert.AlertType.ERROR, "Error",
						selected_book.getTitle() + " could not be deleted!");
			}

		} else {
			AlertMaker.getAlertMessage(Alert.AlertType.ERROR, "Cancelled", "Deletion book process is cancelled");
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		initBooksTableView();
		loadDataBooksTableFromDB();
	}

	private void loadDataBooksTableFromDB() {
		// TODO Auto-generated method stub
		DBHandler dbHandler = DBHandler.getInstance();
		String query = "SELECT * FROM books";
		ResultSet rs = dbHandler.execQuery(query);
		try {
			booksList.clear();
			while (rs.next()) {
				String id = rs.getString("id");
				String title = rs.getString("title");
				String author = rs.getString("author");
				String publisher = rs.getString("publisher");
				Boolean isAvail = rs.getBoolean("isAvail");

				booksList.add(new Book(id, title, author, publisher, isAvail));
			}
		} catch (SQLException e) {
			System.err.println("#Error_Message_loadDataBooksTableFromDB : " + e.getLocalizedMessage());
		}
		tv_books.getItems().setAll(booksList);
	}

	private void initBooksTableView() {
		// TODO Auto-generated method stub
		col_id.setCellValueFactory(new PropertyValueFactory<Book, String>("id"));
		col_title.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		col_author.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
		col_publisher.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));
		col_availability.setCellValueFactory(new PropertyValueFactory<Book, Boolean>("isAvail"));
	}

}
