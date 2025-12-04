package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import service.UserService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameService gameService = new GameService();
    private final UserService userService = new UserService();

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand action = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (UserGameCommand.getCommandType()) {
                case LEAVE -> leave(action.getAuthToken(), action.getGameID(), ctx.session);
                case CONNECT -> connect(action.getAuthToken(), action.getGameID(), ctx.session);
                case MAKE_MOVE ->
                        makeMove(((MakeMoveCommand) action).getMove(), action.getAuthToken(), action.getGameID(), ctx.session);
                case RESIGN -> resign(action.getAuthToken(), action.getGameID(), ctx.session);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(String authToken, int gameID, Session session) throws IOException, DataAccessException {
        connections.add(session);
        String username = userService.getUsername(authToken);
        GameData game = gameService.getGame(gameID);
        String color = username.equals(game.blackUsername()) ? "Black" : "White";
        var message = String.format("%s joined the game as %s", username, color);
        var notification = new NotificationMessage(message);
        connections.broadcast(session, notification);
    }

    private void leave(String authToken, int gameID, Session session) throws IOException, DataAccessException {
        String username = userService.getUsername(authToken);
        var message = String.format("%s left the game", username);
        var notification = new NotificationMessage(message);
        connections.broadcast(session, notification);
        connections.remove(session);
    }

    public void makeMove(String move, String authToken, int gameID, Session session) throws Exception {
        try {
            String username = userService.getUsername(authToken);
            var message = String.format("%s made move %s", username, move);
            var notification = new NotificationMessage(message);
            connections.broadcast(null, notification);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private String uninterpretMove(ChessMove move) {
        String moveString = "";

    }
}