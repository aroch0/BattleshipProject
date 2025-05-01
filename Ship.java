/******************************************************************************
 * Ships class for initializing ship name and size
 * getter & setter methods
 * fixed helper method: initializeShips()
 */

public class Ship 
{
    private int size;
    private String name;

    public Ship(String name, int size) 
    {
        this.name = name;
        this.size = size;    

    }

    public Ship() {}

    public String getShipName() 
    {
        return name;
    }

    public int getShipSize() 
    {
        return size;
    } 

    public void setShipName(String shipName) 
    {
        name = shipName;
    }

    public void setShipSize(int shipSize) 
    {
        size = shipSize;
    }

    public Ship[] initializeShips() 
    {
        return new Ship[] 
        {
            new Ship("AircraftCarrier", 5),
            new Ship("Battleship", 4),
            new Ship("Cruiser", 3),
            new Ship("Submarine", 3),
            new Ship("Destroyer", 2)
        };
    }
}