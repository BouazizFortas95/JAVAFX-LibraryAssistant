/**
 * 
 */
package controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import dbConnect.DBHandler;
import helpers.AlertMaker;
import helpers.LibraryAssistantUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Bouaziz Fortas
 *
 */
public class MainAppController implements Initializable {

	@FXML
	private StackPane sp_root;

	@FXML
	private AnchorPane anchorPane, ap_issue;

	@FXML
	private JFXHamburger hamberger;

	@FXML
	private HBox hb_book_info, hb_user_info;

	@FXML
	private JFXTextField tf_book_id, tf_user_id, tf_searcheBookByID;

	@FXML
	private Label lab_book_name, lab_book_status, lab_author, lab_username, lab_email, lab_mobile, lab_issue_time,
			lab_renew_count, lab_days_elapsed, lab_get_book_title, lab_get_author, lab_get_book_publisher,
			lab_get_username, lab_get_mobile, lab_get_email;

	@FXML
	private JFXButton btn_renew, btn_submission;

	@FXML
	private JFXDrawer drawer;

	DBHandler dbHandler;
	Boolean isReady4Submission = false;
	Boolean isAvail = false;

	@FXML
	void loadAddBookPage(ActionEvent event) throws MalformedURLException {
		LibraryAssistantUtil.loadWindow(null, getClass().getResource("/views/FXMLAddBook.fxml"), "Add Book");
	}

	@FXML
	void loadAddUserPage(ActionEvent event) throws MalformedURLException {
		LibraryAssistantUtil.loadWindow(null, getClass().getResource("/views/FXMLAddUser.fxml"), "Add User");
	}

	@FXML
	void loadSettingsPage(ActionEvent event) throws MalformedURLException {
		LibraryAssistantUtil.loadWindow(null, getClass().getResource("/views/FXMLSettings.fxml"), "Settings");
	}

	@FXML
	void loadShowBooksPage(ActionEvent event) throws MalformedURLException {
		LibraryAssistantUtil.loadWindow(null, getClass().getResource("/views/FXMLBooksTable.fxml"), "Books List");
	}

	@FXML
	void loadShowUsersPage(ActionEvent event) throws MalformedURLException {
		LibraryAssistantUtil.loadWindow(null, getClass().getResource("/views/FXMLUsersTable.fxml"), "Usres List");
	}

	@FXML
	void signoutBTNPushed(ActionEvent event) throws MalformedURLException {
		((Stage) hb_book_info.getScene().getWindow()).close();
		LibraryAssistantUtil.loadWindow(null, getClass().getResource("/views/FXMLLogin.fxml"), "Login");
	}

