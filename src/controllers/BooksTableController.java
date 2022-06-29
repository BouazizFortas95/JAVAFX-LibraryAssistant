/**
 * 
 */
package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import dbConnect.DBHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
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
	
	ObservableList<Book> booksList = FXCollections.observableArrayList();

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
