import java.util.ArrayList;

public class Greedy extends Algorithm{

    public Greedy(int width, int height, int depth, ArrayList<Integer> values, char[] inputArray)
    {
        super(width,height,depth,values,inputArray);
    }

    @Override
    public int[][][] solve() {
        maximizeGreedy(0,0,0);
        findAndReplaceDoubleBs();

        return this.boxOfIDs;
    }

    private void findAndReplaceDoubleBs() {
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    int[] dimensions = searchCoordinatesOfB(i,j,k, boxOfIDs[i][j][k]);
                    if(dimensions != null)
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

    private void maximizeGreedy(int x, int y, int z) {
        for (Pentomino currentPentomino : parcelRotations) {

            if (currentFits(x, y, z, currentPentomino)) {

                ID++;
                putBlockOnTheBoard(x, y, z, currentPentomino);

                score+= currentPentomino.getValue();

                for (int k = 0; k < depth; k++) { //checking for free cell
                    for (int j = 0; j < height; j++) {
                        for (int i = 0; i < width; i++) {

                            if (cargoSpace[k][j][i] == ' ') {
                                maximizeGreedy(k, j, i);/// go into the recursion

                                if (solutionFound) //if the condition is true the program will go out of all recursions
                                {
                                    return;
                                }
                                else
                                {
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
}
