package com.olgabiro.mcppoc.mcp;

import com.olgabiro.mcppoc.model.Note;
import com.olgabiro.mcppoc.model.TodoItem;
import com.olgabiro.mcppoc.model.Topic;
import com.olgabiro.mcppoc.state.KnowledgeBaseState;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.StringJoiner;

@Service
public class KnowledgeBaseTools {

    private final KnowledgeBaseState state;

    public KnowledgeBaseTools(KnowledgeBaseState state) {
        this.state = state;
    }

    @McpTool(description = "Add a learning note (sticky note) to the knowledge base. Use this to save insights, definitions, or key concepts about AI, LLMs, agents, or MCP servers.")
    public String addNote(
            @McpToolParam(description = "Short title for the note") String title,
            @McpToolParam(description = "Content/details of the note") String content,
            @McpToolParam(description = "Topic category: MCP, LLM, AGENTS, or AI_GENERAL") String topic) {
        Topic t = parseTopic(topic);
        Note note = state.addNote(title, content, t);
        return "Note created: [" + note.getId() + "] " + note.getTitle() + " (topic: " + t.getLabel() + ")";
    }

    @McpTool(description = "List all notes in the knowledge base. Returns titles, topics, and IDs of every saved note.")
    public String listNotes() {
        Collection<Note> notes = state.getAllNotes();
        if (notes.isEmpty()) {
            return "No notes in the knowledge base yet.";
        }
        StringJoiner joiner = new StringJoiner("\n");
        for (Note n : notes) {
            joiner.add("[" + n.getId() + "] " + n.getTitle() + " (" + n.getTopic().getLabel() + ") — " + truncate(n.getContent(), 80));
        }
        return joiner.toString();
    }

    @McpTool(description = "Get the full content of a specific note by its ID.")
    public String getNote(@McpToolParam(description = "The note ID") String id) {
        Note note = state.getNote(id);
        if (note == null) {
            return "Note not found: " + id;
        }
        return note.getTitle() + " (" + note.getTopic().getLabel() + ")\n\n" + note.getContent();
    }

    @McpTool(description = "Delete a note from the knowledge base by its ID.")
    public String deleteNote(@McpToolParam(description = "The note ID to delete") String id) {
        boolean deleted = state.deleteNote(id);
        return deleted ? "Note " + id + " deleted." : "Note not found: " + id;
    }

    @McpTool(description = "Add a learning task (todo) to track your AI/LLM/agents learning progress.")
    public String addTodo(
            @McpToolParam(description = "Short title for the task") String title,
            @McpToolParam(description = "Details about what to learn or do") String description,
            @McpToolParam(description = "Topic category: MCP, LLM, AGENTS, or AI_GENERAL") String topic) {
        Topic t = parseTopic(topic);
        TodoItem todo = state.addTodo(title, description, t);
        return "Todo created: [" + todo.getId() + "] " + todo.getTitle() + " (topic: " + t.getLabel() + ")";
    }

    @McpTool(description = "Mark a learning task as completed. The todo will appear checked-off in the dashboard.")
    public String completeTodo(@McpToolParam(description = "The todo ID to complete") String id) {
        boolean completed = state.completeTodo(id);
        return completed ? "Todo " + id + " marked as completed!" : "Todo not found: " + id;
    }

    @McpTool(description = "List all learning tasks (todos) in the knowledge base.")
    public String listTodos() {
        Collection<TodoItem> todos = state.getAllTodos();
        if (todos.isEmpty()) {
            return "No learning tasks yet.";
        }
        StringJoiner joiner = new StringJoiner("\n");
        for (TodoItem t : todos) {
            String status = t.isCompleted() ? "[x]" : "[ ]";
            joiner.add(status + " [" + t.getId() + "] " + t.getTitle() + " (" + t.getTopic().getLabel() + ")");
        }
        return joiner.toString();
    }

    private Topic parseTopic(String topic) {
        try {
            return Topic.valueOf(topic.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return Topic.AI_GENERAL;
        }
    }

    private String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 3) + "...";
    }
}
