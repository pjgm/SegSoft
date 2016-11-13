package database;

import org.apache.commons.dbutils.DbUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private Connection connection;

    public DBUtil() throws SQLException {
        String driver = "org.sqlite.JDBC";
        DbUtils.loadDriver(driver);
        setupConnection();
    }

    private void setupConnection() throws SQLException {
        String url = "jdbc:sqlite:Auth.db";
        connection = DriverManager.getConnection(url);
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        DbUtils.closeQuietly(connection);
    }


}
