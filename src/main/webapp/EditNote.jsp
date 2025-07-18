<!DOCTYPE html>
<%@page import="org.hibernate.Session"%>
<%@page import="com.connection.FactoryProvider"%>
<%@page import="com.entities.Notes"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Note - Simple Notes Taker</title>
    <link rel="stylesheet" href="css/styles.css">
    </head>
<body>
    <div class="container">
        <header>
            <h1>Note Taker </h1>
        </header>

        <main class="notes-app">
            <section class="edit-section">
                <h2>Edit your Note </h2>
                <form id="note-edit-form" action="editNoteServlet" method="POST">
                
                <%
                	int id = Integer.parseInt(request.getParameter("note_id").trim());
                	Session session4 = FactoryProvider.getFactory().openSession();
                	Notes notes = session4.get(Notes.class, id);
                	session4.close();
                %>
                
                    <input type="hidden" id="note-id" name="noteId" value="<%= notes.getId() %>">

                    <div class="form-group">
                        <label for="edit-note-title">Title:</label>
                        <input type="text" id="edit-note-title" name="title"
                               placeholder="Enter note title" required
                               value="<%= notes.getTitle() %>">
                    </div>
                    <div class="form-group">
                        <label for="edit-note-content">Content:</label>
                        <textarea id="edit-note-content" name="content"
                                  placeholder="Write your note here..." rows="12" required><%= notes.getContent() %></textarea>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">Update Note</button>
                        <button type="button" class="btn btn-secondary" onclick="window.history.back()">Cancel</button>
                    </div>
                </form>
            </section>
        </main>
    </div>

    <footer>
        <p>&copy; 2025 Simple Notes Taker. All rights reserved.</p>
    </footer>
</body>
</html>