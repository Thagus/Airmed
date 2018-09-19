import controller.MenuController;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.avaje.datasource.DataSourceConfig;
import utils.ShakeTransition;
import java.io.IOException;
import java.util.Locale;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Locale.setDefault(new Locale("es", "mx"));
        this.primaryStage = primaryStage;

        databaseLogin();
    }

    private void databaseLogin(){
        DataSourceConfig dsConfig = new DataSourceConfig();
        dsConfig.setUsername("airmed");
        dsConfig.setUrl("jdbc:h2:" + System.getProperty("user.home") + "/.db/airmed;TRACE_LEVEL_FILE=0;CIPHER=AES");
        dsConfig.setDriver("org.h2.Driver");

        ServerConfig config = new ServerConfig();
        config.setName("airmed");
        config.setDefaultServer(true);
        config.setRunMigration(true);

        //Create prompt dialog
        Dialog dialog = new Dialog();
        dialog.setTitle("Airmed");
        dialog.setHeaderText("Acceso al sistema");
        dialog.initModality(Modality.NONE);

        //Create buttons
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        //Create pane
        HBox box = new HBox();
        box.setSpacing(10);

        //Add password label and field to the layout
        PasswordField passwordField = new PasswordField();
        box.getChildren().addAll(new Label("Password :"), passwordField);

        //Disable login button by default
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);
        //Enable button if there's text in the password field
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> loginButton.setDisable(newValue.trim().isEmpty()));

        dialog.getDialogPane().setContent(box);

        // Request focus on the player name field by default.
        Platform.runLater(passwordField::requestFocus);

        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.show();

        //Add listener for when the button is pressed
        loginButton.addEventFilter(EventType.ROOT,
                e->{
                    if(e.getEventType().equals(ActionEvent.ACTION)){
                        e.consume();

                        //Get password and verify is not empty
                        String password = passwordField.getText();
                        if (password.length()>0) {
                            //Set the DB password
                            dsConfig.setPassword(password + " " + password);

                            //Initialize server config
                            config.setDataSourceConfig(dsConfig);

                            //Try to initiate db connection
                            try {
                                EbeanServerFactory.create(config);

                                //Close the dialog and initiate primary stage
                                dialog.close();
                                createPrimaryStage();
                            }
                            catch (Exception ignored){  //If the login fails, shake the dialog and ask for the password again
                                ShakeTransition anim = new ShakeTransition(dialog.getDialogPane(), t -> passwordField.requestFocus());
                                anim.playFromStart();
                            }
                        }
                    }
                });
    }

    private void createPrimaryStage() throws IOException {
        this.primaryStage.setTitle("Airmed");

        Scene scene = new Scene(new StackPane());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Menu.fxml"));

        scene.setRoot(loader.load());
        MenuController controller = loader.getController();
        controller.init();

        controller.showAgenda(null);

        this.primaryStage.setScene(scene);
        this.primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
