package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Treatment;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.textfield.CustomTextField;
import utils.ActionButtonTableCell;

import java.util.Optional;

public class TreatmentsController {

    @FXML private TableView<Treatment> treatmentsTable;
    @FXML private CustomTextField searchField;

    @FXML private TableColumn<Treatment, String> descriptionColumn;
    @FXML private TableColumn<Treatment, String>  nameColumn;
    @FXML private TableColumn<Treatment, Button>  editColumn;
    @FXML private TableColumn<Treatment, Button>  deleteColumn;

    private ObservableList<Treatment> treatments;

    public void init() {
        treatmentsTable.setPlaceholder(new Label("Sin resultados"));

        treatments = FXCollections.observableList(Treatment.find.all());

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        editColumn.setCellFactory(ActionButtonTableCell.forTableColumn("Editar", (Treatment treatment) -> {
            editTreatment(treatment);
            return treatment;
        }));

        deleteColumn.setCellFactory(ActionButtonTableCell.forTableColumn("Borrar", (Treatment treatment) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Borrar estudio");
            alert.setHeaderText(null);
            alert.setContentText("¿Estás seguro que deseas borrar el estudio " + treatment.getName() + " ?");

            Optional<ButtonType> result = alert.showAndWait();

            result.ifPresent(buttonType -> {
                if(buttonType == ButtonType.OK){
                    treatment.delete();
                    treatments.remove(treatment);
                }
            });

            return treatment;
        }));


        FilteredList<Treatment> filteredTreatments = new FilteredList<>(treatments, p -> true);   //Wrap the observable list into a filtered list

        searchField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredTreatments.setPredicate(treatment -> {
                //If filter text is empty, display all.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                //Simplify the search string by striping accents and making it lowercase
                String lcSearch = StringUtils.stripAccents(newValue).toLowerCase();

                //Return true if all the search terms are contained in the name
                boolean match = true;
                for(String term : lcSearch.split("\\s+")){
                    match = match && (
                            StringUtils.stripAccents(treatment.getName()).toLowerCase().contains(term) ||
                            StringUtils.stripAccents(treatment.getDescription()).toLowerCase().contains(term)
                    );
                }

                return match;
            });
        }));

        SortedList<Treatment> sortedTreatments = new SortedList<>(filteredTreatments);        //Wrap the filtered list in a sorted list
        sortedTreatments.comparatorProperty().bind(treatmentsTable.comparatorProperty());   //Bind the sorted list comparator to the table comparator

        treatmentsTable.setItems(sortedTreatments);
    }

    public void newTreatment(ActionEvent actionEvent) {

    }

    public void editTreatment(Treatment treatment){

    }
}