	@FXML
	void loadFullScreen(ActionEvent event) {
		Stage stage = ((Stage) sp_root.getScene().getWindow());
		stage.setFullScreen(!stage.isFullScreen());
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
				isAvail = rs.getBoolean("isAvail");
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
		lab_book_name.setText("");
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
	public void getInfoBookByID(String book_id) {
		isReady4Submission = false;

		String query = "SELECT * FROM issue WHERE bookID = '" + book_id + "'";
		ResultSet rs = dbHandler.execQuery(query);
		try {
			cardInfo(rs);
		} catch (SQLException e) {
			System.err.println("#Error_Message_getBookInfo4Tab2 : " + e.getLocalizedMessage());
		}
	}

	/**
	 * @param rs
	 * @throws SQLException
	 */
	public void cardInfo(ResultSet rs) throws SQLException {
		if (rs.next()) {
			String query;

			Timestamp issueTime = rs.getTimestamp("issueTime");
			Date dateOfIssue = new Date(issueTime.getTime());
			Long daysElapsed, timeElapsed = System.currentTimeMillis() - issueTime.getTime();
			daysElapsed = TimeUnit.DAYS.convert(timeElapsed, TimeUnit.MILLISECONDS);

			lab_issue_time.setText("Issue Date : " + dateOfIssue.toString());
			lab_renew_count.setText("Renew Count : " + rs.getInt("renew_count"));
			lab_days_elapsed.setText("Days Elapsed : " + daysElapsed.toString());

			query = "SELECT * FROM books WHERE id = '" + rs.getString("bookID") + "'";
			ResultSet rsBook = dbHandler.execQuery(query);
			while (rsBook.next()) {
				lab_get_book_title.setText("Title : " + rsBook.getString("title"));
				lab_get_author.setText("Author : " + rsBook.getString("author"));
				lab_get_book_publisher.setText("Publisher : " + rsBook.getString("publisher"));
			}

			query = "SELECT * FROM users WHERE id = '" + rs.getString("userID") + "'";
			ResultSet rsUser = dbHandler.execQuery(query);
			while (rsUser.next()) {
				lab_get_username.setText("Username : " + rsUser.getString("username"));
				lab_get_mobile.setText("Mobile : " + rsUser.getString("mobile"));
				lab_get_email.setText("Email : " + rsUser.getString("email"));
			}

			isReady4Submission = true;
			controlButtons(isReady4Submission);
		} else {
			clearCards();
		}
	}

	/**
	 * 
	 */
	public void clearCards() {
		AlertMaker.dialogShowMessage(sp_root, anchorPane, tf_searcheBookByID, Arrays.asList(new JFXButton("Okey!")),
				"Worning!", "No such book exists in Issue records!.");
		lab_issue_time.setText("Issue Date");
		lab_renew_count.setText("Renew Count");
		lab_days_elapsed.setText("Days Elapsed");
		lab_get_book_title.setText("Title");
		lab_get_author.setText("Author");
		lab_get_book_publisher.setText("Publisher");
		lab_get_username.setText("Username");
		lab_get_mobile.setText("Mobile");
		lab_get_email.setText("Email");
		controlButtons(isReady4Submission);
	}

	/**
	 * 
	 */
	public void userClearCache() {
		lab_username.setText("");
		lab_mobile.setText("");
		lab_email.setText("");
		tf_user_id.setText(null);
	}

	@FXML
	void issueOperationBTNPUSHED(ActionEvent event) {
		String userID = tf_user_id.getText();
		String bookID = tf_book_id.getText();

		if (lab_book_name.getText() != "" && lab_username.getText() != "" ) {
			if (isAvail) {
				JFXButton yesBtn = new JFXButton("YES");
				yesBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
					String query_1 = "INSERT INTO `issue`(`bookID`, `userID`) VALUES ('" + bookID + "','" + userID + "')";
					String query_2 = "UPDATE `books` SET isAvail = false WHERE id = '" + bookID + "'";
					
					if (dbHandler.executeAction(query_1) && dbHandler.executeAction(query_2)) {
						AlertMaker.dialogShowMessage(sp_root, ap_issue, null, Arrays.asList(new JFXButton("Done!")),
								"SUCCESS!", "Book Issue is Complet.");
					} else {
						AlertMaker.dialogShowMessage(sp_root, ap_issue, null, Arrays.asList(new JFXButton("Okey!")),
								"ERROR!", "Issue Operation is Faild!!");
					}
					
				});

				JFXButton noBtn = new JFXButton("NO");
				noBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
					AlertMaker.dialogShowMessage(sp_root, ap_issue, null, Arrays.asList(new JFXButton("Okey!")), "WARNING!",
							"Issue Operation is Canceled");
				});

