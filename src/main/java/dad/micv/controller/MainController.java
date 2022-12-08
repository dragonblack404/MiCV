package dad.micv.controller;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import org.hildan.fxgson.FxGson;

import com.google.gson.Gson;

import dad.micv.App;
import dad.micv.json.LocalDateAdapter;
import dad.micv.model.CV;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;



public class MainController implements Initializable{

	//model
	
	private ObjectProperty<CV> cv = new SimpleObjectProperty<>();
	private File archivoActual;
	private static Gson gson = FxGson.fullBuilder().setPrettyPrinting()
			.registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
	
	
	//controller
	private PersonalController personalController = new PersonalController();
	private ContactoController contactoController = new ContactoController();
	private FormacionController formacionController = new FormacionController();
	private ExperienciaController experienciaController = new ExperienciaController();
	private ConocimientosController conocimientoController = new ConocimientosController();

	
	// view
	
	@FXML
	private MenuItem abrirItem;
	
	@FXML
	private Menu archivoMenu;
	
	@FXML
	private Menu ayudaMenu;
	
	@FXML
	private Tab conocimientosTab;
	
	@FXML
	private Tab contactoTab;
	
	@FXML
	private Tab experienciaTab;
	
	@FXML
	private Tab formacionTab;
	
	@FXML
	private MenuItem guardarComoItem;
	
	@FXML
	private MenuItem guardarItem;
	
	@FXML
	private MenuItem nuevoItem;
	
	@FXML
	private Tab personalTab;
	
	@FXML
	private BorderPane rootPane;
	
	@FXML
	private MenuItem salirItem;
	
	@FXML
	private TabPane tabPane;
	
	
	public MainController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
			loader.setController(this);
			loader.load();
		}catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Ha ocurrido un error");
			alert.setContentText(e.getLocalizedMessage());
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			App.primaryStage.close();
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		personalTab.setContent(personalController.getView());
		contactoTab.setContent(contactoController.getView());
		formacionTab.setContent(formacionController.getView());
		experienciaTab.setContent(experienciaController.getView());
		conocimientosTab.setContent(conocimientoController.getView());
	
		
		//listener

		cv.addListener((o,ov,nv)-> onCVChanged(o,ov,nv));
		cv.set(new CV());
		archivoActual = null;
	}
	
	public BorderPane getView() {
		return rootPane;
	}
	
	public void onCVChanged(ObservableValue<? extends CV> o, CV ov, CV nv) {

		if (ov != null) {
			personalController.personalProperty().unbind();
			contactoController.contactoProperty().unbind();
			formacionController.titulosListProperty().unbind();
			experienciaController.experienciaListProperty().unbind();
			conocimientoController.conocimientosListProperty().unbind();
		}

		if (nv != null) {
			personalController.personalProperty().bind(nv.personalProperty());
			contactoController.contactoProperty().bind(nv.contactoProperty());
			formacionController.titulosListProperty().bind(nv.formacionProperty());
			experienciaController.experienciaListProperty().bind(nv.experienciaProperty());
			conocimientoController.conocimientosListProperty().bind(nv.conocimientosProperty());

		}

	}
	
	
	@FXML
	void onAbrirAction(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Abrir currículum");

			// Agregar filtros
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Todos los archivos", "*.*"),
					new FileChooser.ExtensionFilter("Currículum", "*.cv"));

			File cvFile = fileChooser.showOpenDialog(App.primaryStage);

			if (cvFile != null) {
				cv.set(gson.fromJson(new FileReader(cvFile), CV.class));
				archivoActual = cvFile;				
			}} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Ha ocurrido un error");
				alert.setContentText(e.getLocalizedMessage());
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.showAndWait();
				App.primaryStage.close();
				e.printStackTrace();
			}
	}
	
	@FXML
	void onGuardarAction(ActionEvent event) {
		try {
			if (archivoActual != null) {
				String json = gson.toJson(cv.get(), CV.class);
				Files.writeString(archivoActual.toPath(), json, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
			} else {
				onGuardarComoAction(event);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Ha ocurrido un error");
			alert.setContentText(e.getLocalizedMessage());
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			App.primaryStage.close();
			e.printStackTrace();
		}
	}
	
	@FXML
	void onGuardarComoAction(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Guardar currículum como");

			// Agregar filtros para facilitar la busqueda
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Todos los archivos", "*.*"),
					new FileChooser.ExtensionFilter("Currículum", "*.cv"));

			File cvFile = fileChooser.showSaveDialog(App.primaryStage);

			if (cvFile != null) {
				String json = gson.toJson(cv.get(), CV.class);
				Files.writeString(cvFile.toPath(), json, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
				
				archivoActual = cvFile;
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Ha ocurrido un error");
			alert.setContentText(e.getLocalizedMessage());
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			App.primaryStage.close();
			e.printStackTrace();
		}
	}
	
	@FXML
	void onNuevoAction(ActionEvent event) {
		cv.set(new CV());

	}
	
	@FXML
	void onSalirAction(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Salir");
		alert.setHeaderText(null);
		alert.setContentText("¿Quiere cerrar la aplicación? ");
		Optional<ButtonType> action = alert.showAndWait();

		if (action.get() == ButtonType.OK) {
			App.primaryStage.close();
		}
	}
	
	
	
	
}
