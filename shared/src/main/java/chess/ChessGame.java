package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;


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
        board.resetBoard();
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
        turn = team;
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
        var piece = board.getPiece(start);
        if (piece == null) {
            return null;
        }
        turn = piece.getTeamColor();
        Collection<ChessMove> moves = ChessPiece.pieceMoves(board, start);
        Collection<ChessMove> validMoves = new ArrayList<>();
        if (moves == null) {
            return validMoves;
        }
        for (var move : moves) {
            ChessBoard tempBoard = board.copy();
            tempBoard.makeMove(move, turn);
            if (!isInCheck(turn, tempBoard)) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        if (board.getPiece(start) == null || board.getPiece(start).getTeamColor() != turn) {
            throw new InvalidMoveException("That move isn't valid");
        }
        var moves = validMoves(start);
        if (moves == null) {
            throw new InvalidMoveException("That move isn't valid");
        }
        if (moves.contains(move)) {
            board.makeMove(move, turn);
            turn = (turn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        } else {
            throw new InvalidMoveException("That move isn't valid");
        }
    }

    public boolean isInCheck(TeamColor teamColor, ChessBoard grid) {
        //loop through to find the King
        for (int x = 1; x < 9; x++) {
            for (int y = 1; y < 9; y++) {
                ChessPosition pos = new ChessPosition(x, y);
                var piecePos = grid.getPiece(pos);
                if (piecePos != null) {
                    if (piecePos.getPieceType() == ChessPiece.PieceType.KING && piecePos.getTeamColor() == teamColor) {
                        //Once King is found, check that nothing can hit him
                        return kingCheck(pos, teamColor, grid);
                    }
                }
            }
        }
        //I wasn't sure what to do if there is no King, so this is it for now.
        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, board);
    }

    private boolean kingCheck(ChessPosition start, TeamColor team, ChessBoard grid) {
        int[][] straight = getInts(ChessPiece.ROOK);
        int[][] diagonal = getInts(ChessPiece.Bishop);
        int[][] knightMoves = getInts(ChessPiece.Knight);
        int row;
        int col;

        //Rook and Queen handling
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
                var piecePos = grid.getPiece(pos);
                if (piecePos != null) {
                    if (piecePos.getTeamColor() != team && (piecePos.getPieceType() == ChessPiece.PieceType.QUEEN)) {
                        return true;
                    } else if (piecePos.getTeamColor() != team && piecePos.getPieceType() == ChessPiece.PieceType.ROOK) {
                        return true;
                    }
                    break;
                }
            }
        }

        //Bishop and part queen handling
        for (int[] dir : diagonal) {
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
                var piecePos = grid.getPiece(pos);
                if (piecePos != null) {
                    if (piecePos.getTeamColor() != team && (piecePos.getPieceType() == ChessPiece.PieceType.QUEEN)) {
                        return true;
                    } else if (piecePos.getTeamColor() != team && piecePos.getPieceType() == ChessPiece.PieceType.BISHOP) {
                        return true;
                    }
                    break;
                }
            }
        }

        if (handleKnight(start, grid, team)) { return true; }

        if (handleKing(start, grid, team)) { return true; }
        
        if (handlePawn(start, grid, team)) { return true; }
        
        return false;
    }

     private static int[][] getInts(ChessPiece.PieceType pieceType) {
        int[][] directions = {
                {}
        };
        if (pieceType == PieceType.ROOK) {
            directions = new int[][]{
                    {-1, 0}, //Up
                    {0, -1}, //Right
                    {1, 0}, //Down
                    {0, 1}, //Left
            };
        }
        if (pieceType == PieceType.BISHOP) {
            directions = new int[][]{
                    {-1, -1},
                    {1, -1},
                    {1, 1},
                    {-1, 1},
            };
        }
        if (pieceType == PieceType.PAWN && piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            directions = new int[][]{
                    {1, 0},
                    {1, 1},
                    {1, -1}
            };
        }
        if (pieceType == PieceType.PAWN && piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            directions = new int[][]{
                    {-1, 0},
                    {-1, -1},
                    {-1, 1}
            };
        }
        if (pieceType == PieceType.KING || piece.getPieceType() == PieceType.QUEEN) {
            directions = new int[][]{
                    {-1, 0},
                    {-1, 1},
                    {-1, -1},
                    {1, 0},
                    {1, 1},
                    {0, 1},
                    {0, -1},
                    {1, -1}
            };
        }
        if (pieceType == PieceType.KNIGHT) {
            directions = new int[][]{
                    {-1, 2},
                    {-2, 1},
                    {1, 2},
                    {2, 1},
                    {2, -1},
                    {1, -2},
                    {-1, -2},
                    {-2, -1}
            };
        }
        return directions;
    }

    private boolean handleKnight(ChessPosition start, ChessBoard grid, TeamColor team) {
        int[][] knightMoves = getInts(ChessPiece.Knight);
        for (int[] dir : knightMoves) {
            int deltaRow = dir[0];
            int deltaCol = dir[1];

            int row = start.getRow();
            int col = start.getColumn();

            row += deltaRow;
            col += deltaCol;
            String testing = "test";
            if (row < 1 || row > 8 || col < 1 || col > 8) {
                continue;
            }
            ChessPosition pos = new ChessPosition(row, col);
            var piecePos = grid.getPiece(pos);
            if (piecePos != null) {
                if (piecePos.getTeamColor() != team && piecePos.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean handleKing(ChessPosition start, ChessBoard grid, TeamColor team) {
        int[][] straight = getInts(ChessPiece.Rook);
        int[][] diagonal = getInts(ChessPiece.Bishop);
        int[][][] directions = {straight, diagonal};
        for (int[][] dirSet : directions) {
            for (int[] dir : dirSet) {
                int deltaRow = dir[0];
                int deltaCol = dir[1];

                int row = start.getRow();
                int col = start.getColumn();

                row += deltaRow;
                col += deltaCol;
                String test = "testing";
                if (row < 1 || row > 8 || col < 1 || col > 8) {
                    continue;
                }
                ChessPosition pos = new ChessPosition(row, col);
                var piecePos = grid.getPiece(pos);
                if (piecePos != null) {
                    return (piecePos.getTeamColor() != team && piecePos.getPieceType() == ChessPiece.PieceType.KING);
                }
            }
        }
        return false;
    }
    
    private boolean handlePawn(ChessPosition start, ChessBoard grid, TeamColor team) {
        int row = start.getRow();
        int col = start.getColumn();
        int deltaRow;
        if (team == TeamColor.BLACK) {
            deltaRow = -1;
        } else {
            deltaRow = 1;
        }
        row += deltaRow;
        col -= 1;
        if (row > 0 && row < 9 && col > 0 && col < 9) {
            ChessPosition pos = new ChessPosition(row, col);
            var piecePos = grid.getPiece(pos);
            if (piecePos != null) {
                if (piecePos.getTeamColor() != team && piecePos.getPieceType() == ChessPiece.PieceType.PAWN) {
                    return true;
                }
            }
        }
        col += 2;
        if (row > 0 && row < 9 && col > 0 && col < 9) {
            ChessPosition pos = new ChessPosition(row, col);
            var piecePos = grid.getPiece(pos);
            if (piecePos != null) {
                return piecePos.getTeamColor() != team && piecePos.getPieceType() == ChessPiece.PieceType.PAWN;
            }
        }
        return false;
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
        for (int x = 1; x <= 8; x++) {
            for (int y = 1; y <= 8; y++) {
                ChessPosition pos = new ChessPosition(x, y);
                var piecePos = board.getPiece(pos);
                if (piecePos != null) {
                    if (piecePos.getTeamColor() == teamColor) {
                        if (!validMoves(pos).isEmpty()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(TeamColor.WHITE)) {
            return false;
        }
        if (isInCheck(TeamColor.BLACK)) {
            return false;
        }
        for (int x = 1; x <= 8; x++) {
            for (int y = 1; y <= 8; y++) {
                chess.ChessPosition pos = new ChessPosition(x, y);
                ChessPiece piecePos = board.getPiece(pos);
                if (piecePos != null) {
                    if (piecePos.getTeamColor() == teamColor && !validMoves(pos).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && turn == chessGame.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, turn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", turn=" + turn +
                '}';
    }
}
