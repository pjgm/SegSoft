package database;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.DbUtils;

import javax.activation.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceManager {

    //TODO: Set parameters from config file
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String URL = "jdbc:sqlite:Auth.db";

    private BasicDataSource dataSource;

    public DataSourceManager() {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(DRIVER);
        dataSource.setUrl(URL);
        //dataSource.setDefaultAutoCommit(true);
    }

    public BasicDataSource getDataSource() {
        return dataSource;
    }


}
