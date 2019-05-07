package model;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import io.ebean.datasource.DataSourceConfig;
import org.h2.tools.ChangeFileEncryption;
import org.mindrot.jbcrypt.BCrypt;

public class ConnectionManager {
    private final String dbLocation;
    private final String dbName;

    private String hashedPassword;
    private EbeanServer ebeanServer;

    private static ConnectionManager instance;
    private ConnectionManager(){
        dbLocation = System.getProperty("user.home") + "/.db/";
        dbName = "airmed";
    }
    public static synchronized ConnectionManager getInstance(){
        if(instance==null){
            instance = new ConnectionManager();
        }

        return instance;
    }

    /**
     * Create a connection to the database
     * @param password The password for the database
     * @throws Exception Trowed exception in case of an error connecting
     */
    public void createConnection(String password) throws Exception{
        DataSourceConfig dsConfig = new DataSourceConfig();
        dsConfig.setUsername("airmed");
        dsConfig.setUrl("jdbc:h2:" + dbLocation + dbName + ";TRACE_LEVEL_FILE=0;CIPHER=AES");
        dsConfig.setDriver("org.h2.Driver");

        ServerConfig config = new ServerConfig();
        config.setName("airmed");
        config.setDefaultServer(true);
        config.setRunMigration(true);

        //Set the connection password
        dsConfig.setPassword(password + " " + password);

        config.setDataSourceConfig(dsConfig);

        ebeanServer = EbeanServerFactory.create(config);

        //Save password hash
        hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Close the existing db connection
     */
    public void closeConnection(){
        ebeanServer.shutdown(true, false);
    }

    /**
     * Change the access password for the db
     * @param oldPwd The current password
     * @param newPwd The new password
     * @return The success of the operation
     */
    public boolean changeDBPassword(String oldPwd, String newPwd){
        //Verify that the old password is the correct one
        if(!BCrypt.checkpw(oldPwd, hashedPassword)){
            return false;
        }
        try {
            //Change user password
            Ebean.createSqlUpdate("ALTER USER AIRMED SET PASSWORD '" + newPwd + "'").execute();
            //Close the connection to the database
            closeConnection();
            //Change encryption for database
            ChangeFileEncryption.execute(dbLocation, dbName, "AES", oldPwd.toCharArray(), newPwd.toCharArray(), true);
            //Reopen connection
            createConnection(newPwd);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
