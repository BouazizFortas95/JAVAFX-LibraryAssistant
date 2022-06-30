 package application;
	
import dbConnect.DBHandler;
import helpers.LibraryAssistantUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Setting;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/views/FXMLLogin.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Library Assistant Login");
			primaryStage.show();
			
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
