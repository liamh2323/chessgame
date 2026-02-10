
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

    /*
     * if i store the position of both kings in the main and then check each square
     * in a ring around the king while crossing off all squares that cannot have a
     * check aka edge of board
     * or a piece of the same colour
     * then i keep making the rings bigger until all 8 directions are accounted for/
     * a check has been found
     * 
     * then create an exception class for Knights as a Knight move can never be a
     * revealed check and a check with a Knight must mean that a Knight was the last
     * Piece moved
     * 
     */

    public static boolean allFalse(boolean[] allDirections) {
        for (boolean b : allDirections) {
            if (b) 
                return false;
        }
        return true;
    }

    public static boolean kingBeingAttacked(int finalColumn, int finalRow, int
     whiteKingColumn, int whiteKingRow, int blackKingColumn, int blackKingRow, 
     int countOfMoves, Piece[][] board){

    boolean[] allDirections = new boolean [7];
     
    int ringSize = 0;
     
    boolean negativeX = true;
    boolean positiveX = true;
    boolean negativeY = true;
    boolean positiveY = true;
     
     // for the 4 possible diagonals i usesd the ASTC unit circle and a pneumonic
     // "adam sandler touches children " which decides which diagonal im referring
     
    boolean diagAdam = true;
    boolean diagSandler = true;
    boolean diagTouches = true;
    boolean diagChildren = true;

     allDirections[0] = negativeX;
     allDirections[1] = positiveX;
     allDirections[2] = negativeY;
     allDirections[3] = positiveY;

     allDirections[4] = diagAdam;
     allDirections[5] = diagSandler;
     allDirections[6] = diagTouches;
     allDirections[7] = diagChildren;
     
     if (countOfMoves % 2 == 0) {
    
}

while (!allFalse(allDirections)){

        if ((blackKingRow + ringSize) == 7 && ((!positiveY) || (!diagAdam) || (!diagSandler))){
            positiveY = false;
            diagAdam = false;
            diagSandler = false;
        }
        if ((blackKingRow -ringSize) == 0 && ((!negativeY) || (!diagTouches) || (!diagChildren))){
            negativeY = false;
            diagTouches = false;
            diagChildren = false;
        }
        if ((blackKingColumn - ringSize) == 0 && ((!negativeX) ||  (!diagSandler) || (!diagTouches))){
            negativeX = false;
            diagSandler = false;
            diagTouches = false;
        }
        if ((blackKingColumn + ringSize) == 7 && ((!positiveX)|| (!diagAdam) || (!diagChildren))){
            positiveX = false;
            diagAdam = false; 
            diagChildren = false;
        }

        ringSize ++;


        if(negativeX){
            if(board[blackKingRow][blackKingColumn - ringSize] instanceof Queen.getColour(WHITE) || Rook.getColour(WHITE)){
                return true;
            }
            else{
                negativeX = (board[blackKingRow][blackKingColumn -ringSize] == null);
            }
        }

        if(positiveX){
            if(board[blackKingRow][blackKingColumn +ringSize] instanceof Queen.getColour(WHITE) || Rook.getColour(WHITE) ){
                return true;
            }
            else{
                positiveX = (board[blackKingRow][blackKingColumn +ringSize] == null);
            }
        }

        if(negativeY){
            if(board[blackKingRow -ringSize][blackKingColumn] instanceof Queen.getColour(WHITE) || Rook.getColour(WHITE)){
                return true;
            }
            else{
                negativeY = (board[blackKingRow -ringSize][blackKingColumn] == null);
            }
        }
        if(positiveY){
            if(board[blackKingRow +ringSize][blackKingColumn] instanceof Queen.WHITE || Rook.WHITE){
                return true;
            }
            else{
                positiveY = (board[blackKingRow +ringSize][blackKingColumn] == null);
            }
        }
        if(diagAdam){
            if(board[blackKingRow +ringSize][blackKingColumn +ringSize] instanceof Bishop.WHITE || Queen.WHITE){
                return true;    
            }
            else{
                diagAdam = (board[blackKingRow +ringSize][blackKingColumn +ringSize] == null);
            }
        }

        if(diagSandler){
            if(board[blackKingRow -ringSize][blackKingColumn +ringSize] instanceof Bishop.WHITE ||  Queen.WHITE){
                return true;
            }
            else{
                diagSandler = (board[blackKingRow -ringSize][blackKingColumn +ringSize] == null);
            }
        }

        if(diagTouches){
            if(board[blackKingRow -ringSize][blackKingColumn -ringSize] instanceof Bishop.WHITE ||  Queen.WHITE ||  (Pawn.WHITE && ringSize ==1)){
                return true;
            }
            else{
                diagTouches = (board[blackKingRow -ringSize][blackKingColumn -ringSize] == null);
            }
        }
     
        if(diagChildren){
            if(board[blackKingRow +ringSize][blackKingColumn -ringSize] instanceof Bishop.WHITE || Queen.WHITE || (Pawn.WHITE && ringSize ==1)){
                return true;
            }
            else{
                diagChildren = (board[blackKingRow +ringSize][blackKingColumn -ringSize] == null);
            }
        }
     }
     return false;
     }
