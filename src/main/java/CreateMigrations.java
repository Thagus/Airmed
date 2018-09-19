import io.ebean.dbmigration.DbMigration;

import java.io.IOException;

public class CreateMigrations {
    public static void main(String[] args) throws IOException {
        //Generate migrations
        DbMigration dbMigration = DbMigration.create();
        dbMigration.generateMigration();
    }
}
