package de.cybine.factory.api.v1.sensor;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.cybine.factory.service.sensor.SensorEvent;
import de.cybine.quarkus.util.event.EventHandler;
import de.cybine.quarkus.util.event.Subscriber;
import jakarta.inject.Singleton;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Singleton
@RequiredArgsConstructor
@ServerEndpoint("/api/v1/sensor/feed")
public class SensorFeed implements Subscriber
{
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper;

    @OnOpen
    public void onOpen(Session session)
    {
        this.sessions.put(session.getId(), session);
        log.info("New websocket connection: {}", session.getId());
    }

    @OnClose
    public void onClose(Session session)
    {
        this.sessions.remove(session.getId());
        log.info("Closed websocket connection: {}", session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable)
    {
        this.sessions.remove(session.getId());
        log.warn("Websocket connection errored out: {} ({})", session.getId(), throwable.getMessage());
    }

    @OnMessage
    public void onMessage(Session session, String message)
    {

    }

    @EventHandler
    @SneakyThrows
    public void onSensorEvent(SensorEvent event)
    {
        String message = this.objectMapper.writeValueAsString(event.getData());
        this.broadcast(message);
    }

    private void broadcast(String message)
    {
        this.sessions.values().forEach(session -> session.getAsyncRemote().sendText(message, result ->
        {
            if (result.getException() != null)
                log.warn("Unable to send message via websocket to '{}': {}", session.getId(),
                        result.getException().getMessage());
        }));
    }
}
