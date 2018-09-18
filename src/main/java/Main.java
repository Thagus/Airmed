import controller.MenuController;
import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.avaje.datasource.DataSourceConfig;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        createDatabase();

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Airmed");

        Locale.setDefault(new Locale("es", "mx"));

        Scene scene = new Scene(new StackPane());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Menu.fxml"));

        scene.setRoot(loader.load());
        MenuController controller = loader.getController();
        controller.init();

        controller.showAgenda(null);

        this.primaryStage.setScene(scene);
        this.primaryStage.show();
    }

    private void createDatabase() {
        DataSourceConfig dsConfig = new DataSourceConfig();
        dsConfig.setUsername("airmed");
        dsConfig.setUrl("jdbc:h2:" + System.getProperty("user.home") + "/.db/airmed;TRACE_LEVEL_FILE=0;CIPHER=AES");
        dsConfig.setDriver("org.h2.Driver");

        boolean aprooved = false;

        //Get password from user
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Contraseña de BD");
        dialog.setHeaderText(null);
        dialog.setContentText("Contraseña:");
        dialog.setOnCloseRequest(event ->
            System.exit(0)
        );

        do {
            Optional<String> result = dialog.showAndWait();

            if(result.isPresent()){
                String password = result.get();
                if(password.length()>0) {
                    dsConfig.setPassword(password + " " + password);
                    System.out.println("##########" + password);

                    ServerConfig config = new ServerConfig();
                    config.setName("airmed");
                    config.setDataSourceConfig(dsConfig);
                    config.setDefaultServer(true);

                    try {
                        EbeanServer server = EbeanServerFactory.create(config);

                        aprooved = true;

                        /*
                        //TODO: Run migrations
                        MigrationConfig migrationConfig = new MigrationConfig();
                        migrationConfig.set
                        MigrationRunner runner = new MigrationRunner();
                        */
                    }
                    catch (Exception ignored){}
                }
            }
        }
        while (!aprooved);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
