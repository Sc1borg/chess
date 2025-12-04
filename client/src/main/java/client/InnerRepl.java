package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
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
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.EscapeSequences.SET_TEXT_COLOR_YELLOW;

public class InnerRepl implements NotificationHandler {
    private final LoginResult user;
    private final GameData game;
    private final ChessGame.TeamColor persp;
    private final WebSocketFacade ws;
    private ChessGame board;

    public InnerRepl(ServerFacade server, LoginResult user, GameData game, String persp) {
        this.user = user;
        this.game = game;
        String serverUrl = server.getUrl();
        this.persp = Objects.equals(persp, "WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        try {
            ws = new WebSocketFacade(serverUrl, this);
            ws.joinGame(user.authToken(), game.gameID());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        PrintBoard.highlight(game.game(), null, persp);
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            Shared.printNew();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                String help = "This is a cry for help";
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
                case "redraw" -> Shared.redraw(board, persp);
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign();
                case "highlight" -> Shared.highlight(board, persp, params);
                default -> help();
            };
        } catch (Throwable ex) {
            return ex.getMessage();
        }
    }

    private String leave() {
        try {
            ws.leave(user.authToken(), game.gameID());
        } catch (Exception e) {
            return e.getMessage();
        }
        return "quit";
    }

    private String move(String[] params) {
        if (game.game().getTeamTurn() != persp) {
            return "not your turn";
        }
        ChessPiece.PieceType promo = null;
        if (params.length != 3 && params.length != 2) {
            return "Invalid parameters";
        }
        if (params.length == 3) {
            promo = switch (params[2].toUpperCase()) {
                case "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
                case "BISHOP" -> ChessPiece.PieceType.BISHOP;
                case "QUEEN" -> ChessPiece.PieceType.QUEEN;
                case "ROOK" -> ChessPiece.PieceType.ROOK;
                default -> ChessPiece.PieceType.PAWN;
            };
            if (promo == ChessPiece.PieceType.PAWN) {
                return "Invalid parameters";
            }
        }
        ChessMove move;
        ChessPosition startPos;
        ChessPosition endPos;
        try {
            startPos = Shared.interpretPos(params[0]);
            endPos = Shared.interpretPos(params[1]);
            move = new ChessMove(startPos, endPos, promo);
        } catch (Exception e) {
            return e.getMessage();
        }
        try {
            game.game().makeMove(move);
            Shared.redraw(game.game(), persp);
            ws.makeMove(move.toString(), user.authToken(), game.gameID());
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "";
    }

    private String resign() {
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (true) {
            System.out.print("Are you sure? (yes/no)");
            Shared.printNew();
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("yes") || line.equalsIgnoreCase("no")) {
                result = line.toLowerCase();
                break;
            }
        }
        if (result.equals("no")) {
            return "You chose not to resign";
        }
        try {
            ws.resign(user.authToken(), game.gameID());
        } catch (Exception e) {
            return e.getMessage();
        }
        return "You have resigned";
    }

    private String help() {
        return """
                highlight <position> - highlights legal moves from the piece at that position
                redraw - redraws chess board
                leave - exit the game
                move <start position> <end position> - move piece at start position
                resign - give up
                help - list possible commands
                """;
    }

    @Override
    public void notify(ServerMessage notification) {
        if (notification instanceof LoadGameMessage) {
            board = ((LoadGameMessage) notification).getGame();
        } else if (notification instanceof NotificationMessage) {
            System.out.println(SET_TEXT_COLOR_YELLOW + ((NotificationMessage) notification).getMessage());
        } else if (notification instanceof ErrorMessage) {
            System.out.println(SET_TEXT_COLOR_RED + ((ErrorMessage) notification).getErrorMessage());
        }
    }
}
