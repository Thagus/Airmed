package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.Patient;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.textfield.CustomTextField;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class PatientsController {

    private MenuController menuController;

    @FXML private CustomTextField searchField;
    @FXML private TableView patientsTable;

    @FXML private TableColumn nameColumn;
    @FXML private TableColumn lastnameColumn;
    @FXML private TableColumn gender;
    @FXML private TableColumn recordColumn;
    @FXML private TableColumn consultationColumn;
    @FXML private TableColumn deleteColumn;

    public void init(MenuController menuController){
        this.menuController = menuController;
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
            //Save patient to database

            if(showRecordAfterwards.get()) {
                menuController.showPatientRecord(patient);
            }
        });
    }
}
