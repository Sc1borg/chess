package client;

import chess.*;
import ui.EscapeSequences;

import java.util.ArrayList;
import java.util.Collection;

public class PrintBoard {
    public static void printDaBoard(ChessBoard board, String persp) {
        chess.ChessGame.TeamColor perspEnum = persp.equalsIgnoreCase("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        String borderTop = perspEnum == ChessGame.TeamColor.WHITE ? "     A  B  C  D  E  F  G  H    " : "     H  G  F  E  D  C  B  A    ";
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        int counter = (perspEnum == ChessGame.TeamColor.WHITE) ? 8 : 1;
        int moveDir = perspEnum == ChessGame.TeamColor.WHITE ? 1 : -1;
        int startNum = perspEnum == ChessGame.TeamColor.WHITE ? 7 : 0;
        System.out.print(borderTop);
        System.out.println(EscapeSequences.RESET_BG_COLOR);
        for (int x = startNum; moveDir == 1 ? x >= 0 : x < 8; x -= moveDir) {
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            System.out.print("  ");
            System.out.print(counter);
            System.out.print(" ");
            for (int y = (perspEnum == ChessGame.TeamColor.WHITE) ? 0 : 7;
                 (perspEnum == ChessGame.TeamColor.WHITE) ? y < 8 : y >= 0;
                 y += (perspEnum == ChessGame.TeamColor.WHITE) ? 1 : -1) {
                String bgColor = ((y + x) % 2 == 0) ? EscapeSequences.SET_BG_COLOR_BLACK : EscapeSequences.SET_BG_COLOR_WHITE;
                System.out.print(bgColor);
                System.out.print(" ");
                ChessPosition pos = new ChessPosition(x + 1, y + 1);
                ChessPiece piecePos = board.getPiece(pos);
                String piece = piecePos == null ? " " : piecePos.toString();
                String textColor = piecePos == null ? "" :
                        piecePos.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.SET_TEXT_COLOR_BLUE :
                                EscapeSequences.SET_TEXT_COLOR_RED;
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
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print(borderTop);
        System.out.print(EscapeSequences.RESET_BG_COLOR);
    }

    public static void highlight(ChessBoard board, ChessPosition position, String persp) {
        ChessPiece startPiece = board.getPiece(position);
//        Collection<ChessMove> validMoves = game.validMoves(startPiece);
        Collection<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessPosition> positions = new ArrayList<ChessPosition>();
        for (ChessMove move : validMoves) {
            positions.add(move.getEndPosition());
        }
        chess.ChessGame.TeamColor perspEnum = persp.equalsIgnoreCase("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        String borderTop = perspEnum == ChessGame.TeamColor.WHITE ? "     A  B  C  D  E  F  G  H    " : "     H  G  F  E  D  C  B  A    ";
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        int counter = (perspEnum == ChessGame.TeamColor.WHITE) ? 8 : 1;
        int moveDir = perspEnum == ChessGame.TeamColor.WHITE ? 1 : -1;
        int startNum = perspEnum == ChessGame.TeamColor.WHITE ? 7 : 0;
        System.out.print(borderTop);
        System.out.println(EscapeSequences.RESET_BG_COLOR);
        for (int x = startNum; moveDir == 1 ? x >= 0 : x < 8; x -= moveDir) {
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            System.out.print("  ");
            System.out.print(counter);
            System.out.print(" ");
            for (int y = (perspEnum == ChessGame.TeamColor.WHITE) ? 0 : 7;
                 (perspEnum == ChessGame.TeamColor.WHITE) ? y < 8 : y >= 0;
                 y += (perspEnum == ChessGame.TeamColor.WHITE) ? 1 : -1) {
                ChessPosition pos = new ChessPosition(x + 1, y + 1);
                String bgColor = ((y + x) % 2 == 0) ? EscapeSequences.SET_BG_COLOR_BLACK : EscapeSequences.SET_BG_COLOR_WHITE;
                if (positions.contains(pos)) {
                    bgColor = EscapeSequences.SET_BG_COLOR_GREEN;
                }
                System.out.print(bgColor);
                System.out.print(" ");
                ChessPiece piecePos = board.getPiece(pos);
                String piece = piecePos == null ? " " : piecePos.toString();
                String textColor = piecePos == null ? "" :
                        piecePos.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.SET_TEXT_COLOR_BLUE :
                                EscapeSequences.SET_TEXT_COLOR_RED;
                String hello = "hello";
                System.out.print(textColor);
                System.out.print(piece);
                System.out.print(" ");
            }
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            System.out.print(" ");
            System.out.print(counter + " ");
            String goodbye = "goodbye";
            System.out.println(EscapeSequences.RESET_BG_COLOR);
            counter += (perspEnum == ChessGame.TeamColor.WHITE) ? -1 : 1;
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print(borderTop);
        System.out.print(EscapeSequences.RESET_BG_COLOR);
    }
}
