package main.java.filters;

import main.java.access_control.AccessController;
import main.java.access_control.Capability;
import main.java.access_control.CapabilityClass;
import main.java.app.Authenticator;
import main.java.exceptions.UndefinedAccountException;
import main.java.model.Account;

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
                if (ac.checkPermission(cap) && (resource.contains(cap.getResource()) ||
                        resource.equals("")))
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
        boolean setupDone = (boolean) config.getServletContext().getAttribute("isSetupDone");
        boolean isSetupRequest = request.getRequestURI().equals(setupURI);

        String loginURI = "/Login";
        boolean isLoginRequest = requestURI.equals(loginURI);

        boolean loggedIn = session != null && session.getAttribute("USER") != null;
        boolean hasPermission = false;

        if (!setupDone) {
            if (isSetupRequest)
                filterChain.doFilter(request, response);
            else
                response.sendRedirect(setupURI);
            return;
        }
        else if (!loggedIn) {
            if (isLoginRequest)
                filterChain.doFilter(request, response);
            else
                response.sendRedirect(loginURI);
            return;
        }

        Account acc = (Account) session.getAttribute("USER");

        try {
            acc = auth.get_account(acc.getUsername());
            session.setAttribute("USER", acc);
            hasPermission = hasPermission(acc.getUsername(), requestURI.substring(1));
        } catch (SQLException | UndefinedAccountException e) {
            e.printStackTrace();
        }

        boolean isLocked = loggedIn && ((Account) session.getAttribute("USER")).getLocked() == 1;

        if (isLocked) {
            session.invalidate();
            response.sendRedirect(loginURI);
            return;
        }

        if (!hasPermission) {
            response.getOutputStream().print("Error: You have no permission to access this page");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}