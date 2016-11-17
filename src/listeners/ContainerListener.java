package listeners;

import app.AuthenticatorClass;
import app.Authenticator;
import database.DataSourceManager;

import javax.servlet.annotation.WebListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

@WebListener
public class ContainerListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        DataSourceManager cm = new DataSourceManager();

        ServletContext sc = servletContextEvent.getServletContext();
        Authenticator auth;

        try {
            auth = new AuthenticatorClass(cm.getDataSource());
            sc.setAttribute("authenticator", auth);
            sc.setAttribute("isSetupDone", auth.isSetupDone());
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
            // throwing RunTimeException makes the tomcat container exit
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
