package com.codeforanyone.edujavajsp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeforanyone.edujavajsp.database.UserDAO;
import com.codeforanyone.edujavajsp.model.UserNotFoundException;
import com.codeforanyone.edujavajsp.model.UserObj;

@SuppressWarnings("serial")
public class LogInServlet extends HttpServlet {
	static Logger log = LoggerFactory.getLogger(LogInServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.debug("Received a doGet() request");
		resp.setContentType("text/html"); // What does it do if you set
											// "text/plain" ? Try it!
		HttpSession session = req.getSession(false);
		if (session != null & req.getParameter("username") != null) {
			session.invalidate();
		}
		session = req.getSession(true);
		PrintWriter pw = resp.getWriter();
		if (req.getParameter("username") != null) {
			UserDAO udao = new UserDAO();

			try {
				UserObj u = udao.findUser(req.getParameter("username"));
				if (udao.isCorrectPW(req.getParameter("password"), u)) {
					pw.println("PW matches!");

					session.setAttribute("UserId", u.getId());
					pw.print("<br> u.getId(): " + u.getId());
					// req.setAttribute("page", "home");
					// req.getRequestDispatcher("/user").forward(req, resp);
					resp.sendRedirect("/user?page=home");

				} else {
					resp.sendRedirect("/Static/badLogIn.html");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (UserNotFoundException e) {
				resp.sendRedirect("/Static/badLogIn.html");
				e.printStackTrace();
			}

		} else {
			req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
		}

		pw.close();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.debug("Received a doGet() request");
		doPost(req, resp);
	}

}
