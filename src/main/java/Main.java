import controller.MenuController;
import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.DbMigrationConfig;
import io.ebean.config.ServerConfig;
import io.ebean.config.dbplatform.h2.H2DbEncrypt;
import io.ebeaninternal.dbmigration.DdlGenerator;
import io.ebeaninternal.server.core.DefaultServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Setting;
import org.avaje.datasource.DataSourceConfig;

import java.io.IOException;
import java.util.Locale;

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

    private void createDatabase(){
        DataSourceConfig dsConfig = new DataSourceConfig();
        dsConfig.setUsername("airmed");
        dsConfig.setPassword("airmed airmed");
        dsConfig.setUrl("jdbc:h2:" + System.getProperty("user.home") + "/.db/airmed;TRACE_LEVEL_FILE=0;CIPHER=AES");
        dsConfig.setDriver("org.h2.Driver");

        ServerConfig config = new ServerConfig();
        config.setName("airmed");
        config.setDataSourceConfig(dsConfig);
        config.setDefaultServer(true);

        //config.setDdlGenerate(true);
        //config.setDdlRun(true);

        EbeanServer server = EbeanServerFactory.create(config);

        //Run migrations
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
