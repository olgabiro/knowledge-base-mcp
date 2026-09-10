package com.olgabiro.mcppoc.model;

public enum Topic {
    MCP("MCP", "#a855f7"),
    LLM("LLM", "#3b82f6"),
    AGENTS("Agents", "#22c55e"),
    AI_GENERAL("AI General", "#f97316");

    private final String label;
    private final String color;

    Topic(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public String getColor() {
        return color;
    }
}
