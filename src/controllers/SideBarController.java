package controllers;

import javafx.fxml.FXML;

import java.net.MalformedURLException;

import helpers.LibraryAssistantUtil;
import javafx.event.ActionEvent;

public class SideBarController {

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
	
	// Event Listener on JFXButton.onAction
	@FXML
	public void signoutBTNPushed(ActionEvent event) {
//		((Stage) hb_book_info.getScene().getWindow()).close();
//		LibraryAssistantUtil.loadWindow(null, getClass().getResource("/views/FXMLLogin.fxml"), "Login");
	}
}
