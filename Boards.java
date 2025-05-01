/******************************************************************************
 * Boards 
 * 
 * -- Provided --
 * Board isplay & Initialization:
 * Boards() - constructor
 * initBoard()
 * printBoard()
 * displayGameBoards()
 * 
 * Ships Logic:
 * placeShip()
 * decipherCoordinates()
 * markComputersShips()
 * setComputerHits()
 * setPlayerHits()
 * 
 * Checking Methods:
 * checkCountPlayerShips()
 * checkCountCompShips()
 * checkStatusPlayerShips()
 * checkStatusCompShips()
 * 
 * Ship Status Logic:
 * shipDamageCheck()
 * reportShipDamage()
 * allCompShipsSunk()
 * allPlayerShipsSunk()
 * 
 */

import java.util.ArrayList;

public class Boards 
{
    // boards for player and computer's ships/hits
    public char[][] playersShips;
    public char[][] computersShips;
    private char[][] computerHits;
    private char[][] playerHits;

    // initialize "empty" board positions with water (W)
    private final char WATER = 'W';
    // for hits and misses
    private final char HIT = 'H'; 
    private final char MISS = 'O';

    // counts to keep track of player's ships
    private int Acount = 5; 
    private int Bcount = 4;
    private int Ccount = 3;
    private int Dcount = 2;
    private int Scount = 3;

    // counts to keep track of player's ships
    private int AcountComp = 5;
    private int BcountComp = 4;
    private int CcountComp = 3;
    private int DcountComp = 2;
    private int ScountComp = 3;

    // ArrayList to keep track of coordinates/initialize them
    ArrayList<Integer> coordinates;

    public Boards() 
    {
        playersShips = new char[11][11];
        computersShips = new char[11][11];
        computerHits = new char[11][11];
        playerHits = new char[11][11];

        initBoard(playersShips);
        initBoard(computersShips);
        initBoard(computerHits);
        initBoard(playerHits);
    }

    private void initBoard(char[][] board) 
    {
        board[0][0] = ' ';
        for (int i = 1; i <= 10; i++) {
            // row and col headings of 1-10, A-J
            board[0][i] = (char) ('A' + i - 1);
            board[i][0] = (char) ('0' + i / 10);
            if (i < 10) board[i][0] = (char) ('0' + i);
            else board[i][0] = '1';
            // rest of board filled with water 
            for (int j = 1; j <= 10; j++) {
                board[i][j] = WATER;
            }
        }
    }

    // prints board helper method for displayGameBoards()
    private void printBoard(char[][] board) 
    {
        for (int i = 0; i < board.length; i++) 
        {
            for (int j = 0; j < board[i].length; j++) 
            {
                System.out.printf("%-5c", board[i][j]);
            }
            System.out.println();
        }
    }

    // prints player hits and ships board
    public void displayGameBoards() 
    {
        System.out.println("\nHits/Miss Board");
        printBoard(playerHits);
        System.out.println("Your Ships Board");
        printBoard(playersShips);
    } //end comment here to use alternative

    //alternative printing option for intellij -uncomment to use
    /*  for (int i = 1; i <= 10; i++) {
            // row and col headings of 1-10, A-J
            board[0][i] = (char) ('A' + i - 1);
                for (int j = 1; j <= 10; j++)
                    board[i][j] = WATER;}
            int num = 1;//row #'s using charcodes
            for (int i = 9312; i <= 9321; i++){
            board[num][0] = ( char ) i;
            num++;}
        }*/
    // Uncomment above to use

    /* method for the placement of ships using U (up), D (down), 
    left (L), right (R) */

    public void placeShip(int size, int x, int y, char direction, 
    char shipLetter) 
    {
        if (direction == 'U'||direction=='u') 
        {
            for (int i = 0; i < size; i++) 
            {
                this.playersShips[x - i][y] = shipLetter;
            }
        } 
        else if (direction == 'D'||direction=='d') 
        {
            for (int i = 0; i < size; i++) 
            {
                this.playersShips[x + i][y] = shipLetter;
            }
        } 
        else if (direction == 'L'||direction=='l') 
        {
            for (int i = 0; i < size; i++) 
            {
                this.playersShips[x][y - i] = shipLetter;
            }
        } 
        else if (direction == 'R'||direction=='r') 
        {
            for (int i = 0; i < size; i++) 
            {
                this.playersShips[x][y + i] = shipLetter;
            }
        }
    }

