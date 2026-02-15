// toString () will print all attributes of my class

package ChessProject;

public abstract class Piece {
    protected Colour colour;
    protected int value;

    public Piece(Colour colour, int value) {
        this.colour = colour;
        this.value = value;
    }

    public Colour getColour() {
        return colour;
    }

    public int getValue() {
        return value;
    }

    public abstract boolean isValidMove(int initialRow, int initialColumn, int finalColumn,
            int finalRow, int colDifference, int rowDifference, int absoluteRowDiff, Piece[][] board);

    public abstract String toString();
}

class Pawn extends Piece {
    public Pawn(Colour colour) {
        super(colour, 1);
    }

    @Override
    public boolean isValidMove(int initialRow, int initialColumn, int finalColumn,
            int finalRow, int colDifference, int rowDifference, int absoluteRowDiff, Piece[][] board) {
        boolean validMove = true;

        int direction = (colour == Colour.WHITE) ? 1 : -1;

        if (colDifference == 0 && rowDifference == direction) {
            validMove = true;
        }

        else if ((colDifference == 0) && (initialRow == 1 || initialRow == 6) &&
                (rowDifference == 2 || rowDifference == -2) && (board[finalRow][finalColumn] == null)) {
            validMove = true;

        } else if ((colDifference == 1 && rowDifference == 1) && (board[finalRow][finalColumn] != null))
            validMove = true;

        else {
            validMove = false;
        }
        return validMove;
    }

    public String toString() {
        return colour == Colour.WHITE ? "P" : "p";
    }

}

class Bishop extends Piece {
    public Bishop(Colour colour) {
        super(colour, 3);
    }

    @Override
    public boolean isValidMove(int initialRow, int initialColumn, int finalColumn,
            int finalRow, int colDifference, int absoluteRowDiff, int rowDifference, Piece[][] board) {
        return (absoluteRowDiff == colDifference);
    }

    public String toString() {
        return colour == Colour.WHITE ? "B" : "b";
    }
}

class Rook extends Piece {
    public Rook(Colour colour) {
        super(colour,5);
    }

    @Override
    public boolean isValidMove(int initialRow, int initialColumn, int finalColumn, int finalRow,
            int colDifference, int rowDifference, int absoluteRowDiff, Piece[][] board) {
        return ((colDifference == 0) ^ (absoluteRowDiff == 0));
    }

    public String toString() {
        return colour == Colour.WHITE ? "R" : "r";
    }
}

class Knight extends Piece {
    public Knight(Colour colour) {
        super(colour,3);
    }

    @Override
    public boolean isValidMove(int initialRow, int initialColumn, int finalColumn,
            int finalRow, int colDifference, int rowDifference, int absoluteRowDiff, Piece[][] board) {

        return ((colDifference == 1 && absoluteRowDiff == 2) || (colDifference == 2 && absoluteRowDiff == 1));

    }

    public String toString() {
        return colour == Colour.WHITE ? "N" : "n";
    }
}

class Queen extends Piece {
    public Queen(Colour colour) {
        super(colour, 8);
    }

    @Override
    public boolean isValidMove(int initialRow, int initialColumn, int finalColumn, int finalRow,
            int colDifference, int rowDifference, int absoluteRowDiff, Piece[][] board) {
        return true;
    }

    public String toString() {
        return colour == Colour.WHITE ? "Q" : "q";
    }
}

class King extends Piece {
    public King(Colour colour) {
        super(colour,100);
    }

    @Override
    public boolean isValidMove(int initialRow, int initialColumn, int finalColumn, int finalRow,
            int colDifference, int rowDifference, int absoluteRowDiff, Piece[][] board) {
        boolean validKingMove = true;
        if (absoluteRowDiff > 1 || colDifference > 1)
            validKingMove = false;

        return validKingMove;
    }

    public String toString() {
        return colour == Colour.WHITE ? "K" : "k";
    }
}