package dad.micv.ui.dialogs;

import java.net.URL;
import java.util.ResourceBundle;

import dad.micv.model.Web;
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

public class WebDialog extends Dialog<Web> implements Initializable {

	// model

	private StringProperty url = new SimpleStringProperty();

	// view

	@FXML
    private GridPane nuevaWebView;

    @FXML
    private TextField webText;

	public WebDialog() {
			super();
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NuevaWeb.fxml"));
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

		setTitle("Nueva web");
		setHeaderText("Crear una nueva dirección web.");
		getDialogPane().setContent(nuevaWebView);
		getDialogPane().getButtonTypes().addAll(crearButtonType, ButtonType.CANCEL);

		setResultConverter(this::onConvertResult);

		Button crearButton = (Button) getDialogPane().lookupButton(crearButtonType);
		crearButton.disableProperty().bind(url.isNull());

		// bindings

		url.bind(webText.textProperty());

	}

	private Web onConvertResult(ButtonType buttonType) {
		if (buttonType.getButtonData() == ButtonData.OK_DONE) {
			Web web = new Web();
			web.setUrl(url.get());
			return web;
		}
		return null;
	}

}