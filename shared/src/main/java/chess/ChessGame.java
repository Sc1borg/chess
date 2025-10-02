package chess;

import java.util.ArrayList;
import java.util.Collection;


/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard board = new ChessBoard();

    TeamColor turn;
    public ChessGame() {
        turn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn=team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param start the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition start) {
        Collection<ChessMove> moves = ChessPiece.pieceMoves(board, start);
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int x = 1; x<9; x++) {
            for (int y = 1; y<9; y++) {
                ChessPosition pos = new ChessPosition(x,y);
                if (board.getPiece(pos).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(pos).getTeamColor()==teamColor) {
                    return kingCheck(pos)
                }
            }
        }
    }
    private boolean kingCheck(ChessPosition start) {
        teamColor = start.getTeamColor()
        int[][] straight = {
            {1,0},
            {0,1},
            {-1,0},
            {0,-1}
        };
        int[][] diagonal = {
            {1,1},
            {1,-1},
            {-1,1},
            {-1, -1}
        };
        int[][] knightMoves = {
            {2, 1},
            {2, -1},
            {-2, 1},
            {-2, -1},
            {1, 2},
            {1, -2},
            {-1, 2},
            {-1, -2}
        };
        for (int[] dir : straight) {
            int deltaRow = dir[0];
            int deltaCol = dir[1];

            row = start.getRow();
            col = start.getColumn();

            while (true) {
                row += deltaRow;
                col += deltaCol;
                if (row < 1 || row > 8 || col < 1 || col > 8) {
                    break;
                }
                ChessPosition pos = new ChessPosition(row, col);
                var piecePos = board.getPiece(pos);
                if (piecePos != null) {
                    if (piecePos.getTeamColor() != teamColor && (piecePos.getPieceType() == ChessPiece.PieceType.QUEEN || piecePos.getPieceType() == ChessPiece.PieceType.ROOK) {
                        return true;
                    }
                }
            break;
            }
        }
        for (int[] dir : diagonal) {
            int deltaRow = dir[0];
            int delaCol = dir[1];

            row = start.getRow();
            col = start.getColumn();

            while (true) {
                row += deltaRow;
                col += deltaCol;
                if (row <1 || row > 8 || col < 1 || col > 8) {
                    break;
                }
                ChessPosition pos = new ChessPosition(row, col);
                var piecePos = board.getPiece(pos);
                if (piecePos != null) {
                    if (piecePos.getTeamColor() != teamColor && (piecePos.getPieceType() == ChessPiece.PieceType.QUEEN || piecePos.getPieceType() == ChessPiece.PieceType.BISHOP)) {
                        return true;
                    }
                }
            }
        }
        for (int[] dir : knightMoves) {
            int deltaRow = dir[0];
            int delaCol = dir[1];

            row = start.getRow();
            col = start.getColumn();
            
            row += deltaRow;
            col += deltaCol;
            if (row <1 || row > 8 || col < 1 || col > 8) {
                break;
            }
            ChessPosition pos = new ChessPosition(row, col);
            var piecePos = board.getPiece(pos);
            if (piecePos != null) {
                if (piecePos.getTeamColor() != teamColor && piecePos.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
        }
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        for (int x = 1; x<8; x++) {
            for (int y = 1; y<8; y++) {
                ChessPosition pos = new ChessPosition(x,y);
                if (board.getPiece(pos) == ChessPiece.PieceType.KING && board.getPiece(pos).getTeamColor()==teamColor) {
                    if (validMoves(pos) == null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board1 the new board to use
     */
    public void setBoard(ChessBoard board1) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                board.getBoard()[x][y] = board1.getBoard()[x][y];
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
