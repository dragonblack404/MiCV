package dad.micv.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.micv.App;
import dad.micv.model.Conocimiento;
import dad.micv.model.Idioma;
import dad.micv.model.Nivel;
import dad.micv.ui.dialogs.ConocimientoDialog;
import dad.micv.ui.dialogs.IdiomaDialog;
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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class ConocimientosController implements Initializable {

	// model

	private ListProperty<Conocimiento> conocimientosList = new SimpleListProperty<>(
			FXCollections.observableArrayList());

	public final ListProperty<Conocimiento> conocimientosListProperty() {
		return this.conocimientosList;
	}

	public final ObservableList<Conocimiento> getConocimientosList() {
		return this.conocimientosListProperty().get();
	}

	public final void setConocimientosList(final ObservableList<Conocimiento> conocimientosList) {
		this.conocimientosListProperty().set(conocimientosList);
	}

	// view

	@FXML
	private Button addConocimientoButton;

	@FXML
	private Button addIdiomaButton;

	@FXML
	private TableView<Conocimiento> conocimientosTableView;

	@FXML
	private HBox conocimientosView;

	@FXML
	private Button eliminarButton;

	@FXML
	private TableColumn<Conocimiento, String> denominacionColumn;

	@FXML
	private TableColumn<Conocimiento, Nivel> nivelColumn;

	public ConocimientosController() {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ConocimientosView.fxml"));
			loader.setController(this);
			loader.load();

		} catch (IOException e) {

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

		conocimientosTableView.itemsProperty().bind(conocimientosList);
		eliminarButton.disableProperty()
				.bind(conocimientosTableView.getSelectionModel().selectedItemProperty().isNull());

		// cell value factories

		denominacionColumn.setCellValueFactory(v -> v.getValue().denominacionProperty());
		nivelColumn.setCellValueFactory(v -> v.getValue().nivelProperty());

		// cell factories

		denominacionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nivelColumn.setCellFactory(ComboBoxTableCell.forTableColumn(Nivel.values()));

	}

	HBox getView() {
		return conocimientosView;
	}

	@FXML
	void onAddConocimientoAction(ActionEvent event) {
		ConocimientoDialog conDialog = new ConocimientoDialog();
		conDialog.initOwner(App.primaryStage);
		Conocimiento conocimientoResult = conDialog.showAndWait().orElse(null);
		if(conocimientoResult != null)
			conocimientosList.add(conocimientoResult);
	}

	@FXML
	void onAddIdiomaAction(ActionEvent event) {
		IdiomaDialog idiomaDialog = new IdiomaDialog();
		idiomaDialog.initOwner(App.primaryStage);
		Idioma idiomaResult = idiomaDialog.showAndWait().orElse(null);
		if(idiomaResult != null) 
			conocimientosList.add(idiomaResult);
	}

	@FXML
	void onEliminarAction(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Borrar título");
		alert.setHeaderText(null);
		alert.setContentText("¿Quiere eliminar el siguiente conocimiento? "
				+ conocimientosTableView.getSelectionModel().getSelectedItem().getDenominacion());
		Optional<ButtonType> action = alert.showAndWait();

		if (action.get() == ButtonType.OK) {

			conocimientosList.get().remove(conocimientosTableView.getSelectionModel().getSelectedIndex());

		}
	}

}
