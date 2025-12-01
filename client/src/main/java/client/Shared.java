package client;

import chess.ChessBoard;

import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class Shared {
    public static void printNew() {
        System.out.print("\n" + ">>>" + SET_TEXT_COLOR_WHITE);
    }

    public static String redraw(ChessBoard board, String persp) {
        PrintBoard.printDaBoard(board, persp);
        return "";
    }

    public static String highlight(ChessBoard board, String[] params) {
        PrintBoard.highlight(board, params[0]);
        return "";
    }
}
