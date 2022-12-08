package dad.micv.ui.dialogs;

import java.net.URL;
import java.util.ResourceBundle;

import dad.micv.model.Email;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class EmailDialog extends Dialog<Email> implements Initializable {

	// model

	private StringProperty direccion = new SimpleStringProperty();

	// view
	
	@FXML
	private TextField direccionText;

	@FXML
	private GridPane nuevoEmailView;

	public EmailDialog() {
		super();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevoEmail.fxml"));
			loader.setController(this);
			loader.load();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// init dialog

		ButtonType crearButtonType = new ButtonType("Añadir", ButtonData.OK_DONE);

		setTitle("Nuevo e-mail");
		setHeaderText("Crear una nueva dirección de correo.");
		getDialogPane().setContent(nuevoEmailView);
		getDialogPane().getButtonTypes().addAll(crearButtonType, ButtonType.CANCEL);

		setResultConverter(this::onConvertResult);

		Button crearButton = (Button) getDialogPane().lookupButton(crearButtonType);
		crearButton.disableProperty().bind(direccion.isNull());

		// bindings

		direccion.bind(direccionText.textProperty());

	}

	private Email onConvertResult(ButtonType buttonType) {
		if (buttonType.getButtonData() == ButtonData.OK_DONE) {
			Email email = new Email();
			email.setDireccion(direccion.get());
			return email;
		}
		return null;
	}

}
