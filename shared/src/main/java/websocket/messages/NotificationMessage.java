package websocket.messages;

import com.google.gson.Gson;

public class NotificationMessage extends ServerMessage {
    String message;

    public NotificationMessage(String msg) {
        super(ServerMessageType.NOTIFICATION);
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
