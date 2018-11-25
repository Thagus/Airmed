package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Setting;
import utils.EmailManager;
import utils.Values;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

public class SettingsController {
    @FXML private TextField medicAddressField;
    @FXML private TextField medicNameField;
    @FXML private Spinner<Integer> consLengthSpinner;

    @FXML private TextField emailHostField;
    @FXML private TextField emailPortField;
    @FXML private TextField emailUserField;
    @FXML private TextField emailField;
    @FXML private PasswordField emailPasswordField;

    @FXML private TextField topBorderField;
    @FXML private TextField rightBorderField;
    @FXML private TextField leftBorderField;
    @FXML private TextField bottomBorderField;

    private int consLengthMins = 30;

    public void init(VBox settingsPane) {
        AtomicReference<Setting> medicName = new AtomicReference<>(Setting.find.byId("medic_name"));
        if(medicName.get() !=null)
            medicNameField.setText(medicName.get().getValue());

        AtomicReference<Setting> medicAddress = new AtomicReference<>(Setting.find.byId("medic_address"));
        if(medicAddress.get() !=null)
            medicAddressField.setText(medicAddress.get().getValue());

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

        AtomicReference<Setting> host = new AtomicReference<>(Setting.find.byId("email_host"));
        AtomicReference<Setting> port = new AtomicReference<>(Setting.find.byId("email_port"));
        AtomicReference<Setting> user = new AtomicReference<>(Setting.find.byId("email_user"));
        AtomicReference<Setting> email = new AtomicReference<>(Setting.find.byId("email_email"));
        AtomicReference<Setting> password = new AtomicReference<>(Setting.find.byId("email_password"));

        if(host.get() !=null)
            emailHostField.setText(host.get().getValue());
        if(port.get() !=null)
            emailPortField.setText(port.get().getValue());
        if(user.get() !=null)
            emailUserField.setText(user.get().getValue());
        if(email.get() !=null)
            emailField.setText(email.get().getValue());
        if(password.get() !=null)
            emailPasswordField.setText(password.get().getValue());

        if(host.get() !=null && port.get() !=null && user.get() !=null && email.get() !=null && password.get() !=null){
            EmailManager.getInstance().setSession(host.get().getValue(), Integer.parseInt(port.get().getValue()), user.get().getValue(), email.get().getValue(), password.get().getValue());
        }

        topBorderField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)")) {
                topBorderField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });
        rightBorderField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)")) {
                rightBorderField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });
        leftBorderField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)")) {
                leftBorderField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });
        bottomBorderField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)")) {
                bottomBorderField.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });

        AtomicReference<Setting> topPrintBorder = new AtomicReference<>(Setting.find.byId("print_top"));
        AtomicReference<Setting> rightPrintBorder = new AtomicReference<>(Setting.find.byId("print_right"));
        AtomicReference<Setting> bottomPrintBorder = new AtomicReference<>(Setting.find.byId("print_bottom"));
        AtomicReference<Setting> leftPrintBorder = new AtomicReference<>(Setting.find.byId("print_left"));

        if(topPrintBorder.get() !=null) {
            topBorderField.setText(topPrintBorder.get().getValue());
            Values.topBorder = new BigDecimal(topPrintBorder.get().getValue()).doubleValue();
        }
        else {
            topBorderField.setText("3.3");
            Values.topBorder = new BigDecimal("3.3").doubleValue();
            topPrintBorder.set(Setting.create("print_top", "3.3"));
        }
        if(rightPrintBorder.get() !=null) {
            rightBorderField.setText(rightPrintBorder.get().getValue());
            Values.rightBorder = new BigDecimal(rightPrintBorder.get().getValue()).doubleValue();
        }
        else {
            rightBorderField.setText("1.5");
            Values.rightBorder = new BigDecimal("1.5").doubleValue();
            rightPrintBorder.set(Setting.create("print_right", "1.5"));
        }
        if(bottomPrintBorder.get() !=null) {
            bottomBorderField.setText(bottomPrintBorder.get().getValue());
            Values.bottomBorder = new BigDecimal(bottomPrintBorder.get().getValue()).doubleValue();
        }
        else {
            bottomBorderField.setText("1.5");
            Values.bottomBorder = new BigDecimal("1.5").doubleValue();
            bottomPrintBorder.set(Setting.create("print_bottom", "1.5"));
        }
        if(leftPrintBorder.get() !=null) {
            leftBorderField.setText(leftPrintBorder.get().getValue());
            Values.leftBorder = new BigDecimal(leftPrintBorder.get().getValue()).doubleValue();
        }
        else {
            leftBorderField.setText("1.5");
            Values.leftBorder = new BigDecimal("1.5").doubleValue();
            leftPrintBorder.set(Setting.create("print_left", "1.5"));
        }

        //Save the changes once the pane is no longer visible
        settingsPane.visibleProperty().addListener((observable, oldValue, newValue) -> {
            finalConsLength.save();

            if(medicNameField.getText().length()>0){
                if(medicName.get() ==null)
                    medicName.set(Setting.create("medic_name", medicNameField.getText()));
                else {
                    medicName.get().setValue(medicNameField.getText());
                    medicName.get().save();
                }
            }

            if(medicAddressField.getText().length()>0){
                if(medicAddress.get() ==null)
                    medicAddress.set(Setting.create("medic_address", medicAddressField.getText()));
                else {
                    medicAddress.get().setValue(medicAddressField.getText());
                    medicAddress.get().save();
                }
            }

            //If all the email fields are specified, save them
            if(emailHostField.getText().length()>0 && emailPortField.getText().length()>0 && emailUserField.getText().length()>0 && emailField.getText().length()>0 && emailPasswordField.getText().length()>0){
                if(host.get() ==null)
                    host.set(Setting.create("email_host", emailHostField.getText()));
                else {
                    host.get().setValue(emailHostField.getText());
                    host.get().save();
                }

                if(port.get() ==null)
                    port.set(Setting.create("email_port", emailPortField.getText()));
                else {
                    port.get().setValue(emailPortField.getText());
                    port.get().save();
                }

                if(user.get() ==null)
                    user.set(Setting.create("email_user", emailUserField.getText()));
                else {
                    user.get().setValue(emailUserField.getText());
                    user.get().save();
                }

                if(email.get() ==null)
                    email.set(Setting.create("email_email", emailField.getText()));
                else {
                    email.get().setValue(emailField.getText());
                    email.get().save();
                }

                if(password.get() ==null)
                    password.set(Setting.create("email_password", emailPasswordField.getText()));
                else {
                    password.get().setValue(emailPasswordField.getText());
                    password.get().save();
                }

                //Reconfigure the email session
                EmailManager.getInstance().setSession(emailHostField.getText(), Integer.parseInt(emailPortField.getText()), emailUserField.getText(), emailField.getText(), emailPasswordField.getText());
            }

            if(topBorderField.getText().length()>0){
                if(topPrintBorder.get() !=null){
                    topPrintBorder.get().setValue(topBorderField.getText());
                    topPrintBorder.get().update();
                }
                else {
                    topPrintBorder.set(Setting.create("print_top", topBorderField.getText()));
                }

                Values.topBorder = new BigDecimal(topPrintBorder.get().getValue()).doubleValue();
            }
            if(rightBorderField.getText().length()>0){
                if(rightPrintBorder.get() !=null){
                    rightPrintBorder.get().setValue(rightBorderField.getText());
                    rightPrintBorder.get().update();
                }
                else {
                    rightPrintBorder.set(Setting.create("print_right", rightBorderField.getText()));
                }

                Values.rightBorder = new BigDecimal(rightPrintBorder.get().getValue()).doubleValue();
            }
            if(bottomBorderField.getText().length()>0){
                if(bottomPrintBorder.get() !=null){
                    bottomPrintBorder.get().setValue(bottomBorderField.getText());
                    bottomPrintBorder.get().update();
                }
                else {
                    bottomPrintBorder.set(Setting.create("print_bottom", bottomBorderField.getText()));
                }

                Values.bottomBorder = new BigDecimal(bottomPrintBorder.get().getValue()).doubleValue();
            }
            if(leftBorderField.getText().length()>0){
                if(leftPrintBorder.get() !=null){
                    leftPrintBorder.get().setValue(leftBorderField.getText());
                    leftPrintBorder.get().update();
                }
                else {
                    leftPrintBorder.set(Setting.create("print_left", leftBorderField.getText()));
                }

                Values.leftBorder = new BigDecimal(leftPrintBorder.get().getValue()).doubleValue();
            }
        });
    }
}
