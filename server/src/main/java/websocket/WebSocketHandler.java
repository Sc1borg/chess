package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import service.UserService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
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
            MakeMoveCommand mvcmd = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);
            UserGameCommand action = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (action.getCommandType()) {
                case LEAVE -> leave(action.getAuthToken(), action.getGameID(), ctx.session);
                case CONNECT -> connect(action.getAuthToken(), action.getGameID(), ctx.session);
                case MAKE_MOVE -> makeMove(mvcmd.getMove(), action.getAuthToken(), action.getGameID(), ctx.session);
                case RESIGN -> resign(action.getAuthToken(), action.getGameID(), ctx.session);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(String authToken, int gameID, Session session) throws IOException, DataAccessException {
        connections.add(session, gameID);
        String username = userService.getUsername(authToken);
        GameData game = gameService.getGame(gameID);
        String color = username.equals(game.blackUsername()) ? "Black" : username.equals(game.whiteUsername()) ? "White" : "Observer";
        var message = String.format("%s joined the game as %s", username, color);
        var notification = new NotificationMessage(message);
        var newMessage = new LoadGameMessage(game.game());
        connections.broadcastSelf(session, newMessage);
        connections.broadcast(session, notification, gameID);
    }

    private void leave(String authToken, int gameID, Session session) throws IOException, DataAccessException {
        String username = userService.getUsername(authToken);
        userService.leaveGame(gameID, username);
        var message = String.format("%s left the game", username);
        var notification = new NotificationMessage(message);
        connections.broadcast(session, notification, gameID);
        connections.remove(session);
    }

    private void resign(String authToken, int gameID, Session session) throws DataAccessException, IOException {
        String username = userService.getUsername(authToken);
        GameData game = gameService.getGame(gameID);
        if (!username.equals(game.whiteUsername()) && !username.equals(game.blackUsername())) {
            var message = "Error: You are not a player";
            var error = new ErrorMessage(message);
            connections.broadcastSelf(session, error);
            return;
        }
        if (game.game().getOver()) {
            var message = "Error: Game is already over";
            var error = new ErrorMessage(message);
            connections.broadcastSelf(session, error);
            return;
        }
        game.game().setOver();
        var message = String.format("%s has resigned", username);
        var notification = new NotificationMessage(message);
        connections.broadcast(null, notification, gameID);
    }

    private void makeMove(ChessMove move, String authToken, int gameID, Session session) throws Exception {
        try {
            String username = userService.getUsername(authToken);
            GameData game = gameService.getGame(gameID);
            String moveString = uninterpretMove(move);
            ChessGame.TeamColor color = username.equals(game.whiteUsername()) ? ChessGame.TeamColor.WHITE :
                    username.equals(game.blackUsername()) ? ChessGame.TeamColor.BLACK : null;
            if (color == null) {
                var message = "Error: You are not a player";
                var error = new ErrorMessage(message);
                connections.broadcastSelf(session, error);
                return;
            }
            if (color != game.game().getTeamTurn()) {
                throw new Exception("Error: Not your turn");
            }
            if (game.game().getOver()) {
                throw new Exception("Error: Game is already over");
            }
            try {
                game.game().makeMove(move);
            } catch (InvalidMoveException e) {
                var message = e.getMessage();
                var error = new ErrorMessage(message);
                connections.broadcastSelf(session, error);
                return;
            }
            gameService.updateGame(game);
            var message = String.format("%s made move %s", username, moveString);
            var notification = new NotificationMessage(message);
            var newMessage = new LoadGameMessage(game.game());
            connections.broadcast(null, newMessage, gameID);
            connections.broadcast(session, notification, gameID);
        } catch (Exception ex) {
            var message = ex.getMessage();
            var error = new ErrorMessage(message);
            connections.broadcastSelf(session, error);
        }
    }

    private String uninterpretMove(ChessMove move) {
        char y = letter(move.getStartPosition().getColumn());
        char p = letter(move.getEndPosition().getColumn());
        int x = move.getStartPosition().getRow();
        int q = move.getEndPosition().getRow();
        return "" + y + x + " " + p + q;
    }

    private char letter(int num) {
        return switch (num) {
            case 1 -> 'a';
            case 2 -> 'b';
            case 3 -> 'c';
            case 4 -> 'd';
            case 5 -> 'e';
            case 6 -> 'f';
            case 7 -> 'g';
            case 8 -> 'h';
            default -> 9;
        };
    }
}