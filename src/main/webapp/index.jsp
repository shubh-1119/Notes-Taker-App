<!DOCTYPE html>
<%@page import="com.connection.FactoryProvider"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.entities.Notes"%>
<%@page import="java.util.List"%>
<%@page import="org.hibernate.query.Query"%>
<%@page import="org.hibernate.Session"%>
<%@page import="com.connection.FactoryProvider"%>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Simple Notes Taker</title>
<link rel="stylesheet" href="css/styles.css">
</head>
<body>
	<div class="container">
		<header>
			<h1>Notes Taker</h1>
		</header>

		<main class="notes-app">
			<aside class="sidebar">
				<h2>Add New Note</h2>
				<%
				// Check for a message from the session (set by servlets)
				String message = (String) session.getAttribute("message");
				if (message != null) {
				%>
				<div
					class="message-container <%=message.contains("failed") ? "message-error" : "message-success"%>">
					<%=message%>
				</div>
				<%
				session.removeAttribute("message"); // Remove the message after displaying it
				}
				%>

				<form action="SaveNoteServlet" method="post">
					<input type="hidden" id="note-id" value="">
					<div class="form-group">
						<label for="note-title">Title:</label> <input type="text"
							name="title" id="note-title" placeholder="Enter note title"
							required>
					</div>
					<div class="form-group">
						<label for="note-content">Content:</label>
						<textarea name="content" id="note-content"
							placeholder="Write your note here..." rows="8" required></textarea>
					</div>
					<div class="form-group">
						<button type="submit" id="save-note-btn" class="btn btn-primary">Save
							Note</button>
						<button type="button" id="clear-form-btn"
							class="btn btn-secondary">Clear All</button>
					</div>
				</form>
			</aside>

			<section class="notes-display">
				<h2>Your Notes</h2>
				<div id="notes-list" class="notes-grid">
					<%
					Session session2 = null;
					List<Notes> notes = null;
					try {
						session2 = FactoryProvider.getFactory().openSession();
						Query q = session2.createQuery("from Notes");
						notes = q.list();
					} catch (Exception e) {
						e.printStackTrace();

					} finally {
						if (session2 != null && session2.isOpen()) {
							session2.close();
						}
					}

					SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

					if (notes == null || notes.isEmpty()) {
					%>
					<p id="no-notes-message">You don't have any notes yet. Add one
						above!</p>
					<%
					} else {
					for (Notes n : notes) {
					%>
					<div class="note-card">
						<h3><%=n.getTitle()%></h3>
						<p><%=n.getContent()%></p>
						<small><%=dateFormat.format(n.getAddedDate())%></small>
						<div class="note-actions">
							<a href="EditNote.jsp?note_id=<%=n.getId()%>">
								<button class="btn btn-edit">Edit</button>
							</a> <a href="DeleteNoteServlet?note_id=<%=n.getId()%>">
								<button class="btn btn-delete">Delete</button>
							</a>

						</div>
					</div>
					<%
					}
					}
					%>
				</div>
			</section>
		</main>
	</div>

	<footer>
		<p>&copy; 2025 Simple Notes Taker. All rights reserved.</p>
	</footer>

	<script>
		document.addEventListener('DOMContentLoaded',
				function() {
					const clearButton = document
							.getElementById('clear-form-btn');
					const noteTitleInput = document
							.getElementById('note-title');
					const noteContentTextarea = document
							.getElementById('note-content');

					if (clearButton) {
						clearButton.addEventListener('click', function() {
							noteTitleInput.value = '';
							noteContentTextarea.value = '';
						});
					}

                    // Fade out messages after a 3 seconds
                    const messageContainer = document.querySelector('.message-container');
                    if (messageContainer) {
                        setTimeout(() => {
                            messageContainer.style.transition = 'opacity 1s ease-out';
                            messageContainer.style.opacity = '0';
                            setTimeout(() => {
                                messageContainer.remove();
                            }, 1000); // Remove after fade out
                        }, 3000); // Display for 3 seconds
                    }
				});
	</script>
</body>
</html>