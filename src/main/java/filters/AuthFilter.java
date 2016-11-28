package filters;

import access_control.AccessController;
import access_control.Capability;
import access_control.CapabilityClass;
import app.Authenticator;
import exceptions.UndefinedAccountException;
import model.Account;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    private FilterConfig config;
    private Authenticator auth;
    private AccessController ac;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
        auth = (Authenticator) filterConfig.getServletContext().getAttribute("authenticator");
        ac = (AccessController) filterConfig.getServletContext().getAttribute("accesscontroller");
    }

    private boolean hasPermission(String username, String resource) {
        boolean hasPermission = false;
        try {
            List<CapabilityClass> caps = ac.getCapabilities(username);
            for (Capability cap : caps)
                if (ac.checkPermission(cap) && cap.getResource().equals(resource))
                    hasPermission = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hasPermission;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();
        String setupURI = "/Setup";
        String loginURI = "/Login";
        String rootURI = "/";

        boolean setupDone = (boolean) config.getServletContext().getAttribute("isSetupDone");
        boolean setupRequest = request.getRequestURI().equals(setupURI);

        boolean loggedIn = session != null && session.getAttribute("USER") != null;
        boolean loginRequest = requestURI.equals(loginURI);
        boolean rootRequest = requestURI.equals(rootURI);

        boolean hasPermission = false;

        if(!setupDone) {
            if (setupRequest)
                filterChain.doFilter(request, response);
            else
                response.sendRedirect(setupURI);
            return;
        }

        if (loggedIn) {
            Account acc = (Account) session.getAttribute("USER");
            try {
                acc = auth.get_account(acc.getUsername());
                session.setAttribute("USER", acc);
                hasPermission = hasPermission(acc.getUsername(), requestURI.substring(1));
            } catch (SQLException | UndefinedAccountException e) {
                e.printStackTrace();
            }
        }

        boolean isLocked = loggedIn && ((Account) session.getAttribute("USER")).getLocked() == 1;

        if (isLocked) {
            session.invalidate();
            response.sendRedirect(loginURI);
            return;
        }

        if (loggedIn && !hasPermission){
            response.getOutputStream().print("Error: You have no permission to access this page");
            return;
        }

        if (loggedIn || loginRequest)
            filterChain.doFilter(request, response);
        else
            response.sendRedirect(loginURI);
    }

    @Override
    public void destroy() {
    }
}
