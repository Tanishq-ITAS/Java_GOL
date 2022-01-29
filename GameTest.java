import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class GameTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class GameTest
{
    private Game g;
    private int maxX, maxY;
    
    /**
     * Default constructor for test class GameTest
     */
    public GameTest() {
        g = new Game();
        g.setup();
        maxX = g.getSizeX() - 1; // Max index is one less than array size
        maxY = g.getSizeY() - 1;
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        // nothing to do
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown() {
        // nothing to do
    }
    
    /**
     * Verify that the Game.calcNumNeighbors() method 
     * returns the correct number of neighbors at 
     * carious locations in the matrix.
     */
    @Test
    public void testUpperLeftCorner() {
        assertEquals(g.calcNumNeighbors(0,0), 2);
    }
    
    /**
     * Verify that the Game.calcNumNeighbors() method 
     * returns the correct number of neighbors at 
     * carious locations in the matrix.
     */
    @Test
    public void testLowerLeftCorner() {
        assertEquals(g.calcNumNeighbors(maxX,0), 2);
    }
    
    /**
     * Verify that the Game.calcNumNeighbors() method 
     * returns the correct number of neighbors at 
     * carious locations in the matrix.
     */
    @Test
    public void testUpperRightCorner() {
        assertEquals(g.calcNumNeighbors(0,maxY), 1);
    }
    
    /**
     * Verify that the Game.calcNumNeighbors() method 
     * returns the correct number of neighbors at 
     * carious locations in the matrix.
     */
    @Test
    public void testLowerRight() {
        assertEquals(g.calcNumNeighbors(maxX,maxY), 3);
    }

    /**
     * Verify that the Game.calcNumNeighbors() method 
     * returns the correct number of neighbors at 
     * carious locations in the matrix.
     */
    @Test
    public void test0Neighbors() {
        assertEquals(g.calcNumNeighbors(4,5), 0);
    }
    /**
     * Verify that the Game.calcNumNeighbors() method 
     * returns the correct number of neighbors at 
     * carious locations in the matrix.
     */
    @Test
    public void test1Neighbors() {
        assertEquals(g.calcNumNeighbors(4,4), 1);
    }
        /**
     * Verify that the Game.calcNumNeighbors() method 
     * returns the correct number of neighbors at 
     * carious locations in the matrix.
     */
    @Test
    public void test2Neighbors() {
        assertEquals(g.calcNumNeighbors(4,3), 2);
    }
        /**
     * Verify that the Game.calcNumNeighbors() method 
     * returns the correct number of neighbors at 
     * carious locations in the matrix.
     */
    @Test
    public void test3Neighbors() {
        assertEquals(g.calcNumNeighbors(4,2), 3);
    }
        /**
     * Verify that the Game.calcNumNeighbors() method 
     * returns the correct number of neighbors at 
     * carious locations in the matrix.
     */
    @Test
    public void test4Neighbors() {
        assertEquals(g.calcNumNeighbors(2,1), 4);
    }
        /**
     * Verify that the Game.calcNumNeighbors() method 
     * returns the correct number of neighbors at 
     * carious locations in the matrix.
     */
    @Test
    public void test5Neighbors() {
        assertEquals(g.calcNumNeighbors(2,2), 5);
    }
        /**
     * Verify that the Game.calcNumNeighbors() method 
     * returns the correct number of neighbors at 
     * carious locations in the matrix.
     */
    @Test
    public void test6Neighbors() {
        assertEquals(g.calcNumNeighbors(3,18), 6);
    }
        /**
     * Verify that the Game.calcNumNeighbors() method 
     * returns the correct number of neighbors at 
     * carious locations in the matrix.
     */
    @Test
    public void test7Neighbors() {
        assertEquals(g.calcNumNeighbors(7,18), 7);
    }
        /**
     * Verify that the Game.calcNumNeighbors() method 
     * returns the correct number of neighbors at 
     * carious locations in the matrix.
     */
    @Test
    public void test8Neighbors() {
        assertEquals(g.calcNumNeighbors(11,18), 8);
    }    
}
