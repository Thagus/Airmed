package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.LocalTimePicker;
import jfxtras.scene.control.agenda.Agenda;
import model.Appointment;
import model.Patient;
import model.Setting;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class AgendaController {

    @FXML private Agenda agendaView;

    @FXML private CustomTextField searchField;

    @FXML private CalendarPicker calendarPicker;

    private int consLengthMins = 30;

    public void init(){
        //Load week agenda
        LocalDate now = LocalDate.now();
        TemporalField field = WeekFields.of(Locale.getDefault()).dayOfWeek();
        
        LocalDate weekStart = now.with(field, 1);
        LocalDate weekEnd = now.with(field, 7);

        List<Appointment> appointments = Appointment.find.query().where().between("dateTime", weekStart, weekEnd).findList();

        Setting consLength = Setting.find.byId("cons_length");

        //If the setting doesn't exist, create the default one
        if(consLength==null){
            Setting.create("cons_length", "" + consLengthMins);
        }
        else {
            consLengthMins = Integer.parseInt(consLength.getValue());
        }

        //Add all week appointments to the AgendaView
        for(Appointment appointment:appointments){
            agendaView.appointments().add(
                    new Agenda.AppointmentImplLocal()
                    .withStartLocalDateTime(appointment.getDateTime())
                    .withEndLocalDateTime(appointment.getDateTime().plus(consLengthMins, ChronoUnit.MINUTES))
                    .withDescription(appointment.getPatient().getFullName())
            );
        }
    }

    public void newAppointment(){
        Dialog<Appointment> dialog = new Dialog<>();
        dialog.setTitle("Nueva cita");
        dialog.setHeaderText(null);

        AtomicReference<Patient> patient = new AtomicReference<>();

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

            patientDialog.setResultConverter(patientDialogButton -> {
                if(patientDialogButton == selectPatient){
                    //Return the selected patient or an alert if null
                    return Patient.create(
                            "Jane",
                            "Doe",
                            'F',
                            "0+",
                            LocalDate.now(),
                            "",
                            "",
                            ""
                    );
                }
                return null;
            });

            Optional<Patient> patientResult = patientDialog.showAndWait();

            patientResult.ifPresent(patient::set);
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
                return Appointment.create(
                        patient.get(),
                        LocalDateTime.of(dateField.getValue(), timeField.getLocalTime())
                );
            }

            return null;
        });

        Optional<Appointment> result = dialog.showAndWait();

        result.ifPresent(appointment -> {
            //Save appointment and update agenda
        });
    }
}
