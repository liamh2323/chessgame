package ChessProject;

import java.util.Scanner;

public class Chess {

    public enum Pieces {
        PAWN,
        KNIGHT,
        BISHOP,
        ROOK,
        QUEEN,
        KING,
    }

    public enum GameState {
        CHECK,
        CHECKMATE,
        STALEMATE,
        NORMAL
    }

    public enum whosMove {
        WHITE_MOVED,
        BLACK_MOVED
    }

    public static Piece[][] StartingBoard() {

        Piece[][] newBoard = new Piece[8][8];

        for (int index = 0; index < newBoard.length; index++) {
            newBoard[1][index] = new Pawn(Colour.WHITE);
            newBoard[6][index] = new Pawn(Colour.BLACK);
        }

        newBoard[0][0] = new Rook(Colour.WHITE);
        newBoard[0][7] = new Rook(Colour.WHITE);
        newBoard[7][0] = new Rook(Colour.BLACK);
        newBoard[7][7] = new Rook(Colour.BLACK);

        newBoard[0][1] = new Knight(Colour.WHITE);
        newBoard[0][6] = new Knight(Colour.WHITE);
        newBoard[7][1] = new Knight(Colour.BLACK);
        newBoard[7][6] = new Knight(Colour.BLACK);

        newBoard[0][2] = new Bishop(Colour.WHITE);
        newBoard[0][5] = new Bishop(Colour.WHITE);
        newBoard[7][2] = new Bishop(Colour.BLACK);
        newBoard[7][5] = new Bishop(Colour.BLACK);

        newBoard[0][3] = new Queen(Colour.WHITE);
        newBoard[7][3] = new Queen(Colour.BLACK);

        newBoard[0][4] = new King(Colour.WHITE);
        newBoard[7][4] = new King(Colour.BLACK);

        return newBoard;
    }

    public static int convertCharToChessNotation(char column) {
        int value = column - 'a';
        return value;
    }

    public static int convertIntToChessNotation(int row) {
        int value = row;
        if (value > 8 || value <= 0)
            return -1;
        else
            return (value - 1);
    }

    public static String printBoard(Piece[][] board, int countOfMoves) {
        String newBoard = "";
        if (countOfMoves % 2 == 0) {
            for (int numOfRows = 0; numOfRows < 8; numOfRows++) {
                newBoard += ((numOfRows + 1) + "| ");
                for (int col = 7; col >= 0; col--) {
                    newBoard += ((board[numOfRows][col] != null) ? board[numOfRows][col] : ".");
                    newBoard += (" ");
                }
                newBoard += "\n";
            }

            newBoard += ("   ---------------- ") + "\n" + "   ";
            for (char coord = 'h'; coord >= 'a'; coord--) {
                newBoard += (coord + " ");
            }

        } else {
            for (int numOfRows = 7; numOfRows >= 0; numOfRows--) {
                newBoard += ((numOfRows + 1) + "| ");
                for (int col = 0; col < 8; col++) {
                    newBoard += ((board[numOfRows][col] != null) ? board[numOfRows][col] : ".");
                    newBoard += (" ");
                }
                newBoard += "\n";
            }

            newBoard += ("   ---------------- " + "\n" + "   ");
            for (char coord = 'a'; coord <= 'h'; coord++) {
                newBoard += (coord + " ");
            }
        }
        return newBoard;
    }

    public static void main(String[] args) {

    
        Piece[][] board = new Piece[8][8];
        board = StartingBoard();

        Piece pieceBeingMoved;
        int countOfMoves = 0;
        int whiteKingRow = 0;
        int whiteKingColumn = 4;
        int blackKingRow = 7;
        int blackKingColumn = 4;
        int whiteScore;
        int blackScore;
        boolean validMove = true;
        boolean squareIsFree = true;
        boolean moveIsACheck = false;

        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to chess");
        System.out.println("Enter your move in the format rowColumn -> rowColumn >");
        String move = input.nextLine();
        while (!move.equals("quit")) {

            char column = move.charAt(0);
            int row = Character.getNumericValue(move.charAt(1));
            int initialColumn = convertCharToChessNotation(column);
            int initialRow = convertIntToChessNotation(row);

            column = move.charAt(3);
            row = Character.getNumericValue(move.charAt(4));

            int finalColumn = convertCharToChessNotation(column);
            int finalRow = convertIntToChessNotation(row);

            int colDifference = Math.abs(finalColumn - initialColumn);
            int rowDifference = (finalRow - initialRow);
            int absoluteRowDiff = Math.abs(rowDifference);

            pieceBeingMoved = board[initialRow][initialColumn];
            

            validMove = board[initialRow][initialColumn].isValidMove(initialRow, initialColumn, finalColumn, finalRow,
                    colDifference, rowDifference, absoluteRowDiff, board);

            /*
             * System.out.println(initialColumn);
             * System.out.println(initialRow);
             * System.out.println(finalColumn);
             * System.out.println(finalRow);
             * System.out.println(colDifference);
             * System.out.println(rowDifference);
             */

            moveIsACheck = Moves.kingBeingAttacked(finalColumn, finalRow, whiteKingColumn,
                    whiteKingRow, blackKingColumn, blackKingRow, countOfMoves, board, 
                    pieceBeingMoved, ((pieceBeingMoved.getColour() == Colour.WHITE) ? Colour.BLACK : Colour.WHITE));

            System.out.println(validMove);
            System.out.println(moveIsACheck);

            if ((initialColumn == -1 || initialRow == -1) || (finalColumn == -1 ||
                    finalRow == -1)) {
                validMove = false;
            }

            squareIsFree = Moves.squareIsFree(finalColumn, finalRow, board);

            if (!squareIsFree) {
                validMove = Moves.isValidCapture(finalColumn, finalRow, initialColumn, initialRow,
                        board);
                if (countOfMoves % 2 == 0) {
                    whiteScore = board[finalRow][finalColumn].getValue();
                } else {
                    blackScore = board[finalRow][finalColumn].getValue();
                }
            }

            validMove = Moves.noObstruction(initialRow, initialColumn, finalColumn, finalRow, colDifference,
                    rowDifference,
                    absoluteRowDiff, board);

            whiteScore = 0;
            if (validMove) {

                if (board[initialRow][initialColumn] instanceof King) {
                    if (countOfMoves % 2 == 0) {
                        whiteKingColumn = finalColumn;
                        whiteKingRow = finalRow;
                    }
                    blackKingColumn = finalColumn;
                    blackKingRow = finalRow;
                }

                board[finalRow][finalColumn] = board[initialRow][initialColumn];
                board[initialRow][initialColumn] = null;

                System.out.print(printBoard(board, countOfMoves));

                System.out.println();
                countOfMoves++;
                System.out.println("Enter your next move >");
                move = input.nextLine();
            } else {
                System.out.println("error. invalid move retard.");
                System.out.println("Enter an actual move >");
                move = input.nextLine();
            }
        }

        input.close();
    }
}