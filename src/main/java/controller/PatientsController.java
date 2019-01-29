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
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import model.Consultation;
import model.Patient;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.textfield.CustomTextField;
import utils.ActionButtonTableCell;
import utils.IMCUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
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
            return new SimpleObjectProperty<>(patientData.getAge());
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

        genderSegment.getToggleGroup().selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });

        TextField bloodTypeField = new TextField();
        bloodTypeField.setPromptText("Grupo sanguíneo");
        bloodTypeField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 4 ? change : null));

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

                if(name.length()==0 || lastname.length()==0 || !(gender=='F' || gender=='M') || birthdate==null){
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

    public void exportConsultations(ActionEvent actionEvent) {
        //Create dialog to define the date range to export
        Dialog<Workbook> dialog = new Dialog<>();
        dialog.setTitle("Exportar consultas a excel");
        dialog.setHeaderText(null);

        ButtonType generateButton = new ButtonType("Generar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, generateButton);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        DatePicker startDateField = new DatePicker();
        DatePicker endDateField = new DatePicker();

        grid.add(new Label("Fecha de inicio"), 0, 0);
        grid.add(startDateField, 0, 1);
        grid.add(new Label("Fecha de fin"), 1 ,0);
        grid.add(endDateField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == generateButton){
                LocalDate startDate = startDateField.getValue();
                LocalDate endDate = endDateField.getValue();

                //Fetch consultations and patient basic data
                List<Consultation> consultationList = Consultation.find.query()
                        .fetch("patient", "name,lastname,gender,birthdate")
                        .fetch("vitalSign")
                        .where()
                        .ge("dateTime", startDate)
                        .le("dateTime", endDate)
                        .findList();

                //Create excel file with the information, calculating imc and interpretation
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Consultas");

                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 12);

                CellStyle headerCellStyle = workbook.createCellStyle();
                headerCellStyle.setFont(headerFont);

                // Create header row
                String[] columns = {"Fecha", "Hora", "Nombre Completo", "Género", "Edad", "Diagnóstico", "Altura (m)", "Peso (kg)", "IMC", "Interpretación IMC", "Presión (S/D)", "Respiración (/min)", "Pulso (/min)", "Temperatura (°C)", "Glucosa (mg)", "Hemoglobina (%)", "Colesterol (%)", "Triglicéridos (%)"};
                Row headerRow = sheet.createRow(0);

                for (int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);
                    cell.setCellStyle(headerCellStyle);
                }

                //Autosize diagnostic column, so it doesn't autosize on the actual diagnostics size
                sheet.autoSizeColumn(5);

                int rowNum = 1;

                for(Consultation consultation : consultationList){
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(consultation.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    row.createCell(1).setCellValue(consultation.getDateTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
                    row.createCell(2).setCellValue(consultation.getPatient().getFullName());
                    row.createCell(3).setCellValue(consultation.getPatient().getGender());
                    row.createCell(4).setCellValue(consultation.getPatient().getAge());
                    row.createCell(5).setCellValue(consultation.getDiagnostic());
                    if(consultation.getMeasurement()!=null) {
                        row.createCell(6).setCellValue(BigDecimal.valueOf(consultation.getMeasurement().getHeight()).setScale(2, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(100)).toPlainString());
                        row.createCell(7).setCellValue(BigDecimal.valueOf(consultation.getMeasurement().getWeight()).setScale(1, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(1000)).toPlainString());
                        row.createCell(8).setCellValue(IMCUtils.calculateIMC(consultation.getMeasurement().getWeight(), consultation.getMeasurement().getHeight()).toPlainString());
                        row.createCell(9).setCellValue(IMCUtils.interpretIMC(IMCUtils.calculateIMC(consultation.getMeasurement().getWeight(), consultation.getMeasurement().getHeight()), consultation.getPatient().getBirthdate()));
                    }
                    if(consultation.getVitalSign()!=null) {
                        row.createCell(10).setCellValue(consultation.getVitalSign().getPressureS() + "/" + consultation.getVitalSign().getPressureD());
                        row.createCell(11).setCellValue(consultation.getVitalSign().getBreath());
                        row.createCell(12).setCellValue(consultation.getVitalSign().getPulse());
                        row.createCell(13).setCellValue(consultation.getVitalSign().getTemperature());
                        row.createCell(14).setCellValue(consultation.getVitalSign().getGlucose());
                        row.createCell(15).setCellValue(consultation.getVitalSign().getHemoglobin());
                        row.createCell(16).setCellValue(consultation.getVitalSign().getCholesterol());
                        row.createCell(17).setCellValue(consultation.getVitalSign().getTriglycerides());
                    }
                }

                for(int i=0; i<columns.length; i++){
                    if(i==5){   //Skip diagnostic column
                        continue;
                    }
                    sheet.autoSizeColumn(i);
                }

                return workbook;
            }

            return null;
        });

        Optional<Workbook> result = dialog.showAndWait();

        result.ifPresent(workbook -> {
            //Choose file destination
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar archivo");
            fileChooser.setInitialFileName("consultas-" + startDateField.getValue().format(DateTimeFormatter.BASIC_ISO_DATE) +  "-" + endDateField.getValue().format(DateTimeFormatter.BASIC_ISO_DATE) + ".xlsx");
            File file = fileChooser.showSaveDialog(menuController.getPrimaryStage());
            if (file != null) {
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    workbook.write(out);
                    out.close();

                    Notifications.create().title("Archivo guardado")
                            .text("Archivo de consultas guardado con éxito")
                            .position(Pos.BASELINE_RIGHT)
                            .showInformation();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    Notifications.create().title("Error al guardar archivo")
                            .text(ex.getCause().getMessage())
                            .position(Pos.BASELINE_RIGHT)
                            .showInformation();
                }
            }
        });
    }
}
