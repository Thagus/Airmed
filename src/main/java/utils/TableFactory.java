package utils;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Patient;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.CustomTextField;

import java.time.LocalDate;
import java.time.Period;

public class TableFactory {
    public static TableView<Patient> createPatientTable (VBox vBox){
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

        return patientTableView;
    }
}
