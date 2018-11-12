package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;
import model.Setting;

public class SettingsController {
    @FXML private Spinner<Integer> consLengthSpinner;

    private int consLengthMins = 30;

    public void init(VBox settingsPane) {
        Setting consLength = Setting.find.byId("cons_length");

        //If the setting doesn't exist, create the default one
        if(consLength==null){
            consLength = Setting.create("cons_length", "" + consLengthMins);
        }
        else {
            consLengthMins = Integer.parseInt(consLength.getValue());
        }

        Setting finalConsLength = consLength;   //Final Setting for use inside lambdas

        //Spinner value factory to change the value in the spinner
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 60, consLengthMins);
        valueFactory.setAmountToStepBy(5);  //5 min step
        consLengthSpinner.setValueFactory(valueFactory);
        consLengthSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            finalConsLength.setValue(newValue + "");
        });

        //Save the changes once the pane is no longer visible
        settingsPane.visibleProperty().addListener((observable, oldValue, newValue) -> {
            finalConsLength.save();
        });
    }
}
