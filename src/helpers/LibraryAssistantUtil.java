/**
 * 
 */
package helpers;

import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author Bouaziz Fortas
 *
 */
public class LibraryAssistantUtil {
	public static final String LOGO_PATH = "/images/library Assitant.png";
	
	public static void setStageIcon(Stage stage) {
		stage.getIcons().add(new Image(LOGO_PATH));
	}
}
