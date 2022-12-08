package dad.micv.ui.dialogs;

import java.net.URL;
import java.util.ResourceBundle;

import dad.micv.App;
import dad.micv.model.Conocimiento;
import dad.micv.model.Nivel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class ConocimientoDialog extends Dialog<Conocimiento> implements Initializable {

	// model
	
	private StringProperty denominacion = new SimpleStringProperty();
	private ObjectProperty<Nivel> nivel = new SimpleObjectProperty<>();
	
	// view

	@FXML
	private TextField denominacionField;

	@FXML
	private ComboBox<Nivel> nivelBox;

	@FXML
	private GridPane nuevoConocimientoView;

	@FXML
	private Button resetNivelButton;

	public ConocimientoDialog() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevoConocimientoView.fxml"));
			loader.setController(this);
			loader.load();
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// init dialog

		ButtonType crearButtonType = new ButtonType("Crear", ButtonData.OK_DONE);

		setTitle("Nuevo conocimiento");
		getDialogPane().setContent(nuevoConocimientoView);
		getDialogPane().getButtonTypes().addAll(crearButtonType, ButtonType.CANCEL);

		setResultConverter(this::onConvertResult);

		Button crearButton = (Button) getDialogPane().lookupButton(crearButtonType);
		crearButton.disableProperty().bind(denominacion.isEmpty().or(nivel.isNull()));

		// bindings

		denominacion.bind(denominacionField.textProperty());
		nivel.bindBidirectional(nivelBox.valueProperty());

		// load combo

		nivelBox.getItems().setAll(Nivel.values());

	}
	
	private Conocimiento onConvertResult(ButtonType buttonType) {
		if (buttonType.getButtonData() == ButtonData.OK_DONE) {
			Conocimiento conocimiento = new Conocimiento();
			conocimiento.setDenominacion(denominacion.get());
			conocimiento.setNivel(nivel.get());
			return conocimiento;
		}
		return null;
	}

	@FXML
	void onResetNivelAction(ActionEvent event) {

		nivel.setValue(null);
		
	}

}
