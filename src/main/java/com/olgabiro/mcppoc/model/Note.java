package com.olgabiro.mcppoc.model;

import java.time.Instant;
import java.util.UUID;

public class Note {

    private final String id;
    private String title;
    private String content;
    private Topic topic;
    private final Instant createdAt;

    public Note(String title, String content, Topic topic) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.title = title;
        this.content = content;
        this.topic = topic;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
