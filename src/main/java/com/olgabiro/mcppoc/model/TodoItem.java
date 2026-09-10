package com.olgabiro.mcppoc.model;

import java.time.Instant;
import java.util.UUID;

public class TodoItem {

    private final String id;
    private String title;
    private String description;
    private Topic topic;
    private boolean completed;
    private final Instant createdAt;

    public TodoItem(String title, String description, Topic topic) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.title = title;
        this.description = description;
        this.topic = topic;
        this.completed = false;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
