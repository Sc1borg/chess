package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition start) {
        ChessPiece piece = board.getPiece(start);
        if (piece == null) {return null;}
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] directions = getInts(piece);
        int row;
        int col;
        if (piece.getPieceType() == PieceType.QUEEN || piece.getPieceType() == PieceType.BISHOP || piece.getPieceType() == PieceType.ROOK) {
            for (int[] dir : directions) {
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
                    if (piecePos == null) {
                        moves.add(new ChessMove(start, pos, null));
                    } else if (piecePos.getTeamColor() != piece.getTeamColor()) {
                        moves.add(new ChessMove(start, pos, null));
                        break;
                    } else {
                        break;
                    }
                }
            }
        }
        else if (piece.getPieceType() == PieceType.KING || piece.getPieceType() == PieceType.KNIGHT) {
            for (int[] dir : directions) {
                int deltaRow = dir[0];
                int deltaCol = dir[1];

                row = start.getRow();
                col = start.getColumn();
                row += deltaRow;
                col += deltaCol;

                if (row < 1 || row > 8 || col < 1 || col > 8) {
                    continue;
                }
                ChessPosition pos = new ChessPosition(row, col);
                var piecePos = board.getPiece(pos);
                if (piecePos == null) {
                    moves.add(new ChessMove(start, pos, null));
                } else if (piecePos.getTeamColor() != piece.getTeamColor()) {
                    moves.add(new ChessMove(start, pos, null));
                }
            }
        }
        else if (piece.getPieceType() == PieceType.PAWN){
            for (int[] dir : directions) {
                int deltaRow = dir[0];
                int deltaCol = dir[1];

                row = start.getRow();
                col = start.getColumn();
                row += deltaRow;
                col += deltaCol;

                if (row < 1 || row > 8 || col < 1 || col > 8) {
                    continue;
                }
                ChessPosition pos = new ChessPosition(row, col);
                var piecePos = board.getPiece(pos);
                if (piecePos == null) {
                    if (col == start.getColumn()) {
                        if (row == 8 || row == 1) {
                            for (var promo : PieceType.values()) {
                                if (promo == PieceType.PAWN || promo == PieceType.KING) {continue;}
                                moves.add(new ChessMove(start, pos, promo));
                            }
                        }
                        else {moves.add(new ChessMove(start, pos, null));}
                    }
                    else {continue;}
                }
                if (col != start.getColumn() && piecePos != null && piecePos.getTeamColor() != piece.getTeamColor()) {
                    if (row == 8 || row == 1) {
                        for (var promo : PieceType.values()) {
                            if (promo == PieceType.PAWN || promo == PieceType.KING) {continue;}
                            moves.add(new ChessMove(start, pos, promo));
                        }
                    }
                    else {moves.add(new ChessMove(start, pos, null));}
                }
                if (start.getRow() == 2 && piecePos == null && piece.getTeamColor()== ChessGame.TeamColor.WHITE) {
                    row += deltaRow;
                    pos = new ChessPosition(row, col);
                    piecePos = board.getPiece(pos);
                    if (piecePos == null) {
                        moves.add(new ChessMove(start, pos, null));
                    }
                }
                if (start.getRow() == 7 && piecePos == null && piece.getTeamColor()== ChessGame.TeamColor.BLACK) {
                    row += deltaRow;
                    pos = new ChessPosition(row, col);
                    piecePos = board.getPiece(pos);
                    if (piecePos == null) {
                        moves.add(new ChessMove(start, pos, null));
                    }

                }

            }
        }
        return moves;
    }

    private static int[][] getInts(ChessPiece piece) {
        int[][] directions = {
                {}
        };
        if (piece.getPieceType() == PieceType.ROOK) {
            directions = new int[][]{
                    {-1, 0}, //Up
                    {0, -1}, //Right
                    {1, 0}, //Down
                    {0, 1}, //Left
            };
        }
        if (piece.getPieceType() == PieceType.BISHOP) {
            directions = new int[][]{
                    {-1, -1},
                    {1, -1},
                    {1, 1},
                    {-1, 1},
            };
        }
        if (piece.getPieceType() == PieceType.PAWN && piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            directions = new int[][]{
                    {1, 0},
                    {1, 1},
                    {1, -1}
            };
        }
        if (piece.getPieceType() == PieceType.PAWN && piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            directions = new int[][]{
                    {-1, 0},
                    {-1, -1},
                    {-1, 1}
            };
        }
        if (piece.getPieceType() == PieceType.KING || piece.getPieceType() == PieceType.QUEEN) {
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
        if (piece.getPieceType() == PieceType.KNIGHT) {
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


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChessPiece that = (ChessPiece) obj;
        return (getTeamColor() == that.getTeamColor()) && (getPieceType() == that.getPieceType());
    }

    @Override
    public int hashCode() {
        int result = getTeamColor().hashCode();
        result = 31 * result + getPieceType().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ChessPiece: " + getPieceType().toString() + " Color: " + getTeamColor().toString();
    }
}
