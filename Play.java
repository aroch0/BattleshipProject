/******************************************************************************
 * Game Mechanics
 * 
 * -- Provided --
 * Introduction:
 * Play() - constructor
 * displayShips()
 * 
 * Game Logic:
 * playerDirectedShips()
 * computerDirectedShips()
 * playerTurn()
 * computerTurn()
 * 
 * Checking Methods:
 * validatePlacement()
 * isValidCoordinate()
 * 
 */

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Play extends Boards
{
    // Play variables

    private Scanner in = new Scanner(System.in);
    // initializes the fixed ships for both player and computer
    public Ship playerShips = new Ship();
    public Ship[] pShips = playerShips.initializeShips();
    public Ship computerShips = new Ship();
    public Ship[] cShips = computerShips.initializeShips();
    
    // official method of the game's entire function
    public Play() 
    {
        // introduction
        System.out.println("Welcome to Battleship!");
        System.out.print("Enter player name: ");
        String playerName = in.nextLine();

        Player player = new Player(playerName);

        // calls displayShips to display the "fixed" ships
        displayShips();

        System.out.println("\n" + player.getName() + ", here are your boards:");
        displayGameBoards();

        // calls this method so that player can place their ships
        playerDirectedShips();

        // this method is a randomized ship placement method for computer
        computerDirectedShips();

        boolean gameOver = false;
        // while gameOver is not true
        while (!gameOver)
        {
            // alternates between player and computer
            playerTurn();
            computerTurn();
            // displays marked h/m board
            System.out.println(
            "\nYour H/M board has been updated, displaying your gameboards:");
            displayGameBoards();

            // checks if computer or player's ships board are all sunk
            // and displays appropriate winner
            if (allCompShipsSunk()) 
            {
                System.out.println("Congratulations! " + playerName + " wins!");
                gameOver = true;
            } else if (allPlayerShipsSunk()) 
            {
                System.out.println("Game over! Computer wins!");
                gameOver = true;
            }
        }

        // loop breaks when gameOver == true, now concluding game is over.
        System.out.println("Game Over!");
    }

    // method to display "fixed" ships using initializeShips() in Ship class
    public void displayShips()
    {
        System.out.println("\n-- Provided ships -- ");

        // loops through initializeShips()'s array and prints its info
        for (Ship ship : pShips)
        {
            System.out.println(ship.getShipName() + " (Size: " + 
            ship.getShipSize() + ")");
        }

        /* just to let user know that computer ships have been initialized as 
        well (class variables)*/
        System.out.println("Computer ships have been initialized.");

    }

    // directs player to place their ships
    public void playerDirectedShips()
    {
        // loops through player's array of ships
        for (Ship ship : pShips)
        {
            boolean placed = false;
            while (!placed)
            {
                System.out.println("\nPlacing " + ship.getShipName() + 
                " (Size: " + ship.getShipSize() + ")");

                String start = "";
                boolean valid = false;
                // loop for validating coordinates
                while (!valid) 
                {
                    System.out.print("Enter starting coordinate (e.g., A1): ");
                    start = in.nextLine().toUpperCase();
                    if (isValidCoordinate(start)) 
                    {
                        valid = true;
                    } 
                    else 
                    {
                        System.out.println(
                            "Invalid coordinate. Try again (A1 to J10).");
                    }
                }

                char direction = ' ';
                boolean validDirection = false;
                // loop for validating UDLR direction
                while (!validDirection) 
                {
                    System.out.print("Enter direction (U, D, L, R): ");
                    String dirInput = in.nextLine().toUpperCase();

                    if (dirInput.length() == 1 && 
                    (dirInput.equals("U") || dirInput.equals("D") 
                    || dirInput.equals("L") || dirInput.equals("R"))) 
                    {
                        direction = dirInput.charAt(0);
                        validDirection = true;
                    } 
                    else 
                    {
                        System.out.println(
                            "Invalid direction. Please enter U,D, L, or R.");
                    }
                }

                /* convert the user input (like "A1") into integer (number) 
                board coordinates */
                ArrayList<Integer> coordsList = decipherCoordinates(start);
                // gets the coords in the coordsList and sets it (x, y)
                int x = coordsList.get(0);
                int y = coordsList.get(1);

                /* check if the ship can be placed at the chosen position and 
                direction */
                if (validatePlacement(playersShips, x, y, direction, 
                ship.getShipSize()))
                {
                    // eet the first letter of the ship's name as its identifier
                    char shipLetter = ship.getShipName().toUpperCase().
                    charAt(0);
                    // places the ship using method from Boards class
                    placeShip(ship.getShipSize(), x, y, direction, shipLetter);
                    displayGameBoards();
                    placed = true;
                }
                else
                {
                    System.out.println("Can't place ship here. Try again.");
                }
            }
        }
    }

    // randomizes computer's ship placement
    public void computerDirectedShips()
    {
        Random placement = new Random();
        char[] directions = {'U', 'D', 'L', 'R'};

        // loops through computer ships
        for (Ship ship : cShips)
        {
            boolean comPlaced = false;
            // until a valid placement is found
            while (!comPlaced)
            {
                // generates random starting coordinates (1–10)
                int x =(int)(Math.random()*10)+1;
                int y =(int)(Math.random()*10)+1;
                // randomly chooses its up, down, left, right direction
                char randDirection = directions[placement.nextInt
                (directions.length)];
                char letter = ship.getShipName().toUpperCase().charAt(0);

                // validates randomly generated coords and direction
                if (validatePlacement(computersShips, x, y, randDirection, 
                ship.getShipSize()))
                {
                    // marks computer ship using the method from Boards class
                   markComputersShips(ship.getShipSize(), x, y, randDirection, 
                   letter);
                   comPlaced = true;
                }
            }
        }
        System.out.println("\nComputer has placed its ships.");
        System.out.println("\nAll ships placed. Ready for battle!");
    }

    // method for player's turn
    public void playerTurn()
    {
        boolean validGuess = false;
        while (!validGuess) 
        {
            System.out.print("\nEnter a coordinate to attack (e.g., A5): ");
            String input = in.nextLine().toUpperCase();
            ArrayList<Integer> coords = decipherCoordinates(input);
            int x = coords.get(0);
            int y = coords.get(1);

            /*call method from Boards class to check if guessed coord is water 
            or ship */
            validGuess = checkStatusCompShips(x, y);
        }
    }

    // method for computer's turn
    public void computerTurn()
    {
        boolean validGuess = false;
        while (!validGuess) 
        {
            int x =(int)(Math.random()*10)+1;
            int y =(int)(Math.random()*10)+1;

            /* method from Boards class to check if randomly guessed coord is 
            water or ship */
            validGuess = checkStatusPlayerShips(x, y);

            /*
             * x = 1-10
             * y = A-J, but ints so it's also 1-10
             * So, weirdly enough: J5 = 5 10(J)
             */
            if (validGuess == true) 
            {
                System.out.println("Computer guessed: " + x + y);
            }
        }
    }

    /*
     * created a validate placement helper method to check if
     * the coordinates/moves is valid for the ships to be placed
     */
    public boolean validatePlacement(char[][] board, int x, int y, char direction, int size)
    {
        try
        {
            if (direction == 'U')
            {
                for (int i = 0; i < size; i++)
                {
                    if (board[x - i][y] != 'W') return false;
                }
            }
            else if (direction == 'D')
            {
                for (int i = 0; i < size; i++)
                {
                    if (board[x + i][y] != 'W') return false;
                }
            }
            else if (direction == 'L')
            {
                for (int i = 0; i < size; i++)
                {
                    if (board[x][y - i] != 'W') return false;
                }
            }
            else if (direction == 'R')
            {
                for (int i = 0; i < size; i++)
                {
                    if (board[x][y + i] != 'W') return false;
                }
            }
            else
            {
                // placement invalid
                return false;
            }
            // placement valid
            return true;
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            // if index goes out of bounds, invalid
            return false;
        }
    }

    public boolean isValidCoordinate(String input) 
    {
        // check if input is null or has invalid length < or > 2
        if (input == null || input.length() < 2 || input.length() > 3)
        {
            return false;
        }

        char firstChar = input.charAt(0);
        String number = input.substring(1);
    
        // checks firstChar letter A-J
        if (firstChar < 'A' || firstChar > 'J') 
        {
            return false;
        }

        // checks number if 1-10
        try 
        {
            int convertNum = Integer.parseInt(number);
            if (convertNum < 1 || convertNum > 10)
            {
                // if number is not within the range
                return false;
            }
        } 
        catch (NumberFormatException e) 
        {
            // returns false if number is not even a number
            return false;
        }
        return true;
    }
}