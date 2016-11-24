package filters;

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

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    private FilterConfig config;
    private Authenticator auth;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
        auth = (Authenticator) filterConfig.getServletContext().getAttribute("authenticator");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        String setupURI = "/Setup";
        String loginURI = "/Login";

        boolean setupDone = (boolean) config.getServletContext().getAttribute("isSetupDone");
        boolean setupRequest = request.getRequestURI().equals(setupURI);

        boolean loggedIn = session != null && session.getAttribute("USER") != null;
        boolean loginRequest = request.getRequestURI().equals(loginURI);


        if(!setupDone) {
            if(setupRequest)
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

        if (loggedIn || loginRequest)
            filterChain.doFilter(request, response);
        else
            response.sendRedirect(loginURI);
    }

    @Override
    public void destroy() {
    }
}
