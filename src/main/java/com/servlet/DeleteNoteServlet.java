package com.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.connection.FactoryProvider;
import com.entities.Notes;

@WebServlet("/DeleteNoteServlet")
public class DeleteNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Session session3 = null;
		Transaction txn = null;
		HttpSession httpSession = request.getSession();

		try {
			int id = Integer.parseInt(request.getParameter("note_id").trim());

			session3 = FactoryProvider.getFactory().openSession();
			txn = session3.beginTransaction();

			Notes notes = session3.get(Notes.class, id);

			if (notes != null) {
				session3.remove(notes);
				txn.commit();
				System.out.println("Note with ID " + id + " deleted from Database...");
				httpSession.setAttribute("message", "Note deleted successfully!");
			} else {
				// Note not found
				txn.rollback(); // Rollback if transaction was started and note not found
				System.out.println("Note with ID " + id + " not found for deletion.");
				httpSession.setAttribute("message", "Error: Note not found for deletion.");
			}

		} catch (Exception e) {
			if (txn != null && txn.isActive()) { // Check if transaction is active before rolling back
				txn.rollback();
			}
			e.printStackTrace();
			System.out.println("Error deleting note: " + e.getMessage());
			httpSession.setAttribute("message", "Failed to delete note. Please try again.");
		} finally {
			if (session3 != null && session3.isOpen()) {
				session3.close();
			}
		}

		response.sendRedirect("index.jsp");
	}
}