				AlertMaker.dialogShowMessage(sp_root, ap_issue, null, Arrays.asList(yesBtn, noBtn),
						"CONFIRMATION!", "Are you sure want to issue te book " + lab_book_name.getText()
								+ "\n to " + lab_username.getText() + " ?");
				
			}else {
				AlertMaker.dialogShowMessage(sp_root, ap_issue, null, Arrays.asList(new JFXButton("Okey!")), "WARNING!",
						"Your selected book is not available, Please try with another book!");
			}
			
			bookClearCache();
			userClearCache();
		} else {
			AlertMaker.dialogShowMessage(sp_root, ap_issue, null, Arrays.asList(new JFXButton("Okey!")), "WARNING!",
					"Your data is not complet, Please try again!");
		}

	}

	@FXML
	void renewBTNPushed(ActionEvent event) {
		if (!isReady4Submission) {
			AlertMaker.getAlertMessage(Alert.AlertType.ERROR, "WARNING!!", "Please select a book to renew!!");
			return;
		}

		JFXButton yesBtn = new JFXButton("YES");
		yesBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
			String issue_query = "UPDATE `issue` SET issueTime = CURRENT_TIMESTAMP, renew_count = renew_count+1  WHERE bookID = '"
					+ tf_searcheBookByID.getText() + "'";
			if (dbHandler.executeAction(issue_query)) {
				AlertMaker.dialogShowMessage(sp_root, anchorPane, null, Arrays.asList(new JFXButton("Done!")), "SUCCESS!", "Book has been Renewed.");
				getInfoBookByID(tf_searcheBookByID.getText());
			} else {
				AlertMaker.dialogShowMessage(sp_root, anchorPane, tf_searcheBookByID, Arrays.asList(new JFXButton("Okey!")), "ERROR!", "Renew has been Faild!");
			}
		});

		JFXButton noBtn = new JFXButton("NO");
		noBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
			AlertMaker.dialogShowMessage(sp_root, ap_issue, null, Arrays.asList(new JFXButton("Okey!")), "WARNING!",
					"Renew Operation is Canceled");
		});
		
		AlertMaker.dialogShowMessage(sp_root, ap_issue, null, Arrays.asList(yesBtn, noBtn),
				"CONFIRMATION!", "Are you sure want to renew the book ?");
	}

	@FXML
	void submissionBTNPushed(ActionEvent event) {
		if (!isReady4Submission) {
			AlertMaker.dialogShowMessage(sp_root, anchorPane, tf_searcheBookByID, Arrays.asList(new JFXButton("Okey!")), "WARNING!", "Please select a book to submit!!");
			return;
		}

		JFXButton yesBtn = new JFXButton("YES");
		yesBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
			String id = tf_searcheBookByID.getText();
			String issue_query = "DELETE FROM `issue` WHERE bookID ='" + id + "'";
			String books_query = "UPDATE `books` SET isAvail = true WHERE id = '" + id + "'";
			if (dbHandler.executeAction(issue_query) && dbHandler.executeAction(books_query)) {
				AlertMaker.dialogShowMessage(sp_root, anchorPane, tf_searcheBookByID, Arrays.asList(new JFXButton("Done!")), "SUCCESS!", "Book has been submitted.");
				clearCards();
			} else {
				AlertMaker.dialogShowMessage(sp_root, anchorPane, tf_searcheBookByID, Arrays.asList(new JFXButton("Okey!")), "ERROR!", "Submission has been Faild!");
			}
		});
		
		JFXButton noBtn = new JFXButton("NO");
		noBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
			AlertMaker.dialogShowMessage(sp_root, ap_issue, null, Arrays.asList(new JFXButton("Okey!")), "WARNING!",
					"Submission Operation is Canceled");
		});
		
		AlertMaker.dialogShowMessage(sp_root, ap_issue, null, Arrays.asList(yesBtn, noBtn),
				"Confirmation!", "Are you sure want to return the book ?");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		JFXDepthManager.setDepth(hb_book_info, 1);
		JFXDepthManager.setDepth(hb_user_info, 1);

		dbHandler = DBHandler.getInstance();

		initDrawer();
		controlButtons(isReady4Submission);
	}

	private void initDrawer() {
		try {
			VBox toolBar = FXMLLoader.load(getClass().getResource("/views/FXMLSideBar.fxml"));
			drawer.setSidePane(toolBar);
			drawer.setDefaultDrawerSize(200);
		} catch (IOException e) {
			System.err.println("#Error_Message_initDrawer : " + e.getLocalizedMessage());
		}

		HamburgerSlideCloseTransition hTransition = new HamburgerSlideCloseTransition(hamberger);
		hTransition.setRate(-1);
		hamberger.addEventHandler(MouseEvent.MOUSE_CLICKED, (ev) -> {

			hTransition.setRate(hTransition.getRate() * -1);
			hTransition.play();

			if (drawer.isClosed()) {
				drawer.open();
			} else {
				drawer.close();
			}
		});
	}

	private void controlButtons(Boolean control) {

		if (control) {
			btn_renew.setDisable(false);
			btn_submission.setDisable(false);
		} else {
			btn_renew.setDisable(true);
			btn_submission.setDisable(true);
		}
	}

}
