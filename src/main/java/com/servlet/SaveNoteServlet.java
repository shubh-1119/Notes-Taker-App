package com.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.connection.FactoryProvider;
import com.entities.Notes;

@WebServlet("/SaveNoteServlet")
public class SaveNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String title = request.getParameter("title");
		String content = request.getParameter("content");

		
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = formatter.format(currentDate);
		System.out.println("Formatted Date (DD-MM-YYYY): " + formattedDate);

		Notes notes = new Notes(title, content, currentDate);
		System.out.println("Title : " + notes.getTitle());
		System.out.println("Content : " + notes.getContent());

		Session session = null;
		Transaction txn = null;
		try {
			// Hibernate save
			session = FactoryProvider.getFactory().openSession();
			txn = session.beginTransaction();
			session.persist(notes);
			txn.commit();
			System.out.println("Note added to Database...");

			// Set success message in session
			HttpSession httpSession = request.getSession();
			httpSession.setAttribute("message", "Note saved successfully!");

		} catch (Exception e) {
			if (txn != null) {
				txn.rollback();
			}
			e.printStackTrace();
			// set an error message in session if saving fails
			HttpSession httpSession = request.getSession();
			httpSession.setAttribute("message", "Failed to save note. Please try again.");
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		response.sendRedirect("index.jsp");
	}

}