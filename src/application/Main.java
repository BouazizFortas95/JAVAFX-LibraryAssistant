 package application;
	
import dbConnect.DBHandler;
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
			Parent root = FXMLLoader.load(getClass().getResource("/views/FXMLMainApp.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					DBHandler.getInstance();
				}
			}).start();
			
			Setting.getSettings();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}