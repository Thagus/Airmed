package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Consultation;
import model.Patient;
import org.controlsfx.control.textfield.CustomTextField;

import java.time.LocalDate;
import java.util.Optional;

public class ConsultationController {

    private Patient patient;
    private Consultation consultation;
    private MenuController menuController;

    public void init(MenuController menuController){
        this.menuController = menuController;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;

        consultation = new Consultation(
                //Assign consultation data
        );
    }

    public void goToPrescription(ActionEvent actionEvent) {
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

        patientResult.ifPresent(menuController::beginConsultation);
    }
}
