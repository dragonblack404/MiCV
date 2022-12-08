package dad.micv.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.micv.App;
import dad.micv.model.Titulo;
import dad.micv.ui.dialogs.TituloDialog;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.converter.LocalDateStringConverter;

public class FormacionController implements Initializable {

	// model

	private ListProperty<Titulo> titulosList = new SimpleListProperty<>(FXCollections.observableArrayList());

	public final ListProperty<Titulo> titulosListProperty() {
		return this.titulosList;
	}

	public final ObservableList<Titulo> getTitulosList() {
		return this.titulosListProperty().get();
	}

	public final void setTitulosList(final ObservableList<Titulo> titulosList) {
		this.titulosListProperty().set(titulosList);
	}

	// view

	@FXML
	private Button addFormacion;

	@FXML
	private Button deleteFormacion;

	@FXML
	private TableView<Titulo> formacionTableView;

	@FXML
	private TableColumn<Titulo, String> denominacionColumn;

	@FXML
	private TableColumn<Titulo, LocalDate> desdeColumn;

	@FXML
	private HBox formacionView;

	@FXML
	private TableColumn<Titulo, LocalDate> hastaColumn;

	@FXML
	private TableColumn<Titulo, String> organizadorColumn;

	public FormacionController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FormacionView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Ha ocurrido un error");
			alert.setContentText(e.getLocalizedMessage());
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// bindings

		formacionTableView.itemsProperty().bind(titulosList);
		deleteFormacion.disableProperty().bind(formacionTableView.getSelectionModel().selectedItemProperty().isNull());
		;

		// cell value factories

		desdeColumn.setCellValueFactory(v -> v.getValue().desdeProperty());
		hastaColumn.setCellValueFactory(v -> v.getValue().hastaProperty());
		denominacionColumn.setCellValueFactory(v -> v.getValue().denominacionProperty());
		organizadorColumn.setCellValueFactory(v -> v.getValue().organizadorProperty());

		// cell factories

		desdeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
		hastaColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
		denominacionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		organizadorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	}

	public HBox getView() {
		return formacionView;
	}

	@FXML
	public void onAddFormacionAction(ActionEvent event) {
		TituloDialog tituloDialog = new TituloDialog();
		tituloDialog.initOwner(App.primaryStage);
		Titulo tituloResult = tituloDialog.showAndWait().orElse(null);
		if (tituloResult != null)
			titulosList.add(tituloResult);
	}

	@FXML
	void onDeleteFormacionAction(ActionEvent event) {

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Borrar título");
		alert.setHeaderText(null);
		alert.setContentText("¿Quiere eliminar el siguiente título? "
				+ formacionTableView.getSelectionModel().getSelectedItem().getDenominacion());
		Optional<ButtonType> action = alert.showAndWait();

		if (action.get() == ButtonType.OK)
			titulosList.get().remove(formacionTableView.getSelectionModel().getSelectedIndex());
	}

}
