import javafx.geometry.Point3D;

import java.util.ArrayList;

public class DivideAndConquer extends Algorithm {

    DivideAndConquer(int width, int height, int depth, ArrayList<Integer> values, char[] inputArray)
    {
        super(width,height,depth,values,inputArray);
        bestState = new char[depth][height][width];
        bestBoxOfIDs = new int[depth][height][width];
    }

    private char[][][] bestState;
    private int[][][] bestBoxOfIDs;

    private int currentMaxScore;
    private int bestScore;

    private int maxDepthOfRecursion;
    @Override
    public int[][][] solve() {

        for (int i = 1; i < 10; i++) {
            maxDepthOfRecursion = i + 1;
            long startTime = System.nanoTime();
            while (true) {
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
                bestScore += currentMaxScore;
                currentMaxScore = 0;

                //System.out.println(bestScore);
            }

            long endTime = System.nanoTime();

            System.out.println("Answer found in " + (endTime - startTime) / 1000000 + " milliseconds");
            System.out.println(bestScore);
            bestScore = 0;
            System.out.println("with depth " + (i + 1));
            for (int l = 0; l < depth; l++) {
                for (int j = 0; j < height; j++) {
                    for (int k = 0; k < width; k++) {
                        cargoSpace[l][j][k] = ' ';
                    }
                }
            }

        }
        return this.bestBoxOfIDs;
    }

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

    private void controlledBacktracking(int x, int y, int z, int depthOfRecursion) {
        if(depthOfRecursion == maxDepthOfRecursion)
        {
            return;
        }
        for (Pentomino currentPentomino : parcelRotations) {

            if (currentFits(x, y, z, currentPentomino)) {

                //calculatePosition();

                putBlockOnTheBoard(x, y, z, currentPentomino);

                ID++;

                score+= currentPentomino.getValue();

                if(currentMaxScore < score) {
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

                removeBlock(x,y,z,currentPentomino);
                //ID--;
                score-= currentPentomino.getValue();

            }
        }

    }
}
