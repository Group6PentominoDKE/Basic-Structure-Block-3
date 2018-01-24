import javafx.geometry.Point3D;
/**
 * Divide And Conquer class that extends the abstract class Algorithm
 * @author Zhecho Mitev
 */

import java.util.ArrayList;

public class DivideAndConquer extends Algorithm {

    /**
     * Constructor that sets the needed environment for the D&C and calls the constructor of the Algorithm class
     * @param width width of the container
     * @param height height of the container
     * @param depth depth of the container
     * @param values values that are going to be assigned to the parcels
     * @param inputArray character representation of the sorted parcels by value
     */
    DivideAndConquer(int width, int height, int depth, ArrayList<Integer> values, char[] inputArray)
    {
        super(width,height,depth,values,inputArray);

        bestState = new char[depth][height][width];
        bestBoxOfIDs = new int[depth][height][width];
        finalBoxOfIDs = new int[depth][height][width];
        maxDepthOfRecursion = 1;
        maxScore=0;
    }

    private static final int MAX_MILLISECONDS_FOR_EXECUTION = 1000;

    private char[][][] bestState;
    private int[][][] bestBoxOfIDs;

    private int[][][] finalBoxOfIDs;

    private int currentMaxScore;
    private int bestScoreForIteration;
    private int maxScore;

    private int maxDepthOfRecursion;//how many parcels would be considered at once
    @Override
    /**
     * the method is calling the recursive method controlledBacktracking, until a container is filled
     * The most outer while loop detects when the given time for execution is elapsed and returns the best solution so far
     */
    public int[][][] solve() {

            long startTime = System.nanoTime();

            long endTime = System.nanoTime();
            while (((endTime - startTime) / 1000000) < MAX_MILLISECONDS_FOR_EXECUTION) {//after every filled box the maxDepthOfRecursion is increased
                maxDepthOfRecursion++;
                while (true) {//filling one truck considering 'maxDepthOfRecursion' number of parcels

                    Point3D empty = findFirstEmptyCell();
                    if (empty == null) {
                        break;
                    }
                    int x = (int) empty.getX();
                    int y = (int) empty.getY();
                    int z = (int) empty.getZ();

                    controlledBacktracking(x, y, z, 0);

                    solutionFound = false;

                    if (bestState[x][y][z] == ' ') {
                        bestState[x][y][z] = 'i';
                        bestBoxOfIDs[x][y][z] = 0;
                    }
                    copyTheBestOrder();
                    score = 0;
                    bestScoreForIteration += currentMaxScore;
                    currentMaxScore = 0;

                    //System.out.println(bestScoreForIteration);
                }

                endTime = System.nanoTime();
                System.out.println("considering " + (maxDepthOfRecursion) + " parcels at once");
                System.out.println("Answer found in " + (endTime - startTime) / 1000000 + " milliseconds");
                System.out.println("Score: " + bestScoreForIteration);
                if(bestScoreForIteration > maxScore) { //
                    maxScore = bestScoreForIteration;
                    for (int k = 0; k < depth; k++) {
                        for (int j = 0; j < height; j++) {
                            for (int i = 0; i < width; i++) {
                                finalBoxOfIDs[k][j][i] = bestBoxOfIDs[k][j][i];
                            }
                        }
                    }
                }
                bestScoreForIteration = 0;
                System.out.println();
                for (int l = 0; l < depth; l++) {
                    for (int j = 0; j < height; j++) {
                        for (int k = 0; k < width; k++) {
                            cargoSpace[l][j][k] = ' ';
                        }
                    }
                }
            }
       // }
        score = maxScore;
        return this.finalBoxOfIDs;
    }

    /**
     * coping the values from the best representation of the ID and character boxes
     */
    private void copyTheBestOrder() {
        for (int k = 0; k < depth; k++) {
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    cargoSpace[k][j][i] = bestState[k][j][i];
                    boxOfIDs[k][j][i] = bestBoxOfIDs[k][j][i];
                }
            }
        }
    }

    private Point3D findFirstEmptyCell() {
        for (int k = 0; k < depth; k++) { //checking for free cell
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {

                    if (cargoSpace[k][j][i] == ' ') {
                        return new Point3D(k, j, i);
                    }
                }
            }
        }
        return null;
    }

    /**
     * The method iterates through every possible combination of parcels, but it goes no deeper than the maxDepthOfRecursion which is set.
     * @param x coordinate x to put a parcel
     * @param y coordinate y to put a parcel
     * @param z coordinate z to put a parcel
     * @param depthOfRecursion the current depth of the recursion
     */
    private void controlledBacktracking(int x, int y, int z, int depthOfRecursion) {
        if(depthOfRecursion == maxDepthOfRecursion)
        {
            return;
        }
        for (Parcel currentParcel : parcelRotations) {

            if (currentFits(x, y, z, currentParcel)) {

                ID++;
                putBlockOnTheBoard(x, y, z, currentParcel);

                double value = currentParcel.getValue();
                score+= value;
                if(currentMaxScore < score){
                    for (int k = 0; k < depth; k++) { //checking for free cell
                        for (int j = 0; j < height; j++) {
                            for (int i = 0; i < width; i++) {
                                bestState[k][j][i] = cargoSpace[k][j][i];
                                bestBoxOfIDs[k][j][i] = boxOfIDs[k][j][i];
                            }
                        }
                    }
                    currentMaxScore = score;
                    if(currentMaxScore >=maxDepthOfRecursion*5) {
                        solutionFound =true;
                        return;
                    }
                }

                boolean freeCellFound = false;

                for (int k = 0; k < depth; k++) { //checking for free cell
                    for (int j = 0; j < height; j++) {
                        for (int i = 0; i < width; i++) {

                            if (cargoSpace[k][j][i] == ' ') {

                                freeCellFound = true;
                                controlledBacktracking(k, j, i, depthOfRecursion+1);/// go into the recursion


                                if (solutionFound) //if the condition is true the program will go out of all recursions
                                {
                                    return;
                                }
                                break;/// when the method executes, the recursion has stopped and we should stop iterating the cargoSpace
                            }
                        }
                        if (freeCellFound) break;
                    }
                    if(freeCellFound) break;
                }

                removeBlock(x,y,z, currentParcel);

                score-= value;
            }
        }

    }
}
