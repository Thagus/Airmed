package utils;

import controller.MenuController;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.*;
import model.entities.Dose;
import model.entities.Medicine;
import model.entities.Patient;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableFactory {
    private static MenuController menuController;

    public static void init(MenuController menuController){
        TableFactory.menuController = menuController;
    }

    public static TableView<Patient> createPatientTable (VBox vBox){
        CustomTextField searchField = new CustomTextField();
        Label searchLabel = new Label();
        searchLabel.setStyle("-fx-padding: 0 2 0 7;");
        searchField.setLeft(searchLabel);
        searchField.setPromptText("Buscar");

        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Region(), searchField);

        TableView<Patient> patientTableView = new TableView<>();

        TableColumn<Patient, String> nameColumn = new TableColumn<>("Nombre");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Patient, String> lastnameColumn = new TableColumn<>("Apellido");
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        TableColumn<Patient, Character> genderColumn = new TableColumn<>("Sexo");
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        TableColumn<Patient, Integer> ageColumn = new TableColumn<>("Edad");
        ageColumn.setCellValueFactory(cellData -> {
            Patient patientData = cellData.getValue();
            int years = Period.between(patientData.getBirthdate(), LocalDate.now()).getYears();
            return new SimpleObjectProperty<>(years);
        });

        nameColumn.setMinWidth(175);

        lastnameColumn.setMinWidth(175);

        genderColumn.setMinWidth(86);
        genderColumn.setMaxWidth(86);

        ageColumn.setMinWidth(86);
        ageColumn.setMinWidth(86);

        patientTableView.getColumns().addAll(nameColumn, lastnameColumn, genderColumn, ageColumn);

        ObservableList<Patient> patients = FXCollections.observableList(Patient.find.all());
        FilteredList<Patient> filteredPatients = new FilteredList<>(patients, p -> true);   //Wrap the observable list into a filtered list

        searchField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredPatients.setPredicate(patient1 -> {
                //If filter text is empty, display all.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                //Simplify the search string by striping accents and making it lowercase
                String lcSearch = StringUtils.stripAccents(newValue).toLowerCase();

                //Return true if all the search terms are contained in the name or the lastname
                boolean match = true;
                for(String term : lcSearch.split("\\s+")){
                    match = match && (
                            StringUtils.stripAccents(patient1.getName()).toLowerCase().contains(term) ||
                                    StringUtils.stripAccents(patient1.getLastname()).toLowerCase().contains(term)
                    );
                }

                return match;
            });
        }));

        SortedList<Patient> sortedPatients = new SortedList<>(filteredPatients);        //Wrap the filtered list in a sorted list
        sortedPatients.comparatorProperty().bind(patientTableView.comparatorProperty());   //Bind the sorted list comparator to the table comparator
        patientTableView.setItems(sortedPatients);

        vBox.getChildren().addAll(hBox, patientTableView);
        //patientTableView.setStyle("-fx-selection-bar: blue;");

        patientTableView.getSelectionModel().setCellSelectionEnabled(true);

        return patientTableView;
    }

    public static TableView<Dose> createTreatmentDosesTable (VBox vBox, List<Dose> doses){
        if(doses==null){
            doses = new ArrayList<>();
        }
        vBox.setSpacing(5);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        TextField medicineField = new TextField();
        TextFields.bindAutoCompletion(medicineField, AutocompleteBindings.getInstance().getMedicineNames());
        Button addMedicineButton = new Button("+");
        hBox.getChildren().addAll(new Label("Medicina"), medicineField, addMedicineButton);

        //Define table and columns
        TableView<Dose> medicineTable = new TableView<>();
        TableColumn<Dose, String> medicineColumn = new TableColumn<>();
        TableColumn<Dose, String> doseColumn = new TableColumn<>();
        TableColumn<Dose, Button> deleteColumn = new TableColumn<>();

        medicineColumn.setText("Medicina");
        medicineColumn.setMinWidth(100);
        doseColumn.setText("Dósis");
        doseColumn.setMinWidth(175);

        //Table data
        ObservableList<Dose> doseObservableList = FXCollections.observableList(doses);
        medicineTable.setItems(doseObservableList);

        //Column data definitions
        medicineColumn.setCellValueFactory(cellData -> {
            Dose dose = cellData.getValue();
            return new SimpleObjectProperty<>(dose.getMedicine().getName());
        });
        doseColumn.setCellValueFactory(new PropertyValueFactory<>("dose"));
        deleteColumn.setCellFactory(ActionButtonTableCell.forTableColumn("X", (Dose dose) -> {
            doseObservableList.remove(dose);
            return dose;
        }));

        deleteColumn.setMinWidth(86);
        deleteColumn.setMaxWidth(86);

        //Add button action
        addMedicineButton.setOnAction((event -> {
            //Open dialog to fill dose data, based on existing db medicines with autocomplete?
            //Or autocomplete the medicineField, or both
            Dialog<Dose> dialog = new Dialog<>();
            dialog.setTitle("Agregar medicamento");
            dialog.setHeaderText(null);
            menuController.getjMetro().applyTheme(dialog.getDialogPane());

            // Add buttons
            ButtonType addButton = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addButton);

            //Form fields
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField nameField = new TextField();
            nameField.setPromptText("Nombre del medicamento");
            nameField.setText(medicineField.getText());

            TextField doseField = new TextField();
            doseField.setPromptText("Dósis del medicamento");
            //Limit the amount of characters in the text field
            doseField.setTextFormatter(new TextFormatter<String>(change ->
                    change.getControlNewText().length() <= 255 ? change : null));

            if(nameField.getText().length()>0){
                TextFields.bindAutoCompletion(doseField, AutocompleteBindings.getInstance().getDosesForMedicine(nameField.getText()));
            }

            grid.add(new Label("Nombre"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Dósis"), 0, 1);
            grid.add(doseField, 1, 1);

            dialog.getDialogPane().setContent(grid);

            //Focus the name field whe starting the dialog if there's no name
            if(nameField.getText().length()==0)
                Platform.runLater(nameField::requestFocus);
            else
                Platform.runLater(doseField::requestFocus);

            dialog.setResultConverter(dialogButton -> {
                if(dialogButton == addButton){

                    ///Check that all fields are correct
                    String name = nameField.getText();
                    String doseStr = doseField.getText();

                    if(name.length()==0 || doseStr.length()==0){
                        return null;
                    }

                    //Find if the medicine already exists
                    Medicine medicine = Medicine.find.query().where().eq("name", name).findOne();
                    Dose dose = null;

                    //If the medicine exists, try to find an existing dose
                    if(medicine!=null) {
                        dose = Dose.find.query().where().eq("dose", doseStr).eq("medicine", medicine).findOne();
                    }
                    //If the medicine doesn't exist, create it
                    else {
                        medicine = Medicine.create(name);
                        AutocompleteBindings.getInstance().addMedicineName(name);
                    }

                    //If the dose hasn't been found, create it
                    if(dose==null){
                        dose = Dose.create(doseStr, medicine);
                    }

                    return dose;
                }

                return null;
            });

            Optional<Dose> result = dialog.showAndWait();

            result.ifPresent(doseObservableList::add);
        }));

        medicineTable.getColumns().addAll(medicineColumn, doseColumn, deleteColumn);

        vBox.getChildren().addAll(hBox, medicineTable);

        medicineTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return medicineTable;
    }
}