    // method where computer's randomly chosen coord is checked
    public boolean checkStatusPlayerShips(int x, int y) 
    {
        if (playersShips[x][y] == WATER) 
        {
            System.out.println("Computer missed!");
            // calls setter method to mark computer's hit/miss board
            setComputerHits(x, y, MISS);
            return true;
        } 
        // checks if computer ALREADY GUESSED the coordinate
        else if (playersShips[x][y] == MISS || playersShips[x][y] == HIT) 
        {
            return false;
        } 
        else 
        {
            // otherwise, modify player's count for the ship comp attacked
            checkCountPlayerShips(x, y);
            // and marks on computer's board 'H'
            setComputerHits(x, y, HIT);
            return true;
        }
    }

    // method to check player's attack coordinates
    public boolean checkStatusCompShips(int x, int y) 
    {
        if (this.computersShips[x][y] == WATER) 
        {
            System.out.println("You missed!");
            // mark player's hit/miss board with 'M'
            setPlayerHits(x, y, MISS);
            return true;
        } 
        else if (this.computersShips[x][y] == MISS || 
        this.computersShips[x][y] == HIT) 
        {
            System.out.println("You already tried that spot. Try again!");
            return false;
        } 
        else 
        {
            // modify computer's count for the ship player attacked
            checkCountCompShips(x, y);
            // mark with 'H'
            setPlayerHits(x, y, HIT);
            return true;
        }
    }

    /* setter methods for player and computer's hits/misses */
    private void setComputerHits(int x, int y, char hitMiss) 
    {
        computerHits[x][y] = hitMiss;
        playersShips[x][y] = hitMiss;
    }

    public void setPlayerHits(int x, int y, char hitMiss) 
    {
        playerHits[x][y] = hitMiss;
        computersShips[x][y] = hitMiss;
    }

    // marker method for computer's randomly generated direction for ships
    public void markComputersShips(int size, int x, int y, char direction, 
    char shipLetter) 
    {
        if (direction == 'U' || direction == 'u' ) 
        {
            for (int i = 0; i < size; i++) 
            {
                this.computersShips[x - i][y] = shipLetter;
            }
        } else if (direction == 'D'||direction=='d') 
        {
            for (int i = 0; i < size; i++) 
            {
                this.computersShips[x + i][y] = shipLetter;
            }
        } else if (direction == 'L'||direction=='l') 
        {
            for (int i = 0; i < size; i++) 
            {
                this.computersShips[x][y - i] = shipLetter;
            }
        } else if (direction == 'R'||direction=='r') 
        {
            for (int i = 0; i < size; i++) 
            {
                this.computersShips[x][y + i] = shipLetter;
            }
        }
    }

