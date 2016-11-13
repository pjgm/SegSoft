package database;

import org.apache.commons.dbutils.DbUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    protected Connection connection;

    private final String driver = "org.sqlite.JDBC";
    private final String url = "jdbc:sqlite:Auth.db";

    public DBUtil() throws SQLException {
        DbUtils.loadDriver(driver);
        setupConnection();
    }

    public void setupConnection() throws SQLException {
        connection = DriverManager.getConnection(url);
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        DbUtils.closeQuietly(connection);
    }


}
