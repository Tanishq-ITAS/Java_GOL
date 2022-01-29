import java.awt.*;          // Using AWT's Graphics and Color
import java.net.URL;
import javax.swing.*;       // Using Swing's components and container
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Simulation of a cellular biological experiment.
 * For more info see https://en.wikipedia.org/wiki/Conway's_Game_of_Life
 * 
 * See http://www.bitstorm.org/gameoflife/ for a demonstration 
 * of a fully implemented version, or JavaScript version I showed in class:
 * http://pmav.eu/stuff/javascript-game-of-life-v3.1.1/
 * 
 * Copyright Mark Dutchuk - NOT for distribution outside 
 * the ITAS classroom.
 * 
 * NOTE: Set DEBUGMODE to false for AWT/Swing Graphical interfae
 * This is less useful for debugging the program though as when
 * DEBUGMODE is true, it draws the game map and shows you how 
 * many neightbours each cell has.
 * 
 * @author Mark Dutchuk 2016, updates by David Croft, Nov. 2020
 */
public class Game extends JFrame {

    // Declare some constants to be used throughout the program
    private Random random = new Random(); // for picking images in random

    private int SIZE_X;                         // Size of array columns
    private int SIZE_Y;                         // Size of array rows

    private final boolean T = true;             // short-hand booleans
    private final boolean F = false;
    
    // Change these once program is developed
    private final int MAX_GENERATIONS = 100;    // number of loops to perform
    private final boolean DEBUGMODE = true;    // set to false once development / testing is done

    // Images
    private String liveCellImagePathname = "images/live.gif";
    private String deaCellImagePathname = "images/dead.gif";
    private Image liveImage;
    private Image deadImage;
    private static final int IMAGE_SIZE = 25;
    private static final int PADDING = 3;

    // Declare two arrays: 
    // 'current' represents the current state if the cell matrix
    // 'next' represents the results of the calculations for deciding
    // whether a cell lives, dies, or is born.  
    private boolean[][] current; // this is the current map we will be checking
    private boolean[][] next;    // this is the future state we will store true/false
                                 // updated values for the next generation

    // Set drawing canvas related fields
    private DrawCanvas canvas;
    private int canvasSize;    
    private int cellSize;
    
    //count the number of crash
    private int crashCounter;

    /**
     * Main entry point into application. Requires no arguments at runtime.
     */
    public static void main(String[] args)  {
        Game g = new Game();
        g.setup();
        g.run();
    }

    /**
     * Sets the initial conditions of the game.
     * DON'T CHANGE THIS ARRAY! - Doing so will cause
     * the unit tests to fail. But you could create 
     * another array and assign it (see below) if you want
     * to experiment. As long as its easy to flip back 
     * to this data for testing.
     */
    public void setup()  {

        boolean[][] data = {
                {F,T,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F},
                {T,F,T,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T},
                {F,F,F,T,F,F,F,F,F,F,F,F,F,F,F,F,F,T,T,T,F,F,F},
                {F,T,T,T,F,F,F,F,F,F,F,F,F,F,F,F,F,T,T,T,F,F,F},
                {F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,F,F,F,F,F},
                {F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F},
                {F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,T,T,F,F,F},
                {F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,T,T,F,F,F},
                {F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,T,F,F,F,F},
                {F,F,F,F,F,F,F,F,T,T,F,F,F,F,F,F,F,F,T,F,F,F,F},
                {F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,T,T,F,F,F},
                {F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,T,T,F,F,F},
                {F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,T,T,F,F,F},
                {F,F,F,F,F,T,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F},
                {F,F,F,T,T,F,F,F,F,F,F,F,F,T,T,T,F,F,F,F,F,F,F},
                {F,F,F,F,F,T,F,F,F,F,F,F,F,F,F,F,T,F,F,F,F,F,F},
                {F,F,F,F,F,F,T,F,F,F,F,F,F,T,F,T,T,F,F,F,F,F,F},
                {F,F,F,T,F,T,F,T,F,F,F,F,F,F,T,T,F,T,F,F,F,F,F},
                {F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,F,T,F,F,F,F,F},
                {F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,F,F,F,F,F,F,F},
                {F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F},
                {F,T,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,T},
                {F,T,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,T,F}
            };

        // Assign the data to the 'current' array    
        current = data;

        // Determine the dimensions of the 'current' array
        SIZE_X = current[0].length; // think of this as left-right columns
        SIZE_Y = current.length;    // think of this as top-down rows

        // create a 'next' array with the same datatype and dimensions as the current array
        // Note we have SIZE_Y rows with SIZE_X columns per row
        next = new boolean[SIZE_Y][SIZE_X];

        // Set the display size of each cell 
        cellSize = IMAGE_SIZE + 2 * PADDING;

        // set the size of the 2D matrix that the cells are displayed in
        canvasSize = cellSize * SIZE_X;
    }

