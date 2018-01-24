import java.util.ArrayList;

/**
 * Simple first-fit greedy algorithm using a optimization working for ABC parcels
 * @author Zhecho Mitev
 * @author Dimitar Popov
 */

public class Greedy extends Algorithm{
    /**
     * Constructor that calls the constructor of the Algorithm class
     * @param width width of the container
     * @param height height of the container
     * @param depth depth of the container
     * @param values values that are going to be assigned to the parcels
     * @param inputArray character representation of the sorted parcels by value
     */
    public Greedy(int width, int height, int depth, ArrayList<Integer> values, char[] inputArray)
    {
        super(width,height,depth,values,inputArray);
    }

    /**
     * calls the greedy method and after that replaces Bs with As if applicable
     * @return box of IDs needed for its display
     */
    @Override
    public int[][][] solve() {
        maximizeGreedy(0,0,0);
        if(values.get(0)*3 > values.get(1)*2 && !usingPents(parcelRotations[0].getType()))//if 3 A parcels worth more than 2 B
            findAndReplaceDoubleBs();

        return this.boxOfIDs;
    }

    /**
     * simple recursive method which places the first possible parcel to a position, if no parcel fits the current cell is left empty
     * @param x coordinte x to put the currentParcel
     * @param y coordinte y to put the currentParcel
     * @param z coordinte z to put the currentParcel
     */
    private void maximizeGreedy(int x, int y, int z) {
        for (Parcel currentParcel : parcelRotations) {

            if (currentFits(x, y, z, currentParcel)) {

                ID++;
                putBlockOnTheBoard(x, y, z, currentParcel);

                score+= currentParcel.getValue();

                for (int k = 0; k < depth; k++) { //checking for free cell
                    for (int j = 0; j < height; j++) {
                        for (int i = 0; i < width; i++) {

                            if (cargoSpace[k][j][i] == ' ') {
                                maximizeGreedy(k, j, i);/// go into the recursion

                                if (solutionFound){ //if the condition is true the program will go out of all recursions{
                                    return;
                                }
                                else {
                                    cargoSpace[k][j][i] = 'i';
                                }

                            }
                        }
                    }
                }

                solutionFound = true;///all cells are checked and nothing more can be placed
                return;

            }
        }
    }

    /**
     * finds and replaces 2 Bs for 3 As
     */
    private void findAndReplaceDoubleBs() {
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    int[] dimensions = searchCoordinatesOfB(i,j,k, boxOfIDs[i][j][k]);
                    if(dimensions != null)//the Bs to replace are found
                    {
                        
                        if(dimensions[3] == 0 && i+5<depth && checkForB(i + 3,j,k,dimensions))
                        {
                            score++;
                            for (int l = 0; l < 6; l++) {
                                if(l%2==0) ID++;
                                for (int m = 0; m < dimensions[1]; m++) {
                                    for (int n = 0; n < dimensions[2]; n++) {
                                        boxOfIDs[l+i][m+j][n+k] = ID;
                                        cargoSpace[l+i][m+j][n+k] = 'a';
                                    }
                                }
                            }
                        }
                        else if (dimensions[3] == 1 && j+5<height && checkForB(i,j+3,k,dimensions))
                        {
                            score++;
                            for (int l = 0; l < 6; l++) {
                                if(l%2==0) ID++;
                                for (int m = 0; m < dimensions[0]; m++) {
                                    for (int n = 0; n < dimensions[2]; n++) {
                                        boxOfIDs[m+i][l+j][n+k] = ID;
                                        cargoSpace[m+i][l+j][n+k] = 'a';
                                    }
                                }
                            }
                        }
                        else if(dimensions[3] == 2 && k+5<width && checkForB(i,j,k+3,dimensions))
                        {
                            score++;
                            for (int l = 0; l < 6; l++) {
                                if(l%2==0) ID++;
                                for (int m = 0; m < dimensions[0]; m++) {
                                    for (int n = 0; n < dimensions[1]; n++) {
                                        boxOfIDs[m+i][n+j][l+k] = ID;
                                        cargoSpace[m+i][n+j][l+k] = 'a';
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * searches for specific B at certain coordinates
     * @param i coordinate x to search for B  
     * @param j coordinate y to search for B
     * @param k coordinate z to search for B
     * @param dimensions the width,height and length of the B
     * @return true if the B is found 
     */
    private boolean checkForB(int i, int j, int k, int[] dimensions) {

        for (int l = 0; l < dimensions[0]; l++) {
            for (int m = 0; m < dimensions[1]; m++) {
                for (int n = 0; n < dimensions[2]; n++) {
                    if(cargoSpace[i+l][j+m][k+n] != 'b')
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private int[] searchCoordinatesOfB(int i, int j, int k, int ID) {
        int currentDepth = 0;
        int currentHeight = 0;
        int currentWidth = 0;
        for(int a=i; a<boxOfIDs.length && boxOfIDs[a][j][k] == ID; a++) {
            currentDepth++;
        }
        for(int b=j; b<boxOfIDs[0].length && boxOfIDs[i][b][k] == ID; b++) {
            currentHeight++;
        }
        for(int c=k; c<boxOfIDs[0][0].length && boxOfIDs[i][j][c] == ID; c++) {
            currentWidth++;
        }

        if(currentDepth*currentHeight*currentWidth == 24)
        {
            int dimensionToIncrease;
            if(currentDepth == 3) dimensionToIncrease =0;
            else if(currentHeight == 3) dimensionToIncrease = 1;
            else dimensionToIncrease =2 ;
            return new int[] {currentDepth,currentHeight,currentWidth,dimensionToIncrease};
        }
        return null;
    }


    /**
     * The method is needed to indicate when to use the optimization 'impossible cases'
     * @param c the first character of the inputArray
     * @return true if the array consist of TPL and false if it's ABC
     */
    private boolean usingPents(char c)
    {
        return c != 'a' && c != 'b' && c != 'c';
    }
}
