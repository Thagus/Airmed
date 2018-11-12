package controller;

import agenda.AgendaWeekSkin;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.LocalTimePicker;
import jfxtras.scene.control.agenda.Agenda;
import model.Appointment;
import model.Patient;
import model.Setting;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class AgendaController {

    @FXML private ListView<Appointment> todayAppointmentsList;
    @FXML private Agenda agendaView;

    @FXML private Button prevWeekButton;
    @FXML private Button todayButton;
    @FXML private Button nextWeekButton;

    private ObservableList<Appointment> todaysAppointments;

    private MenuController menuController;

    private DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");

    private int consLengthMins = 60;

    public void init(MenuController menuController){
        this.menuController = menuController;
        agendaView.setAllowDragging(false);
        agendaView.setAllowResize(false);

        AgendaWeekSkin agendaWeekSkin = new AgendaWeekSkin(agendaView);
        agendaView.setSkin(agendaWeekSkin);

        agendaView.setDisplayedLocalDateTime(LocalDateTime.now());

        Setting consLength = Setting.find.byId("cons_length");

        //If the setting doesn't exist, create the default one
        if(consLength==null){
            Setting.create("cons_length", "" + consLengthMins);
        }
        else {
            consLengthMins = Integer.parseInt(consLength.getValue());
        }

        todaysAppointments = FXCollections.observableArrayList();

        //Set this week appointment
        setWeekAppointments(LocalDate.now());
        setTodaysAppointments();

        //Make buttons change DisplayedDateTime on the agenda
        prevWeekButton.setOnAction(event -> {
            agendaView.setDisplayedLocalDateTime(agendaView.getDisplayedLocalDateTime().minusWeeks(1));
            //Set the appointments of the week
            setWeekAppointments(agendaView.getDisplayedLocalDateTime().toLocalDate());
        });
        nextWeekButton.setOnAction(event -> {
            agendaView.setDisplayedLocalDateTime(agendaView.getDisplayedLocalDateTime().plusWeeks(1));
            //Set the appointments of the week
            setWeekAppointments(agendaView.getDisplayedLocalDateTime().toLocalDate());
        });
        todayButton.setOnAction(event -> {
            agendaView.setDisplayedLocalDateTime(LocalDateTime.now());
            //Reload this week appointments
            setWeekAppointments(LocalDate.now());
            setTodaysAppointments();
        });

        //Disable de edit popup
        agendaView.setEditAppointmentCallback(param -> null);
        //When double clicked, show edit pane
        agendaView.setActionCallback((appointment) -> {
            Appointment appointmentObj = Appointment.find.byId(Integer.valueOf(appointment.getDescription()));
            if(appointmentObj!=null)
                editAppointment(appointment, appointmentObj);
            return null;
        });

        //Populate list with this week appointments: https://stackoverflow.com/questions/36657299/how-can-i-populate-a-listview-in-javafx-using-custom-objects, https://stackoverflow.com/questions/22542015/how-to-add-a-mouse-doubleclick-event-listener-to-the-cells-of-a-listview-in-java
        todayAppointmentsList.setCellFactory(param -> new ListCell<Appointment>() {
            @Override
            protected void updateItem(Appointment appointment, boolean empty) {
                super.updateItem(appointment, empty);

                if (empty || appointment == null) {
                    setText(null);
                } else {
                    setText(appointment.getPatient().getFullName() + "\n" + appointment.getDateTime().format(hourFormatter));
                }
            }
        });
        todayAppointmentsList.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {
                //Start the double clicked appointment as consultation
                menuController.startAppointment(todayAppointmentsList.getSelectionModel().getSelectedItem());
            }
        });
        todayAppointmentsList.setItems(todaysAppointments);
    }

    private void setTodaysAppointments() {
        //Find the appointments that occur today, excluding those that already should have happened
        todaysAppointments.setAll(
                Appointment.find.query()
                        .where()
                        .between("dateTime",
                                LocalDateTime.of(LocalDate.now(), LocalTime.now().minusMinutes(consLengthMins)),
                                LocalDateTime.of(LocalDate.now(), LocalTime.MAX)).orderBy("dateTime").findList()
        );
    }

    private void setWeekAppointments(LocalDate date){
        agendaView.appointments().clear();

        TemporalField field = WeekFields.of(Locale.getDefault()).dayOfWeek();

        LocalDateTime weekStart = LocalDateTime.of(date.with(field, 1), LocalTime.MIN);
        LocalDateTime weekEnd = LocalDateTime.of(date.with(field, 7), LocalTime.MAX);

        List<Appointment> appointments = Appointment.find.query().where().between("dateTime", weekStart, weekEnd).findList();

        //Add all week appointments to the AgendaView
        for(Appointment appointment : appointments){
            if(appointment.isAnswered() || appointment.getDateTime().isBefore(LocalDateTime.now().minusMinutes(consLengthMins))){   //Appointment that has ended
                agendaView.appointments().add(
                        new Agenda.AppointmentImplLocal()
                                .withStartLocalDateTime(appointment.getDateTime())
                                .withEndLocalDateTime(appointment.getDateTime().plus(consLengthMins-1, ChronoUnit.MINUTES))
                                .withDescription("" + appointment.getId())
                                .withSummary(appointment.getPatient().getFullName())
                                .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group18"))
                );
            }
            else {
                agendaView.appointments().add(
                        new Agenda.AppointmentImplLocal()
                                .withStartLocalDateTime(appointment.getDateTime())
                                .withEndLocalDateTime(appointment.getDateTime().plus(consLengthMins-1, ChronoUnit.MINUTES))
                                .withDescription("" + appointment.getId())
                                .withSummary(appointment.getPatient().getFullName())
                                .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group9"))
                );
            }
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
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            TableColumn<Patient, String> lastnameColumn = new TableColumn<>("Apellido");
            lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
            TableColumn<Patient, Character> genderColumn = new TableColumn<>("Género");
            genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
            TableColumn<Patient, Integer> ageColumn = new TableColumn<>("Edad");
            ageColumn.setCellValueFactory(cellData -> {
                Patient patientData = cellData.getValue();
                int years = Period.between(patientData.getBirthdate(), LocalDate.now()).getYears();
                return new SimpleObjectProperty<>(years);
            });

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

            patientDialog.getDialogPane().setContent(vBox);

            patientDialog.setResultConverter(patientDialogButton -> {
                if(patientDialogButton == selectPatient){
                    //Return the selected patient or an alert if null
                    return patientTableView.getSelectionModel().getSelectedItem();
                }
                return null;
            });

            patientDialog.initOwner(dialog.getOwner());
            Optional<Patient> patientResult = patientDialog.showAndWait();

            patientResult.ifPresent(selectedPatient -> {
                patient.set(selectedPatient);
                patientNameField.setText(selectedPatient.getFullName());
            });
        });


        DatePicker dateField = new DatePicker();

        LocalTimePicker timeField = new LocalTimePicker();
        timeField.setMinuteStep(consLengthMins);

        grid.add(new Label("Paciente"), 0, 0);
        grid.add(patientNameField, 1, 0);
        grid.add(addPatientButton, 2, 0);
        grid.add(new Label("Fecha"), 0, 1);
        grid.add(dateField, 1, 1);
        grid.add(new Label("Hora"), 0, 2);
        grid.add(timeField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(patientNameField::requestFocus);

        final Button addButton = (Button) dialog.getDialogPane().lookupButton(addAppointmentButton);
        //Verify that there isn't an appointment already at that time
        addButton.addEventFilter(ActionEvent.ACTION, event -> {
            LocalDateTime dateTime = LocalDateTime.of(dateField.getValue(), timeField.getLocalTime());

            //Search for an appointment that starts between the desired start and the expected end
            Appointment existingAppointment = Appointment.find.query().where().between("dateTime", dateTime, dateTime.plusMinutes(consLengthMins)).findOne();

            if(existingAppointment!=null){
                //Alert the user that there's already an appointment in that moment
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Conflicto de citas");
                alert.setHeaderText(null);
                alert.setContentText("Ya existe una cita para esa fecha, para el paciente " + existingAppointment.getPatient().getFullName() + ", que empieza a las " + existingAppointment.getDateTime().format(hourFormatter));

                alert.showAndWait();

                event.consume();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == addAppointmentButton){
                LocalDateTime dateTime = LocalDateTime.of(dateField.getValue(), timeField.getLocalTime());
                //Save appointment to database
                return Appointment.create(
                        patient.get(),
                        dateTime
                );
            }

            return null;
        });

        dialog.initOwner(menuController.getPrimaryStage());
        Optional<Appointment> result = dialog.showAndWait();

        result.ifPresent(appointment -> {
                //Add the appointment and update agenda
            if(appointment.isAnswered() || appointment.getDateTime().isBefore(LocalDateTime.now().minusMinutes(consLengthMins))){   //Appointment that has ended
                agendaView.appointments().add(
                        new Agenda.AppointmentImplLocal()
                                .withStartLocalDateTime(appointment.getDateTime())
                                .withEndLocalDateTime(appointment.getDateTime().plus(consLengthMins-1, ChronoUnit.MINUTES))
                                .withDescription("" + appointment.getId())
                                .withSummary(appointment.getPatient().getFullName())
                                .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group18"))
                );
            }
            else {
                agendaView.appointments().add(
                        new Agenda.AppointmentImplLocal()
                                .withStartLocalDateTime(appointment.getDateTime())
                                .withEndLocalDateTime(appointment.getDateTime().plus(consLengthMins-1, ChronoUnit.MINUTES))
                                .withDescription("" + appointment.getId())
                                .withSummary(appointment.getPatient().getFullName())
                                .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group9"))
                );
            }
            agendaView.refresh();

            //If the appointment is today, also refresh the list
            if(appointment.getDateTime().toLocalDate().isEqual(LocalDate.now())){
                setTodaysAppointments();
            }
        });
    }

    private void editAppointment(Agenda.Appointment appointmentInAgenda, Appointment appointmentToEdit){
        //Display dialog to edit or delete an appointment
        Dialog<Appointment> dialog = new Dialog<>();
        dialog.setTitle("Editar cita");
        dialog.setHeaderText(null);

        // Add buttons
        ButtonType addAppointmentButton = new ButtonType("Editar", ButtonBar.ButtonData.OK_DONE);
        ButtonType deleteAppointmentButton = new ButtonType("Borrar", ButtonBar.ButtonData.LEFT);
        dialog.getDialogPane().getButtonTypes().addAll(addAppointmentButton, ButtonType.CANCEL, deleteAppointmentButton);

        //Form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 100, 10, 10));


        DatePicker dateField = new DatePicker();
        LocalTimePicker timeField = new LocalTimePicker();
        timeField.setMinuteStep(consLengthMins);

        dateField.setValue(appointmentToEdit.getDateTime().toLocalDate());
        timeField.setLocalTime(appointmentToEdit.getDateTime().toLocalTime());

        grid.add(new Label("Paciente: " + appointmentToEdit.getPatient().getFullName()), 0, 0, 2, 1);
        grid.add(new Label("Fecha"), 0, 1);
        grid.add(dateField, 1, 1);
        grid.add(new Label("Hora"), 0, 2);
        grid.add(timeField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        final Button deleteButton = (Button) dialog.getDialogPane().lookupButton(deleteAppointmentButton);
        //Verify that there isn't an appointment already at that time
        deleteButton.addEventFilter(ActionEvent.ACTION, event -> {
            //Alert the user that there's already an appointment in that moment
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar cita");
            alert.setHeaderText(null);
            alert.setContentText("¿Esta seguro que desea eliminar esta cita?");

            Optional<ButtonType> result = alert.showAndWait();

            result.ifPresent(buttonType -> {
                if(buttonType == ButtonType.OK){
                    appointmentToEdit.delete();
                    agendaView.appointments().remove(appointmentInAgenda);
                    todaysAppointments.remove(appointmentToEdit);
                }
                else {
                    event.consume();
                }
            });
        });

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == addAppointmentButton){
                LocalDateTime dateTime = LocalDateTime.of(dateField.getValue(), timeField.getLocalTime());
                //Update appointment
                appointmentToEdit.setDateTime(dateTime);
                appointmentToEdit.update();

                appointmentInAgenda.setStartLocalDateTime(dateTime);
                appointmentInAgenda.setEndLocalDateTime(dateTime.plusMinutes(consLengthMins));

                if(dateTime.isBefore(LocalDateTime.now().minusMinutes(consLengthMins))){
                    appointmentInAgenda.setAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group18"));
                }
                else {
                    appointmentInAgenda.setAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group9"));
                }

                return appointmentToEdit;
            }

            return null;
        });

        Optional<Appointment> result = dialog.showAndWait();

        result.ifPresent(appointment -> {
            agendaView.refresh();

            //If the appointment is today, also refresh the list
            if(appointment.getDateTime().toLocalDate().isEqual(LocalDate.now())){
                setTodaysAppointments();
            }
        });

        //Check if the appointment was today or is now today, to update the todayAppointmentsList
        if(appointmentToEdit.getDateTime().toLocalDate().isEqual(LocalDate.now())){
            setTodaysAppointments();
        }
    }
}
