/**
 * 
 */
package helpers;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Bouaziz Fortas
 *
 */
public class LibraryAssistantUtil {
	public static final String LOGO_PATH = "/images/library Assitant.png";

	public static void setStageIcon(Stage stage) {
		stage.getIcons().add(new Image(LOGO_PATH));
	}

	public static void loadWindow(Stage parentStage, URL path, String title) {
		// TODO Auto-generated method stub
		try {
			Parent parent = FXMLLoader.load(path);
			Stage stage = null;
			if (parentStage!=null) {
				stage = parentStage;
			} else {
				stage = new Stage(StageStyle.DECORATED);
			}
			stage.setTitle(title);
			stage.setScene(new Scene(parent));
			stage.show();
			setStageIcon(stage);
		} catch (IOException e) {
			System.err.println("#Error_Message_loadPage : " + e.getLocalizedMessage());
		}
	}
}
