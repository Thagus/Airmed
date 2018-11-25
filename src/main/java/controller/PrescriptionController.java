package controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.print.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.*;
import org.controlsfx.control.textfield.TextFields;
import utils.ActionButtonTableCell;
import utils.AutocompleteBindings;
import utils.TableFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrescriptionController {
    @FXML private TextArea notesArea;

    @FXML private TextField treatmentField;
    @FXML private TableView<Treatment> treatmentsTable;
    @FXML private TableColumn<Treatment, String> treatmentNameColumn;
    @FXML private TableColumn<Treatment, Button> deleteTreatmentColumn;

    @FXML private TextField studyField;
    @FXML private TableView<Study> studiesTable;
    @FXML private TableColumn<Study, String> studyNameColumn;
    @FXML private TableColumn<Study, Button> deleteStudyColumn;

    @FXML private TextField medicineField;
    @FXML private TableView<Dose> medicinesTable;
    @FXML private TableColumn<Dose, String> medicineNameColumn;
    @FXML private TableColumn<Dose, String> doseColumn;
    @FXML private TableColumn<Dose, Button> deleteMedicineColumn;

    @FXML private Button printConsultationButton;

    private Consultation consultation;
    private Prescription prescription;

    private MenuController menuController;

    private boolean showPrescription;

    private ObservableList<Dose> medicines;
    private ObservableList<Study> studies;
    private ObservableList<Treatment> treatments;

    public void init(MenuController menuController, VBox prescriptionPane){
        this.menuController = menuController;
        this.showPrescription = false;

        //Confirm prescription exit without saving
        prescriptionPane.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue && !showPrescription && consultation!=null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Guardar");
                alert.setHeaderText(null);
                alert.setContentText("¿Desea guardar la consulta?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    savePrescription(null);
                }
            }
        });

        notesArea.setFocusTraversable(false);

        medicines = FXCollections.observableArrayList();
        medicineNameColumn.setCellValueFactory(cellData -> {
            Dose dose = cellData.getValue();
            return new SimpleObjectProperty<>(dose.getMedicine().getName());
        });
        doseColumn.setCellValueFactory(new PropertyValueFactory<>("dose"));
        deleteMedicineColumn.setCellFactory(ActionButtonTableCell.forTableColumn("X", (Dose dose) -> {
            medicines.remove(dose);
            return dose;
        }));
        medicineNameColumn.setMinWidth(200);
        doseColumn.setMinWidth(200);
        medicinesTable.setItems(medicines);

        studies = FXCollections.observableArrayList();
        studyNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        deleteStudyColumn.setCellFactory(ActionButtonTableCell.forTableColumn("X", (Study study) -> {
            studies.remove(study);
            return study;
        }));
        studyNameColumn.setMinWidth(300);
        studiesTable.setItems(studies);

        treatments = FXCollections.observableArrayList();
        treatmentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        deleteTreatmentColumn.setCellFactory(ActionButtonTableCell.forTableColumn("X", (Treatment treatment) -> {
            treatments.remove(treatment);
            return treatment;
        }));
        treatmentNameColumn.setMinWidth(300);
        treatmentsTable.setItems(treatments);


        TextFields.bindAutoCompletion(treatmentField, AutocompleteBindings.getInstance().getTreatmentNames());
        TextFields.bindAutoCompletion(studyField, AutocompleteBindings.getInstance().getStudyNames());
        TextFields.bindAutoCompletion(medicineField, AutocompleteBindings.getInstance().getMedicineNames());

        notesArea.setWrapText(true);
    }

    public void setPatient(Patient patient) {
        this.showPrescription = false;
        clearFields();
        this.consultation = Consultation.create(patient, LocalDateTime.now());
        this.prescription = Prescription.create(patient);
        this.consultation.setPrescription(prescription);
    }

    public void setConsultation(Consultation consultation) {
        this.showPrescription = false;
        clearFields();
        this.consultation = consultation;
        this.prescription = Prescription.create(consultation.getPatient());
        this.consultation.setPrescription(prescription);
    }

    public void savePrescription(ActionEvent actionEvent) {
        //Get data
        prescription.setNotes(notesArea.getText());
        prescription.setMedicines(medicines);
        prescription.setTreatments(treatments);
        prescription.setStudies(studies);

        if(!showPrescription) {
            //Save to database
            prescription.save();
            consultation.save();

            prescription = null;
            consultation = null;
            clearFields();

            //Return to the agenda
            menuController.showAgenda(null);
        }
        else {
            //Save to database
            prescription.update();
            consultation.update();

            Patient patient = consultation.getPatient();

            prescription = null;
            consultation = null;
            clearFields();

            //Return to the patient record
            menuController.showPatientRecord(patient);
        }


    }

    public void printPrescription(ActionEvent actionEvent) {
        prescription.setNotes(notesArea.getText());
        prescription.setMedicines(medicines);
        prescription.setTreatments(treatments);
        prescription.setStudies(studies);

        //Show printing dialog
        VBox prescriptionPane = new VBox();
        prescriptionPane.setSpacing(10);

        prescriptionPane.getChildren().addAll(
                new Label(LocalDate.now().format(DateTimeFormatter.ISO_DATE)),
                new Label(prescription.getPatient().getFullName()),
                new Label()
        );

        List<Dose> doses = new ArrayList<>(prescription.getMedicines());
        for(Treatment treatment : prescription.getTreatments()){
            doses.addAll(treatment.getMedicines());
        }

        for(Dose dose : doses) {
            Label label = new Label(dose.getMedicine().getName() + "\n- " + dose.getDose());
            label.setWrapText(true);
            prescriptionPane.getChildren().add(label);
        }

        for(Study study : prescription.getStudies()){
            Label label = new Label(study.getName() + "\n" + study.getDescription());
            label.setWrapText(true);
            prescriptionPane.getChildren().add(label);
        }

        //Add notes if they exist
        if(prescription.getNotes()!=null && prescription.getNotes().length()>0) {
            Label label = new Label(prescription.getNotes());
            label.setWrapText(true);
            prescriptionPane.getChildren().add(label);
        }

        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(menuController.getPrimaryStage().getOwner())){
            prescriptionPane.setPrefWidth(printerJob.getJobSettings().getPageLayout().getPrintableWidth());
            boolean success = printerJob.printPage(prescriptionPane);
            if (success) {
                printerJob.endJob();
            }
        }
    }

    public void printConsultation(ActionEvent actionEvent) {
        prescription.setNotes(notesArea.getText());
        prescription.setMedicines(medicines);
        prescription.setTreatments(treatments);
        prescription.setStudies(studies);

        if(consultation!=null)
            consultation.print(menuController);
    }

    public void showPrescription(Consultation consultation) {
        this.showPrescription = true;
        clearFields();

        this.consultation = consultation;
        this.prescription = consultation.getPrescription();

        //Fill prescription fields
        notesArea.setText(prescription.getNotes());

        medicines.setAll(prescription.getMedicines());
        studies.setAll(prescription.getStudies());
        treatments.setAll(prescription.getTreatments());

        medicinesTable.refresh();
        studiesTable.refresh();
        treatmentsTable.refresh();
    }

    public void addMedicine(ActionEvent actionEvent) {
        Dialog<Dose> dialog = new Dialog<>();
        dialog.setTitle("Agregar medicamento");
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

        result.ifPresent((dose -> {
            medicines.add(dose);
            medicinesTable.refresh();
        }));
    }

    public void addStudy(ActionEvent actionEvent) {
        String studyString = studyField.getText();

        if(studyString.length()==0){
            return;
        }

        Study study = Study.find.query().where().ieq("name", studyString.toLowerCase()).findOne();

        if(study==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Estudio inválido");
            alert.setHeaderText(null);
            alert.setContentText("Cree primero el estudio para poder recetarlo");
        }
        else {
            studies.add(study);
            studiesTable.refresh();
        }
    }

    public void addTreatment(ActionEvent actionEvent) {
        String treatmentString = treatmentField.getText();

        if(treatmentString.length()==0){
            return;
        }

        Treatment treatment = Treatment.find.query().where().ieq("name", treatmentString.toLowerCase()).findOne();

        if(treatment==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Tratamiento inválido");
            alert.setHeaderText(null);
            alert.setContentText("Cree primero el tratamiento para poder recetarlo");
        }
        else {
            treatments.add(treatment);
            treatmentsTable.refresh();
        }
    }

    public void newPrescription() {
        Dialog<Patient> patientDialog = new Dialog<>();
        patientDialog.setTitle("Nueva receta");
        patientDialog.setHeaderText(null);

        //Add buttons
        ButtonType selectPatient = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        patientDialog.getDialogPane().getButtonTypes().addAll(selectPatient, ButtonType.CLOSE);

        //Show the patients list
        VBox vBox = new VBox();
        TableView<Patient> patientTableView = TableFactory.createPatientTable(vBox);
        patientDialog.getDialogPane().setContent(vBox);

        patientDialog.setResultConverter(patientDialogButton -> {
            if(patientDialogButton == selectPatient){
                //Return the selected patient or an alert if null
                return patientTableView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        Optional<Patient> patientResult = patientDialog.showAndWait();

        patientResult.ifPresent(menuController::beginPrescription);
    }

    private void clearFields(){
        medicines.clear();
        studies.clear();
        treatments.clear();

        notesArea.clear();
        medicineField.clear();
        studyField.clear();
        treatmentField.clear();
    }


}
