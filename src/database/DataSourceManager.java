package database;

import org.apache.commons.dbcp2.BasicDataSource;

import java.util.Properties;

public class DataSourceManager {

    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String URL = "jdbc:sqlite:Auth.db";

    private BasicDataSource dataSource;

    public DataSourceManager() {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(DRIVER);
        dataSource.setUrl(URL);
        dataSource.addConnectionProperty("foreign_keys", "true");
    }

    public BasicDataSource getDataSource() {
        return dataSource;
    }


}
