package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import jfxtras.scene.control.LocalTimePicker;
import model.Appointment;
import model.Patient;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuController {

    @FXML private StackPane stackPane;

    private HBox calendarView;
    private VBox patientsPane;
    private VBox treatmentsPane;
    private VBox studiesPane;
    private VBox configPane;

    private CalendarController calendarController;
    private PatientsController patientsController;
    private TreatmentsController treatmentsController;
    private StudiesController studiesController;
    private ConfigController configController;

    public void init() throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/view/Calendar.fxml"));
        calendarView = loader.load();
        calendarController = loader.getController();
        calendarController.init();

        loader = new FXMLLoader(getClass().getResource("/view/Patients.fxml"));
        patientsPane = loader.load();
        patientsController = loader.getController();
        patientsController.init();

        loader = new FXMLLoader(getClass().getResource("/view/Treatments.fxml"));
        treatmentsPane = loader.load();
        treatmentsController = loader.getController();
        treatmentsController.init();


        loader = new FXMLLoader(getClass().getResource("/view/Studies.fxml"));
        studiesPane = loader.load();
        studiesController = loader.getController();
        studiesController.init();

        loader = new FXMLLoader(getClass().getResource("/view/Config.fxml"));
        configPane = loader.load();
        configController = loader.getController();
        configController.init();

        stackPane.getChildren().addAll(
                calendarView,
                patientsPane,
                treatmentsPane,
                studiesPane,
                configPane
        );

        hideAll();
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
        ToggleButton male = new ToggleButton("M");
        male.setToggleGroup(genderGroup);
        male.setUserData('M');
        SegmentedButton gender = new SegmentedButton();
        gender.getButtons().addAll(female, male);

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
        grid.add(gender, 1, 3);
        grid.add(new Label("Correo"), 0, 4);
        grid.add(emailField, 1, 4);
        grid.add(new Label("Teléfono"), 0, 5);
        grid.add(phoneField, 1, 5);
        grid.add(new Label("Celular"), 0, 6);
        grid.add(cellphoneField, 1, 6);

        /*
        //Disable buttons that add the patient, to wait for field validation
        Node addButtonNode = dialog.getDialogPane().lookupButton(addPatientButton);
        addButtonNode.setDisable(true);
        Node recordButtonNode = dialog.getDialogPane().lookupButton(showMedicalRecordButton);
        recordButtonNode.setDisable(true);


        Pattern pattern = Pattern.compile("[^A-Za-z0-9 ]");

        ChangeListener<String> changeListener = (observable, oldValue, newValue) -> {
            Matcher m = pattern.matcher(newValue);
            if(!newValue.trim().isEmpty() && !m.find()){
                addButtonNode.setDisable(false);
                recordButtonNode.setDisable(false);
            }
            else {
                addButtonNode.setDisable(true);
                recordButtonNode.setDisable(true);
            }
        };

        //Add validators
        nameField.textProperty().addListener(changeListener);
        lastnameField.textProperty().addListener(changeListener);
*/

        dialog.getDialogPane().setContent(grid);

        //Focus the name field whe starting the dialog
        Platform.runLater(nameField::requestFocus);


        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == addPatientButton || dialogButton == showMedicalRecordButton){
                if (dialogButton == showMedicalRecordButton){
                    showRecordAfterwards.set(true);
                }

                return new Patient(
                        nameField.getText(),
                        lastnameField.getText(),
                        (char) gender.getToggleGroup().getUserData(),
                        birthdateField.getValue(),
                        emailField.getText(),
                        phoneField.getText(),
                        cellphoneField.getText()
                );
            }

            return null;
        });

        Optional<Patient> result = dialog.showAndWait();

        result.ifPresent(patient -> {

        });
    }

    public void newAppointment(ActionEvent actionEvent) {
        Dialog<Appointment> dialog = new Dialog<>();
        dialog.setTitle("Nueva cita");
        dialog.setHeaderText(null);

        // Add buttons
        ButtonType addAppointmentButton = new ButtonType("Agendar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addAppointmentButton, ButtonType.CANCEL);

        //Form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField patientNameField = TextFields.createClearableTextField();
        patientNameField.setEditable(false);

        Button addPatientButton = new Button("+");

        addPatientButton.setOnMouseClicked(event -> {
            Dialog<Patient> patientDialog = new Dialog<>();
            patientDialog.setTitle("Seleccionar paciente");
            patientDialog.setHeaderText(null);

            // Add buttons
            ButtonType selectPatient = new ButtonType("Seleccionar", ButtonBar.ButtonData.OK_DONE);
            patientDialog.getDialogPane().getButtonTypes().addAll(selectPatient, ButtonType.CLOSE);

            //Show the patients list
            VBox vBox = new VBox();

            CustomTextField searchField = new CustomTextField();
            Label searchLabel = new Label();
            searchLabel.setStyle("-fx-padding: 0 2 0 7;");
            FontAwesomeIconView searchIcon = new FontAwesomeIconView();
            searchIcon.setGlyphName("SEARCH");
            searchIcon.setGlyphSize(13);
            searchLabel.setGraphic(searchIcon);
            searchField.setLeft(searchLabel);
            searchField.setPromptText("Buscar");
            searchField.setStyle("-fx-border-radius: 50 50 50 50; -fx-background-radius: 50 50 50 50;");

            HBox hBox = new HBox();
            hBox.getChildren().addAll(new Region(), searchField);

            TableView<Patient> patientTableView = new TableView<>();

            TableColumn<Patient, String> nameColumn = new TableColumn<>("Nombre");
            TableColumn<Patient, String> lastnameColumn = new TableColumn<>("Apellido");
            TableColumn<Patient, Character> genderColumn = new TableColumn<>("Género");
            TableColumn<Patient, Integer> ageColumn = new TableColumn<>("Edad");

            patientTableView.getColumns().addAll(nameColumn, lastnameColumn, genderColumn, ageColumn);

            vBox.getChildren().addAll(hBox, patientTableView);

            patientDialog.getDialogPane().setContent(vBox);

            patientDialog.showAndWait();
        });


        DatePicker dateField = new DatePicker();

        LocalTimePicker timeField = new LocalTimePicker();

        grid.add(new Label("Paciente"), 0, 0);
        grid.add(patientNameField, 1, 0);
        grid.add(addPatientButton, 2, 0);
        grid.add(new Label("Fecha"), 0, 1);
        grid.add(dateField, 1, 1);
        grid.add(new Label("Hora"), 0, 2);
        grid.add(timeField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(patientNameField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == addAppointmentButton){
                return new Appointment(
                        patientNameField.getText(),
                        dateField.getValue(),
                        timeField.getLocalTime()
                );
            }

            return null;
        });

        Optional<Appointment> result = dialog.showAndWait();

        result.ifPresent(appointment -> {

        });
    }

    public void newConsultation(ActionEvent actionEvent) {
        Dialog<Patient> patientDialog = new Dialog<>();
        patientDialog.setTitle("Nueva consulta");
        patientDialog.setHeaderText(null);

        //Add buttons
        ButtonType selectPatient = new ButtonType("Iniciar", ButtonBar.ButtonData.OK_DONE);
        patientDialog.getDialogPane().getButtonTypes().addAll(selectPatient, ButtonType.CLOSE);

        //Show the patients list
        VBox vBox = new VBox();

        CustomTextField searchField = new CustomTextField();
        Label searchLabel = new Label();
        searchLabel.setStyle("-fx-padding: 0 2 0 7;");
        FontAwesomeIconView searchIcon = new FontAwesomeIconView();
        searchIcon.setGlyphName("SEARCH");
        searchIcon.setGlyphSize(13);
        searchLabel.setGraphic(searchIcon);
        searchField.setLeft(searchLabel);
        searchField.setPromptText("Buscar");
        searchField.setStyle("-fx-border-radius: 50 50 50 50; -fx-background-radius: 50 50 50 50;");

        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Region(), searchField);

        TableView<Patient> patientTableView = new TableView<>();

        TableColumn<Patient, String> nameColumn = new TableColumn<>("Nombre");
        TableColumn<Patient, String> lastnameColumn = new TableColumn<>("Apellido");
        TableColumn<Patient, Character> genderColumn = new TableColumn<>("Género");
        TableColumn<Patient, Integer> ageColumn = new TableColumn<>("Edad");

        patientTableView.getColumns().addAll(nameColumn, lastnameColumn, genderColumn, ageColumn);

        vBox.getChildren().addAll(hBox, patientTableView);

        patientDialog.getDialogPane().setContent(vBox);

        patientDialog.showAndWait();
    }

    public void newPrescription(ActionEvent actionEvent) {
        Dialog<Patient> patientDialog = new Dialog<>();
        patientDialog.setTitle("Nueva receta");
        patientDialog.setHeaderText(null);

        //Add buttons
        ButtonType selectPatient = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        patientDialog.getDialogPane().getButtonTypes().addAll(selectPatient, ButtonType.CLOSE);

        //Show the patients list
        VBox vBox = new VBox();

        CustomTextField searchField = new CustomTextField();
        Label searchLabel = new Label();
        searchLabel.setStyle("-fx-padding: 0 2 0 7;");
        FontAwesomeIconView searchIcon = new FontAwesomeIconView();
        searchIcon.setGlyphName("SEARCH");
        searchIcon.setGlyphSize(13);
        searchLabel.setGraphic(searchIcon);
        searchField.setLeft(searchLabel);
        searchField.setPromptText("Buscar");
        searchField.setStyle("-fx-border-radius: 50 50 50 50; -fx-background-radius: 50 50 50 50;");

        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Region(), searchField);

        TableView<Patient> patientTableView = new TableView<>();

        TableColumn<Patient, String> nameColumn = new TableColumn<>("Nombre");
        TableColumn<Patient, String> lastnameColumn = new TableColumn<>("Apellido");
        TableColumn<Patient, Character> genderColumn = new TableColumn<>("Género");
        TableColumn<Patient, Integer> ageColumn = new TableColumn<>("Edad");

        patientTableView.getColumns().addAll(nameColumn, lastnameColumn, genderColumn, ageColumn);

        vBox.getChildren().addAll(hBox, patientTableView);

        patientDialog.getDialogPane().setContent(vBox);

        patientDialog.showAndWait();
    }

    public void showCalendar(ActionEvent actionEvent) {
        hideAll();
        calendarView.setVisible(true);
    }

    public void showPatients(ActionEvent actionEvent) {
        hideAll();
        patientsPane.setVisible(true);
    }

    public void showTreatments(ActionEvent actionEvent) {
        hideAll();
        treatmentsPane.setVisible(true);
    }

    public void showStudies(ActionEvent actionEvent) {
        hideAll();
        studiesPane.setVisible(true);
    }

    public void showConfig(ActionEvent actionEvent) {
        hideAll();
        configPane.setVisible(true);
    }

    private void hideAll(){
        calendarView.setVisible(false);
        patientsPane.setVisible(false);
        treatmentsPane.setVisible(false);
        studiesPane.setVisible(false);
        configPane.setVisible(false);
    }
}
