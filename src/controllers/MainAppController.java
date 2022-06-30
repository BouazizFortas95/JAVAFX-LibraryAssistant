/**
 * 
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;

import dbConnect.DBHandler;
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
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Bouaziz Fortas
 *
 */
public class MainAppController implements Initializable {

	@FXML
	private StackPane sp_root;

	@FXML
	private HBox hb_book_info, hb_user_info;

	@FXML
	private JFXTextField tf_book_id, tf_user_id, tf_searcheBookByID;

	@FXML
	private Label lab_book_name, lab_book_status, lab_author, lab_username, lab_email, lab_mobile;

	@FXML
	private JFXListView<String> lv_issue_data;

	DBHandler dbHandler;
	Boolean isReady4Submission = false;

	@FXML
	void loadAddBookPage(ActionEvent event) {
		loadWindow("/views/FXMLAddBook.fxml", "Add Book");
	}

	@FXML
	void loadAddUserPage(ActionEvent event) {
		loadWindow("/views/FXMLAddUser.fxml", "Add User");
	}

	@FXML
	void loadSettingsPage(ActionEvent event) {
		loadWindow("/views/FXMLSettings.fxml", "Settings");
	}

	@FXML
	void loadShowBooksPage(ActionEvent event) {
		loadWindow("/views/FXMLBooksTable.fxml", "Books List");
	}

	@FXML
	void loadShowUsersPage(ActionEvent event) {
		loadWindow("/views/FXMLUsersTable.fxml", "Usres List");
	}

	@FXML
    void loadFullScreen(ActionEvent event) {
		Stage stage = ((Stage) sp_root.getScene().getWindow());
		stage.setFullScreen(!stage.isFullScreen());
    }

	@FXML
	void signoutBTNPushed(ActionEvent event) {
		((Stage) hb_book_info.getScene().getWindow()).close();
		loadWindow("/views/FXMLLogin.fxml", "Login");
	}

	@FXML
	void menu_closeBTNPushed(ActionEvent event) {
		((Stage) hb_book_info.getScene().getWindow()).close();
	}

