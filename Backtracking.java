import java.util.ArrayList;

public class Backtracking extends Algorithm {

    private int emptyCells;

    @Override
    public int[][][] solve() {

        searchSolution(0,0,0);

        if(solutionFound) return this.boxOfIDs;
        else return null;
    }

    public Backtracking(int width, int height, int depth, ArrayList<Integer> values, char[] inputArray)
    {
        super(width,height,depth,values,inputArray);
    }

    private void searchSolution(int x, int y, int z) {

        for (Pentomino currentPentomino : parcelRotations) {

            boolean currentFits = currentFits(x, y, z, currentPentomino);

            if (currentFits) {

                ID++;
                putBlockOnTheBoard(x, y, z, currentPentomino);

                if (usingPents(currentPentomino.getType()) && impossibleCase()) {
                    removeBlock(x, y, z , currentPentomino);
                    continue;
                }
                score+= currentPentomino.getValue();

                boolean freeCellFound = false;


                for (int k = 0; k < depth; k++) { //checking for free cell
                    for (int j = 0; j < height; j++) {
                        for (int i = 0; i < width; i++) {
                            if (cargoSpace[k][j][i] == ' ') {

                                freeCellFound = true;
                                searchSolution(k, j, i);/// go into the recursion

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

                if (!freeCellFound) {
                    solutionFound = true;
                    return;

                }

                removeBlock(x, y,z, currentPentomino); //backtracking
                score -= currentPentomino.getValue();
            }

        }
    }

    /**
     * The method is recursive and counts all isolated empty cells in one region
     * @param x coordinate of x axis
     * @param y coordinate of y axis
     */
    private void countEmptyCells(int x, int y, int z) {

        if (y >= 0 && x < depth && x >= 0 && y < height && z < width && z >= 0 && cargoSpace[x][y][z] == ' '){
            cargoSpace[x][y][z] = 'a';
            emptyCells++;

        }
        else{

            return;
        }
        countEmptyCells(x,y-1, z);
        countEmptyCells(x+1,y, z);
        countEmptyCells(x,y+1, z);
        countEmptyCells(x-1,y, z);
        countEmptyCells(x,y, z+1);
        countEmptyCells(x,y, z-1);

    }

    /**
     * The method checks the most left cells and searches for an empty one. When found, it counts how many isolated cells are there
     *
     */
    private boolean impossibleCase() {
        for (int i = 0; i < width; i++) {
            for (int l = 0; l < height; l++) { //checking for free cell
                for (int j = 0; j < depth; j++) {
                    if (cargoSpace[j][l][i] == ' '){
                        emptyCells =0;
                        countEmptyCells(j, l,i);

                        for (int k = 0; k < height; k++) { //backtracking
                            for (int m = 0; m < width; m++) {
                                for (int n = 0; n < depth; n++) {
                                    if (cargoSpace[n][k][m] == 'a')
                                        cargoSpace[n][k][m] = ' ';
                                }

                            }
                        }

                        if(emptyCells % 5 != 0) {
                            return true;
                        }
                        return false;
                    }
                }
            }
        }
        return false;
    }

    private boolean usingPents(char c)
    {
        if(c == 'a' || c == 'b' || c =='c')
        {
            return false;
        }
        return true;
    }
}
