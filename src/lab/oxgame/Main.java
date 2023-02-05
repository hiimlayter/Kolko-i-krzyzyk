package lab.oxgame;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lab.oxgame.dao.RozgrywkaDAOImpl;
import lab.oxgame.engine.OXGameImpl;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Main.fxml"));
			loader.setControllerFactory(c -> {
				return new MainController(new RozgrywkaDAOImpl(), new OXGameImpl());
			});
			
			Parent root = loader.load();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			MainController controller = loader.getController();
			primaryStage.setOnCloseRequest(event -> {
				controller.shutdown();
				Platform.exit();
			});
			
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setMinWidth(400);
			primaryStage.setMinHeight(600);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
