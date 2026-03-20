package ChessProject;

public class Moves {

    static final int NEGATIVE_X = 0;
    static final int POSITIVE_X = 1;
    static final int NEGATIVE_Y = 2;
    static final int POSITIVE_Y = 3;
    static final int DIAG_ADAM = 4;
    static final int DIAG_SANDLER = 5;
    static final int DIAG_TOUCHES = 6;
    static final int DIAG_CHILDREN = 7;

    public static boolean squareIsFree(int finalColumn, int finalRow, Piece[][] board) {
        return board[finalRow][finalColumn] == null;
    }

    public static boolean isValidCapture(int finalColumn, int finalRow, int initialColumn, int initialRow,
            Piece[][] board) {
        boolean validCapture = true;
        Piece predator = board[initialRow][initialColumn];
        Piece prey = board[finalRow][finalColumn];

        if (prey instanceof King) {
            validCapture = false;
        } else if (predator.getColour() == prey.getColour()) {
            validCapture = false;
        }
        return validCapture;
    }

    public static boolean noObstruction(int initialRow, int initialColumn, int finalColumn, int finalRow,
            int colDifference, int rowDifference, int absoluteRowDiff, Piece[][] board) {
        boolean noObstruction = true;
        int numberOfBlockingPieces = 0;
        int smallerNumber = 0;
        int biggerNumber = 0;

        if (board[initialRow][initialColumn] instanceof Knight)
            noObstruction = true;

        else if (colDifference != 0) {
            if (finalColumn - initialColumn > 0) {
                smallerNumber = initialColumn;
                biggerNumber = finalColumn;
            } else {
                smallerNumber = finalColumn;
                biggerNumber = initialColumn;
            }
            for (int i = smallerNumber; i < biggerNumber; i++) {
                if (board[smallerNumber][i] != null)
                    numberOfBlockingPieces++;
            }
            if (numberOfBlockingPieces > 1) {
                noObstruction = false;
            }
        } else if (absoluteRowDiff != 0) {
            if (finalRow - initialRow > 0) {
                smallerNumber = initialRow;
                biggerNumber = finalRow;
            } else {
                smallerNumber = finalRow;
                biggerNumber = initialRow;
            }
            for (int i = smallerNumber; i < biggerNumber; i++) {
                if (board[i][smallerNumber] != null) {
                    numberOfBlockingPieces++;
                }
                if (numberOfBlockingPieces > 1) {
                    noObstruction = false;
                }

            }
        }

        return noObstruction;
    }


    public static boolean allFalse(boolean[] allDirections) {
        for (boolean b : allDirections) {
            if (b) 
                return false;
        }
        return true;
    }

    public static class CheckInfo {
        private final boolean inCheck;
        private final int[] attackerPosition;

        public CheckInfo(boolean inCheck, int[] attackerPosition) {
            this.inCheck = inCheck;
            this.attackerPosition = attackerPosition;
        }

        public boolean isInCheck() {
            return inCheck;
        }

        public int[] getAttackerPosition() {
            return attackerPosition;
        }
    }

    public static CheckInfo kingBeingAttacked(int finalColumn, int finalRow, int
     whiteKingColumn, int whiteKingRow, int blackKingColumn, int blackKingRow, 
     int countOfMoves, Piece[][] board, Piece pieceBeingMoved, Colour kingColour){

        int kingRow = (kingColour == Colour.BLACK) ? blackKingRow : whiteKingRow;
        int kingColumn = (kingColour == Colour.BLACK) ? blackKingColumn : whiteKingColumn;
        Colour enemyColour = (kingColour == Colour.BLACK) ? Colour.WHITE : Colour.BLACK;

        // knight threats
        int[][] knightMoves = {{-2,-1},{-2,1},{-1,-2},{-1,2},{1,-2},{1,2},{2,-1},{2,1}};
        for (int[] km : knightMoves) {
            int r = kingRow + km[0];
            int c = kingColumn + km[1];
            if (r >= 0 && r < 8 && c >= 0 && c < 8) {
                Piece p = board[r][c];
                if (p instanceof Knight && p.getColour() == enemyColour) {
                    return new CheckInfo(true, new int[]{r, c});
                }
            }
        }

        // pawn threat: enemy pawns attack king square
        int pawnDirection = (enemyColour == Colour.WHITE) ? 1 : -1;
        int[] pawnAttackCols = {kingColumn - 1, kingColumn + 1};
        for (int c : pawnAttackCols) {
            int r = kingRow - pawnDirection; // from pawn perspective to king
            if (r >= 0 && r < 8 && c >= 0 && c < 8) {
                Piece p = board[r][c];
                if (p instanceof Pawn && p.getColour() == enemyColour) {
                    return new CheckInfo(true, new int[]{r, c});
                }
            }
        }

        // straight and diagonal threats from sliding pieces
        int[][] directions = {{0,1},{0,-1},{1,0},{-1,0},{1,1},{1,-1},{-1,1},{-1,-1}};
        for (int[] d : directions) {
            int r = kingRow + d[0];
            int c = kingColumn + d[1];
            while (r >= 0 && r < 8 && c >= 0 && c < 8) {
                Piece p = board[r][c];
                if (p != null) {
                    if (p.getColour() == enemyColour) {
                        if ((d[0] == 0 || d[1] == 0) && (p instanceof Rook || p instanceof Queen)) {
                            return new CheckInfo(true, new int[]{r, c});
                        }
                        if (Math.abs(d[0]) == Math.abs(d[1]) && (p instanceof Bishop || p instanceof Queen)) {
                            return new CheckInfo(true, new int[]{r, c});
                        }
                    }
                    break;
                }
                r += d[0];
                c += d[1];
            }
        }

        return new CheckInfo(false, null);
    }
}