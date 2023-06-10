package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDb {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";


    private static  ConnectionDb db = null;
    private ConnectionDb() {
    }
    public static ConnectionDb ConnectionDb() throws SQLException {

        if(db == null) {
            db = new ConnectionDb();
            return db;
        }else {
            return db;
        }
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        return conn;
    }
}
