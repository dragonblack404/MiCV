package dad.micv.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import dad.micv.App;
import dad.micv.model.Nacionalidad;
import dad.micv.model.Personal;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class PersonalController implements Initializable {

	// model

	private ObjectProperty<Personal> personal = new SimpleObjectProperty<>();
	private List<String> nacionalidadesCargadas = new ArrayList<>();

	public final ObjectProperty<Personal> personalProperty() {
		return this.personal;
	}

	public final Personal getPersonal() {
		return this.personalProperty().get();
	}

	public final void setPersonal(final Personal personal) {
		this.personalProperty().set(personal);
	}
	
	
	// view

	@FXML
	private Button addButton;

	@FXML
	private TextField apellidosText;

	@FXML
	private TextField codigoPostalText;

	@FXML
	private Button deleteButton;

	@FXML
	private TextArea direccionText;

	@FXML
	private TextField identificacionText;

	@FXML
	private DatePicker fechaNac;

	@FXML
	private TextField localidadText;

	@FXML
	private ListView<Nacionalidad> nacionalidadListView;

	@FXML
	private TextField nombreText;

	@FXML
	private ComboBox<String> paisComboBox;

	@FXML
	private GridPane personalPane;

	public PersonalController() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PersonalView.fxml"));
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

	public GridPane getView() {
		return personalPane;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		

		// load data

		try {
			FileReader paisesFile = new FileReader("csv/paises.csv");
			FileReader nacionalidadesFile = new FileReader("csv/nacionalidades.csv");
			BufferedReader readerPaises = new BufferedReader(paisesFile);
			BufferedReader readerNacionalidades = new BufferedReader(nacionalidadesFile);
			String pais, nacionalidad;

			while ((pais = readerPaises.readLine()) != null) {
				paisComboBox.getItems().add(pais);
			}
			while ((nacionalidad = readerNacionalidades.readLine()) != null) {
				nacionalidadesCargadas.add(nacionalidad);
			}

			readerPaises.close();
			readerNacionalidades.close();

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
		
		// listener

		this.personal.addListener((o, ov, nv) -> onPersonalChanged(o, ov, nv));

	}

	private void onPersonalChanged(ObservableValue<? extends Personal> o, Personal ov, Personal nv) {
		
		if (ov != null) {
			
			identificacionText.textProperty().unbindBidirectional(ov.identificacionProperty());
			nombreText.textProperty().unbindBidirectional(ov.nombreProperty());
			apellidosText.textProperty().unbindBidirectional(ov.apellidosProperty());
			fechaNac.valueProperty().unbindBidirectional(ov.fechaNacimientoProperty());
			direccionText.textProperty().unbindBidirectional(ov.direccionProperty());
			codigoPostalText.textProperty().unbindBidirectional(ov.codigoPostalProperty());
			localidadText.textProperty().unbindBidirectional(ov.localidadProperty());
			ov.paisProperty().unbind();
			nacionalidadListView.itemsProperty().unbind();
			deleteButton.disableProperty().unbind();
		}

		if (nv != null) {
			
			identificacionText.textProperty().bindBidirectional(nv.identificacionProperty());
			nombreText.textProperty().bindBidirectional(nv.nombreProperty());
			apellidosText.textProperty().bindBidirectional(nv.apellidosProperty());
			fechaNac.valueProperty().bindBidirectional(nv.fechaNacimientoProperty());
			direccionText.textProperty().bindBidirectional(nv.direccionProperty());
			codigoPostalText.textProperty().bindBidirectional(nv.codigoPostalProperty());
			localidadText.textProperty().bindBidirectional(nv.localidadProperty());
			paisComboBox.valueProperty().bindBidirectional(nv.paisProperty());
			nacionalidadListView.itemsProperty().bind(nv.nacionalidadesProperty());
			deleteButton.disableProperty()
					.bind(nacionalidadListView.getSelectionModel().selectedItemProperty().isNull());
		}

	}

	@FXML
	void onAddNacionalidadAction(ActionEvent event) {

		ChoiceDialog<String> nacionalidadChoice = new ChoiceDialog<String>(null, nacionalidadesCargadas);
		nacionalidadChoice.initOwner(App.primaryStage);
		nacionalidadChoice.setTitle("Nueva nacionalidad");
		nacionalidadChoice.setHeaderText("Añadir nacionalidad");
		nacionalidadChoice.setContentText("Seleccione una nacionaliad ");
		nacionalidadChoice.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
		String nacionalidad = nacionalidadChoice.showAndWait().orElse(null);

		if (nacionalidad != null) 
			personal.get().getNacionalidades().add(new Nacionalidad(nacionalidad));
	}

	@FXML
	void onQuitarNacionalidadAction(ActionEvent event) {
		personal.get().getNacionalidades().remove(nacionalidadListView.getSelectionModel().getSelectedItem());
	}



}
