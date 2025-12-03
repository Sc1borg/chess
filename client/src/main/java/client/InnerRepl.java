package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_YELLOW;

public class InnerRepl {
    private final GameData game;
    private final ChessGame.TeamColor persp;

    public InnerRepl(GameData game, String persp) {
        this.game = game;
        this.persp = Objects.equals(persp, "WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
    }

    public void run() {
        PrintBoard.highlight(game, null, persp);
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
                case "redraw" -> Shared.redraw(game, persp);
                case "leave" -> "quit";
                case "move" -> move(params);
                case "resign" -> resign();
                case "highlight" -> Shared.highlight(game, persp, params);
                default -> help();
            };
        } catch (Throwable ex) {
            return ex.getMessage();
        }
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
        try {
            startPos = Shared.interpretPos(params[0]);
            ChessPosition endPos = Shared.interpretPos(params[1]);
            move = new ChessMove(startPos, endPos, promo);
        } catch (Exception e) {
            return e.getMessage();
        }
        try {
            game.game().makeMove(move);
            Shared.redraw(game, persp);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return "";
    }

    private String resign() {
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
}
