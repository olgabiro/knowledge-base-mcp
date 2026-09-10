package com.olgabiro.mcppoc.state;

import com.olgabiro.mcppoc.model.Note;
import com.olgabiro.mcppoc.model.TodoItem;
import com.olgabiro.mcppoc.model.Topic;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KnowledgeBaseState {

    private final Map<String, Note> notes = new ConcurrentHashMap<>();
    private final Map<String, TodoItem> todos = new ConcurrentHashMap<>();
    private final List<Runnable> listeners = new ArrayList<>();

    @PostConstruct
    void init() {
        seedData();
    }

    private void seedData() {
        addNote("What is MCP?",
                "Model Context Protocol - a standardized way for AI agents to connect to external tools and data sources. Think of it as a USB-C port for AI.",
                Topic.MCP);
        addNote("MCP Server vs Client",
                "Server exposes tools/resources/prompts. Client (hosted in an AI app) connects to servers to use their capabilities.",
                Topic.MCP);
        addNote("LLM Temperature",
                "Controls randomness in outputs. Low (0.1) = deterministic, High (1.0) = creative. For code, use low. For brainstorming, go high.",
                Topic.LLM);
        addNote("Chain-of-Thought Prompting",
                "Ask the model to 'think step by step' before answering. Dramatically improves reasoning on complex tasks.",
                Topic.LLM);
        addNote("Agent Architecture",
                "Agents combine an LLM with tools, memory, and a planning loop. The LLM decides which tool to call based on the user request.",
                Topic.AGENTS);
        addNote("Retrieval-Augmented Generation",
                "RAG grounds LLM responses in real documents. Retrieve relevant chunks from a vector store, inject into the prompt context.",
                Topic.AI_GENERAL);

        addTodo("Build a simple MCP server", "Implement a server with 3 tools using Spring AI", Topic.MCP);
        addTodo("Experiment with prompt engineering", "Test chain-of-thought vs zero-shot on a reasoning task", Topic.LLM);
        addTodo("Design an agent workflow", "Map out tool dependencies and error handling for a multi-step agent", Topic.AGENTS);
    }

    public Note addNote(String title, String content, Topic topic) {
        Note note = new Note(title, content, topic);
        notes.put(note.getId(), note);
        notifyListeners();
        return note;
    }

    public boolean deleteNote(String id) {
        boolean removed = notes.remove(id) != null;
        if (removed) notifyListeners();
        return removed;
    }

    public Note getNote(String id) {
        return notes.get(id);
    }

    public Collection<Note> getAllNotes() {
        return List.copyOf(notes.values());
    }

    public TodoItem addTodo(String title, String description, Topic topic) {
        TodoItem todo = new TodoItem(title, description, topic);
        todos.put(todo.getId(), todo);
        notifyListeners();
        return todo;
    }

    public boolean completeTodo(String id) {
        TodoItem todo = todos.get(id);
        if (todo != null) {
            todo.setCompleted(true);
            notifyListeners();
            return true;
        }
        return false;
    }

    public boolean deleteTodo(String id) {
        boolean removed = todos.remove(id) != null;
        if (removed) notifyListeners();
        return removed;
    }

    public TodoItem getTodo(String id) {
        return todos.get(id);
    }

    public Collection<TodoItem> getAllTodos() {
        return List.copyOf(todos.values());
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }
}
