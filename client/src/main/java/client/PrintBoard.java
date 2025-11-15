package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import org.junit.jupiter.api.Test;
import ui.EscapeSequences;

public class PrintBoard {
    public static void printDaBoard(ChessBoard board, String persp) {
        chess.ChessGame.TeamColor perspEnum = persp.equalsIgnoreCase("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        String borderTop = "     A  B  C  D  E  F  G  H    ";
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        int counter = (perspEnum == ChessGame.TeamColor.WHITE) ? 8 : 1;
        System.out.print(borderTop);
        System.out.println(EscapeSequences.RESET_BG_COLOR);
        for (int y = 0; y < 8; y++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            System.out.print("  ");
            System.out.print(counter);
            System.out.print(" ");
            for (int x = 0; x < 8; x++) {
                String bgColor = ((x + y) % 2 == 0) ? EscapeSequences.SET_BG_COLOR_WHITE : EscapeSequences.SET_BG_COLOR_BLACK;
                System.out.print(bgColor);
                System.out.print(" ");
                ChessPosition pos = new ChessPosition(y + 1, x + 1);
                ChessPiece piecePos = board.getPiece(pos);
                String piece = piecePos == null ? " " : piecePos.toString();
                String textColor = piecePos == null ? "" : piecePos.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.SET_TEXT_COLOR_BLUE : EscapeSequences.SET_TEXT_COLOR_RED;
                System.out.print(textColor);
                System.out.print(piece);
                System.out.print(" ");
            }
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            System.out.print(" ");
            System.out.print(counter + " ");
            System.out.println(EscapeSequences.RESET_BG_COLOR);
            counter += (perspEnum == ChessGame.TeamColor.WHITE) ? -1 : 1;
        }
        System.out.print(borderTop);
    }

    @Test
    void callBoard() {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        printDaBoard(board, "WHITE");
    }
}
