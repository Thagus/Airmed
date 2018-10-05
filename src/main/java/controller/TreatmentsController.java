package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.Dose;
import model.Treatment;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.CustomTextField;
import utils.ActionButtonTableCell;
import utils.TableFactory;

import java.util.Optional;

public class TreatmentsController {

    @FXML private TableView<Treatment> treatmentsTable;
    @FXML private CustomTextField searchField;

    @FXML private TableColumn<Treatment, String> descriptionColumn;
    @FXML private TableColumn<Treatment, String>  nameColumn;
    @FXML private TableColumn<Treatment, Button>  editColumn;
    @FXML private TableColumn<Treatment, Button>  deleteColumn;

    private ObservableList<Treatment> treatments;

    public void init() {
        treatmentsTable.setPlaceholder(new Label("Sin resultados"));

        treatments = FXCollections.observableList(Treatment.find.all());

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        editColumn.setCellFactory(ActionButtonTableCell.forTableColumn("Editar", (Treatment treatment) -> {
            editTreatment(treatment);
            return treatment;
        }));

        deleteColumn.setCellFactory(ActionButtonTableCell.forTableColumn("Borrar", (Treatment treatment) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Borrar estudio");
            alert.setHeaderText(null);
            alert.setContentText("¿Estás seguro que deseas borrar el estudio " + treatment.getName() + " ?");

            Optional<ButtonType> result = alert.showAndWait();

            result.ifPresent(buttonType -> {
                if(buttonType == ButtonType.OK){
                    treatment.delete();
                    treatments.remove(treatment);
                }
            });

            return treatment;
        }));


        FilteredList<Treatment> filteredTreatments = new FilteredList<>(treatments, p -> true);   //Wrap the observable list into a filtered list

        searchField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredTreatments.setPredicate(treatment -> {
                //If filter text is empty, display all.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                //Simplify the search string by striping accents and making it lowercase
                String lcSearch = StringUtils.stripAccents(newValue).toLowerCase();

                //Return true if all the search terms are contained in the name
                boolean match = true;
                for(String term : lcSearch.split("\\s+")){
                    match = match && (
                            StringUtils.stripAccents(treatment.getName()).toLowerCase().contains(term) ||
                            StringUtils.stripAccents(treatment.getDescription()).toLowerCase().contains(term)
                    );
                }

                return match;
            });
        }));

        SortedList<Treatment> sortedTreatments = new SortedList<>(filteredTreatments);        //Wrap the filtered list in a sorted list
        sortedTreatments.comparatorProperty().bind(treatmentsTable.comparatorProperty());   //Bind the sorted list comparator to the table comparator

        treatmentsTable.setItems(sortedTreatments);
    }

    public void newTreatment(ActionEvent actionEvent) {
        Dialog<Treatment> dialog = new Dialog<>();
        dialog.setTitle("Nuevo tratamiento");
        dialog.setHeaderText(null);

        // Add buttons
        ButtonType addButton = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addButton);

        //Form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Nombre del tratamiento");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Descripción del tratamiento");
        //Limit the amount of characters in the text field
        descriptionField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));
        descriptionField.setWrapText(true);

        grid.add(new Label("Nombre"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Descripción"), 0, 1);
        grid.add(descriptionField, 1, 1);

        VBox vBox = new VBox();
        TableView<Dose> doseTable = TableFactory.createTreatmentDosesTable(vBox, null);

        grid.add(vBox, 0, 2, 2, 1);

        dialog.getDialogPane().setContent(grid);

        //Focus the name field whe starting the dialog
        Platform.runLater(nameField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == addButton){

                ///Check that all fields are correct
                String name = nameField.getText();
                String description = descriptionField.getText();

                if(name.length()==0 || description.length()==0){
                    return null;
                }

                return Treatment.create(
                        name,
                        description,
                        doseTable.getItems()
                );
            }

            return null;
        });

        Optional<Treatment> result = dialog.showAndWait();

        result.ifPresent(treatment -> {
            treatments.add(treatment);
            treatmentsTable.refresh();
        });
    }

    public void editTreatment(Treatment oTreatment){
        Dialog<Treatment> dialog = new Dialog<>();
        dialog.setTitle("Nuevo tratamiento");
        dialog.setHeaderText(null);

        // Add buttons
        ButtonType addButton = new ButtonType("Editar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addButton);

        //Form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Nombre del tratamiento");
        nameField.setText(oTreatment.getName());
        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Descripción del tratamiento");
        descriptionField.setText(oTreatment.getDescription());
        //Limit the amount of characters in the text field
        descriptionField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));
        descriptionField.setWrapText(true);

        grid.add(new Label("Nombre"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Descripción"), 0, 1);
        grid.add(descriptionField, 1, 1);

        VBox vBox = new VBox();
        TableView<Dose> doseTable = TableFactory.createTreatmentDosesTable(vBox, oTreatment.getMedicines());

        grid.add(vBox, 0, 2, 2, 1);

        dialog.getDialogPane().setContent(grid);

        //Focus the name field whe starting the dialog
        Platform.runLater(nameField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == addButton){

                ///Check that all fields are correct
                String name = nameField.getText();
                String description = descriptionField.getText();

                if(name.length()==0 || description.length()==0){
                    return null;
                }

                oTreatment.setName(name);
                oTreatment.setDescription(description);
                oTreatment.setMedicines(doseTable.getItems());

                return oTreatment;
            }

            return null;
        });

        Optional<Treatment> result = dialog.showAndWait();

        result.ifPresent(treatment -> {
            oTreatment.update();
            treatmentsTable.refresh();
        });
    }
}