    /**
     * Loops through the various program functions MAX_GENERATIONS number of times.  
     * Each loop represents one generation of a biological matrix, where individual
     * cells live, die, or are born into the matrix.
     */
    public void run()  {

        int numGeneration = 0; //number of generations in simulation

        //milliseconds 
        //measured in milliseconds, between
        //the current time and midnight, January 1, 1970 UTC
        long startTime = System.currentTimeMillis();


        while ( numGeneration < MAX_GENERATIONS ) {
            
            // clear the blueJ screen
            clearScreen();
            
            // While debugging, we want text output, but once all the 
            // development and testing is done, switch to graphical mode
            if (DEBUGMODE) {     
                printArray(current); // Text output
            } else {
                drawImage(current);  // Graphical output
            }

            //report the # of generations
            System.out.println("\nGeneration #" + numGeneration);
            
            // Process the current world to determine who lives/dies
            calcNextGen();
            
            // Once all calculations are made, the 'next' array must be 
            // copied to the 'current'array, and the 'next' must 
            // be re-initialized to 'all falses'
            copyArray();
            
            // Slow the whole thing down for human consumption
            try {
                Thread.sleep(350);  // make this value bigger if you want it to run slower
            } catch (InterruptedException e) {
                //
            }

            // Increment the loop counter to do it all again ..
            numGeneration++;
        }

        //milliseconds 
        //measured in milliseconds, between
        //the current time and midnight, January 1, 1970 UTC
        long endTime = System.currentTimeMillis();

        //total time to run the simulation
        System.out.printf("The elapsed time: %d milliseconds\n", (endTime - startTime));
    }

    /**
     * Print the celular matrix to the console. For every cell in the matrix, if 
     * the cell is alive, print the number of neighbors that cell has. If the cell
     * is dead, print an empty space.
     */
    public void printArray(boolean[][] a) {

        // Loop through the rows and columns dimensions of the arrays
        for (int row = 0; row < (SIZE_Y); row++) {
            for (int col = 0; col < (SIZE_X); col++) {   
                
                // Print a row of the array.  
                // If there is a live cell at this location. Remember the each 
                // cell holds a boolean value, so no need to say 'if (a[row][col] == true)'
                if (a[row][col]) {
                    // print the number of neigbours of this cell location
                    System.out.print(" " + calcNumNeighbors(row, col) + " ");
                } else {
                    // just print a dot to signify an empty cell
                    System.out.print(" . ");
                }
            }
            // add a linefeed after each row of output
            System.out.println();
        }

    }

    /**
     * Given a cell at a particular x and y coordinate, calculate the number 
     * of 'live' cells directly next to it.  Should return a number between
     * 0 and 8.  Must not count the cell at position x,y, since that is the 
     * cell itself and not a neighbor. Must be careful when calculating 
     * neighbours around the edges of the matrix to avoid 
     * index-out-of-bounds errors.
     *
     * @param x int the cell location's X coordinate
     * @param y int the cell location's Y coordinate
     * @return int - the number of neighbors surrounding this cell location
     */
    public int calcNumNeighbors(int x, int y) {

    	//number of neighbors
        int numNeighbors = 0;
        
        try{
        
          // Insert your code here
          // read neighbors of (x, y) cell
          // 9 cells arround it (including itself (x,y))
          for (int i = -1; i <= 1; i++){
              for (int j = -1; j <= 1; j++){            	
                //check itself
                if (i == 0 && j == 0) {
                  //ignore itself
                }else if (y + i >= 0 && y + i < SIZE_Y 
                    && x + j >= 0 && x + j < SIZE_X)
                    { //valid cell, check to avoid index-out-of-bounds errors.
                      if (current[x + j][y + i]){ //live cell?
                          numNeighbors++;
                      }
                  }
              }
          }//end for
        } catch (ArrayIndexOutOfBoundsException e) {  
          crashCounter++;  
          System.out.println("\nWARNING: Check at bottom right was out of bounds,Crash#: " +  
            crashCounter);  
          System.out.println("\tValue of x is: " + x);  
          System.out.println("\tValue of y is: " + y);  
          System.out.println("\tMax Index for SIZE_X and SIZE_Y is: " + (SIZE_X - 1));  
          long time = System.currentTimeMillis();  
          System.out.println("\tThe current time in milliseconds is: " + time);
        }
        
        return numNeighbors;
    }

