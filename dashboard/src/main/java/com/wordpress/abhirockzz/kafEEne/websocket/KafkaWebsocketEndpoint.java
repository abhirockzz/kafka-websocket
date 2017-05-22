package com.wordpress.abhirockzz.kafEEne.websocket;

import com.wordpress.abhirockzz.kafEEne.websocket.model.Payload;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/{topic}/")
public class KafkaWebsocketEndpoint {

    @Inject
    private Peers peers;

    @OnOpen
    public void open(@PathParam("topic") String topic, Session peer) {
        System.out.println("Peer " + peer.getId() + " joined & subscribed to topic " + topic);
        peer.getUserProperties().put("topic", topic);
        peers.add(peer);
    }

    @OnClose
    public void close(Session peer) {
        peers.remove(peer);
        System.out.println("Peer " + peer.getId() + " left");
    }

    public void broadcast(@Observes Payload eventPayload) {
        System.out.println("Broadcasting payload " + eventPayload);
        
        try (Jsonb jsonb = JsonbBuilder.create();) {

            peers.peers().stream()
                    .filter(s -> s.isOpen())
                    .filter(s -> s.getUserProperties().get("topic").equals(eventPayload.getTopic()))
                    .forEach(s -> s.getAsyncRemote().sendText(jsonb.toJson(eventPayload)));
        } catch (Exception e) {
            System.out.println("Error during payload broadcast "+ e.getMessage());
        }

    }
}
