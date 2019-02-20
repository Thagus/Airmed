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
import model.Study;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.CustomTextField;
import utils.ActionButtonTableCell;
import utils.AutocompleteBindings;

import java.util.Optional;

public class StudiesController {

    @FXML private CustomTextField searchField;

    @FXML private TableView<Study> studiesTable;

    @FXML private TableColumn<Study, String> nameColumn;
    @FXML private TableColumn<Study, String> descriptionColumn;
    @FXML private TableColumn<Study, Button> editColumn;
    @FXML private TableColumn<Study, Button> deleteColumn;

    private ObservableList<Study> studies;

    public void init() {
        studiesTable.setPlaceholder(new Label("Sin resultados"));

        studies = FXCollections.observableList(Study.find.all());

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        editColumn.setCellFactory(ActionButtonTableCell.forTableColumn("Editar", (Study study) -> {
            editStudy(study);
            return study;
        }));

        deleteColumn.setCellFactory(ActionButtonTableCell.forTableColumn("Borrar", (Study study) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Borrar estudio");
            alert.setHeaderText(null);
            alert.setContentText("¿Estás seguro que deseas borrar el estudio " + study.getName() + " ?");

            Optional<ButtonType> result = alert.showAndWait();

            result.ifPresent(buttonType -> {
                if(buttonType == ButtonType.OK){
                    AutocompleteBindings.getInstance().removeStudyName(study.getName());
                    study.delete();
                    studies.remove(study);
                }
            });

            return study;
        }));


        FilteredList<Study> filteredStudies = new FilteredList<>(studies, p -> true);   //Wrap the observable list into a filtered list

        searchField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredStudies.setPredicate(study -> {
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
                            StringUtils.stripAccents(study.getName()).toLowerCase().contains(term) ||
                            StringUtils.stripAccents(study.getDescription()).toLowerCase().contains(term)
                    );
                }

                return match;
            });
        }));

        SortedList<Study> sortedStudies = new SortedList<>(filteredStudies);        //Wrap the filtered list in a sorted list
        sortedStudies.comparatorProperty().bind(studiesTable.comparatorProperty());   //Bind the sorted list comparator to the table comparator

        studiesTable.setItems(sortedStudies);

        studiesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void newStudy(ActionEvent actionEvent) {
        Dialog<Study> dialog = new Dialog<>();
        dialog.setTitle("Nuevo estudio");
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
        nameField.setPromptText("Nombre del estudio");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Descripción del estudio");
        //Limit the amount of characters in the text field
        descriptionField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));
        descriptionField.setWrapText(true);

        grid.add(new Label("Nombre"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Descripción"), 0, 1);
        grid.add(descriptionField, 1, 1);

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

                return Study.create(
                        name,
                        description
                );
            }

            return null;
        });

        Optional<Study> result = dialog.showAndWait();

        result.ifPresent(study -> {
            AutocompleteBindings.getInstance().addStudyName(study.getName());
            studies.add(study);
            studiesTable.refresh();
        });
    }

    private void editStudy(Study study){
        Dialog<Study> dialog = new Dialog<>();
        dialog.setTitle("Nuevo estudio");
        dialog.setHeaderText(null);

        // Add buttons
        ButtonType addPatientButton = new ButtonType("Editar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addPatientButton);

        //Form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Nombre del estudio");
        nameField.setText(study.getName());
        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Descripción del estudio");
        descriptionField.setText(study.getDescription());
        //Limit the amount of characters in the text field
        descriptionField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));
        descriptionField.setWrapText(true);

        grid.add(new Label("Nombre"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Descripción"), 0, 1);
        grid.add(descriptionField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        //Focus the name field whe starting the dialog
        Platform.runLater(nameField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == addPatientButton){

                ///Check that all fields are correct
                String name = nameField.getText();
                String description = descriptionField.getText();

                if(name.length()==0 || description.length()==0){
                    return null;
                }

                String prevName = null;
                if(!study.getName().equals(name)){
                    prevName = study.getName();
                }

                study.setName(name);
                study.setDescription(description);

                try {
                    study.update();
                    if(prevName!=null){
                        AutocompleteBindings.getInstance().removeStudyName(prevName);
                        AutocompleteBindings.getInstance().addStudyName(name);
                    }
                    return study;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            return null;
        });

        Optional<Study> result = dialog.showAndWait();

        result.ifPresent(s -> studiesTable.refresh());
    }
}
