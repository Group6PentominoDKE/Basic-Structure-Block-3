import java.util.ArrayList;
/**
 * @author Zhecho Mitev
 * @author Ahmad Mohammad
 */
public class Backtracking extends Algorithm {

    /**
     * Constructor that calls the constructor of the Algorithm class
     * @param width width of the container
     * @param height height of the container
     * @param depth depth of the container
     * @param values 3 values that are going to be assigned to the parcels
     * @param inputArray character representation of the sorted parcels by value
     */
    public Backtracking(int width, int height, int depth, ArrayList<Integer> values, char[] inputArray) {
        super(width, height, depth, values, inputArray);
    }

    private int emptyCells;
    private int[][][] smallBox;

    /**
     * the method is first trying to fill small boxes and with them to fill the bigger box
     * @return the boxOfIDs which it needed for the displaying of the final container
     */
    @Override
    public int[][][] solve() {

        ArrayList<Integer> dimensionsOfSmallBoxes = creatingMultiples(depth,height, width);
        for (int i = 0; i < dimensionsOfSmallBoxes.size(); i+=3) {
            this.depth = dimensionsOfSmallBoxes.get(i);
            this.height = dimensionsOfSmallBoxes.get(i+1);
            this.width = dimensionsOfSmallBoxes.get(i + 2);
            smallBox = new int[depth][height][width];
            searchSolution(0,0,0);
            int scoreOfSmallBox = score;
            score=0;
            System.out.println(i/3);
            if (solutionFound)
            {
                assignValuesOfSmallBox();
                int size = dimensionsOfSmallBoxes.size();
                depth = dimensionsOfSmallBoxes.get(size-3);
                height = dimensionsOfSmallBoxes.get(size-2);
                width = dimensionsOfSmallBoxes.get(size-1);
                for (int j = 0; j < depth; j+=smallBox.length) {
                    for (int k = 0; k < height; k+=smallBox[0].length) {
                        for (int l = 0; l < width; l+=smallBox[0][0].length) {
                            putSmallBoxAt(j,k,l);
                            score+=scoreOfSmallBox;
                        }
                    }
                }
                return this.boxOfIDs;
            }
        }

        //searchSolution(0,0,0);
        //if(solutionFound) return boxOfIDs;

        return null;
    }

    /**
     * The boxOfIDs is assigned with the values of the small box for specific coordinates
     * @param j coordinate x of boxOfIDs
     * @param k coordinate y of boxOfIDs
     * @param l coordinate z of boxOfIDs
     */
    private void putSmallBoxAt(int j, int k, int l) {
        int depthOfSmall = smallBox.length;
        int heightOfSmall = smallBox[0].length;
        int widthOfSmall = smallBox[0][0].length;
        for (int a = 0; a < depthOfSmall; a++) {
            for (int b = 0; b < heightOfSmall; b++) {
                for (int c = 0; c < widthOfSmall; c++) {

                    boxOfIDs[a+j][b+k][c+l] = smallBox[a][b][c];
                    smallBox[a][b][c]+= ID;
                }
            }
        }
    }

    /**
     * Takes the big container and assigns its IDs to the small box
     */
    private void assignValuesOfSmallBox() {
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    smallBox[i][j][k] = boxOfIDs[i][j][k];
                }
            }
        }
    }

    /**
     * The main recursive method that iterates through all combinations to find a if the container can be filled
     * @param x coordinte x to put the currentPentomino
     * @param y coordinte y to put the currentPentomino
     * @param z coordinte z to put the currentPentomino
     */
    private void searchSolution(int x, int y, int z) {

        for (Parcel currentParcel : parcelRotations) {

            boolean currentFits = currentFits(x, y, z, currentParcel);

            if (currentFits) {

                ID++;
                putBlockOnTheBoard(x, y, z, currentParcel);

                if (usingPents(currentParcel.getType()) && impossibleCase()) {
                    removeBlock(x, y, z , currentParcel);
                    continue;
                }
                score += currentParcel.getValue();

                boolean freeCellFound = false;


                for (int k = x; k < depth; k++) { //checking for free cell
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
                    if (freeCellFound) break;
                }

                if (!freeCellFound) {
                    solutionFound = true;
                    return;

                }

                removeBlock(x, y, z, currentParcel); //backtracking
                score -= currentParcel.getValue();
            }

        }
    }

    /**
     * The method is recursive and counts all isolated empty cells in one region
     *
     * @param x coordinate of x axis
     * @param y coordinate of y axis
     */
    private void countEmptyCells(int x, int y, int z) {

        if (y >= 0 && x < depth && x >= 0 && y < height && z < width && z >= 0 && cargoSpace[x][y][z] == ' ') {
            cargoSpace[x][y][z] = 'a';
            emptyCells++;

        } else {

            return;
        }
        countEmptyCells(x, y - 1, z);
        countEmptyCells(x + 1, y, z);
        countEmptyCells(x, y + 1, z);
        countEmptyCells(x - 1, y, z);
        countEmptyCells(x, y, z + 1);
        countEmptyCells(x, y, z - 1);

    }

    /**
     * The method checks the most left cells and searches for an empty one.
     * When found, it counts how many isolated cells are there
     * @return true if the number of empty cells is divisible by 5, false if not
     */
    private boolean impossibleCase() {
        for (int i = 0; i < width; i++) {
            for (int l = 0; l < height; l++) { //checking for free cell
                for (int j = 0; j < depth; j++) {
                    if (cargoSpace[j][l][i] == ' ') {
                        emptyCells = 0;
                        countEmptyCells(j, l, i);

                        for (int k = 0; k < height; k++) {
                            for (int m = 0; m < width; m++) {
                                for (int n = 0; n < depth; n++) {
                                    if (cargoSpace[n][k][m] == 'a')
                                        cargoSpace[n][k][m] = ' ';
                                }

                            }
                        }

                        if (emptyCells % 5 != 0) {
                            return true;
                        }
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * The method takes the dimensions of the big container
     * @param a the depth of the container
     * @param b the height of the container
     * @param c the width of the container
     * @return the dimensions of all small boxes that can be scaled to the big container
     */
    private ArrayList<Integer> creatingMultiples(int a, int b, int c) {

        ArrayList<Integer> Values = new ArrayList<Integer>();
        ArrayList<Integer> Values2 = new ArrayList<Integer>();
        ArrayList<Integer> Values3 = new ArrayList<Integer>();
        ArrayList<Integer> Final = new ArrayList<Integer>();

        for (int h = 1; h <= a; h++) {
            if (a % h == 0) {
                Values.add(h);
            }
        }
        for (int i = 1; i <= b; i++) {
            if (b % i == 0) {
                Values2.add(i);
            }
        }
        for (int j = 1; j <= c; j++) {
            if (c % j == 0) {
                Values3.add(j);
            }
        }

        for (Integer Value : Values)
            for (Integer aValues2 : Values2)
                for (Integer aValues3 : Values3) {
                    Final.add(Value);
                    Final.add(aValues2);
                    Final.add(aValues3);
                }

        return Final;
    }

    /**
     * The method is needed to indicate when to use the optimization 'impossible cases'
     * @param c the first character of the inputArray
     * @return true if the array consist of TPL and false if it's ABC
     */
    private boolean usingPents(char c)
    {
        if(c == 'a' || c == 'b' || c =='c')
        {
            return false;
        }
        return true;
    }
}
