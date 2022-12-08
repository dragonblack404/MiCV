package dad.micv.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.micv.App;
import dad.micv.model.Contacto;
import dad.micv.model.Email;
import dad.micv.model.Telefono;
import dad.micv.model.TipoTelefono;
import dad.micv.model.Web;
import dad.micv.ui.dialogs.EmailDialog;
import dad.micv.ui.dialogs.TelefonoDialog;
import dad.micv.ui.dialogs.WebDialog;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

public class ContactoController implements Initializable {

	// model

	private ObjectProperty<Contacto> contacto = new SimpleObjectProperty<>();

	public final ObjectProperty<Contacto> contactoProperty() {
		return this.contacto;
	}

	public final Contacto getContacto() {
		return this.contactoProperty().get();
	}

	public final void setContacto(final Contacto contacto) {
		this.contactoProperty().set(contacto);
	}

	// view

	@FXML
	private Button addEmailButton;

	@FXML
	private Button addTel;

	@FXML
	private Button addWebButton;

	@FXML
	private Button deleteEmailButton;

	@FXML
	private Button deleteTel;

	@FXML
	private Button deleteWebButton;

	@FXML
	private TitledPane emailPane;

	@FXML
	private TitledPane telefonoPane;

	@FXML
	private TitledPane websPane;

	@FXML
	private BorderPane contactoPane;

	@FXML
	private TableView<Telefono> telTableView;

	@FXML
	private TableColumn<Telefono, String> numColumn;

	@FXML
	private TableColumn<Telefono, TipoTelefono> typeColumn;

	@FXML
	private TableView<Email> emailTableView;

	@FXML
	private TableColumn<Email, String> emailColumn;

	@FXML
	private TableColumn<Web, String> webColumn;

	@FXML
	private TableView<Web> websTableView;

	public ContactoController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ContactoView.fxml"));
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

		// cell value factories

		numColumn.setCellValueFactory(v -> v.getValue().numeroProperty());
		typeColumn.setCellValueFactory(v -> v.getValue().tipoProperty());
		emailColumn.setCellValueFactory(v -> v.getValue().direccionProperty());
		webColumn.setCellValueFactory(v -> v.getValue().urlProperty());

		// cell factories

		numColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		typeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(TipoTelefono.values()));
		emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		webColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		// listeners

		this.contacto.addListener((o, ov, nv) -> onContactoChanged(o, ov, nv));

	}

	public BorderPane getView() {
		return contactoPane;
	}

	public void onContactoChanged(ObservableValue<? extends Contacto> o, Contacto ov, Contacto nv) {

		if (ov != null) {
			telTableView.itemsProperty().unbind();
			deleteTel.disableProperty().unbind();
			emailTableView.itemsProperty().unbind();
			deleteEmailButton.disableProperty().unbind();
			websTableView.itemsProperty().unbind();
			deleteWebButton.disableProperty().unbind();
		}

		if (nv != null) {
			telTableView.itemsProperty().bind(nv.telefonoProperty());
			deleteTel.disableProperty().bind(telTableView.getSelectionModel().selectedItemProperty().isNull());

			emailTableView.itemsProperty().bind(nv.emailProperty());
			deleteEmailButton.disableProperty()
					.bind(emailTableView.getSelectionModel().selectedItemProperty().isNull());

			websTableView.itemsProperty().bind(nv.webProperty());
			deleteWebButton.disableProperty().bind(websTableView.getSelectionModel().selectedItemProperty().isNull());
		}

	}

	@FXML
	void onAddTelAction(ActionEvent event) {

		TelefonoDialog telDialog = new TelefonoDialog();
		telDialog.initOwner(App.primaryStage);
		Telefono telResult = telDialog.showAndWait().orElse(null);
		if (telResult != null) {
			contacto.get().getTelefono().add(telResult);
		}

	}

	@FXML
	void onDeleteTelAction(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Borrar telefono");
		alert.setHeaderText(null);
		alert.setContentText("¿Quiere eliminar el siguiente teléfono? "
				+ telTableView.getSelectionModel().getSelectedItem().getNumero());
		Optional<ButtonType> action = alert.showAndWait();

		if (action.get() == ButtonType.OK)
			contacto.get().telefonoProperty().remove(telTableView.getSelectionModel().getSelectedItem());

	}

	@FXML
	void onAddEmailAction(ActionEvent event) {
		EmailDialog emailDialog = new EmailDialog();
		emailDialog.initOwner(App.primaryStage);
		;
		Email emailResult = emailDialog.showAndWait().orElse(null);
		if (emailResult != null) {
			contacto.get().emailProperty().add(emailResult);
		}
	}

	@FXML
	void onDeleteEmailAction(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Borrar e-mail");
		alert.setHeaderText(null);
		alert.setContentText("¿Quiere eliminar el siguiente e-mail? "
				+ emailTableView.getSelectionModel().getSelectedItem().getDireccion());
		Optional<ButtonType> action = alert.showAndWait();

		if (action.get() == ButtonType.OK)
			contacto.get().emailProperty().remove(emailTableView.getSelectionModel().getSelectedIndex());
	}

	@FXML
	void onAddWeb(ActionEvent event) {
		WebDialog webDialog = new WebDialog();
		webDialog.initOwner(App.primaryStage);
		Web webResult = webDialog.showAndWait().orElse(null);
		if (webResult != null)
			contacto.get().webProperty().add(webResult);

	}

	@FXML
	void onDeleteWeb(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Borrar telefono");
		alert.setHeaderText(null);
		alert.setContentText(
				"¿Quiere eliminar la siguiente web? " + websTableView.getSelectionModel().getSelectedItem().getUrl());
		Optional<ButtonType> action = alert.showAndWait();

		if (action.get() == ButtonType.OK)
			contacto.get().webProperty().remove(websTableView.getSelectionModel().getSelectedIndex());
	}

}
