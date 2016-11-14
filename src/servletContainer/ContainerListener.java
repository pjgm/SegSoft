package servletContainer;

import app.Application;
import app.Authenticator;
import database.DataSourceManager;

import javax.servlet.annotation.WebListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

@WebListener
public class ContainerListener implements ServletContextListener {

    private DataSourceManager cm;
    // throwing RunTimeException makes the tomcat container exit
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        cm = new DataSourceManager();

        ServletContext sc = servletContextEvent.getServletContext();
        Authenticator auth;

        try {
            auth = new Application(cm.getDataSource());
            sc.setAttribute("authenticator", auth);
            sc.setAttribute("isSetupDone", auth.isSetupDone());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("container destroyed");
    }
}
