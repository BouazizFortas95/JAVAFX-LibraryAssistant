 package application;
	
import dbConnect.DBHandler;
import helpers.LibraryAssistantUtil;
import javafx.application.Application;
import javafx.stage.Stage;
import models.Setting;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			LibraryAssistantUtil.loadWindow(primaryStage, getClass().getResource("/views/FXMLLogin.fxml"), "Library Assistant - Login");
			LibraryAssistantUtil.setStageIcon(primaryStage);
			new Thread(new Runnable() {
				
				@Override
				public void run() { 
					DBHandler.getInstance();
				}
			}).start();
			
			Setting.initConfig();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
