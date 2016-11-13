package servletContainer;

import app.Authenticator;
import app.AuthenticatorClass;
import database.DBUtil;

import javax.servlet.annotation.WebListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

@WebListener
public class ContainerListener implements ServletContextListener {

    private DBUtil db;
    // throwing RunTimeException makes the tomcat container exit
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        try {
            db = new DBUtil();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        ServletContext sc = servletContextEvent.getServletContext();
        Authenticator auth = new AuthenticatorClass(db.getConnection());
        sc.setAttribute("authenticator", auth);

        try {
            sc.setAttribute("isSetupDone", auth.isSetupDone());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            closeDBConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeDBConnection() throws SQLException {
        if (db.getConnection() != null)
            db.closeConnection();
    }
}
