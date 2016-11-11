package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//Info on servlet url patterns: http://stackoverflow.com/questions/4140448/difference-between-and-in-servlet-mapping-url-pattern

@WebServlet(description = "Home Page", urlPatterns = { "/Home", "/" })
public class Home extends AbstractServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGetRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
	}

	@Override
	protected void processPostRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}