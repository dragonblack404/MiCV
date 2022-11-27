package dad.micv.ui.dialogs;
 
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.micv.model.Telefono;
import dad.micv.model.TipoTelefono;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
 
public class TelefonoDialog extends Dialog<Telefono> implements Initializable {
 
	// model
 
	private StringProperty numero = new SimpleStringProperty();
	private ObjectProperty<TipoTelefono> tipo = new SimpleObjectProperty<>();
 
	// view
 
    @FXML
    private TextField numeroText;
 
    @FXML
    private ComboBox<TipoTelefono> tipoCombo;
 
    @FXML
    private GridPane view;
 
	public TelefonoDialog() {
		super();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevoTelefono.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
 
		// init dialog
 
		ButtonType crearButtonType = new ButtonType("Añadir", ButtonData.OK_DONE);
 
		setTitle("Nuevo teléfono");
		setHeaderText("Introduzca el nuevo número de teléfono");
		getDialogPane().setContent(view);
		getDialogPane().getButtonTypes().addAll(crearButtonType, ButtonType.CANCEL);
 
		setResultConverter(this::onConvertResult);
 
		Button crearButton = (Button) getDialogPane().lookupButton(crearButtonType);
		crearButton.disableProperty().bind(numero.isEmpty().or(tipo.isNull()));
 
		// bindings
 
		numero.bind(numeroText.textProperty());
		tipo.bind(tipoCombo.getSelectionModel().selectedItemProperty());
 
		// load combo
 
		tipoCombo.getItems().setAll(TipoTelefono.values());
 
	}
 
	private Telefono onConvertResult(ButtonType buttonType) {
		if (buttonType.getButtonData() == ButtonData.OK_DONE) {
			Telefono telefono = new Telefono();
			telefono.setNumero(numero.get());
			telefono.setTipo(tipo.get());
			return telefono;
		}
		return null;
	}
 
}
 