package main.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//Info on servlet url patterns: http://stackoverflow.com/questions/4140448/difference-between-and-in-servlet-mapping-url-pattern
// The / doesn't override any other servlet. It only replaces the servletcontainer's builtin default servlet for all requests which doesn't match any other registered servlet.
@WebServlet(name = "Home Page", urlPatterns = { "/Home", "/" })
public class Home extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}