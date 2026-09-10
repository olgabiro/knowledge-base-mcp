package com.olgabiro.mcppoc.web;

import com.olgabiro.mcppoc.state.KnowledgeBaseState;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final KnowledgeBaseState state;
    private final Set<SseEmitter> emitters = ConcurrentHashMap.newKeySet();

    public DashboardController(KnowledgeBaseState state) {
        this.state = state;
        state.addListener(this::broadcastUpdate);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));
        return emitter;
    }

    @GetMapping("/state")
    public Map<String, Object> getState() {
        return Map.of(
                "notes", state.getAllNotes(),
                "todos", state.getAllTodos()
        );
    }

    private void broadcastUpdate() {
        Map<String, Object> payload = Map.of(
                "notes", state.getAllNotes(),
                "todos", state.getAllTodos()
        );
        var deadEmitters = new java.util.ArrayList<SseEmitter>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("update")
                        .data(payload, MediaType.APPLICATION_JSON));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        }
        emitters.removeAll(deadEmitters);
    }
}
