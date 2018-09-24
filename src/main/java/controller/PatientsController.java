package controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
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
import model.Patient;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.textfield.CustomTextField;
import utils.ActionButtonTableCell;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class PatientsController {

    private MenuController menuController;

    @FXML private CustomTextField searchField;
    @FXML private TableView<Patient> patientsTable;

    @FXML private TableColumn<Patient,String> nameColumn;
    @FXML private TableColumn<Patient,String> lastnameColumn;
    @FXML private TableColumn<Patient,String> genderColumn;
    @FXML private TableColumn<Patient,Integer> ageColumn;
    @FXML private TableColumn<Patient, Button> recordColumn;
    @FXML private TableColumn<Patient, Button> deleteColumn;

    private ObservableList<Patient> patients;

    public void init(MenuController menuController){
        this.menuController = menuController;

        patientsTable.setPlaceholder(new Label("Sin resultados"));

        patients = FXCollections.observableList(Patient.find.all());

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        ageColumn.setCellValueFactory(cellData -> {
            Patient patientData = cellData.getValue();
            int years = Period.between(patientData.getBirthdate(), LocalDate.now()).getYears();
            return new SimpleObjectProperty<>(years);
        });

        recordColumn.setCellFactory(ActionButtonTableCell.forTableColumn("Expediente", (Patient patient) -> {
            menuController.showPatientRecord(patient);
            return patient;
        }));

        deleteColumn.setCellFactory(ActionButtonTableCell.forTableColumn("Borrar", (Patient patient) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Borrar paciente");
            alert.setHeaderText(null);
            alert.setContentText("¿Estás seguro que deseas borrar a " + patient.getFullName() + " ?");

            Optional<ButtonType> result = alert.showAndWait();

            result.ifPresent(buttonType -> {
                if(buttonType == ButtonType.OK){
                    patient.delete();
                    patients.remove(patient);
                }
            });

            return patient;
        }));

        FilteredList<Patient> filteredPatients = new FilteredList<>(patients, p -> true);   //Wrap the observable list into a filtered list

        searchField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredPatients.setPredicate(patient -> {
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
                            StringUtils.stripAccents(patient.getName()).toLowerCase().contains(term) ||
                            StringUtils.stripAccents(patient.getLastname()).toLowerCase().contains(term)
                    );
                }

                return match;
            });
        }));

        SortedList<Patient> sortedPatients = new SortedList<>(filteredPatients);        //Wrap the filtered list in a sorted list
        sortedPatients.comparatorProperty().bind(patientsTable.comparatorProperty());   //Bind the sorted list comparator to the table comparator

        patientsTable.setItems(sortedPatients);
    }

    public void newPatient(ActionEvent actionEvent) {
        AtomicBoolean showRecordAfterwards = new AtomicBoolean(false);

        Dialog<Patient> dialog = new Dialog<>();
        dialog.setTitle("Nuevo paciente");
        dialog.setHeaderText(null);

        // Add buttons
        ButtonType addPatientButton = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        ButtonType showMedicalRecordButton = new ButtonType("Expediente");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, showMedicalRecordButton, addPatientButton);

        //Form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Nombre del paciente");

        TextField lastnameField = new TextField();
        lastnameField.setPromptText("Apellidos del paciente");

        DatePicker birthdateField = new DatePicker();

        ToggleGroup genderGroup = new ToggleGroup();
        ToggleButton female = new ToggleButton("F");
        female.setToggleGroup(genderGroup);
        female.setUserData('F');
        female.setSelected(true);
        ToggleButton male = new ToggleButton("M");
        male.setToggleGroup(genderGroup);
        male.setUserData('M');
        SegmentedButton genderSegment = new SegmentedButton();
        genderSegment.getButtons().addAll(female, male);

        TextField bloodTypeField = new TextField();
        bloodTypeField.setPromptText("Grupo sanguíneo");

        TextField emailField = new TextField();
        emailField.setPromptText("Correo electrónico");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Teléfono");

        TextField cellphoneField = new TextField();
        cellphoneField.setPromptText("Teléfono celular");

        grid.add(new Label("Nombre"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Apellido"), 0, 1);
        grid.add(lastnameField, 1, 1);
        grid.add(new Label("Fecha de nacimiento"), 0, 2);
        grid.add(birthdateField, 1, 2);
        grid.add(new Label("Género"), 0, 3);
        grid.add(genderSegment, 1, 3);
        grid.add(new Label("Grupo Sanguíneo"), 0, 4);
        grid.add(bloodTypeField, 1, 4);
        grid.add(new Label("Correo"), 0, 5);
        grid.add(emailField, 1, 5);
        grid.add(new Label("Teléfono"), 0, 6);
        grid.add(phoneField, 1, 6);
        grid.add(new Label("Celular"), 0, 7);
        grid.add(cellphoneField, 1, 7);

        dialog.getDialogPane().setContent(grid);

        //Focus the name field whe starting the dialog
        Platform.runLater(nameField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == addPatientButton || dialogButton == showMedicalRecordButton){
                if (dialogButton == showMedicalRecordButton){
                    showRecordAfterwards.set(true);
                }

                ///Check that all fields are correct
                String name = nameField.getText();
                String lastname = lastnameField.getText();
                char gender = (char) genderSegment.getToggleGroup().getSelectedToggle().getUserData();
                String bloodtype = bloodTypeField.getText();
                LocalDate birthdate = birthdateField.getValue();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String cellphone = cellphoneField.getText();

                if(name.length()==0 || lastname.length()==0 || !(gender=='F' || gender=='M') || bloodtype.length()==0 || birthdate==null){
                    return null;
                }

                return Patient.create(
                        name,
                        lastname,
                        gender,
                        bloodtype,
                        birthdate,
                        email,
                        phone,
                        cellphone
                );
            }

            return null;
        });

        Optional<Patient> result = dialog.showAndWait();

        result.ifPresent(patient -> {
            patients.add(patient);
            patientsTable.refresh();

            if(showRecordAfterwards.get()) {
                menuController.showPatientRecord(patient);
            }
        });
    }
}
