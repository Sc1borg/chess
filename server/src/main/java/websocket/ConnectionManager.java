package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Session, Integer> connections = new ConcurrentHashMap<Session, Integer>();

    public void add(Session session, int gameID) {
        connections.put(session, gameID);
    }

    public void remove(Session session) {
        connections.remove(session);
    }

    public void broadcast(Session excludeSession, ServerMessage notification, Integer gameID) throws IOException {
        String msg = notification.toString();
        for (Session c : connections.keySet()) {
            if (c.isOpen() && Objects.equals(connections.get(c), gameID)) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }


    public void broadcastSelf(Session selfSession, ServerMessage message) throws IOException {
        String msg = message.toString();
        for (Session c : connections.keySet()) {
            if (c.isOpen()) {
                if (c.equals(selfSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}