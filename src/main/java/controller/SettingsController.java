package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Setting;
import utils.EmailManager;

public class SettingsController {
    @FXML private TextField medicAddressField;
    @FXML private TextField medicNameField;
    @FXML private Spinner<Integer> consLengthSpinner;

    @FXML private TextField emailHostField;
    @FXML private TextField emailPortField;
    @FXML private TextField emailUserField;
    @FXML private TextField emailField;
    @FXML private PasswordField emailPasswordField;

    private int consLengthMins = 30;

    public void init(VBox settingsPane) {
        Setting medicName = Setting.find.byId("medic_name");
        if(medicName!=null)
            medicNameField.setText(medicName.getValue());

        Setting medicAddress = Setting.find.byId("medic_address");
        if(medicAddress!=null)
            medicAddressField.setText(medicAddress.getValue());

        Setting consLength = Setting.find.byId("cons_length");
        //If the setting doesn't exist, create the default one
        if(consLength==null)
            consLength = Setting.create("cons_length", "" + consLengthMins);
        else
            consLengthMins = Integer.parseInt(consLength.getValue());

        Setting finalConsLength = consLength;   //Final Setting for use inside lambdas

        //Spinner value factory to change the value in the spinner
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 60, consLengthMins);
        valueFactory.setAmountToStepBy(5);  //5 min step
        consLengthSpinner.setValueFactory(valueFactory);
        consLengthSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            finalConsLength.setValue(newValue + "");
        });

        //The port can only be a number
        emailPortField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                emailPortField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        medicAddressField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));
        medicNameField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 255 ? change : null));

        Setting host = Setting.find.byId("email_host");
        Setting port = Setting.find.byId("email_port");
        Setting user = Setting.find.byId("email_user");
        Setting email = Setting.find.byId("email_email");
        Setting password = Setting.find.byId("email_password");

        if(host!=null)
            emailHostField.setText(host.getValue());
        if(port!=null)
            emailPortField.setText(port.getValue());
        if(user!=null)
            emailUserField.setText(user.getValue());
        if(email!=null)
            emailField.setText(email.getValue());
        if(password!=null)
            emailPasswordField.setText(password.getValue());

        if(host!=null && port!=null && user!=null && email!=null && password!=null){
            EmailManager.getInstance().setSession(host.getValue(), Integer.parseInt(port.getValue()), user.getValue(), email.getValue(), password.getValue());
        }

        //Save the changes once the pane is no longer visible
        settingsPane.visibleProperty().addListener((observable, oldValue, newValue) -> {
            finalConsLength.save();

            if(medicNameField.getText().length()>0){
                if(medicName==null)
                    Setting.create("medic_name", medicNameField.getText());
                else {
                    medicName.setValue(medicNameField.getText());
                    medicName.save();
                }
            }

            if(medicAddressField.getText().length()>0){
                if(medicAddress==null)
                    Setting.create("medic_address", medicAddressField.getText());
                else {
                    medicAddress.setValue(medicAddressField.getText());
                    medicAddress.save();
                }
            }

            //If all the email fields are specified, save them
            if(emailHostField.getText().length()>0 && emailPortField.getText().length()>0 && emailUserField.getText().length()>0 && emailField.getText().length()>0 && emailPasswordField.getText().length()>0){
                if(host==null)
                    Setting.create("email_host", emailHostField.getText());
                else {
                    host.setValue(emailHostField.getText());
                    host.save();
                }

                if(port==null)
                    Setting.create("email_port", emailPortField.getText());
                else {
                    port.setValue(emailPortField.getText());
                    port.save();
                }

                if(user==null)
                    Setting.create("email_user", emailUserField.getText());
                else {
                    user.setValue(emailUserField.getText());
                    user.save();
                }

                if(email==null)
                    Setting.create("email_email", emailField.getText());
                else {
                    email.setValue(emailField.getText());
                    email.save();
                }

                if(password==null)
                    Setting.create("email_password", emailPasswordField.getText());
                else {
                    password.setValue(emailPasswordField.getText());
                    password.save();
                }

                //Reconfigure the email session
                EmailManager.getInstance().setSession(emailHostField.getText(), Integer.parseInt(emailPortField.getText()), emailUserField.getText(), emailField.getText(), emailPasswordField.getText());
            }
        });
    }
}