    /** 
     * For every element in the current array, calculate whether the cell lives on 
     * into the next generation:
     * <p> - Any live cell with fewer than two live neighbours dies, as if caused by under-population.
     * <p> - Any live cell with two or three live neighbours lives on to the next generation.
     * <p> - Any live cell with more than three live neighbours dies, as if by overcrowding.
     * <p> - Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
     */
     //calNextGen method is used to calculate next gen rule of life
    public void calcNextGen() {

        // Insert your code here - you need to update the 2D next array 
        // with boolean true or false values based upon the rules above

        int numNeighbors; //count of neighbors

        //for each row
        for (int y = 0; y < SIZE_Y; y++){

            //for each column
            for (int x = 0; x < SIZE_X; x++){

                //calculate the number of neighbors (live cells) of (x,y)
            	numNeighbors = calcNumNeighbors(x, y);

                //live cell case
                if (current[y][x]){ //is live cell

                  //Any live cell with fewer than two live neighbours dies, as if caused by under-population
                  if (numNeighbors < 2){
                    next[y][x] = false;
                  }else if (numNeighbors == 2 || numNeighbors == 3)
                  {//Any live cell with two or three live neighbours lives on to the next generation.
                    next[y][x] = true;
                  }else
                  {//Any live cell with more than three live neighbours dies, as if by overcrowding.
                    next[y][x] = false;
                  }
                }else{//dead cell case
                  //Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
                  if (numNeighbors == 3){
                    next[y][x] = true;
                  }else{
                    next[y][x] = false;
                  }
                }                
            }
        }
    }

    /**
     * Copy the next array to the current array
     */
    public void copyArray() {
        // Insert your code here
        // you need to update the current array with the values from the next array

         //for each row
        for (int y = 0; y < SIZE_Y; y++){

            //for each column
            for (int x = 0; x < SIZE_X; x++){

              current[y][x] = next[y][x];

            }
        }
    }
    
    /**
     * Private class that models the cells 2 dimensional world
     * DO NOT MODIFY
     */
    private class DrawCanvas extends JPanel {

        boolean[][] cellWorld;

        public DrawCanvas(boolean[][] w) {
            cellWorld = w;
        }

        @Override
        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            setBackground(Color.WHITE);

            for (int row = 0; row < SIZE_Y; row++) {
                for (int col = 0; col < SIZE_X; col++) {                  
                    Image img = cellWorld[row][col] ? liveImage : deadImage;
                    g.drawImage(
                        img,
                        cellSize * col + PADDING, 
                        cellSize * row + PADDING,
                        IMAGE_SIZE, 
                        IMAGE_SIZE, 
                        null
                    );
                }
            }
        }
    }

    /**
     * Outputs the 2D world created above as a graphics image
     * DO NOT MODIFY
     */
    public void drawImage(boolean[][] w) {

        // Prepare the ImageIcon and Image objects for drawImage()
        ImageIcon iconCross = null;
        ImageIcon iconNought = null;

        URL imgURL = getClass().getClassLoader().getResource(liveCellImagePathname);
        iconCross = new ImageIcon(imgURL);
        liveImage = iconCross.getImage();

        imgURL = getClass().getClassLoader().getResource(deaCellImagePathname);
        iconNought = new ImageIcon(imgURL);
        deadImage = iconNought.getImage();

        canvas = new DrawCanvas(w);
        canvas.setPreferredSize(new Dimension(canvasSize, canvasSize));
        setContentPane(canvas);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setTitle("ITAS Game Of Life Cell Display");
        setVisible(true);
    }
    
    /* 
     * --------  Some helper functions  --------
     */
    
    /**
     * Clear the BlueJ console screen and put the cursor in the top left position
     */
    private void clearScreen() {
        System.out.print('\u000C');
    }

    /**
     * Used by the Unit testing code 
     */
    public int getSizeX() {
        return SIZE_X;
    }

    /**
     * Used by the Unit testing code 
     */
    public int getSizeY() {
        return SIZE_Y;
    }

}
