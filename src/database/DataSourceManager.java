package database;

import org.apache.commons.dbcp2.BasicDataSource;

public class DataSourceManager {

    //TODO: Set parameters from config file
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String URL = "jdbc:sqlite:Auth.db";

    private BasicDataSource dataSource;

    public DataSourceManager() {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(DRIVER);
        dataSource.setUrl(URL);
    }

    public BasicDataSource getDataSource() {
        return dataSource;
    }


}