    // converts a user input string into integer coordinates [x, y]
    public ArrayList<Integer> decipherCoordinates(String xy) 
    {
        int x = 0;
        int y = 0;
        coordinates = new ArrayList<Integer>();

        if (xy.charAt(0) == 'A') y = 1;
        else if (xy.charAt(0) == 'B'|| xy.charAt(0)=='b') y = 2;
        else if (xy.charAt(0) == 'C'||xy.charAt(0)=='c') y = 3;
        else if (xy.charAt(0) == 'D'||xy.charAt(0)=='d') y = 4;
        else if (xy.charAt(0) == 'E'||xy.charAt(0)=='e') y = 5;
        else if (xy.charAt(0) == 'F'||xy.charAt(0)=='f') y = 6;
        else if (xy.charAt(0) == 'G'||xy.charAt(0)=='g') y = 7;
        else if (xy.charAt(0) == 'H'||xy.charAt(0)=='h') y = 8;
        else if (xy.charAt(0) == 'I'||xy.charAt(0)=='i') y = 9;
        else if (xy.charAt(0) == 'J'||xy.charAt(0)=='j') y = 10;

        if (xy.length() == 2) 
        {
            if (xy.charAt(1) == '1') x = 1;
            else if (xy.charAt(1) == '2') x = 2;
            else if (xy.charAt(1) == '3') x = 3;
            else if (xy.charAt(1) == '4') x = 4;
            else if (xy.charAt(1) == '5') x = 5;
            else if (xy.charAt(1) == '6') x = 6;
            else if (xy.charAt(1) == '7') x = 7;
            else if (xy.charAt(1) == '8') x = 8;
            else if (xy.charAt(1) == '9') x = 9;
            else System.out.println("Invalid coordinate");
        }

        if (xy.length() == 3) 
        {
            if (xy.charAt(1) == '1' && xy.charAt(2) == '0') x = 10;
            else System.out.println("Invalid coordinate");
        }

        coordinates.add(0, x);
        coordinates.add(1, y);
        return coordinates;
    }

    public boolean checkCountPlayerShips(int x, int y) 
    {
        char c = playersShips[x][y];
        // true means player's ship was hit
        return shipDamageCheck(c, true); 
    }

    public boolean checkCountCompShips(int x, int y) 
    {
        char c = computersShips[x][y];
        // false means computer's ship was hit
        return shipDamageCheck(c, false);
    }

    private boolean shipDamageCheck(char c, boolean playerOrComputer) 
    {
        int remaining = -1;
        String shipName = "";

        if (c == 'A') 
        {
            shipName = "Aircraft Carrier";
            // if player (true) or computer's (false) ship is hit
            if (playerOrComputer) 
                // player's was hit
                remaining = --Acount;
            else 
                // computer was hit
                remaining = --AcountComp;
        } 
        else if (c == 'B') 
        {
            shipName = "Battleship";
            if (playerOrComputer) 
                remaining = --Bcount;
            else 
                remaining = --BcountComp;
        } 
        else if (c == 'C') 
        {
            shipName = "Cruiser";
            if (playerOrComputer) 
                remaining = --Ccount;
            else 
                remaining = --CcountComp;
        } 
        else if (c == 'D') 
        {
            shipName = "Destroyer";
            if (playerOrComputer) 
                remaining = --Dcount;
            else 
                remaining = --DcountComp;
        } 
        else if (c == 'S') 
        {
            shipName = "Submarine";
            if (playerOrComputer) 
                remaining = --Scount;
            else 
                remaining = --ScountComp;
        }
        else 
        {
            // otherwise, it's a miss
            return false;
        }
        return reportShipDamage(shipName, remaining, playerOrComputer);
    }
   
    // keeps track of what has been hit/sunk
    private boolean reportShipDamage(String shipName, int count, 
    boolean playerOrComputer) 
    {
        if (count == 0) 
        {
        if (playerOrComputer == false) 
        {
            System.out.println("You sunk the " + shipName + "!");
        } 
        else 
        {
            System.out.println("Computer sunk the " + shipName + "!");
        }
        } 
        else 
        {
        if (playerOrComputer == false) 
        {
            System.out.println("You hit the " + shipName + "!");
        } 
        else 
        {
            System.out.println("Computer hit the " + shipName + "!");
        }
        }

        // check if all player or computer's ships have sunk
        if (playerOrComputer) 
        {
            return allPlayerShipsSunk();
        } 
        else 
        {
            return allCompShipsSunk();
        }
    }

    /* check methods to see if ships have sunk */ 
    public boolean allCompShipsSunk() 
    {
        return AcountComp == 0 && BcountComp == 0 && CcountComp == 0 && 
        DcountComp == 0 && ScountComp == 0;
    }

    public boolean allPlayerShipsSunk() 
    {
        return Acount == 0 && Bcount == 0 && Ccount == 0 && 
        Dcount == 0 && Scount == 0;
    }
}