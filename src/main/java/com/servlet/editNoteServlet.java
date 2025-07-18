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

@WebServlet("/editNoteServlet")
public class editNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession httpSession = request.getSession();

		Session session5 = null;
		Transaction txn = null;

		try {
			Date currentDate = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			String formattedDate = formatter.format(currentDate);
			System.out.println("Formatted Date (DD-MM-YYYY): " + formattedDate);

			int id = Integer.parseInt(request.getParameter("noteId").trim());
			String title = request.getParameter("title");
			String content = request.getParameter("content");

			session5 = FactoryProvider.getFactory().openSession();
			txn = session5.beginTransaction();

			Notes notes = session5.get(Notes.class, id);

			if (notes != null) {
				notes.setTitle(title);
				notes.setContent(content);
				notes.setAddedDate(currentDate);

				session5.merge(notes); // Use merge for updating an existing detached object

				txn.commit();
				System.out.println("Note with ID " + id + " updated in Database...");
				httpSession.setAttribute("message", "Note updated successfully!");
			} else {
				// Note not found for update
				if (txn != null && txn.isActive()) {
					txn.rollback(); // Rollback if transaction was started and note not found
				}
				System.out.println("Note with ID " + id + " not found for update.");
				httpSession.setAttribute("message", "Error: Note not found for update.");
			}

		} catch (NumberFormatException e) {
			// Handle case where noteId is not a valid integer
			if (txn != null && txn.isActive()) {
				txn.rollback();
			}
			e.printStackTrace();
			System.out.println("Invalid note ID provided: " + e.getMessage());
			httpSession.setAttribute("message", "Error: Invalid note ID. Cannot update.");
		}
		catch (Exception e) {
			if (txn != null && txn.isActive()) { // Check if transaction is active before rolling back
				txn.rollback();
			}
			e.printStackTrace();
			System.out.println("Error updating note: " + e.getMessage());
			httpSession.setAttribute("message", "Failed to update note. Please try again.");
		} finally {
			if (session5 != null && session5.isOpen()) {
				session5.close();
			}
		}

		response.sendRedirect("index.jsp");
	}
}