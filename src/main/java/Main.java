import controller.MenuController;
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
import jfxtras.styles.jmetro8.JMetro;
import model.ConnectionManager;
import utils.ShakeTransition;
import java.io.IOException;
import java.util.Locale;

public class Main extends Application {
    private Stage primaryStage;
    private JMetro jMetro;

    @Override
    public void start(Stage primaryStage) {
        Locale.setDefault(new Locale("es", "mx"));
        this.primaryStage = primaryStage;

        this.jMetro = new JMetro(JMetro.Style.LIGHT); //Setting to change style to dark

        databaseLogin();
    }

    /**
     * Login to the database based on the password provided by the user
     */
    private void databaseLogin(){
        //Create prompt dialog
        Dialog dialog = new Dialog();
        dialog.setTitle("Airmed");
        dialog.setHeaderText("Acceso al sistema");
        dialog.initModality(Modality.NONE);

        this.jMetro.applyTheme(dialog.getDialogPane().getScene());

        //Create buttons
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        //Create pane
        HBox box = new HBox();
        box.setSpacing(10);

        //Add password label and field to the layout
        PasswordField passwordField = new PasswordField();
        box.getChildren().addAll(new Label("Contraseña :"), passwordField);

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
                            //Disable login button to prevent issues
                            loginButton.setDisable(true);

                            //Try to initiate db connection
                            try {
                                ConnectionManager.getInstance().createConnection(password);

                                //Close the dialog and initiate primary stage
                                dialog.close();
                                createPrimaryStage();
                            }
                            catch (Exception ignored){  //If the login fails, shake the dialog and ask for the password again
                                loginButton.setDisable(false);
                                ShakeTransition anim = new ShakeTransition(dialog.getDialogPane(), t -> passwordField.requestFocus());
                                anim.playFromStart();
                            }
                        }
                    }
                });
    }

    /**
     * Create the primary stage, initialize the controllers and show the agenda as main view
     * @throws IOException IF the fxml is missing from resources
     */
    private void createPrimaryStage() throws IOException {
        this.primaryStage.setTitle("Airmed");

        Scene scene = new Scene(new StackPane());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Menu.fxml"));

        scene.setRoot(loader.load());
        MenuController controller = loader.getController();
        controller.init(primaryStage, jMetro);

        this.jMetro.applyTheme(scene);

        controller.showAgenda(null);

        this.primaryStage.setScene(scene);
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
