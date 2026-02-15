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

public static boolean kingBeingAttacked(int finalColumn, int finalRow, int
     whiteKingColumn, int whiteKingRow, int blackKingColumn, int blackKingRow, 
     int countOfMoves, Piece[][] board, Piece pieceBeingMoved, Colour kingColour){

    boolean[] allDirections = new boolean [8];
     
    int ringSize = -1;
    Piece piece;
    int kingRow = (kingColour == Colour.BLACK) ? blackKingRow : whiteKingRow;
    int kingColumn = (kingColour == Colour.BLACK) ? blackKingColumn : whiteKingColumn;
    Colour enemyColour = (kingColour == Colour.BLACK) ? Colour.WHITE : Colour.BLACK;
    int pawnDiag1 = (enemyColour == Colour.WHITE) ? DIAG_TOUCHES : DIAG_ADAM;
    int pawnDiag2 = (enemyColour == Colour.WHITE) ? DIAG_SANDLER : DIAG_CHILDREN;
     
     allDirections[0] = true;
     allDirections[1] = true;
     allDirections[2] = true;
     allDirections[3] = true;
     allDirections[4] = true;
     allDirections[5] = true;
     allDirections[6] = true;
     allDirections[7] = true;

    if(pieceBeingMoved instanceof Knight){
        int KnightColDiff = Math.abs(finalColumn - kingColumn);
        int KnightRowDiff = Math.abs(finalRow - kingRow);
        if((KnightColDiff == 1 && KnightRowDiff == 2) || (KnightColDiff == 2 && KnightRowDiff == 1)){
            return true;
        }
        else {
            return false;
        }
    }
     
    while (!allFalse(allDirections)){

        ringSize++;

        if ((kingRow + ringSize) >= 7){
            allDirections[POSITIVE_Y] = false;
            allDirections[DIAG_ADAM] = false;
            allDirections[DIAG_SANDLER] = false;
        }
        if ((kingRow - ringSize) <= 0){
            allDirections[NEGATIVE_Y] = false;
            allDirections[DIAG_TOUCHES] = false;
            allDirections[DIAG_CHILDREN] = false;
        }
        if ((kingColumn - ringSize) <= 0){
            allDirections[NEGATIVE_X] = false;
            allDirections[DIAG_SANDLER] = false;
            allDirections[DIAG_TOUCHES] = false;
        }
        if ((kingColumn + ringSize) >= 7){
            allDirections[POSITIVE_X] = false;
            allDirections[DIAG_ADAM] = false; 
            allDirections[DIAG_CHILDREN] = false;
        }

        if(allDirections[NEGATIVE_X]){
            piece = board[kingRow][kingColumn - ringSize];
            if((piece instanceof Queen || piece instanceof Rook) && piece.getColour() == enemyColour){
                return true;
            }
            else{
                allDirections[NEGATIVE_X] = (piece == null);
            }
        }

        if(allDirections[POSITIVE_X]){
            piece = board[kingRow][kingColumn + ringSize];
            if((piece instanceof Queen || piece instanceof Rook) && piece.getColour() == enemyColour){
                return true;
            }
            else{
                allDirections[POSITIVE_X] = (piece == null);
            }
        }

        if(allDirections[NEGATIVE_Y]){
            piece = board[kingRow - ringSize][kingColumn];
            if((piece instanceof Queen || piece instanceof Rook) && piece.getColour() == enemyColour){
                return true;
            }
            else{
                allDirections[NEGATIVE_Y] = (piece == null);
            }
        }

        if(allDirections[POSITIVE_Y]){
            piece = board[kingRow + ringSize][kingColumn];
            if((piece instanceof Queen || piece instanceof Rook) && piece.getColour() == enemyColour){
                return true;
            }
            else{
                allDirections[POSITIVE_Y] = (piece == null);
            }
        }

        if(allDirections[DIAG_ADAM]){
            piece = board[kingRow + ringSize][kingColumn + ringSize];
            if((piece instanceof Bishop || piece instanceof Queen) && piece.getColour() == enemyColour){
                return true;    
            }
            else if(piece instanceof Pawn && piece.getColour() == enemyColour && ringSize == 1 && DIAG_ADAM == pawnDiag1){
                return true;
            }
            else{
                allDirections[DIAG_ADAM] = (piece == null);
            }
        }

        if(allDirections[DIAG_SANDLER]){
            piece = board[kingRow - ringSize][kingColumn + ringSize];
            if((piece instanceof Bishop || piece instanceof Queen) && piece.getColour() == enemyColour){
                return true;
            }
            else if(piece instanceof Pawn && piece.getColour() == enemyColour && ringSize == 1 && DIAG_SANDLER == pawnDiag2){
                return true;
            }
            else{
                allDirections[DIAG_SANDLER] = (piece == null);
            }
        }

        if(allDirections[DIAG_TOUCHES]){
            piece = board[kingRow - ringSize][kingColumn - ringSize];
            if((piece instanceof Bishop || piece instanceof Queen) && piece.getColour() == enemyColour){
                return true;
            }
            else if(piece instanceof Pawn && piece.getColour() == enemyColour && ringSize == 1 && DIAG_TOUCHES == pawnDiag1){
                return true;
            }
            else{
                allDirections[DIAG_TOUCHES] = (piece == null);
            }
        }
     
        if(allDirections[DIAG_CHILDREN]){
            piece = board[kingRow + ringSize][kingColumn - ringSize];
            if((piece instanceof Bishop || piece instanceof Queen) && piece.getColour() == enemyColour){
                return true;
            }
            else if(piece instanceof Pawn && piece.getColour() == enemyColour && ringSize == 1 && DIAG_CHILDREN == pawnDiag2){
                return true;
            }
            else{
                allDirections[DIAG_CHILDREN] = (piece == null);
            }   
        }
    }
    return false;
    }
}