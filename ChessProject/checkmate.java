package ChessProject;

public class checkmate {

    public static boolean isCheckmate(int whiteKingColumn, int whiteKingRow,
                                     int blackKingColumn, int blackKingRow,
                                     Piece[][] board, Colour kingColour) {
        Moves.CheckInfo checkInfo = Moves.kingBeingAttacked(0, 0,
                whiteKingColumn, whiteKingRow, blackKingColumn, blackKingRow,
                0, board, null, kingColour);

        if (!checkInfo.isInCheck()) {
            return false;
        }

        int[] attacker = checkInfo.getAttackerPosition();
        if (attacker == null || attacker.length != 2) {
            return false;
        }

        int kingRow = (kingColour == Colour.BLACK) ? blackKingRow : whiteKingRow;
        int kingColumn = (kingColour == Colour.BLACK) ? blackKingColumn : whiteKingColumn;

        if (canKingEscape(kingRow, kingColumn, board, kingColour,
                whiteKingRow, whiteKingColumn, blackKingRow, blackKingColumn)) {
            return false;
        }

        if (canCaptureAttacker(attacker[0], attacker[1], board, kingColour,
                whiteKingRow, whiteKingColumn, blackKingRow, blackKingColumn)) {
            return false;
        }

        if (canBlockAttack(kingRow, kingColumn, attacker[0], attacker[1], board, kingColour,
                whiteKingRow, whiteKingColumn, blackKingRow, blackKingColumn)) {
            return false;
        }

        return true;
    }

    private static boolean canKingEscape(int kingRow, int kingColumn, Piece[][] board, Colour kingColour,
                                          int whiteKingRow, int whiteKingColumn,
                                          int blackKingRow, int blackKingColumn) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int nr = kingRow + dr;
                int nc = kingColumn + dc;
                if (nr < 0 || nr >= 8 || nc < 0 || nc >= 8) continue;

                Piece target = board[nr][nc];
                if (target != null && target.getColour() == kingColour) continue;

                Piece[][] copy = copyBoard(board);
                copy[kingRow][kingColumn] = null;
                copy[nr][nc] = new King(kingColour);

                int newWhiteKingRow = (kingColour == Colour.WHITE) ? nr : whiteKingRow;
                int newWhiteKingColumn = (kingColour == Colour.WHITE) ? nc : whiteKingColumn;
                int newBlackKingRow = (kingColour == Colour.BLACK) ? nr : blackKingRow;
                int newBlackKingColumn = (kingColour == Colour.BLACK) ? nc : blackKingColumn;

                if (!Moves.kingBeingAttacked(0, 0,
                        newWhiteKingColumn, newWhiteKingRow,
                        newBlackKingColumn, newBlackKingRow,
                        0, copy, null, kingColour).isInCheck()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean canCaptureAttacker(int attackerRow, int attackerColumn,
                                              Piece[][] board, Colour kingColour,
                                              int whiteKingRow, int whiteKingColumn,
                                              int blackKingRow, int blackKingColumn) {

        Colour defenderColour = kingColour;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p == null || p.getColour() != defenderColour) continue;

                int colDiff = Math.abs(attackerColumn - c);
                int rowDiff = attackerRow - r;
                int absRowDiff = Math.abs(rowDiff);

                if (!p.isValidMove(r, c, attackerColumn, attackerRow, colDiff, rowDiff, absRowDiff, board)) continue;
                if (!Moves.noObstruction(r, c, attackerColumn, attackerRow, colDiff, rowDiff, absRowDiff, board)) continue;

                Piece[][] copy = copyBoard(board);
                copy[attackerRow][attackerColumn] = p;
                copy[r][c] = null;

                int newWhiteRow = (defenderColour == Colour.WHITE) ? attackerRow : whiteKingRow;
                int newWhiteCol = (defenderColour == Colour.WHITE) ? attackerColumn : whiteKingColumn;
                int newBlackRow = (defenderColour == Colour.BLACK) ? attackerRow : blackKingRow;
                int newBlackCol = (defenderColour == Colour.BLACK) ? attackerColumn : blackKingColumn;

                if (!Moves.kingBeingAttacked(0, 0,
                        newWhiteCol, newWhiteRow, newBlackCol, newBlackRow,
                        0, copy, null, kingColour).isInCheck()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean canBlockAttack(int kingRow, int kingColumn,
                                          int attackerRow, int attackerColumn,
                                          Piece[][] board, Colour kingColour,
                                          int whiteKingRow, int whiteKingColumn,
                                          int blackKingRow, int blackKingColumn) {
        Piece attacker = board[attackerRow][attackerColumn];
        if (attacker == null) return false;
        if (attacker instanceof Knight || attacker instanceof Pawn) {
            return false; // knight/pawn cannot be blocked
        }

        int dr = Integer.signum(kingRow - attackerRow);
        int dc = Integer.signum(kingColumn - attackerColumn);

        int r = attackerRow + dr;
        int c = attackerColumn + dc;
        while (r != kingRow || c != kingColumn) {
            for (int sr = 0; sr < 8; sr++) {
                for (int sc = 0; sc < 8; sc++) {
                    Piece p = board[sr][sc];
                    if (p == null || p.getColour() != kingColour || p instanceof King) continue;

                    int colDiff = Math.abs(c - sc);
                    int rowDiff = r - sr;
                    int absRowDiff = Math.abs(rowDiff);

                    if (!p.isValidMove(sr, sc, c, r, colDiff, rowDiff, absRowDiff, board)) continue;
                    if (!Moves.noObstruction(sr, sc, c, r, colDiff, rowDiff, absRowDiff, board)) continue;

                    Piece[][] copy = copyBoard(board);
                    copy[r][c] = p;
                    copy[sr][sc] = null;

                    int newWhiteRow = (kingColour == Colour.WHITE) ? whiteKingRow : kingRow;
                    int newWhiteCol = (kingColour == Colour.WHITE) ? whiteKingColumn : kingColumn;
                    int newBlackRow = (kingColour == Colour.BLACK) ? blackKingRow : kingRow;
                    int newBlackCol = (kingColour == Colour.BLACK) ? blackKingColumn : kingColumn;

                    if (!Moves.kingBeingAttacked(0, 0, newWhiteCol, newWhiteRow, newBlackCol, newBlackRow,
                            0, copy, null, kingColour).isInCheck()) {
                        return true;
                    }
                }
            }

            r += dr;
            c += dc;
        }
        return false;
    }

    private static Piece[][] copyBoard(Piece[][] board) {
        Piece[][] copy = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 8);
        }
        return copy;
    }
}
