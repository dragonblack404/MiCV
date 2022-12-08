package dad.micv.ui.dialogs;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import dad.micv.model.Experiencia;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ExperienciaDialog extends Dialog<Experiencia> implements Initializable {

	// model

	private ObjectProperty<LocalDate> desde = new SimpleObjectProperty<>();
	private ObjectProperty<LocalDate> hasta = new SimpleObjectProperty<>();
	private StringProperty denominacion = new SimpleStringProperty();
	private StringProperty empleador = new SimpleStringProperty();

	// view

	@FXML
	private TextField denominacionText;

	@FXML
	private DatePicker desdeDate;

	@FXML
	private TextField empleadorText;

	@FXML
	private DatePicker hastaDate;

	@FXML
	private GridPane nuevaExperienciaView;

	public ExperienciaDialog() {
		super();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevaExperienciaView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// init dialog

		ButtonType crearButtonType = new ButtonType("Crear", ButtonData.OK_DONE);

		setTitle("Nuevo experiencia");
		getDialogPane().setContent(nuevaExperienciaView);
		getDialogPane().getButtonTypes().addAll(crearButtonType, ButtonType.CANCEL);

		setResultConverter(this::onConvertResult);

		Button crearButton = (Button) getDialogPane().lookupButton(crearButtonType);

		// bindings

		crearButton.disableProperty()
				.bind(desde.isNull().or(hasta.isNull().or(denominacion.isNull().or(empleador.isNull()))));

		desde.bind(desdeDate.valueProperty());
		hasta.bind(hastaDate.valueProperty());
		denominacion.bind(denominacionText.textProperty());
		empleador.bind(empleadorText.textProperty());

	}
	
	private Experiencia onConvertResult(ButtonType buttonType) {
		if (buttonType.getButtonData() == ButtonData.OK_DONE) {
			Experiencia experiencia = new Experiencia();
			experiencia.setDesde(desde.get());
			experiencia.setHasta(hasta.get());
			experiencia.setDenominacion(denominacion.get());
			experiencia.setEmpleador(empleador.get());
			return experiencia;
		}
		return null;
	}

}
