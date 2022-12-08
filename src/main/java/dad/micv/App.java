package dad.micv;

import dad.micv.controller.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application{

	public static Stage primaryStage;
	
	private MainController rootController;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		rootController = new MainController();
		
		App.primaryStage = primaryStage;
		
		primaryStage.setTitle("MiCV");
		primaryStage.getIcons().add(new Image("/images/cv64x64.png"));
		primaryStage.setScene(new Scene(rootController.getView()));
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
