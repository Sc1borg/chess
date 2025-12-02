package client;

import chess.ChessGame;
import chess.ChessPosition;
import model.GameData;

import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class Shared {
    public static void printNew() {
        System.out.print("\n" + ">>>" + SET_TEXT_COLOR_WHITE);
    }

    public static String redraw(GameData game, ChessGame.TeamColor persp) {
        PrintBoard.highlight(game, null, persp);
        return "";
    }

    public static String highlight(GameData game, ChessGame.TeamColor persp, String[] params) {
        if (params.length != 1) {
            return "invalid parameters";
        }
        ChessPosition pos;
        try {
            pos = interpretPos(params[0]);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        PrintBoard.highlight(game, pos, persp);
        return "";
    }

    public static ChessPosition interpretPos(String pos) throws Exception {
        if (pos.length() != 2) {
            throw new Exception("Invalid position");
        }
        pos = pos.toLowerCase();
        int y = switch (pos.charAt(0)) {
            case 'a' -> 1;
            case 'b' -> 2;
            case 'c' -> 3;
            case 'd' -> 4;
            case 'e' -> 5;
            case 'f' -> 6;
            case 'g' -> 7;
            case 'h' -> 8;
            default -> 9;
        };
        int x = pos.charAt(1) - '0';
        if (y == 9 || x > 8 || x < 1) {
            throw new Exception("Invalid position");
        }
        return new ChessPosition(x, y);
    }
}
