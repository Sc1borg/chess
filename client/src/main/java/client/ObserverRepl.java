package client;

import chess.ChessGame;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import model.GameData;
import model.LoginResult;
import server.ServerFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.EscapeSequences.SET_TEXT_COLOR_YELLOW;

public class ObserverRepl implements NotificationHandler {
    private final GameData game;
    private ChessGame board;
    private final LoginResult user;
    private final WebSocketFacade ws;

    public ObserverRepl(ServerFacade server, LoginResult user, GameData game) {
        this.game = game;
        this.user = user;
        String serverUrl = server.getUrl();
        try {
            ws = new WebSocketFacade(serverUrl, this);
            ws.joinGame(user.authToken(), game.gameID());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            Shared.printNew();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_YELLOW + result);
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
    }

    private String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

            return switch (cmd) {
                case "leave" -> "quit";
                case "redraw" -> Shared.redraw(board, ChessGame.TeamColor.WHITE);
                case "highlight" -> Shared.highlight(board, ChessGame.TeamColor.WHITE, params);
                default -> help();
            };
        } catch (Throwable ex) {
            return ex.getMessage();
        }
    }

    private String help() {
        return """
                leave - exit the game
                redraw - redraws the board
                highlight <position> - highlights legal moves for piece at that position
                help - list possible commands
                """;
    }

    @Override
    public void notify(ServerMessage notification) {
        if (notification instanceof LoadGameMessage) {
            System.out.println();
            board = ((LoadGameMessage) notification).getGame();
            Shared.redraw(board, ChessGame.TeamColor.WHITE);
            Shared.printNew();
        } else if (notification instanceof NotificationMessage) {
            System.out.println();
            System.out.println(SET_TEXT_COLOR_YELLOW + ((NotificationMessage) notification).getMessage());
            Shared.printNew();
        } else if (notification instanceof ErrorMessage) {
            System.out.println();
            System.out.println(SET_TEXT_COLOR_RED + ((ErrorMessage) notification).getErrorMessage());
            Shared.printNew();
        }
    }
}
