package dad.micv.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.micv.App;
import dad.micv.model.Experiencia;
import dad.micv.ui.dialogs.ExperienciaDialog;
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

public class ExperienciaController implements Initializable {

	// model

	private ListProperty<Experiencia> experienciaList = new SimpleListProperty<>(FXCollections.observableArrayList());

	public final ListProperty<Experiencia> experienciaListProperty() {
		return this.experienciaList;
	}

	public final ObservableList<Experiencia> getExperienciaList() {
		return this.experienciaListProperty().get();
	}

	public final void setExperienciaList(final ObservableList<Experiencia> experienciaList) {
		this.experienciaListProperty().set(experienciaList);
	}

	public ExperienciaController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ExperienciaView.fxml"));
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

	// view

	@FXML
	private Button addButton;

	@FXML
	private Button deleteButton;

	@FXML
	private TableView<Experiencia> experienciaTableView;

	@FXML
	private TableColumn<Experiencia, String> denominacionColumn;

	@FXML
	private TableColumn<Experiencia, String> empleadorColumn;

	@FXML
	private TableColumn<Experiencia, LocalDate> desdeColumn;

	@FXML
	private TableColumn<Experiencia, LocalDate> hastaColumn;

	@FXML
	private HBox experienciaView;

	@FXML
	void onAddExperiencia(ActionEvent event) {
		ExperienciaDialog experienciaDialog = new ExperienciaDialog();
		experienciaDialog.initOwner(App.primaryStage);
		Experiencia experienciaResult = experienciaDialog.showAndWait().orElse(null);
		if (experienciaResult != null)
			experienciaList.add(experienciaResult);
	}
	
	@FXML
	void onDeleteExperiencia(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Borrar experiencia");
		alert.setHeaderText(null);
		alert.setContentText("¿Quiere eliminar la siguiente experiencia? "
				+ experienciaTableView.getSelectionModel().getSelectedItem().getDenominacion());
		Optional<ButtonType> action = alert.showAndWait();
		
		if (action.get() == ButtonType.OK)
			experienciaList.get().remove(experienciaTableView.getSelectionModel().getSelectedIndex());
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// bindings

		experienciaTableView.itemsProperty().bind(experienciaList);
		deleteButton.disableProperty().bind(experienciaTableView.getSelectionModel().selectedItemProperty().isNull());

		// cell value factories

		desdeColumn.setCellValueFactory(v -> v.getValue().desdeProperty());
		hastaColumn.setCellValueFactory(v -> v.getValue().hastaProperty());
		denominacionColumn.setCellValueFactory(v -> v.getValue().denominacionProperty());
		empleadorColumn.setCellValueFactory(v -> v.getValue().empleadorProperty());

		// cell factories

		desdeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
		hastaColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
		denominacionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		empleadorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	}

	public HBox getView() {
		return experienciaView;
	}


}