	@FXML
	void getBookInfo(ActionEvent event) {
		String book_id = tf_book_id.getText();
		String query = "SELECT * FROM books WHERE id = '" + book_id + "'";
		ResultSet rs = dbHandler.execQuery(query);
		Boolean flag = false;
		try {
			while (rs.next()) {
				String title = rs.getString("title");
				String author = rs.getString("author");
				Boolean isAvail = rs.getBoolean("isAvail");
				flag = true;

				lab_book_name.setText(title);
				lab_author.setText(author);
				String status = (isAvail) ? "Available" : "Not Available";
				lab_book_status.setText(status);
			}

			if (!flag) {
				bookClearCache();
			}
		} catch (SQLException e) {
			System.err.println("#Error_Message_getBookInfo : " + e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 */
	public void bookClearCache() {
		lab_book_name.setText("No Such Book Available!!");
		lab_author.setText("");
		lab_book_status.setText("");
		tf_book_id.setText(null);
	}

	@FXML
	void getUserInfo(ActionEvent event) {
		String user_id = tf_user_id.getText();
		String query = "SELECT * FROM users WHERE id = '" + user_id + "'";
		ResultSet rs = dbHandler.execQuery(query);
		Boolean flag = false;
		try {
			while (rs.next()) {
				String username = rs.getString("username");
				String mobile = rs.getString("mobile");
				String email = rs.getString("email");
				flag = true;

				lab_username.setText(username);
				lab_mobile.setText(mobile);
				lab_email.setText(email);
			}

			if (!flag) {
				userClearCache();
			}
		} catch (SQLException e) {
			System.err.println("#Error_Message_getBookInfo : " + e.getLocalizedMessage());
		}
	}

	@FXML
	void getBookInfo4Tab2(ActionEvent event) {
		String book_id = tf_searcheBookByID.getText();
		getInfoBookByID(book_id);
	}

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void getInfoBookByID(String book_id) {
		isReady4Submission = false;

		String query = "SELECT * FROM issue WHERE bookID = '" + book_id + "'";
		ResultSet rs = dbHandler.execQuery(query);

		ObservableList<String> issueList = FXCollections.observableArrayList();
		try {
			while (rs.next()) {
				String mBook_id = book_id;
				String mUser_id = rs.getString("userID");
				Timestamp mIssueTime = rs.getTimestamp("issueTime");
				int mRenewCount = rs.getInt("renew_count");
				issueList.add("---------------------------------Issue Information :---------------------------------");
				issueList.add("\tIssue Date and Time : " + mIssueTime.toGMTString());
				issueList.add("\tRenew Count : " + mRenewCount);
				issueList.add("---------------------------------Book Information :---------------------------------");

				query = "SELECT * FROM books WHERE id = '" + mBook_id + "'";
				ResultSet rsBook = dbHandler.execQuery(query);
				while (rsBook.next()) {
					issueList.add("\tBook ID : " + rsBook.getString("id"));
					issueList.add("\tTitle : " + rsBook.getString("title"));
					issueList.add("\tAuthor : " + rsBook.getString("author"));
					issueList.add("\tPublisher : " + rsBook.getString("publisher"));
				}

				issueList.add("---------------------------------User Information :---------------------------------");
				query = "SELECT * FROM users WHERE id = '" + mUser_id + "'";
				ResultSet rsUser = dbHandler.execQuery(query);
				while (rsUser.next()) {
					issueList.add("\tUser ID : " + rsUser.getString("id"));
					issueList.add("\tUsername : " + rsUser.getString("username"));
					issueList.add("\tMobile : " + rsUser.getString("mobile"));
					issueList.add("\tEmail : " + rsUser.getString("email"));
				}
				isReady4Submission = true;
			}
		} catch (SQLException e) {
			System.err.println("#Error_Message_getBookInfo4Tab2 : " + e.getLocalizedMessage());
		}
		lv_issue_data.getItems().setAll(issueList);
	}

	/**
	 * 
	 */
	public void userClearCache() {
		lab_username.setText("No Such User exist!!");
		lab_mobile.setText("");
		lab_email.setText("");
		tf_user_id.setText(null);
	}

	@FXML
	void issueOperationBTNPUSHED(ActionEvent event) {
		String userID = tf_user_id.getText();
		String bookID = tf_book_id.getText();

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Confirm Issue Operation");
		alert.setContentText("Are you sure want to issue te book " + lab_book_name.getText() + "\n to "
				+ lab_username.getText() + " ?");
		Optional<ButtonType> response = alert.showAndWait();

		if (response.get() == ButtonType.OK) {
			String query_1 = "INSERT INTO `issue`(`bookID`, `userID`) VALUES ('" + bookID + "','" + userID + "')";
			String query_2 = "UPDATE `books` SET isAvail = false WHERE id = '" + bookID + "'";
			if (dbHandler.executeAction(query_1) && dbHandler.executeAction(query_2)) {
				Alert alert_info = new Alert(Alert.AlertType.INFORMATION);
				alert_info.setHeaderText(null);
				alert_info.setTitle("Successful");
				alert_info.setContentText("Book Issue is Complet.");
				alert_info.showAndWait();
			} else {
				Alert alert_err = new Alert(Alert.AlertType.ERROR);
				alert_err.setHeaderText(null);
				alert_err.setTitle("Error");
				alert_err.setContentText("Issue Operation is Faild!!");
				alert_err.showAndWait();
			}
		} else {
			Alert alert_info = new Alert(Alert.AlertType.INFORMATION);
			alert_info.setHeaderText(null);
			alert_info.setTitle("Cancelled");
			alert_info.setContentText("Book Issue is Canceled.");
			alert_info.showAndWait();
		}
		bookClearCache();
		userClearCache();

	}

	@FXML
	void renewBTNPushed(ActionEvent event) {
		if (!isReady4Submission) {
			Alert alert_err = new Alert(Alert.AlertType.ERROR);
			alert_err.setHeaderText(null);
			alert_err.setTitle("Failed");
			alert_err.setContentText("Please select a book to renew!!");
			alert_err.showAndWait();
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Confirm Renew Operation");
		alert.setContentText("Are you sure want to renew the book ?");
		Optional<ButtonType> response = alert.showAndWait();

		if (response.get() == ButtonType.OK) {
			String issue_query = "UPDATE `issue` SET issueTime = CURRENT_TIMESTAMP, renew_count = renew_count+1  WHERE bookID = '"
					+ tf_searcheBookByID.getText() + "'";
			if (dbHandler.executeAction(issue_query)) {
				Alert alert_info = new Alert(Alert.AlertType.INFORMATION);
				alert_info.setHeaderText(null);
				alert_info.setTitle("Success");
				alert_info.setContentText("Book has been Renewed.");
				alert_info.showAndWait();
				getInfoBookByID(tf_searcheBookByID.getText());
			} else {
				Alert alert_err = new Alert(Alert.AlertType.ERROR);
				alert_err.setHeaderText(null);
				alert_err.setTitle("Failed");
				alert_err.setContentText("Renew has been Faild!!");
				alert_err.showAndWait();
			}
		} else {
			Alert alert_info = new Alert(Alert.AlertType.INFORMATION);
			alert_info.setHeaderText(null);
			alert_info.setTitle("Cancelled");
			alert_info.setContentText("Renew Operation cancelled!.");
			alert_info.showAndWait();
		}
	}

	@FXML
	void submissionBTNPushed(ActionEvent event) {
		if (!isReady4Submission) {
			Alert alert_err = new Alert(Alert.AlertType.ERROR);
			alert_err.setHeaderText(null);
			alert_err.setTitle("Failed");
			alert_err.setContentText("Please select a book to submit!!");
			alert_err.showAndWait();
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Confirm Issue Operation");
		alert.setContentText("Are you sure want to return the book ?");
		Optional<ButtonType> response = alert.showAndWait();

		if (response.get() == ButtonType.OK) {
			String id = tf_searcheBookByID.getText();
			String issue_query = "DELETE FROM `issue` WHERE bookID ='" + id + "'";
			String books_query = "UPDATE `books` SET isAvail = true WHERE id = '" + id + "'";

			if (dbHandler.executeAction(issue_query) && dbHandler.executeAction(books_query)) {
				Alert alert_info = new Alert(Alert.AlertType.INFORMATION);
				alert_info.setHeaderText(null);
				alert_info.setTitle("Success");
				alert_info.setContentText("Book has been submitted.");
				alert_info.showAndWait();
				lv_issue_data.getItems().clear();
			} else {
				Alert alert_err = new Alert(Alert.AlertType.ERROR);
				alert_err.setHeaderText(null);
				alert_err.setTitle("Failed");
				alert_err.setContentText("Submission has been Faild!!");
				alert_err.showAndWait();
			}
		} else {
			Alert alert_info = new Alert(Alert.AlertType.INFORMATION);
			alert_info.setHeaderText(null);
			alert_info.setTitle("Cancelled");
			alert_info.setContentText("Submission Operation cancelled!.");
			alert_info.showAndWait();
			lv_issue_data.getItems().clear();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		JFXDepthManager.setDepth(hb_book_info, 1);
		JFXDepthManager.setDepth(hb_user_info, 1);

		dbHandler = DBHandler.getInstance();
	}

	private void loadWindow(String path, String title) {
		// TODO Auto-generated method stub
		try {
			Parent parent = FXMLLoader.load(getClass().getResource(path));// FXMLLoader.load(getClass().getResource("/views/FXMLMainApp.fxml"));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle(title);
			stage.setScene(new Scene(parent));
			stage.show();
			

			LibraryAssistantUtil.setStageIcon(stage);
		} catch (IOException e) {
			System.err.println("#Error_Message_loadPage : " + e.getLocalizedMessage());
		}
	}

}
