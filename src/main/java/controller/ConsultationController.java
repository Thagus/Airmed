package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.controlsfx.control.textfield.CustomTextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.Optional;

public class ConsultationController {

    @FXML private TextField pressureField;
    @FXML private TextField breathField;
    @FXML private TextField pulseField;
    @FXML private TextField temperatureField;

    @FXML private TextField heightField;
    @FXML private TextField weightField;

    @FXML private TextField awarenessField;
    @FXML private TextField collaborationField;
    @FXML private TextField mobilityField;
    @FXML private TextField attitudeField;
    @FXML private TextField nutritionField;
    @FXML private TextField hydrationField;

    private Consultation consultation;
    private MenuController menuController;

    public void init(MenuController menuController){
        this.menuController = menuController;

        //Add filters to vital sign fields
        pressureField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || !newValue.matches("/")) {
                pressureField.setText(newValue.replaceAll("[^\\d/]", ""));
            }
        });
        breathField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                breathField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        pulseField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                pulseField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        temperatureField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                temperatureField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        //Add numeric only filters to measurement fields
        heightField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                heightField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        weightField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                weightField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void setPatient(Patient patient) {
        //Find if the patient had an appointment that day
        LocalDate today = LocalDate.now();
        Appointment appointment = Appointment.find.query().where().eq("patient", patient).between("dateTime", LocalDateTime.of(today, LocalTime.MIN), LocalDateTime.of(today, LocalTime.MAX)).findOne();

        if(appointment!=null){
            appointment.setAnswered(true);
            appointment.update();
        }

        consultation = new Consultation(patient, LocalDateTime.now());
    }

    public void goToPrescription(ActionEvent actionEvent) {
        String[] pressures = pressureField.getText().split("/");

        int pressureD = 0;
        int pressureS = 0;

        if(pressures.length>=2) {
            pressureD = NumberUtils.toInt(pressures[0], 0);
            pressureS = NumberUtils.toInt(pressures[1], 0);
        }

        //Save data to consultation
        consultation.setVitalSign(VitalSign.create(
                consultation,
                pressureD,
                pressureS,
                NumberUtils.toInt(pulseField.getText(), 0),
                NumberUtils.toInt(temperatureField.getText(), 0),
                NumberUtils.toInt(breathField.getText(), 0)
        ));

        consultation.setMeasurement(Measurement.create(
                consultation,
                NumberUtils.toInt(weightField.getText(), 0),
                NumberUtils.toInt(heightField.getText(), 0)
        ));

        consultation.setExploration(Exploration.create(
                consultation,
                awarenessField.getText(),
                collaborationField.getText(),
                mobilityField.getText(),
                attitudeField.getText(),
                nutritionField.getText(),
                hydrationField.getText()
        ));


        //Begin the prescription stage
        menuController.beginPrescription(consultation);
    }

    public void newConsultation() {
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

        Optional<Patient> patientResult = patientDialog.showAndWait();

        patientResult.ifPresent(menuController::beginConsultation);
    }
}
